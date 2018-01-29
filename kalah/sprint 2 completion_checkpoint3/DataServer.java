import static java.lang.System.out;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
public class DataServer {

    //server

        public static void main(String[] args) throws IOException {
            ServerSocket listener = new ServerSocket(7896);
            System.out.println("Kalah server is running");
            try {
                while (true) {
                    Socket socket = listener.accept();
                    try {
                        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                        System.out.println(new Date().toString());
                    } finally {
                        socket.close();
                    }
                }
            }
            finally {
                listener.close();
            }
        }
    }
class ThreadHandler extends Thread{
    private Socket socket;
    private int client;

    public ThreadHandler(Socket socket, int client){
        this.socket = socket;
        this.client = client;
        System.out.println("New connection");
    }



public void run(){
    try{
    BufferedReader in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    out.println("Welcome");

    //messages from client
    while(true){
        String input = in.readLine();
        if(input == null)
            break;
        out.println(input); //do we call a game state here?

    }
    }

catch(IOException e){
    //log??
    out.println("Error handling" + client + ":" + e);
}
finally {
    try{
        socket.close();
    } catch(IOException e){
        System.out.println("problem");
    }
    System.out.println("connection with " + client + "closed");

}
}
}
