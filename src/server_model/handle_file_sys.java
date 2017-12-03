/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_model;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import server_integration.HW3DAO;
import server_model.handle_client;
import server_model.handle_file_info;


/**
 *
 * @author davidren
 */
public class handle_file_sys {
    private HW3DAO hw3DAO;
    private final List<handle_file_info> file_info = new ArrayList<>();
    
    
    public handle_file_sys(){
        hw3DAO = HW3DAO.getCurrentDAO();
    }
    public boolean upload(String filename, long size, handle_client handleClient, String access, String permissions) throws IOException{
         handle_file handleFile = hw3DAO.findFileByName(filename, false);
        //case the file is not in the database
        if (handleFile == null){
          hw3DAO.writeFile(new handle_file(filename, size, handleClient, access, permissions));
          return true;
        }
        //case the file is in the database and we are the owners that want to update the file
        else if (handleFile.getOwner().getUsername().equals(handleClient.getUsername())){
            try {
                File file = new File("src/ServerFileCatalog/" + filename);
                boolean deleted = file.delete();
            } catch (Exception e){
                System.out.println("Something went wrong while trying to delete and update the file");
            }

            handleFile.setSize(size);
            handleFile.setAccess(access);
            handleFile.setPermissions(permissions);
            hw3DAO.updateDatabase();
            return true;
        }
        //case the file is in the database but it's not ours, but it's public with write permissions.
        else if (handleFile.getAccess().equals("public") && handleFile.getPermissions().equals("write")){
            try {
                File file = new File("src/ServerFileCatalog/" + filename);
                boolean deleted = file.delete();
            } catch (Exception e){
                System.out.println("Something went wrong while trying to delete and update the file");
            }

            handleFile.setSize(size);
            //avoid user mistyping trying to change the access and permissions
            handleFile.setAccess("public");
            handleFile.setPermissions("write");
            hw3DAO.updateDatabase();
            assignStatusList(filename, handleFile.getOwner().getUsername(), handleClient.getUsername(), "update");
            return true;
        }else {
            return false;
        }
    }
    public boolean download(String filename, handle_client handleClient) throws IOException {
        handle_file handleFile = hw3DAO.findFileByName(filename, false);
        //case not found in DB
        if (handleFile == null) {
            return false;
        }
        //case it is private and we are not the owners
        if (handleFile.getAccess().equals("public") && !handleFile.getOwner().getUsername().equals(handleClient.getUsername())){
            assignStatusList(filename,handleFile.getOwner().getUsername(), handleClient.getUsername(), "retrieve");
        }
        return !handleFile.getAccess().equals("private") || handleFile.getOwner().getUsername().equals(handleClient.getUsername());
    }
    public boolean delete(String filename, handle_client handleClient) throws FileException {
        
        
        handle_file handleFile = hw3DAO.findFileByName(filename, false);
        //case not found in DB
        if (handleFile == null) {
            return false;
        }
        //case it is private and we are not the owners
        if (handleFile.getAccess().equals("private") && !handleFile.getOwner().getUsername().equals(handleClient.getUsername())){
            return false;
        }
        //case it is public with read permissions and we are not the owners
        if (handleFile.getAccess().equals("public") && handleFile.getPermissions().equals("read") &&  !handleFile.getOwner().getUsername().equals(handleClient.getUsername())){
            return false;
        }
        //if the previous are false then we can delete the file from server directory and DB
        try {
            assignStatusList(filename, handleFile.getOwner().getUsername(), handleClient.getUsername(), "delete");
            File file = new File("src/ServerFileCatalog/" + filename);
            boolean deleted = file.delete();
            hw3DAO.deleteFileByName(handleFile);
            return true;
        } catch (Exception e){
            System.out.println("Something went wrong while trying to delete the file");
        }
        return false;
    }
    public String listFiles(handle_client handleClient){
        
        List<handle_file> files = hw3DAO.getClientFiles(handleClient.getUsername());
        if (files.isEmpty()) {
            return "No files to show";
        } else {
            StringBuilder visibleFiles = new StringBuilder();
            for (handle_file file : files){
                visibleFiles.append("File name: ");
                visibleFiles.append(file.getFilename());
                visibleFiles.append(", size(bytes): ");
                visibleFiles.append(file.getSize());
                visibleFiles.append(", owner: ");
                visibleFiles.append(file.getOwner().getUsername());
                visibleFiles.append(", access: ");
                visibleFiles.append(file.getAccess());
                visibleFiles.append(", permission: ");
                visibleFiles.append(file.getPermissions());
                visibleFiles.append("\n");
            }
            return visibleFiles.toString();
        }
    }
    public String fileInfo(String filename, handle_client handleClient) {
        boolean check = false;
        StringBuilder fileString = new StringBuilder();
        for (handle_file_info file_Info: file_info){
            
            if (file_Info.getOwner().equals(handleClient.getUsername()) && file_Info.getFilename().equals(filename)){
                check = true;
                fileString.append("Retrieved by: ");
                if (!file_Info.getRetrieveList().isEmpty()) {
                    fileString.append(file_Info.getRetrieveList());
                } else {fileString.append("none");}
                fileString.append(", updated by: ");
                if (!file_Info.getUpdateList().isEmpty()) {
                    fileString.append(file_Info.getUpdateList());
                } else {fileString.append("none");}
                fileString.append(", deleted by: ");
                if (!file_Info.getDeleteList().isEmpty()) {
                    fileString.append(file_Info.getDeleteList());
                } else {fileString.append("none");}
                break;
            }
        }
        if (check){
            return fileString.toString();
        } else {
            return "Nothing to show!";
        }
    }
    
    private void assignStatusList(String filename, String owner, String client, String action){
        
        boolean check = false;
        for (handle_file_info file_Info : file_info){
            if (file_Info.getFilename().equals(filename)){
                switch (action){
                    case "update": file_Info.setUpdateList(client); 
                    check=true; 
                    break;
                    case "delete": file_Info.setDeleteList(client); 
                    check=true; 
                    break;
                    case "retrieve": file_Info.setRetrieveList(client); 
                    check=true; 
                    break;
                }
            }
        }
        if (!check){
            handle_file_info file_Info = new handle_file_info(filename, owner);
            switch (action){
                case "update": file_Info.setUpdateList(client); break;
                case "delete": file_Info.setDeleteList(client); break;
                case "retrieve": file_Info.setRetrieveList(client); break;
            }
            file_info.add(file_Info);
        }

    }
}
