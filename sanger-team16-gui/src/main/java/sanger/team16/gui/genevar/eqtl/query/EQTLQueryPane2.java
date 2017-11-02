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
package sanger.team16.gui.genevar.eqtl.query;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

import javax.xml.ws.WebServiceException;

import sanger.team16.common.business.dao.Statistic;
import sanger.team16.common.hbm.Study;
import sanger.team16.common.hbm.TranscriptMapping;
import sanger.team16.common.hbm.Variation;
import sanger.team16.gui.genevar.Message;
import sanger.team16.gui.genevar.UI;
import sanger.team16.gui.genevar.eqtl.EQTLAnalysisPane;
import sanger.team16.gui.genevar.eqtl.MatchedFeatureTableModel;
import sanger.team16.gui.genevar.eqtl.TranscriptTableModel;
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
public class EQTLQueryPane2 extends EQTLAnalysisPane implements ActionListener
{
    private Study study;
    private JComboBox cbStatistic, cbPermutation;
    private BaseJTable transcriptTable, matchedFeatureTable;
    private JTextArea taSNP;
    private BaseJTextField tfSNPFile;
    private JButton bSNPFile;
    //private boolean isPopulationEmpty = false;
    
    public EQTLQueryPane2(UI ui, Study study, List<TranscriptMapping> transcriptMappings, String snpString) throws WebServiceException {
        super(ui);
        this.study = study;
        this.matchedFeatureTable = new MatchedFeatureTableModel(this.getMatchedFeaturesGxE(study.getId())).getTable();
        this.transcriptTable = new TranscriptTableModel(true, transcriptMappings).getTable();

        this.setMatchedFeaturePanel(study);
        this.setBlankPanel();
        this.setTranscriptPanel(study.getExpressionPlatformId());
        this.setBlankPanel();
        this.setSubmitSNPPanel(snpString);
        this.setBlankPanel();
        this.setParameterPanel();
        this.setBlankPanel();
        this.setRemoveSubmitPanel(this, "  SNP-Gene Association Plot  ");

        if (matchedFeatureTable.getRowCount() == 0)
            this.bSubmit.setEnabled(false);
        else
            this.bSubmit.setEnabled(true);
        
        //TODO
        if (study.toString().contains("MuTHER Resource")) {   //TODO QUICK FIX 06/09/12
            cbStatistic.setEnabled(false);   //TODO
            cbPermutation.setEnabled(false);   //TODO
            bSubmit.setEnabled(false);   //TODO
        }
    }

    private void setMatchedFeaturePanel(Study study) {
        BaseJPane panel = new BaseJPane("Genotype-Expression Pairs");
        
        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();

        p0.add(new JLabel("Study: "));
        p0.add(new BaseJLabel(study.getName(), new Font("Arial", Font.BOLD, 13)));
        
        p0.setBaseSpringBoxTrailing();
        panel.add(p0);
        // ------------------ Panel p0 (end) ------------------ //
        
        // ------------------ Panel p1 (start) ------------------ //
        //this.matchedFeatureTable = new MatchedFeatureTableModel(this.getMatchedFeatures(dataset.getId())).getTable();

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
    
    private void setTranscriptPanel(int platformId) {
        BaseJPane panel = new BaseJPane("Matched Transcript(s)");

        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();

        p0.add(new JLabel("Platform: "));
        p0.add(new BaseJLabel(this.ui.getPlatform(platformId).getName(), new Font("Arial", Font.BOLD, 13)));
        
        p0.setBaseSpringBoxTrailing();
        panel.add(p0);
        // ------------------ Panel p0 (end) ------------------ //

        panel.add(new JLabel(""));
        
        // ------------------ Panel p1 (start) ------------------ //
        //this.transcriptTable = this.transcriptTableModel.getTable();
        
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
    
    public void setSubmitSNPPanel(String snpString) {
        BaseJPane panel = new BaseJPane("Associate SNPs with Gene");
        
        // ------------------ Panel p0 (start) ----------------- */
        BaseJPane p0 = new BaseJPane();
        p0.add(new JLabel("Enter rs ID(s): "));
        
        taSNP = new JTextArea(snpString, 5, 1);
        p0.add(new JScrollPane(taSNP));
        
        p0.add(new JLabel(" "));
        p0.setBaseSpringBoxTrailing();
        panel.add(p0);
        // ------------------ Panel p0 (end) ----------------- */
        
        //       
        
        // ------------------ Panel p1 (start) ----------------- */
        BaseJPane p1 = new BaseJPane();
        p1.add(new JLabel("or read from a text file with list of SNPs: ")); 
        
        tfSNPFile = new BaseJTextField(5, true, true);
        tfSNPFile.setText("");
        p1.add(tfSNPFile);        
        
        bSNPFile = new JButton("Browse...");
        bSNPFile.addActionListener(this);
        p1.add(bSNPFile);
        
        p1.add(new JLabel(" "));
        p1.setBaseSpringBoxTrailing();
        panel.add(p1);
        // ------------------ Panel p1 (end) ----------------- */
        
        panel.setBaseSpringGrid(1);
        this.add(panel);
    }   

    public void setParameterPanel() {
        BaseJPane panel = new BaseJPane("Analysis Parameters");
        
        // ------------------ Panel p0 (start) ----------------- */
        BaseJPane p0 = new BaseJPane();

        p0.add(new JLabel("1. Correlation and regression: "));
        cbStatistic = new JComboBox(Message.getStatistics(this.ui.getAddress()));
        cbStatistic.removeItemAt(cbStatistic.getComponentCount());   //ADD 26/07/12
        cbStatistic.setSelectedIndex(0);
        cbStatistic.addActionListener(this);
        p0.add(cbStatistic);
        
        p0.add(new JLabel(" "));
        
        p0.setBaseSpringBoxTrailing();
        panel.add(p0);
        // ------------------ Panel p0 (end) ----------------- */
        
        panel.add(new JLabel(""));
        
        // ------------------ Panel p1 (start) ----------------- */
        BaseJPane p1 = new BaseJPane();

        p1.add(new JLabel("2. Number of permutations: "));
        cbPermutation = new JComboBox(permutations);
        //cbPermutation.setSelectedIndex();
        cbPermutation.addActionListener(this);
        p1.add(cbPermutation);
        
        p1.add(new JLabel(" "));
        
        p1.setBaseSpringBoxTrailing();
        panel.add(p1);
        // ------------------ Panel p1 (end) ----------------- */
        
        panel.setBaseSpringGrid(1);
        this.add(panel);
    }
    
    public void actionPerformed(ActionEvent ae) {
        try {
            this.queryPane2ActionPerformed(ae);
            
        } catch (Exception e) {   //ADD 19/11/10
            this.showConnectionErrorMessage(e);
        }
    }
    
    public void queryPane2ActionPerformed(ActionEvent ae) {
        if (ae.getSource() == bRemove) {
            this.ui.removeCurrentNode();
            
        } else if (ae.getSource() == bSNPFile) {
            this.ui.addChoosableFileFilter(Message.TXT);
            
            if (this.ui.showFileChooserOpenDialog(this.ui, null)) {
                File file = this.ui.getFileChooserSelectedFile();
                tfSNPFile.setText(file.getAbsolutePath());
            }
            
        } else if (ae.getSource() == bSubmit)
            new QueryPane2ProgressBar().execute();
        
        //this.setSubmit();
    }

    /**
     * @author Tsun-Po Yang <tpy@sanger.ac.uk>
     * @link   http://www.sanger.ac.uk/Teams/Team16/
     */
    private class QueryPane2ProgressBar extends SwingWorker<Object, Object>
    {   
        private BaseProgressBar progressBar = new BaseProgressBar(ui);

        @Override
        protected Void doInBackground() {
            List<String> inputSNPs = getInputSNPs(taSNP.getText(), tfSNPFile.getText());
            Statistic statistic = (Statistic) cbStatistic.getSelectedItem();
            int permutation = 0;
            if (cbPermutation.getSelectedIndex() != 0)
                permutation = new Integer(((String) cbPermutation.getSelectedItem()).replaceAll(",",""));

            try {
                for (int i=0 ; i<inputSNPs.size() ; i++) {
                    String variationName = inputSNPs.get(i);
                    Variation snp = getSNP(study.getId(), variationName);
                    String description = variationName;
                    
                    if (snp != null) {
                        description += " (" + statistic.abbreviation;   //TODO
                        if (permutation != 0)
                            description += ", FDR";
                        description += ")";
                            
                        ui.addToCurrentNode(description, new EQTLQueryPane3(ui, matchedFeatureTable, snp, transcriptTable, statistic.getId(), permutation)); 
                    
                    } else
                        ui.addToCurrentNode(description, new EQTLQueryPane3(ui, variationName));
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
