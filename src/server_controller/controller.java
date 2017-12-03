/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_controller;



import exception.badInput;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


import common.fileSys;
import common.fileDTO;
import server_net.transfer;
import server_model.*;
/**
 *
 * @author davidren
 */
public class controller extends UnicastRemoteObject implements fileSys  {
   
    private final handle_client_sys clientsSystem;
    private final handle_client_command clientsCommand;
    private final handle_file_sys filesSystem;
    
    public controller() throws RemoteException {
        super();
        clientsSystem = new handle_client_sys();
        clientsCommand = new handle_client_command();
        filesSystem = new handle_file_sys();
    }

    
    @Override
    public fileDTO FileDTO(String command) throws RemoteException, badInput {
        return clientsCommand.fileDTO(command);
    }
    

    @Override
    public void register(String username, String password) throws RemoteException, badInput {
        clientsSystem.register(username, password);
    }

    /**
     * Unregisters an account given a username
     * @throws java.rmi.RemoteException
     */
    @Override
    public void unregister(String username)throws RemoteException, badInput{
        clientsSystem.unregister(username);
    }

    /**
     * List files in the file system
     */
   

    /**
     * Login user
     * @throws java.rmi.RemoteException
     */
    @Override
    public void login(String username, String password) throws RemoteException, badInput{
        clientsSystem.login(username, password);
    }

    /**
     * Logout user
     * @throws java.rmi.RemoteException
     */
    @Override
    public void logout(String username) throws RemoteException, badInput {
        clientsSystem.logout(username);
    }

    /**
     * upload file
     * @throws java.io.IOException
     */
    @Override
    public void upload(String username, String filename, long size, String access, String permissions)throws IOException, badInput{
        handle_client clientHandler = clientsSystem.getClient(username);
        if (filesSystem.upload(filename, size, clientHandler, access, permissions)){
            transfer fileTransfer = new transfer();
            fileTransfer.fileReceiving();
        }else {
            throw new badInput("error when uploading to the server!");
        }
    }

    /**
     * download file
     * @throws java.io.IOException
     */
    @Override
    public void download(String username, String filename)throws IOException, badInput{
        handle_client clientHandler = clientsSystem.getClient(username);
        if (filesSystem.download(filename, clientHandler)){
            transfer fileTransfer = new transfer();
            fileTransfer.fileSending(filename);
        }else {
            throw new badInput("error at server controller download! ");
        }
    }

    /**
     * delete file
     * @throws server_model.FileException
     */
    @Override
    public void delete(String username, String filename)throws RemoteException,FileException {
        handle_client clientHandler = clientsSystem.getClient(username);
        if (!filesSystem.delete(filename, clientHandler)){
            throw new FileException("error at server controller delete!");
        }
    }

    /**
     * list all file
     */
    @Override
    public String list(String username)throws RemoteException{
        handle_client clientHandler = clientsSystem.getClient(username);
        return filesSystem.listFiles(clientHandler);
    }

    /**
     * get file info
     */
    @Override
    public String fileInfo(String username, String filename)throws RemoteException{
        handle_client clientHandler = clientsSystem.getClient(username);
        return filesSystem.fileInfo(filename, clientHandler);
    }

}
