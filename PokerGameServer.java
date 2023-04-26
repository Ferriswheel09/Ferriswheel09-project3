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
    DeckOfCards deck;

    public PokerGameServer(int port){

        //Instantiates all of the components, including the server socket, member names, and all active connections
        try{
            serverSock = new ServerSocket(port);
            index = 0;
            members = new ArrayList<String>();
            connections = new ArrayList<Socket>();
            deck = new DeckOfCards();
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
                        if(connectionOne == null){
                            connectionOne = clientSock;
                            System.out.println("connectionOne established");
                        }
                        else{
                            connectionTwo = clientSock;
                            System.out.println("connectionTwo established");
                        }

                
                         //start the thread
                        (new ClientHandler(clientSock)).start();   

                    if(connections.size() == 2){
                        deck.shuffle();
                        (new GameTracker()).start();
                        System.out.println("GameTracker Thread Started");
                    }

               
                
                
               
                
                //continue looping
            }catch(Exception e){} //exit serve if exception
        }
    }

    private class ClientHandler extends Thread{

        Socket sock;

        public ClientHandler(Socket sock){
            this.sock=sock;
        }

        public void run(){
            PrintWriter outConnectionOne=null;
            PrintWriter outConnectionTwo=null;
            BufferedReader in=null;
            int tally = 0;
            try{
                //Creates the input/output corresponding to the sockets stream
                outConnectionOne = new PrintWriter(connectionOne.getOutputStream());
                boolean firstTime = true;
               

                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                //read and echo back forever!
                while(true){
                if(connectionTwo != null){
                    outConnectionTwo = new PrintWriter(connectionTwo.getOutputStream());
                }
                    //Checks first to see if the message is null
                    String msg = in.readLine();
                   
                    
                    if(msg.equals("Test") && firstTime){
                        outConnectionOne.println("Receiving first river");
                        outConnectionTwo.println("Receiving first river");
                        String str = deck.drawCard();
                        String strTwo = deck.drawCard();
                        outConnectionOne.println(str);
                        outConnectionTwo.println(str);
                        outConnectionOne.println(strTwo);
                        outConnectionTwo.println(strTwo);
                        outConnectionOne.flush();
                        outConnectionTwo.flush();
                        firstTime = false;
                        tally+=2;
                    }
                    else if(msg.equals("Test") && tally<5){
                        outConnectionOne.println("Receiving new river card");
                        outConnectionTwo.println("Receiving new river card");
                        String str = deck.drawCard();
                        outConnectionOne.println(str);
                        outConnectionTwo.println(str);
                        outConnectionOne.flush();
                        outConnectionTwo.flush();
                        tally++;

                    }
                }

            }catch(Exception e){}

            //note the loss of the connection
            System.out.println("Connection lost: "+sock.getRemoteSocketAddress());
            System.out.println("This exception is occurring");

        }

    }

    public static void main(String args[]){
        int port = Integer.parseInt(args[0]);
        PokerGameServer server = new PokerGameServer(port);
        server.serve();
    }

 
    private class GameTracker extends Thread{
        int memberNumbers;
        boolean handshake = true;
        boolean cardDemo = true;
        
        public GameTracker(){
            memberNumbers = 2;
            
        }

        public void run(){
            try{
                boolean initial = true;
                while(true){
                   
                    if(handshake){
                        for(int i=0; i<connections.size(); i++){    
                            PrintWriter out = new PrintWriter(connections.get(i).getOutputStream());
                            out.println("Two players joined. Game will now begin!");
                            out.flush();
                            handshake = false;
                        }
                    }

                    if(initial){
                        for(int i=0; i<connections.size(); i++){
                            PrintWriter out = new PrintWriter(connections.get(i).getOutputStream());
                            out.println("Receiving initial cards");
                            out.println(deck.drawCard());
                            out.println(deck.drawCard());
                            out.flush();
                        }
                        initial=false;
                    }


                    
                        
                    
                    
                }
            }

            catch(Exception e){
                System.err.println("GameTracker failed");
            }

        }
    }
    
}