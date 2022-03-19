import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private static final int THREAD_NUMBER = 2;
    private static final int PORT = 1234;
    private static ServerSocket socket;
    private static ClientList clients;
    private static MessageList messages;
    private static Thread[] threads;
    private static int clientIdCounter;

    public static void main(String[] args) throws IOException {
        socket = new ServerSocket(PORT);
        clients = new ClientList();
        messages = new MessageList();
        threads = initializeThreads();
        run();
    }
    private static void run(){
        boolean running = true;
        while(running){
            try {
                Socket client = socket.accept();
                clients.put(client);
            } catch (IOException e) {
                running = false;
            }
        }
    }

    private static Thread[] initializeThreads(){
        Thread[] threads = new Thread[THREAD_NUMBER];
        for(Thread thread : threads) {
            thread = new Thread(new ClientHandler(clients, messages, getNextClientId()));
            thread.start();
        }
        return threads;
    }

    private static int getNextClientId(){
        return ++clientIdCounter;
    }
}
