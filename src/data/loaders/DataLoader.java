package data.loaders;

import com.sun.istack.internal.NotNull;
import data.Data;

public abstract class DataLoader {
    @NotNull
    protected abstract Data load(int id);
}
