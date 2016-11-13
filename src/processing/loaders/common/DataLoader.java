package processing.loaders.common;

import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import processing.data.Data;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Purpose of this class is in loading one piece of
 * T data from github.com by it's ID
 *
 * @param <T>
 */
public abstract class DataLoader <T extends Data> {
    @NotNull private final Logger log = Logger.getLogger(getClass().getCanonicalName());
    @NotNull private final String GITHUB_DEFAULT_URL = "https://api.github.com";
    @NotNull private String globalRequestUrl = GITHUB_DEFAULT_URL;

    @NotNull
    protected T load(int id) {
        return null;
    }

    @NotNull
    public List<T> loadPage(int startIndex) throws IOException {
        return null;
    }

    @NotNull
    public List<T> loadPage(@NotNull String url) throws IOException {
        return null;
    }

    @Nullable
    protected  <V> V loadData(@NotNull Class<V> type, @NotNull String request) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getConnectionDataStream(request)))) {
            return new Gson().fromJson(reader.lines().collect(Collectors.joining()), type);
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
                    log.log(Level.INFO, "Permanent redirect to " + globalRequestUrl);
                    break;
                case 302:
                case 307:
                    currentRequestUrl = getUpdatedRequestUrl(headerFields, currentRequestUrl);
                    log.log(Level.INFO, "Temporal redirect to " + currentRequestUrl);
                    break;
            }
        }
        return connection.getInputStream();
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
            String errorMessage = "Rate limit ended, current time " + LocalDateTime.now() + "\n" +
                    "Will wait until " + LocalDateTime.ofEpochSecond(secondsToReset, 0, ZoneOffset.ofHours(3)).toLocalTime();
            log.log(Level.WARNING, errorMessage);
            sleepUntilReset(secondsToReset);
        }
    }

    private void sleepUntilReset(long secondsToReset) {
        while (Instant.now().toEpochMilli()/1000 <= secondsToReset) {
            long secondsToWait = secondsToReset - Instant.now().toEpochMilli()/1000;
            if (secondsToWait > 0) {
                try {
                    Thread.sleep(secondsToWait * 1000);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}
