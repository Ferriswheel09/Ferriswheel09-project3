import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.net.URL;
import java.util.*;
import java.io.*;

public class PokerGameGUI {

    private JFrame frame;
    private JTextField ip;
    private JTextField port;
    private JButton foldButton;
    private JButton callButton;
    private JButton raiseButton;
    private JTextField textField_3;
    private JLabel lblNewLabel_3;
    private JLabel lblNewLabel_4;
    private JTextArea pot;
    private JPanel riverPanel;
    private JPanel playerHandPanel;
    private ClientSide hello;

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

    public PokerGameGUI() {
        initialize();
    }

    private JLabel createCardLabel(Card card) {
        String imagePath = card.getImagePath();
        URL imageURL = getClass().getResource(imagePath);

        if (imageURL == null) {
            System.err.println("Error: Image not found for path " + imagePath);
        } else {
            ImageIcon icon = new ImageIcon(imageURL);
            Image img = icon.getImage().getScaledInstance(90, 100, Image.SCALE_DEFAULT);
            ImageIcon resizedIcon = new ImageIcon(img);
            return new JLabel(resizedIcon);
        }
        return null;
    }

    private Card getRandomCard(Set<Card> excludedCards) {
        Random random = new Random();
        Card card;

        do {
            int rank = random.nextInt(13) + 1;
            int suit = random.nextInt(4) + 1;
            card = new Card(rank, suit);
        } while (excludedCards.contains(card));

        return card;
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
		

		
		JLabel lblNewLabel_1 = new JLabel("IP");
		lblNewLabel_1.setBounds(448, 11, 46, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Port");
		lblNewLabel_2.setBounds(574, 11, 46, 14);
		frame.getContentPane().add(lblNewLabel_2);

		
		ip = new JTextField();
		ip.setBounds(458, 8, 86, 20);
		frame.getContentPane().add(ip);
		ip.setColumns(10);
		
		port = new JTextField();
		port.setBounds(600, 8, 61, 20);
		frame.getContentPane().add(port);
		port.setColumns(10);
		
		JButton connectButton = new JButton("Connect");
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
        try{
          int portNum = Integer.parseInt(port.getText());
          hello = new ClientSide(ip.getText(), portNum);
        }
        catch(Exception f){
          System.err.println("Error connecting");
          JOptionPane.showMessageDialog(null, "Error connecting");
        }
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
        riverPanel.setBounds(150, 150, 500, 100);
        riverPanel.setLayout(new GridLayout(1, 5));
        frame.getContentPane().add(riverPanel);

        playerHandPanel = new JPanel();
        playerHandPanel.setBounds(150, 300, 200, 100);
        playerHandPanel.setLayout(new GridLayout(1, 2));
        frame.getContentPane().add(playerHandPanel);

        Set<Card> dealtCards = new HashSet<>();

        // Generate random unique cards for the river
        Set<Card> riverCards = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            Card card = getRandomCard(dealtCards);
            dealtCards.add(card);
            riverCards.add(card);
            riverPanel.add(createCardLabel(card));
        }

        // Generate random unique cards for the player's hand, ensuring they are not in the dealt cards
        Set<Card> playerHand = new HashSet<>();
        for (int i = 0; i < 2; i++) {
            Card card = getRandomCard(dealtCards);
            dealtCards.add(card);
            playerHand.add(card);
            playerHandPanel.add(createCardLabel(card));
        }
    }
    
    private class ClientSide{
        private String ip;
        private int port;
        private PrintWriter out;
        private BufferedReader in;
        private Socket sock;

        public ClientSide(String ip, int port){
            this.ip = ip;
            this.port = port;
            try{
                sock = new Socket(ip, port);
            }
            catch(Exception e){
                System.err.println("Cannot connect");
            }

            try{
                out = new PrintWriter(sock.getOutputStream());
                out.println(ip + " " + port);

                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                
            }
            catch(Exception e){
                System.err.println("IO Exception");
            }
        }
    }
}