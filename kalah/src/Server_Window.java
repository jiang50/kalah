import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

public class Server_Window extends JFrame {
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
	public Server_Window() {
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
		
		//controls that set board state
		JSpinner pit_num_spinner = new JSpinner();
		pit_num_spinner.setModel(new SpinnerNumberModel(6, 4, 9, 1));
		pit_num_spinner.setBounds(845, 433, 72, 20);
		pit_num_spinner.setVisible(true);
		contentPane.add(pit_num_spinner);
		
		JSpinner seed_number_spinner = new JSpinner();
		seed_number_spinner.setModel(new SpinnerNumberModel(4, 1, 10, 1));
		seed_number_spinner.setBounds(952, 433, 72, 20);
		seed_number_spinner.setVisible(true);
		contentPane.add(seed_number_spinner);
		
		JLabel pit_num_label = new JLabel("Pit number");
		pit_num_label.setFont(new Font("Showcard Gothic", Font.PLAIN, 11));
		pit_num_label.setBounds(845, 408, 72, 14);
		pit_num_label.setVisible(true);
		contentPane.add(pit_num_label);
		
		JLabel seed_number_label = new JLabel("Seed Number");
		seed_number_label.setFont(new Font("Showcard Gothic", Font.PLAIN, 11));
		seed_number_label.setBounds(952, 408, 105, 14);
		seed_number_label.setVisible(true);
		contentPane.add(seed_number_label);
		
		JToggleButton random_toggle = new JToggleButton("Random Distribution");
		random_toggle.setFont(new Font("Showcard Gothic", Font.PLAIN, 11));
		random_toggle.setBounds(845, 464, 179, 23);
		random_toggle.setVisible(true);
		contentPane.add(random_toggle);
		
		JLabel lblSecondsPerTurn = new JLabel("Seconds per turn:");
		lblSecondsPerTurn.setFont(new Font("Showcard Gothic", Font.PLAIN, 11));
		lblSecondsPerTurn.setBounds(845, 504, 116, 14);
		lblSecondsPerTurn.setVisible(true);
		contentPane.add(lblSecondsPerTurn);
		
		JSpinner time_spinner = new JSpinner();
		time_spinner.setModel(new SpinnerNumberModel(8, 1, 20, 1));
		time_spinner.setBounds(971, 498, 53, 20);
		time_spinner.setVisible(true);
		contentPane.add(time_spinner);
		//end of controls that set board state

			
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
				
				if(IP_field.getText().equals("")||Port_field.getText().equals(""))
				{
					server_inputs_valid=false;
					lblInvalidIpport.setVisible(true);
				}
				if(player_type=="")
				{
					player_type_valid=false;
					Player_type_error.setVisible(true);
				}
				if(player_type=="ai"&&difficulty=="")
				{
					difficulty_valid=false;
					Difficulty_error.setVisible(true);
				}
				
				if(difficulty_valid==true&&server_inputs_valid==true&&player_type_valid==true)
				{
					int pit_num=(Integer)pit_num_spinner.getValue();
					int seed_num=(Integer)seed_number_spinner.getValue();
					String ip=IP_field.getText();
					String port=Port_field.getText();
					//choosing which game window to load
					if(player_type=="ai")
					{
						
						Server_AI_GUI frame2=new Server_AI_GUI(pit_num,seed_num,(Integer)time_spinner.getValue()*1000,random_toggle.isSelected(),ip,port,difficulty);
						contentPane.setVisible(false);
						frame2.setVisible(true);
					}
					else
					if(player_type=="user")
					{
						Server_Player_GUI frame2=new Server_Player_GUI(pit_num,seed_num,(Integer)time_spinner.getValue()*1000,random_toggle.isSelected(),ip,port);
						contentPane.setVisible(false);
						frame2.setVisible(true);
					}
					else
					if(player_type=="host")
					{
						Server_Host_GUI frame2=new Server_Host_GUI(pit_num,seed_num,(Integer)time_spinner.getValue()*1000,random_toggle.isSelected(),ip,port);
						contentPane.setVisible(false);
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
		
		JLabel Opponent_type = new JLabel("Player Type");
		Opponent_type.setFont(new Font("Showcard Gothic", Font.PLAIN, 40));
		Opponent_type.setBounds(801, 101, 274, 50);
		Opponent_type.setForeground(new Color(0, 128, 0));
		contentPane.add(Opponent_type);
		
		//buttons to set user type
		JButton AI_button = new JButton("AI");
		AI_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player_type="ai";
				
				contentPane.repaint();
			}
		});
		AI_button.setFont(new Font("Showcard Gothic", Font.PLAIN, 18));
		AI_button.setBounds(845, 181, 179, 56);
		contentPane.add(AI_button);
		
		JButton User_button = new JButton("Player");
		User_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player_type="user";
				
				contentPane.repaint();
			}
		});
		User_button.setFont(new Font("Showcard Gothic", Font.PLAIN, 18));
		User_button.setBounds(845, 248, 179, 56);
		contentPane.add(User_button);
		
		JButton btnHost = new JButton("Host");
		btnHost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player_type="host";
			}
		});
		btnHost.setFont(new Font("Showcard Gothic", Font.PLAIN, 18));
		btnHost.setBounds(845, 315, 179, 56);
		contentPane.add(btnHost);
		
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
		
		JLabel lblHostAServer = new JLabel("Host a Server");
		lblHostAServer.setFont(new Font("Showcard Gothic", Font.PLAIN, 24));
		lblHostAServer.setBounds(526, 23, 191, 50);
		contentPane.add(lblHostAServer);
		
		
	}
}
