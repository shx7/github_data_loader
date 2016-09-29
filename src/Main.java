import cli.CommandLineOptionsParser;

public class Main {

    public static void main(String[] args) {
        new CommandLineOptionsParser().parseCommandLineInput(args);
        //new UserLoaderService(user -> System.out.println(user.toString())).loadData(1, 2);
    }
}
