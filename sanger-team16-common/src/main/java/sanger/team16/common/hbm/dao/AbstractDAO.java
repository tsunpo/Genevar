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
package sanger.team16.common.hbm.dao;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class AbstractDAO
{
    private String address;
    private Session session;   //Abandoned to test c3p0 pool? //CHANGE TO private 23/11/10
    //protected Transaction transaction;   //BAD 09/02/10
    
    public AbstractDAO(String address) {
        this.address = address;
        this.setCurrentSession();
    }

    protected void setCurrentSession() {
    	try {
            if (address.equals("tpy_team16_genevar_2_0_0.cfg.xml"))
                session = HibernateUtil2.getSessionFactory().getCurrentSession();
            else
                session = HibernateUtil3.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            
        } catch (Exception ex) {
            // The last packet successfully received from the server was 7,200,036 milliseconds ago.
            System.err.println("Cannot open connection" + ex);   //ADD 15/06/12
        }
    }

    /*
     * Generic DAO
     */
    protected Session getSession() {
        try {
            if (session == null)   //ADD 22/09/13
                this.setCurrentSession();
            
        } catch (Exception e) {}
        
        return session;
    }
    
    public void commit() throws RuntimeException {
        session.getTransaction().commit();
        //session.close();   //BAD BAD BAD 14/09/10
    }
    
    public void flushAndClear() {
        session.flush();
        session.clear();
    }
    
    protected int returnId(Integer id) {
        if (id == null) 
            return 0;
        return id.intValue();
    }
    
    /**
     * Unlike persist(), save() will return the auto increment primary key.
     */
    public int save(Object object, boolean isCommitted) throws RuntimeException {
        Integer id = (Integer) session.save(object);

        if (isCommitted)
            this.commit();
        return this.returnId(id);
    }
    
    /**
     * Unlike save(), persist() will not return primary key.
     */
    public void persist(Object object, boolean isCommitted) throws RuntimeException {
        session.persist(object);

        if (isCommitted)
            this.commit();
    }
    
    /**
     * Command line mode
     */
    public int uniqueResult(String name, String tableName, boolean isCommitted) throws NonUniqueResultException {
        try {
            Integer id = (Integer) session.createQuery("SELECT id FROM " + tableName + " WHERE name LIKE '%" + name.replaceAll("-", "%") + "%'")
            .uniqueResult();
            
            if (isCommitted)
                this.commit();
            return this.returnId(id);
            
        } catch (NonUniqueResultException e) {
            return 0;
        }
    }
}

/*
protected Session getSession() {
    try {
        if (session != null)   //BUG 09/08/11
            if (session.beginTransaction() == null)   //BUG 16/11/10
                this.setCurrentSession();
            //else   //MODIFY 31/08/11
                //session.beginTransaction().rollback();
        else
            this.setCurrentSession();
    
    } catch (Exception e) {   //ADD 06/08/11 //BUG JDBCConnectionException
        this.setCurrentSession();
    }
    
    return session;
}*/
/*
protected Session getSession() {
    try {          
        //if (session.beginTransaction() != null)   //BUG 10/08/11 when session.close()
        if (session != null)   //BUG 09/08/11
            //session.beginTransaction().rollback();   //BUG 10/08/11 can't do a rollback after a commit
            session.beginTransaction();
        else    
            this.setCurrentSession();
        
    } catch (Exception e) {   //ADD 06/08/11 //BUG JDBCConnectionException
        rollback(e);
    }
    
    return session;
}

private void rollback(Exception e) {
    if (session.beginTransaction() != null)   //BUG 16/11/10
        session.beginTransaction().rollback();
    else
        this.setCurrentSession();
}*/

//
/*
    protected Session getSession() {
        try {
            if (session == null)   //BUG
            //if (session.beginTransaction() != null)   //ADD 06/08/11 -> BUG
                session.beginTransaction().rollback();
                //throw new IllegalStateException("Session has not been set on DAO before usage");
           
        } catch (Exception e) {   //TODO
            if (session.beginTransaction() != null)
                session.beginTransaction().rollback();
            System.out.println(e);   //TODO
            //throw e;
        }
       
        return session;
    }
*/