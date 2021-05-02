import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// implementing runnable helps me achieve multi-threading
public class HandleClient implements Runnable{
    private final Socket client;

    public HandleClient(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            handleClient(client);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // this method consumes request from a client and processes it
    private static void handleClient(Socket client) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));

        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while(!(line = br.readLine()).isBlank()) {
            requestBuilder.append(line + "\r\n");
           System.out.println(line);
        }
        String request = requestBuilder.toString();
        String[] requestLines = request.split("\r\n");
        String[] requestLine = requestLines[0].split(" ");
        String path = requestLine[1];


        Path filepath = getFilePath(path);
        if (Files.exists(filepath)) {
            String contentType = guessContentType(filepath);
            sendResponse (client, "HTTP/1.1 200 ok", contentType, Files.readAllBytes(filepath));
        } else {
            System.out.println("dfg");
            byte[] notFoundContent = "<h1> 404 not found </h1>".getBytes();
            sendResponse(client, "HTTP/1.1 404 Not Found", "text/html", notFoundContent);
        }
    }


    // this method processes the response to be sent
    public static void sendResponse (Socket client, String status, String contentType, byte[] content) throws IOException {
        System.out.println(status);
        OutputStream clientOutput = client.getOutputStream();
        clientOutput.write((status + "\r\n").getBytes());
        clientOutput.write(("ContentType: " + contentType + "\r\n" + "\r\n").getBytes());
        clientOutput.write("\r\n".getBytes());
        clientOutput.write(content);
        clientOutput.write("\r\n\r\n".getBytes());
        clientOutput.flush();
        client.close();
    }

    // this getter retrieves a filepath
    private static Path getFilePath(String path) {
        if ("/".equals(path)) {
            path = "src/main/resources/Demo.html";
        }
        else if ("/json".equals(path)) {
            path = "src/main/resources/demo.json";
        }
        return Paths.get(path);

    }

    // this method consumes a filepath and supplies a String correlating with a path if found
    private static String guessContentType (Path filepath) throws IOException {
        return Files.probeContentType(filepath);
    }
}
