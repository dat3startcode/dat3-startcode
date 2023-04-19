package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

public class ApiFacade {
    // Purpose of this class is to demo how to read data from an external API
    // and return it as a DTO
    public static void main(String[] args) throws IOException {
        Gson GSON = new GsonBuilder().create();
        String urlString = "https://api.dataforsyningen.dk/postnumre";
        URL yahoo = new URL(urlString);
        URLConnection yc = yahoo.openConnection();
        BufferedReader in = new BufferedReader( new InputStreamReader( yc.getInputStream()));
        String inputLine;
        StringBuilder sb = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
            sb.append(inputLine);
        in.close();
//        System.out.println(sb);
        MyDTO[] myDtos = GSON.fromJson(sb.toString(), MyDTO[].class);
        Arrays.stream(myDtos).forEach(dto->System.out.println(dto.nr + " " + dto.navn));
    }
    class MyDTO {
        public String nr;
        public String navn;
    }
}
