package fr.redstonneur1256.redutilities.async;

import fr.redstonneur1256.redutilities.function.Functions;
import fr.redstonneur1256.redutilities.function.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Simple class like CompletableFuture
 * Probably better or less better IDK have fun using it
 */
public class Task<T> {

    private final Object lock;
    private boolean have;
    private List<Consumer<T>> listeners;
    private List<Consumer<Throwable>> failListeners;
    private T item;
    private Throwable exception;

    public Task() {
        lock = new Object();
        listeners = new ArrayList<>();
        failListeners = new ArrayList<>();
    }

    /**
     * Private constructor used for maps
     */
    private <O> Task(Task<O> oldTask, Function<O, T> mapper) {
        this();
        if(oldTask.isComplete()) {
            if(oldTask.exception != null) {
                fail(oldTask.exception);
            }else {
                complete(mapper.apply(oldTask.item));
            }
        }else {
            oldTask.onComplete(item -> complete(mapper.apply(item)));
            oldTask.onFail(this::fail);
        }
    }

    public static <T> Task<T> fromFuture(CompletableFuture<T> future) {
        Task<T> task = new Task<>();
        future.thenAccept(task::complete);
        future.exceptionally(exception -> {
            task.fail(exception);
            return null;
        });
        return task;
    }

    public boolean isCompleted() {
        synchronized(lock) {
            return have;
        }
    }

    public void complete(T item) {
        synchronized(lock) {
            if(have) {
                throw new IllegalStateException("The Task have already been completed");
            }
            this.item = item;
            completed();
        }
    }

    public void fail(Throwable exception) {
        synchronized(lock) {
            if(have) {
                throw new IllegalStateException("The Task have already been completed");
            }
            this.exception = exception;
            this.completed();
        }
    }

    public <U> Task<U> map(Function<T, U> function) {
        return new Task<>(this, function);
    }

    public boolean isComplete() {
        synchronized(lock) {
            return have;
        }
    }

    public void waitComplete() {
        waitComplete(0);
    }

    public void waitComplete(long timeout) {
        synchronized(lock) {
            if(!have) {
                Functions.runtime(() -> lock.wait(timeout));
            }
        }
    }

    public T get() {
        return get(0);
    }

    public T get(long timeout) {
        synchronized(lock) {
            if(!have) {
                Functions.runtime(() -> lock.wait(timeout));
            }
            if(!have) {
                return null;
            }

            if(exception != null) {
                throw new RuntimeException(exception);
            }
            return item;
        }
    }

    public T getNow(T orElse) {
        return getNow(() -> orElse);
    }

    public T getNow(Provider<T> orElse) {
        synchronized(lock) {
            if(exception != null) {
                throw new RuntimeException(exception);
            }
            return have ? item : orElse.get();
        }
    }

    public Throwable getException() {
        return getException(0);
    }

    public Throwable getException(long timeout) {
        synchronized(lock) {
            if(!have) {
                Functions.runtime(() -> lock.wait(timeout));
            }
            return exception;
        }
    }

    /**
     * Add a listener to the Task
     * Note that if the task has already been completed the listener will be called directly
     */
    public void onComplete(Consumer<T> action) {
        synchronized(lock) {
            if(have) {
                if(exception == null) {
                    action.accept(item);
                }
            }else {
                listeners.add(action);
            }
        }
    }

    public void onFail(Consumer<Throwable> action) {
        synchronized(lock) {
            if(have) {
                if(exception != null) {
                    action.accept(exception);
                }
            }else {
                failListeners.add(action);
            }
        }
    }

    public void onCompleteAsync(Consumer<T> action) {
        onComplete(item -> Threads.daemon(() -> action.accept(item)));
    }

    public CompletableFuture<T> toFuture() {
        CompletableFuture<T> future = new CompletableFuture<>();
        onComplete(future::complete);
        onFail(future::completeExceptionally);
        return future;
    }

    private void completed() {
        have = true;
        lock.notifyAll();

        if(exception == null) {
            for(Consumer<T> listener : listeners) {
                listener.accept(item);
            }
        }else {
            for(Consumer<Throwable> failListener : failListeners) {
                failListener.accept(exception);
            }
        }

        listeners = null;
        failListeners = null;
    }

}
