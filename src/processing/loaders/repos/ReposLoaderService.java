package processing.loaders.repos;

import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;
import javafx.util.Pair;
import oauth.OAuthCredentialsProvider;
import processing.data.Repository;
import processing.data.User;
import processing.loaders.common.DataLoader;
import processing.loaders.common.DataLoaderService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReposLoaderService implements DataLoaderService<Repository> {
    private final static Pattern userRangePattern = Pattern.compile("users-([0-9]+)-([0-9]+).*");
    @NotNull private final OAuthCredentialsProvider credentialsProvider;
    private static Logger log = Logger.getLogger(ReposLoader.class.getCanonicalName());

    public ReposLoaderService(@NotNull OAuthCredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
    }

    public void processUsersFile(@NotNull String filePath) throws IOException {
        DataLoader<Repository> dataLoader = new ReposLoader();
        User[] users = new Gson().fromJson(Files.newBufferedReader(Paths.get(filePath)), User[].class);
        Pair<Integer, Integer> usersRange = extractUserFileRange(Paths.get(filePath).getFileName().toString());
        Consumer<Repository> dataProcessor = new FileReposConsumer(usersRange.getKey(), usersRange.getValue());
        for (User user : users) {
            try {
                dataLoader.loadPage(buildRequestUrl(user)).forEach(dataProcessor);
            } catch (IOException e) {
                log.log(Level.WARNING, e.toString());
                log.log(Level.WARNING, "Error extracting repositories for user " + user.getLogin() + " id=" + user.getId());
            }
        }
    }

    public Pair<Integer, Integer> extractUserFileRange(@NotNull String filename) {
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