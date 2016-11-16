package processing.data;

public class Repository implements Data {
    int id;
    String name;
    String full_name;
    User owner;
    String description;
    String url;
    boolean fork;
}
