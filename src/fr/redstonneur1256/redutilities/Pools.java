package fr.redstonneur1256.redutilities;

import java.util.*;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class Pools {

    private static final Map<Class<?>, Pool<?>> pools;

    static {
        pools = new HashMap<>();
    }

    public static <T> Pool<T> getPool(Class<T> type, Supplier<T> provider) {
        Pool<T> pool = (Pool<T>) pools.get(type);
        if(pool == null) {
            Objects.requireNonNull(provider, "null provider while pool not created");
            pool = new Pool<>(provider);
            pools.put(type, pool);
        }
        return pool;
    }

    public static <T> T get(Class<T> type, Supplier<T> provider) {
        return getPool(type, provider).get();
    }

    public static <T> void release(T object) {
        if(object == null)
            return;

        Pool<T> pool = (Pool<T>) pools.get(object.getClass());
        if(pool == null)
            return;

        pool.release(object);
    }

    public static <T> void delete(Class<T> type) {
        Pool<?> pool = pools.remove(type);
        if(pool != null) {
            pool.freeObjects.clear();
            pool.deleted = true;
        }
    }

    public interface Poolable {

        void reset();

    }

    public static class Pool<T> {

        private Supplier<T> provider;
        private int limit;
        private Queue<T> freeObjects;
        private boolean deleted;

        public Pool(Supplier<T> provider) {
            this(provider, Integer.MAX_VALUE);
        }

        public Pool(Supplier<T> provider, int limit) {
            this.provider = provider;
            this.limit = limit;
            this.freeObjects = new LinkedList<>();
        }

        public T get() {
            if(deleted) {
                throw new IllegalStateException("This pool have been deleted, obtain a new one with Pools#get");
            }
            return freeObjects.isEmpty() ? provider.get() : freeObjects.poll();
        }

        public void release(T object) {
            if(deleted) {
                return;
            }
            if(object == null) {
                return;
            }
            if(freeObjects.size() >= limit) {
                return;
            }

            freeObjects.offer(object);
            if(object instanceof Poolable) {
                ((Poolable) object).reset();
            }
        }

        public void delete() {
            deleted = true;
            freeObjects.clear();
        }

    }

}
