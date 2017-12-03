/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_controller;

import java.util.concurrent.CompletableFuture;

import common.fileSys;
import client_net.connection;
/**
 *
 * @author davidren
 */
public class controller {
    private final connection connection = new connection();
    
    
    public void prepareUpload(fileSys fileSys, String username, String filename, long size, String access, String permissions){
        
        CompletableFuture.runAsync(() -> {
            try {
                fileSys.upload(username, filename, size, access, permissions);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }
    
    public void finishUpload(String path, String filename) {
        try {
            connection.fileSending(path, filename);
            System.out.println("Uploaded");
        } catch (Exception e) {
            System.out.println("Error when uploading");
        }
    }
    
    public void prepareDownload(fileSys fileSys, String username, String filename) {
        
        CompletableFuture.runAsync(() -> {
            try {
                fileSys.download(username, filename);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }
    
    public void finishDownload() {
        try {
            connection.fileReceiving();
            System.out.println("Downloaded");
        } catch (Exception e) {
            System.out.println("Error when downloading");
        }
    }
    
    
}
