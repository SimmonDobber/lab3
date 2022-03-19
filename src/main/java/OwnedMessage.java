public class OwnedMessage extends Message
{
    private int ownerId;

    public OwnedMessage(Message message, int ownerId){
        super(message.getContent());
        this.id = message.getId();
        this.ownerId = ownerId;
    }

    public int getOwnerId() {
        return ownerId;
    }
}
