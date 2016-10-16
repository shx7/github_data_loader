import oauth.OAuthCredentialsProvider;
import processing.loaders.users.FileUsersConsumer;
import processing.loaders.users.UserLoaderService;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        //new CommandLineOptionsParser().parseCommandLineInput(args);
        OAuthCredentialsProvider credentialsProvider = new OAuthCredentialsProvider("oauth.prop");
        FileUsersConsumer usersConsumer = new FileUsersConsumer();
        new UserLoaderService(usersConsumer, credentialsProvider).loadData(0, 100);
        usersConsumer.flush();
    }
}
