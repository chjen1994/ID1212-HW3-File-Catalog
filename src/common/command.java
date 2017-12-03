/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author davidren
 */
public enum command {


    REGISTER,
    
    UNREGISTER,
    /**
     * Log in the specified account.
     */
    LOGIN,
    /**
     * Log out the specified account.
     */
    LOGOUT,
    /**
     * Get help with the command.
     */
    HELP,
    /**
     * Leave the service.
     */
    QUIT,
    /**
     * Upload the File.
     */
    UPLOAD,
    /**
     * Download the File.
     */
    DOWNLOAD,
    /**
     * Delete the File.
     */
    DELETE, 
    /**
     * List all the File.
     */
    LIST,
    
    
    /**
     * None of the valid commands above was specified.
     */
    ILLEGAL_COMMAND
}