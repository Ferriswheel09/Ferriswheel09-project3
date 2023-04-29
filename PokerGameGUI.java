import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.net.URL;
import java.util.*;
import java.io.*;

public class PokerGameGUI extends JFrame {

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

  private JTextArea members;
  private JTextArea messages;
  private JTextField compose;
  private ClientSide hello;

  private JLabel createCardLabel(Card card) {
    String imagePath = card.getImagePath();
    URL imageURL = getClass().getResource(imagePath);

    if (imageURL == null) {
      System.err.println("Error: Image not found for path " + imagePath);
    } else {
      ImageIcon icon = new ImageIcon(imageURL);
      Image img = icon
        .getImage()
        .getScaledInstance(90, 100, Image.SCALE_DEFAULT);
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
  public PokerGameGUI() {
    //Sets the Foundation for the Window
    super();
    status = "Disconnected";
    this.setTitle("GWack -- GW Slack Simulator (" + status + ")");
    this.setSize(800, 600);

    //Creates the name, ip address, and port fields to be inputted
    ipadd = new JTextField(5);
    port = new JTextField(5);

    //Creates the Connect Button (barebones for now)
    JButton connect = new JButton("Connect");
    connect.addActionListener(e -> {
      if (connect.getText().equals("Connect")) {
        //Sets all the text to flip around
        connect.setText("Disconnect");
        status = "Connected";
        this.setTitle("GWack -- GW Slack Simulator (" + status + ")");
        try {
          int portNum = Integer.parseInt(port.getText());
          hello = new ClientSide(ipadd.getText(), portNum);
        } catch (Exception f) {
          System.err.println("Error connecting");
          JOptionPane.showMessageDialog(null, "Error connecting");
        }
      }
    });

    //The top panel for the name, IP address, port, and connect
    JPanel topPanel = new JPanel(new FlowLayout());
    topPanel.add(new JLabel("IP:"));
    topPanel.add(ipadd);
    topPanel.add(new JLabel("Port:"));
    topPanel.add(port);
    topPanel.add(connect);
    this.add(topPanel, BorderLayout.NORTH);


    //Bottom panel is added to have chat and compose
    JPanel bottomPanel = new JPanel(new FlowLayout());
    foldButton = new JButton("Fold");
    foldButton.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          hello.fold();
        }
      }
    );
    bottomPanel.add(foldButton);

    callButton = new JButton("Call");
    callButton.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          hello.call();
        }
      }
    );
    bottomPanel.add(callButton);

    raiseButton = new JButton("Raise");
    raiseButton.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          hello.raise();
        }
      }
    );
    bottomPanel.add(raiseButton);


    JLabel chipsLabel = new JLabel("Chips");
    bottomPanel.add(chipsLabel);

    chips = new JTextField(5);
    bottomPanel.add(chips);

    JLabel currentPot = new JLabel("Current Pot");
    bottomPanel.add(currentPot);

    pot = new JTextField(5);
    bottomPanel.add(pot);

    this.add(bottomPanel, BorderLayout.SOUTH);

    //Created a center panel to organize the members and chat
    centerPanel = new JPanel();
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

    //Center panel is added
    
    riverPanel = new JPanel();
    riverPanel.setLayout(new FlowLayout());
    
    centerPanel.add(riverPanel, BorderLayout.NORTH);

    JPanel gamingPanel = new JPanel();
    gamingPanel.setLayout(new BoxLayout(gamingPanel, BoxLayout.Y_AXIS));
    JLabel player1 = new JLabel("You");
    gamingPanel.add(player1);
    playerRivalHandPanel = new JPanel(new FlowLayout());
    gamingPanel.add(playerRivalHandPanel, BorderLayout.WEST);

    gamingPanel.add(new JLabel("Opponent"));
    JPanel rivalPanel = new JPanel(new FlowLayout());
    rivalPanel.add(createCardLabel(new Card(0,0)));
    rivalPanel.add(createCardLabel(new Card(0,0)));
    gamingPanel.add(rivalPanel);
    
    centerPanel.add(gamingPanel, BorderLayout.SOUTH);

    this.add(centerPanel, BorderLayout.CENTER);

  }

  private class ClientSide {

    private String ip;
    private int port;
    private PrintWriter out;
    private BufferedReader in;
    private Socket sock;
    private Thread t;

    public ClientSide(String ip, int port) {
      this.ip = ip;
      this.port = port;
      try {
        sock = new Socket(ip, port);
      } catch (Exception e) {
        System.err.println("Cannot connect");
      }

      try {
        out = new PrintWriter(sock.getOutputStream());
        out.println(ip + " " + port);

        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        t = new Reader(sock);
        t.start();
      } catch (Exception e) {
        System.err.println("IO Exception");
      }
    }

    public void fold(){
      out.println("Fold");
      out.flush();
    }

    public void raise(){
      out.println("Raise");
      out.flush();
    }
    
    //Match the bet that was made
    public void call(){
      out.println("Call");
      out.flush();
    }
  }

  private class Reader extends Thread {

    Socket sock;

    public Reader(Socket sock) {
      this.sock = sock;
    }

    public void run() {
      try {
        System.out.println("Reader started");
        BufferedReader in = new BufferedReader(
          new InputStreamReader(sock.getInputStream())
        );
        while (true) {
          String msg = in.readLine();
          if (msg.equals("Established connection")) {
            System.out.println(msg);
          }
          if(msg.equals("Receiving initial cards")){
            String cardOne = in.readLine();
            String[] parts = cardOne.split("_");
            
            String cardTwo = in.readLine();
            String[] partsTwo = cardTwo.split("_");

            int first = Integer.parseInt(parts[0]);
            int second = Integer.parseInt(parts[1]);

            int third = Integer.parseInt(partsTwo[0]);
            int fourth = Integer.parseInt(partsTwo[1]);

            playerRivalHandPanel.add(createCardLabel(new Card(first, second)));
            playerRivalHandPanel.add(createCardLabel(new Card(third, fourth)));
            centerPanel.revalidate();
          }
          
          if(msg.equals("Receiving first river")){
            String cardOne = in.readLine();
            String[] parts = cardOne.split("_");
            
            String cardTwo = in.readLine();
            String[] partsTwo = cardTwo.split("_");

            int first = Integer.parseInt(parts[0]);
            int second = Integer.parseInt(parts[1]);

            int third = Integer.parseInt(partsTwo[0]);
            int fourth = Integer.parseInt(partsTwo[1]);

            riverPanel.add(createCardLabel(new Card(first, second)));
            riverPanel.add(createCardLabel(new Card(third, fourth)));
            centerPanel.revalidate();
          }
        if(msg.equals("Receiving new river card")){
          String cardOne = in.readLine();
          String[] parts = cardOne.split("_");
          int first = Integer.parseInt(parts[0]);
          int second = Integer.parseInt(parts[1]);
          riverPanel.add(createCardLabel(new Card(first, second)));
          centerPanel.revalidate();
        }
        if(msg.equals("Lost")){
          JOptionPane.showMessageDialog(null, "You lost. Try again.");
        }
        if(msg.equals("Won")){
          JOptionPane.showMessageDialog(null, "All players folded. You won!!");
        }
        
        if(msg.equals("Clear")){
          System.out.println("Made it here");
          playerRivalHandPanel.removeAll();
          riverPanel.removeAll();
          centerPanel.revalidate();
        }
          
        }
      } catch (Exception e) {
        System.err.println("Reader failed");
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