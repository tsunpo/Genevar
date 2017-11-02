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

import javax.swing.JLabel;

import sanger.team16.gui.jface.BaseJPane;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class QTLAnalysisPane extends AbstractPane implements ActionListener
{
    public QTLAnalysisPane(UI ui, String name) {
        super(ui);
        
        this.setAboutPanel(name);
        this.setBlankPanel();
        this.setRemovePanel(this);
    }
    
    private void setAboutPanel(String name) {
        BaseJPane panel = new BaseJPane("About " + name);
        
        /* ------------------ Panel p0 (start) ------------------ */
        BaseJPane p0 = new BaseJPane();

        p0.add(new JLabel(" "));
        
        p0.setBaseSpringBoxTrailing();
        panel.add(p0);
        /* ------------------ Panel p0 (end) ------------------ */

        panel.add(new JLabel(""));

        panel.setBaseSpringGrid(1);
        this.add(panel);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bRemove) {
            this.ui.removeCurrentNode();
        }
    }
}
