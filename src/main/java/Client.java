import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    private static Socket socket;
    private static BufferedReader reader;
    private static ObjectOutputStream writer;
    private static Scanner scanner;
    private static int messageNumber;

    public static void main(String[] args) throws IOException {
        socket = new Socket("localhost", 1234);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new ObjectOutputStream(socket.getOutputStream());
        scanner = new Scanner(System.in);
        messageNumber = 0;
        run();
    }

    private static void run() {
        boolean running = true;
        while(running){
            try {
                String serverResponse = reader.readLine();
                if(serverResponse == null){
                    System.out.println("Connection lost");
                    running = false;
                }
                else if(isServerReady(serverResponse))
                    sendMessageNumber();
                else if(isServerReadyForMessages(serverResponse))
                    sendMessages();
            } catch (IOException e) {
                System.out.println("Disconnected");
                running = false;
            }
        }
    }



    private static void sendMessageNumber() throws IOException {
        System.out.println("Server prepared for message number");
        while(!scanner.hasNextInt()){
            if(scanner.next().equals("end"))
                throw new IOException();
        }
        messageNumber = scanner.nextInt();
        writer.writeObject(messageNumber);
    }

    private static void sendMessages() throws IOException {
        System.out.println("Server prepared for " + messageNumber + " messages");
        for(int i = 0; i < messageNumber; i++){
            Message message = new Message(scanner.next());
            writer.writeObject(message);
        }
        completeSending();
    }

    private static void completeSending() throws IOException {
        messageNumber = 0;
        String serverResponse = reader.readLine();
        if(hasServerGotMessages(serverResponse))
            System.out.println("Server received messages");
    }

    private static boolean isServerReady(String serverResponse){
        return serverResponse.equals("ready");
    }

    private static boolean isServerReadyForMessages(String serverResponse){
        return serverResponse.equals("ready for messages");
    }

    private static boolean hasServerGotMessages(String serverResponse){
        return serverResponse.equals("finished");
    }
}
