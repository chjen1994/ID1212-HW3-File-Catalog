/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_integration;

import javax.persistence.*;
import java.util.*;
import server_model.handle_client;


import exception.badInput;




import server_model.handle_file;

/**
 *
 * @author davidren
 */
public class HW3DAO {
    private final EntityManagerFactory EMFactory;
    private final ThreadLocal<EntityManager> threadLocalEM = new ThreadLocal<>();
    private static HW3DAO hw3DAO;
    
    public HW3DAO(){
        EMFactory = Persistence.createEntityManagerFactory("HW3-RMI_DBPU");
    }
    
    
    //following codes are referenced from BankDAO
    public handle_client findClientByName(String username) {
        if (username == null) {
            return null;
        }

        try {
            EntityManager em = beginTransaction();
            try {
                return em.createNamedQuery("findClientName", handle_client.class).
                        setParameter("username", username).getSingleResult();
            } catch (NoResultException noSuchAccount) {
                return null;
            }
        } finally {
            
                commitTransaction();
            
        }
    }
    public void register(handle_client clientHandler) {
        try {
            EntityManager em = beginTransaction();
            em.persist(clientHandler);
        }
        finally{
            commitTransaction();
        }
    }
    public void unregister(String username) throws badInput {
        try{
            EntityManager em = beginTransaction();
            em.createNamedQuery("deleteClientName",handle_client.class).setParameter("username",username).executeUpdate();
        } finally{
            commitTransaction();
        }
    }
    
    
    
    public handle_file findFileByName(String filename, boolean finishTransaction) {
        if (filename == null){return null;}
        try {
            EntityManager em = beginTransaction();
            try {
                return em.createNamedQuery("findFileByName", handle_file.class).
                        setParameter("filename", filename).getSingleResult();
            } catch (NoResultException fileNotFound){
                return null;
            }
        } finally {
            if (finishTransaction){
                commitTransaction();
            }
        }
    }
    
    public void deleteFileByName (handle_file files) throws badInput {
        String filename = files.getFilename();
        try{
            EntityManager em = beginTransaction();
            int FILE = em.createNamedQuery("deleteFileByName", handle_file.class).setParameter("filename", filename).executeUpdate();
            if (FILE != 1){
                throw new badInput("File " + filename + " was not deleted!");
            }
        } finally{
            commitTransaction();
        }
    }
    
    public List<handle_file> getClientFiles(String username){
        try{
            try {
                EntityManager em = beginTransaction();
                return em.createNamedQuery("getClientFiles", handle_file.class).setParameter("username", username).getResultList();
            }catch (NoResultException noSuchAccount) {
                return new ArrayList<>();  
            }
        }finally {
            commitTransaction();
        }
    }
    
    public void writeFile(handle_file files) {
        try {
            EntityManager em = beginTransaction();
            em.persist(files);
        } finally {
            commitTransaction();
        }
    }
    
    public void updateDatabase(){
        commitTransaction();
    }

    private EntityManager beginTransaction() {
        EntityManager em = EMFactory.createEntityManager();
        threadLocalEM.set(em);
        EntityTransaction transaction = em.getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
        return em;
    }
    
    private void commitTransaction() {
        threadLocalEM.get().getTransaction().commit();
    }
    
    public static HW3DAO getCurrentDAO(){
        if (hw3DAO == null) {
            hw3DAO = new HW3DAO();
        }
        return hw3DAO;
    }
}
