package processing.loaders.common;

import com.sun.istack.internal.NotNull;
import processing.data.Data;

import java.util.function.Consumer;

/**
 * Abstract data loader service. Used to load
 * structured data (such as User, Repository, Organization info)
 * from github.com via calling corresponding api methods
 * type of data to be loaded
 */

public interface DataLoaderService <T extends Data> {
}
