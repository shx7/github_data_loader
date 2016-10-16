package processing.loaders;

import com.sun.istack.internal.NotNull;
import processing.data.Repository;
import processing.loaders.common.DataLoader;
import processing.loaders.common.DataLoaderService;

import java.util.function.Consumer;

// TODO: remove ID's with pagination and string ID.
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