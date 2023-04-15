import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.*;

public class PokerGameGUI extends JFrame {

  private JPanel mainPanel;
  private JPanel gamePanel;
  private JTextField name;
  private JTextField ipadd;
  private JTextField port;
  private JPanel[] cardPanels;
  private JLabel[][] cardLabels;

  private JLabel[] playerLabels;
  private JLabel[] playerMoneyLabels;
  private JLabel potLabel;
  private JButton betButton;
  private JButton callButton;
  private JTextField callAmount;
  private JButton foldButton;
  private JButton connect;

  private Socket socket;
  private PrintWriter out;
  private BufferedReader in;

  public PokerGameGUI() {
    super();
    this.setTitle("Poker Game");
    this.setSize(800, 600);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());

    gamePanel = new JPanel();
    gamePanel.setLayout(new GridLayout(5, 3));

    // Create player labels
    playerLabels = new JLabel[4];
    playerMoneyLabels = new JLabel[4];
    cardPanels = new JPanel[4];
    cardLabels = new JLabel[4][2];
    for (int i = 0; i < 4; i++) {
      playerLabels[i] = new JLabel("Player " + (i + 1));
      playerMoneyLabels[i] = new JLabel("$1000");
      cardPanels[i] = new JPanel();
      cardPanels[i].setLayout(new FlowLayout());
      cardLabels[i][0] = new JLabel();
      cardLabels[i][1] = new JLabel();
      cardPanels[i].add(cardLabels[i][0]);
      cardPanels[i].add(cardLabels[i][1]);
      gamePanel.add(playerLabels[i]);
      gamePanel.add(new JLabel(""));
      gamePanel.add(playerMoneyLabels[i]);
    }

    // Create pot label
    potLabel = new JLabel("Pot: $0");
    gamePanel.add(new JLabel(""));
    gamePanel.add(potLabel);
    gamePanel.add(new JLabel(""));

    // Create betting buttons and connection buttons
    JPanel bottomPanel = new JPanel(new FlowLayout());
    JPanel topPanel = new JPanel(new FlowLayout());
    name = new JTextField(5);
    ipadd = new JTextField(5);
    port = new JTextField(5);
    betButton = new JButton("Bet");
    callButton = new JButton("Call");
    callAmount = new JTextField(5);
    foldButton = new JButton("Fold");
    connect = new JButton("Connect");

    topPanel.add(new JLabel("Name"));
    topPanel.add(name);
    topPanel.add(new JLabel("IP"));
    topPanel.add(ipadd);
    topPanel.add(new JLabel("Port"));
    topPanel.add(port);
    topPanel.add(connect);
    bottomPanel.add(betButton);
    bottomPanel.add(callButton);
    bottomPanel.add(callAmount);
    bottomPanel.add(foldButton);

    mainPanel.setBackground(Color.green);
    mainPanel.add(gamePanel, BorderLayout.CENTER);
    mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    mainPanel.add(topPanel, BorderLayout.NORTH);

    setContentPane(mainPanel);
    setVisible(true);

    // Connect to server
    connect.addActionListener(e -> {
      if (connect.getText().equals("Connect")) {
        //Sets all the text to flip around
        connect.setText("Disconnect");
        try {
          int portNum = Integer.parseInt(port.getText());
          socket = new Socket(ipadd.getText(), portNum);
          out = new PrintWriter(socket.getOutputStream());
          out.println(name.getText());
          in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          new Thread(new ServerListener()).start();
        } catch (Exception f) {
          System.err.println("Error connecting");
          JOptionPane.showMessageDialog(null, "Error connecting");
        }
      } else {
        connect.setText("Connect");
      }
    });

    // Add button listeners
    betButton.addActionListener(e -> {
      out.println("bet");
      out.flush();
    });
    callButton.addActionListener(e -> {
      out.println("call");
      out.flush();
    });
    foldButton.addActionListener(e -> {
      out.println("fold");
      out.flush();
    });
  }

  private class ServerListener implements Runnable {

    public void run() {
      try {
        while (true) {
          String message = in.readLine();
          Scanner scanner = new Scanner(message);
          String command = scanner.next();
          if (command.equals("update")) {
            // Update game state
            for (int i = 0; i < 4; i++) {
              String cards = scanner.next();
              String[] cardStrings = cards.split(",");
              ImageIcon[] cardImages = new ImageIcon[2];
              for (int j = 0; j < 2; j++) {
                cardImages[j] =
                  new ImageIcon(
                    getClass().getResource("/cards/" + cardStrings[j] + ".png")
                  );
                cardLabels[i][j].setIcon(cardImages[j]);
              }
              int money = scanner.nextInt();
              playerMoneyLabels[i].setText("$" + money);
            }
            int pot = scanner.nextInt();
            potLabel.setText("Pot: $" + pot);
          } else if (command.equals("start")) {
            for (int i = 0; i < 4; i++) {
              String cards = scanner.next();
              String[] cardStrings = cards.split(",");
              ImageIcon[] cardImages = new ImageIcon[2];
              for (int j = 0; j < 2; j++) {
                cardImages[j] =
                  new ImageIcon(
                    getClass().getResource("/cards/" + cardStrings[j] + ".png")
                  );
                cardLabels[i][j].setIcon(cardImages[j]);
              }
              int money = scanner.nextInt();
              playerMoneyLabels[i].setText("$" + money);
            }
            int pot = scanner.nextInt();
            potLabel.setText("Pot: $" + pot);
          } else if (command.equals("end")) {
            // End game
            JOptionPane.showMessageDialog(PokerGameGUI.this, "Game Over!");
            System.exit(0);
          }
        }
      } catch (Exception e) {
        System.err.println("ServerListener failed");
      }
    }
  }
}
