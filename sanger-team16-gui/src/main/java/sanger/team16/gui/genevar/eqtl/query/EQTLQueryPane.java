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
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
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
public class EQTLQueryPane extends EQTLAnalysisPane implements ActionListener
{
    private JButton bQuery;
    private JComboBox cbStudy, cbReference;
    private JTextArea taGene, taSNP;
    private JCheckBox ckMatch;
    private JButton bFile, bSNPFile;
    private BaseJTextField tfGeneFile, tfSNPFile;
    //private ArrayList<String> geneSymbols = new ArrayList<String>();
    private InputKeeper inputKeeper;
    private BaseJTable geneTable;
    
    public EQTLQueryPane(UI ui) {
        super(ui);
        this.inputKeeper = new InputKeeper();
        this.geneTable = new GeneTableModel().getTable2();
        
        this.refresh(new SelectedIndex(), false);
    }
    
    public void refresh(SelectedIndex selectedIndex, boolean searched) {
        this.removeAll();
        this.repaint();
        
        this.setQueryGenePanel(selectedIndex, searched);
        this.setBlankPanel();
        this.setSubmitSNPPanel(selectedIndex);
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
        cbReference.setSelectedIndex(selectedIndex.referencesIndex);
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
        p3.add(new JLabel("    or read from a text file with list of gene-SNP pairs (tab-delimited): "));
        
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
        //geneTable = this.geneTableModel.getTable();   //TODO
        
        BaseJPane p3 = new BaseJPane();
        p3.setLayout(new BorderLayout());
        p3.add(geneTable.getTableHeader(), BorderLayout.PAGE_START);
        p3.add(geneTable, BorderLayout.CENTER);
        
        panel.add(p3);
        // ------------------ Panel p3 (end) ------------------ //
        
        panel.setBaseSpringGrid(1);
        return panel;
    }
    
    public void setSubmitSNPPanel(SelectedIndex selectedIndex) {
        BaseJPane panel = new BaseJPane("Associate SNPs with Each Gene (Optional)");

        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();
        p0.add(new JLabel("Enter rs ID(s): "));
        
        taSNP = new JTextArea(selectedIndex.textArea2, 3, 1);
        p0.add(new JScrollPane(taSNP));
        
        p0.add(new JLabel(" "));
        p0.setBaseSpringBoxTrailing();
        panel.add(p0);
        // ------------------ Panel p0 (end) ------------------ //
        
        //        
        
        // ------------------ Panel p1 (start) ------------------ //
        BaseJPane p1 = new BaseJPane();
        p1.add(new JLabel("or read from a text file with list of SNPs: "));
        
        tfSNPFile = new BaseJTextField(5, true, true);
        tfSNPFile.setText(selectedIndex.textField2);
        p1.add(tfSNPFile);        
        
        bSNPFile = new JButton("Browse...");
        bSNPFile.addActionListener(this);
        p1.add(bSNPFile);
        
        p1.add(new JLabel(" "));
        p1.setBaseSpringBoxTrailing();
        panel.add(p1);
        // ------------------ Panel p1 (end) ------------------ //
        
        panel.setBaseSpringGrid(1);
        this.add(panel);
    }
    
    public void actionPerformed(ActionEvent ae) {
        try {
            this.queryPaneActionPerformed(ae);
            
        } catch (Exception e) {   //ADD 19/11/10
            this.showConnectionErrorMessage(e);
        }
    }
    
    public void queryPaneActionPerformed(ActionEvent ae) {
        int platformId = ((Study) cbStudy.getSelectedItem()).getExpressionPlatformId();
        int referenceId = ((Reference) cbReference.getSelectedItem()).getId(); 
        int studyIndex = cbStudy.getSelectedIndex();
        int referenceIndex = cbReference.getSelectedIndex();
        String geneString = taGene.getText();
        String geneFile = tfGeneFile.getText();
        String snpString = taSNP.getText();
        String snpFile = tfSNPFile.getText();
        boolean matched = ckMatch.isSelected();
        
        if (ae.getSource() == cbStudy) {
            this.geneTable = new GeneTableModel().getTable2();

            if (cbStudy.getSelectedIndex() == 0)
                this.refresh(new SelectedIndex(0, 0, geneString, geneFile, snpString, snpFile, matched), false);
            else
                this.refresh(new SelectedIndex(studyIndex, this.getStudyReferenceIndex(studyIndex), geneString, geneFile, snpString, snpFile, matched), false);
            
        } else if (ae.getSource() == bReset) {
            this.inputKeeper = new InputKeeper();
            this.geneTable = new GeneTableModel().getTable2();
            
            this.refresh(new SelectedIndex(), false); 
    
        } else if (ae.getSource() == bFile) {
            this.ui.addChoosableFileFilter(Message.TXT);
            
            if (this.ui.showFileChooserOpenDialog(this.ui, null)) {
                File file = this.ui.getFileChooserSelectedFile();
                tfGeneFile.setText(file.getAbsolutePath());
            }
            
        } else if (ae.getSource() == bSNPFile) {
            this.ui.addChoosableFileFilter(Message.TXT);
            
            if (this.ui.showFileChooserOpenDialog(this.ui, null)) {
                File file = this.ui.getFileChooserSelectedFile();
                tfSNPFile.setText(file.getAbsolutePath());
            }
        
        } else if (ae.getSource() == bQuery) {
            this.inputKeeper = new InputKeeper(geneString, geneFile);
            this.geneTable = new GeneTableModel(this.getGenes(platformId, referenceId, inputKeeper.inputs, matched), inputKeeper.inputs).getTable();
            
            this.refresh(new SelectedIndex(studyIndex, referenceIndex, geneString, geneFile, snpString, snpFile, matched), true);
            this.setSubmitButton();
            
        } else if (ae.getSource() == bSubmit) {
            new QueryPaneProgressBar().execute();
            
            bSubmit.setEnabled(false);
        }
        
        this.setQueryButton();
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
    
    private void setQueryButton() {
        if (cbStudy.getSelectedIndex() != 0 && cbReference.getSelectedIndex() != 0)
            this.bQuery.setEnabled(true);
        else
            this.bQuery.setEnabled(false);
    }

    private void setSubmitButton() {
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
    private class QueryPaneProgressBar extends SwingWorker<Object, Object>
    {   
        private BaseProgressBar progressBar = new BaseProgressBar(ui);

        @Override
        protected Void doInBackground() {
            Study study = (Study) cbStudy.getSelectedItem();
            int referenceId = ((Reference) cbReference.getSelectedItem()).getId();
            
            try {
                int row = geneTable.getRowCount();
                for (int i=0 ; i<row ; i++)
                    if ((Boolean) geneTable.getValueAt(i, 0)) {
                        String geneSymbol = (String) geneTable.getValueAt(i, 6);
                        String snp = (String) geneTable.getValueAt(i, 7);
                        ArrayList<String> snps = new ArrayList<String>();
                        String snpString = taSNP.getText();
                        String fileName = tfSNPFile.getText();
                        List<TranscriptMapping> transcriptMappings = getTranscriptMappings(study.getExpressionPlatformId(), referenceId, geneSymbol);
                        
                        if (!snp.equals(""))
                            snps.addAll(convertTextAreaToArrayList(snp.replaceAll(", ", "\n")));
                        if (!snpString.equals(""))
                            snps.addAll(convertTextAreaToArrayList(snpString));
                        if (!fileName.equals(""))
                            snps.addAll(readFromFile(fileName));

                        ui.addToCurrentNode(geneSymbol, new EQTLQueryPane2(ui, study, transcriptMappings, convertArrayListToTextAreaString(snps)));
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
