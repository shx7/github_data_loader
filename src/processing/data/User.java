package processing.data;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

// TODO: complete definition corresponds to github api
public class User implements Data {
    @NotNull String login;
    int id;
    @NotNull String avatarUrl;
    @NotNull String gravatarUrl;
    @NotNull String url;
    @NotNull String htmlUrl;
    @NotNull String followerUrl;
    @NotNull String gistsUrl;
    @NotNull String starredUrl;
    @NotNull String subscriptionsUrl;
    @NotNull String organizationsUrl;
    @NotNull String reposUrl;
    @NotNull String eventsUrl;
    @NotNull String receivedEventsUrl;
    @NotNull String type;
    @NotNull String siteAdmin;
    @NotNull String name;
    @NotNull String company;
    @NotNull String blog;
    @NotNull String location;
    @NotNull String email;
    @NotNull String hireable;
    @NotNull String bio;
    int publicRepos;
    @NotNull String publicGists;
    int followers;
    int following;
    @NotNull String createdAt;
    @NotNull String updatedAt;

    // Authentificated area
    @Nullable Integer totalPrivateRepos;
    @Nullable Integer ownedPrivateRepos;
    @Nullable Integer privateGists;
    @Nullable Integer diskUsage;
    @Nullable Integer collaborators;
    @Nullable Plan plan;

    private static class Plan {
        @NotNull String name;
        int space;
        int privateRepos;
        int collaborators;
    }
}
