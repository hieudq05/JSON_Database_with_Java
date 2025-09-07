package server;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Session extends Thread {
    private final Socket socket;
    private final Service service = Service.getInstance();
    private Gson gson = new Gson();

    public Session(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())
        ) {
            Object input = inputStream.readObject();

            Response response = service.processCommand(input);

            outputStream.writeUTF(gson.toJson(response));

            Args args = new Args();

            JCommander.newBuilder()
                    .addObject(args)
                    .build()
                    .parse((String[]) input);

            if (args.getType().equals("exit")) {
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
