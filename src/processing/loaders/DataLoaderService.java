package processing.loaders;

import com.sun.istack.internal.NotNull;
import java.util.function.Consumer;

public abstract class DataLoaderService <T> {
    @NotNull
    private final Consumer<T> dataProcessor;
    @NotNull
    private final DataLoader dataLoader;

    public DataLoaderService(@NotNull Consumer<T> dataProcessor, @NotNull DataLoader dataLoader) {
        this.dataProcessor = dataProcessor;
        this.dataLoader = dataLoader;
    }

    public abstract void loadData(int startId, int endId);
}
