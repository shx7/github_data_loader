package fs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class UsersRange {
    private static final String USERS_RANGE_FILE = "users_range.properties";
    private static final String START_ID = "START_ID";
    private static final String END_ID = "END_ID";
    private int startId;
    private int endId;
    private Properties rangeProperties = new Properties();

    public void loadRange() throws IOException {
        Properties rangeProperties = new Properties();
        rangeProperties.load(Files.newBufferedReader(Paths.get(USERS_RANGE_FILE)));
        startId = Integer.parseInt(rangeProperties.getProperty(START_ID));
        endId = Integer.parseInt(rangeProperties.getProperty(END_ID));
    }

    public void updateStartId(int value) {
        startId = value;
        rangeProperties.setProperty(START_ID, String.valueOf(startId));
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
