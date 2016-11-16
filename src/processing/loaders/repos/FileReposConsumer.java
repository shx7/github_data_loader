package processing.loaders.repos;

import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;
import fs.DataRangeProperties;
import fs.FileUtil;
import processing.data.Repository;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

class FileReposConsumer implements Consumer<Repository> {
    @NotNull private static final Logger log = Logger.getLogger(FileReposConsumer.class.getCanonicalName());
    private static final long FLUSH_THRESHOLD = 500;
    private long consumedCount = 0;
    @NotNull private final List<Repository> data;
    @NotNull private String fileNameSuffix;
    private int reposFlushIndex = 0;

    public FileReposConsumer(int userStartId, int userEndId) {
        data = new ArrayList<>();
        fileNameSuffix = "-users-" + userStartId + "-" + userEndId;
    }

    @Override
    public void accept(Repository repository) {
        data.add(repository);
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

    private void flush() throws IOException {
        Path path = FileUtil.getNextRepositoryFile(fileNameSuffix, reposFlushIndex);
        if (Files.exists(path)) {
            throw new IOException("Flush failed to file " + path.toAbsolutePath());
        }
        PrintWriter writer = new PrintWriter(Files.newBufferedWriter(path));
        writer.write(new Gson().toJson(data.toArray(), Repository[].class));
        writer.flush();
        data.clear();
        log.log(Level.INFO, "Flushed " + consumedCount + " repos to " + path);
        consumedCount = 0;
        reposFlushIndex++;
    }
}