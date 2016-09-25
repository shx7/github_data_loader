package data.loaders;

import com.sun.istack.internal.NotNull;
import data.Data;

import java.util.function.Consumer;

public abstract class DataLoaderService {
    @NotNull
    private final Consumer<Data> dataProcessor;
    @NotNull
    private final DataLoader loader;

    public DataLoaderService(@NotNull Consumer<Data> dataProcessor, @NotNull DataLoader loader) {
        this.dataProcessor = dataProcessor;
        this.loader = loader;
    }

    public void startLoading(int startId, int endId) {
        for (int id = startId; id <= endId; ++id) {
            dataProcessor.accept(loader.load(id));
        }
    }
}
