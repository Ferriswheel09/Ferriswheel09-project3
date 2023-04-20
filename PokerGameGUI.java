
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import java.net.URL;






public class PokerGameGUI {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JButton foldButton;
	private JButton callButton;
	private JButton raiseButton;
	private JTextField textField_3;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JTextArea pot;
	private JPanel riverPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PokerGameGUI window = new PokerGameGUI();
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
	public PokerGameGUI() {
		initialize();
	}
	
	private JLabel createCardLabel(Card card) {
    String imagePath = card.getImagePath();
    System.out.println("Card: " + card.getRank() + " of suit " + card.getSuit());
    System.out.println("Image path: " + imagePath);
    URL imageURL = getClass().getResource(imagePath);
    System.out.println("Image URL: " + imageURL);

    if (imageURL == null) {
        System.err.println("Error: Image not found for path " + imagePath);
    } else {
        ImageIcon icon = new ImageIcon(imageURL);
        return new JLabel(icon);
    }
    return null;
}
	/**
	 * Initialize the contents of the frame.
	 */
	
	private void initialize() {
		//frame.setSize(800, 600);
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Name");
		lblNewLabel.setBounds(308, 11, 27, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("IP");
		lblNewLabel_1.setBounds(448, 11, 46, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Port");
		lblNewLabel_2.setBounds(574, 11, 46, 14);
		frame.getContentPane().add(lblNewLabel_2);
		
		textField = new JTextField();
		textField.setBounds(336, 8, 86, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(458, 8, 86, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(600, 8, 61, 20);
		frame.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		JButton connectButton = new JButton("Connect");
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		connectButton.setBounds(685, 7, 89, 23);
		frame.getContentPane().add(connectButton);
		
		foldButton = new JButton("Fold"); 
		foldButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		foldButton.setBounds(402, 527, 89, 23);
		frame.getContentPane().add(foldButton);
		
		callButton = new JButton("Call");
		callButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		callButton.setBounds(501, 527, 89, 23);
		frame.getContentPane().add(callButton);
		
		raiseButton = new JButton("Raise");
		raiseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		raiseButton.setBounds(600, 527, 89, 23);
		frame.getContentPane().add(raiseButton);
		
		textField_3 = new JTextField();
		textField_3.setBounds(699, 528, 38, 20);
		frame.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		lblNewLabel_3 = new JLabel("Chips");
		lblNewLabel_3.setBounds(10, 511, 46, 14);
		frame.getContentPane().add(lblNewLabel_3);
		
		JTextArea chips = new JTextArea();
		chips.setBounds(10, 526, 46, 22);
		frame.getContentPane().add(chips);
		
		lblNewLabel_4 = new JLabel("Current Pot");
		lblNewLabel_4.setBounds(92, 511, 68, 14);
		frame.getContentPane().add(lblNewLabel_4);
		
		pot = new JTextArea();
		pot.setBounds(92, 526, 68, 22);
		frame.getContentPane().add(pot);
		
		riverPanel = new JPanel();
	        riverPanel.setBounds(250, 250, 300, 100);
	        frame.getContentPane().add(riverPanel);

	        // Example: create a sample river with 5 cards
	        Card[] river = new Card[]{
	            new Card(10, 2), // 10 of Diamonds
	            new Card(11, 1), // Jack of Clubs
	            new Card(12, 3), // Queen of Hearts
	            new Card(13, 4), // King of Spades
	            new Card(1, 1)   // Ace of Clubs
	        };

	        for (Card card : river) {
	            riverPanel.add(createCardLabel(card));
	        }
		

	}
}
