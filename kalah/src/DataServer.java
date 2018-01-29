

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Timer;

import javax.swing.JOptionPane;

import java.util.Scanner;

//import Backend.mode;
public class DataServer {
	
    //server

    public DataServer(int in_pits,int in_seeds,int in_time,boolean in_rand,String in_server_ip,Integer in_server_port) throws IOException {
        ServerSocket listener = new ServerSocket(in_server_port);
        System.out.println("Kalah server is running");
        try {
				Game my_game = new Game(in_pits, in_seeds, in_time,in_rand , Game.mode.two_player, Game.level.easy);
                Game.Player player1 = my_game.new Player(listener.accept(), "F");
                Game.Player player2 = my_game.new Player(listener.accept(), "S");
               	player1.opponent=player2;
               	player2.opponent=player1;
               	my_game.currentPlayer = player1;
                player1.start();
                player2.start(); 

        }
	    finally {
	    	listener.close();
	    }
    } 
}               
            
class Game{
		private int pits=6;
		private int seeds=4;
		private int[][] gamestate;
		private long time=0;
		public Player currentPlayer;
		
		public enum mode {two_player, one_player, two_computer}
		private mode m=mode.two_player;
		public enum level {easy,medium,hard}
		private level lv=level.easy;
		private boolean gameover = false;
		private boolean tie = false;
		private boolean winner = true;//true for player1
		private boolean turn = true;//true for player1
		private boolean is_rand=false;
		public Game(int hole, int seed, long t, boolean rand,mode md,level l) {//game configuration
			time=t;
			m=md;
			pits=hole;
			seeds=seed;
			lv=l;
			is_rand=rand;
			int board[][]=new int[2][hole+2];
			board[0][0]=0;
			board[1][hole+1]=0;
			
			for(int i=0;i<2;i++)
				for(int j=1;j<hole+1;j++) {
					board[i][j]=seed;
				}
			if(rand) {
				for(int j=1;j<30;j++) {
					int n=(int) (Math.random() * hole)+1;
					int m=(int) (Math.random() * hole)+1;
					if(board[0][n]>0) {
						board[0][n]--;
						board[0][m]++;
						board[1][hole+1-n]--;
						board[1][hole+1-m]++;
					}			
				}
			}
			gamestate=board;
			
			
		}
		
		public void printboard() {
			for(int i=0;i<pits+1;i++) {
				System.out.print(gamestate[0][i]);
				System.out.print(" ");
			}
			System.out.println("  ");
			System.out.print("  ");
			for(int i=1;i<pits+2;i++) {
				System.out.print(gamestate[1][i]);
				System.out.print(" ");
				
			}
			System.out.println(" ");
			
		}
		
		public void pie_rule() {//player 2's choice to swap
			swap();
			System.out.println("swap");
			turn=true;
			currentPlayer=currentPlayer.opponent;
			
		}
		
		public void swap() {
			int temp=gamestate[0][0];
			gamestate[0][0]=gamestate[1][pits+1];
			gamestate[1][pits+1]=temp;
			for(int i=1;i<pits+1;i++) {
				int t=gamestate[0][i];
				gamestate[0][i]=gamestate[1][pits+1-i];
				gamestate[1][pits+1-i]=t;
			}
		}
		
		
		public synchronized String mouse_move(int i) {
			String return_string;
			if(turn) {
				return_string=basic_move(i);//player 1 move
			}
			else {
				swap();
				return_string=basic_move(pits+1-i);
				swap();
			}
			if(check_end()) {
				check_result();
				return return_string;
			}
			else return return_string;
		}
		
		public String basic_move(int i) {
			int j=1;
			String return_string="";
			if(i<1||i>pits||gamestate[j][i]==0) {
				System.out.println("illegal move");
				return_string="ILLEGAL";
			}
			else {
				int total=gamestate[1][i];
				gamestate[1][i]=0;
				
				while(total>0) {
					if(j==1&&i<pits+1) {
						gamestate[j][i+1]++;
						total--;
						i++;
					}
					else if(j==1&&i==pits+1) {
						j=0;
						i=pits;
						gamestate[j][i]++;
						total--;
						
					}
					else if(j==0&&i==1) {
						j=1;
						i=1;
						gamestate[1][1]++;
						total--;
						
					}
					else if(j==0&&i>1) {
						gamestate[j][i-1]++;
						total--;
						i--;
					}
				}
			}
//			System.out.println(j);
//			System.out.println(i);
			if(j!=1||i!=pits+1) {//capture
				currentPlayer=currentPlayer.opponent;
				turn=!turn;
				if(j==1&&gamestate[1][i]==1&&gamestate[0][i]!=0) {
					gamestate[1][pits+1]=gamestate[1][pits+1]+1+gamestate[0][i];
					gamestate[1][i]=0;
					gamestate[0][i]=0;
				}
			}
//			

			return return_string;
			
		}
		
		public boolean check_end() {//check if player 1 or 2 cannot move
			int p1=0;
			int p2=0;
			for(int i=1;i<pits+1;i++) {
				p1+=gamestate[1][i];
				p2+=gamestate[0][i];
			}
			return p1==0||p2==0;
		}
		
		public void check_result() {//count the number of seeds on both side, decide winner
			int p1=gamestate[1][pits+1];
			int p2=gamestate[0][0];
			for(int i=1;i<pits+1;i++) {
				p1+=gamestate[1][i];
				p2+=gamestate[0][i];
			}
			if(p1==p2)tie=true;
			else if(p1>p2) {
				winner=true;
			}
			else {
				winner=false;
			}
		}	
		public synchronized int[][] get_board()
		{
			return gamestate;
		}
		public Boolean is_player_one_turn()
		{
			return turn;
		}
		public void set_player_turn(Boolean in_turn)
		{
			turn=in_turn;
		}
		public Boolean is_player_one_winner()
		{
			return winner;
		}
		public synchronized void set_board(int[][] in_board)
		{
			gamestate=in_board;
		}
		public int AI_move()
		{
			int i=-1;
			int[][] clonestate=new int[2][pits+2];
			for(int j=0;j<2;j++) {
				System.arraycopy(gamestate[j],0,clonestate[j],0,pits+2);
			}
			int numpit=pits;
			state st=new state(clonestate,numpit);
			if(lv==level.easy)i=AI.easy(st);
			else if(lv==level.medium)i=AI.easy(st);
			else i=AI.hard(st,time);
			
			return i;
		}

	class Player extends Thread {
        public Player opponent;
        Socket socket;
        BufferedReader in;
        PrintWriter out;
        String player_order;

        /**
         * Constructs a handler thread for a given socket and mark
         * initializes the stream fields, displays the first two
         * welcoming messages.
         */
        public Player(Socket socket, String player_order) {
            this.socket = socket;
            this.player_order=player_order;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("WELCOME");
                out.println("Waiting for opponent to connect");
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            }
        }
        //helper function to determine if a move was received
      	public boolean isNumeric(String str)  
  		{
  		  try  
  		  {  
  		    double d = Double.parseDouble(str);  
  		  }  
  		  catch(NumberFormatException nfe)  
  		  {  
  		    return false;  
  		  }  
  		  return true;  
  		}
      	
      	public void otherPlayerMoved(String move)
      	{
      		System.out.println("sending to other player: "+move);
      		out.println(move);
      	}
        public void run(){
			try{
			    //sending info to players
			    if(!is_rand)
			    	out.println("INFO "+pits+" "+seeds+" "+time+" "+player_order+" S"); 
			    else
			    {
			    	String send_board_state="";
			    	for(int i=1;i<gamestate[1].length-1;++i)
			    	{
			    		send_board_state=send_board_state+" "+gamestate[1][i];
			    	}
			    	out.println("INFO "+pits+" "+seeds+" "+time+" "+player_order+" R"+send_board_state); 
			    }
			    //keep on getting input from client
			    while(true)
			    {
			        String input = in.readLine();
			        System.out.println(input);
			        if(input!=null)
			        {
				        String input_array[]= input.split(" ");
				        if((is_player_one_turn()&&player_order=="F")||(!is_player_one_turn()&&player_order=="S"))
					        //separate array
					        if(input == null)
					            out.println("ERROR");
					        //THIS HANDLES PIE RULES SENT BY CLIENT
					        if(input.startsWith("P")&&player_order=="S")
					        {
					           pie_rule();
					           opponent.otherPlayerMoved("P");
					        }
					        
					        //THIS HANDLES THE MOVE COMMAND SENT BY CLIENT
					        //the move command is in the format of a string of numbers (Ex. "2 3 4")
					        if(isNumeric(input_array[0].trim())){
					        	String send_moves="";
					        	
					        	for(int k=0; k< input_array.length; ++k){
					        		//checking if the game is over
					        		if(!check_end())
					        		{
						        		int curr_move = Integer.parseInt(input_array[k]);
						        		
					        			System.out.println("Processing move: "+curr_move);
					        			String str_illegal=mouse_move(curr_move);
					        			if(str_illegal=="ILLEGAL")
					        			{
					        				out.println("ILLEGAL");
					        				opponent.otherPlayerMoved("WINNER");
					        				break;
					        			}
					        			send_moves=send_moves+curr_move+" ";
					        			printboard();
					        		}
					        		out.println("OK");
					        		//determining who won the game
					        		if(check_end())
					        		{
					        			check_result();
					        			if(is_player_one_winner()&&player_order=="F")
					        			{
					        				out.println("WINNER");
					        				opponent.otherPlayerMoved("LOSER");
					        				break;
					        			}
					        			else
					        			if(!is_player_one_winner()&&player_order=="S")
					        			{
					        				out.println("WINNER");
					        				opponent.otherPlayerMoved("LOSER");
					        				break;
					        			}
					        		}
					        	}
					        	System.out.println(send_moves);
					        	opponent.otherPlayerMoved(send_moves);
					        }
			        }
			    }
			}
			
			catch(IOException e){
			   
			    //out.println("Error handling" + opponent + ":" + e);
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







