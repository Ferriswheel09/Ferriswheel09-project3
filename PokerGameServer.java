import java.util.*;
import java.net.*;
import java.io.*;
public class PokerGameServer{

    ServerSocket serverSock;
    ArrayList<Socket> connections;
    ArrayList<String> members;
    int index;
    int users;
    boolean playerFold;
    volatile boolean player1Confirm;
    volatile boolean player2Confirm;

    public PokerGameServer(int port){

        //Instantiates all of the components, including the server socket, member names, and all active connections
        try{
            serverSock = new ServerSocket(port);
            users = 0;
            index = 0;
            members = new ArrayList<String>();
            connections = new ArrayList<Socket>();
            System.out.println("PokerServer started on port " + port);
        }
        catch(Exception e){
            System.err.println("Cannot establish server socket");
            System.exit(1);
        }
    }

    
    public void serve(){

        while(true){
            
            try{
                //accept incoming connection
                Socket clientSock = serverSock.accept();
                System.out.println("New connection: "+ clientSock.getRemoteSocketAddress());
                
                //If the first line is secret, then a password is created
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
               
                        connections.add(clientSock);
                        

                
                         //start the thread
                        (new ClientHandler(clientSock, users)).start();   
                        
                        System.out.println("Connection " + users);
                        users++;

                    if(connections.size() == 2){
                        playerFold = false;
                        (new GameTracker()).start();
                        System.out.println("GameTracker Thread Started");
                    }

               
                
                
               
                
                //continue looping
            }catch(Exception e){} //exit serve if exception
        }
    }

    private class ClientHandler extends Thread{

        Socket sock;
        int id;

        public ClientHandler(Socket sock, int id){
            this.sock=sock;
            this.id = id;
        }

        public void run(){
            BufferedReader in=null;
            int tally = 0;
            try{
                //Creates the input/output corresponding to the sockets stream
                boolean firstTime = true;
               

                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                //read and echo back forever!
                while(true){
                    //Checks first to see if the message is null
                    String msg = in.readLine();
                    
                    if(msg.equals("Fold")){
                        for(int i=0; i<users; i++){
                            if(id == i){
                                PrintWriter pw = new PrintWriter(connections.get(i).getOutputStream());
                                pw.println("Lost Fold");
                                pw.flush();

                            }
                            else{
                                PrintWriter pw = new PrintWriter(connections.get(i).getOutputStream());
                                pw.println("Won Fold");
                                pw.flush();
                            }
                        }
                        playerFold = true;
                    }
                    
                    if(msg.equals("Raise")){
                        for(int i=0; i<users; i++){
                            if(id == i && id == 0){
                                player1Confirm=true;
                            }
                            else if(id == i && id ==1){
                                player2Confirm=true;
                            }
                        }
                    }
                }

            }catch(Exception e){}

            //note the loss of the connection
            System.out.println("Connection lost: "+sock.getRemoteSocketAddress());
            System.out.println("This error is occurring");

        }

    }

    public static void main(String args[]){
        int port = Integer.parseInt(args[0]);
        PokerGameServer server = new PokerGameServer(port);
        server.serve();
    }

 
    private class GameTracker extends Thread{
        int memberNumbers;
        boolean cardDemo = true;
        HashSet<String> player1Cards;
        HashSet<String> player2Cards;
        HashSet<String> community;
        boolean preflop;
        boolean flop;
        boolean turn;
        boolean river;
        
        
        public GameTracker(){
            memberNumbers = 2;
            player1Cards = new HashSet<String>();
            player2Cards = new HashSet<String>();
            community = new HashSet<String>();
            
            preflop = true;
            flop = true;
            turn = true;
            river = true;
            

        }

        public void run(){
            try{
                boolean initial = true;
                boolean endgame = false;
                while(true){
                    DeckOfCards deck = new DeckOfCards();
                    deck.shuffle();
                   
                    if(initial){
                        for(int i=0; i<connections.size(); i++){
                            PrintWriter out = new PrintWriter(connections.get(i).getOutputStream());
                            out.println("Receiving initial cards");
                            if(i == 0){
                                String str = deck.drawCard();
                                String strTwo = deck.drawCard();
                                player1Cards.add(str);
                                player1Cards.add(strTwo);
                                out.println(str);
                                out.println(strTwo);
                            }

                            if(i == 1){
                                String str = deck.drawCard();
                                String strTwo = deck.drawCard();
                                player2Cards.add(str);
                                player2Cards.add(strTwo);
                                out.println(str);
                                out.println(strTwo);
                            }
                            
                            out.flush();
                        }
                        initial=false;
                    }


                        while(preflop){
                            if(player1Confirm && player2Confirm && playerFold == false){
                                String str = deck.drawCard();
                                String strTwo = deck.drawCard();
                                community.add(str);
                                community.add(strTwo);
                                for(int i=0; i<connections.size(); i++){
                                    PrintWriter pw = new PrintWriter(connections.get(i).getOutputStream());
                                    pw.println("Receiving first river"); 
                                    pw.println(str);
                                    pw.println(strTwo);
                                    pw.flush();
                                }
                                preflop = false;
                                player1Confirm = false;
                                player2Confirm = false;
                            }

                            if(playerFold){
                                preflop = false;
                                flop=false;
                                turn=false;
                                river=false;
                                endgame=true;
                            }
                        }

                        while(flop){
                            if(player1Confirm && player2Confirm && playerFold==false){
                                String str = deck.drawCard();
                                community.add(str);
                                for(int i=0; i<connections.size(); i++){
                                    PrintWriter pw = new PrintWriter(connections.get(i).getOutputStream());
                                    pw.println("Receiving new river card");
                                    pw.println(str);
                                    pw.flush();
                                }
                                flop = false;
                                player1Confirm = false;
                                player2Confirm = false;
                            }
                             if(playerFold){
                                flop=false;
                                turn=false;
                                river=false;
                                endgame=true;
                            }
                        }

                        while(turn){
                            if(player1Confirm && player2Confirm && playerFold == false){
                                String str = deck.drawCard();
                                community.add(str);
                                for(int i=0; i<connections.size(); i++){
                                    PrintWriter pw = new PrintWriter(connections.get(i).getOutputStream());
                                    pw.println("Receiving new river card");
                                    pw.println(str);
                                    pw.flush();
                                }
                                turn = false;
                                player1Confirm = false;
                                player2Confirm = false;
                            }
                             if(playerFold){
                                turn=false;
                                river=false;
                                endgame=true;
                            }
                        }

                        while(river){
                            if(player1Confirm && player2Confirm && playerFold==false){
                                String str = deck.drawCard();
                                community.add(str);
                                for(int i=0; i<connections.size(); i++){
                                    PrintWriter pw = new PrintWriter(connections.get(i).getOutputStream());
                                    pw.println("Receiving new river card");
                                    pw.println(str);
                                    pw.flush();
                                }
                                river = false;
                                player1Confirm = false;
                                player2Confirm = false;
                                endgame = true;
                            }
                             if(playerFold){
                                river=false;
                                endgame = true;
                                
                            }
                            
                        }

                    //Todo: add an extra step that, if neither player folded, checks conditions and declares a winner
                    
                    
                    
                    //If it gets to the end, it means the game is over
                    if(endgame){

                    HashSet player1CardsConverted = new HashSet<Card>();
                    HashSet player2CardsConverted = new HashSet<Card>();

                    for(String s: player1Cards){
                        String[] parts = s.split("_");
                        int first = Integer.parseInt(parts[0]);
                        int second = Integer.parseInt(parts[1]);    
                        player1CardsConverted.add(new Card(first, second));
                    }

                    for(String s: player2Cards){
                        String[] parts = s.split("_");
                        int first = Integer.parseInt(parts[0]);
                        int second = Integer.parseInt(parts[1]);    
                        player2CardsConverted.add(new Card(first, second));
                    }
                    

                    Hand calculatorPlayerOne = new Hand(player1CardsConverted);
                    int playerOneScore = calculatorPlayerOne.calculateHandValue();

                    Hand calculatorPlayerTwo = new Hand(player2CardsConverted);
                    int playerTwoScore = calculatorPlayerTwo.calculateHandValue();

                    if(playerOneScore > playerTwoScore){
                        for(int i=0; i<connections.size(); i++){
                            if(i == 0){
                                PrintWriter pw = new PrintWriter(connections.get(i).getOutputStream());
                                pw.println("Won Showdown");
                                pw.flush();
                            }
                            else{
                                PrintWriter pw = new PrintWriter(connections.get(i).getOutputStream());
                                pw.println("Lost Showdown");
                                pw.flush();
                            }
                        }
                    }

                    else{
                        for(int i=0; i<connections.size(); i++){
                            if(i == 1){
                                PrintWriter pw = new PrintWriter(connections.get(i).getOutputStream());
                                pw.println("Won Showdown");
                                pw.flush();
                            }
                            else{
                                PrintWriter pw = new PrintWriter(connections.get(i).getOutputStream());
                                pw.println("Lost Showdown");
                                pw.flush();
                            }
                        }
                    }

                    initial=true;
                    flop=true;
                    river=true;
                    turn=true;
                    preflop=true;
                    for(int i=0; i<connections.size(); i++){
                        PrintWriter pw = new PrintWriter(connections.get(i).getOutputStream());
                        pw.println("Clear");
                        pw.flush();
                    }
                    endgame = false;
                    playerFold = false;

                    player1Cards.clear();
                    player2Cards.clear();
                    }
                        
                    
                    
                }
            }

            catch(Exception e){
                System.err.println("GameTracker failed");
            }

        }
    }
    
}