/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.securelogin.servlet;

import dk.cphbusiness.securelogin.db.Database;
import dk.cphbusiness.securelogin.db.UserMapper;
import dk.cphbusiness.securelogin.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author marekrigan
 */
@WebServlet(name="Servlet", urlPatterns = {"/loginRequest"})
public class Servlet extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        User user = null;
        
        // read form fields
        String username = request.getParameter("username");
        String password = request.getParameter("password");
         
        System.out.println("username: " + username);
        System.out.println("password: " + password);
 
        Connection conn = Database.getConnection(); 
        System.out.println("### conn opened");
         
        UserMapper um = new UserMapper();
        
        try 
        {
            user = um.login(username, password, conn);
            
            if (conn != null)
            {
                conn.close();
                conn = null;
                System.out.println("### conn closed ");
            }
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        
        // get response writer
        PrintWriter writer = response.getWriter();
        
        // build HTML code
        String htmlRespone = "<html>";
        
        if (user != null)
        {
            htmlRespone += "<h2>User is logged in!</h2>";
        }
        else
        {
            htmlRespone += "<h2>NO user found</h2>";
        }
        
        htmlRespone += "</html>";
         
        // return response
        System.out.println("htmlResponse");
        
        writer.println(htmlRespone);
    }

}
