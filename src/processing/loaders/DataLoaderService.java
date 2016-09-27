package processing.loaders;

import com.sun.istack.internal.NotNull;
import processing.data.Data;

import java.util.function.Consumer;

/**
 * Abstract data loader service. Used to load
 * structured data (such as User, Repository, Organization info)
 * from github.com via calling corresponding api methods
 * @param <T extends Data> type of data to be loaded
 */

public abstract class DataLoaderService <T extends Data> {
    @NotNull
    protected final Consumer<T> dataProcessor;
    @NotNull
    protected final DataLoader<T> dataLoader;

    public DataLoaderService(@NotNull Consumer<T> dataProcessor, @NotNull DataLoader<T> dataLoader) {
        this.dataProcessor = dataProcessor;
        this.dataLoader = dataLoader;
    }

    public void loadData(int startId, int endId) {
        try {
            for (int id = startId; id <= endId; ++id) {
                T data = dataLoader.load(id);
                dataProcessor.accept(data);
            }
        } catch (Exception ignored) {
            // TODO: handle exceptions
        }
    }
}
