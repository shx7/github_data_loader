package processing.loaders;

import com.sun.istack.internal.NotNull;
import processing.data.Data;

/**
 * Purpose of this class is in loading one piece of
 * T data from github.com by it's ID
 *
 * @param <T>
 */
abstract class DataLoader <T extends Data> {
    @NotNull
    protected abstract T load(int id);
}
