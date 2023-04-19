package mc.obliviate.util.database;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Defines all database classes,
 * provides connection management and object save/load methods.
 * @param <T> the object of database
 */
public interface Database<T> {

    default void save(Iterable<T> objects) {
        objects.forEach(this::save);
    }

    void save(T object);

    default CompletableFuture<Void> saveAsync(T object) {
        return CompletableFuture.runAsync(() -> save(object));
    }

    T load(Object id);

    default CompletableFuture<T> loadAsync(Object id) {
        return CompletableFuture.supplyAsync(() -> load(id));
    }

    List<T> loadAll();

    default CompletableFuture<List<T>> loadAllAsync() {
        return CompletableFuture.supplyAsync(this::loadAll);
    }
}
