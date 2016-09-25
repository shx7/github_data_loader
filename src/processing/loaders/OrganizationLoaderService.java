package processing.loaders;

import com.sun.istack.internal.NotNull;
import processing.data.Organization;

import java.util.function.Consumer;

public class OrganizationLoaderService extends DataLoaderService<Organization> {
    public OrganizationLoaderService(@NotNull Consumer<Organization> dataProcessor, @NotNull DataLoader dataLoader) {
        super(dataProcessor, new OrganizationLoader());
    }

    @Override
    public void loadData(int startId, int endId) {

    }
}

class OrganizationLoader extends DataLoader<Organization> {

    @NotNull
    @Override
    protected Organization load(int id) {
        return null;
    }
}
