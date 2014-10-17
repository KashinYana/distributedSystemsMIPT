package restapi;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class CommandRM {
    private String mToken;
    public static String HELP = "rm <file_name>";

    public CommandRM(String token) {
        mToken = token;
    }

    public URL getRequestURL(String path) throws Exception {
        return new URL(CommandHelper.RESOURCES_URL + "/?path=" + URLEncoder.encode(path, "UTF-8"));
    }

    public HttpURLConnection getConnection(String path) throws Exception {
        URL requestURL = getRequestURL(path);
        HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Authorization", "OAuth " + mToken);
        return connection;
    }

    public void execute(String path) throws Exception{
        HttpURLConnection connection = getConnection(path);

        int code = connection.getResponseCode();

        if (400 <= code && code <= 600) {
            CommandHelper.printErrorInformation(connection);
            return;
        }

        CommandHelper.printSuccessInformation(connection);
    }
}
