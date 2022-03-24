package week3.netty.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// 单线程的socket程序
public class HttpServer01 {
    public static void main(String[] args) throws IOException {
        // TODO: socket 知识需要补充
        ServerSocket serverSocket = new ServerSocket(8801);
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                service(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void service(Socket socket) {
        try (final BufferedReader buf = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            // TODO: 字符流知需要补充
//            String str = null;
//            while ((str = buf.readLine()) != null) {
//                System.out.println(str);
//            }
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            // 添加试试
            printWriter.println("Connection: keep-alive");
            String body = "hello,nio1,8801";
            // 一定要告诉他长度
            printWriter.println("Content-Length: " + body.getBytes().length);
            printWriter.println();
            printWriter.write(body);
//            Thread.sleep(1000);
            printWriter.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}