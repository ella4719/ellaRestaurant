import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import java.io.File;

public class StartPageGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartPageGUI frame = new StartPageGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public StartPageGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1366,1024);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 228, 225));
		panel.setBounds(355, 159, 660, 185);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Welcome to the\nElla's Restaurant\n");
		lblNewLabel.setBounds(77, 64, 558, 42);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 35));
		panel.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Start Now!");
		btnNewButton.setBounds(482, 527, 382, 76);
		btnNewButton.addActionListener(e -> {
            File file = new File("restaurantData.ser");
            if (file.exists()) {
                startApplication("restaurantData.ser");
            } else {
                JOptionPane.showMessageDialog(this, "No saved session found. Please start a new session.", "Start New Session", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Start new session");
		
		btnNewButton_1.addActionListener(e -> startApplication(null));
		btnNewButton_1.setBounds(694, 741, 159, 29);
		contentPane.add(btnNewButton_1);
	}
	
	private void startApplication(String filename) {
        EventQueue.invokeLater(() -> {
            try {
                Restaurant restaurant = (filename != null) ? new Restaurant(filename) : new Restaurant();
                MainFrameGUI mainFrame = MainFrameGUI.getInstance(restaurant);
                mainFrame.setVisible(true);
                MenuManagementGUI menuManagement = new MenuManagementGUI(restaurant);
                PaymentGUI paymentGUI = new PaymentGUI(restaurant);

                // 현재 StartPageGUI 창을 숨김
                setVisible(false);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

