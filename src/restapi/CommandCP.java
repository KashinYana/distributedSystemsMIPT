package restapi;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class CommandCP {
    private String mToken;
    public static String HELP = "cp <file_name_from> <file_name_to>";

    public CommandCP(String token) {
        mToken = token;
    }

    public URL getRequestURL(String pathFrom, String pathTo) throws Exception {
        return new URL(CommandHelper.RESOURCES_URL + "/copy?from=" + URLEncoder.encode(pathFrom, "UTF-8")
                + "&path=" + URLEncoder.encode(pathTo, "UTF-8"));
    }

    public HttpURLConnection getConnection(String pathFrom, String pathTo) throws Exception {
        URL requestURL = getRequestURL(pathFrom, pathTo);
        HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "OAuth " + mToken);
        return connection;
    }

    public void execute(String pathFrom, String pathTo) throws Exception {
        HttpURLConnection connection = getConnection(pathFrom, pathTo);

        int code = connection.getResponseCode();

        if (400 <= code && code <= 600) {
            CommandHelper.printErrorInformation(connection);
            return;
        }

        CommandHelper.printSuccessInformation(connection);
    }
}
