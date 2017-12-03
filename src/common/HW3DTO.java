/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.Serializable;

/**
 *
 * @author davidren
 */
public interface HW3DTO extends Serializable{

     /**
     * Returns the username of the account.
     * @return
     */
    public String getUsername();

    /**
     * Returns the password of the account.
     * @return
     */
    public String getPassword();

    /**
     * Returns the id of the account.
     * @return
     */
    public long getId();
}
