package fs;

import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class FileUtil {
    private static final String USERS_DEFAULT_DIRECTORY = "users";
    private static String usersDirectory = USERS_DEFAULT_DIRECTORY;

    public static void setUsersDirectory(@NotNull String path) {
        usersDirectory = path;
    }

    @NotNull
    private static Path getUsersDirectory() throws IOException {
        Path path = Paths.get(usersDirectory);
        if (!Files.exists(path)) {
            return Files.createDirectory(path);
        }
        return path;
    }

    @NotNull
    public static Path getUsersFile(int startId, int endEnd) throws IOException {
        String usersGeneratedFilename = "users-" + startId + "-" + endEnd + "-" + LocalDateTime.now();
        Path usersDirectory = getUsersDirectory();
        Path path = Paths.get(usersDirectory.toAbsolutePath().toString(), usersGeneratedFilename);
        if (!Files.exists(path)) {
            return Files.createFile(path);
        }
        return path;
    }
}
