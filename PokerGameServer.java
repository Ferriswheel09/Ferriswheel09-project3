import java.util.*;
import java.net.*;
import java.io.*;
public class PokerGameServer{

    ServerSocket serverSock;
    ArrayList<Socket> connections;
    int index;
    volatile boolean isDead;

    public PokerGameServer(int port){

        //Instantiates all of the components, including the server socket, member names, and all active connections
        try{
            //Adding comments
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

                        //After it does the removal, it sends all active connections the active list 
                        //While removing one of the names 
                        for(int i=0; i<connections.size(); i++){
                            PrintWriter tempOut = new PrintWriter(connections.get(i).getOutputStream());
                            tempOut.println("START_CLIENT_LIST");
                            for(int j=0; j<members.size(); j++){
                                tempOut.println(members.get(j));
                            }
                            tempOut.println("END_CLIENT_LIST");
                            tempOut.flush();
                        }
                    }

                    //Otherwise, we can assume that what we received was a message, and we can send it to all active users
                    else{
                        
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

 

    
}