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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;

import sanger.team16.common.hbm.MethylationFeature;
import sanger.team16.common.hbm.Study;
import sanger.team16.common.hbm.Platform;
import sanger.team16.common.hbm.Population;
import sanger.team16.common.hbm.TissueType;
import sanger.team16.common.hbm.Method;
import sanger.team16.common.hbm.dao.MethylationFeatureDAO;
import sanger.team16.common.io.MethylationLoader;
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
public class MethylationLoadPane extends AbstractManagerPane implements ActionListener
{
    private JComboBox cbStudy, cbPopulation, cbPlatform, cbTissueType, cbMethod;
    private BaseJTextField tfFile;
    private JButton bFile;
    private JSpinner probeIdSpinner, individualSpinner;

    public MethylationLoadPane(UI ui) {
        super(ui);
        
        this.init();
        this.refresh(0);
    }
    
    public void refresh(int studyIndex) {
        this.removeAll();
        
        this.setUploadToAnalysisPanel(studyIndex);
        this.setBlankPanel();
        if (this.ui.isServices())
            this.setResetSubmitPanel(this, " Proceed (Disabled in Services Mode) ");
        else
            this.setResetSubmitPanel(this, "     Proceed     ");
    }
    
    private void setUploadToAnalysisPanel(int studyIndex) {
        BaseJPane panel = new BaseJPane("Load Methylation Profiling Data into Local Database");
        
        // ================== Panel p0 (start) ================== //
        BaseJPane p0 = new BaseJPane();
        p0.add(new JLabel("1. Select a study: "));
        
        cbStudy = new JComboBox(this.ui.getStudies());
        cbStudy.setSelectedIndex(studyIndex);
        cbStudy.addActionListener(this);
        p0.add(cbStudy);
        
        p0.setBaseSpringBoxTrailing();
        panel.add(p0);
        // ================== Panel p0 (end) ================== //
        
        panel.add(new JLabel(""));
        
        // ================== Panel p1 (start) ================== //
        BaseJPane p1 = new BaseJPane();
        p1.add(new JLabel("2. Specify the file: "));
        
        cbPopulation = new JComboBox(studyPopulations);
        cbPopulation.setSelectedIndex(0);
        p1.add(cbPopulation);
        p1.add(new JLabel("/"));

        cbPlatform = new JComboBox(studyPlatforms);
        cbPlatform.setSelectedIndex(0);
        p1.add(cbPlatform);
        p1.add(new JLabel("/"));
        
        cbTissueType = new JComboBox(this.ui.getTissueTypes());
        cbTissueType.setSelectedIndex(0);
        p1.add(cbTissueType);
        p1.add(new JLabel("/"));
        
        cbMethod = new JComboBox(this.ui.getMethods("M"));
        cbMethod.setSelectedIndex(0);
        p1.add(cbMethod);
        
        p1.setBaseSpringBox();
        panel.add(p1);
        // ================== Panel p1 (end) ================== //
        
        panel.add(new JLabel(""));
        
        // ================== Panel p2 (start) ================== //
        BaseJPane p2 = new BaseJPane();
        p2.add(new JLabel("3. Methylation profiling file: "));
        
        tfFile = new BaseJTextField(15, true, true);
        p2.add(tfFile);
        
        bFile = new JButton("Browse...");
        bFile.addActionListener(this);
        p2.add(bFile);

        p2.setBaseSpringBox();
        panel.add(p2);
        // ================== Panel p2 (end) ================== //

        panel.add(new JLabel(""));
        
        // ================== Panel p3 (start) ================== //
        BaseJPane p3 = new BaseJPane();
        p3.add(new JLabel("                                                            "));
        p3.add(Box.createHorizontalGlue());

        p3.add(new JLabel("Probe ID column:"));
        probeIdSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        p3.add(probeIdSpinner);
        
        p3.add(new JLabel(" Individual IDs start from column:"));
        individualSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 5, 1));
        p3.add(individualSpinner);
 
        p3.setBaseSpringBox();
        panel.add(p3);
        // ================== Panel p3 (end) ================== //
        
        panel.setBaseSpringGrid(1);
        this.add(panel);
    }
    
    public void actionPerformed(ActionEvent ae) {
        try {
            this.loadPaneActionPerformed(ae);
            
        } catch (Exception e) {   //ADD 19/11/10
            showConnectionErrorMessage(e);
        }
    }
    
    public void loadPaneActionPerformed(ActionEvent ae) {
        this.ui.resetFileChooser();
        
        if (ae.getSource() == cbStudy) {
            this.init();
            
            if (cbStudy.getSelectedIndex() != 0) {
                Study study = (Study) cbStudy.getSelectedItem();
                studyPopulations.addAll(this.getPopulationsWhereStudyId(study.getId()));
                studyPlatforms.clear();
                studyPlatforms.add(this.ui.getPlatform(study.getMethylationPlatformId()));
                
                this.refresh(cbStudy.getSelectedIndex());
                
            } else
                this.refresh(0);
            
        } else if (ae.getSource() == bFile) {
            this.ui.addChoosableFileFilter(Message.TXT);
            
            if (this.ui.showFileChooserOpenDialog(this.ui, null)) {
                File file = this.ui.getFileChooserSelectedFile();
                tfFile.setText(file.getAbsolutePath());
            }
            
        } else if (ae.getSource() == bReset)
            this.setReset();
            
        else if (ae.getSource() == bSubmit)
            new MethylationLoaderPaneProgressBar().execute();
        
        this.setSubmit();
    }
    
    private void setReset() {
        cbStudy.setSelectedIndex(0);
        cbPopulation.setSelectedIndex(0);
        cbPlatform.setSelectedIndex(0);
        cbTissueType.setSelectedIndex(0);
        cbMethod.setSelectedIndex(0);
        tfFile.setText("");
        probeIdSpinner.setValue(1);
        individualSpinner.setValue(2);
    }
    
    private void setSubmit() {
        if (!tfFile.getText().equals("") && 
            cbPopulation.getSelectedIndex() != 0 && cbPlatform.getItemCount() == 1 && 
            cbTissueType.getSelectedIndex() != 0 && cbMethod.getSelectedIndex() != 0)
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
    private class MethylationLoaderPaneProgressBar extends SwingWorker<Object, Object>
    {   
        private BaseProgressBar progressBar;
        private int populationId, platformId, tissueTypeId, methodId;
        private String description;
        private int modification;
        private int individual;
        
        @Override
        protected Void doInBackground() {
            populationId = ((Population) cbPopulation.getSelectedItem()).getId();
            platformId = ((Platform) cbPlatform.getSelectedItem()).getId();
            tissueTypeId = ((TissueType) cbTissueType.getSelectedItem()).getId();
            methodId = ((Method) cbMethod.getSelectedItem()).getId();
            description = cbPopulation.getSelectedItem().toString() + " / " + cbPlatform.getSelectedItem().toString() + " / " + cbTissueType.getSelectedItem().toString() + " / " + cbMethod.getSelectedItem().toString();
            
            try {
                List<Integer> methylationFeatureIds = getMethylationFeatureIds(populationId, platformId, tissueTypeId, methodId);
                
                Object[] options = {"  Yes  ", "   No   "};
                if (methylationFeatureIds.size() == 0) {   // New MethylationFeature!!
                    int answer = JOptionPane.showOptionDialog(ui, "Upload \'" + description + "\' to the database?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                    if (answer == JOptionPane.YES_OPTION)
                        this.load(null);
                
                } else {                                   // MethylationFeature already exists!!
                    int answer = JOptionPane.showOptionDialog(ui, "'" + description + "\' already exists\nWould you like to re-submit again?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                    if (answer == JOptionPane.YES_OPTION)
                        this.load(methylationFeatureIds);
                }
                
            } catch (IOException ioe) {
                showMessageDialogError("File or directory does not exist");
                
            } catch (NumberFormatException ne) {
                showMessageDialogError("Wrong file format");
            }
            
            return null;
        }
        
        private void load(List<Integer> methylationFeatureIds) throws IOException, NumberFormatException {
            progressBar = new BaseProgressBar(ui, "Teatime?");   //ADD 18/02/10
            int probeIdColumn = (Integer) probeIdSpinner.getValue() - 1;
            int individualColumn = (Integer) individualSpinner.getValue() - 1;
            
            // 1. Create a new MethylationFeature
            int newMethylationFeatureId = new MethylationFeatureDAO(ui.getAddress()).save(new MethylationFeature(populationId, platformId, tissueTypeId, methodId, description, "tpy"), true);

            // 2. Load profile into Methylation
            MethylationLoader methylationLoader = new MethylationLoader(ui.getAddress(), newMethylationFeatureId, populationId, platformId);
            methylationLoader.load(tfFile.getText(), probeIdColumn, individualColumn);
            
            modification = methylationLoader.modification;
            individual = methylationLoader.individualIds.size();
            //System.out.println("HERE: " + modification + "\t" + individual);
            
            // 3. Update the log part of MethylationFeature
            new MethylationFeatureDAO(ui.getAddress()).updateMethylationFeature(newMethylationFeatureId, tfFile.getText(), individual, modification);
            if (methylationFeatureIds != null)
                new MethylationFeatureDAO(ui.getAddress()).updateParentFeatureId(methylationFeatureIds);
        }
        
        @Override
        protected void done() {
            progressBar.dispose(ui);
            tfFile.setText("");
            ui.moveToParentNode();
            
            String upload = individual + " individuals and " + modification + " modifications uploaded";
            JOptionPane.showMessageDialog(ui, upload,
                "Uploded", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
