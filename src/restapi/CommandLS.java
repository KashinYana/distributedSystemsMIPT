package restapi;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class CommandLS {
    private String mToken;
    public static String HELP = "ls <path>";

    public CommandLS(String token) {
        mToken = token;
    }

    public URL getRequestURL(String path) throws Exception {
        return new URL(CommandHelper.RESOURCES_URL + "/?path=" + URLEncoder.encode(path, "UTF-8"));
    }

    public HttpURLConnection getConnection(String path) throws Exception {
        URL requestURL = getRequestURL(path);
        HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "OAuth " + mToken);
        return connection;
    }

    public void execute(String path) throws Exception {
        HttpURLConnection connection = getConnection(path);

        int code = connection.getResponseCode();

        if (400 <= code && code <= 600) {
            CommandHelper.printErrorInformation(connection);
            return;
        }

        InputStream inputStream = connection.getInputStream();

        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String data = reader.readLine();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(data);
            System.out.println(json);
            JSONArray items =
                (JSONArray) (
                    (JSONObject) json.get("_embedded"))
                        .get("items");

            for (int i = 0; i < items.size(); ++i) {
                System.out.println(((JSONObject) items.get(i)).get("name"));
            }
        }
    }
}
