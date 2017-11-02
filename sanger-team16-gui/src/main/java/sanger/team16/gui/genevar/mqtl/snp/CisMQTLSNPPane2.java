/**
 *  This file is part of Genevar (GENe Expression VARiation)
 *  Copyright (C) 2013  Genome Research Ltd.
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
package sanger.team16.gui.genevar.mqtl.snp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingWorker;

import org.jfree.chart.ChartPanel;

import sanger.team16.common.business.dao.Statistic;
import sanger.team16.common.hbm.Algorithm;
import sanger.team16.common.hbm.ModificationMapping;
import sanger.team16.common.hbm.Variation;
import sanger.team16.gui.genevar.Message;
import sanger.team16.gui.genevar.UI;
import sanger.team16.gui.genevar.eqtl.query.EQTLQueryPane3;
import sanger.team16.gui.genevar.mqtl.MQTLAnalysisPane;
import sanger.team16.gui.genevar.mqtl.ModificationTableModel;
import sanger.team16.gui.jface.BaseJPane;
import sanger.team16.gui.jface.BaseProgressBar;
import sanger.team16.gui.jface.table.BaseJTable;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class CisMQTLSNPPane2 extends MQTLAnalysisPane implements ActionListener
{   
    private JComboBox cbPermutation;
    private Variation snp;
    private BaseJTable matchedFeatureTable;
    private JTable modificationTable;
    private List<String> significants;
    private int assemblyId, distanceToSNP;
    private String fileName;
    
    public CisMQTLSNPPane2(UI ui, Variation snp, BaseJTable matchedFeatureTable, ChartPanel regionalLinePlot, List<ModificationMapping> modificationMappings, List<String> significants, int assemblyId, int distanceToSNP,  int statisticId, Vector<Algorithm> algorithms, String fileName) {
        super(ui);
        this.assemblyId = assemblyId;
        this.snp = snp;
        this.matchedFeatureTable = matchedFeatureTable;
        this.modificationTable = new ModificationTableModel(false, modificationMappings).getSortedTable();   //TODO
        this.significants = significants;
        this.distanceToSNP = distanceToSNP;
        this.fileName = fileName;
        
        this.setMofidicationPanel(snp.getName(), regionalLinePlot);
        this.setBlankPanel();
        this.setParameterPanel(statisticId, algorithms);
        this.setBlankPanel();
        this.setRemoveSubmitPanel(this, "  SNP-CpG Association Plot  ");
        
        //TODO
        if (Statistic.isExternalAlgorithm(statisticId)) {
            cbStatistic.setEnabled(false);   //TODO
            cbAlgorithm.setEnabled(false);   //TODO
            cbPermutation.setEnabled(false);   //TODO
            bSubmit.setEnabled(false);   //TODO
        }
    }
    
    public CisMQTLSNPPane2(UI ui, String variationName) {
        super(ui);
        
        this.setErrorPanel(variationName);
        this.setBlankPanel();
        this.setRemovePanel(this);
    }

    private void setMofidicationPanel(String variationName, ChartPanel regionalLinePlot) throws ArrayIndexOutOfBoundsException {
        BaseJPane panel = new BaseJPane(variationName,10,20,10,20);
        
        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();

        p0.add(regionalLinePlot);
        
        //p0.setBaseSpringBoxTrailing();
        panel.add(p0);
        // ------------------ Panel p0 (end) ------------------ //
        
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        
        // ------------------ Panel p1 (start) ------------------ //
        BaseJPane p1 = new BaseJPane();
        
        JButton bSelectAll = new JButton(" Deselect All ");
        JButton bExport = new JButton("  Export List  ");
        CisSNPPane2Listener cisSNP2Listener = new CisSNPPane2Listener(bSelectAll, bExport);
        
        //bSelectAll.setForeground(new Color(34,139,34));
        bSelectAll.setForeground(Color.GREEN.darker());
        bSelectAll.addActionListener(cisSNP2Listener);
        p1.add(bSelectAll);

        bExport.addActionListener(cisSNP2Listener);
        p1.add(bExport);
        
        p1.add(Box.createHorizontalGlue());
        this.setLinkToPanel(p1, assemblyId, snp.getChromosome(), snp.getPosition(), distanceToSNP);
        
        p1.setBaseSpringBox();
        panel.add(p1);
        // ------------------ Panel p1 (end) ------------------ //
        
        // ------------------ Panel p2 (start) ------------------ //
        //BaseJTable transcriptTable = this.transcriptTable.getSortedTable();   //TODO
        if (modificationTable.isEnabled()) {
            bSelectAll.setEnabled(false);
            bExport.setEnabled(false);
        }
        this.setTranscriptTable();
        
        BaseJPane p2 = new BaseJPane();
        p2.setLayout(new BorderLayout());
        p2.add(modificationTable.getTableHeader(), BorderLayout.PAGE_START);
        p2.add(modificationTable, BorderLayout.CENTER);
        
        //p0.setBaseSpringGrid(1, 1);
        panel.add(p2);
        // ------------------ Panel p2 (end) ------------------ //
        
        panel.add(new JLabel(" (Use CTRL+C to copy CpG probes of interest)"));
        
        panel.setBaseSpringGrid(1);
        this.add(panel);
    }
    
    private void setTranscriptTable() {
        if (significants != null && significants.size() != 0)   // BUG 20/10/10
            for (int i=0 ; i<significants.size() ; i++) {
                int row = modificationTable.getRowCount();
                
                for (int j=0 ; j<row ; j++)
                    if (((String) modificationTable.getValueAt(j, 1)).equals(significants.get(i)))
                        modificationTable.setValueAt(true, j, 0);
            }
    }
    
    public void setParameterPanel(int statisticId, Vector<Algorithm> algorithms) {
        BaseJPane panel = new BaseJPane("Analysis Parameters",10,20,10,20);
        
        // ------------------ Panel p0 (start) ----------------- */
        BaseJPane p0 = new BaseJPane();

        p0.add(new JLabel("1. Correlation and regression: "));
        cbStatistic = new JComboBox(Message.getStatistics(this.ui.getAddress()));

        if (!Statistic.isExternalAlgorithm(statisticId)) {
            this.setNullAlgorithms(Statistic.getStatisticIndex(statisticId));
            cbStatistic.removeItemAt(2);
        } else
            this.setAlgorithms(algorithms);
        cbStatistic.addActionListener(this);
        p0.add(cbStatistic);
        p0.add(cbAlgorithm);
        
        p0.add(new JLabel(" "));
        
        p0.setBaseSpringBoxTrailing();
        panel.add(p0);
        // ------------------ Panel p0 (end) ----------------- */
        
        panel.add(new JLabel(""));
        
        // ------------------ Panel p1 (start) ----------------- */
        BaseJPane p1 = new BaseJPane();

        p1.add(new JLabel("2. Number of permutations: "));
        cbPermutation = new JComboBox(permutations);
        cbPermutation.setSelectedIndex(1);
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
            this.cisSNPPane2ActionPerformed(ae);
            
        } catch (Exception e) {   //ADD 19/11/10
            this.showConnectionErrorMessage(e);
        }
    }
    
    private void cisSNPPane2ActionPerformed(ActionEvent e) {
        if (e.getSource() == bRemove)
            this.ui.removeCurrentNode();

        else if (e.getSource() == bSubmit)
            new CisSNPPane2ProgressBar().execute();
    }
    
    /**
     * @author Tsun-Po Yang <tpy@sanger.ac.uk>
     * @link   http://www.sanger.ac.uk/resources/software/genevar/
     */
    private class CisSNPPane2Listener implements ActionListener
    {
        //private UI ui;   //BUG 07/02/11
        private JButton bSelectAll, bExport;
        private boolean isSelectAll = false;

        public CisSNPPane2Listener(JButton bSelectAll, JButton bExport) {
            //this.ui = ui;
            this.bSelectAll = bSelectAll;
            this.bExport = bExport;
        }
        
        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == bSelectAll) {
                int row = modificationTable.getRowCount();

                for (int i=0 ; i<row ; i++)
                    modificationTable.setValueAt(this.isSelectAll, i, 0);
                
                if (this.isSelectAll) {
                    this.bSelectAll.setText(" Deselect All ");
                    this.isSelectAll = false;

                } else {
                    this.bSelectAll.setText("   Select All   ");
                    this.isSelectAll = true;
                }
                
            } else if (ae.getSource() == bExport) {
                ui.showFileChooserSaveDialog(ui, fileName + ".txt");
                int col = modificationTable.getColumnCount();
                int row = modificationTable.getRowCount();
                
                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter(ui.getFileChooserSelectedFile()));

                    for (int i=1 ; i<col-1 ; i++)
                        out.write((String) modificationTable.getColumnName(i) + "\t");
                    out.write((String) modificationTable.getColumnName(col-1) + "\n");
                    
                    for (int j=0 ; j<row ; j++)
                        if ((Boolean) modificationTable.getValueAt(j, 0)) {
                            out.write(modificationTable.getValueAt(j, 1) + "\t");
                            out.write(modificationTable.getValueAt(j, 2) + "\t");
                            out.write(modificationTable.getValueAt(j, 3) + "\t");
                            out.write(modificationTable.getValueAt(j, 4) + "\t");
                            out.write(modificationTable.getValueAt(j, 5) + "\t");
                            out.write(modificationTable.getValueAt(j, 6) + "\t");
                            out.write(modificationTable.getValueAt(j, 7) + "\t");
                            out.write(modificationTable.getValueAt(j, 8) + "\n");
                        }
                    out.close();
                    
                } catch (IOException ex) {}
            }
        }
    }
    
    /**
     * @author Tsun-Po Yang <tpy@sanger.ac.uk>
     * @link   http://www.sanger.ac.uk/Teams/Team16/
     */
    private class CisSNPPane2ProgressBar extends SwingWorker<Object, Object>
    {   
        private BaseProgressBar progressBar = new BaseProgressBar(ui);

        @Override
        protected Void doInBackground() {
            //int statistic = cbStatistic.getSelectedIndex();   //BUG FIED 016/06/10
            Statistic statistic = (Statistic) cbStatistic.getSelectedItem();
            int permutation = 0;
            if (cbPermutation.getSelectedIndex() != 0)
                permutation = new Integer(((String) cbPermutation.getSelectedItem()).replaceAll(",",""));

            try {
                for (int i=0 ; i<modificationTable.getRowCount() ; i++)
                    if (((Boolean) modificationTable.getValueAt(i, 0)).booleanValue()) {
                    	ModificationMapping modificationMapping = (ModificationMapping) modificationTable.getValueAt(i, 9);   //TODO
                        String probeId = modificationMapping.getProbeId();
                        String cpgIslands = modificationMapping.getCpgIslands();
                        String geneSymbol = modificationMapping.getGeneSymbol();
                        int modificationId = modificationMapping.getModificationId();
                        
                        String title = snp.getName() + " / " + probeId + " / " + cpgIslands + " / " + geneSymbol;
                        String description = probeId;

                        description += " (" + statistic.abbreviation;   //TODO
                        if (permutation != 0)
                            description += ", FDR";
                        description += ")";
                            
                        ui.addToCurrentNode(description, new EQTLQueryPane3(ui, title, matchedFeatureTable, snp, modificationId, statistic.getId(), permutation));
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
