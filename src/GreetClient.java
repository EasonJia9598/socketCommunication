
import java.net.*;
import java.io.*;


public class GreetClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
 
    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
 
    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        String resp = in.readLine();
        return resp;
    }
 
    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
    public void givenGreetingClient_whenServerRespondsWhenStarted_thenCorrect() throws IOException {
        GreetClient client = new GreetClient();
        client.startConnection("127.0.0.1", 6666);
        String response = client.sendMessage("index.html");
        System.out.println(response);
        stopConnection();
    }

    public static void main(String[] args) {
        GreetClient client = new GreetClient();
        try {
            client.givenGreetingClient_whenServerRespondsWhenStarted_thenCorrect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}