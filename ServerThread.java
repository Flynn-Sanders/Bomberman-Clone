import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerThread implements Runnable{
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private Manager mg;

    private String username;

    public ServerThread(Socket s, Manager m){
        clientSocket=s; mg=m;
    }
    
    public void send(String data) {
        try {
            if (out != null) {
                out.writeObject(data);
                out.flush();
            }
        } catch (IOException e) {
            System.out.println("IO exception");
        }
        
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(clientSocket.getInputStream());
            while (true) {
                String message = (String) in.readObject();
            }
        }
        catch (IOException e) {
            System.out.println("IO EXCEPTION");
            e.printStackTrace();
        }
        catch(ClassNotFoundException e){
            System.out.println("CLASSNOTFOUND EXCEPTION");
            e.printStackTrace();
        }
    }
    

    public String getName() {
        return username;
    }

}
