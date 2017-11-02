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
package sanger.team16.gui.genevar.eqtl.gene;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import sanger.team16.common.business.dao.MatchedFeature;
import sanger.team16.common.business.dao.Statistic;
import sanger.team16.common.hbm.Algorithm;
import sanger.team16.common.hbm.Study;
import sanger.team16.common.hbm.TranscriptMapping;
import sanger.team16.gui.genevar.Message;
import sanger.team16.gui.genevar.SelectedIndex;
import sanger.team16.gui.genevar.UI;
import sanger.team16.gui.genevar.eqtl.EQTLAnalysisPane;
import sanger.team16.gui.genevar.eqtl.MatchedFeatureTableModel;
import sanger.team16.gui.genevar.eqtl.TranscriptTableModel;
import sanger.team16.gui.jface.BaseJLabel;
import sanger.team16.gui.jface.BaseJPane;
import sanger.team16.gui.jface.BaseProgressBar;
import sanger.team16.gui.jface.table.BaseJTable;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class CisEQTLGenePane2 extends EQTLAnalysisPane implements ActionListener
{
    private Study study;
    private JComboBox cbDistance, cbPValue;
    private BaseJTable transcriptTable, matchedFeatureTable;
    private Vector<Algorithm> algorithms;
    private boolean isDefault = true;
    
    public CisEQTLGenePane2(UI ui, Study study, List<TranscriptMapping> transcriptMappings) {
        super(ui);
        this.study = study;
        this.transcriptTable = new TranscriptTableModel(true, transcriptMappings).getTable();
        this.matchedFeatureTable = new MatchedFeatureTableModel(this.getMatchedFeaturesGxE(study.getId())).getTable();
        
        this.refresh(new SelectedIndex(0));
    }
    
    public void refresh(SelectedIndex selectedIndex) {
        this.removeAll();
        this.repaint();   //BUG 10/11/10

        this.setMatchedFeaturePanel();
        this.setBlankPanel();
        this.setTranscriptPanel();
        this.setBlankPanel();
        this.setParameterPanel(selectedIndex, "2. Distance to TSS: ");
        this.setBlankPanel();
        this.setRemoveSubmitPanel(this, "           Run           ");

        this.setSubmitButton();   //BUG 10/11/10
    }

    private void setMatchedFeaturePanel() {
        BaseJPane panel = new BaseJPane("Genotype-Expression Pairs");
        
        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();

        p0.add(new JLabel("Study: "));
        p0.add(new BaseJLabel(study.getName(), new Font("Arial", Font.BOLD, 13)));
        
        p0.setBaseSpringBoxTrailing();
        panel.add(p0);
        // ------------------ Panel p0 (end) ------------------ //
        
        // ------------------ Panel p1 (start) ------------------ //
        //this.matchedFeatureTableModel = new MatchedFeatureTableModel(this.getMatchedFeatures(study.getId()));
        //JTable matchedFeatureTable = this.matchedFeatureTableModel.getTable();
        
        BaseJPane p1 = new BaseJPane();
        p1.setLayout(new BorderLayout());
        p1.add(matchedFeatureTable.getTableHeader(), BorderLayout.PAGE_START);
        p1.add(matchedFeatureTable, BorderLayout.CENTER);

        //p0.setBaseSpringGrid(1, 1);
        panel.add(p1);
        // ------------------ Panel p1 (end) ------------------ //
        
        panel.setBaseSpringGrid(1);
        this.add(panel);        
    }
    
    private void setTranscriptPanel() {
        BaseJPane panel = new BaseJPane("Matched Transcript(s)");

        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();

        p0.add(new JLabel("Platform: "));
        p0.add(new BaseJLabel(this.ui.getPlatform(this.study.getExpressionPlatformId()).getName(), new Font("Arial", Font.BOLD, 13)));
        
        p0.setBaseSpringBoxTrailing();
        panel.add(p0);
        // ------------------ Panel p0 (end) ------------------ //

        panel.add(new JLabel(""));
        
        // ------------------ Panel p1 (start) ------------------ //
        //JTable transcriptTable = this.transcriptTableModel.getTable();   //NOT Thread safe 10/07/11
        
        BaseJPane p1 = new BaseJPane();
        p1.setLayout(new BorderLayout());
        //p1.add(new JLabel("Platform: " + platformName));
        p1.add(transcriptTable.getTableHeader(), BorderLayout.PAGE_START);
        p1.add(transcriptTable, BorderLayout.CENTER);
        
        //p1.setBaseSpringGrid(1, 1);
        panel.add(p1);
        // ------------------ Panel p1 (end) ------------------ //
        
        panel.setBaseSpringGrid(1);
        this.add(panel);
    }
  
    public void setParameterPanel(SelectedIndex selectedIndex, String distanceTo) {
        BaseJPane panel = new BaseJPane("Analysis Parameters");
        
        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();
        algorithms = this.getQTLAlgorithms(study.getId(), "E");
   
        p0.add(new JLabel("1. Correlation and regression: "));
        cbStatistic = new JComboBox(Message.getStatistics(this.ui.getAddress()));
        if (isDefault)
            if (algorithms.size() != 0 && selectedIndex.studyIndex != 0) {
                this.setAlgorithms(algorithms);
                isDefault = false;
            } else {
                this.setNullAlgorithms(selectedIndex.statisticIndex);
                cbStatistic.removeItemAt(cbStatistic.getComponentCount());   //ADD 30/11/10
            }
        else
            if (selectedIndex.statisticIndex == cbStatistic.getComponentCount())
                this.setAlgorithms(algorithms);
            else
                this.setNullAlgorithms(selectedIndex.statisticIndex);
        cbStatistic.addActionListener(this);
        p0.add(cbStatistic);
        p0.add(cbAlgorithm);
        
        p0.add(new JLabel(""));
        
        p0.setBaseSpringBoxTrailing();
        panel.add(p0);
        // ------------------ Panel p0 (end) ------------------ //
        
        panel.add(new JLabel(""));
        
        // ------------------ Panel p1 (start) ------------------ //
        BaseJPane p1 = new BaseJPane();

        p1.add(new JLabel(distanceTo));
        cbDistance = new JComboBox(distances);
        cbDistance.setEditable(true);
        cbDistance.addActionListener(this);
        p1.add(cbDistance);
        p1.add(new BaseJLabel("(Editable, no greater than 1 Mb)", Color.GREEN.darker()));
        
        p1.setBaseSpringBoxTrailing();
        panel.add(p1);
        // ------------------ Panel p1 (end) ------------------ //
        
        panel.add(new JLabel(""));
        
        // ------------------ Panel p2 (start) ------------------ //
        BaseJPane p2 = new BaseJPane();

        p2.add(new JLabel("3. P-value threshold: "));
        cbPValue = new JComboBox(pValues);
        cbPValue.setSelectedIndex(2);
        cbPValue.setEditable(true);
        cbPValue.addActionListener(this);
        p2.add(cbPValue);
        p2.add(new BaseJLabel("(Editable)", Color.GREEN.darker()));
        
        p2.setBaseSpringBoxTrailing();
        panel.add(p2);
        // ------------------ Panel p2 (end) ------------------ //
        
        panel.setBaseSpringGrid(1);
        this.add(panel);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            this.cisGenePane2ActionPerformed(ae);

        } catch (Exception e) {   //ADD 19/11/10
            this.showConnectionErrorMessage(e);
        }
    }
    
    public void cisGenePane2ActionPerformed(ActionEvent ae) {
        int statisticIndex = cbStatistic.getSelectedIndex();
        SelectedIndex selectedIndex = new SelectedIndex(statisticIndex);
  
        if (ae.getSource() == bRemove)
            this.ui.removeCurrentNode();
        
        else if (ae.getSource() == cbStatistic) {
            this.refresh(selectedIndex);
            this.setSubmitButton();
        
        } else if (ae.getSource() == bSubmit) {
            int distance = new Integer(((String) cbDistance.getSelectedItem()).replaceAll(",",""));
            if (distance > 1000000)
                JOptionPane.showMessageDialog(ui, "No greater than 1 Mb",
                    "Distance to TSS", JOptionPane.WARNING_MESSAGE);
            else
                new CisGenePane2ProgressBar().execute();
        }
    }
    
    private void setSubmitButton() {
        if (matchedFeatureTable.getRowCount() == 0)
            this.bSubmit.setEnabled(false);
        else
            this.bSubmit.setEnabled(true);
    }
    
    /**
     * @author Tsun-Po Yang <tpy@sanger.ac.uk>
     * @link   http://www.sanger.ac.uk/Teams/Team16/
     */
    private class CisGenePane2ProgressBar extends SwingWorker<Object, Object>
    {   
        private BaseProgressBar progressBar = new BaseProgressBar(ui);
        
        @Override
        protected Void doInBackground() {
            Statistic statistic = (Statistic) cbStatistic.getSelectedItem();
            int statisticId = getStatisticId(statistic, cbAlgorithm);
            double threshold = getThreshold((String) cbPValue.getSelectedItem());
            int distance = new Integer(((String) cbDistance.getSelectedItem()).replaceAll(",",""));
            
            try {   //ADD 19/11/10
                for (int i=0 ; i<matchedFeatureTable.getRowCount() ; i++)
                    if (((Boolean) matchedFeatureTable.getValueAt(i, 0)).booleanValue()) {
                        String populationName = (String) matchedFeatureTable.getValueAt(i, 1);
                        MatchedFeature matchedFeature = (MatchedFeature) matchedFeatureTable.getValueAt(i, 4);
                        
                        String treeNoteName = getTreeNoteName(populationName, statistic, threshold, distance);
                        ui.addToCurrentNode(treeNoteName, new CisEQTLGenePane3(ui, treeNoteName, transcriptTable, study, matchedFeature, statisticId, algorithms, distance, threshold));   
                    } 
                
            } catch (Exception e) {   //ADD 19/11/10
                showConnectionErrorMessage(e);
            }
            
            return null;
        }
        
        @Override
        protected void done() {
            progressBar.dispose(ui);
        }
    }
}
