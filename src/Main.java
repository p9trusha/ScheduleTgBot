import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

class EtuApi {
    String getJson(String group) throws IOException {
        group = URLEncoder.encode(group, StandardCharsets.UTF_8);
        String apiLink = String.format(
                "https://https://digital.etu.ru/api/mobile/schedule",
                group
        );
        URL searchReguest = new URL(apiLink);
        URLConnection urlCon = searchReguest.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        urlCon.getInputStream()));
        String inputLine;
        inputLine = in.readLine();
        in.close();
        return (inputLine);
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}