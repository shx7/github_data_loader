package fs;

import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class UsersRange {
    @NotNull private static final String START_ID = "START_ID";
    @NotNull private static final String END_ID = "END_ID";
    @NotNull private Properties rangeProperties;
    @NotNull private final String usersRangeFilename;
    private int startId;
    private int endId;

    public UsersRange(@NotNull String filename) {
        usersRangeFilename = filename;
        rangeProperties = new Properties();
    }

    public void loadRange() throws IOException {
        rangeProperties.load(Files.newBufferedReader(Paths.get(usersRangeFilename)));
        startId = Integer.parseInt(rangeProperties.getProperty(START_ID));
        endId = Integer.parseInt(rangeProperties.getProperty(END_ID));
    }

    public void updateStartId(int value) {
        startId = value;
        rangeProperties.setProperty(START_ID, String.valueOf(startId));
        try {
            rangeProperties.store(Files.newOutputStream(Paths.get(usersRangeFilename)), "");
        } catch (IOException ignored) {}
    }

    public void updateEndId(int value) {
        endId = value;
        rangeProperties.setProperty(END_ID, String.valueOf(endId));
    }

    public int getStartId() {
        return startId;
    }

    public int getEndId() {
        return endId;
    }
}
