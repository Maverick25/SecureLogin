/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.securelogin.model;

/**
 *
 * @author marekrigan
 */
public class User 
{
    private long id;
    private String username;
    private String password;
    private String salt;
    
    public User() {}
    
    public User(long id, String username, String salt, String password) 
    {
        this.id = id;
        this.username = username;
        this.salt = salt;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", password=" + password + ", salt=" + salt + '}';
    }
}
