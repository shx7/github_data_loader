import cli.CommandLineOptionsParser;
import oauth.OAuthCredentialsProvider;
import processing.loaders.UserLoaderService;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        //new CommandLineOptionsParser().parseCommandLineInput(args);
        OAuthCredentialsProvider credentialsProvider = new OAuthCredentialsProvider("oauth.prop");
        new UserLoaderService(user -> System.out.println(user.toString()),
                credentialsProvider).loadData(0, 100);
    }
}
