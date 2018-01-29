import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Server_AI_GUI extends JFrame {

	private JPanel contentPane;
	private int[][] board_state;

	/**
	 * Create the frame.
	 */
	public Server_AI_GUI(int pits,int seeds,int in_time,boolean is_rand_distribution,String server_ip,String server_port,String difficulty) {
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
		
		try {
			DataServer_threaded run_server=new DataServer_threaded(pits,seeds,in_time,is_rand_distribution,server_ip,Integer.parseInt(server_port));
			run_server.start();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		AI_window frame2=new AI_window(server_ip,server_port,difficulty);
		contentPane.setVisible(false);
		frame2.setVisible(true);
	}
}