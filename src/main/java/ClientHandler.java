import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable
{
    private int clientId;
    private ClientList clients;
    private MessageList messages;
    private boolean running;
    PrintWriter writer;
    ObjectInputStream reader;

    public ClientHandler(ClientList clients, MessageList messages, int clientId) {
        this.clients = clients;
        this.messages = messages;
        this.clientId = clientId;
        this.running = true;
    }

    @Override
    public void run() {
        while(running){
            try {
                handleClient(clients.take());
            } catch (InterruptedException | IOException | ClassNotFoundException e) {
                running = false;
            }
        }
    }

    private void handleClient(Socket socket) throws IOException, ClassNotFoundException {
        initializeClientConnectionStream(socket);
        while(readClientResponse())
            ;
    }

    private void initializeClientConnectionStream(Socket socket) throws IOException {
        this.reader = new ObjectInputStream(socket.getInputStream());
        this.writer = new PrintWriter(socket.getOutputStream());
    }

    private boolean readClientResponse(){
        try {
            messageClient("ready");
            Object clientResponse = reader.readObject();
            if(clientResponse == null)
                return false;
            takeResponse(clientResponse);
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    private void takeResponse(Object clientResponse) throws IOException, ClassNotFoundException {
        if(clientResponse instanceof Integer){
            messageClient("ready for messages");
            takeMessages((Integer)clientResponse);
        }
    }

    private void takeMessages(int messageNumber) throws IOException, ClassNotFoundException {
        for(int i = 0; i < messageNumber; i++)
            takeMessage();
        messageClient("finished");
    }

    private void takeMessage() throws IOException, ClassNotFoundException {
        Message clientResponse = (Message)reader.readObject();
        messages.put(new OwnedMessage(clientResponse, clientId));
    }

    private void messageClient(String message){
        writer.println(message);
        writer.flush();
    }
}