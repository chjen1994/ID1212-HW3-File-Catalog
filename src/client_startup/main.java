/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_startup;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import common.fileSys;
import client_view.interpreter;
/**
 *
 * @author davidren
 */
public class main {
        public static void main(String[] args) {
        try {
            fileSys file = (fileSys) Naming.lookup(fileSys.FILESYSTEM_NAME_IN_REGISTRY);
            new interpreter().start(file);
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            System.out.println("Could not start file client.");
        }
          
    }
}
