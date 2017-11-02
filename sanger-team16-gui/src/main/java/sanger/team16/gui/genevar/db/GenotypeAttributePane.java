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
package sanger.team16.gui.genevar.db;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import sanger.team16.gui.genevar.AbstractAttributePane;
import sanger.team16.gui.genevar.UI;
import sanger.team16.gui.jface.BaseJLabel;
import sanger.team16.gui.jface.BaseJPane;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class GenotypeAttributePane extends AbstractAttributePane
{
    public GenotypeAttributePane(UI ui) {
        super(ui);
        
        this.init();
        this.refresh(0, 0, 0, 0);
    }
    
    /*
     * Overwrite refresh
     */
    public void refresh(int studyIndex, int genotypeReferenceIndex, int methylationPlatformIndex, int expressionPlatformIndex) {
        this.removeAll();
        
        this.setNewStudyPanel(studyIndex, genotypeReferenceIndex, methylationPlatformIndex, expressionPlatformIndex);
        this.setBlankPanel();
        this.setAddNewAttributePanel(studyIndex);
        this.setBlankPanel();
        //this.setHideDatasetPanel();
        //this.setBlankPanel();
        this.setResetPanel(this);
        
        if (this.ui.isServices()) {
            bStudy.setEnabled(false);
            bPopulation.setEnabled(false);
            bMethod.setEnabled(false);
        }
        bReset.setVisible(false);
    }

    private void setAddNewAttributePanel(int studyIndex) {
        BaseJPane panel = new BaseJPane("Add New Attributes");
        
        // ------------------ Panel p1 (start) ------------------ //
        BaseJPane p1 = new BaseJPane();

        cbPopulation = new JComboBox(studyPopulations);
        cbPopulation.setSelectedIndex(0);
        cbPopulation.setEditable(true);
        p1.add(cbPopulation);
        p1.add(new BaseJLabel("(Editable; please select a project from above first)", Color.GREEN.darker()));
        
        bPopulation = new JButton("     Add     ");
        bPopulation.addActionListener(this);
        p1.add(bPopulation);
        
        p1.setBaseSpringBoxTrailing();
        panel.add(p1);
        // ------------------ Panel p1 (end) ------------------ //
        
        panel.add(new JLabel(""));
        
        // ------------------ Panel p2 (start) ------------------ //
        BaseJPane p2 = new BaseJPane();

        cbMethod = new JComboBox(this.ui.getMethods("G"));
        cbMethod.setEditable(true);
        cbMethod.setSelectedIndex(0);
        p2.add(cbMethod);
        p2.add(new BaseJLabel("(Editable)", Color.GREEN.darker()));
        
        bMethod = new JButton("     Add     ");
        bMethod.addActionListener(this);
        p2.add(bMethod);
        
        p2.setBaseSpringBoxTrailing();
        panel.add(p2);
        // ------------------ Panel p2 (end) ------------------ //
        
        panel.add(new JLabel(""));
        
        panel.setBaseSpringGrid(1);
        this.add(panel);
    }
}
