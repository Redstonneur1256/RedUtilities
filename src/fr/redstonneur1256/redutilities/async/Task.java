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
 */
public class Task<T> {

    private final Object lock;
    private boolean have;
    private List<Consumer<T>> listeners;
    private T item;

    public Task() {
        lock = new Object();
        listeners = new ArrayList<>();
    }

    /**
     * Private constructor used for maps
     */
    private <O> Task(Task<O> oldTask, Function<O, T> mapper) {
        this();
        if(oldTask.have) {
            complete(mapper.apply(oldTask.item));
        }else {
            oldTask.onComplete(item -> complete(mapper.apply(item)));
        }
    }

    public static <T> Task<T> fromFuture(CompletableFuture<T> future) {
        Task<T> task = new Task<>();
        future.thenAccept(task::complete);
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
            this.have = true;
            this.lock.notifyAll();
            for(Consumer<T> listener : listeners) {
                listener.accept(item);
            }
            listeners = null; // Forget them
        }
    }

    public <U> Task<U> map(Function<T, U> function) {
        return new Task<>(this, function);
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
            return item;
        }
    }

    public T getNow(T other) {
        return getNow(() -> other);
    }

    public T getNow(Provider<T> orElse) {
        synchronized(lock) {
            return have ? item : orElse.get();
        }
    }

    /**
     * Add a listener to the Task
     * Note that if the task has already been completed the listener will be called directly
     */
    public void onComplete(Consumer<T> action) {
        synchronized(lock) {
            if(have) {
                action.accept(item);
            }else {
                listeners.add(action);
            }
        }
    }

    public void onCompleteAsync(Consumer<T> action) {
        onComplete(item -> Threads.daemon(() -> action.accept(item)));
    }

    public CompletableFuture<T> toFuture() {
        CompletableFuture<T> future = new CompletableFuture<>();
        onComplete(future::complete);
        return future;
    }

}
