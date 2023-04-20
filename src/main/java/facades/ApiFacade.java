package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

public class ApiFacade {
    // Purpose of this class is to demo how to read data from an external API
    // and return it as a DTO
    public static void zipRequester() throws IOException {
        Gson GSON = new GsonBuilder().create();
        String urlString = "https://api.dataforsyningen.dk/postnumre";
        URL yahoo = new URL(urlString);
        URLConnection yc = yahoo.openConnection();
        yc.setRequestProperty("Accept", "application/json");
        BufferedReader in = new BufferedReader( new InputStreamReader( yc.getInputStream()));
        String inputLine;
        StringBuilder sb = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
            sb.append(inputLine);
        in.close();

//      System.out.println(sb); // Uncomment to see the raw JSON data. This will help you to create the DTO properly!!
        ZipDTO[] myDtos = GSON.fromJson(sb.toString(), ZipDTO[].class);
        Arrays.stream(myDtos).forEach(dto->System.out.println(dto.nr + " " + dto.navn));
    }
    class ZipDTO {
        public String nr;
        public String navn;
    }

    class  JokeDTO {
        JsonObject[] body;
    }

    class DataDTO{
        public JsonObject data;
    }

    public static String jokesAPI(String apiKey) throws IOException {
        // Purpose of this example is to show an atypical json response (dealing with an array and JsonObject)
        // In order to run REGISTER TO GET API KEY HERE: https://rapidapi.com/KegenGuyll/api/dad-jokes/
        Gson GSON = new GsonBuilder().create();
        String urlString = "https://dad-jokes.p.rapidapi.com/random/joke";
        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();

        urlConnection.setRequestProperty("X-RapidAPI-Key", apiKey);
        urlConnection.setRequestProperty("X-RapidAPI-Host", "dad-jokes.p.rapidapi.com");

        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String inputLine;
        StringBuilder sb = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
            sb.append(inputLine);
        System.out.println(sb);
        in.close();

        JokeDTO myDTO = GSON.fromJson(sb.toString(), JokeDTO.class);
        System.out.println(myDTO.body[0].get("setup"));

        return myDTO.body[0].get("setup").toString() + " " + myDTO.body[0].get("punchline").toString();
    }

    public static void main(String[] args) throws Exception {
        try {
//            zipRequester();
//            jokesAPI("89f34a559fmsh6e84c8889eb98bfp12d8bejsn157e59c6bca6");
            String url = "https://api.jikan.moe/v4/random/anime";
            String response = getHttpResponse(url);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getHttpResponse(String url) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("accept", "application/json")
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // {"data":{"mal_id":18771,"url":"https:\/\/myanimelist.net\/anime\/18771\/Gifuu_Doudou__Kanetsugu_to_Keiji","images":{"jpg":{"image_url":"https:\/\/cdn.myanimelist.net\/images\/anime\/5\/51427.jpg",
        System.out.println(response.body().toString());

        DataDTO dataDTO = new Gson().fromJson(response.body(), DataDTO.class);
        JsonObject jo = dataDTO.data;
        return jo.get("images").getAsJsonObject().get("jpg").getAsJsonObject().get("image_url").getAsString();
    }
}
