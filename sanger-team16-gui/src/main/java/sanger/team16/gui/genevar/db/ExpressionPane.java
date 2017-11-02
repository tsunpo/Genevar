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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;

import sanger.team16.common.hbm.Study;
import sanger.team16.common.hbm.Population;
import sanger.team16.common.hbm.ExpressionFeature;
import sanger.team16.gui.genevar.AbstractManagerPane;
import sanger.team16.gui.genevar.UI;
import sanger.team16.gui.jface.BaseJPane;
import sanger.team16.gui.jface.table.BaseJTable;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class ExpressionPane extends AbstractManagerPane implements ActionListener
{
    public ExpressionPane(UI ui) {
        super(ui);
        
        this.refresh();
    }

    public void refresh() {
        this.removeAll();
        this.ui.setCore();

        for (int i=1 ; i<this.ui.getStudies().size() ; i++) {
            Study study = this.ui.getStudies().get(i);

            if (study.getExpressionPlatformId() != 0) {
                this.setStudyPanel(study.getId(), study.getName());
                this.setBlankPanel();	
            }
        }
        this.setResetPanel(this);
    }
    
    private void setStudyPanel(int studyId, String studyName) {
        BaseJPane panel = new BaseJPane(studyName,0,20,10,20);

        List<ExpressionFeature> expressionFeatures = this.getExpressionFeaturesWhereStudyId(studyId);
        Vector<Population> studyPopulations = this.getPopulationsWhereStudyId(studyId);
        
        if (studyPopulations.size() == 0)
            panel.add(this.getPopulationPanel("Population: ", "N/A", new ExpressionTableModel()));
        else {
        	String tissueType = "";
        	
            for (int i=0 ; i<studyPopulations.size() ; i++) {
                Population population = studyPopulations.get(i);
                Object[][] data = this.getExpressionFeaturesWherePopulationId(expressionFeatures, population.getId());
                
                if (data.length != 1)
                    panel.add(this.getPopulationPanel("Population: ", population.getName(), new ExpressionTableModel(data)));                
                else if ((String) data[0][1] != tissueType) {
                    tissueType = (String) data[0][1];
                    Object[][] data2 = new Object[studyPopulations.size()][data[0].length];
                    
                    for (int j=0 ; j<studyPopulations.size() ; j++) {
                        Population population2 = studyPopulations.get(j);
                        
                        Object[][] data3 = this.getExpressionFeaturesWherePopulationId(expressionFeatures, population2.getId());
                        for (int k=0 ; k<data3[0].length; k++)
                            data2[j][k] = data3[0][k];
                        data2[j][1] = population2;
                    }
                    
                    panel.add(this.getPopulationPanel("Tissue Type: ", tissueType, new ExpressionTableModel(ExpressionTableModel.columnNames2, data2)));
                }
            }
        }
        
        panel.setBaseSpringGrid(1);
        this.add(panel);
    }

    private BaseJPane getPopulationPanel(String title, String populationName, ExpressionTableModel expressionTableModel) {
        BaseJPane panel = new BaseJPane();

        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();

        p0.add(new JLabel(title));
        p0.add(this.getJLabeledPopulationName(populationName));
        
        p0.setBaseSpringBox();
        panel.add(p0);
        // ------------------ Panel p0 (end) ------------------ //
        
        //panel.add(new JLabel(""));
        
        // ------------------ Panel p1 (start) ------------------ //
        BaseJTable table = expressionTableModel.getTable();

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

/*
    public ExpressionPane(UI ui, String errorMessage) {
        super(ui);

        this.setEmptyProjectPanel(errorMessage);
        this.setBlankPanel();
        this.setRefreshPanel(this);
        
        bReset.setVisible(true);
    }
*/