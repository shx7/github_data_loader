package processing.loaders.users;

import com.sun.istack.internal.NotNull;
import fs.FileUtil;
import processing.data.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUsersConsumer implements Consumer<User> {
    private static final Logger log = Logger.getLogger(FileUsersConsumer.class.getCanonicalName());
    private static final long FLUSH_THRESHOLD = 500;
    private int startId = -1;
    private int endId = -1;
    private long consumedCount = 0;
    @NotNull private final List<User> data;

    public FileUsersConsumer() {
        data = new ArrayList<>();
    }

    @Override
    public void accept(User user) {
        endId = user.getId();
        if (startId < 0) {
            startId = endId;
        }
        data.add(user);
        ++consumedCount;
        checkThreshold();
    }

    private void checkThreshold() {
        if (consumedCount >= FLUSH_THRESHOLD) {
            try {
                flush();
            } catch (IOException e) {
                log.log(Level.WARNING, "Failed to flush: " + e.getMessage());
                log.log(Level.WARNING, e.getLocalizedMessage());
                log.log(Level.WARNING, e.toString());
            }
        }
    }

    public void flush() throws IOException {
        if (startId > 0) {
            Path path = FileUtil.getUsersFile(startId, endId);
            PrintWriter writer = new PrintWriter(Files.newBufferedWriter(path));
            data.forEach(user -> writer.write(user.toString()));
            data.clear();
            log.log(Level.INFO, "Flushed " + consumedCount + " users from " + startId + " to " + endId);
            consumedCount = 0;
            startId = endId = -1;
        }
    }
}
