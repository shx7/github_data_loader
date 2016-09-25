package processing.loaders;

import com.sun.istack.internal.NotNull;
import processing.data.User;

import java.util.function.Consumer;

public class UserLoaderService extends DataLoaderService<User> {
    public UserLoaderService(@NotNull Consumer<User> dataProcessor) {
        super(dataProcessor, new UserLoader());
    }

    @Override
    public void loadData(int startId, int endId) {
        // TODO: load all users with dependencies
    }
}

class UserLoader extends DataLoader<User> {
    @NotNull
    @Override
    protected User load(int id) {
        return null;
    }
}
