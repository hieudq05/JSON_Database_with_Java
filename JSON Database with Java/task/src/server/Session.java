package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Session extends Thread {
    private final Socket socket;
    private final Service service = Service.getInstance();

    public Session(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                DataInputStream inputStream = new DataInputStream(socket.getInputStream())
        ) {
            String requestJson = inputStream.readUTF();

            String responseJson = service.processCommand(requestJson);

            outputStream.writeUTF(responseJson);

            // Check if it's exit command
            if (requestJson.contains("\"type\":\"exit\"") || requestJson.contains("\"type\": \"exit\"")) {
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
