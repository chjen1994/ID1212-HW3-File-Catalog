/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;



import java.rmi.Remote;
import java.rmi.RemoteException;


import exception.badInput;
import java.io.IOException;
import server_model.FileException;
        
/**
 *
 * @author davidren
 */

public interface fileSys extends Remote {



    String FILESYSTEM_NAME_IN_REGISTRY = "filesystem";

    /**
     * Creates an account with the specified username.
     * @param command
     * @return 
     * @throws java.rmi.RemoteException
     * @throws exception.badInput
     * 
     */
    fileDTO FileDTO(String command) throws RemoteException, badInput;

    void register(String username, String password) throws RemoteException, badInput;

    /**
     * Unregisters an account given a username
     * @param username
     * @throws java.rmi.RemoteException
     * @throws exception.badInput
     */
    void unregister(String username) throws RemoteException, badInput;

    /**
     * List files in the file system
     */
   

    /**
     * Login user
     * @param username
     * @param password
     * @throws exception.badInput
     */
    void login(String username, String password) throws RemoteException, badInput;

    /**
     * Logout user
     * @param username
     * @throws exception.badInput
     */
    void logout(String username) throws RemoteException, badInput;
    /**
     * upload file
     * @param username
     * @param filename
     * @param size
     * @param access
     * @param permissions
     * @throws exception.badInput
     */
    
    void upload(String username, String filename, long size, String access, String permissions) throws IOException, badInput;
    /**
     * download file
     * @param username
     * @param filename
     * @throws exception.badInput
     */
    void download(String username, String filename) throws IOException, badInput;
    /**
     * delete file
     * @param username
     * @param filename
     * @throws java.rmi.RemoteException
     */
    void delete(String username, String filename) throws RemoteException, FileException;
    /**
     * list all file
     * @param username
     * @return 
     * @throws java.rmi.RemoteException
     */
    String list(String username) throws RemoteException;
    /**
     * get file info
     * @param username
     * @param filename
     * @return 
     * @throws java.rmi.RemoteException
     */
    String fileInfo(String username, String filename)throws RemoteException;
    
}
