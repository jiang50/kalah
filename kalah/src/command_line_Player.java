import java.awt.BorderLayout;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.*;

public class command_line_Player extends JFrame {

	private JPanel contentPane;
	private int[][] board_state;
	private backend ba;
	private int pit_num;
	private Boolean is_rand_distribution;
	private Boolean my_turn;
	private JLabel messageLabel=new JLabel("");
	private String my_moves;
	private String num_player;
	private int num_turns;
	
	//variables for server
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out_server;
	
	//variables for timer
	private int original_time;
	private int current_time;
	javax.swing.Timer move_timer;
	JLabel lblTime;

	/**
	 * Create the frame.
	 *
	 */
	public command_line_Player(int[][] user_board,String server_ip,String server_port) throws Exception{
		//setting up networking
		
			try {
				socket=new Socket(server_ip,Integer.valueOf(server_port));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out_server=new PrintWriter(socket.getOutputStream(),true);
			
			
			System.out.println("HELLO");
			//wait for server to acknowledge then begin playing
			play();
			
	}
	public static void main(String[] args) throws IOException {
		//move();
	}
	//function to display a varying amount of pit buttons
	private void move()
	{
		while(my_turn)
		{
			System.out.print("Enter the index of pit: ");
			Scanner sc = new Scanner(System.in);
		    int i = sc.nextInt();
			//adding current user's buttons below
			//one indexed to exclude the first player's goal
			if(my_turn)
			{
				ba.mouse_move(i);
				ba.printboard();
				//appending the move made to the string that is sent to the server
				my_moves=my_moves+i+" ";
				board_state=ba.get_board();
				if(!ba.is_player_one_turn()&&num_player=="one"){
					out_server.println(my_moves);
				}
				else
				if(ba.is_player_one_turn()&&num_player=="two")
				{
					out_server.println(my_moves);
				}
				current_time=original_time;	
				messageLabel.setText("Move sent to server");
			}
		}
		my_turn=false;
		my_moves="";
	}
	
	public void play() throws Exception{
		String response;
		try {
			while(true) {
				System.out.println("DOWN UNDER");
				response=in.readLine();
				System.out.println(response);
				String response_array[]=response.split(" ");
				if(response.startsWith("WELCOME"))
				{
					messageLabel.setText("Connected to the server");
					System.out.println("Got WELCOME");
					contentPane.repaint();
				}
				if(response.startsWith("INFO"))
				{
					System.out.println("INFO GOTTEN");
					pit_num=Integer.valueOf(response_array[1]);
					int seed_num=Integer.valueOf(response_array[2]);
					original_time=Integer.valueOf(response_array[3]);
					
					board_state=new int[2][pit_num+2];
					if(response_array[5].startsWith("S"))
					{
						ba=new backend(pit_num,seed_num,original_time,false,backend.mode.two_player,backend.level.easy);
					}
					else
					if(response_array[5].startsWith("R"))
					{
						ba=new backend(pit_num,seed_num,original_time,false,backend.mode.two_player,backend.level.easy);
						int[][] random_board_input=new int[2][pit_num+2];
						
						//setting board as configured by server
						int response_index=5;
						for(int i=1;i<pit_num-2;++i)
						{
							++response_index;
							random_board_input[1][i]=Integer.valueOf(response_array[response_index]);
						}
						for(int i=1;i<pit_num-2;++i)
						{
							random_board_input[0][i]=Integer.valueOf(response_array[response_index]);
							--response_index;
						}
						ba.set_board(random_board_input);
					}
					//letting server know I got the board
					out_server.println("READY");
					
					//checking if I am going first or second
					System.out.println(response_array[4]);
					if(response_array[4].startsWith("F"))
					{
						System.out.println("FIRST PLAYER");
						my_turn=true;
						num_player="one";
						move();
					}
					else
					{
						num_player="two";
						my_turn=false;
					}
				}
				if(response.startsWith("OK")) 
				{
					messageLabel.setText("Move received by server");
				}
				if(response.startsWith("ILLEGAL"))
				{
					JOptionPane.showMessageDialog(contentPane, "Illegal move, you lose");
					break;
				}
				if(response.startsWith("TIME"))
				{
					JOptionPane.showMessageDialog(contentPane,"You ran out of time and Lost!");
					break;
				}
				if(response.startsWith("LOSER"))
				{
					JOptionPane.showMessageDialog(contentPane, "You Lose");
					break;
				}
				if(response.startsWith("Winner"))
				{
					JOptionPane.showMessageDialog(contentPane, "You win!");
					break;
				}
				if(response.startsWith("TIE"))
				{
					JOptionPane.showMessageDialog(contentPane, "It is a draw");
					break;
				}
				if(isNumeric(response_array[0]))
				{	
					for(int i=1;i<response_array.length;++i)
					{
						ba.mouse_move(Integer.valueOf(response_array[i]));
					}
					my_turn=true;
					if(num_turns==0)
					{
						ba.pie_rule();
					}
					++num_turns;
					messageLabel.setText("Opponent moved, your go");
					move();
				}
			}
		}
		finally {
			socket.close();
		}
	}
	
	//helper function to determine if a move was received
	public static boolean isNumeric(String str)  
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
}
