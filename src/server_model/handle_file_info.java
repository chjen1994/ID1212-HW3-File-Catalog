/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_model;


import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author davidren
 */
public class handle_file_info {
    private String filename;
    private String owner;
    private List<String> update;
    private List<String> delete;
    private List<String> retrieve;
    
    
    public handle_file_info(String filename, String owner){
        this.filename = filename;
        this.owner = owner;
        this.update = new ArrayList<>();
        this.delete = new ArrayList<>();
        this.retrieve = new ArrayList<>();
    }
    
    
    public String getFilename() {
        return filename;
    }

    String getOwner() {
        return owner;
    }
    
    //get and set update list
    void setUpdateList(String username){
        update.add(username);
    }
    List<String> getUpdateList() {
        return update;
    }

    
    //get and set delete list
    void setDeleteList(String username){
        delete.add(username);
    }
    
    List<String> getDeleteList() {
        return delete;
    }
    
    //get and set retrieve list
    void setRetrieveList(String username){
        retrieve.add(username);
    }
    List<String> getRetrieveList() {
        return retrieve;
    }
    




}
