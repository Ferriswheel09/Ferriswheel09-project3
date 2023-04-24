import java.util.*;
import java.net.*;
import java.io.*;
public class PokerGameServer{

    ServerSocket serverSock;
    ArrayList<Socket> connections;
    ArrayList<String> members;
    int index;
    volatile boolean isDead;

    public PokerGameServer(int port){

        //Instantiates all of the components, including the server socket, member names, and all active connections
        try{
            serverSock = new ServerSocket(port);
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
                        (new ClientHandler(clientSock)).start();   

                    if(connections.size() == 2){
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
            PrintWriter out=null;
            BufferedReader in=null;
            try{
                //Creates the input/output corresponding to the sockets stream
                out = new PrintWriter(sock.getOutputStream());
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                //read and echo back forever!
                while(true){
                    //Checks first to see if the message is null
                    String msg = in.readLine();
                    if(msg == null){
                        out.close();
                        in.close();
                        sock.close();

                        //After the socket is closed, once it reaches this condition
                        //If the connection arraylist finds the same closed socket, it removes it from the arraylist
                        //Of name and active connections
                        for(int i=0; i<connections.size(); i++){
                            if(connections.get(i) == sock){
                                connections.remove(i);
                                members.remove(i);
                            }

                        }


                    }  
                }

            }catch(Exception e){}

            //note the loss of the connection
            System.out.println("Connection lost: "+sock.getRemoteSocketAddress());

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
                while(true){
                   
                    if(handshake){
                        for(int i=0; i<connections.size(); i++){    
                            PrintWriter out = new PrintWriter(connections.get(i).getOutputStream());
                            out.println("Established connection");
                            out.flush();
                            handshake = false;
                        }
                    }
                    if(cardDemo){
                        for(int i=0; i<connections.size(); i++){
                            PrintWriter out = new PrintWriter(connections.get(i).getOutputStream());
                            out.println("7_3");
                            out.println("13_1");
                            out.flush();
                            cardDemo = false;
                        }
                        
                    }
                    
                }
            }

            catch(Exception e){
                System.err.println("GameTracker failed");
            }

        }
    }
    
}