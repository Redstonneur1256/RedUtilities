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
        Pool<?> pool = pools.get(type);
        if(pool != null) {
            pool.freeObjects.clear();
            pool.deleted = true;
        }
    }

    private static class Pool<T> {

        private Supplier<T> provider;
        private Queue<T> freeObjects;
        private boolean deleted;
        public Pool(Supplier<T> provider) {
            this.provider = provider;
            this.freeObjects = new LinkedList<>();
        }

        public T get() {
            if(deleted)
                throw new IllegalStateException("This pool have been deleted, obtain a new one with Pools#get");
            return freeObjects.isEmpty() ? provider.get() : freeObjects.poll();
        }

        public void release(T object) {
            freeObjects.offer(object);
            if(object instanceof Poolable) {
                ((Poolable) object).reset();
            }
        }
    }

    public interface Poolable {
        void reset();
    }

}
