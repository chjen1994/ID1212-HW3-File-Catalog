/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_model;

import java.nio.file.Path;
import java.nio.file.Paths;

import common.command;
import common.fileDTO;
import exception.badInput;

/**
 *
 * @author davidren
 */
public class handle_client_command {
    private command Command;
    private String username;
    private String password;
    private String filename;
    private String path;
    private String access;
    private String permissions;
    private float size;
    private boolean check;
    private boolean uploadReady;

    public handle_client_command(){
        
    }
    public fileDTO fileDTO(String Inputcommand) throws badInput{
        String[] words = Inputcommand.split("\\s");//splits the string based on whitespace
        
        switch (words[0].toUpperCase()){
            case "REGISTER":
                Command = command.REGISTER;
                setCredentials(words);
                break;
            case "UNREGISTER":
                Command = command.UNREGISTER;
                check = words.length == 1;
                break;
            case "LOGIN":
                Command = command.LOGIN;
                setCredentials(words);
                break;
            case "LOGOUT":
                Command = command.LOGOUT;
                check = words.length == 1;
                break;
            case "UPLOAD":
                Command = command.UPLOAD;
                getFileInfo(Inputcommand);
                break;
            case "DOWNLOAD":
                Command = command.DOWNLOAD;
                setFileName(words);
                break;
            case "LIST":
                Command = command.LIST;
                check = words.length == 1;
                break;
            case "DELETE":
                Command = command.DELETE;
                setFileName(words);
                break;
            case "QUIT":
                Command = command.QUIT;
                check = words.length == 1;
                break;
            default:
                check = false;
                break;
        
        }
        return new fileDTO(Command, username, password, filename, path, access, permissions, check, uploadReady);
    }
    
    private void setCredentials(String[] words){
        if (words.length != 3){
            check = false;
        } else {
            username = words[1];
            password = words[2];
            check = true;
        }
    }
    private void setFileName(String[] info){
        if (info.length != 2){
            check = false;
        } else {
            filename = info[1];
            check = true;
        }
    }
    private void getFileInfo(String Inputcomman){
        try {
            
            String[] splitInfo = Inputcomman.split("\"");
            path = splitInfo[1].replace("\\", "/");
            
            Path filePath = Paths.get(path);
            filename = filePath.getFileName().toString();
            
            String[] temp = splitInfo[2].split("\\s");
            if (temp.length == 2){
                if (temp[1].equals("private")){
                    access = "private";
                    permissions = "write";
                    check = true;
                    uploadReady = true;
                } else {
                    uploadReady = false;
                    check = false;
                }
            } else if (temp.length == 3){
                if (temp[1].equals("public") && (temp[2].equals("write")
                        || temp[2].equals("read"))){
                    access = "public";
                    permissions = temp[2];
                    check = true;
                    uploadReady = true;
                } else {
                    check = false;
                    uploadReady = false;
                }
            } else {
                uploadReady = false;
                check = false;
            }
        }
        //giving empty values to avoid null exceptions.
        catch (Exception e){
            uploadReady = false;
            check = true;
            access = "";
            permissions = "";
            path = "";
            filename = "";
        }
    
    }
    
    
}
