/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_model;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author davidren
 */
@NamedQueries({
        @NamedQuery(
                name = "findClientName",
                query = "SELECT client FROM handle_client client WHERE client.username LIKE :username"
        ),
        @NamedQuery(
                name = "deleteClientName",
                query = "DELETE FROM handle_client client WHERE client.username LIKE :username"
        )
})

@Entity (name = "handle_client")
@Table(name = "client", schema = "APP", catalog = "")
public class handle_client implements Serializable{
    @Id
    @Column(name = "clientID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long clientId;
    @Basic
    @Column(name = "username", nullable = false, length = 20)
    private String username;
    @Basic
    @Column(name = "password", nullable = false, length = 20)
    private String password;
    
    
    public handle_client(){
        this(null,null);
    }
    public handle_client(String username, String password){
        this.username = username;
        this.password = password;
    }
    public long getClientID(){
        return clientId;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public void setClientID(long ID){
        this.clientId = ID;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    
    
}
