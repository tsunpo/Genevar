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

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class BaseJFileChooser extends JFileChooser
{
    public BaseJFileChooser() {
        //super(System.getProperty("user.dir"));
        //super(System.getProperty("java.library.path"));
    }
    
    public boolean showOpenDialog(Component ui, BaseFileFilter filter) {
        if (filter != null) this.addChoosableFileFilter(filter);
        
        if (this.showOpenDialog(ui) == JFileChooser.APPROVE_OPTION)
            return true;
        return false;
    } 
    
    public boolean showSaveDialog(Component ui, String file) {
        this.setSelectedFile(new File(file));

        if (this.showSaveDialog(ui) == JFileChooser.APPROVE_OPTION)
            return true;
        return false;
    }
    
    public void reset() {
        this.setSelectedFile(new File(""));
        this.resetChoosableFileFilters();
    }
}
