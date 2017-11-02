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
package sanger.team16.gui.genevar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import sanger.team16.gui.jface.BaseJMenuBar;
import sanger.team16.gui.jface.BrowserLauncher;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class UIMenuBar extends BaseJMenuBar implements ActionListener
{
    private UI ui;
    //private String username, password;   //BUGY BUG 11/02/11
    private JMenu mConnection, mTools, mHelp;
    
    public UIMenuBar(UI ui) {
        this.ui = ui;
        //this.username = ui.username;
        //this.password = ui.password;
        
        mConnection = new JMenu("Data Sources");
        mConnection.add(createJMenuItem(this, "Connect to " + Message.NEW_WS + "...", Message.NEW_WS, true));
        mConnection.add(createJMenuItem(this, "Connect to " + Message.NEW_DB + "...", Message.NEW_DB, true));
        mConnection.addSeparator();
        mConnection.add(createJMenuItem(this, Message.NEW_EXIT, Message.NEW_EXIT, true));
        add(mConnection);
        
        mTools = new JMenu("Tools");
        mTools.add(createJMenuItem(this, "New " + Message.NEW_EQTL, Message.NEW_EQTL, true));
        mTools.add(createJMenuItem(this, "New " + Message.NEW_MQTL, Message.NEW_MQTL, true));
        add(mTools);
        
        mHelp = new JMenu("Help");
        mHelp.add(createJMenuItem(this, Message.HELP_QUICKSTART, Message.URL_QUICKSTART, true));
        mHelp.add(createJMenuItem(this, Message.HELP_DATABASE, Message.URL_DATABASE, true));
        mHelp.add(createJMenuItem(this, Message.HELP_FORMAT, Message.URL_UPLOAD, true));
        mHelp.addSeparator();
        mHelp.add(createJMenuItem(this, Message.HELP_ABOUT, Message.URL_ABOUT, true));
        add(mHelp);
    }

    public void actionPerformed(ActionEvent ae) {
        String action = ae.getActionCommand();
        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        //String username = "", password = "";
        
        if (action == Message.NEW_WS) {
            String[] services = {Message.GENEVAR_DEFAULT,
                                 Message.GENEVAR_INTERNAL,
                                 Message.GENEVAR_MUTHER,
                                 Message.GENEVAR_CARDIO};
            final Map<String, String> serviceMap= new HashMap<String, String>(); 
            serviceMap.put(services[0], Message.WS_SANGER_DEFAULT);
            serviceMap.put(services[1], Message.WS_SANGER_INTERNAL);
            serviceMap.put(services[2], Message.WS_SANGER_MUTHER);
            serviceMap.put(services[3], Message.WS_SANGER_CARDIO);
            
            final String s = (String) JOptionPane.showInputDialog(this.ui, "", "New Web Services Connection", JOptionPane.PLAIN_MESSAGE, null, services, "");
            if (s != null) {
                if (!s.equals(services[0])) {
                    int a = JOptionPane.showConfirmDialog(this.ui, new UILoginPane(usernameField, passwordField), "Login", JOptionPane.OK_CANCEL_OPTION);  
                    if (a == JOptionPane.OK_OPTION) {
                        final String username = usernameField.getText();
                        final String password = new String(passwordField.getPassword());
                    
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                new UI(s + " - " + Message.NEW_WS, true, serviceMap.get(s), username, password);
                            }
                        });
                        
                    } else
                        return;
                }  // TODO for default
                return;
            }

        } else if (action == Message.NEW_DB) {
            String[] databases = {"localhost/tpy_team16_genevar_3_0_0",
                                  "localhost/tpy_team16_genevar_2_0_0"};
            final Map<String, String> databaseMap= new HashMap<String, String>(); 
            databaseMap.put(databases[0], Message.DB_3_0_0);
            databaseMap.put(databases[1], Message.DB_2_0_0);
            //databaseMap.put(databases[0], Message.DB_2_0_0);

            final String s = (String) JOptionPane.showInputDialog(this.ui, "", "New Database Connection", JOptionPane.PLAIN_MESSAGE, null, databases, "");
            if (s != null) {
                final String username = "";
                final String password = "";

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new UI(s + " - " + Message.NEW_DB, false, databaseMap.get(s), username, password);
                    }
                });
                
                return;
            }
 
        } else if (action == Message.NEW_EXIT)
            System.exit(0);
        
        else if (action == Message.NEW_EQTL)
            this.ui.setEQTLAnalysisTreeNote(true);

        else if (action == Message.NEW_MQTL)
            this.ui.setMQTLAnalysisTreeNote(true);
        
        else
            BrowserLauncher.openURL(action);
    }
}
