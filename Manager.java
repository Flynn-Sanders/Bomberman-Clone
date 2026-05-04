import java.util.ArrayList;

public class Manager {
    private ArrayList<ServerThread> servers;
    private ServerScreen sc;

    private int numReady;

    public Manager(ServerScreen screen){
        sc=screen;
        servers = new ArrayList<>();
        numReady = 0;

    }
    
    public void addThread(ServerThread thread){
        servers.add(thread);
    }
    public void broadcast(String message) {
        for(ServerThread thread : servers) {
            thread.send(message);
        }
        sc.addMessage(message);
    }

    public void readyUp(){
        numReady++;
        broadcast(numReady+" out of "+servers.size()+" ready.");
        if(numReady>=servers.size()) startGame();
    }

    

    public void startGame() {
        numReady=0;
    }
    public void endGame(){ 
        numReady = 0;
    }
}
