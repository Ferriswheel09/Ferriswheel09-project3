import java.util.*;
import java.net.*;
import java.io.*;
public class PokerGameServer{

    ServerSocket serverSock;
    ArrayList<Socket> connections;
    Socket connectionOne;
    Socket connectionTwo;
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
                                pw.println("Lost");
                                pw.flush();

                            }
                            else{
                                PrintWriter pw = new PrintWriter(connections.get(i).getOutputStream());
                                pw.println("Won");
                                pw.flush();
                            }
                        }
                        playerFold = true;
                    }
                    
                    if(msg.equals("Raise")){
                        for(int i=0; i<users; i++){
                            if(id == i && id == 0){
                                player1Confirm=true;
                                System.out.println("Player 1");
                                System.out.println(player1Confirm);
                            }
                            else if(id == i && id ==1){
                                player2Confirm=true;
                                System.out.println("Player 2");
                                System.out.println(player2Confirm);
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
        String[] player1Cards;
        String[] player2Cards;
        String[] community;
        boolean preflop;
        boolean flop;
        boolean turn;
        boolean river;
        
        
        public GameTracker(){
            memberNumbers = 2;
            player1Cards = new String[2];
            player2Cards = new String[2];
            community = new String[5];
            
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
                                player1Cards[0] = deck.drawCard();
                                player1Cards[1] = deck.drawCard();
                                out.println(player1Cards[0]);
                                out.println(player1Cards[1]);
                            }

                            if(i == 1){
                                player2Cards[0] = deck.drawCard();
                                player2Cards[1] = deck.drawCard();
                                out.println(player2Cards[0]);
                                out.println(player2Cards[1]);
                            }
                            
                            out.flush();
                        }
                        initial=false;
                    }


                        while(preflop){
                            if(player1Confirm && player2Confirm && playerFold == false){
                                String str = deck.drawCard();
                                String strTwo = deck.drawCard();
                                community[0] = str;
                                community[1] = strTwo;
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
                                community[2] = str;
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
                                community[3] = str;
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
                                community[4] = str;
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
                    }
                        
                    
                    
                }
            }

            catch(Exception e){
                System.err.println("GameTracker failed");
            }

        }
    }
    
}