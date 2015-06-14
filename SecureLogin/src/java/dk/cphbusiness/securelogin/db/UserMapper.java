/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.securelogin.db;

import dk.cphbusiness.securelogin.model.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author marekrigan
 */
public class UserMapper 
{
    
    public User login(String username, String password, Connection conn) throws SQLException
    {
        User user = null;
        String getSaltSQL = // get salt
                "select salt "
                + "from security_users "
                + "where user_name = ?";
        
        String loginSQL = 
                "select * "
                + "from security_users "
                + "where user_name = ? and user_pw = ?";
        
        PreparedStatement getSaltStatement = null;
        PreparedStatement loginStatement = null;
        
        String hashPw = null;
        
        try
        {
            // get salt
            getSaltStatement = conn.prepareStatement(getSaltSQL);
            getSaltStatement.setString(1, username);
            ResultSet rs = getSaltStatement.executeQuery();
            
            String salt = null;
            if (rs.next())
            {
                salt = rs.getString(rs.findColumn("salt"));
            }
     
            // hash pw
            hashPw = hashPassword(password, salt);
            
            // login
            loginStatement = conn.prepareCall(loginSQL);
            loginStatement.setString(1, username);
            loginStatement.setString(2, hashPw);
            
            rs = loginStatement.executeQuery();
            
            if (rs.next())
            {
                user = new User(rs.getInt(rs.findColumn("user_id")), 
                        rs.getString(rs.findColumn("user_name")), 
                        rs.getString(rs.findColumn("salt")),
                        rs.getString(rs.findColumn("user_pw")));
            }
            
        } 
        catch (Exception e)
        {
            System.out.println("Fail in UserMapper - login");
            e.printStackTrace();
        } 
        finally
        {
            getSaltStatement.close();
            loginStatement.close();
        }

        return user;
    }
    
    private String hashPassword(String password, String salt)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(password.getBytes());
            byte[] digest = md.digest();

            StringBuffer sb = new StringBuffer();
            for (byte single : digest)
            {
                sb.append(Integer.toString((single & 0xff) + 0x100,16).substring(1));
            }

            String hashedStr = sb.toString();

            String combined = hashedStr+salt;

            md.update(combined.getBytes());
            digest = md.digest();

            sb = new StringBuffer();
            for (byte single : digest)
            {
                sb.append(Integer.toString((single & 0xff) + 0x100,16).substring(1));
            }

            return sb.toString();
        }
        catch(NoSuchAlgorithmException e)
        {
            return null;
        }
    }
}