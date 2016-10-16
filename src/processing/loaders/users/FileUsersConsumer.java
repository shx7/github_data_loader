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
    private static final long FLUSH_THRESHOLD = 200;
    private int startId = -1;
    private int endId = -1;
    private int count = 0;
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
        ++count;
        checkThreshold();
    }

    private void checkThreshold() {
        if (count >= FLUSH_THRESHOLD) {
            try {
                flush();
            } catch (IOException e) {
                log.log(Level.WARNING, "Failed to flush: " + e.getMessage());
                log.log(Level.WARNING, e.getLocalizedMessage());
            }
        }
    }

    public void flush() throws IOException {
        if (startId > 0) {
            Path path = FileUtil.getUsersFile(startId, endId);
            PrintWriter writer = new PrintWriter(Files.newBufferedWriter(path));
            data.forEach(user -> writer.write(user.toString()));
            data.clear();
            count = 0;
            log.log(Level.INFO, "Flush users from " + startId + " to " + endId);
            startId = endId = -1;
        }
    }
}
