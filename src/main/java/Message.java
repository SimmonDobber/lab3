import java.io.Serializable;

public class Message implements Serializable
{
    private static int idCounter;
    protected Integer id;
    protected String content;

    public Message(String content){
        this.id = getNextId();
        this.content = content;
    }

    private int getNextId(){
        return ++idCounter;
    }

    public Integer getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
