package client;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;
import server.Args;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

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
            String request;
            JCommander.newBuilder()
                    .addObject(argsParams)
                    .build()
                    .parse(args);
            if (argsParams.getIn() != null) {
                try {
                    Path path = Paths.get(System.getProperty("user.dir") + "/src/client/data/" + argsParams.getIn());
                    request = new String(Files.readAllBytes(path));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                request = gson.toJson(argsParams);
            }
            outputStream.writeUTF(request);
            System.out.println("Sent: " + request);

            System.out.println("Received: " + inputStream.readUTF());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
