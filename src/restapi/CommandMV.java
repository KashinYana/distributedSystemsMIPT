package restapi;

import java.net.HttpURLConnection;
import java.net.URL;

public class CommandMV {
    private String mToken;
    public static String HELP = "mv <file_name_from> <file_name_to>";

    public CommandMV(String token) {
        mToken = token;
    }

    public URL getRequestURL(String pathFrom, String pathTo) throws Exception {
        return new URL(CommandHelper.RESOURCES_URL + "/move?from=" + pathFrom + "&path=" + pathTo);
    }

    public HttpURLConnection getConnection(String pathFrom, String pathTo) throws Exception {
        URL requestURL = getRequestURL(pathFrom, pathTo);
        HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "OAuth " + mToken);
        return connection;
    }

    public void execute(String pathFrom, String pathTo) throws Exception{
        HttpURLConnection connection = getConnection(pathFrom, pathTo);

        int code = connection.getResponseCode();

        if (400 <= code && code <= 600) {
            CommandHelper.printErrorInformation(connection);
            return;
        }

        CommandHelper.printSuccessInformation(connection);
    }
}