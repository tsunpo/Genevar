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

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JProgressBar;

import sanger.team16.gui.genevar.UI;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class BaseProgressBar
{
    //private UI ui;   //BUGY BUG 11/02/11
    private JDialog dialog;
    
    public BaseProgressBar(UI ui) {
        this.createAndShow(ui, "Processing...");
    }
    
    public BaseProgressBar(UI ui, String d) {
        this.createAndShow(ui, d);
    }
    
    private void createAndShow(final UI ui, final String d) {
        //this.ui = ui;
        ui.setEnabled(false);
        
        dialog = new JDialog(ui, d);
        dialog.setSize(360, 45);
        dialog.setResizable(false);
        dialog.setEnabled(false);
        dialog.setLocationRelativeTo(ui);
        dialog.setAlwaysOnTop(false);
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(360, 45));
        progressBar.setIndeterminate(true);
        
        dialog.setContentPane(progressBar);
        dialog.setVisible(true);
    }

    public void dispose(UI ui) {
        ui.setEnabled(true);
        dialog.dispose();
    }
}