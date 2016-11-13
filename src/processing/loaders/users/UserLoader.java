package processing.loaders.users;

import com.sun.istack.internal.NotNull;
import oauth.OAuthCredentialsProvider;
import processing.data.User;
import processing.loaders.common.DataLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class UserLoader extends DataLoader<User> {
    @NotNull private final OAuthCredentialsProvider credentialsProvider;

    UserLoader(OAuthCredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
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
}
