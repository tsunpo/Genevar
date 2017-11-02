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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;

import sanger.team16.common.hbm.Study;
import sanger.team16.common.hbm.Population;
import sanger.team16.common.hbm.GenotypeFeature;
import sanger.team16.common.hbm.Method;
import sanger.team16.common.hbm.dao.GenotypeFeatureDAO;
import sanger.team16.common.io.GenotypeLoader;
import sanger.team16.gui.genevar.AbstractManagerPane;
import sanger.team16.gui.genevar.Message;
import sanger.team16.gui.genevar.UI;
import sanger.team16.gui.jface.BaseJPane;
import sanger.team16.gui.jface.BaseJTextField;
import sanger.team16.gui.jface.BaseProgressBar;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class GenotypeLoadPane extends AbstractManagerPane implements ActionListener
{
    private JButton bBIM, bPED, bTeam16;
    private JComboBox cbStudy, cbPopulation, cbAssembly, cbMethod, cbFormat;
    private BaseJTextField tfBIM, tfPED, tfTeam16 = new BaseJTextField(15, true, true);
    private JSpinner chrSpinner, posSpinner, snpIdSpinner, allelesSpinner, individualSpinner;
    private JCheckBox ckAppend;
    
    public GenotypeLoadPane(UI ui) {
        super(ui);

        this.init();
        this.refresh(0, 0, 0, 0, 0, false);
    }
   
    public void refresh(int studyIndex, int populaitonIndex, int referenceIndex, int methodIndex, int formatIndex, boolean partOf) {
        this.removeAll();
        
        this.setUploadToGenotypePanel(studyIndex, populaitonIndex, referenceIndex, methodIndex, formatIndex, partOf);
        this.setBlankPanel();
        if (this.ui.isServices())
            this.setResetSubmitPanel(this, " Proceed (Disabled in Services Mode) ");
        else
            this.setResetSubmitPanel(this, "     Proceed     ");
    }

    private void setUploadToGenotypePanel(int studyIndex, int populaitonIndex, int referenceIndex, int methodIndex, int formatIndex, boolean appended) {
        BaseJPane panel = new BaseJPane("Load Genotypic Data into Local Database");

        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();
        p0.add(new JLabel("1. Select a study: "));
        
        cbStudy = new JComboBox(this.ui.getStudies());
        cbStudy.setSelectedIndex(studyIndex);
        cbStudy.addActionListener(this);
        p0.add(cbStudy);
        
        p0.setBaseSpringBoxTrailing();
        panel.add(p0);
        // ------------------ Panel p0 (end) ------------------ //
        
        panel.add(new JLabel(""));
        
        // ------------------ Panel p1 (start) ------------------ //
        BaseJPane p1 = new BaseJPane();
        p1.add(new JLabel("2. Specify the file: "));
        
        //cbPopulation = new JComboBox(this.ui.primaryKeyManager.populations);
        cbPopulation = new JComboBox(studyPopulations);
        cbPopulation.setSelectedIndex(populaitonIndex);
        p1.add(cbPopulation);
        p1.add(new JLabel("/"));

        cbAssembly = new JComboBox(studyAssemblies);
        cbAssembly.setSelectedIndex(referenceIndex);
        p1.add(cbAssembly);
        p1.add(new JLabel("/"));
        
        cbMethod = new JComboBox(this.ui.getMethods("G"));
        cbMethod.setSelectedIndex(methodIndex);
        p1.add(cbMethod);
        
        p1.setBaseSpringBox();
        panel.add(p1);
        // ------------------ Panel p1 (end) ------------------ //
        
        panel.add(new JLabel(""));
        
        // ------------------ Panel p2 (start) ------------------ //
        BaseJPane p2 = new BaseJPane();
        p2.add(new JLabel("3. File formats: "));
        
        String[] formats = {"PLINK files", "Team16 merged file"};
        cbFormat = new JComboBox(formats);
        cbFormat.setSelectedIndex(formatIndex);
        cbFormat.addActionListener(this);
        p2.add(cbFormat);

        p2.setBaseSpringBoxTrailing();
        panel.add(p2);
        // ------------------ Panel p2 (end) ------------------ //
        
        panel.add(new JLabel(""));
        
        // ------------------ Panel p3 (start) ------------------ //
        BaseJPane p3 = new BaseJPane();
        
        if (formatIndex == 0)
            p3.add(this.setPLINKFormatPanel(appended));
        else if (formatIndex == 1)
            p3.add(this.setTeam16FormatPanel());
        
        p3.setBaseSpringBox();
        panel.add(p3);
        // ------------------ Panel p3 (end) ------------------ //

        panel.setBaseSpringGrid(1);
        this.add(panel);
    }
    
    private BaseJPane setPLINKFormatPanel(boolean appended) {
        BaseJPane panel = new BaseJPane();
        
        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();
        p0.add(new JLabel("    BIM file: "));
        
        tfBIM = new BaseJTextField(15, true, true);
        p0.add(tfBIM);        
        
        bBIM = new JButton("Browse...");
        bBIM.addActionListener(this);
        p0.add(bBIM);
        
        p0.setBaseSpringBox();
        panel.add(p0);
        // ------------------ Panel p0 (end) ------------------ //
           
        panel.add(new JLabel(""));
        
        // ------------------ Panel p1 (start) ------------------ //
        BaseJPane p1 = new BaseJPane();
        p1.add(new JLabel("    PED file: "));
        
        tfPED = new BaseJTextField(15, true, true);
        p1.add(tfPED);        
        
        bPED = new JButton("Browse...");
        bPED.addActionListener(this);
        p1.add(bPED);
        
        p1.setBaseSpringBox();
        panel.add(p1);
        // ------------------ Panel p1 (end) ------------------ //
        
        // ------------------ Panel p4 (start) ------------------ //
        BaseJPane p4 = new BaseJPane();
        p4.add(Box.createHorizontalGlue());

        ckAppend = new JCheckBox();
        ckAppend.setBackground(Color.WHITE);
        ckAppend.setSelected(appended);
        p4.add(ckAppend);
        p4.add(new JLabel("Append files to existing entry (if divided by chromosomes)"));

        p4.setBaseSpringBox();
        panel.add(p4);
        // ------------------ Panel p4 (end) ------------------ //  
  
        panel.setBaseSpringGrid(1);
        return panel;
    }
    
    private BaseJPane setTeam16FormatPanel() {
        BaseJPane panel = new BaseJPane();
        
        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();
        p0.add(new JLabel("    Genotype file: "));
        
        tfTeam16 = new BaseJTextField(15, true, true);
        p0.add(tfTeam16);        
        
        bTeam16 = new JButton("Browse...");
        bTeam16.addActionListener(this);
        p0.add(bTeam16);
        
        p0.setBaseSpringBox();
        panel.add(p0);
        // ------------------ Panel p0 (end) ------------------ //

        panel.add(new JLabel(""));
        
        // ------------------ Panel p3 (start) ------------------ //
        BaseJPane p3 = new BaseJPane();
        p3.add(new JLabel("     "));
        p3.add(Box.createHorizontalGlue());

        p3.add(new JLabel(" Chromosome column:"));
        chrSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
        p3.add(chrSpinner);
        
        p3.add(new JLabel(" Position:"));
        posSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 8, 1));
        p3.add(posSpinner);
        
        p3.add(new JLabel(" SNP ID:"));
        snpIdSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 8, 1));
        p3.add(snpIdSpinner);

        p3.add(new JLabel(" Alleles:"));
        allelesSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 8, 1));
        p3.add(allelesSpinner);
        
        p3.add(new JLabel(" Individual IDs start from column:"));
        individualSpinner = new JSpinner(new SpinnerNumberModel(5, 2, 8, 1));
        p3.add(individualSpinner);
 
        p3.setBaseSpringBox();
        panel.add(p3);
        // ------------------ Panel p3 (end) ------------------ //
        
        panel.setBaseSpringGrid(1);
        return panel;
    }
    
    public void actionPerformed(ActionEvent ae) {
        try {
            this.loadPaneActionPerformed(ae);
        
        } catch (Exception e) {   //ADD 19/11/10
            if (e.getCause() instanceof java.lang.ArrayIndexOutOfBoundsException)   //ADD 15/08/11
                JOptionPane.showMessageDialog(this, "File not found exception",
                    "Input Failure", JOptionPane.ERROR_MESSAGE);
            else
                showConnectionErrorMessage(e);
        }
    }
    
    public void loadPaneActionPerformed(ActionEvent e) throws Exception {
        this.ui.resetFileChooser(); 

        if (e.getSource() == cbStudy) {
            this.init();
            
            if (cbStudy.getSelectedIndex() != 0) {
                Study study = (Study) cbStudy.getSelectedItem();
                studyPopulations.addAll(this.getPopulationsWhereStudyId(study.getId()));
                studyAssemblies.clear();
                studyAssemblies.add(this.ui.getAssembly(study.getGenotypeAssemblyId()));
                
                this.refresh(cbStudy.getSelectedIndex(), 0, cbAssembly.getSelectedIndex(), cbMethod.getSelectedIndex(), cbFormat.getSelectedIndex(), false);
            } else                                   // ^^^ ADD 13/10/10
                this.refresh(0, 0, 0, 0, 0, false);
    
        } else if (e.getSource() == cbFormat) {
            this.refresh(cbStudy.getSelectedIndex(), cbPopulation.getSelectedIndex(), cbAssembly.getSelectedIndex(), cbMethod.getSelectedIndex(), cbFormat.getSelectedIndex(), false);
            
        } else if (e.getSource() == bBIM || e.getSource() == bPED || e.getSource() == bTeam16) {
            if (e.getSource() == bBIM) {
                this.ui.addChoosableFileFilter(Message.BIM);
                if (this.ui.showFileChooserOpenDialog(this.ui, null)) {
                    File bim = this.ui.getFileChooserSelectedFile();
                    tfBIM.setText(bim.getAbsolutePath());
                    tfPED.setText(bim.getAbsolutePath().replaceFirst(".bim", ".ped"));
                }
                
            } else if (e.getSource() == bPED) {
                this.ui.addChoosableFileFilter(Message.PED);
                if (this.ui.showFileChooserOpenDialog(this.ui, null)) {
                    File ped = this.ui.getFileChooserSelectedFile();
                    tfPED.setText(ped.getAbsolutePath());
                    tfBIM.setText(ped.getAbsolutePath().replaceFirst(".ped", ".bim"));
                }

            } else if (e.getSource() == bTeam16) {
                this.ui.addChoosableFileFilter(Message.TXT);
                if (this.ui.showFileChooserOpenDialog(this.ui, null)) {
                    File team16 = this.ui.getFileChooserSelectedFile();
                    tfTeam16.setText(team16.getAbsolutePath());
                }
            }
            
        } else if (e.getSource() == bReset) {
            this.setReset();
            
        } else if (e.getSource() == bSubmit)
            new GenotypeLoadPaneProgressBar().execute();

        this.setSubmit();
    }
    
    private void setReset() {
        cbStudy.setSelectedIndex(0);    
        cbPopulation.setSelectedIndex(0);
        cbAssembly.setSelectedIndex(0);
        cbMethod.setSelectedIndex(0);
        cbFormat.setSelectedIndex(0);
        tfBIM.setText("");
        tfPED.setText("");
        tfTeam16.setText("");
        if (chrSpinner != null) chrSpinner.setValue(1);
        if (posSpinner != null) posSpinner.setValue(2);
        if (snpIdSpinner!= null) snpIdSpinner.setValue(3);
        if (allelesSpinner != null) allelesSpinner.setValue(4);
        if (individualSpinner != null) individualSpinner.setValue(5);
        ckAppend.setSelected(false);
    }
    
    private void setSubmit() {
        if (((!tfBIM.getText().equals("") && !tfPED.getText().equals("")) || !tfTeam16.getText().equals("")) &&
            cbPopulation.getSelectedIndex() != 0 && cbMethod.getSelectedIndex() != 0)
            bSubmit.setEnabled(true);
        else
            bSubmit.setEnabled(false);
        
        if (this.ui.isServices())
            this.setSubmitEnabled(false);
    }
    
    /**
     * @author Tsun-Po Yang <tpy@sanger.ac.uk>
     * @link   http://www.sanger.ac.uk/Teams/Team16/
     */
    private class GenotypeLoadPaneProgressBar extends SwingWorker<Object, Object>
    {   
        private BaseProgressBar progressBar;
        private int studyId, assemblyId, populationId, methodId;
        private String description;
        private int variation;
        private int individual;
        
        @Override
        protected Void doInBackground() {
        	Study study = (Study) cbStudy.getSelectedItem();
        	studyId = study.getId();
            assemblyId = study.getGenotypeAssemblyId();
            populationId = ((Population) cbPopulation.getSelectedItem()).getId();
            methodId = ((Method) cbMethod.getSelectedItem()).getId();
            description = cbPopulation.getSelectedItem().toString() + " / " + cbAssembly.getSelectedItem().toString() + " / " + cbMethod.getSelectedItem().toString();
            boolean appended = ckAppend.isSelected();
            int formatIndex = cbFormat.getSelectedIndex();
            
            try {
                List<Integer> genotypeFeatureIds = getGenotypeFeatureIds(populationId, assemblyId, methodId);
                
                Object[] options = {"  Yes  ", "   No   "};
                if (genotypeFeatureIds.size() == 0) {   // New GenotypeFeature!!
                    int answer = JOptionPane.showOptionDialog(null, "Upload \'" + description + "\' to the database?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                    if (answer == JOptionPane.YES_OPTION)
                        if (formatIndex == 0)
                            this.loadPLINK(genotypeFeatureIds);
                        else if (formatIndex == 1)
                            this.loadTeam16(genotypeFeatureIds);
                    
                } else {                                // GenotypeFeature already exists!!
                    if (appended) {
                        int answer = JOptionPane.showOptionDialog(null, "Append to '" + description + "\'?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                        if (answer == JOptionPane.YES_OPTION)
                            if (formatIndex == 0)
                                this.appendPLINK(genotypeFeatureIds.get(genotypeFeatureIds.size() - 1));
                            //else if (formatIndex == 1)   //TODO
                                //this.appendTeam16(populationGenotypeIds.get(size));
                    } else {
                        int answer = JOptionPane.showOptionDialog(null, "'" + description + "\' already exists\nWould you like to re-submit again?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                        if (answer == JOptionPane.YES_OPTION)
                            if (formatIndex == 0)
                                this.loadPLINK(genotypeFeatureIds);
                            else if (formatIndex == 1)
                                this.loadTeam16(genotypeFeatureIds);   
                    }
                }
                
            } catch (IOException ioe) {
                showMessageDialogError("File or directory does not exist");
                
            } catch (NumberFormatException ne) {
                showMessageDialogError("Wrong file format");
            }
            
            return null;
        }
        
        private void loadPLINK(List<Integer> genotypeFeatureIds) throws IOException, NumberFormatException {
            progressBar = new BaseProgressBar(ui, "Teatime?");   //ADD 18/02/10

            // 1. Create a new Analysis
            int newGenotypeFeatureId = new GenotypeFeatureDAO(ui.getAddress()).save(new GenotypeFeature(populationId, assemblyId, methodId, description, "tpy"), true);

            // 2. Load BIM and PED 
            GenotypeLoader genotypeLoader = new GenotypeLoader(ui.getAddress(), studyId, newGenotypeFeatureId, populationId);
            genotypeLoader.loadBIM(tfBIM.getText());
            genotypeLoader.loadPED(tfPED.getText());
            
            variation = genotypeLoader.variationIds.size();
            individual = genotypeLoader.individualIds.size();
            
            // 3. Update the log part of Analysis
            new GenotypeFeatureDAO(ui.getAddress()).updateGenotypeFeature(newGenotypeFeatureId, tfPED.getText(), individual, variation);
            if (genotypeFeatureIds != null)
                new GenotypeFeatureDAO(ui.getAddress()).updateParentFeatureId(genotypeFeatureIds);
        }
        
        private void appendPLINK(int genotypeFeatureId) throws IOException, NumberFormatException {
            progressBar = new BaseProgressBar(ui, "Teatime?");   //ADD 18/02/10

            // 1. Create a new Analysis
            int newGenotypeFeatureId = new GenotypeFeatureDAO(ui.getAddress()).save(new GenotypeFeature(populationId, assemblyId, methodId, description, "tpy"), true);
            
            // 2. Load BIM and PED 
            //GenotypeLoader genotypeLoader = new GenotypeLoader(ui.getAddress(), study, genotypeFeatureId, populationId);   //NOTE: not newPopulationGenotypeId
            GenotypeLoader genotypeLoader = new GenotypeLoader(ui.getAddress(), studyId, newGenotypeFeatureId, populationId);
            genotypeLoader.loadBIM(tfBIM.getText());                                   // CHANGE TO newGenotypeFeatureId 28/11/11
            genotypeLoader.loadPED(tfPED.getText());
            
            variation = genotypeLoader.variationIds.size();
            individual = genotypeLoader.individualIds.size();
            
            // 3. Update the log part of Analysis
            new GenotypeFeatureDAO(ui.getAddress()).updateGenotypeFeature(newGenotypeFeatureId, tfPED.getText(), individual, variation, genotypeFeatureId);
        }

        private void loadTeam16(List<Integer> genotypeFeatureIds) throws IOException, NumberFormatException {
            progressBar = new BaseProgressBar(ui, "Teatime?");   //ADD 18/02/10
            int chrColumn = (Integer) chrSpinner.getValue() - 1;
            int posColumn = (Integer) posSpinner.getValue() - 1;
            int snpIdColumn = (Integer) snpIdSpinner.getValue() - 1;
            int allelesColumn = (Integer) allelesSpinner.getValue() - 1;
            int individualColumn = (Integer) individualSpinner.getValue() - 1;
            
            // 1. Create a new Analysis
            int newGenotypeFeatureId = new GenotypeFeatureDAO(ui.getAddress()).save(new GenotypeFeature(populationId, assemblyId, methodId, description, "tpy"), true);

            // 2. Load Team16 Format
            GenotypeLoader genotypeLoader = new GenotypeLoader(ui.getAddress(), studyId, newGenotypeFeatureId, populationId);
            genotypeLoader.loadTeam16(tfTeam16.getText(), chrColumn, posColumn, snpIdColumn, allelesColumn, individualColumn);
            
            variation = genotypeLoader.variationIds.size();
            individual = genotypeLoader.individualIds.size();
            
            // 3. Update the log part of Analysis
            new GenotypeFeatureDAO(ui.getAddress()).updateGenotypeFeature(newGenotypeFeatureId, tfTeam16.getText(), individual, variation);
            if (genotypeFeatureIds != null)
                new GenotypeFeatureDAO(ui.getAddress()).updateParentFeatureId(genotypeFeatureIds);
        }
        
        @Override
        protected void done() {
            progressBar.dispose(ui);
            tfBIM.setText("");
            tfPED.setText("");
            tfTeam16.setText("");
            ui.moveToParentNode();
            
            String upload = individual + " individuals and " + variation + " variations uploaded";
            JOptionPane.showMessageDialog(ui, upload,
                "Uploded", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
