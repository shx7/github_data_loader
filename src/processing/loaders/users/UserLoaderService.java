package processing.loaders.users;

import com.sun.istack.internal.NotNull;
import oauth.OAuthCredentialsProvider;
import processing.data.User;
import processing.loaders.common.DataLoaderService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserLoaderService extends DataLoaderService<User> {
    private static final int SKIP_USERS_COUNT = 100;
    private static final Logger log = Logger.getLogger(UserLoaderService.class.getCanonicalName());

    public UserLoaderService(@NotNull Consumer<User> dataProcessor,
                             @NotNull OAuthCredentialsProvider credentialsProvider) {
        super(dataProcessor, new UserLoader(credentialsProvider));
    }

    @Override
    public void loadData(int startId, int endId) {
        log.log(Level.INFO, "loadData(" + startId + ", " + endId + ")");
        for (int id = startId; id <= endId; ) {
            try {
                List<User> users = dataLoader.loadPage(id);
                users.forEach(dataProcessor);
                id = users.get(users.size() - 1).getId();
            } catch (IOException e) {
                log.log(Level.WARNING, "Failed to get users from page since id = " + id);
                log.log(Level.SEVERE, e.toString());
                log.log(Level.SEVERE, e.getLocalizedMessage());
                log.log(Level.SEVERE, e.getMessage());
                log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
                startId += SKIP_USERS_COUNT;
                log.log(Level.INFO, "Skipping " + SKIP_USERS_COUNT + " retry from id = " + startId);
            }
        }
    }
}