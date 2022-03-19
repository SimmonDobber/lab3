import java.util.LinkedList;
import java.util.List;

public class MessageList
{
    private List<OwnedMessage> messageList;

    public MessageList(){
        messageList = new LinkedList<>();
    }

    public synchronized void put(OwnedMessage message){
        messageList.add(message);
        System.out.println("Client " + message.getOwnerId() + ": " + message.getContent());
    }
}
