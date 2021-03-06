import java.util.*;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import javax.swing.SwingConstants;

public class Board_window extends JFrame {

	//variables for timer
	private int original_time;
	private int current_time;
	javax.swing.Timer move_timer;
	JLabel lblTime;
	//variable for pie rule button
	private Boolean is_player2_first_turn=true;
	
	private JPanel contentPane;
	private int[][] board_state;
	private backend ba;

	/**
	 * Create the frame.
	 */
	public Board_window(int pits,int seeds,boolean is_rand_distrubution,int time) {
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
		
		original_time=time*1000;
		current_time=time*1000;
		//setting up timer
		move_timer=new javax.swing.Timer(1,new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!ba.check_end())
				{
					current_time-=1;
					lblTime.setText(String.valueOf(current_time));
					if(current_time==0)
					{
						if(ba.is_player_one_turn())
						{
							JOptionPane.showMessageDialog(contentPane, "Player 2 wins, Player 1 ran out of time!");
						}
						else
						{
							JOptionPane.showMessageDialog(contentPane, "Player 1 wins, Player 2 ran out of time!");
						}
						move_timer.stop();
					}
				}
				
				contentPane.repaint();
			}
		});
		
		
		//creating local game
		ba= new backend(pits,seeds,5000,is_rand_distrubution,backend.mode.two_player,backend.level.easy);
		board_state=ba.get_board();
		move_timer.start();
		
		paint_window();
	}
	
	//function to display a varying amount of pit buttons
	private void paint_window()
	{
		contentPane.removeAll();
		int new_row=(board_state[1].length-1);
		int x_grid=7-(board_state[1].length)/2;
		int y_grid=4;
		
		JLabel lblTimeLeft = new JLabel("Time Left:");
		GridBagConstraints gbc_lblTimeLeft = new GridBagConstraints();
		gbc_lblTimeLeft.insets = new Insets(0, 0, 5, 5);
		gbc_lblTimeLeft.gridx = 11;
		gbc_lblTimeLeft.gridy = 0;
		contentPane.add(lblTimeLeft, gbc_lblTimeLeft);
		
		
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
		
		//adding current move indicators
		JLabel p1_indicator = new JLabel("<");
		p1_indicator.setFont(new Font("Tahoma", Font.PLAIN, 35));
		p1_indicator.setForeground(Color.RED);
		GridBagConstraints gbc_move_indicator_p1 = new GridBagConstraints();
		gbc_move_indicator_p1.gridy = 4;
		gbc_move_indicator_p1.insets = new Insets(0, 0, 5, 5);
		gbc_move_indicator_p1.gridx = x_grid+new_row-1;
		
		//adding time
		lblTime = new JLabel(String.valueOf(current_time));
		lblTime.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblTimelbl = new GridBagConstraints();
		gbc_lblTimelbl.insets = new Insets(0, 0, 5, 0);
		gbc_lblTimelbl.gridx = 12;
		gbc_lblTimelbl.gridy = 0;
		contentPane.add(lblTime, gbc_lblTimelbl);
		
		if(ba.is_player_one_turn())
		{
			gbc_move_indicator_p1.gridy = 4;
		}
		else
		{
			gbc_move_indicator_p1.gridy=2;
		}
		contentPane.add(p1_indicator, gbc_move_indicator_p1);
		
		//adding pie rule button if applicable
		if(is_player2_first_turn&&!ba.is_player_one_turn())
		{
			GridBagConstraints constraints=new GridBagConstraints();
			constraints.gridy=6;
			constraints.gridx=12;
			
			JButton pie_button = new JButton("Pie Rule");
			pie_button.setPreferredSize(new Dimension(90, 90));
			pie_button.setBackground(new Color(51, 153, 204));
			pie_button.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
			pie_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ba.pie_rule();
					paint_window();
					current_time=original_time;
				}
			});
			contentPane.add(pie_button,constraints);
			is_player2_first_turn=false;
		}
		
	//adding current user's buttons below
		//one indexed to exclude the first player's goal
		for (int i=1; i<board_state[1].length-1;++i)
		{
			GridBagConstraints constraints=new GridBagConstraints();
			constraints.gridy=y_grid;
			constraints.gridx=x_grid;
			++x_grid;
			
			JButton pit_button = new JButton(String.valueOf(board_state[1][i]));
			pit_button.setPreferredSize(new Dimension(90, 90));
			pit_button.setBackground(new Color(51, 153, 204));
			pit_button.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
			
			int button_text=i;
			pit_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(ba.is_player_one_turn()&&board_state[1][button_text]!=0)
					{
						ba.mouse_move(button_text);
						board_state=ba.get_board();
						paint_window();
						current_time=original_time;	
					}
				}
			});
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
			
			JButton pit_button = new JButton(String.valueOf(board_state[0][i]));
			pit_button.setPreferredSize(new Dimension(90, 90));
			pit_button.setBackground(new Color(51, 153, 204));
			pit_button.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
			
			int button_text=i;
			pit_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!ba.is_player_one_turn()&&board_state[0][button_text]!=0)
					{
						ba.mouse_move(button_text);
						board_state=ba.get_board();
						paint_window();
						current_time=original_time;
						
					}
				}
			});
			contentPane.add(pit_button,constraints);
		}
		
		if(ba.check_end())
		{
			ba.check_result();
			if(!ba.is_player_one_winner())
			{
				JOptionPane.showMessageDialog(contentPane, "Player two wins");
				current_time=-1;
			}
			else
			{
				JOptionPane.showMessageDialog(contentPane, "Player one wins");
				current_time=-1;
			}
		}
		//repainting window
		contentPane.revalidate();
		contentPane.repaint();
	}

}

