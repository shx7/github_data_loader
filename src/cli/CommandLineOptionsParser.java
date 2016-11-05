package cli;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.logging.Logger;
import org.apache.commons.cli.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class CommandLineOptionsParser {
    private static final Logger log = Logger.getLogger(CommandLineOptionsParser.class);

    @NotNull
    public Map<String, Object> parseCommandLineInput(@NotNull String[] commandLineArguments) {
        Map<String, Object> result = new HashMap<>();
        Options options = new Options();
        options.addOption("mode", true, "Select which structures to download: users, repos, orgs")
               .addOption("start_id", true, "From which id start downloading")
               .addOption("end_id", true, "End of downloading id range");
        Parser parser = new PosixParser();
        try {
            for (Option option : parser.parse(options, commandLineArguments).getOptions()) {
                result.put(option.getArgName(), option.getValue());
            }
        } catch (ParseException e) {
            log.log(Level.WARNING, e.getMessage());
        }
        return result;
    }
}
