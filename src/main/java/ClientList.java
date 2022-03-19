import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ClientList
{
    private List<Socket> clientList;

    public ClientList(){
        clientList = new LinkedList<>();
    }

    public synchronized Socket take() throws InterruptedException {
        while(clientList.isEmpty()){
            wait();
        }
        Socket chosenClientHandler = clientList.get(0);
        clientList.remove(0);
        return chosenClientHandler;
    }

    public synchronized void put(Socket clientHandler){
        clientList.add(clientHandler);
        notifyAll();
    }
}
