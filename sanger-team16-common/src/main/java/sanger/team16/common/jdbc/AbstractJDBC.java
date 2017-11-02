/**
 *  This file is part of Genevar (GENe Expression VARiation)
 *  Copyright (C) 2010  Genome Research Ltd.
 *
 *  Genevar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sanger.team16.common.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.cfg.Configuration;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class AbstractJDBC
{
    protected String address;
    protected Connection conn;

    public AbstractJDBC(String address) {
        this.address = address;
        Configuration cfg = new Configuration().configure(address);
        String driver = cfg.getProperty("hibernate.connection.driver_class");
        String url = cfg.getProperty("hibernate.connection.url");
        String username = cfg.getProperty("hibernate.connection.username");
        String password = cfg.getProperty("connection.password");

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            
        } catch (SQLException e) {
            
        } catch (ClassNotFoundException e) {}
    }

    protected String replaceBackslashWithDoubleBackslash(String string) {
        //loadFile.replaceAll("\\", "\\\\");   //Exception in thread "Thread-7" java.util.regex.PatternSyntaxException: Unexpected internal error near index 1
        //String[] st = loadFile.split("\\");   //Exception in thread "Thread-7" java.util.regex.PatternSyntaxException: Unexpected internal error near index 1
        char[] st = string.toCharArray();
        
        string = "";
        for (int i=0 ; i<st.length ; i++)
            if (st[i] == '\\')
                string += "\\\\";
            else
                string += st[i];
        
        return string;
    }
    
    protected void execute(String sql) {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            stmt.close();
            this.conn.close();
            
        } catch (SQLException e) {}
    }
    
    /* VERY BAD!! 22/09/13
    protected void executeUpdate(String sql) {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            this.conn.close();
            
        } catch (SQLException e) {}
    }*/
}
