package processing.loaders;

import com.sun.istack.internal.NotNull;
import java.util.function.Consumer;

public abstract class DataLoaderService <T> {
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
