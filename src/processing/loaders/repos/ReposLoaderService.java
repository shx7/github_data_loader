package processing.loaders.repos;

import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;
import javafx.util.Pair;
import oauth.OAuthCredentialsProvider;
import processing.data.Repository;
import processing.data.User;
import processing.loaders.common.DataLoader;
import processing.loaders.common.DataLoaderService;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.APPEND;

public class ReposLoaderService implements DataLoaderService<Repository> {
    @NotNull private final static Pattern userRangePattern = Pattern.compile("users-([0-9]+)-([0-9]+).*");
    @NotNull private final OAuthCredentialsProvider credentialsProvider;
    @NotNull private final static Logger log = Logger.getLogger(ReposLoader.class.getCanonicalName());
    @NotNull private final Set<Path> processedUserFiles = new HashSet<>();
    @NotNull private final Set<String> processedUsers = new HashSet<>();
    private final String PROCESSED_USER_FILES_PATH = "processed_user_files.txt";
    private final String PROCESSED_USERS_PATH = "processed_users.txt";

    public ReposLoaderService(@NotNull OAuthCredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
        loadProcessedUserFilenames();
        loadProcessedUsers();
    }

    private void loadProcessedUsers() {
        Path path = Paths.get(PROCESSED_USERS_PATH);
        try {
            processedUsers.addAll(Files.readAllLines(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProcessedUserFilenames() {
        Path path = Paths.get(PROCESSED_USER_FILES_PATH);
        try {
            processedUserFiles.addAll(Files.readAllLines(path).stream().map(it -> Paths.get(it).toAbsolutePath()).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processUsersFile(@NotNull File file) throws IOException {
        Path path = Paths.get(file.getAbsolutePath()).toAbsolutePath();
        if (processedUserFiles.add(path)) {
            DataLoader<Repository> dataLoader = new ReposLoader();
            User[] users = new Gson().fromJson(Files.newBufferedReader(path), User[].class);
            Pair<Integer, Integer> usersRange = extractUserFileRange(path.getFileName().toString());
            Consumer<Repository> dataProcessor = new FileReposConsumer(usersRange.getKey(), usersRange.getValue());
            for (User user : users) {
                try {
                    if (processedUsers.add(user.getLogin())) {
                        dataLoader.loadPage(buildRequestUrl(user)).forEach(dataProcessor);
                        markUserProcessed(user);
                    }
                } catch (IOException e) {
                    log.log(Level.WARNING, e.toString());
                    log.log(Level.WARNING, "Error extracting repositories for user " + user.getLogin() + " id=" + user.getId());
                }
            }
            markFileProcessed(path);
        }
    }

    private void markFileProcessed(@NotNull Path path) throws IOException {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(PROCESSED_USER_FILES_PATH), APPEND))) {
            writer.append(path.toAbsolutePath().toString() + "\n");
        }
    }

    public void markUserProcessed(@NotNull User user) throws IOException {
        try (Writer writer = Files.newBufferedWriter(Paths.get(PROCESSED_USERS_PATH), APPEND)) {
            writer.append(user.getLogin() + "\n");
        }
    }

    private Pair<Integer, Integer> extractUserFileRange(@NotNull String filename) {
        Matcher matcher = userRangePattern.matcher(filename);
        if (!matcher.matches()) {
            throw new RuntimeException("Wrong input user file name " + filename);
        }
        int startId = Integer.valueOf(matcher.group(1));
        int endId= Integer.valueOf(matcher.group(2));
        return new Pair<>(startId, endId);
    }

    @NotNull
    private String buildRequestUrl(@NotNull User user) {
        return "users/" + user.getLogin() +
                "/repos?client_id=" + credentialsProvider.getClientId() +
                "&client_secret=" + credentialsProvider.getClientSecret();
    }
}

class ReposLoader extends DataLoader<Repository> {
    @Override
    public List<Repository> loadPage(@NotNull String reposUrl) throws IOException {
        return Arrays.asList(loadData(Repository[].class, reposUrl));
    }
}