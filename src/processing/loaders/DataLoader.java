package processing.loaders;

import com.sun.istack.internal.NotNull;

public abstract class DataLoader <T> {
    @NotNull
    protected abstract T load(int id);
}
