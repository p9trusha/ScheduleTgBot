import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

class EtuApi {
    static String getJson() throws IOException {
        String apiLink = "https://digital.etu.ru/api/mobile/schedule";
        return IOUtils.toString(URI.create(apiLink).toURL(), StandardCharsets.UTF_8);
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        String jsonContent = EtuApi.getJson();
        HashMap<String, Group> schedule = Parser.json(jsonContent);
    }
}
