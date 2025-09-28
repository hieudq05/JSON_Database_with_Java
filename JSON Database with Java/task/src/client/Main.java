package client;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;
import server.Args;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Main {
    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 2610;
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(InetAddress.getByName(SERVER_HOST), SERVER_PORT);
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                DataInputStream inputStream = new DataInputStream(socket.getInputStream())
        ) {
            System.out.println("Client started!");

            Args argsParams = new Args();
            JCommander.newBuilder()
                    .addObject(argsParams)
                    .build()
                    .parse(args);

            String request = gson.toJson(argsParams);
            outputStream.writeUTF(request);
            System.out.println("Sent: " + request);

            System.out.println("Received: " + inputStream.readUTF());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
