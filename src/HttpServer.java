
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class HttpServer {
    private static final Integer port = 1989;
    
    public static void main(String[] args) {
        ServerSocket serverSocket;
        
        try {
            //Set up server socket, and start listening

            serverSocket = new ServerSocket(port);

            System.out.println("Server is running on port:"+serverSocket.getLocalPort());
            // Enter while loop for listening without stop

            while(true){
                final Socket socket = serverSocket.accept();
                System.out.println("establish TCP connection ,the " +
                        "client" +
                        " address is:"+
                        socket.getInetAddress()+":"+socket.getPort());

                //start dealing with client request
                service(socket);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void service(Socket socket)
    {
        new Thread(){
            public void run(){

                InputStream inSocket;

                try {

                    //Receive header of http request.

                    inSocket = socket.getInputStream();
                    int size = inSocket.available();
                    byte[] buffer = new byte[size];
                    inSocket.read(buffer);
                    String request = new String(buffer);

                    System.out.println("ClientBrowser:\n"+request+"\n"
                            + "------------------------------------------------------------------");
                    
                    String firstLineOfRequest = "";

                    String[] heads;
                    String uri = "/index.html";
                    String contentType ="";

                    if(request.length() > 0){
                        firstLineOfRequest = request.substring(0,request.indexOf("\r\n"));
                        heads = firstLineOfRequest.split(" ");
                        uri = heads[1];
                        
                        if(uri.indexOf("html") != -1){
                            contentType = "text/html";
                        }else{
                            contentType = "application/octet-stream";
                        }
                    }
                    //将响应头发送给客户端
                    String responseFirstLine = "HTTP/1.1 200 OK\r\n";
                    
                    String responseHead = "Content-Type:" + contentType +"\r\n";
                    
                    OutputStream outSocket = socket.getOutputStream();
                    System.out.println("ServerResponse:\n"+responseFirstLine+"\n"+responseHead+"\n"
                            + "--------------------------------------------------------------------");
                    outSocket.write(responseFirstLine.getBytes());
                    outSocket.write(responseHead.getBytes());

                    FileInputStream writehtml =
                            new FileInputStream(new File("/Developer/html" +
                                    "/error.html"));

                    //通过HTTP请求中的uri读取相应文件发送给客户端
                    try {
                        writehtml =
                                new FileInputStream(new File("/Developer/html/" +uri));
                    }catch (IOException e){
                        e.printStackTrace();
                    }


                    outSocket.write("\r\n".getBytes());  
                    byte[] htmlbuffer = new byte[writehtml.available()];

                    if(writehtml !=null){
                        int len = 0;
                        System.out.println("writeHtml");
                        while((len = writehtml.read(htmlbuffer)) != -1){
                            outSocket.write(htmlbuffer, 0,len);
                        }
                    }

                    outSocket.close();
                    socket.close();
                
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }.start();
    }

}
