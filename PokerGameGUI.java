import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.net.URL;
import java.util.*;
import javax.swing.*;

public class PokerGameGUI extends JFrame {

  private String status;
  private JTextField ipadd;
  private JTextField port;
  private JButton foldButton;
  private JButton callButton;
  private JButton raiseButton;
  private JTextField pot;
  private JTextField chips;

  private JPanel playerRivalHandPanel;
  private JPanel riverPanel;
  private JPanel centerPanel;

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
        if(msg.equals("Lost Fold")){
          JOptionPane.showMessageDialog(null, "You folded. Try again.");
        }
        if(msg.equals("Won Fold")){
          JOptionPane.showMessageDialog(null, "All players folded. You won!!");
        }
        
        if(msg.equals("Clear")){
          playerRivalHandPanel.removeAll();
          riverPanel.removeAll();
          centerPanel.revalidate();
        }

        if(msg.equals("Won Showdown")){
          JOptionPane.showMessageDialog(null, "Better hand. You won!!");
        }

        if(msg.equals("Lost Showdown")){
          JOptionPane.showMessageDialog(null, "Weak hand. Try again.");
        }
          
        }
      } catch (Exception e) {
        System.err.println("Reader failed");
      }
    }
  }
}
