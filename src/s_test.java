import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class s_test {


        public static void main(String[] args) {

            String base_url = "/Users/WillJia/Documents/Data structure/" +
                    "socketCommunication/web/";

            while (true) {
                try {
                    ServerSocket serverSocket = new ServerSocket(1989);
                    System.out.println("正在等待情书中...");
                    Socket socket = serverSocket.accept();
                    System.out.println("收到情书，我要开始解析！");
                    InputStream inputStream = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inputStream));
                    String line = reader.readLine();
                    System.out.println(line);
                    String url = line.substring(5, line.indexOf("HTTP") - 1);
                    System.out.println("url is " + url);
                    System.out.println("情书解析完毕，我要想想怎么回复了...");


                    // 获取文件内容
                    inputStream = new FileInputStream(base_url + url);
                    OutputStream outputStream = socket.getOutputStream();
                    
                    byte[] buffer = new byte[4 * 1024];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        System.out.println(Arrays.toString(buffer));
                        outputStream.write(buffer, 0, len);
                    }
                    outputStream.flush();

                    System.out.println("情书请求已发送给客户端");

                    //关闭对应的资源
                    serverSocket.close();
                    socket.shutdownInput();
                    socket.close();
                    inputStream.close();
                    reader.close();
                    outputStream.close();
                } catch (Exception e) {
                }
            }
        }
    }


