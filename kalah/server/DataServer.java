import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Timer;

//import Backend.mode;
public class DataServer {

    //server

        public static void main(String[] args) throws IOException {
            ServerSocket listener = new ServerSocket(7896);
            System.out.println("Kalah server is running");
            try {
                while(true) {
                	boolean rand_setup=true;
					Backend game = new Backend(8, 4, 50000,rand_setup , Backend.mode.one_player, Backend.level.easy);
                   Socket socket = listener.accept();
                    try {
                        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                        System.out.println(new Date().toString());
                    } finally {
                        socket.close();
       /*       Game.Player player1 = game.new Player(listener.accept(), board[1][0]);
                Game.Player player2 = game.new Player(listener.accept(), board[0][1]);
               	player1.setOpponent(player2);
               	player2.setOpponent(player1);
               	game.currentPlayer = player1;
                player1.start();
                player2.start(); 

*/
                }
            }
            
              
            
        }finally {
        	listener.close();
        }
    } 
                
            
static class Game{

    private int[][] board_state;
    private int pit_num;
    private int seed_num;
    private boolean rand_setup = true;
    private boolean player_turn;
    private String level_difficulty;
    private Backend game;
    Player currentPlayer;
    public enum mode {two_player, one_player, two_computer}
	private mode m=mode.two_player;
	public enum level {easy,medium,hard}
	private level lv=level.easy;
   
    
    
    
    
     
               // Timer timer = new Timer();
    

  
   /* public Boolean hasWinner(){} //check_result function from backend?
    public synchronized boolean legalMove(int boardstate, Player player) {
        if (player == currentPlayer && board[location] == null) {
            board[location] = currentPlayer;
            currentPlayer = currentPlayer.opponent;
            currentPlayer.otherPlayerMoved(location);
            return true;
        }
        return false;  
    }*/
  
    
	public void swap() {
  /*      int temp=gamestate[0][0];
        gamestate[0][0]=gamestate[1][pits+1];
        gamestate[1][pits+1]=temp;
        for(int i=1;i<pits+1;i++) {
            int t=gamestate[0][i];
            gamestate[0][i]=gamestate[1][pits+1-i];
            gamestate[1][pits+1-i]=t;
        }
    }
*/
}
 class Player extends Thread {
        Player opponent;
        Socket socket;
        BufferedReader input;
        PrintWriter output;
		private int[][] board;

        /**
         * Constructs a handler thread for a given socket and mark
         * initializes the stream fields, displays the first two
         * welcoming messages.
         */
        public Player(Socket socket, int board[][]) {
            this.socket = socket;
            this.board = board_state;
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
/*
class ThreadHandler extends Thread{
    private Socket socket;
    private int client;

    public ThreadHandler(Socket socket, int client){
        this.socket = socket;
        this.client = client;
        System.out.println("New connection");
    }

*/

public void run(){
    try{
    BufferedReader in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    out.println("Welcome");
    //display message for first player
    //messages from client
    while(true){
                long startTime = System.currentTimeMillis();
                long endTime = startTime + 5000;

          //      System.out.println(backend.getTurn() + "'s turn!");
          //o      game.updateButtons(backend);


             /*   while(System.currentTimeMillis() < endTime )
                {
                    System.out.println("IN TIME LOOP: " + System.currentTimeMillis());

                    if((Backend.isAi() == true) && (Backend.getTurn() == "P2"))
                    {
                        AI.positionPS(backend);
                        System.out.println("AI TURN");
                        break;
                    }
                    else
                    {

                        while(backend.getplayerWait() == true)
                        {*/
                            if(System.currentTimeMillis() > endTime)
                            {
                                System.out.println("TIMEOUT");
                                break;
                            }
                        
                  

        //            }


            } 
    	board_state = game.get_board();
        String input = in.readLine();
        String input_array[]= input.split(" ");
        //separate array
        if(input == null)
            out.println("ERROR");
        if(input=="P")
           swap();
        int result;
        if(input.startsWith("MOVE")){
        	for(int k=1; k< input_array.length; ++k){
        		result = Integer.parseInt(input_array[k]);
        		if(game.mouse_move(result)) //detail for player
        			out.println("OK");
        		else out.println("ILLEGAL MOVE"); //check if legal move
        	}
            //if illegal, say so
        }
        else for(int j=1; j< input_array.length; ++j){
        		result = Integer.parseInt(input_array[j]);
                game.mouse_move(result);
     /*   for(int i=0; i< input_array.length; ++i)
            if(input_array[i]<1||input_array[i]>pits)
                out.println("ILLEGAL MOVE"); //
        else out.println("OK"); */
    }
    }

catch(IOException e){
    //log??
    out.println("Error handling" + opponent + ":" + e);
}
finally {
    try{
        socket.close();
    } catch(IOException e){
        System.out.println("problem");
    }finally{
    System.out.println("connection with " + opponent + "closed");
    		}
		}
    }

 }
}
}





