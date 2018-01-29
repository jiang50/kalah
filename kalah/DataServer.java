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
                 /*   Socket socket = listener.accept();
                    try {
                        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                        System.out.println(new Date().toString());
                    } finally {
                        socket.close();
                    } */
                Game game = new Game();
                Game.Player player1 = game.new Player(listener.accept(), board[1][0]); //what's in place of mark?
                Game.Player player2 = game.new Player(listener.accept(), board[0][1]); //mark?
                player1.setOpponent(player2);
                player2.setOpponent(player1);
                game.currentPlayer = player1;
                player1.start();
                player2.start()

                }
            }
            finally {
                listener.close();
            }
        }
    }
class Game{
    hole = 0; //how decide?
    int board[][]=new int[2][hole+2];

    Player currentPlayer;
    public Boolean hasWinner(){} //check_result function from backend?
    public synchronized boolean legalMove(int location, Player player) {
        if (player == currentPlayer && board[location] == null) {
            board[location] = currentPlayer;
            currentPlayer = currentPlayer.opponent;
            currentPlayer.otherPlayerMoved(location);
            return true;
        }
        return false;
    }
    private void swap() {
        int temp=gamestate[0][0];
        gamestate[0][0]=gamestate[1][pits+1];
        gamestate[1][pits+1]=temp;
        for(int i=1;i<pits+1;i++) {
            int t=gamestate[0][i];
            gamestate[0][i]=gamestate[1][pits+1-i];
            gamestate[1][pits+1-i]=t;
        }
    }

}
 class Player extends Thread {
        char mark;
        Player opponent;
        Socket socket;
        BufferedReader input;
        PrintWriter output;

        /**
         * Constructs a handler thread for a given socket and mark
         * initializes the stream fields, displays the first two
         * welcoming messages.
         */
        public Player(Socket socket, int board[][]) {
            this.socket = socket;
            this.board = board;
            try {
                input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println("WELCOME PLAYER");
                output.println("Waiting for opponent to connect");
            } catch (IOException e) {
                System.out.println("Player died: " + e);
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
    //display message for first player
    //messages from client
    while(true){
        String input = in.readLine();
        String input_array[]= input.split(" ");
        //separate array
        if(input == null)
            break;
        if(input=="P")
           backend.swap();
        if(input.startsWith("MOVE"))
            //check if legal move
            //if illegal, say so
            else for(int j=1; j< input_array.length; ++j)
                backend.mouse_move(input_array[j]);
        for(int i=0; i< input_array.length; ++i)
            if(input_array[i]<1||input_array[i]>pits)
                out.println("ILLEGAL MOVE"); //do we call a game state here?
        else out.println("OK");
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






