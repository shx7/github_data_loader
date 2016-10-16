package processing.data;

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
    private int publicRepos;
    private @NotNull String publicGists;
    private int followers;
    private int following;
    private @NotNull String createdAt;
    private @NotNull String updatedAt;

    // Authentificated area
    private @Nullable Integer totalPrivateRepos;
    private @Nullable Integer ownedPrivateRepos;
    private @Nullable Integer privateGists;
    private @Nullable Integer diskUsage;
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
        return "User{" +
                "login='" + login + '\'' +
                ", id=" + id +
                ", avatar_url='" + avatar_url + '\'' +
                ", gravatar_id='" + gravatar_id + '\'' +
                ", url='" + url + '\'' +
                ", html_url='" + html_url + '\'' +
                ", followers_url='" + followers_url + '\'' +
                ", following_url='" + following_url + '\'' +
                ", gists_url='" + gists_url + '\'' +
                ", starred_url='" + starred_url + '\'' +
                ", subscriptions_url='" + subscriptions_url + '\'' +
                ", organizations_url='" + organizations_url + '\'' +
                ", repos_url='" + repos_url + '\'' +
                ", events_url='" + events_url + '\'' +
                ", received_events_url='" + received_events_url + '\'' +
                ", type='" + type + '\'' +
                ", site_admin='" + site_admin + '\'' +
                ", name='" + name + '\'' +
                ", company='" + company + '\'' +
                ", blog='" + blog + '\'' +
                ", location='" + location + '\'' +
                ", email='" + email + '\'' +
                ", hireable='" + hireable + '\'' +
                ", bio='" + bio + '\'' +
                ", publicRepos=" + publicRepos +
                ", publicGists='" + publicGists + '\'' +
                ", followers=" + followers +
                ", following=" + following +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", totalPrivateRepos=" + totalPrivateRepos +
                ", ownedPrivateRepos=" + ownedPrivateRepos +
                ", privateGists=" + privateGists +
                ", diskUsage=" + diskUsage +
                ", collaborators=" + collaborators +
                ", plan=" + plan +
                '}';
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
