/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_net;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author davidren
 */
public class transfer {
    public transfer(){
        
    }
    public void fileReceiving() throws IOException {
        
        
        ServerSocket serverSocket=new ServerSocket(8080);
        System.out.println("Waiting for connection...");
        Socket socket = serverSocket.accept();
        
        DataInputStream input=new DataInputStream(socket.getInputStream());
        DataOutputStream output=new DataOutputStream(socket.getOutputStream());
        
        String filename = input.readUTF();

        byte fileByte[]=new byte [1024];
        FileOutputStream fileOutput=new FileOutputStream(new File("src/ServerFileCatalog/" + filename),true);
        
        long bytesRead;
        //start receiving
        do {
            bytesRead = input.read(fileByte, 0, fileByte.length);
            
            fileOutput.write(fileByte,0,fileByte.length);
            
        } while(!(bytesRead<1024));

        //close connection
        fileOutput.close();
        output.close();
        socket.close();
        serverSocket.close();

    }
    public void fileSending(String filename) throws IOException{
        
        
        ServerSocket serverSocket=new ServerSocket(8080);
        System.out.println("Waiting for connection...");
        Socket socket = serverSocket.accept();
        
        
        DataInputStream input=new DataInputStream(socket.getInputStream());
        DataOutputStream output=new DataOutputStream(socket.getOutputStream());
        
        System.out.println("Sending File: " + filename);
        output.writeUTF(filename);
        output.flush();

        File file = new File("src/ServerFileCatalog/" + filename);
        FileInputStream fileInput = 
                new FileInputStream(file);
        
        byte fileByte[]=new byte [1024];
        int check;

        while((check = fileInput.read(fileByte)) != -1){
            
            output.write(fileByte, 0, check);
            output.flush();
        }

        fileInput.close();
        output.flush();
        input.close();
        socket.close();
        serverSocket.close();
    }
}
