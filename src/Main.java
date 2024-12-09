import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

class EtuApi {
    String getJson() throws IOException {
        String apiLink = "https://https://digital.etu.ru/api/mobile/schedule";
        URL searchRequest = URI.create(apiLink).toURL();
        URLConnection urlCon = searchRequest.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
        String inputLine;
        inputLine = in.readLine();
        in.close();
        return inputLine;
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}
