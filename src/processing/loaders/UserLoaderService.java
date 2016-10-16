package processing.loaders;

import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import processing.data.User;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserLoaderService extends DataLoaderService<User> {
    private static final Logger log = Logger.getLogger(UserLoaderService.class.getCanonicalName());

    public UserLoaderService(@NotNull Consumer<User> dataProcessor) {
        super(dataProcessor, new UserLoader());
    }

    @Override
    public void loadData(int startId, int endId) {
        for (int id = startId; id <= endId; ) {
            User[] users = dataLoader.loadPage(id);
            if (users != null) {
                for (User user : users) {
                    dataProcessor.accept(user);
                }
                id = users[users.length - 1].getId();
            } else {
                log.log(Level.WARNING, "Failed to get users from page since id = " + id);
            }
        }
    }

    private static class UserLoader extends DataLoader<User> {
        @NotNull private final String GITHUB_DEFAULT_URL = "https://api.github.com";
        @NotNull private String defaultRequestUrl = GITHUB_DEFAULT_URL + "/users";
        @NotNull private final Gson gson = new Gson();

        /**
         * Load pack of users data, starting from @id.
         * Size of page approximately about 45 users
         */
        @Nullable
        @Override
        protected User[] loadPage(int id) {
            try {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(getConnectionDataStream(id)))) {
                    String currentLine;
                    StringBuilder result = new StringBuilder();
                    while ((currentLine = reader.readLine()) != null) {
                        result = result.append(currentLine);
                    }
                    return gson.fromJson(result.toString(), User[].class);
                }
            } catch (IOException | RuntimeException e) {
                e.printStackTrace();
                log.log(Level.SEVERE, e.getMessage(), e.getLocalizedMessage());
            }
            return null;
        }

        @NotNull
        private InputStream getConnectionDataStream(int id) throws IOException {
            String currentRequestUrl = defaultRequestUrl;
            HttpsURLConnection connection = null;
            Map<String, List<String>> headerFields = null;

            while (!isRequestSucceded(headerFields)) {
                URL url = new URL(currentRequestUrl + "?since=" + id);
                connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                headerFields = connection.getHeaderFields();
                currentRequestUrl = getUpdatedRequestUrl(headerFields, currentRequestUrl);
            }

            return connection.getInputStream();
        }

        private boolean isRequestSucceded(@Nullable Map<String, List<String>> responseHeaders) {
            if (responseHeaders != null) {
                List<String> value = responseHeaders.get("Status");
                if (value != null) {
                    if (value.contains("200 OK")) return true;
                }
            }
            return false;
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
    }
}