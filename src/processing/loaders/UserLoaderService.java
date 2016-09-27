package processing.loaders;

import com.sun.istack.internal.NotNull;
import processing.data.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.IntStream;

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
        IntStream.range(startId, endId).forEach(usersToProcess::add);
        try {
            while (!usersToProcess.isEmpty()) {
                int id = usersToProcess.pop();
                if (!processedUsers.contains(id)) {
                    User user = dataLoader.load(id);
                    dataProcessor.accept(user);
                    processUserDependecies(user);
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void processUserDependecies(User user) {
        // TODO: for id in user.neighbours do process
    }

    private static class UserLoader extends DataLoader<User> {
        @NotNull private final String requestURL = "http://api.github.com/";
        @NotNull private final String requestType = "GET";

        @Override
        protected User load(int id) {
            try {
                URL url = new URL(createRequestUrl(id));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(requestType);
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String readLine;
                    StringBuilder result = new StringBuilder();
                    while ((readLine = reader.readLine()) != null) {
                        result = result.append(readLine);
                    }
                    System.out.println("Read answer for id " + id + "'" + result + "'");
                }
                // TODO: parse JSON here
                return new User();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @NotNull
        String createRequestUrl(int id) {
            return requestURL + "/user=id";
        }
    }
}