/*
 * Author   : Zesheng Jia
 * A number : A00416452
 *
 *
 * Reference:
 * 1. https://www.jianshu.com/p/eea234c157e2
 * 2. https://stackoverflow.com/questions/3732109/simple-http-server-in-
 * java-using-only-java-se-api
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private static final Integer port = 1989;

    public static void main(String[] args) {
        ServerSocket serverSocket;
        try {
            //Set up server socket, and start listening

            serverSocket = new ServerSocket(port);
            System.out.println("Server is running on port:"
                    + serverSocket.getLocalPort());

            // Enter while loop for listening without stopping
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
        // set original file path is the local path
//        String file_path = "/Developer/html/";
//        String file_path = "/home/student/z_jia/html/";
        // use Thread to deal with concurrence
        new Thread(){
            public void run(){
                InputStream inSocket;
                try {

                    // Receive header of http request.
                    // use inputStream and buffer to save request header

                    inSocket = socket.getInputStream();
                    int size = inSocket.available();
                    byte[] buffer = new byte[size];
                    inSocket.read(buffer);
                    // convert byter array as string
                    String request = new String(buffer);

                    // print request header
                    System.out.println("Client request header:\n"+request+"\n"
                            + "--------------------------------------------" +
                            "----------------------");

                    String firstLine = "";

                    String[] heads;
                    String url = "/index.html";
                    String contentType ="";

                    if(request.length() > 0){
                        System.out.println("Check file name");
                        firstLine = request.substring(0,
                                request.indexOf("\r\n"));
                        // split first GET line to find target file
                        heads = firstLine.split(" ");
                        // save file name as head
                        url = heads[1];

                        if(url.contains("html")){
                            // set response type is text/html
                            contentType = "text/html";
                        }else{
                            contentType = "application/octet-stream";
                        }
                    }

                    System.out.println("client request html file: " + url);
                    // send response header back to client
                    // notice HTTP/0.9 won't work
                    String responseFirstLine = "HTTP/1.1 200 OK\r\n";

                    String responseHead = "Content-Type:" +
                            contentType +"\r\n";

                    OutputStream outputStream = socket.getOutputStream();

                    // print out response in console
                    System.out.println("ServerResponse:\n"
                            + responseFirstLine + "\n" + responseHead+"\n"
                            + "------------------------------------------" +
                            "--------------------------");

                    // write response header to client
                    outputStream.write(responseFirstLine.getBytes());
                    outputStream.write(responseHead.getBytes());


                    // set default html file as error.html
                    // read file from server path
                    FileInputStream targetHtml =
                            new FileInputStream(new File("/Developer/html/" +
                                    "error.html"));

                    //get url with file path
                    try {
                        // default as error html : 404 not found.
                        // if url is not empty then try to open user request
                        // html file
                        targetHtml =
                                new FileInputStream(new File(
                                        "/Developer/html" + url));
                    }catch (IOException e){
                        // if failed than targetHtml still will be error.html
                        e.printStackTrace();
                    }

                    outputStream.write("\r\n".getBytes());
                    byte[] contentBuffer = new byte[targetHtml.available()];

                    // write content in file back to client
                    int len = 0;
                    System.out.println("sending html page source code");

                    while((len = targetHtml.read(contentBuffer)) != -1){
                        outputStream.write(contentBuffer, 0,len);
                    }

                    // close stream
                    outputStream.close();
                    // close socket
                    socket.close();
                    inSocket.close();
                    targetHtml.close();

                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }.start();
    }

}

