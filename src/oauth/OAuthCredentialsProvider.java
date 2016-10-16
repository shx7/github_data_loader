package oauth;

import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class OAuthCredentialsProvider {
    @NotNull private final String clientId;
    @NotNull private final String clientSecret;

    public OAuthCredentialsProvider(@NotNull String propertiesFilename) throws IOException {
        Properties properties = new Properties();
        properties.load(Files.newBufferedReader(Paths.get(propertiesFilename)));
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
