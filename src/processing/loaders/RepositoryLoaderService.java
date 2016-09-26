package processing.loaders;

import com.sun.istack.internal.NotNull;
import processing.data.Repository;

import java.util.function.Consumer;

public class RepositoryLoaderService extends DataLoaderService<Repository> {
    public RepositoryLoaderService(@NotNull Consumer<Repository> dataProcessor) {
        super(dataProcessor, new RepositoryLoader());
    }

    private static class RepositoryLoader extends DataLoader<Repository> {
        @NotNull
        @Override
        protected Repository load(int id) {
            return null;
        }
    }
}