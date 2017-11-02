/**
 *  This file is part of Genevar (GENe Expression VARiation)
 *  Copyright (C) 2011  Genome Research Ltd.
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

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class UILoginPane extends JPanel
{
    public UILoginPane(JTextField usernameField, JPasswordField passwordField) {
        JPanel panel = new JPanel();
        //panel.setLayout(new GridLayout(2,1));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JPanel p0 = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        //usernameField = new JTextField(15); 
        JLabel l0 = new JLabel("Username:");
        l0.setLabelFor(usernameField);        
        p0.add(l0);
        p0.add(usernameField);
        panel.add(p0);

        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        //passwordField = new JPasswordField(15);
        JLabel l1 = new JLabel("Password:");
        l1.setLabelFor(passwordField);      
        p1.add(l1);
        p1.add(passwordField);
        panel.add(p1);
        
        add(panel);
    }
}
