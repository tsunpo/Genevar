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

import java.awt.Component;
import java.io.File;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import sanger.team16.common.business.CoreRetrieval;
import sanger.team16.common.business.dao.Core;
import sanger.team16.common.hbm.Assembly;
import sanger.team16.common.hbm.Method;
import sanger.team16.common.hbm.Platform;
import sanger.team16.common.hbm.Reference;
import sanger.team16.common.hbm.Study;
import sanger.team16.common.hbm.TissueType;
import sanger.team16.gui.genevar.db.ExpressionAttributePane;
import sanger.team16.gui.genevar.db.ExpressionPane;
import sanger.team16.gui.genevar.db.ExpressionLoadPane;
import sanger.team16.gui.genevar.db.GenotypeAttributePane;
import sanger.team16.gui.genevar.db.GenotypePane;
import sanger.team16.gui.genevar.db.GenotypeLoadPane;
import sanger.team16.gui.genevar.db.MethylationAttributePane;
import sanger.team16.gui.genevar.db.MethylationPane;
import sanger.team16.gui.genevar.db.MethylationLoadPane;
import sanger.team16.gui.genevar.eqtl.gene.CisEQTLGenePane;
import sanger.team16.gui.genevar.eqtl.query.EQTLQueryPane;
import sanger.team16.gui.genevar.eqtl.snp.CisEQTLSNPPane;
import sanger.team16.gui.genevar.mqtl.gene.CisMQTLGenePane;
import sanger.team16.gui.genevar.mqtl.snp.CisMQTLSNPPane;
import sanger.team16.gui.jface.BaseFileFilter;
import sanger.team16.gui.jface.BaseJFileChooser;
import sanger.team16.gui.jface.BaseJFrame;
import sanger.team16.gui.jface.BaseJPane;
import sanger.team16.gui.jface.BaseTreeNodeInfo;
import sanger.team16.service.CoreRetrievalService;
import sanger.team16.service.client.CoreRetrievalFactory;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
@SuppressWarnings("serial")
public class UI extends BaseJFrame implements TreeSelectionListener
{
    private boolean isServices = true, shouldBeVisible = false;
    private String address = "", username = "", password = "";
    private BaseJFileChooser fc = new BaseJFileChooser();
    private Core core;
    protected GenotypePane genotypePane;
    protected GenotypeAttributePane genotypeAttributePane;
    protected GenotypeLoadPane genotypeUploadPane;
    protected MethylationPane methylationPane;
    protected MethylationAttributePane methylationAttributePane;
    protected MethylationLoadPane methylationUploadPane;
    protected ExpressionPane expressionPane;
    protected ExpressionAttributePane expressionAttributePane;
    protected ExpressionLoadPane expressionUploadPane;
    protected CisEQTLGenePane cisEQTLGenePane;
    protected CisEQTLSNPPane cisEQTLSNPPane;
    protected EQTLQueryPane eQTLQueryPane;
    protected CisMQTLGenePane cisMQTLGenePane;
    protected CisMQTLSNPPane cisMQTLSNPPane;
    //protected MQTLQueryPane mQTLQueryPane;
    
    public static void main(String[] args) throws Exception {
        final String address = Message.WS_SANGER_DEFAULT;
        //final String address = Message.WS_SANGER_INTERNAL;
        //final String address = Message.WS_SANGER_CARDIO;
        //final String address = Message.WS_SANGER_MUTHER;
        //final String address = Message.WS_LOCALHOST;
        final String username = "publicgenevar";
        final String password = "g1f2e3d4c5b6a7";

        UIManager.setLookAndFeel(
            //UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.getSystemLookAndFeelClassName());
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //new UI("localhost/tpy_team16_genevar_3_0_0" + " - " + Message.NEW_DB, false, Message.DB_3_0_0, "", "");   //Database-mode
                //new UI("localhost/tpy_team16_genevar_2_0_0" + " - " + Message.NEW_DB, false, Message.DB_2_0_0, "", "");   //Database-mode
                new UI(Message.GENEVAR_DEFAULT + " - " + Message.NEW_WS, true, address, username, password);   //Web Services-mode
            }
        });
    }

    protected UI(String connectionName, boolean isServices, String address, String username, String password) {
        super(connectionName + " - " + Message.GENEVAR);
        this.isServices = isServices;
        this.address = address;
        this.username = username;
        this.password = password;
       
        this.setLayout();
        
        this.pack();   // !!!
        this.setLocationToCentre();
        //this.setExtendedState(this.getExtendedState()|JFrame.MAXIMIZED_BOTH);   // NOT in Mac
        this.setVisible(true);
        this.setAutoResizeJSplitPanel();
    }

    private void setLayout() {
        try {
            this.setJMenuBar(new UIMenuBar(this));
            this.setCore();
            this.setBasicJTree(this); 
            this.tree.setCellRenderer(new UITreeCellRenderer());
            
            if (!this.isServices)
                shouldBeVisible = true;
            this.setGenotypeTreeNote();
            
            if (address != Message.DB_2_0_0)
                this.setMethylationTreeNote();
            
            this.setExpressionTreeNote();
            this.setEQTLAnalysisTreeNote(true);
            this.setMQTLAnalysisTreeNote(false);
            //if (address != Message.DB_2_0_0)
                //this.setExternalAnalysisTreeNote();            
            //this.setMSEAnalysisTreeNote(false);
            
            tree.setExpandsSelectedPaths(true);
            //tree.setSelectionPath(new TreePath(this.getCurrentChild(0).getPath()));

        } catch (Exception e) {
            this.showConnectionErrorMessage(e);
        }
    }
    
    public boolean isServices() {
        return isServices;
    }

    public String getAddress() {
        return address;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setCore() {
        if (this.isServices) {
            // Web Services //
            CoreRetrievalService client = new CoreRetrievalFactory(this.address, this.username, this.password).create();
            this.core = client.getCore();
        } else
            // Hibernate //
            this.core = new CoreRetrieval(this.address).getCore();
    }
    
    public void setCore(Core core) {
        this.core = core;
    }
    
    /*
     * Was in Core
     */
    public Vector<Study> getStudies() {
        return this.core.studies;
    }
    
    public Vector<Study> getStudies(String type) {
        int size = this.core.studies.size();
        
        Vector<Study> typeStudies = new Vector<Study>();
        typeStudies.add(new Study("Project"));
        
        for (int i=1 ; i<size ; i++) {
            Study study = this.core.studies.get(i);
            
            if (type.equals("E") && study.getExpressionPlatformId() != 0)
            	typeStudies.add(study);
            else if (type.equals("M") && study.getMethylationPlatformId() != 0)
            	typeStudies.add(study);
        }
        
        return typeStudies;
    }
    
    public Platform getPlatform(int platformId) {
        int size = this.core.platforms.size();
        
        for (int i=0 ; i<size ; i++) {
            Platform platform = this.core.platforms.get(i);
            if (platform.getId() == platformId)
                return platform;
        }
        return new Platform();
    }

    public Vector<Platform> getPlatforms(String type) {
        int size = this.core.platforms.size();
        
        Vector<Platform> typePlatforms = new Vector<Platform>();
        if (type.equals("E"))
            typePlatforms.add(new Platform("Expression Platform"));
        else if (type.equals("M"))
            typePlatforms.add(new Platform("Methylation Platform"));
        
        for (int i=1 ; i<size ; i++) {
            Platform platform = this.core.platforms.get(i);
            if (platform.getType().equals(type))
                typePlatforms.add(platform);
        }
        
        return typePlatforms;
    }
    
    public TissueType getTissueType(int tissueTypeId) {
        int size = this.core.tissueTypes.size();
        
        for (int i=0 ; i<size ; i++) {
            TissueType tissueType = this.core.tissueTypes.get(i);
            if (tissueType.getId() == tissueTypeId)
                return tissueType;
        }
        return new TissueType();
    }

    public Vector<TissueType> getTissueTypes() {
        return this.core.tissueTypes;
    }
    
    public Method getMethod(int methodId) {
        int size = this.core.methods.size();
        
        for (int i=0 ; i<size ; i++) {
            Method method = this.core.methods.get(i);
            if (method.getId() == methodId)
                return method;
        }
        return new Method();
    }

    public Vector<Method> getMethods(String type) {
        int size = this.core.methods.size();
        
        Vector<Method> typeMethods = new Vector<Method>();
        if (type.equals("E"))
            typeMethods.add(new Method("Normalisation Method (E)", "E", ""));
        else if (type.equals("M"))
            typeMethods.add(new Method("Normalisation Method (M)", "M", ""));
        else if (type.equals("G"))
            typeMethods.add(new Method("Filter Method", "G", ""));
        
        for (int i=0 ; i<size ; i++) {
            Method method = this.core.methods.get(i);
            if (method.getType().equals(type))
                typeMethods.add(method);
        }
        
        return typeMethods;
    }
    
    /*
     * Assembly and Reference
     * In 2.0.0, GenotypeAssembly represents GenotypeReference (Deprecated)
     */
    public Assembly getAssembly(int assemblyId) {
        if (this.getAddress().equals("tpy_team16_genevar_2_0_0.cfg.xml")) {
            int referenceId = assemblyId;
            int size = this.core.references.size();
            
            for (int i=0 ; i<size ; i++) {
                Reference reference = this.core.references.get(i);
                if (reference.getId() == referenceId)
                    return new Assembly(reference.getId(), reference.getName());   //ADD 05/08/11 Disguised Reference to Assembly
            }
        } else {
            int size = this.core.assemblies.size();
            
            for (int i=0 ; i<size ; i++) {
                Assembly assembly = this.core.assemblies.get(i);
                if (assembly.getId() == assemblyId)
                    return assembly;
            }            
        }
        
        return new Assembly();
    }

    public Vector<Assembly> getAssemblies() {
        if (this.getAddress().equals("tpy_team16_genevar_2_0_0.cfg.xml")) {
            int size = this.core.references.size();
            
            Vector<Assembly> genotypeReferences = new Vector<Assembly>();
            genotypeReferences.add(new Assembly(0, "Genotype Reference"));   //ADD 05/08/11 Disguised Reference to Assembly
            
            for (int i=0 ; i<size ; i++) {
                Reference reference = this.core.references.get(i);
                if (reference.getType().equals("G"))
                    genotypeReferences.add(new Assembly(reference.getId(), reference.getName()));
            }
            
            return genotypeReferences;
        }
        return this.core.assemblies;
    }
    
    public Reference getReference(int referenceId) {
        int size = this.core.references.size();
        
        for (int i=0 ; i<size ; i++) {
            Reference reference = this.core.references.get(i);
            if (reference.getId() == referenceId)
                return reference;
        }
        return new Reference();
    }

    public Vector<Reference> getReferences(int genotypeAssemblyId, int platformId) {
        int size = this.core.references.size();
        genotypeAssemblyId = getGenotypeAssemnlyId(genotypeAssemblyId);
        
        Vector<Reference> platformReferences = new Vector<Reference>();
        platformReferences.add(new Reference(0, "Source", ""));
        
        for (int i=0 ; i<size ; i++) {
            Reference reference = this.core.references.get(i);
            if (reference.getAssemblyId() == genotypeAssemblyId && reference.getPlatformId() == platformId)
                platformReferences.add(reference);
        }
        
        return platformReferences;
    }
    
    private int getGenotypeAssemnlyId(int genotypeAssemblyId) {   //For Database 2.0.0
        int size = this.core.references.size();
        
        if (address.equals("tpy_team16_genevar_2_0_0.cfg.xml"))   //ADD 14/12/11
            for (int i=0 ; i<size ; i++) {
                Reference reference = this.core.references.get(i);
                if (reference.getType().equals("G"))
                    if (reference.getId() == genotypeAssemblyId)
                        return reference.getAssemblyId();
            }
        return genotypeAssemblyId;
    }

    /*
    public Vector<Reference> getReferences(Reference genotypeReference, int platformId) {
        int assemblyId = genotypeReference.getAssemblyId();
        Assembly assembly = this.getAssembly(assemblyId);
        
        return this.getReferences(assembly, platformId);
    }*/
    
    protected void setGenotypeTreeNote() {
        this.genotypePane = new GenotypePane(this);
        this.genotypeAttributePane = new GenotypeAttributePane(this);
        this.genotypeUploadPane = new GenotypeLoadPane(this);
        
        DefaultMutableTreeNode genotypeNote = this.addToNode(root, Message.NEW_GTM, genotypePane, false);
        
        this.addToNode(genotypeNote, Message.EDIT_ATT, genotypeAttributePane, shouldBeVisible);
        this.addToNode(genotypeNote, Message.LOAD_DATA, genotypeUploadPane, shouldBeVisible);
    }

    protected void setMethylationTreeNote() {
        this.methylationPane = new MethylationPane(this);
        this.methylationAttributePane = new MethylationAttributePane(this);
        this.methylationUploadPane = new MethylationLoadPane(this);
        
        DefaultMutableTreeNode methylationNote = this.addToNode(root, Message.NEW_MEM, methylationPane, false);
        
        this.addToNode(methylationNote, Message.EDIT_ATT, methylationAttributePane, shouldBeVisible);
        this.addToNode(methylationNote, Message.LOAD_DATA, methylationUploadPane, shouldBeVisible);
    }
    
    protected void setExpressionTreeNote() {
        this.expressionPane = new ExpressionPane(this);
        this.expressionAttributePane = new ExpressionAttributePane(this);
        this.expressionUploadPane = new ExpressionLoadPane(this);
        
        DefaultMutableTreeNode epressionNote = this.addToNode(root, Message.NEW_EXM, expressionPane, false);
        
        this.addToNode(epressionNote, Message.EDIT_ATT, expressionAttributePane, shouldBeVisible);
        this.addToNode(epressionNote, Message.LOAD_DATA, expressionUploadPane, shouldBeVisible);
    }

    protected void setEQTLAnalysisTreeNote(boolean isNew) {
        this.cisEQTLGenePane = new CisEQTLGenePane(this);
        this.cisEQTLSNPPane = new CisEQTLSNPPane(this);
        this.eQTLQueryPane = new EQTLQueryPane(this);
        
        DefaultMutableTreeNode eqtlAnalysisNote = this.addToNode(root, Message.NEW_EQTL, new QTLAnalysisPane(this, Message.NEW_EQTL), true);

        DefaultMutableTreeNode cisEQTLGeneNote = this.addToNode(eqtlAnalysisNote, Message.EQTL_GENE, cisEQTLGenePane, true);
        this.addToNode(eqtlAnalysisNote, Message.EQTL_SNP, cisEQTLSNPPane, true);
        this.addToNode(eqtlAnalysisNote, Message.EQTL_QUERY, eQTLQueryPane, true);
        
        if (isNew)
            this.tree.setSelectionPath(new TreePath(cisEQTLGeneNote.getPath()));
    }
    
    protected void setMQTLAnalysisTreeNote(boolean isNew) {
        this.cisMQTLGenePane = new CisMQTLGenePane(this);
        this.cisMQTLSNPPane = new CisMQTLSNPPane(this);
        //this.mQTLQueryPane = new MQTLQueryPane(this);
        
        DefaultMutableTreeNode eqtlAnalysisNote = this.addToNode(root, Message.NEW_MQTL, new QTLAnalysisPane(this, Message.NEW_MQTL), true);

        DefaultMutableTreeNode cisEQTLGeneNote = this.addToNode(eqtlAnalysisNote, Message.MQTL_CPG, cisMQTLGenePane, true);
        this.addToNode(eqtlAnalysisNote, Message.MQTL_SNP, cisMQTLSNPPane, true);
        //this.addToNode(eqtlAnalysisNote, Message.MQTL_QUERY, mQTLQueryPane, true);
        
        if (isNew)
            this.tree.setSelectionPath(new TreePath(cisEQTLGeneNote.getPath()));
    }
/*
    void setMSEAnalysisTreeNote(boolean isNew) {
        DefaultMutableTreeNode mseNote = this.addToNode(root, Message.NEW_MSE, new AnalysisPane(this, Message.NEW_MSE), true);

        DefaultMutableTreeNode cisGeneNote = this.addToNode(mseNote, Message.MSE_CIS, new CisMSEGenePane(this), true);
        //DefaultMutableTreeNode cisSNPNote = this.addToNode(analysisNote, Message.EQTL_SNP, new CisSNPPane(this), true);
        DefaultMutableTreeNode queryNote = this.addToNode(mseNote, Message.MSE_QUERY, new QueryMSEPane(this), true);

        if (isNew)
            this.tree.setSelectionPath(new TreePath(cisGeneNote.getPath()));
    }
    */
    
    /**
     * Overwrite BaseJFrame
     */
    public void valueChanged(TreeSelectionEvent tse) {
        //int index = 0;
        
        try {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            //DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
            //index = parentNode.getIndex(node);
            BaseTreeNodeInfo nodeInfo = (BaseTreeNodeInfo) node.getUserObject();   //TODO
            BaseJPane object = (BaseJPane) nodeInfo.getObject();
            //object.refresh();   // Flexible for each BaseJPane
            
            this.objectPanel.setViewportView(object);
    
        } catch (Exception e) {
        	if (!(e instanceof java.lang.NullPointerException))   //ADD 04/12/11 TODO ???
                this.showConnectionErrorMessage(e);
        }
    }

    /**
     * Error Messages (Same as in AbstractPane)
     */
    protected void showConnectionErrorMessage(Exception e) {
        String className = e.getClass().getName();
        
        if (e.getCause() instanceof java.lang.NoClassDefFoundError ||
                e.getCause() instanceof java.lang.ExceptionInInitializerError ||
                className.equals("org.hibernate.hql.ast.QuerySyntaxException") ||   //ADD 23/11/10)
                className.equals("org.hibernate.exception.SQLGrammarException"))   //ADD 23/11/10)   
            this.showMessageDialogConnectionFailure();
        
        else if (e instanceof javax.xml.ws.soap.SOAPFaultException ||
                e instanceof javax.xml.ws.WebServiceException ||   //ADD 19/11/10
                className.equals("org.apache.ws.security.WSSecurityException") ||   //ADD 17/01/11
                className.equals("org.apache.cxf.interceptor.Fault") ||   //ADD 08/02/11
                className.equals("javax.security.auth.callback.UnsupportedCallbackException"))   //ADD 25/02/11
            this.showMessageDialogServicesFailure();
        
        else if (e instanceof java.lang.NullPointerException)
            JOptionPane.showMessageDialog(this, "Data not found exception",
                "Information", JOptionPane.INFORMATION_MESSAGE);

        else if (className.equals("org.hibernate.exception.DataException") ||   //ADD 14/12/11  //TODO
        		className.equals("com.mysql.jdbc.MysqlDataTruncation"))
            JOptionPane.showMessageDialog(this, "Data too long for column",
                "Information", JOptionPane.INFORMATION_MESSAGE);
        
        else
            JOptionPane.showMessageDialog(this, "Unexpected item in the bagging area",
                "Unexpected Error", JOptionPane.ERROR_MESSAGE);

        e.printStackTrace();
    }
    
    protected void showMessageDialogConnectionFailure() {
        JOptionPane.showMessageDialog(this, "Could not obtain database connection",
            "Connection Failure", JOptionPane.ERROR_MESSAGE);
    }
    
    protected void showMessageDialogServicesFailure() {
        JOptionPane.showMessageDialog(this, "Could not obtain Web Services",
            "Services Failure", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Overwrite the moveToParentNode in BaseJFrame
     */
    public DefaultMutableTreeNode moveToParentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) currentSelection.getLastPathComponent();
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) currentNode.getParent();
        
        BaseTreeNodeInfo nodeInfo = (BaseTreeNodeInfo) parent.getUserObject();
        BaseJPane object = (BaseJPane) nodeInfo.getObject();
        object.refresh();   // For ExpressionPane and GenotypePane
        
        this.tree.setSelectionPath(new TreePath(parent.getPath()));
        
        return parent;
    }

    /*
     * To aceess BaseJFileChooser
     */
    public void addChoosableFileFilter(String extension) {
        if (extension == Message.TXT)
            this.fc.addChoosableFileFilter(new BaseFileFilter("txt", "Tab-delimited Text File (*.txt)"));
       
        else if (extension == Message.BIM)
            this.fc.addChoosableFileFilter(new BaseFileFilter("bim", "PLINK BIM File (*.bim)")); 
        
        else if (extension == Message.PED)
            this.fc.addChoosableFileFilter(new BaseFileFilter("ped", "PLINK PED File (*.ped)"));
    }
    
    public boolean showFileChooserOpenDialog(Component ui, BaseFileFilter filter) {
        return this.fc.showOpenDialog(ui, filter);
    } 
    
    public boolean showFileChooserSaveDialog(Component ui, String file) {
        return this.fc.showSaveDialog(ui, file);
    }
    
    public File getFileChooserSelectedFile() {
        return this.fc.getSelectedFile();
    }
    
    public void resetFileChooser() {
        this.fc.reset();
    }
    
    /**
     * @author Tsun-Po Yang <tpy@sanger.ac.uk>
     * @link   http://www.sanger.ac.uk/Teams/Team16/
     */
    private class UITreeCellRenderer extends DefaultTreeCellRenderer {
        Icon dbIcon = createImageIcon("images/db.png");
        Icon editIcon = createImageIcon("images/edit.png");
        Icon uploadIcon = createImageIcon("images/upload.gif");        
        Icon analysisIcon = createImageIcon("images/analysis.png");  
        Icon cisGeneIcon = createImageIcon("images/cis-gene.gif");
        Icon cisSNPIcon = createImageIcon("images/cis-snp.png");
        Icon eQTLIcon = createImageIcon("images/eqtl.gif");
        //Icon mseIcon = createImageIcon("images/mse.gif");
        
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            
            if (((DefaultMutableTreeNode) value).toString() == Message.NEW_GTM ||
                    ((DefaultMutableTreeNode) value).toString() == Message.NEW_MEM ||
                    ((DefaultMutableTreeNode) value).toString() == Message.NEW_EXM ||
                    ((DefaultMutableTreeNode) value).toString() == Message.NEW_EAM)
                setIcon(dbIcon);
            if (((DefaultMutableTreeNode) value).toString() == Message.EDIT_ATT)
                setIcon(editIcon);
            if (((DefaultMutableTreeNode) value).toString() == Message.LOAD_DATA)
                setIcon(uploadIcon);
            
            if (((DefaultMutableTreeNode) value).toString() == Message.NEW_EQTL ||
                    ((DefaultMutableTreeNode) value).toString() == Message.NEW_MQTL)
                setIcon(analysisIcon);
            
            if (((DefaultMutableTreeNode) value).toString() == Message.EQTL_QUERY ||
            		((DefaultMutableTreeNode) value).toString() == Message.MQTL_QUERY)
                setIcon(eQTLIcon);
            if (((DefaultMutableTreeNode) value).toString() == Message.EQTL_GENE ||
            		((DefaultMutableTreeNode) value).toString() == Message.MQTL_CPG)
                setIcon(cisGeneIcon);
            if (((DefaultMutableTreeNode) value).toString() == Message.EQTL_SNP ||
            		((DefaultMutableTreeNode) value).toString() == Message.MQTL_SNP)
                setIcon(cisSNPIcon);
            //else
            //    setToolTipText(null); //no tool tip

            return this;
        }
    }
}

/*
public String getAddress() {
    return address;
}

public boolean isServices() {
    return isServices;
}
*/

/*
            if (index == 0)
                this.objectPanel.setViewportView(new ExpressionPane(this, "Connection Failure"));
            else if (index == 1)
                this.objectPanel.setViewportView(new GenotypePane(this, "Connection Failure"));
            
            if (index == 0)
                this.objectPanel.setViewportView(new ExpressionPane(this, "Services Failure"));
            else if (index == 1)
                this.objectPanel.setViewportView(new GenotypePane(this, "Services Failure"));
*/