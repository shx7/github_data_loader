import fs.UsersRange;
import oauth.OAuthCredentialsProvider;
import processing.loaders.users.FileUsersConsumer;
import processing.loaders.users.UserLoaderService;

import java.io.IOException;

public class Main {
    private static final UsersRange usersRange = new UsersRange();
    private static final FileUsersConsumer usersConsumer = new FileUsersConsumer();

    public static void main(String[] args) throws IOException {
        loadConfiguration();
        initSignalsHandlers();
        startProcessing();
    }

    private static void startProcessing() throws IOException {
        OAuthCredentialsProvider credentialsProvider = new OAuthCredentialsProvider("oauth.prop");
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
    }
}
