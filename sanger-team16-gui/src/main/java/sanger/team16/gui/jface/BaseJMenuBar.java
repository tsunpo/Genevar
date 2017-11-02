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
package sanger.team16.gui.jface;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public abstract class BaseJMenuBar extends JMenuBar
{
    protected JMenuItem createJMenuItem(ActionListener listener, String name, String command, boolean enable) {
        JMenuItem mi = new JMenuItem(name);
        mi.setActionCommand(command);
        mi.addActionListener(listener);
        mi.setEnabled(enable);
        
        return mi;
    }

    protected JMenuItem createJMenuItem(ActionListener listener, String name, ImageIcon imageIcon, String command, boolean enable) {
        JMenuItem mi = new JMenuItem(name, imageIcon);
        mi.setActionCommand(command);
        mi.addActionListener(listener);
        mi.setEnabled(enable);
        
        return mi;
    }
}
