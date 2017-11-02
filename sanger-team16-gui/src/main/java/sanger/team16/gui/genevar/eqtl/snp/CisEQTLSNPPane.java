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
package sanger.team16.gui.genevar.eqtl.snp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import org.jfree.chart.ChartPanel;

import sanger.team16.common.business.dao.MatchedFeature;
import sanger.team16.common.business.dao.Statistic;
import sanger.team16.common.hbm.Algorithm;
import sanger.team16.common.hbm.Study;
import sanger.team16.common.hbm.TranscriptMapping;
import sanger.team16.common.hbm.Variation;
import sanger.team16.common.hbm.Reference;
import sanger.team16.common.xqtl.QTL;
import sanger.team16.gui.genevar.Message;
import sanger.team16.gui.genevar.SelectedIndex;
import sanger.team16.gui.genevar.UI;
import sanger.team16.gui.genevar.eqtl.EQTLAnalysisPane;
import sanger.team16.gui.genevar.eqtl.MatchedFeatureTableModel;
import sanger.team16.gui.jface.BaseJLabel;
import sanger.team16.gui.jface.BaseJPane;
import sanger.team16.gui.jface.BaseJTextField;
import sanger.team16.gui.jface.BaseProgressBar;
import sanger.team16.gui.jface.table.BaseJTable;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class CisEQTLSNPPane extends EQTLAnalysisPane implements ActionListener
{
    private Study study;   //TOTO only in cis-SNP
    private JComboBox cbStudy, cbReference, cbDistance, cbPValue;
    private JTextArea taSNP;
    private JButton bSNPFile;
    private BaseJTextField tfSNPFile;
    private MatchedFeatureTableModel matchedFeatureTableModel;
    private BaseJTable matchedFeatureTable;
    private Vector<Algorithm> algorithms;
    private boolean isDefault = true;
    
    public CisEQTLSNPPane(UI ui) {
        super(ui);
        
        this.refresh(new SelectedIndex());
    }
    
    public void refresh(SelectedIndex selectedIndex) {
        this.removeAll();
        this.repaint();
        
        this.setSubmitSNPPanel(selectedIndex);
        this.setBlankPanel();
        this.setParameterPanel(selectedIndex, "2. Distance to SNP: ");
        this.setBlankPanel();
        this.setResetSubmitPanel(this, "           Run           ");
        
        bReset.setVisible(true);   // MUST HAVE!!! Together with CommonPane.setResetButton()
    }
    
    public void setSubmitSNPPanel(SelectedIndex selectedIndex) {
        BaseJPane panel = new BaseJPane("Query SNPs");

        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();

        p0.add(new JLabel("1. Select a study: "));
        cbStudy = new JComboBox(this.ui.getStudies("E"));
        cbStudy.setSelectedIndex(selectedIndex.studyIndex);
        cbStudy.addActionListener(this);
        p0.add(cbStudy);
        
        p0.add(new JLabel(" "));
        
        p0.setBaseSpringBoxTrailing();
        panel.add(p0);
        // ------------------ Panel p0 (end) ------------------ //
        
        panel.add(new JLabel(""));
        
        // ------------------ Panel p1 (start) ------------------ //
        BaseJPane p1 = new BaseJPane();        
        p1.add(new JLabel("2. Choose a reference: "));
        
        this.study = (Study) cbStudy.getSelectedItem();
        cbReference = new JComboBox(this.ui.getReferences(study.getGenotypeAssemblyId(), study.getExpressionPlatformId()));
        cbReference.setSelectedIndex(selectedIndex.referencesIndex);
        cbReference.addActionListener(this);
        p1.add(cbReference);
        
        p1.add(new JLabel(" "));
        p1.setBaseSpringBoxTrailing();
        panel.add(p1);
        // ------------------ Panel p1 (end) ------------------ // 
        
        panel.add(new JLabel(""));
        
        /* ----------------- Panel p3 (start) ----------------- */
        BaseJPane p3 = new BaseJPane();
        p3.add(new JLabel("3. Enter rs ID(s): "));
        
        taSNP = new JTextArea(selectedIndex.textArea1, 6, 1);
        p3.add(new JScrollPane(taSNP));
        
        p3.add(new JLabel(" "));
        p3.setBaseSpringBoxTrailing();
        panel.add(p3);
        /* ----------------- Panel p3 (end) ----------------- */
        
        //        
        
        /* ----------------- Panel p4 (start) ----------------- */
        BaseJPane p4 = new BaseJPane();
        p4.add(new JLabel("    or read from a text file with list of SNPs: "));
        
        tfSNPFile = new BaseJTextField(5, true, true);
        tfSNPFile.setText("");
        p4.add(tfSNPFile);        
        
        bSNPFile = new JButton("Browse...");
        bSNPFile.addActionListener(this);
        p4.add(bSNPFile);
        
        p4.add(new JLabel(" "));
        p4.setBaseSpringBoxTrailing();
        panel.add(p4);
        /* ----------------- Panel p4 (end) ----------------- */
        
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        
        panel.add(this.setMatchedFeaturePanel());  
        
        panel.setBaseSpringGrid(1);
        this.add(panel);
    }
    
    private BaseJPane setMatchedFeaturePanel() {
        BaseJPane panel = new BaseJPane("Genotype-Expression Pairs");
        //Study study = (Study) cbStudy.getSelectedItem();
        
        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();

        p0.add(new JLabel("Study: "));
        p0.add(this.getJLabeledSelectedItem(cbStudy));
        
        p0.setBaseSpringBoxTrailing();
        panel.add(p0);
        // ------------------ Panel p0 (end) ------------------ //
        
        // ------------------ Panel p1 (start) ------------------ //
        List<MatchedFeature> matchedFeatures = this.getMatchedFeaturesGxE(study.getId());
        this.matchedFeatureTableModel = new MatchedFeatureTableModel(matchedFeatures);
        this.matchedFeatureTable = matchedFeatureTableModel.getTable();   //TODO
        
        BaseJPane p1 = new BaseJPane();
        p1.setLayout(new BorderLayout());
        p1.add(matchedFeatureTable.getTableHeader(), BorderLayout.PAGE_START);
        p1.add(matchedFeatureTable, BorderLayout.CENTER);

        //p0.setBaseSpringGrid(1, 1);
        panel.add(p1);
        // ------------------ Panel p1 (end) ------------------ //
        
        panel.setBaseSpringGrid(1);
        return panel;        
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
                cbStatistic.removeItemAt(cbStatistic.getComponentCount());   //ADD 02/12/10
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
            this.cisSNPPaneActionPerformed(ae);
            
        } catch (Exception e) {   //ADD 19/11/10
            this.showConnectionErrorMessage(e);
        }
    }
    
    private void cisSNPPaneActionPerformed(ActionEvent ae) {
        int studyIndex = cbStudy.getSelectedIndex();
        int statisticIndex = cbStatistic.getSelectedIndex();
        //SelectedIndex selectedIndex = new SelectedIndex(studyIndex, this.getStudyReferenceIndex(studyIndex), statisticIndex, taSNP.getText());   //CHANGE 07/12/11 dun know? only do it if ae.getSource() == cbStudy 
        //int referenceIndex = cbReference.getSelectedIndex();   //TODO merge into SelectedIndex
        //int referenceId = ((Reference) cbReference.getSelectedItem()).getId();   //TODO

        if (ae.getSource() == cbStudy) {
            if (studyIndex == 0)
                this.refresh(new SelectedIndex());
            else {   //TODO
                //List<MatchedFeature> matchedFeatures = this.getMatchedFeatures(study.getId());
                //this.matchedFeatureTable = new MatchedFeatureTableModel(matchedFeatures).getTable();
                statisticIndex = 0;   //ADD 02/12/10
                
                this.refresh(new SelectedIndex(studyIndex, this.getStudyReferenceIndex(studyIndex), statisticIndex, taSNP.getText()));
                this.setSubmitButton();
            }
            isDefault = true;   //ADD 02/12/10
            
        } else if (ae.getSource() == cbReference) {
            this.setSubmitButton();   // ADD 18/06/10
        
        } else if (ae.getSource() == cbStatistic) {
            this.refresh(new SelectedIndex(studyIndex, this.getStudyReferenceIndex(studyIndex), statisticIndex, taSNP.getText()));
            this.setSubmitButton();
            
        } else if (ae.getSource() == bReset) {
            this.refresh(new SelectedIndex()); 

        } else if (ae.getSource() == bSNPFile) {
            this.ui.addChoosableFileFilter(Message.TXT);
            
            if (this.ui.showFileChooserOpenDialog(this.ui, null))
                tfSNPFile.setText(this.ui.getFileChooserSelectedFile().getAbsolutePath());
            
        } else if (ae.getSource() == bSubmit) {
            int distance = new Integer(((String) cbDistance.getSelectedItem()).replaceAll(",",""));
            if (distance > 1000000)
                JOptionPane.showMessageDialog(ui, "No greater than 1 Mb",
                    "Distance to SNP", JOptionPane.WARNING_MESSAGE);
            else
                new CisSNPPaneProgressBar().execute();
            //bSubmit.setEnabled(false);
        }
    }
    
    private int getStudyReferenceIndex(int studyIndex) {   //TODO
    	Study study = (Study) cbStudy.getItemAt(studyIndex);
        String etal = "";
        if (study.getName().contains("("))
            etal = study.getName().split("\\(")[1].split("\\)")[0];
        cbReference = new JComboBox(this.ui.getReferences(study.getGenotypeAssemblyId(), study.getExpressionPlatformId()));
    	
    	for (int i=0 ; i<cbReference.getItemCount() ; i++)
    		if (((Reference) cbReference.getItemAt(i)).getName().contains(etal))
    			return i;
    	return 0;
    }
    
    private void setSubmitButton() {   //TODO
        if (matchedFeatureTable.getRowCount() == 0)
            this.bSubmit.setEnabled(false);
        else
            this.bSubmit.setEnabled(true);
    }
    
    /**
     * @author Tsun-Po Yang <tpy@sanger.ac.uk>
     * @link   http://www.sanger.ac.uk/Teams/Team16/
     */
    private class CisSNPPaneProgressBar extends SwingWorker<Object, Object>
    {   
        private BaseProgressBar progressBar = new BaseProgressBar(ui);

        @Override
        protected Void doInBackground() {
           	int assemblyId = ((Reference) cbReference.getSelectedItem()).getAssemblyId();
            int referenceId = ((Reference) cbReference.getSelectedItem()).getId();   //TODO
            Statistic statistic = (Statistic) cbStatistic.getSelectedItem();
            int statisticId = getStatisticId(statistic, cbAlgorithm);
            int distance = new Integer(((String) cbDistance.getSelectedItem()).replaceAll(",",""));
            double threshold = getThreshold((String) cbPValue.getSelectedItem());
            List<String> inputSNPs = getInputSNPs(taSNP.getText(), tfSNPFile.getText());   //!!!!

            try {
                for (int i=0 ; i< inputSNPs.size(); i++) {
                    String variationName = inputSNPs.get(i);
                    Variation snp = getSNP(study.getId(), variationName);
                    List<String> sigEQTLs = new ArrayList<String>();   //TODO globle?
                    
                    if (snp != null) {
                        String chromosome = snp.getChromosome();
                        int position = snp.getPosition();
                        List<TranscriptMapping> transcriptMappings = getTranscriptMappingsWhereSNP(study.getExpressionPlatformId(), referenceId, snp, distance);
           
                        RegionalLinePlot regionalLinePlot = createRegionalLinePlot(snp, transcriptMappings, sigEQTLs, statisticId, threshold);                        
                        ChartPanel reginalLinePanel = regionalLinePlot.getChartPanel(chromosome, position, distance, threshold);
                        
                        String treeNoteName = getTreeNoteName(snp.getName(), statistic, threshold, distance);
                        ui.addToCurrentNode(treeNoteName, new CisEQTLSNPPane2(ui, snp, matchedFeatureTable, reginalLinePanel, transcriptMappings, sigEQTLs, assemblyId, distance, statisticId, algorithms, treeNoteName)); 

                    } else
                        ui.addToCurrentNode(variationName, new CisEQTLSNPPane2(ui, variationName));
                }
                
            } catch (Exception e) {   //ADD 19/11/10
                showConnectionErrorMessage(e);
            }
            
            return null;
        }

        private RegionalLinePlot createRegionalLinePlot(Variation snp, List<TranscriptMapping> transcriptMappings, List<String> sigEQTLs, int statisticId, double threshold) {
            RegionalLinePlot regionalLinePlot = new RegionalLinePlot();
            
            for (int i=0 ; i<matchedFeatureTable.getRowCount() ; i++)
                if (((Boolean) matchedFeatureTable.getValueAt(i, 0)).booleanValue()) {
                    String populationName = (String) matchedFeatureTable.getValueAt(i, 1);
                    MatchedFeature matchedFeature = (MatchedFeature) matchedFeatureTable.getValueAt(i, 4);
                    
                    //EQTLStats eqtlStats = new EQTLStats(ui.isServices(), ui.getAddress());
                    //int genotypeFeatureId = getGenotypeFeatureId(matchedFeature, snp);   //ADD 08/12/11
                    List<QTL> eqtls = getEQTLsWhereSNP(matchedFeature, snp, transcriptMappings, statisticId, threshold);
                    
                    if (eqtls != null && eqtls.size() != 0) {   // BUG 20/10/10
                        sigEQTLs.addAll(this.getSigEQTLs(eqtls, threshold));
                        regionalLinePlot.addToDataset(populationName, eqtls);   
                    }
                }
            
            return regionalLinePlot;
        }
        
        @Override
        protected void done() {
            progressBar.dispose(ui);
        }
 
        private List<String> getSigEQTLs(List<QTL> eqtls, double threshold) {
            List<String> significants = new ArrayList<String>();
            
            if (eqtls != null && eqtls.size() != 0)   // BUG 20/10/10
                for (int i=0 ; i<eqtls.size() ; i++) {
                    QTL eqtl = eqtls.get(i);
                    
                    if (eqtl.nominalP <= threshold)
                        significants.add(eqtl.transcriptMapping.getProbeId());
                }
            
            return significants;
        }
    }
}
