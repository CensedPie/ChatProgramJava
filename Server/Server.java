package server;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import message.Message;

public class Server {

    private static int ID;
    private ArrayList<ClientThread> clients;
    private SimpleDateFormat sdf;
    private int port;
    private boolean running;
    
    public Server(int port) {
        this.port = port;
        sdf = new SimpleDateFormat("HH:mm:ss");
        clients = new ArrayList();
    }
    
    public void start() {
        running = true;
        try {
            ServerSocket server = new ServerSocket(port);
            while(running) {
                System.out.println("Server waiting for Clients on port " + port + ".");
                Socket socket = server.accept();
                if(!running) {
                    break;
                }
                ClientThread t = new ClientThread(socket);
                clients.add(t);
                t.start();
            }
            try {
                server.close();
                for(int i = 0; i < clients.size(); i++) {
                    try {
                        clients.get(i).sInput.close();
                        clients.get(i).sOutput.close();
                        clients.get(i).socket.close();
                    }
                    catch(IOException e) {
                        
                    }
                }
            }
            catch(Exception e) {
                System.out.println("Exception closing server and clients: " + e);
            }
        }
        catch(IOException e) {
            String error = sdf.format(new Date()) + " Exception on new ServerSocket: " + e;
            System.out.println(error);
        }
    }
    
    void stop() {
        running = false;
        try {
            new Socket("localhost", port);
        }
        catch(Exception e) {
            
        }
    }
    
    private synchronized void broadcast(String message, String username) {
        String time = sdf.format(new Date());
        String messageLf =  username + " - " + time + ": \n" + message;
        System.out.println(messageLf);
        for(int i = clients.size(); --i >=0;) {
            if(!clients.get(i).writeMsg(messageLf)) {
                clients.remove(i);
                System.out.println("Disconnected Client " + clients.get(i).username);
            }
        }
    }
    synchronized void remove(int id) {
        for(int i = 0; i < clients.size(); ++i) {
            if(clients.get(i).id == id) {
                clients.remove(i);
                return;
            }
        }
    }
    
    public static void main(String[] args) {
        int portNumber = 1204;
        Server server = new Server(portNumber);
        server.start();
    }
    
    class ClientThread extends Thread {
        Socket socket;
        ObjectInputStream sInput;
        ObjectOutputStream sOutput;
        int id;
        String username;
        String password;
        Message m;
        String date;
        File users;
        FileWriter fWriter;
        PrintWriter pWriter;
        FileReader fReader;
        BufferedReader bReader;
        
        ClientThread(Socket socket) {
            id = ++ID;
            this.socket = socket;
            System.out.println("Thread trying to create Object Input/Output Streams");
            try {
                users = new File("usersData.dat");
                users.createNewFile();
                fReader = new FileReader(users);
                bReader = new BufferedReader(fReader);
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput = new ObjectInputStream(socket.getInputStream());
                username = (String) sInput.readObject();
                password = (String) sInput.readObject();
                boolean dnf = false;
                String read;
                String readSplit[] = new String[2];
                while((read = bReader.readLine()) != null) {
                    readSplit = read.split(" ");
                    if(username.equals(readSplit[0]) && !(password.equals(readSplit[1]))) {
                        bReader.close();
                        fReader.close();
                        sOutput.writeByte(1);
                        sOutput.flush();
                        remove(id);
                        close();
                    }
                    else if(username.equals(readSplit[0]) && password.equals(readSplit[1])) {
                        sOutput.writeByte(2);
                        sOutput.flush();
                        dnf = true;
                    }
                }
                bReader.close();
                fReader.close();
                if(!dnf) {
                    sOutput.writeByte(2);
                    sOutput.flush();
                    fWriter = new FileWriter(users, true);
                    pWriter = new PrintWriter(fWriter);
                    pWriter.println(username + " " + password);
                    pWriter.flush();
                    pWriter.close();
                    fWriter.close();
                }
                
                System.out.println(username + " just connected with password " + password);
            }
            catch(IOException e) {
                System.out.println("Exception creating new Input/Output Streams: " + e);
            }
            catch(ClassNotFoundException e) {
                
            }
            date = new Date().toString();
        }
        
        @Override
        public void run() {
            boolean running = true;
            while(running) {
                try {
                    m = (Message) sInput.readObject();
                }
                catch(IOException e) {
                    System.out.println(username + " Exception reading Streams: " + e);
                    break;
                }
                catch(ClassNotFoundException e1) {
                    break;
                }
                String message = m.getMsg();
                
                broadcast(message, username);
            }
            remove(id);
            close();
        }
        
        private void close() {
            try {
                if(sOutput != null) {
                    sOutput.close();
                }
                if(sInput != null) {
                    sInput.close();
                }
                if(socket != null) {
                    socket.close();
                }
            }
            catch(Exception e) {
                
            }
        }
        private boolean writeMsg(String msg) {
            if(!socket.isConnected()) {
                close();
                return false;
            }
            try {
                sOutput.writeObject(msg);
            }
            catch(IOException e) {
                System.out.println("Error sending message to " + username);
                System.out.println(e.toString());
            }
            return true;
        }
    }
    
}
