import javafx.util.Pair;
import oauth.OAuthCredentialsProvider;
import processing.loaders.users.FileUsersConsumer;
import processing.loaders.users.UserLoaderService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static FileUsersConsumer usersConsumer = new FileUsersConsumer();
    private static Logger log = Logger.getLogger(Main.class.toString());

    public static void main(String[] args) throws IOException {
        initSignalsHandlers();
        startProcessing();
    }

    private static void startProcessing() throws IOException {
        OAuthCredentialsProvider credentialsProvider = new OAuthCredentialsProvider("oauth.prop");
        Pair<Integer, Integer> usersRange = getUsersToLoadRange();
        new UserLoaderService(usersConsumer, credentialsProvider).loadData(usersRange.getKey(), usersRange.getValue());
        usersConsumer.flush();
    }

    private static void initSignalsHandlers() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                usersConsumer.flush();
            } catch (IOException e) {
                log.log(Level.WARNING, "Failed to flush when shutdown hook handling");
                log.log(Level.WARNING, e.toString());
                log.log(Level.WARNING, e.getMessage());
                log.log(Level.WARNING, e.getLocalizedMessage());
            }
        }));
    }

    private static Pair<Integer, Integer> getUsersToLoadRange() throws IOException {
        Properties rangeProperties = new Properties();
        rangeProperties.load(Files.newBufferedReader(Paths.get("users_range.properties")));
        int startId = Integer.parseInt(rangeProperties.getProperty("START_ID"));
        int endId = Integer.parseInt(rangeProperties.getProperty("END_ID"));
        return new Pair<>(startId, endId);
    }
}
