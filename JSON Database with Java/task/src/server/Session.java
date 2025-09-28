package server;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Session extends Thread {
    private final Socket socket;
    private Database database;
    private AtomicBoolean running;
    private final Service service = Service.getInstance();
    private Gson gson = new Gson();

    public Session(Socket socket, Database database, AtomicBoolean running) {
        this.socket = socket;
        this.database = database;
        this.running = running;
    }

    @Override
    public void run() {
        try (
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                DataInputStream inputStream = new DataInputStream(socket.getInputStream())
        ) {
            String input = inputStream.readUTF();

            Args args = gson.fromJson(input, Args.class);
            Response response = service.processCommand(args, database);
            outputStream.writeUTF(gson.toJson(response));
            if (args.getType().equals("exit")) {
                running.set(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
