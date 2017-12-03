/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_net;



import java.io.*;
import java.net.Socket;
/**
 *
 * @author davidren
 */
public class connection {

    public connection(){
        
    }
    
    public void fileSending(String path, String filename) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8080);

        DataInputStream input=new DataInputStream(socket.getInputStream());
        
        DataOutputStream output=new DataOutputStream(socket.getOutputStream());

        output.writeUTF(filename);
        output.flush();

        File file = new File(path);
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
    }


    public void fileReceiving() throws IOException {
        Socket socket = new Socket("127.0.0.1", 8080);

        DataInputStream input=new DataInputStream(socket.getInputStream());
        DataOutputStream output=new DataOutputStream(socket.getOutputStream());

        String filename = input.readUTF();

        byte fileByte[]=new byte [1024];
        FileOutputStream fileOutput=new FileOutputStream(new File("src/ClientDownload/" + filename),true);
        
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
    }
}
