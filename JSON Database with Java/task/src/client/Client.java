package client;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Client extends Thread {
    private final String host;
    private final int port;
    private final String[] args;
    private static final Gson gson = new GsonBuilder().create();

    public Client(String host, int port, String[] args) {
        this.host = host;
        this.port = port;
        this.args = args;
    }

    @Override
    public void run() {
        server.Args argsParams = new server.Args();
        JCommander.newBuilder()
                .addObject(argsParams)
                .build()
                .parse(args);

        try (
                Socket socket = new Socket(InetAddress.getByName(host), port);
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                DataInputStream inputStream = new DataInputStream(socket.getInputStream())
        ) {
            System.out.println("Client started!");

            String requestJson;

            // Check if reading from file
            if (argsParams.getInputFile() != null) {
                String filePath = System.getProperty("user.dir") + "/src/client/data/" + argsParams.getInputFile();
                requestJson = new String(Files.readAllBytes(Paths.get(filePath)));
            } else {
                // Build request from command-line arguments
                Request request = new Request(
                        argsParams.getType(),
                        argsParams.getKey() != null && argsParams.getKey().length > 1 ? argsParams.getKey() : argsParams.getKeyString(),
                        argsParams.getValue()
                );
                requestJson = gson.toJson(request);
            }

            // Print sent message
            System.out.println("Sent: " + requestJson);

            outputStream.writeUTF(requestJson);

            String responseJson = inputStream.readUTF();
            System.out.println("Received: " + responseJson);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
