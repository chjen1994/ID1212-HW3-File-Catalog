/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_startup;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import common.fileSys;
import server_controller.controller;

/**
 *
 * @author davidren
 */
public class main {


    public static void main(String[] args) {
        try {
            main server = new main();
           
            server.startRMIServant();
            System.out.println("File Catalog server started.");
          
        } catch (RemoteException | MalformedURLException e) {
            System.out.println("Failed to start File Catalog server.");
        }
    }

    private void startRMIServant() throws RemoteException, MalformedURLException {
        try {
            LocateRegistry.getRegistry().list();
        } catch (RemoteException noRegistryRunning) {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
        //controller contr = new controller();
        try {
            Naming.rebind(fileSys.FILESYSTEM_NAME_IN_REGISTRY, new controller());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        
    }


}
