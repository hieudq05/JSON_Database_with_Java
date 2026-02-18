package client;

public class Main {
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 23456, args);
        client.start();
        try {
            client.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
