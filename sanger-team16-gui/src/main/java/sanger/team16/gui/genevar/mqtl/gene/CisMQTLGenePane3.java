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
package sanger.team16.gui.genevar.mqtl.gene;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingWorker;

import sanger.team16.common.business.dao.MatchedFeature;
import sanger.team16.common.business.dao.Statistic;
import sanger.team16.common.hbm.Algorithm;
import sanger.team16.common.hbm.ModificationMapping;
import sanger.team16.common.hbm.Study;
import sanger.team16.common.hbm.Variation;
import sanger.team16.common.io.BigMath;
import sanger.team16.common.xqtl.QTL;
import sanger.team16.common.xqtl.QTLList;
import sanger.team16.gui.genevar.Message;
import sanger.team16.gui.genevar.TestTableModel;
import sanger.team16.gui.genevar.UI;
import sanger.team16.gui.genevar.eqtl.query.EQTLQueryPane3;
import sanger.team16.gui.genevar.mqtl.MQTLAnalysisPane;
import sanger.team16.gui.jface.BaseJPane;
import sanger.team16.gui.jface.BaseProgressBar;
import sanger.team16.gui.jface.table.BaseJTable;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class CisMQTLGenePane3 extends MQTLAnalysisPane implements ActionListener
{   
    private Study study;
    private JComboBox cbPermutation;
    //private int genotypeFeatureId;   //REMOVED 08/12/11
    //private int expressionFeatureId;v   //REMOVED 08/12/11
    private MatchedFeature matchedFeature;
    private List<ModificationMapping> modificationMappings;
    private List<List<QTL>> qtlList;
    private List<JTable> testTables;
    private String geneChromosome;
    private int probeStart;
    private int distanceToCpG;
    private double threshold;
    
    public CisMQTLGenePane3(UI ui, String fileName, BaseJTable modificationTable, Study study, MatchedFeature matchedFeature, int statisticId, Vector<Algorithm> algorithms, int distanceToCpG, double threshold) {
        super(ui);
        this.study = study;
        this.qtlList = new ArrayList<List<QTL>>();
        this.testTables = new ArrayList<JTable>();
        this.matchedFeature = matchedFeature;
        this.modificationMappings = new ArrayList<ModificationMapping>();
   
        for (int i=0 ; i<modificationTable.getRowCount() ; i++) {
            if (((Boolean) modificationTable.getValueAt(i, 0)).booleanValue()) {
            	ModificationMapping modificationMapping = (ModificationMapping) modificationTable.getValueAt(i, 6);   //TODO
                this.modificationMappings.add(modificationMapping);
                
                int modificationId = modificationMapping.getModificationId();
                this.geneChromosome = modificationMapping.getChromosome();
                this.probeStart = modificationMapping.getProbeStart();
                this.distanceToCpG = distanceToCpG;
                this.threshold = threshold;

                QTLList qtls = this.getMQTLsWhereCpG(study.getId(), matchedFeature, modificationId, geneChromosome, probeStart, statisticId, threshold, distanceToCpG);
                this.qtlList.add(qtls.significances);
                
                this.setTestPanel(qtls.significances, qtls.insignificances, statisticId, fileName, modificationMapping);
                this.setBlankPanel();
            }
        }
        this.setParameterPanel(statisticId, algorithms);
        this.setBlankPanel();
        this.setRemoveSubmitPanel(this, "  SNP-Gene Association Plot  ");
        
        //TODO
        if (Statistic.isExternalAlgorithm(statisticId)) {
            cbStatistic.setEnabled(false);
            cbAlgorithm.setEnabled(false);
            cbPermutation.setEnabled(false);
            bSubmit.setEnabled(false);
        }
    }
    
    private void setTestPanel(List<QTL> significances, List<QTL> insignificances, int statisticId, String fileName, ModificationMapping modificationMapping) throws ArrayIndexOutOfBoundsException {
        String probeId = modificationMapping.getProbeId();
        String cpgIslands = modificationMapping.getCpgIslands();
        String geneSymbol = modificationMapping.getGeneSymbol();
        String title = probeId + " / " + cpgIslands + " / " + geneSymbol;
        JTable testTable = this.getTestTable(statisticId, significances);

        BaseJPane panel = new BaseJPane(title,10,20,10,20);
        panel.add(new RegionalPlot(geneChromosome, probeStart, distanceToCpG, threshold, significances, insignificances));
        
        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();
        JButton bSelectAll = new JButton(" Deselect All ");
        JButton bExport = new JButton("  Export List  ");
        CisGenePane3Listener cisGene3Listener = new CisGenePane3Listener(fileName, probeId, cpgIslands, geneSymbol, bSelectAll, bExport, testTable, statisticId);

        //bSelectAll.setForeground(new Color(34,139,34));
        bSelectAll.setForeground(Color.GREEN.darker());
        bSelectAll.addActionListener(cisGene3Listener);
        p0.add(bSelectAll);

        bExport.addActionListener(cisGene3Listener);
        p0.add(bExport);
        
        p0.add(Box.createHorizontalGlue());
        this.setLinkToPanel(p0, study.getGenotypeAssemblyId(), geneChromosome, probeStart, distanceToCpG);
        
        p0.setBaseSpringBox();
        panel.add(p0);
        // ------------------ Panel p0 (end) ------------------ //
        
        if (((String) testTable.getValueAt(0, 1)).equals("")) {
            testTable.setBackground(new Color(220, 220, 220));
            testTable.setEnabled(false);
            bSelectAll.setEnabled(false);
            bExport.setEnabled(false);
        }

        // ------------------ Panel p1 (start) ------------------ //        
        BaseJPane p1 = new BaseJPane();
        p1.setLayout(new BorderLayout());
        p1.add(testTable.getTableHeader(), BorderLayout.PAGE_START);
        p1.add(testTable, BorderLayout.CENTER);
        
        //p0.setBaseSpringGrid(1, 1);
        panel.add(p1);
        // ------------------ Panel p1 (end) ------------------ //

        panel.add(new JLabel(" (Use CTRL+C to copy SNPs of interest)"));
        
        panel.setBaseSpringGrid(1);
        this.add(panel);
    }
    
    private JTable getTestTable(int statisticId, List<QTL> eqtls) throws ArrayIndexOutOfBoundsException {
        JTable testTable = null;

        if (statisticId == Statistic.SPEARMANS) {
            testTable = new JTable(new TestTableModel(statisticId, this.getSpearmansTests(eqtls)));
            
            testTable.getColumnModel().getColumn(6).setPreferredWidth(30);
            testTable.getColumnModel().getColumn(7).setPreferredWidth(70);
            testTable.getColumnModel().getColumn(8).setPreferredWidth(50);
            
        } else if (statisticId == Statistic.LINEAR) {
            testTable = new JTable(new TestTableModel(statisticId, this.getLinearTests(eqtls)));

            testTable.getColumnModel().getColumn(6).setPreferredWidth(30);
            testTable.getColumnModel().getColumn(7).setPreferredWidth(70);
            testTable.getColumnModel().getColumn(8).setPreferredWidth(50);
            testTable.getColumnModel().getColumn(9).setPreferredWidth(40);
            testTable.getColumnModel().getColumn(10).setPreferredWidth(40);
        
        } else if (statisticId == Statistic.EXTERNAL_ALGORITHM_ProbABEL) {
            testTable = new JTable(new TestTableModel(statisticId, this.getProbABELTests(eqtls)));

            testTable.getColumnModel().getColumn(6).setPreferredWidth(10);
            testTable.getColumnModel().getColumn(7).setPreferredWidth(25);
            testTable.getColumnModel().getColumn(8).setPreferredWidth(25);
            testTable.getColumnModel().getColumn(9).setPreferredWidth(25);
            testTable.getColumnModel().getColumn(10).setPreferredWidth(25);
            testTable.getColumnModel().getColumn(11).setPreferredWidth(25);
            testTable.getColumnModel().getColumn(12).setPreferredWidth(25);
            testTable.getColumnModel().getColumn(13).setPreferredWidth(40);
            testTable.getColumnModel().getColumn(14).setPreferredWidth(25);
        }
        testTable.getColumnModel().getColumn(0).setPreferredWidth(10);
        testTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        testTable.getColumnModel().getColumn(2).setPreferredWidth(5);
        testTable.getColumnModel().getColumn(3).setPreferredWidth(40);
        testTable.getColumnModel().getColumn(4).setPreferredWidth(40);
        testTable.getColumnModel().getColumn(5).setPreferredWidth(30);

        testTable.setAutoCreateRowSorter(true);
        testTable.setCellSelectionEnabled(true);
        testTable.getTableHeader().setReorderingAllowed(false);
        testTables.add(testTable);
        return testTable;
    }
    
    private Object[][] getSpearmansTests(List<QTL> eqtls) {
        int size = eqtls.size();
        Object[][] tests = this.initialData(TestTableModel.columnNamesSpearmans.length);
        
        if (size != 0) {
            tests = new Object[size][TestTableModel.columnNamesSpearmans.length];
 
            for (int i=0 ; i<size ; i++) {
                QTL eqtl = eqtls.get(i);
                
                tests[i][0] = true;
                tests[i][1] = eqtl.snp.getName();
                tests[i][2] = this.geneChromosome;
                tests[i][3] = eqtl.snp.getPosition();
                tests[i][4] = this.probeStart;
                tests[i][5] = eqtl.getDistance(this.probeStart);
                tests[i][6] = eqtl.getCorrelation();
                tests[i][7] = eqtl.getNominalP();
                tests[i][8] = eqtl.getMinusLog10P();
                //tests[i][10] = Math.rint(qtl.minusLog10P*10000.0)/10000.0;   // Need to be double to be able to sort!
                //tests[i][10] = df4.format(Math.rint(-Math.log10(qtl.pValue)*10000.0)/10000.0);   // Sting cannot be sorted!
            }
        }
        
        return tests;
    }
    
    private Object[][] getLinearTests(List<QTL> eqtls) {
        int size = eqtls.size();
        Object[][] tests = this.initialData(TestTableModel.columnNamesLinear.length);
        
        if (size != 0) {
            tests = new Object[size][TestTableModel.columnNamesLinear.length];

            for (int i=0 ; i<size ; i++) {
                QTL eqtl = eqtls.get(i);
                
                tests[i][0] = true;
                tests[i][1] = eqtl.snp.getName();
                tests[i][2] = this.geneChromosome;
                tests[i][3] = eqtl.snp.getPosition();
                tests[i][4] = this.probeStart;
                tests[i][5] = eqtl.getDistance(this.probeStart);
                tests[i][6] = eqtl.getCorrelation();
                tests[i][7] = eqtl.getNominalP();
                tests[i][8] = eqtl.getMinusLog10P();
                tests[i][9] = eqtl.adjRSq;
                tests[i][10] = eqtl.slope;
            }
        }

        return tests;
    }
    
    private Object[][] getProbABELTests(List<QTL> eqtls) {
        int size = eqtls.size();
        Object[][] tests = this.initialData(TestTableModel.columnNamesProbABEL.length);
        
        if (size != 0) {
            tests = new Object[size][TestTableModel.columnNamesProbABEL.length];

            for (int i=0 ; i<size ; i++) {
                QTL eqtl = eqtls.get(i);
                
                tests[i][0] = true;
                tests[i][1] = eqtl.snp.getName();
                tests[i][2] = this.geneChromosome;
                tests[i][3] = eqtl.snp.getPosition();
                tests[i][4] = this.probeStart;
                tests[i][5] = eqtl.getDistance(this.probeStart);
                tests[i][6] = eqtl.snp.getAlleles();   //A1
                tests[i][7] = eqtl.snp.getInfo();   //INFO
                tests[i][8] = eqtl.snp.getFreq1();   //Freq1
                tests[i][9] = eqtl.mean_predictor_allele;
                tests[i][10] = eqtl.beta_SNP;
                tests[i][11] = eqtl.sebeta_SNP;
                tests[i][12] = eqtl.chi;
                tests[i][13] = eqtl.getNominalP();
                tests[i][14] = eqtl.getMinusLog10P();
            }
        }

        return tests;
    }
    
    public void setParameterPanel(int statisticId, Vector<Algorithm> algorithms) {
        BaseJPane panel = new BaseJPane("Analysis Parameters",10,20,10,20);
        
        // ------------------ Panel p0 (start) ----------------- */
        BaseJPane p0 = new BaseJPane();

        p0.add(new JLabel("1. Correlation and regression: "));
        cbStatistic = new JComboBox(Message.getStatistics(this.ui.getAddress()));
        
        if (!Statistic.isExternalAlgorithm(statisticId)) {
            this.setNullAlgorithms(Statistic.getStatisticIndex(statisticId));
            cbStatistic.removeItemAt(cbStatistic.getComponentCount());
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
            this.cisGenePane3ActionPerformed(ae);
            
        } catch (Exception e) {   //ADD 19/11/10
            this.showConnectionErrorMessage(e);
        }
    }
        
    public void cisGenePane3ActionPerformed(ActionEvent ae) {   //ADD 19/11/10
        if (ae.getSource() == bRemove)
            this.ui.removeCurrentNode();
        
        else if (ae.getSource() == bSubmit)
            new CisGenePane3ProgressBar().execute();
    }
    
    /**
     * @author Tsun-Po Yang <tpy@sanger.ac.uk>
     * @link   http://www.sanger.ac.uk/Teams/Team16/
     */
    private class CisGenePane3Listener implements ActionListener
    {
        //private UI ui;   //BUG 07/02/11
        private String fileName;
        private String probeId, ensemblGene, geneSymbol;
        private JButton bSelectAll, bExport;
        private boolean isSelectAll = false;
        private JTable testTable;
        private int statisticId;

        public CisGenePane3Listener(String fileName, String probeId, String ensemblGene, String geneSymbol, JButton bSelectAll, JButton bExport, JTable testTable, int statisticId) {
            //this.ui = ui;
            this.fileName = fileName;
            this.probeId = probeId;
            this.ensemblGene = ensemblGene;
            this.geneSymbol = geneSymbol;
            this.bSelectAll = bSelectAll;
            this.bExport = bExport;
            this.testTable = testTable;
            this.statisticId = statisticId;
        }
        
        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == bSelectAll) {
                int row = this.testTable.getRowCount();

                for (int i=0 ; i<row ; i++)
                    this.testTable.setValueAt(this.isSelectAll, i, 0);
                
                if (this.isSelectAll) {
                    this.bSelectAll.setText(" Deselect All ");
                    this.isSelectAll = false;
                    
                } else {
                    this.bSelectAll.setText("   Select All   ");
                    this.isSelectAll = true;
                }
                
            } else if (ae.getSource() == bExport) {
                ui.showFileChooserSaveDialog(ui, fileName + "_" + geneSymbol + "_" + probeId + ".txt");
                int col = this.testTable.getColumnCount();
                int row = this.testTable.getRowCount();
                
                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter(ui.getFileChooserSelectedFile()));

                    out.write((String) this.testTable.getColumnName(1) + "\tProbe Id\tEnsembl Gene\tGene Symbol\t");
                    for (int i=2 ; i<col-1 ; i++)
                        out.write((String) this.testTable.getColumnName(i) + "\t");
                    out.write((String) this.testTable.getColumnName(col-1) + "\n");
                    
                    for (int j=0 ; j<row ; j++) {
                        if ((Boolean) this.testTable.getValueAt(j, 0)) {
                            out.write(this.testTable.getValueAt(j, 1) + "\t");
                            out.write(this.probeId + "\t");
                            out.write(this.ensemblGene + "\t");
                            out.write(this.geneSymbol + "\t");
                            out.write(this.testTable.getValueAt(j, 2) + "\t");
                            out.write(this.testTable.getValueAt(j, 3) + "\t");
                            out.write(this.testTable.getValueAt(j, 4) + "\t");
                            out.write(((Integer) this.testTable.getValueAt(j, 5)).toString() + "\t");
                            
                            if (statisticId == Statistic.SPEARMANS) {
                                out.write(new Double(BigMath.round(((Double) this.testTable.getValueAt(j, 6)), 4)).toString() + "\t");
                                out.write(this.testTable.getValueAt(j, 7) + "\t");
                                out.write(new Double(BigMath.round(((Double) this.testTable.getValueAt(j, 8)), 4)).toString() + "\n");

                            } else if (statisticId == Statistic.LINEAR) {
                                out.write(new Double(BigMath.round(((Double) this.testTable.getValueAt(j, 6)), 4)).toString() + "\t");
                                out.write(this.testTable.getValueAt(j, 7) + "\t");
                                out.write(new Double(BigMath.round(((Double) this.testTable.getValueAt(j, 8)), 4)).toString() + "\t");
                                out.write(new Double(BigMath.round(((Double) this.testTable.getValueAt(j, 9)), 4)).toString() + "\t");
                                out.write(new Double(BigMath.round(((Double) this.testTable.getValueAt(j, 10)), 4)).toString() + "\n");               
                            
                            } else if (statisticId == Statistic.EXTERNAL_ALGORITHM_ProbABEL) {
                                out.write(this.testTable.getValueAt(j, 6)+ "\t");
                                out.write(new Double(BigMath.round(((Double) this.testTable.getValueAt(j, 7)), 4)).toString() + "\t");
                                out.write(new Double(BigMath.round(((Double) this.testTable.getValueAt(j, 8)), 4)).toString() + "\t");
                                out.write(new Double(BigMath.round(((Double) this.testTable.getValueAt(j, 9)), 4)).toString() + "\t");
                                out.write(new Double(BigMath.round(((Double) this.testTable.getValueAt(j, 10)), 4)).toString() + "\t");
                                out.write(new Double(BigMath.round(((Double) this.testTable.getValueAt(j, 11)), 4)).toString() + "\t");
                                out.write(new Double(BigMath.round(((Double) this.testTable.getValueAt(j, 12)), 4)).toString() + "\t");
                                out.write(this.testTable.getValueAt(j, 13) + "\t");
                                out.write(new Double(BigMath.round(((Double) this.testTable.getValueAt(j, 14)), 4)).toString() + "\n");
                            }
                        }
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
    private class CisGenePane3ProgressBar extends SwingWorker<Object, Object>
    {   
        private BaseProgressBar progressBar = new BaseProgressBar(ui);

        @Override
        protected Void doInBackground() {
            //int statistic = cbStatistic.getSelectedIndex();   //BUG FIXED 16/06/10
            Statistic statistic = (Statistic) cbStatistic.getSelectedItem();
            int permutation = 0;
            if (cbPermutation.getSelectedIndex() != 0)
                permutation = new Integer(((String) cbPermutation.getSelectedItem()).replaceAll(",",""));

            try {   //ADD 19/11/10
                for (int i=0 ; i<modificationMappings.size() ; i++) {
                    ModificationMapping modificationMapping = modificationMappings.get(i);
                    String probeId = modificationMapping.getProbeId();
                    String cpgIslands = modificationMapping.getCpgIslands();
                    String geneSymbol = modificationMapping.getGeneSymbol();
                    int modificationId = modificationMapping.getModificationId();
                    JTable testTable = testTables.get(i);   //TODO
                    
                    List<QTL> eqtls = qtlList.get(i);
                    List<Variation> snps = new ArrayList<Variation>();
                    for (int j=0 ; j<testTable.getRowCount() ; j++)
                        if ((Boolean) testTable.getValueAt(j, 0))
                            snps.add(this.getVariation((String) testTable.getValueAt(j, 1), eqtls));
                    
                    if (snps.size() != 0) {
                        String title = probeId + " / " + cpgIslands + " / " + geneSymbol;
                        String description = probeId;
                        
                        description += " (" + statistic.abbreviation;   //TODO
                        if (permutation != 0)
                            description += ", FDR";
                        description += ")";
                            
                        ui.addToCurrentNode(description, new EQTLQueryPane3(ui, title, matchedFeature, snps, modificationId, statistic.getId(), permutation));   
                    }
                }
                
            } catch (Exception e) {   //ADD 19/11/10
                showConnectionErrorMessage(e);
            }
            
            return null;
        }
        
        private Variation getVariation(String variationName, List<QTL> eqtls) {
            for (int i=0 ; i<eqtls.size() ; i++) {
                QTL eqtl = eqtls.get(i);
                if (eqtl.snp.getName() == variationName)
                    return eqtl.snp;
            }
            
            return null;
        }
        
        @Override
        protected void done() {
            progressBar.dispose(ui);
        }
    }
}
