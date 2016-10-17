package processing.loaders.users;

import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import oauth.OAuthCredentialsProvider;
import processing.data.User;
import processing.loaders.common.DataLoader;
import processing.loaders.common.DataLoaderService;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class UserLoaderService extends DataLoaderService<User> {
    private static final Logger log = Logger.getLogger(UserLoaderService.class.getCanonicalName());

    public UserLoaderService(@NotNull Consumer<User> dataProcessor,
                             @NotNull OAuthCredentialsProvider credentialsProvider) {
        super(dataProcessor, new UserLoader(credentialsProvider));
    }

    @Override
    public void loadData(int startId, int endId) {
        for (int id = startId; id <= endId; ) {
            try {
                List<User> users = dataLoader.loadPage(id);
                users.forEach(dataProcessor);
                id = users.get(users.size() - 1).getId();
            } catch (IOException e) {
                log.log(Level.WARNING, "Failed to get users from page since id = " + id);
                log.log(Level.SEVERE, e.getLocalizedMessage());
                log.log(Level.SEVERE, e.getMessage());
                log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            }
        }
    }
}

class UserLoader extends DataLoader<User> {
    @NotNull private final static Logger log = Logger.getLogger(UserLoader.class.getCanonicalName());
    @NotNull private final String GITHUB_DEFAULT_URL = "https://api.github.com";
    @NotNull private String globalRequestUrl = GITHUB_DEFAULT_URL;
    @NotNull private final Gson gson;
    @NotNull private final OAuthCredentialsProvider credentialsProvider;

    UserLoader(OAuthCredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
        this.gson = new Gson();
    }

    /**
     * Load pack of users data, starting from @id.
     * Size of page approximately about 45 users
     */
    @NotNull
    @Override
    public List<User> loadPage(int id) throws IOException {
        List<User> result = new ArrayList<>();
        User[] prefetchedUsers = loadData(User[].class, buildUsersPageRequestUrl(id));
        if (prefetchedUsers != null) {
            for (User user : prefetchedUsers) {
                result.add(loadData(User.class, buildUserRequestUrl(user.getLogin())));
            }
        }
        return result;
    }

    @Nullable
    private <T> T loadData(@NotNull Class<T> type, @NotNull String request) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getConnectionDataStream(request)))) {
            return gson.fromJson(reader.lines().collect(Collectors.joining()), type);
        }
    }

    @NotNull
    private InputStream getConnectionDataStream(@NotNull String request) throws IOException {
        String currentRequestUrl = globalRequestUrl;
        HttpsURLConnection connection = null;
        Map<String, List<String>> headerFields;

        while (connection == null || connection.getResponseCode() != 200) {
            URL url = new URL(currentRequestUrl + "/" + request);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            headerFields = connection.getHeaderFields();
            // Handle error codes
            switch (connection.getResponseCode()) {
                case 403:
                    processRateLimit(headerFields);
                    break;
                case 404:
                    log.log(Level.SEVERE, "404 Not found");
                    return connection.getInputStream();
                case 301:
                    globalRequestUrl = getUpdatedRequestUrl(headerFields, globalRequestUrl);
                    currentRequestUrl = globalRequestUrl;
                    break;
                case 302:
                case 307:
                    currentRequestUrl = getUpdatedRequestUrl(headerFields, currentRequestUrl);
                    break;
            }
        }
        return connection.getInputStream();
    }

    @NotNull
    private String buildUsersPageRequestUrl(int id) {
        return "users?since=" + id +
                "&client_id=" + credentialsProvider.getClientId() +
                "&client_secret=" + credentialsProvider.getClientSecret();
    }

    @NotNull
    private String buildUserRequestUrl(String login) {
        return "users/" + login +
                "?client_id=" + credentialsProvider.getClientId() +
                "&client_secret=" + credentialsProvider.getClientSecret();
    }

    @NotNull
    private String getUpdatedRequestUrl(@NotNull Map<String, List<String>> headersFields,
                                        @NotNull String oldRequestUrl) {
        List<String> redirectionUrl = headersFields.get("Location");
        if (redirectionUrl != null) {
            return redirectionUrl.get(0);
        }
        return oldRequestUrl;
    }

    private void processRateLimit(@NotNull Map<String, List<String>> headers) {
        long remainingRateLimit = Long.valueOf(headers.get("X-RateLimit-Remaining").get(0));
        if (remainingRateLimit == 0) {
            long secondsToReset = Long.valueOf(headers.get("X-RateLimit-Reset").get(0));
            log.log(Level.WARNING, "Rate limit ended, current time " + LocalDateTime.now());
            log.log(Level.WARNING, "Will wait until " + LocalDateTime.ofEpochSecond(secondsToReset, 0, ZoneOffset.ofHours(3)));
            sleepUntilReset(secondsToReset);
        }
    }

    private void sleepUntilReset(long secondsToReset) {
        while (Instant.now().toEpochMilli()/1000 <= secondsToReset) {
            long secondsToWait = secondsToReset - Instant.now().toEpochMilli()/1000;
            try {
                Thread.sleep(secondsToWait * 1000);
            } catch (InterruptedException ignored) {}
        }
    }
}