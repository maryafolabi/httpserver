
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// this class houses my main method that enables my code run
public class WebServer {

    public static void main(String[] args) throws Exception{
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                try  {
                    Socket client = serverSocket.accept();
                    HandleClient handleClient = new HandleClient(client);
                    Thread thread = new Thread(handleClient);
                    thread.start();
                }catch (IOException e){
                    System.out.println(e.getStackTrace());
                }
            }
        }
    }
}
