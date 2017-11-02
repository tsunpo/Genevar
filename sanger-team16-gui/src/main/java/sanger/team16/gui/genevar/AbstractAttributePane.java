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
package sanger.team16.gui.genevar;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.xml.ws.soap.SOAPFaultException;

import sanger.team16.common.business.CoreRetrieval;
import sanger.team16.common.hbm.Assembly;
import sanger.team16.common.hbm.Platform;
import sanger.team16.common.hbm.Population;
import sanger.team16.common.hbm.Study;
import sanger.team16.common.hbm.TissueType;
import sanger.team16.common.hbm.Method;
import sanger.team16.common.hbm.dao.PopulationDAO;
import sanger.team16.common.hbm.dao.StudyDAO;
import sanger.team16.common.hbm.dao.TissueTypeDAO;
import sanger.team16.common.hbm.dao.MethodDAO;
import sanger.team16.gui.jface.BaseJLabel;
import sanger.team16.gui.jface.BaseJPane;
import sanger.team16.service.CoreRetrievalService;
import sanger.team16.service.client.CoreRetrievalFactory;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class AbstractAttributePane extends AbstractPane implements ActionListener
{   
    protected JComboBox cbStudy, cbPopulation, cbGenotypeAssembly, cbMethylationPlatform, cbExpressionPlatform, cbTissueType, cbMethod;
    protected JButton bStudy, bPopulation, bTissueType, bMethod;
    protected Vector<Population> studyPopulations;
    protected Vector<Platform> studyMethylationPlatforms, studyExpressionPlatforms;

    public AbstractAttributePane(UI ui) {
        super(ui);
    }
    
    /*
     * Overwrite in GenotypeAttributePane, ExpressionAttributePane...
     */
    protected void refresh(int studyIndex, int genotypeXrefSourceIndex, int methylationPlatformIndex, int expressionPlatformIndex) {}
    
    protected void init() {
        studyPopulations = new Vector<Population>();
        studyMethylationPlatforms = new Vector<Platform>();
        studyExpressionPlatforms = new Vector<Platform>();
        
        studyPopulations.add(new Population(0, "Population"));
        studyMethylationPlatforms.addAll(this.ui.getPlatforms("M"));
        studyExpressionPlatforms.addAll(this.ui.getPlatforms("E"));
    }
    
    protected void setNewStudyPanel(int studyIndex, int genotypeAssemblyIndex, int methylationPlatformIndex, int expressionPlatformIndex) {
        BaseJPane panel = new BaseJPane("Create a New Study");
        
        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();

        cbStudy = new JComboBox(this.ui.getStudies());
        cbStudy.setSelectedIndex(studyIndex);
        cbStudy.addActionListener(this);
        cbStudy.setEditable(true);
        p0.add(cbStudy);
        p0.add(new BaseJLabel("(Editable)", Color.GREEN.darker()));
        p0.add(new JLabel("/"));
        
        cbGenotypeAssembly = new JComboBox(this.ui.getAssemblies());   //ADD 05/08/11 Merged 2.0.0 to 3.0.0
        cbGenotypeAssembly.setSelectedIndex(genotypeAssemblyIndex);
        p0.add(cbGenotypeAssembly);
        p0.add(new JLabel("/"));

        cbExpressionPlatform = new JComboBox(this.ui.getPlatforms("E"));
        cbExpressionPlatform.setSelectedIndex(expressionPlatformIndex);
        p0.add(cbExpressionPlatform);

        cbMethylationPlatform = new JComboBox(this.ui.getPlatforms("M"));
        cbMethylationPlatform.setSelectedIndex(methylationPlatformIndex);
        if (!this.ui.getAddress().equals("tpy_team16_genevar_2_0_0.cfg.xml")) {
            p0.add(new JLabel("/"));
            p0.add(cbMethylationPlatform);
        }

        bStudy = new JButton("     Create     ");
        bStudy.addActionListener(this);
        p0.add(bStudy);
        
        p0.setBaseSpringBoxTrailing();
        panel.add(p0);
        // ------------------ Panel p0 (end) ------------------ //
        
        panel.setBaseSpringGrid(1);
        this.add(panel);
    }
/*
    private void setHideDatasetPanel() {
        BaseJPane panel = new BaseJPane("Hide a Project",10,25,10,25);
        
        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();

        cbDatasetHide = new JComboBox(this.ui.getCore().primaryKey.datasets);
        p0.add(cbDatasetHide);
        
        bDatasetHide = new JButton("     Hide     ");
        bDatasetHide.addActionListener(this);
        p0.add(bDatasetHide);
        
        p0.setBaseSpringBoxTrailing();
        panel.add(p0);
        // ------------------ Panel p0 (end) ------------------ //
        
        panel.add(new JLabel(""));
        
        panel.setBaseSpringGrid(1);
        this.add(panel);
    }
*/
    public void actionPerformed(ActionEvent ae) {
        try {
            this.attributePaneActionPerformed(ae);
            
        } catch (ExceptionInInitializerError e) {
            this.showMessageDialogConnectionFailure();
            
        } catch (SOAPFaultException e) {
            this.showMessageDialogServicesFailure();
            
        } catch (ClassCastException e) {
            this.showMessageDialogError("This attribute already exists");
        }//catch (NonUniqueResultException e) {}
    }
    
    public void attributePaneActionPerformed(ActionEvent ae) {
        int studyIndex = cbStudy.getSelectedIndex();
        int genotypeAssemblyIndex = cbGenotypeAssembly.getSelectedIndex();
        int methylationPlatformIndex = cbMethylationPlatform.getSelectedIndex();
        int expressionPlatformIndex = cbExpressionPlatform.getSelectedIndex();
        
        if (ae.getSource() == cbStudy) {
            this.init();
            
            if (studyIndex > 0) {
                Study study = (Study) cbStudy.getSelectedItem();
                
                studyPopulations.addAll(this.getPopulationsWhereStudyId(study.getId()));
                studyExpressionPlatforms.clear();
                studyExpressionPlatforms.add(this.ui.getPlatform(study.getExpressionPlatformId()));
                this.refresh(studyIndex, genotypeAssemblyIndex, methylationPlatformIndex, expressionPlatformIndex);
            
            } else if (studyIndex == 0)
                this.refresh(0, genotypeAssemblyIndex, methylationPlatformIndex, expressionPlatformIndex);
        
        } else if (ae.getSource() == bStudy) {
            if (studyIndex == 0 || genotypeAssemblyIndex == 0 || expressionPlatformIndex == 0) {
                this.showMessageDialogWarning("At least the \"Project\", \"Genome Assembly\" and \"Expression Platform\" must be selected");
                
            } else {
                String addNew = "";
                if (studyIndex == -1)
                    addNew = (String) cbStudy.getSelectedItem();
                else if (studyIndex > 0)
                    addNew = ((Study) cbStudy.getSelectedItem()).toString();
                
                if (addNew.equals(""))
                    this.showMessageDialogWarning("Project name should not be empty");
                else {
                    Assembly assembly = (Assembly) cbGenotypeAssembly.getSelectedItem();
                    int genotypeAssemblyId = assembly.getId();  
                    Platform methylationPlatform = (Platform) cbMethylationPlatform.getSelectedItem();
                    Platform expressionPlatform = (Platform) cbExpressionPlatform.getSelectedItem();

                    String description = addNew + " / " + assembly.getName();
                    if (cbMethylationPlatform.getSelectedIndex() != 0)
                        description += " / " + methylationPlatform.getName();
                    description += " / " + expressionPlatform.getName();
                    
                    if (!new StudyDAO(this.ui.getAddress()).uniqueResultOrPersist(addNew, genotypeAssemblyId, methylationPlatform.getId(), expressionPlatform.getId(), description)) { 
                        if (this.showOptionDialogAddNewToTable(description, "Project")) {
                            this.ui.setCore(new CoreRetrieval(this.ui.getAddress()).getCore());
                            this.init();
                            this.refreshAll();
                        }
                    } else
                        this.showMessageAddNewAlreadyExists(description);
                }   
            }
            
        } else if (ae.getSource() == bPopulation) {
            int studyId = ((Study) cbStudy.getSelectedItem()).getId();
            
            if (studyId == 0)
                this.showMessageDialogWarning("Please select a project from \"Create a New Project\" first");
            else {
                String addNew = (String) cbPopulation.getSelectedItem();
                //TODO
                if (new PopulationDAO(this.ui.getAddress()).uniqueResult(studyId, addNew) == 0) {
                    if (this.showOptionDialogAddNewToTable(addNew, "Population"))
                        new PopulationDAO(this.ui.getAddress()).persist(new Population(studyId, addNew), true);
                } else
                    this.showMessageAddNewAlreadyExists(addNew);
                
                this.ui.setCore(new CoreRetrieval(this.ui.getAddress()).getCore());
                this.init();
                studyPopulations.addAll(this.getPopulationsWhereStudyId(studyId));
                this.refreshAll();
                this.refresh(studyIndex, genotypeAssemblyIndex, methylationPlatformIndex, expressionPlatformIndex);
            }
            
        } else {
            if (ae.getSource() == bTissueType)
                this.tissueTypeAction(); 
            
            else if (ae.getSource() == bMethod)
                this.versionAction(((Method) cbMethod.getItemAt(0)).getType());
            
            this.ui.setCore(new CoreRetrieval(this.ui.getAddress()).getCore());
            this.refreshAll();
            this.refresh(studyIndex, genotypeAssemblyIndex, methylationPlatformIndex, expressionPlatformIndex);
        }
    }

    public void tissueTypeAction() {
        String addNew = (String) cbTissueType.getSelectedItem();
        
        if (new TissueTypeDAO(this.ui.getAddress()).uniqueResult(addNew) == 0) {
            if (showOptionDialogAddNewToTable(addNew, "TissueType"))
                new TissueTypeDAO(this.ui.getAddress()).persist(new TissueType(addNew, ""), true);
        } else
            showMessageAddNewAlreadyExists(addNew);
    }
    
    public void versionAction(String type) {
        String addNew = (String) cbMethod.getSelectedItem();
        
        if (new MethodDAO(this.ui.getAddress()).uniqueResult(addNew, type) == 0) {
            if (showOptionDialogAddNewToTable(addNew, "Method"))
                new MethodDAO(this.ui.getAddress()).persist(new Method(addNew, type, ""), true);
        } else
            showMessageAddNewAlreadyExists(addNew);
    }
    
    private void refreshAll() {
        this.ui.genotypePane.refresh();
        this.ui.genotypeAttributePane.refresh(0, 0, 0, 0);
        this.ui.genotypeUploadPane.init();
        this.ui.genotypeUploadPane.refresh(0, 0, 0, 0, 0, false);
        
        //if (address != Message.DB_2_0_0) {
        //    this.ui.getCore().methylationPane.refresh();
        //    this.ui.getCore().methylationAttributePane.refresh(0, 0, 0, 0);
        //    this.ui.getCore().methylationUploadPane.init();
        //    this.ui.getCore().methylationUploadPane.refresh(0);
        //}
        
        this.ui.expressionPane.refresh();
        this.ui.expressionAttributePane.refresh(0, 0, 0, 0);
        this.ui.expressionUploadPane.init();
        this.ui.expressionUploadPane.refresh(0);
        
        this.ui.cisEQTLGenePane.refresh(new SelectedIndex(), false);
        this.ui.cisEQTLSNPPane.refresh(new SelectedIndex());
        this.ui.eQTLQueryPane.refresh(new SelectedIndex(), false);
    }
    
    /**
     * MessageDialog
     */
    protected boolean showOptionDialogAddNewToTable(String addNew, String tableName) {
        Object[] options = {"  Yes  ", "   No   "};
        
        int answer = JOptionPane.showOptionDialog(this.ui, "Add \'" + addNew + "\' to the " + tableName + " table?",
            "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        
        if (answer == JOptionPane.YES_OPTION) return true;
        return false;
    }
    
    protected void showMessageAddNewAlreadyExists(String addNew) {
        JOptionPane.showMessageDialog(this.ui, "'" + addNew + "\' already exists",
            "Warning", JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Same as in AbstractManagerPane
     */
    private Vector<Population> getPopulationsWhereStudyId(int studyId) {
        Vector<Population> populations = new Vector<Population>();
        List<Population> lists;
        
        if (this.ui.isServices()) {
            // Web Services //
            CoreRetrievalService client = new CoreRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            lists = client.getPopulationsWhereStudyId(studyId);
        } else
            // Hibernate //
            lists = new CoreRetrieval(this.ui.getAddress()).getPopulationsWhereStudyId(studyId);
        
        if (lists != null)
            for (int i=0 ; i<lists.size() ; i++)
                populations.add(lists.get(i));
        
        return populations;
    }
}
