/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_model;

import javax.persistence.*;

import server_model.handle_client;
/**
 *
 * @author davidren
 */

@NamedQueries({
        @NamedQuery(
                name = "findFileByName",
                query = "SELECT file FROM handle_file file WHERE file.filename LIKE :filename"
        ),
        @NamedQuery(
                name = "deleteFileByName",
                query = "DELETE FROM handle_file file WHERE file.filename LIKE :filename"
        ),
        @NamedQuery(
                name = "getClientFiles",
                query = "SELECT file FROM handle_file file WHERE file.owner.username = :username OR file.access = 'public'"
        )
})

@Entity(name = "handle_file")
@Table(name = "file", schema = "APP")
public class handle_file {
    private String filename;
    private long size;
    private handle_client owner;
    private String access;
    private String permissions;
    
    public handle_file(){
        
    }
    public handle_file(String filename, long size, handle_client owner, String access, String permissions){
        this.filename = filename;
        this.size = size;
        this.owner = owner;
        this.access = access;
        this.permissions = permissions;
    }
    @Id
    @Column(name = "filename", nullable = false)
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    @Basic
    @Column(name = "size", nullable = false)
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
    
    @ManyToOne
    @JoinColumn(name = "owner", nullable = false)
    public handle_client getOwner() {
        return owner;
    }

    public void setOwner(handle_client owner) {
        this.owner = owner;
    }

    @Basic
    @Column(name = "access", nullable = false)
    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    @Basic
    @Column(name = "permissions", nullable = false)
    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}
