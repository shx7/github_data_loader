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

public class FileUsersConsumer implements Consumer<User> {
    private int startId = -1;
    private int endId = -1;
    @NotNull private final List<User> data;

    public FileUsersConsumer() {
        data = new ArrayList<>();
    }

    @Override
    public void accept(User user) {
        if (startId == -1) {
            startId = user.getId();
        }
        endId = user.getId();
        data.add(user);
    }

    public void flush() throws IOException {
        Path path = FileUtil.getUsersFile(startId, endId);
        PrintWriter writer = new PrintWriter(Files.newBufferedWriter(path));
        data.forEach(user -> writer.write(user.toString()));
    }
}
