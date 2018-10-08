import java.net.*;
import java.io.*;



public class server_test {

    public static void main(String[] args) throws Exception {

        int port = 1989;
        String base_url = "/Users/WillJia/Documents/Data structure/" +
                "socketCommunication/web/";
        String link_url = null;


        // repeatedly wait for connections, and process
        while (true) {

            ServerSocket serverSocket = new ServerSocket(port);
            System.err.println("Server reaching port : " + port);

            Socket clientSocket = serverSocket.accept();
            System.err.println("New client connected");


            BufferedReader in = new BufferedReader(new
                    InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new
                    OutputStreamWriter(clientSocket.getOutputStream()));

            String s;
            int index = 0;
            while ((s = in.readLine()) != null) {
                System.out.println(s);
                if (s.isEmpty()) {
                    break;
                }else if (index == 0){
                    link_url = s.substring(5, s.indexOf("HTTP") - 1);
                    System.out.println("URL is " + base_url + link_url);
                }
                index++;
            }
            out.write("HTTP/1.1 200 OK\r\n");
            out.write("Date: Sun, 07 Oct 2018 22:21:41 GMT\r\n");
            out.write("Server: Apache/2.0.52\r\n");
            out.write("Last-Modified: Tue, 30 Oct 2007 17:00:02 GMT\r\n");
            out.write("Content-Type: text/html\r\n");
            out.write("Content-Length: 59\r\n");
            out.write("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n");
            out.write("Last-modified: Fri, 09 Aug 1996 14:21:40 GMT\r\n");
            out.write("\r\n");

            InputStream inputStream = new FileInputStream(base_url + link_url);
            OutputStream outputStream = clientSocket.getOutputStream();
            byte[] buffer = new byte[4 * 1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();


            System.err.println("Connection to the client over");
            serverSocket.close();
            clientSocket.shutdownInput();
            clientSocket.close();
            inputStream.close();
            outputStream.close();
            out.close();
            in.close();

        }
    }
}
