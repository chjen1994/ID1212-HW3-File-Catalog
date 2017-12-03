/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_model;
import java.util.*;

import server_model.handle_client;
import server_integration.HW3DAO;
import exception.badInput;

/**
 *
 * @author davidren
 */
public class handle_client_sys {
    private HW3DAO hw3DAO;
    private final List<handle_client> activeClients = new ArrayList<>();
    
    public handle_client_sys(){
        hw3DAO = HW3DAO.getCurrentDAO();
    }
    
    public void register(String username, String password) throws badInput {
        handle_client handleClient = hw3DAO.findClientByName(username);
        
        if (handleClient == null) {
            hw3DAO.register(new handle_client(username, password));
        } else {
            throw new badInput("Username: " + username + " chosen. Choose another one");
        }
    }
    
    public void unregister(String username) throws badInput {
        handle_client handleClient = activeClient(username);
        if (handleClient == null){
            throw new badInput("Cannot unregister");
        } else {
            hw3DAO.unregister(username);
            
        }
    }
    public void login(String username, String password) throws badInput {
        handle_client handleClient = hw3DAO.findClientByName(username);
        if (handleClient == null) {
            throw new badInput("No username. Please register first");
        }else {
               if (!handleClient.getPassword().equals(password)){
                   throw new badInput("Wrong password! Try again!");
               }else{
                    activeClients.add(handleClient);
                }
        }
    }
    public void logout(String username) throws badInput {
        handle_client handleClient = activeClient(username);
        if (handleClient == null){
            throw new badInput("Cannot log out");
        } else {
            activeClients.remove(handleClient);
        }
    }
    
    
    private handle_client activeClient(String username){
        handle_client client = null;
        for (handle_client handleClient : activeClients){
            if (handleClient.getUsername().equals(username)){
                client = handleClient;
                break;
            }
        }
        return client;
    }
    
    public handle_client getClient(String username) {
        return activeClient(username);
    }
    
}
