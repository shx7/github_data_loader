package processing.loaders;

import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;
import processing.data.User;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class UserLoaderService extends DataLoaderService<User> {
    @NotNull private final Deque<Integer> usersToProcess;
    @NotNull private final Set<Integer> processedUsers;

    public UserLoaderService(@NotNull Consumer<User> dataProcessor) {
        super(dataProcessor, new UserLoader());
        usersToProcess = new ArrayDeque<>();
        processedUsers = new HashSet<>();
    }

    @Override
    public void loadData(int startId, int endId) {
        try {
            for (int id = startId; id <= endId; ) {
                User[] users = dataLoader.loadPage(id);
                for (User user : users) {
                    dataProcessor.accept(user);
                }
                id += users.length;
            }
        } catch (Exception ignored) {
        }
    }

    private static class UserLoader extends DataLoader<User> {
        @NotNull private final String requestURL = "https://api.github.com/users/";
        @NotNull private final String requestType = "GET";

        @Override
        protected User[] loadPage(int id) {
            try {
                URL url = new URL(createRequestUrl(id));
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod(requestType);
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String readLine;
                    StringBuilder result = new StringBuilder();
                    while ((readLine = reader.readLine()) != null) {
                        result = result.append(readLine);
                    }
                    System.out.println("Read answer for id " + id + "\"" + result + "\"");
                    return new Gson().fromJson(result.toString(), User[].class);
                }
            } catch (IOException | RuntimeException e) {
                e.printStackTrace();
            }
            return null;
        }

        @NotNull
        String createRequestUrl(int id) {
            return requestURL + "shx7";
        }
    }
}