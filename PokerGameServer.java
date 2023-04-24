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
                    
                String name = in.readLine();
                members.add(name);
                        connections.add(clientSock);

                        //After that, a writer is temporarily created to give the joining member all active clients currently
                        PrintWriter pw = new PrintWriter(clientSock.getOutputStream());
                        pw.println("START_CLIENT_LIST");
                        for(int i=0; i<members.size(); i++){
                            pw.println(members.get(i));
                        }
                        pw.println("END_CLIENT_LIST");
                        pw.flush();

                         //start the thread
                        (new ClientHandler(clientSock, name)).start();
                        (new ClientList(clientSock)).start();
                }
                                //continue looping
            }catch(Exception e){} //exit serve if exception
        }
    }

    private class ClientHandler extends Thread{

        Socket sock;
        String name;
        public ClientHandler(Socket sock, String name){
            this.sock=sock;
            this.name=name;
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
                    for(int i=0; i<connections.size(); i++){
                        PrintWriter tempOut = new PrintWriter(connections.get(i).getOutputStream());
                        tempOut.println("["+ name + "] " + msg);
                        tempOut.flush();
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
        GWackChannel server = new GWackChannel(port);
        server.serve();
    }

    //Created a separated ClientList to maintain the state of the clients
    private class ClientList extends Thread{
        Socket sock;
        volatile int membersNumber;

        public ClientList(Socket sock){
            this.sock = sock;
            membersNumber = members.size();
        }

        public synchronized void run(){
            try{
                while(true){
                    //If there are more members in the arraylist than the instantiated number
                    if(membersNumber<members.size()){
                        for(int i=0; i<connections.size(); i++){

                            //We create a new connection to the output stream, and add that new member into the group
                            PrintWriter tempOut = new PrintWriter(connections.get(i).getOutputStream());
                            tempOut.println("START_CLIENT_LIST");
                            for(int j=0; j<members.size(); j++){
                                tempOut.println(members.get(j));
                            }
                            tempOut.println("END_CLIENT_LIST");
                            tempOut.flush();
                        }
                        membersNumber = members.size();
                    } 
                        

                    //If there are less members in the members arraylist, we do the same thing. 
                    //This is for the case where a member actually leaves
                    if(membersNumber>members.size()){
                        for(int i=0; i<connections.size(); i++){
                            PrintWriter tempOut = new PrintWriter(connections.get(i).getOutputStream());
                            tempOut.println("START_CLIENT_LIST");
                            for(int j=0; j<members.size(); j++){
                                tempOut.println(members.get(j));
                            }
                            tempOut.println("END_CLIENT_LIST");
                            tempOut.flush();
                        }
                        membersNumber = members.size();
                    }

                }
            }
        

            catch(Exception e){
                System.out.println("ClientList failed");
            }
        }


    }
}