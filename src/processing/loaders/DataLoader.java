package processing.loaders;

import com.sun.istack.internal.NotNull;
import processing.data.Data;

import java.util.List;

/**
 * Purpose of this class is in loading one piece of
 * T data from github.com by it's ID
 *
 * @param <T>
 */
abstract class DataLoader <T extends Data> {
    @NotNull
    protected T load(int id) {
        return null;
    }

    @NotNull
    protected List<T> loadPage(int startIndex) {
        return null;
    }
}
