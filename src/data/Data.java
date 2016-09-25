package data;

import com.sun.istack.internal.NotNull;

public class Data {
    @NotNull
    private final String jsonData;

    private Data(@NotNull String data) {
        jsonData = data;
    }

    public static Data createDate(@NotNull String data) {
        return new Data(data);
    }

    @NotNull
    public String getData() {
        return jsonData;
    }
}
