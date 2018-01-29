import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;

public class GUI_Class {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_Class window = new GUI_Class();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI_Class() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(222, 184, 135));
		frame.setBounds(100, 100, 1281, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		
		JLabel TitleText = new JLabel("MANCALA");
		TitleText.setBounds(434, 39, 369, 103);
		TitleText.setForeground(new Color(0, 128, 0));
		TitleText.setBackground(new Color(135, 206, 235));
		TitleText.setFont(new Font("Showcard Gothic", Font.PLAIN, 83));
		frame.getContentPane().add(TitleText);
		
		JButton btnStartGame = new JButton("Start Game");
		btnStartGame.setBounds(526, 479, 191, 56);
		btnStartGame.setBackground(new Color(51, 153, 204));
		btnStartGame.setForeground(new Color(0, 0, 0));
		btnStartGame.setFont(new Font("Showcard Gothic", Font.PLAIN, 24));
		btnStartGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game_options frame2=new Game_options();
				frame2.setVisible(true);
				frame.setVisible(false);
				
			}
		});
		frame.getContentPane().add(btnStartGame);
	}
}
