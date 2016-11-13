package processing.data;

import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

// TODO: complete definition corresponds to github api
public class User implements Data {
    @NotNull
    private String login;
    private int id;
    private @NotNull String avatar_url;
    private @NotNull String gravatar_id;
    private @NotNull String url;
    private @NotNull String html_url;
    private @NotNull String followers_url;
    private @NotNull String following_url;
    private @NotNull String gists_url;
    private @NotNull String starred_url;
    private @NotNull String subscriptions_url;
    private @NotNull String organizations_url;
    private @NotNull String repos_url;
    private @NotNull String events_url;
    private @NotNull String received_events_url;
    private @NotNull String type;
    private @NotNull String site_admin;

    // Private area?
    private @NotNull String name;
    private @NotNull String company;
    private @NotNull String blog;
    private @NotNull String location;
    private @NotNull String email;
    private @NotNull String hireable;
    private @NotNull String bio;
    private int public_repos;
    private @NotNull String public_gists;
    private int followers;
    private int following;
    private @NotNull String created_at;
    private @NotNull String updated_at;

    // Authentificated area
    private @Nullable Integer total_private_repos;
    private @Nullable Integer owned_private_repos;
    private @Nullable Integer private_gists;
    private @Nullable Integer disk_usage;
    private @Nullable Integer collaborators;
    private @Nullable Plan plan;

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @NotNull
    public String getReposUrl() {
        return repos_url;
    }

    private static class Plan {
        private @NotNull String name;
        private int space;
        private int private_repos;
        private int collaborators;

        @Override
        public String toString() {
            return "Plan{" +
                    "name='" + name + '\'' +
                    ", space=" + space +
                    ", private_repos=" + private_repos +
                    ", collaborators=" + collaborators +
                    '}';
        }
    }
}
