import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.GridLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.AbstractListModel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;

public class Game_options extends JFrame{

	private JPanel contentPane;
	private String difficulty="";
	private String player_type="";
	private int[][] user_pit_array= {{0,4,4,4,4,5,8,0},{0,4,4,4,4,5,8,0}};
	private JTextField IP_field;
	private JTextField Port_field;
	private Boolean server_inputs_valid=true;
	private Boolean player_type_valid=true;
	private Boolean difficulty_valid=true;
	
	/**
	 * Create the frame.
	 */
	public Game_options() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1281, 720);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(222, 184, 135));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Invalid field error messages
		JLabel lblInvalidIpport = new JLabel("Invalid IP/Port");
		lblInvalidIpport.setForeground(Color.RED);
		lblInvalidIpport.setBounds(579, 546, 138, 14);
		lblInvalidIpport.setVisible(false);
		contentPane.add(lblInvalidIpport);
		
		JLabel Player_type_error = new JLabel("Select player type");
		Player_type_error.setForeground(Color.RED);
		Player_type_error.setBounds(567, 571, 105, 14);
		Player_type_error.setVisible(false);
		contentPane.add(Player_type_error);
		
		JLabel Difficulty_error = new JLabel("Select AI difficulty");
		Difficulty_error.setForeground(Color.RED);
		Difficulty_error.setBounds(567, 571, 105, 14);
		Difficulty_error.setVisible(false);
		contentPane.add(Difficulty_error);
		
		//this group of controls only display for a LOCAL game
			JSpinner pit_num_spinner = new JSpinner();
			pit_num_spinner.setModel(new SpinnerNumberModel(6, 4, 9, 1));
			pit_num_spinner.setBounds(845, 433, 72, 20);
			pit_num_spinner.setVisible(false);
			contentPane.add(pit_num_spinner);
			
			JSpinner seed_number_spinner = new JSpinner();
			seed_number_spinner.setModel(new SpinnerNumberModel(4, 1, 10, 1));
			seed_number_spinner.setBounds(952, 433, 72, 20);
			seed_number_spinner.setVisible(false);
			contentPane.add(seed_number_spinner);
			
			JLabel pit_num_label = new JLabel("Pit number");
			pit_num_label.setFont(new Font("Showcard Gothic", Font.PLAIN, 11));
			pit_num_label.setBounds(845, 408, 72, 14);
			pit_num_label.setVisible(false);
			contentPane.add(pit_num_label);
			
			JLabel seed_number_label = new JLabel("Seed Number");
			seed_number_label.setFont(new Font("Showcard Gothic", Font.PLAIN, 11));
			seed_number_label.setBounds(952, 408, 105, 14);
			seed_number_label.setVisible(false);
			contentPane.add(seed_number_label);
			
			JToggleButton random_toggle = new JToggleButton("Random Distribution");
			random_toggle.setFont(new Font("Showcard Gothic", Font.PLAIN, 11));
			random_toggle.setBounds(845, 464, 179, 23);
			random_toggle.setVisible(false);
			contentPane.add(random_toggle);
			
			JLabel lblSecondsPerTurn = new JLabel("Seconds per turn:");
			lblSecondsPerTurn.setFont(new Font("Showcard Gothic", Font.PLAIN, 11));
			lblSecondsPerTurn.setBounds(845, 504, 116, 14);
			lblSecondsPerTurn.setVisible(false);
			contentPane.add(lblSecondsPerTurn);
			
			JSpinner time_spinner = new JSpinner();
			time_spinner.setModel(new SpinnerNumberModel(8, 1, 20, 1));
			time_spinner.setBounds(971, 498, 53, 20);
			time_spinner.setVisible(false);
			contentPane.add(time_spinner);
		//end of group that only displays for local game

			
		/*
		 *button to start the game
		 */
		JButton Begin_button = new JButton("BEGIN");
		Begin_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				server_inputs_valid=true;
				player_type_valid=true;
				difficulty_valid=true;
				lblInvalidIpport.setVisible(false);
				Player_type_error.setVisible(false);
				Difficulty_error.setVisible(false);
				
				if((IP_field.getText().equals("")||Port_field.getText().equals(""))&&(player_type!="pvp"&&player_type!="pva"))
				{
					server_inputs_valid=false;
					lblInvalidIpport.setVisible(true);
				}
				if(player_type=="")
				{
					player_type_valid=false;
					Player_type_error.setVisible(true);
				}
				if((player_type=="ai"||player_type=="pva")&&difficulty=="")
				{
					difficulty_valid=false;
					Difficulty_error.setVisible(true);
				}
				
				if(difficulty_valid==true&&server_inputs_valid==true&&player_type_valid==true)
				{
					//choosing which game window to load
					if(player_type=="ai")
					{
						AI_window frame2=new AI_window(IP_field.getText(),Port_field.getText(),difficulty);
						contentPane.setVisible(false);
						frame2.setVisible(true);
					}
					else
					if(player_type=="user")
					{
						contentPane.setVisible(false);
						User_Game_Window frame2 = new User_Game_Window(IP_field.getText(),Port_field.getText());
						frame2.setVisible(true);
						
					}
					else
					if(player_type=="pvp")
					{
						Board_window frame2=new Board_window((Integer)pit_num_spinner.getValue(),(Integer)seed_number_spinner.getValue(),random_toggle.isSelected(),(Integer)time_spinner.getValue());
						contentPane.setVisible(false);
						frame2.setVisible(true);
					}
					else
					if(player_type=="pva")
					{
						contentPane.setVisible(false);
						AI_local frame2=new AI_local((Integer)pit_num_spinner.getValue(),(Integer)seed_number_spinner.getValue(),random_toggle.isSelected(),difficulty,(Integer)time_spinner.getValue());
						frame2.setVisible(true);
					}
				}
				
				contentPane.repaint();
			}
		});
		Begin_button.setBounds(526, 479, 191, 56);
		Begin_button.setBackground(new Color(51, 153, 204));
		Begin_button.setFont(new Font("Showcard Gothic", Font.PLAIN, 27));
		Begin_button.setForeground(new Color(0, 0, 0));
		contentPane.add(Begin_button);
		
		//window descriptors
		JLabel Difficulty_label = new JLabel("Difficulty");
		Difficulty_label.setFont(new Font("Showcard Gothic", Font.PLAIN, 40));
		Difficulty_label.setForeground(new Color(0, 128, 0));
		Difficulty_label.setBounds(157, 101, 227, 50);
		contentPane.add(Difficulty_label);
		
		JLabel lblGame_type = new JLabel("Game Type");
		lblGame_type.setFont(new Font("Showcard Gothic", Font.PLAIN, 40));
		lblGame_type.setBounds(819, 101, 256, 50);
		lblGame_type.setForeground(new Color(0, 128, 0));
		contentPane.add(lblGame_type);
		
		//buttons to set user type
		JButton AI_button = new JButton("AI");
		AI_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player_type="ai";
				pit_num_spinner.setVisible(false);
				seed_number_spinner.setVisible(false);
				pit_num_label.setVisible(false);
				seed_number_label.setVisible(false);
				random_toggle.setVisible(false);
				lblSecondsPerTurn.setVisible(false);
				time_spinner.setVisible(false);
				
				contentPane.repaint();
			}
		});
		AI_button.setFont(new Font("Showcard Gothic", Font.PLAIN, 18));
		AI_button.setBounds(845, 181, 179, 38);
		contentPane.add(AI_button);
		
		JButton User_button = new JButton("Player");
		User_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player_type="user";
				pit_num_spinner.setVisible(false);
				seed_number_spinner.setVisible(false);
				pit_num_label.setVisible(false);
				seed_number_label.setVisible(false);
				random_toggle.setVisible(false);
				lblSecondsPerTurn.setVisible(false);
				time_spinner.setVisible(false);
				
				contentPane.repaint();
			}
		});
		User_button.setFont(new Font("Showcard Gothic", Font.PLAIN, 18));
		User_button.setBounds(845, 230, 179, 38);
		contentPane.add(User_button);
		
		JButton PvP_button = new JButton("PvP (local)");
		PvP_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player_type="pvp";
				pit_num_spinner.setVisible(true);
				seed_number_spinner.setVisible(true);
				pit_num_label.setVisible(true);
				seed_number_label.setVisible(true);
				random_toggle.setVisible(true);
				lblSecondsPerTurn.setVisible(true);
				time_spinner.setVisible(true);
				
				contentPane.repaint();
			}
		});
		PvP_button.setFont(new Font("Showcard Gothic", Font.PLAIN, 18));
		PvP_button.setBounds(845, 279, 179, 38);
		contentPane.add(PvP_button);
		
		JButton PvsAI_button = new JButton("PvAI (local)");
		PvsAI_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player_type="pva";
				pit_num_spinner.setVisible(true);
				seed_number_spinner.setVisible(true);
				pit_num_label.setVisible(true);
				seed_number_label.setVisible(true);
				random_toggle.setVisible(true);
				lblSecondsPerTurn.setVisible(true);
				time_spinner.setVisible(true);
			}
		});
		PvsAI_button.setFont(new Font("Showcard Gothic", Font.PLAIN, 18));
		PvsAI_button.setBounds(845, 328, 179, 38);
		contentPane.add(PvsAI_button);
		
		//buttons to set difficulty
		JButton Easy_button = new JButton("EASY");
		Easy_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				difficulty="easy";
			}
		});
		Easy_button.setFont(new Font("Showcard Gothic", Font.PLAIN, 18));
		Easy_button.setBounds(181, 181, 179, 56);
		contentPane.add(Easy_button);
		
		JButton Medium_button = new JButton("MEDIUM");
		Medium_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				difficulty="medium";
			}
		});
		Medium_button.setFont(new Font("Showcard Gothic", Font.PLAIN, 18));
		Medium_button.setBounds(181, 248, 179, 56);
		contentPane.add(Medium_button);
		
		JButton Hard_button = new JButton("HARD");
		Hard_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				difficulty="hard";
			}
		});
		Hard_button.setFont(new Font("Showcard Gothic", Font.PLAIN, 18));
		Hard_button.setBounds(181, 315, 179, 56);
		contentPane.add(Hard_button);
	
		
		//fields and labels for server connection
		JLabel lbl_IP = new JLabel("Server IP");
		lbl_IP.setBounds(586, 181, 72, 14);
		contentPane.add(lbl_IP);
		
		JLabel lbl_Port = new JLabel(" Port Number");
		lbl_Port.setBounds(575, 315, 83, 14);
		contentPane.add(lbl_Port);
		
		IP_field = new JTextField();
		IP_field.setText("127.0.0.1");
		IP_field.setBounds(556, 217, 116, 20);
		contentPane.add(IP_field);
		IP_field.setColumns(10);
		
		Port_field = new JTextField();
		Port_field.setBounds(556, 351, 116, 20);
		contentPane.add(Port_field);
		Port_field.setColumns(10);
		
		JButton host_server_button = new JButton("Host Server");
		host_server_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Server_Window frame2=new Server_Window();
				contentPane.setVisible(false);
				frame2.setVisible(true);
			}
		});
		host_server_button.setFont(new Font("Showcard Gothic", Font.PLAIN, 15));
		host_server_button.setBounds(0, 636, 179, 45);
		contentPane.add(host_server_button);
	
		JLabel lblJoinServer = new JLabel("Join a Server or Play Local");
		lblJoinServer.setFont(new Font("Showcard Gothic", Font.PLAIN, 24));
		lblJoinServer.setBounds(439, 23, 359, 50);
		contentPane.add(lblJoinServer);
		
		
	}
}
