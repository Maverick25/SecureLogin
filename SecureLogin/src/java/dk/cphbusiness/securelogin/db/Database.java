/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.securelogin.db;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author marekrigan
 */
public class Database 
{
    public static Connection getConnection()
    {
        String id = "DB_033"; 
        String pw = "db2015";
        
        Connection conn = null;
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@datdb.cphbusiness.dk:1521:dat", id, pw);
        } 
        catch (Exception e)
        {
            System.out.println("Error in DB.prepare()");
            System.out.println(e);
        }
        return conn;
    }

    public static void releaseConnection(Connection conn)
    {
        try
        {
            conn.close();
        } catch (Exception e)
        {
            System.err.println(e);
        }
    }
}
