package chatmain;

import java.net.*;
import java.io.*;
import java.util.*;
import message.Message;

public class Client {
    private ObjectInputStream sInput;
    private ObjectOutputStream sOutput;
    private Socket socket;
    private String server, username, password;
    private Queue<String> queue;
    private byte error;

    public String getUsername() {
        return username;
    }
    private int port;
    
    public Client(String username, String password) {
        server = "localhost";
        port = 1204;
        this.username = username;
        this.password = password;
        queue = new LinkedList<>();
    }
    
    public boolean start() {
        try {
            socket = new Socket(server, port);
        }
        catch(Exception e) {
            System.out.println("Error connection to server: " + e);
            return false;
        }
        String msg = "Connection accepted " + socket.getInetAddress() + " : " + socket.getPort();
        System.out.println(msg);
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        }
        catch(IOException e) {
            System.out.println("Exception creating new Input/Output Streams: " + e);
            return false;
        }
        try {
            
            sOutput.writeObject(username);
            sOutput.writeObject(password);
            error = sInput.readByte();
            System.out.println("Sent username and password and read error = " + error);
        }
        catch(IOException e) {
            System.out.println("Exception logging in : " + e);
            disconnect();
            return false;
        }
        new ListenFromServer().start();
        if(error == 1) {
            disconnect();
            return false;
        }
        else {
            System.out.println("About to return true");
            return true;
        }
    }
    
    public void sendMessage(Message msg) {
        try {
            sOutput.writeObject(msg);
        }
        catch(IOException e) {
            System.out.println("Exception writing to server: " + e);
        }
    }
    
    protected void disconnect() {
        try {
            if(sInput != null) {
                sInput.close();
            }
            if(sOutput != null) {
                sOutput.close();
            }
            if(socket != null) {
                socket.close();
            }
        }
        catch(Exception e) {
            
        }
    }
    
    public String getFromQueue() {
        return queue.poll();
    }
    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }
    
    class ListenFromServer extends Thread {
        @Override
        public void run() {
            System.out.println("Listening from server");
            while(true) {
                try {
                    String msg = (String) sInput.readObject();
                    queue.add(msg);
                }
                catch(IOException e) {
                    System.out.println("Server has closed the connection: " + e);
                    break;
                }
                catch(ClassNotFoundException e1) {
                    
                }
            }
        }
    }
}
