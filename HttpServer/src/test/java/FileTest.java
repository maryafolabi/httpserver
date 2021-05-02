import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class
FileTest {

    @Test
    public void FileTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expected = Files.readString(Paths.get("src/main/resources/Demo.html"))
                .replaceAll("\\s", "");
        assertEquals(expected, response.body().replaceAll("\\s", ""));
    }

    @Test
    void FileTest2() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/json")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expected = Files.readString(Paths.get("src/main/resources/demo.json"))
                .replaceAll("\\s", "");
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected, response.body().replaceAll("\\s", "")));
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    void FileTest3() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/fg")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expected = "<h1> 404 not found </h1>"
                .replaceAll("\\s", "");
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected, response.body().replaceAll("\\s", "")));
        Assertions.assertEquals(404, response.statusCode());
    }

}
