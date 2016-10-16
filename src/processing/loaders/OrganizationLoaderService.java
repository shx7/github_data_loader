package processing.loaders;

import com.sun.istack.internal.NotNull;
import processing.data.Organization;
import processing.loaders.common.DataLoader;
import processing.loaders.common.DataLoaderService;

import java.util.function.Consumer;

public class OrganizationLoaderService extends DataLoaderService<Organization> {
    public OrganizationLoaderService(@NotNull Consumer<Organization> dataProcessor, @NotNull DataLoader dataLoader) {
        super(dataProcessor, new OrganizationLoader());
    }

    private static class OrganizationLoader extends DataLoader<Organization> {
        @NotNull
        @Override
        protected Organization load(int id) {
            return null;
        }
    }
}