package restapi;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;


public class CommandHelper {
    public static String RESOURCES_URL = "https://cloud-api.yandex.net/v1/disk/resources";

    public static void printErrorInformation(HttpURLConnection connection) throws Exception {
        InputStream inputStream = connection.getErrorStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String data = reader.readLine();
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(data);
        System.out.println(json.get("error"));
    }

    public static void printSuccessInformation(HttpURLConnection connection) throws Exception {
        int code = connection.getResponseCode();
        if (code == 200) {
            System.out.println("Success");
        } else if (code == 201) {
            System.out.println("Created");
        } else if (code == 202) {
            System.out.println("Accepted");
        } else if (code == 204) {
            System.out.println("No content");
        }
    }
}
