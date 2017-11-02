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
package sanger.team16.gui.genevar.db;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;

import sanger.team16.common.hbm.MethylationFeature;
import sanger.team16.common.hbm.Population;
import sanger.team16.common.hbm.Study;
import sanger.team16.gui.genevar.AbstractManagerPane;
import sanger.team16.gui.genevar.UI;
import sanger.team16.gui.jface.BaseJPane;
import sanger.team16.gui.jface.table.BaseJTable;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class MethylationPane extends AbstractManagerPane implements ActionListener
{
    public MethylationPane(UI ui) {
        super(ui);
        
        this.refresh();
    }

    public void refresh() {
        this.removeAll();
        this.ui.setCore();
        
        for (int i=1 ; i<this.ui.getStudies().size() ; i++) {
            Study study = this.ui.getStudies().get(i);
            
            if (study.getMethylationPlatformId() != 0) {
                this.setStudyPanel(study.getId(), study.getName());
                this.setBlankPanel();	
            }
        }
        this.setResetPanel(this);
    }
    
    private void setStudyPanel(int studyId, String studyName) {
        BaseJPane panel = new BaseJPane(studyName,0,20,10,20);

        List<MethylationFeature> methylationFeatures = this.getMethylationFeaturesWhereStudyId(studyId);
        Vector<Population> studyPopulations = this.getPopulationsWhereStudyId(studyId);
        
        if (studyPopulations.size() == 0)
            panel.add(this.getPopulationPanel("N/A", new MethylationTableModel()));
        else
            for (int i=0 ; i<studyPopulations.size() ; i++) {
                Population population = studyPopulations.get(i);

                Object[][] data = this.getMethylationFeaturesWherePopulationId(methylationFeatures, population.getId());
                panel.add(this.getPopulationPanel(population.getName(), new MethylationTableModel(data)));
            }
          
        panel.setBaseSpringGrid(1);
        this.add(panel);
    }

    private BaseJPane getPopulationPanel(String populationName, MethylationTableModel methylationTableModel) {
        BaseJPane panel = new BaseJPane();

        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();

        p0.add(new JLabel("Population: "));
        p0.add(this.getJLabeledPopulationName(populationName));
        
        p0.setBaseSpringBox();
        panel.add(p0);
        // ------------------ Panel p0 (end) ------------------ //
        
        //panel.add(new JLabel(""));
        
        // ------------------ Panel p1 (start) ------------------ //
        BaseJTable table = methylationTableModel.getTable();

        BaseJPane p1 = new BaseJPane();
        p1.setLayout(new BorderLayout());
        p1.add(table.getTableHeader(), BorderLayout.PAGE_START);
        p1.add(table, BorderLayout.CENTER);
        panel.add(p1);
        // ------------------ Panel p1 (end) ------------------ //
        
        panel.setBaseSpringGrid(1);
        return panel;
    }

    public void actionPerformed(ActionEvent e) {
        refresh();
    }
}
