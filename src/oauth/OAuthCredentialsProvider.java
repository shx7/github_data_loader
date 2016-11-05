package oauth;

import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class OAuthCredentialsProvider {
    @NotNull private String credentialsFilename;
    @NotNull private String clientId;
    @NotNull private String clientSecret;
    @NotNull private final Properties properties;

    public OAuthCredentialsProvider(@NotNull String propertiesFilename) {
        credentialsFilename = propertiesFilename;
        properties = new Properties();
    }

    public void loadConfiguration() throws IOException {
        properties.load(Files.newBufferedReader(Paths.get(credentialsFilename)));
        clientId = properties.getProperty("CLIENT_ID");
        clientSecret = properties.getProperty("CLIENT_SECRET");
    }

    @NotNull
    public String getClientId() {
        return clientId;
    }

    @NotNull
    public String getClientSecret() {
        return clientSecret;
    }
}
