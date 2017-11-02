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
import java.io.File;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import sanger.team16.common.hbm.Study;
import sanger.team16.common.hbm.Reference;
import sanger.team16.common.hbm.TranscriptMapping;
import sanger.team16.gui.genevar.InputKeeper;
import sanger.team16.gui.genevar.Message;
import sanger.team16.gui.genevar.SelectedIndex;
import sanger.team16.gui.genevar.UI;
import sanger.team16.gui.genevar.eqtl.EQTLAnalysisPane;
import sanger.team16.gui.genevar.eqtl.GeneTableModel;
import sanger.team16.gui.jface.BaseJPane;
import sanger.team16.gui.jface.BaseJTextField;
import sanger.team16.gui.jface.BaseProgressBar;
import sanger.team16.gui.jface.table.BaseJTable;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class CisEQTLGenePane extends EQTLAnalysisPane implements ActionListener
{
    private JButton bQuery;
    private JComboBox cbStudy, cbReference;
    private JTextArea taGene;
    private JCheckBox ckMatch;
    private JButton bFile;
    private BaseJTextField tfGeneFile;
    private InputKeeper inputKeeper;
    private BaseJTable geneTable;
    
    public CisEQTLGenePane(UI ui) {
        super(ui);
        this.inputKeeper = new InputKeeper();
        this.geneTable = new GeneTableModel().getTable();
        
        this.refresh(new SelectedIndex(), false);
    }
    
    public void refresh(SelectedIndex selectedIndex, boolean searched) {
        this.removeAll();
        this.repaint();
        
        this.setQueryGenePanel(selectedIndex, searched);
        this.setBlankPanel();
        this.setResetSubmitPanel(this, "     Find Probes     ");

        this.setQueryButton();
        bReset.setVisible(true);   // MUST HAVE!!! Together with CommonPane.setResetButton()
    }
    
    private void setQueryGenePanel(SelectedIndex selectedIndex, boolean searched) {
        BaseJPane panel = new BaseJPane("Query Genes");
        
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
        
        Study study = (Study) cbStudy.getSelectedItem();
        cbReference = new JComboBox(this.ui.getReferences(study.getGenotypeAssemblyId(), study.getExpressionPlatformId()));
        cbReference.setSelectedIndex(selectedIndex.referencesIndex);   //TODO combine with getStudyReferenceIndex()
        cbReference.addActionListener(this);
        p1.add(cbReference);
        
        p1.add(new JLabel(" "));
        p1.setBaseSpringBoxTrailing();
        panel.add(p1);
        // ------------------ Panel p1 (end) ------------------ // 
        
        panel.add(new JLabel(""));
        
        // ------------------ Panel p2 (start) ------------------ //
        BaseJPane p2 = new BaseJPane();
        p2.add(new JLabel("3. Enter gene symbol/Ensembl gene/probe ID(s): "));

        taGene = new JTextArea(selectedIndex.textArea1, 5, 0);
        p2.add(new JScrollPane(taGene));
        
        p2.add(new JLabel(" "));
        p2.setBaseSpringBoxTrailing();
        panel.add(p2);
        // ------------------ Panel p2 (end) ------------------ //  

        //panel.add(new JLabel(""));
        
        // ------------------ Panel p3 (start) ------------------ //
        BaseJPane p3 = new BaseJPane();
        p3.add(new JLabel("    or read from a text file with list of genes: "));
        
        tfGeneFile = new BaseJTextField(5, true, true);
        tfGeneFile.setText(selectedIndex.textField1);
        p3.add(tfGeneFile);        
        
        bFile = new JButton("Browse...");
        bFile.addActionListener(this);
        p3.add(bFile);
        
        p3.setBaseSpringBoxTrailing();
        panel.add(p3);
        // ------------------ Panel p3 (end) ------------------ //
        
        panel.add(new JLabel(" "));
        
        // ------------------ Panel p4 (start) ------------------ //
        BaseJPane p4 = new BaseJPane();
        p4.add(Box.createHorizontalGlue());

        ckMatch = new JCheckBox();
        ckMatch.setBackground(Color.WHITE);
        ckMatch.setSelected(selectedIndex.matched);
        p4.add(ckMatch);
        p4.add(new JLabel("Match whole word only "));
        
        bQuery = new JButton("        Search        ");
        bQuery.setFont(new Font("", Font.BOLD, 13));
        //bQuery.setForeground(Color.BLUE);
        bQuery.addActionListener(this);
        p4.add(bQuery);
        
        p4.setBaseSpringBox();
        panel.add(p4);
        // ------------------ Panel p4 (end) ------------------ //  

        //panel.add(new JLabel(""));
        panel.add(this.getSearchResultPanel(searched));
        
        panel.setBaseSpringGrid(1);
        this.add(panel);
    }
    
    private BaseJPane getSearchResultPanel(boolean searched) {
        String title = "Query Results";
        BaseJPane panel = new BaseJPane(title);
        
        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();

        p0.add(new JLabel("Study: "));
        p0.add(this.getJLabeledSelectedItem(cbStudy, searched));
        
        p0.setBaseSpringBoxTrailing();
        panel.add(p0);
        // ------------------ Panel p0 (end) ------------------ //

        //panel.add(new JLabel(""));

        // ------------------ Panel p1 (start) ------------------ //
        BaseJPane p1 = new BaseJPane();

        p1.add(new JLabel("Reference: "));
        p1.add(this.getJLabeledSelectedItem(cbReference, searched));
        
        p1.setBaseSpringBoxTrailing();
        panel.add(p1);
        // ------------------ Panel p1 (end) ------------------ //

        panel.add(new JLabel(""));
        
        // ------------------ Panel p3 (start) ------------------ //
        //BaseJTable geneTable = this.geneTableModel.getTable();

        BaseJPane p3 = new BaseJPane();
        p3.setLayout(new BorderLayout());
        p3.add(geneTable.getTableHeader(), BorderLayout.PAGE_START);
        p3.add(geneTable, BorderLayout.CENTER);
        panel.add(p3);
        // ------------------ Panel p3 (end) ------------------ //
        
        panel.setBaseSpringGrid(1);
        return panel;
    }
    
    public void actionPerformed(ActionEvent ae) {
        try {
            this.cisGenePaneActionPerformed(ae);
            
        } catch (Exception e) {   //ADD 19/11/10
            this.showConnectionErrorMessage(e);
        }
    }
    
    private void cisGenePaneActionPerformed(ActionEvent ae) {
        int studyIndex = cbStudy.getSelectedIndex();
    	int platformId = ((Study) cbStudy.getSelectedItem()).getExpressionPlatformId();
        int referenceId = ((Reference) cbReference.getSelectedItem()).getId();   //TODO
        int referenceIndex = cbReference.getSelectedIndex();   //TODO
        String geneString = taGene.getText();
        String geneFile = tfGeneFile.getText();
        boolean matched = ckMatch.isSelected();
        
        if (ae.getSource() == cbStudy) {
            this.geneTable = new GeneTableModel().getTable();

            if (cbStudy.getSelectedIndex() == 0)
                this.refresh(new SelectedIndex(0, 0, geneString, geneFile, matched), false);
            else
                this.refresh(new SelectedIndex(studyIndex, this.getStudyReferenceIndex(studyIndex), geneString, geneFile, matched), false);
        
        } else if (ae.getSource() == bReset) {
            this.inputKeeper = new InputKeeper();
            this.geneTable = new GeneTableModel().getTable();

            this.refresh(new SelectedIndex(), false); 
    
        } else if (ae.getSource() == bFile) {
            this.ui.addChoosableFileFilter(Message.TXT);
            
            if (this.ui.showFileChooserOpenDialog(this.ui, null)) {
                File file = this.ui.getFileChooserSelectedFile();
                tfGeneFile.setText(file.getAbsolutePath());
            }
            
        } else if (ae.getSource() == bQuery) {
            this.inputKeeper = new InputKeeper(geneString, geneFile);
            this.geneTable = new GeneTableModel(this.getGenes(platformId, referenceId, inputKeeper.inputs, matched)).getTable();
            
            this.refresh(new SelectedIndex(studyIndex, referenceIndex, geneString, geneFile, matched), true);
            this.setSubmitButton();
            
        } else if (ae.getSource() == bSubmit) {
            new CisGenePaneProgressBar().execute();

            bSubmit.setEnabled(false);
        }
        
        this.setQueryButton();
    }
    
    private int getStudyReferenceIndex(int studyIndex) {   //TODO same in eqtl-query and cis-snp
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
    
    private void setQueryButton() {
        if (cbStudy.getSelectedIndex() != 0 && cbReference.getSelectedIndex() != 0)
            this.bQuery.setEnabled(true);
        else
            this.bQuery.setEnabled(false);
    }
    
    private void setSubmitButton() {   //TODO
        int cnt = 0;
        for (int i=0 ; i<geneTable.getRowCount() ; i++)
            if (((Boolean) geneTable.getValueAt(i, 0)).booleanValue())
                cnt++;

        if (cnt == 0)
            bSubmit.setEnabled(false);
        else
            bSubmit.setEnabled(true);
    }
    
    /**
     * @author Tsun-Po Yang <tpy@sanger.ac.uk>
     * @link   http://www.sanger.ac.uk/Teams/Team16/
     */
    private class CisGenePaneProgressBar extends SwingWorker<Object, Object>
    {   
        private BaseProgressBar progressBar = new BaseProgressBar(ui);
        
        @Override
        protected Void doInBackground() {
            Study study = (Study) cbStudy.getSelectedItem();
            int referenceId = ((Reference) cbReference.getSelectedItem()).getId();

            try {   //ADD 19/11/10
                int row = geneTable.getRowCount();
                for (int i=0 ; i<row ; i++)
                    if ((Boolean) geneTable.getValueAt(i, 0)) {
                        String geneSymbol = (String) geneTable.getValueAt(i, 6);
                        List<TranscriptMapping> transcriptMappings = getTranscriptMappings(study.getExpressionPlatformId(), referenceId, geneSymbol);
                        
                        ui.addToCurrentNode(geneSymbol, new CisEQTLGenePane2(ui, study, transcriptMappings));
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
