/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;
import common.HW3DTO;
import java.io.Serializable;

/**
 *
 * @author davidren
 */
public class fileDTO implements Serializable{
    private command command;
    private String username;
    private String password;
    private String filename;
    private String path;
    private String access;
    private String permissions;
    private boolean check;
    private boolean uploadReady;
    
    public fileDTO(command command, String username, String password, String filename, String path, String access, String permissions, boolean check, boolean uploadReady){
        this.command = command;
        this.username= username;
        this.password =password;
        this.filename = filename;
        this.path = path;
        this.access = access;
        this.permissions = permissions;
        this.check = check;
        this.uploadReady = uploadReady;
    }
    
    public command getCommand(){
        return command;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFileName() {
        return filename;
    }

    public String getPath() {
        return path;
    }

    public String getAccess() {
        return access;
    }

    public String getPermissions() {
        return permissions;
    }

    public Boolean checkCommand(){
        return check;
    }

    public Boolean readyToUpload(){
        return uploadReady;
    }


}
