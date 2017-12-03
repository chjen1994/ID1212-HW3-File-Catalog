/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package client_view;
import java.util.*;
import java.nio.file.*;
import java.rmi.*;
import java.io.*;

import client_controller.controller;
import common.HW3DTO;
import common.fileDTO;
import common.fileSys;
import exception.badInput;
import static java.lang.System.console;
import java.nio.file.attribute.BasicFileAttributes;
import java.rmi.server.UnicastRemoteObject;


/**
 *
 * @author davidren
 */
public class interpreter implements Runnable{
    private static final String PROMPT = "> ";
    private final Scanner clientInput = new Scanner(System.in);
    
    
    private final ThreadSafeStdOut messageOut = new ThreadSafeStdOut();

    
    private fileSys fileSystem;
    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    private boolean acceptCommands = false;
    private boolean newLogin;
    private controller controller;

    
    public void start(fileSys fileSystem) throws RemoteException {
        this.fileSystem = fileSystem;
        
        if(acceptCommands) {
            return;
        }
        acceptCommands = true;
        newLogin = false;
        controller = new controller();
        new Thread(this).start();
    }
    
    @Override
    public void run(){
        startMessage();
        //fileDTO file = null;
        while(acceptCommands){
            try{
                System.out.print("> ");
                String Reader = bufferedReader.readLine();
                fileDTO file = fileSystem.FileDTO(Reader);
                if (!file.checkCommand()){
                printWrongCommand();
                }else {
                    switch (file.getCommand()){
                        case REGISTER:
                            try {
                                fileSystem.register(file.getUsername(), file.getPassword());
                                System.out.println("Registration success! Login to continue!\n");
                            } catch (Exception e){
                                System.out.println(e.getMessage());
                            }
                            break;
                        case LOGIN:
                            try {
                                fileSystem.login(file.getUsername(), file.getPassword());
                                loginProcess(file.getUsername());
                            } catch (Exception e){
                                System.out.println(e.getMessage());
                            }
                            break;
                        case QUIT:
                            System.out.println("Quit success!");
                            acceptCommands = false;
                            break;
                        default:
                            System.out.println("The " + file.getCommand() +
                                    " command cannot be performed now!");
                            break;
                    }
                }
            }catch (Exception e){
                if (acceptCommands) {
                    System.out.println("Failure with command processing");
                }
            }
                
                   
        
            }
        }
    
     private void loginProcess(String name){
        printLoginMsg(name);
        newLogin = true;
        while (newLogin){
            System.out.print("> ");
            try {
                String Reader = bufferedReader.readLine();
                
                fileDTO loginCmd = fileSystem.FileDTO(Reader);
                if (!loginCmd.checkCommand()){
                    printWrongCommandLogin();
                }else {
                    switch (loginCmd.getCommand()){
                        case UPLOAD: 
                            upload(loginCmd); 
                            break;
                        case DOWNLOAD: 
                            download(name, loginCmd.getFileName()); 
                            break;
                        case LIST: 
                            listFiles(name); 
                            break;
                        case DELETE: 
                            delete(name, loginCmd.getFileName()); 
                            break;
//                        case STATUS: 
//                            statusInfo(name, loginCmd.getFileName()); 
//                            break;
                        case UNREGISTER: 
                            unregister(name); 
                            break;
                        case LOGOUT: 
                            logout(name); 
                            break;
                        case QUIT: 
                            quit(name); 
                            break;
                        default:
                            System.out.println("The " + loginCmd.getCommand() +
                                    " command cannot be performed now!");
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void upload(fileDTO CmdAfterLogin){
        
        if (new File(CmdAfterLogin.getPath()).isFile() && CmdAfterLogin.readyToUpload()){
            long size = new File(CmdAfterLogin.getPath()).length();
            
            controller.prepareUpload(fileSystem, CmdAfterLogin.getUsername(),
                    CmdAfterLogin.getFileName(), size, CmdAfterLogin.getAccess(), CmdAfterLogin.getPermissions());
            
            controller.finishUpload(CmdAfterLogin.getPath(), CmdAfterLogin.getFileName());
        } else {
            System.out.println("Failed! An example of correct command is: " +
                    "\nupload \"C:\\Users\\chara\\Documents\\text.txt\" public write"+ CmdAfterLogin.readyToUpload());
        }
    }
    
    private void download(String username, String filename){

        controller.prepareDownload(fileSystem, username, filename);
        controller.finishDownload();
    }
    
    private void  listFiles(String username){
        try {
            System.out.println(fileSystem.list(username));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void  delete(String username, String filename){
        try {
            fileSystem.delete(username,filename);
            System.out.println("Deleted");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void unregister(String username) {
        try {
            fileSystem.unregister(username);
            System.out.println("Unregistered");
            newLogin = false;
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    private void logout(String username){
        try {
            fileSystem.logout(username);
            System.out.println("Loged out");
            startMessage();
            newLogin = false;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        
    }
   private void quit(String username){
       try {
            fileSystem.logout(username);
            System.out.println("Quit");
            newLogin = false;
            acceptCommands = false;
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
   } 
    
    

    
    private String readNextLine() {
        messageOut.print(PROMPT);
        return clientInput.nextLine();
    }
    private void startMessage(){
        System.out.println("\nThis is the File Catalog!\n" +
                "Commands avalable:\n\n" +
                "register <username> <password>                    //register an account//\n" +
                "login <username> <password>                       //log in to File Catalog//\n" +
                "quit                                              //quit File Catalog//\n");
    }
    private void printWrongCommand(){
        System.out.println("\nWrong command!\nThe correct commands are: " +
                "\n\nregister <username> <password>\n" +
                "login <username> <password>\nquit\n");
    }
    private void printLoginMsg(String name){
        System.out.println("\nHi " + name +"!\n" +
                "Available Commands:\n\n" +
                "upload <path> <private>                            //to upload a private file//\n" +
                "upload <path> <public> <write or read>             //to upload a public file//\n" +
                "download <filename>                                //to download a file//\n" +
                "list                                               //to view all the available files//\n" +
                "delete <filename>                                  //to delete a specific file//\n" +
                "status <filename>                                  //to check the status of a specific public file//\n" +
                "unregister                                         //to unregister your account//\n" +
                "logout                                             //to logout from catalog//\n" +
                "quit                                               //to exit the system//\n");
    }
    private void printWrongCommandLogin(){
        System.out.println("\nWrong command!\nThe correct commands are: " +
                "\n\nupload <path> <private>\nupload <path> <public> <write or read>" +
                "\ndownload <filename>\n" +
                "list\ndelete <filename>\nstatus <filename>\nunregister\nlogout\nquit\n");
    }
}
