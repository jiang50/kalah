import java.awt.BorderLayout;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class User_Game_Window extends JFrame {

	private JPanel contentPane;
	private int[][] board_state= {{0,0,0},{0,0,0}};
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
	private int original_time=10000;
	private int current_time=10000;
	javax.swing.Timer move_timer;
	JLabel lblTime;

	/**
	 * Create the frame.
	 *
	 */
	public User_Game_Window(String server_ip,String server_port){
		
		
		//setting up format of the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1281, 720);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(222, 184, 135));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90};
		gbl_contentPane.rowHeights = new int[]{90, 90, 90, 90, 90, 90, 90};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		contentPane.setLayout(gbl_contentPane);
		
		messageLabel = new JLabel("");
		GridBagConstraints gbc_lblMessagelabel = new GridBagConstraints();
		gbc_lblMessagelabel.gridx = 12;
		gbc_lblMessagelabel.gridy = 6;
		contentPane.add(messageLabel, gbc_lblMessagelabel);
		
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
		try {
			in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out_server=new PrintWriter(socket.getOutputStream(),true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		play start_game=new play();
		//wait for server to acknowledge then begin playing
		try {
			start_game.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	//function to display a varying amount of pit buttons
	private synchronized Boolean paint_window()
	{
		board_state=ba.get_board();
		contentPane.removeAll();
		int new_row=(board_state[1].length-1);
		int x_grid=7-(board_state[1].length)/2;;
		int y_grid=4;
		
		//adding goal labels
		JLabel lblNewLabel = new JLabel(String.valueOf(board_state[0][0]));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = x_grid-1;
		gbc_lblNewLabel.gridy = 3;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel(String.valueOf(board_state[1][board_state[1].length-1]));
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = x_grid+new_row-1;
		gbc_lblNewLabel_1.gridy = 3;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);

		//adding row indicator
		JLabel p1_indicator = new JLabel("<");
		p1_indicator.setFont(new Font("Tahoma", Font.PLAIN, 35));
		p1_indicator.setForeground(Color.RED);
		GridBagConstraints gbc_move_indicator_p1 = new GridBagConstraints();
		int y_indicator_pos=1;
		if(num_player=="one") {y_indicator_pos=4;}
		if(num_player=="two") {y_indicator_pos=2;}
		gbc_move_indicator_p1.gridy = y_indicator_pos;
		gbc_move_indicator_p1.insets = new Insets(0, 0, 5, 5);
		gbc_move_indicator_p1.gridx = x_grid+new_row-1;
		contentPane.add(p1_indicator,gbc_move_indicator_p1);
		
		//adding message label
		GridBagConstraints gbc_lblMessagelabel = new GridBagConstraints();
		gbc_lblMessagelabel.gridx = 12;
		gbc_lblMessagelabel.gridy = 6;
		contentPane.add(messageLabel, gbc_lblMessagelabel);
		
		
		//adding current user's buttons below
		//one indexed to exclude the first player's goal
		for (int i=1; i<board_state[1].length-1;++i)
		{
			GridBagConstraints constraints=new GridBagConstraints();
			constraints.gridy=y_grid;
			constraints.gridx=x_grid;
			++x_grid;
			
			JLabel pit_button = new JLabel(String.valueOf(board_state[1][i]));
			pit_button.setPreferredSize(new Dimension(90, 90));
			pit_button.setOpaque(true);
			pit_button.setBackground(new Color(51, 153, 204));
			pit_button.setHorizontalAlignment(javax.swing.JLabel.CENTER);
			pit_button.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
			
			contentPane.add(pit_button,constraints);
		}
		
		y_grid=2;
		x_grid=7-(board_state[1].length)/2;
		//creating opponent's buttons
		//excludes last element for P2 array
		for (int i=1; i<board_state[0].length-1;++i)
		{
			GridBagConstraints constraints=new GridBagConstraints();
			constraints.gridy=y_grid;
			constraints.gridx=x_grid;
			++x_grid;
			
			JLabel pit_button = new JLabel(String.valueOf(board_state[0][i]));
			pit_button.setPreferredSize(new Dimension(90, 90));
			pit_button.setOpaque(true);
			pit_button.setBackground(new Color(51, 153, 204));
			pit_button.setHorizontalAlignment(javax.swing.JLabel.CENTER);
			pit_button.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
			
			contentPane.add(pit_button,constraints);
		}
		
		//repainting window
		contentPane.revalidate();
		contentPane.repaint();
		return true;
	}
	
	private void move()
	{
		my_moves="";
		while(my_turn)
		{
			System.out.print("Enter the index of pit: ");
			Scanner sc = new Scanner(System.in);
		    String i = sc.nextLine();
		    //allowing for pie rule
		    if(i.startsWith("P")&&num_turns==1)
		    {
		    	ba.pie_rule();
		    	//ba.set_player_turn(false);
		    	my_turn=false;
		    	out_server.println("P");
		    	break;
		    }
		    else
		    {
		    	ba.mouse_move(Integer.valueOf(i));
		    }
			++num_turns;
			ba.printboard();
			paint_window();
			
			//appending the move made to the string that is sent to the server
			my_moves=my_moves+i+" ";
			board_state=ba.get_board();
			if(!ba.is_player_one_turn()&&num_player=="one"){
				current_time=original_time;
				out_server.println(my_moves);
				my_turn=false;
			}
			else
			if(ba.is_player_one_turn()&&num_player=="two")
			{
				current_time=original_time;
				out_server.println(my_moves);
				my_turn=false;
			}
			current_time=original_time;	
			
		}
		messageLabel.setText("Move sent to server");
		System.out.println("Opponent's turn");
		paint_window();
		my_turn=false;
		my_moves="";
	}
	private class play extends Thread{
		
		play()
		{
			
		}
		public void run()
		{
			String response="";
			try {
				while(true) {
					try {
						response=in.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println(response);
					String response_array[]= response.split(" ");
					if(response.startsWith("WELCOME"))
					{
						System.out.println("Got welcome");
						messageLabel.setText("Connected to the server");
						contentPane.repaint();
					}
					if(response.startsWith("INFO"))
					{
						pit_num=Integer.valueOf(response_array[1]);
						int seed_num=Integer.valueOf(response_array[2]);
						original_time=Integer.valueOf(response_array[3]);
						current_time=original_time;
						
						
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
							System.out.println(pit_num);
							for(int i=1;i<=pit_num;++i)
							{
								++response_index;
								random_board_input[1][i]=Integer.valueOf(response_array[response_index]);
							}
							for(int i=1;i<=pit_num;++i)
							{
								random_board_input[0][i]=Integer.valueOf(response_array[response_index]);
								--response_index;
							}
							ba.set_board(random_board_input);
						}
						//letting server know I got the board
						out_server.println("READY");
						//checking if I am going first or second
						if(response_array[4].startsWith("F"))
						{
							my_turn=true;
							num_player="one";
							ba.printboard();
							paint_window();
							move();
						}
						else
						{
							System.out.println("Opponent's turn");
							num_player="two";
							ba.printboard();
							paint_window();
							my_turn=false;
						}
						
						
					}
					if(response.startsWith("OK")) 
					{
						messageLabel.setText("Move received");
						repaint();
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
					if(response.startsWith("WINNER"))
					{
						JOptionPane.showMessageDialog(contentPane, "You win!");
						break;
					}
					if(response.startsWith("TIE"))
					{
						JOptionPane.showMessageDialog(contentPane, "It is a draw");
						break;
					}
					if(response.startsWith("P"))
					{
						ba.pie_rule();
						my_turn=true;
						paint_window();
						move();
					}
					if(isNumeric(response_array[0].trim()))
					{	
						for(int i=0;i<response_array.length;++i)
						{
							System.out.println("MOVE FROM OPPONENT: "+response_array[i]);
							ba.mouse_move(Integer.valueOf(response_array[i]));
						}
						
						my_turn=true;
						current_time=original_time;
						ba.printboard();
						paint_window();
						++num_turns;
						messageLabel.setText("Opponent moved, your go");
						move();
						
					}
				}
			}
			finally {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
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
	}
}
