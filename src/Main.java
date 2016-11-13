import fs.DataRangeProperties;
import oauth.OAuthCredentialsProvider;
import processing.loaders.users.FileUsersConsumer;
import processing.loaders.users.UserLoaderService;

import java.io.IOException;

public class Main {
    private static final DataRangeProperties usersRange;
    private static final OAuthCredentialsProvider credentialsProvider;
    private static final FileUsersConsumer usersConsumer;

    static {
        usersRange = new DataRangeProperties("users_range.properties");
        credentialsProvider = new OAuthCredentialsProvider("oauth.properties");
        usersConsumer = new FileUsersConsumer(usersRange);
    }

    public static void main(String[] args) throws IOException {
        loadConfiguration();
        initSignalsHandlers();
        startProcessing();
    }

    private static void startProcessing() throws IOException {
        new UserLoaderService(usersConsumer, credentialsProvider).loadData(usersRange.getStartId(), usersRange.getEndId());
        usersConsumer.flush();
    }

    private static void initSignalsHandlers() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                usersConsumer.flush();
            } catch (IOException e) {
                System.err.println("Failed to flush when shutdown hook handling");
                System.err.println(e.toString());
                System.err.println(e.getMessage());
                System.err.println(e.getLocalizedMessage());
            }
        }));
    }

    private static void loadConfiguration() throws IOException {
        usersRange.loadRange();
        credentialsProvider.loadConfiguration();
    }
}
