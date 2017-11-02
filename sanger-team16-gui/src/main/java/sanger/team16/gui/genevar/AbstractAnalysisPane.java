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
package sanger.team16.gui.genevar;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import sanger.team16.common.business.CoreRetrieval;
import sanger.team16.common.business.VariationRetrieval;
import sanger.team16.common.business.dao.Statistic;
import sanger.team16.common.hbm.Algorithm;
import sanger.team16.common.hbm.Variation;
import sanger.team16.gui.genevar.AbstractPane;
import sanger.team16.gui.genevar.Message;
import sanger.team16.gui.genevar.UI;
import sanger.team16.gui.jface.BaseJLabel;
import sanger.team16.gui.jface.BaseJPane;
import sanger.team16.gui.jface.BrowserLauncher;
import sanger.team16.service.CoreRetrievalService;
import sanger.team16.service.VariationRetrievalService;
import sanger.team16.service.client.CoreRetrievalFactory;
import sanger.team16.service.client.VariationRetrievalFactory;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
@SuppressWarnings("serial")
public class AbstractAnalysisPane extends AbstractPane 
{
    protected JComboBox cbStatistic, cbAlgorithm;
    protected String[] distances;
    protected String[] pValues;
    protected String[] permutations = {"None", "10,000"};

    public AbstractAnalysisPane(UI ui) {
        super(ui);
    }

    protected void setLinkToPanel(BaseJPane p, int assembly, String chromosome, int position, int distance) {
        LinkToListener listener = new LinkToListener(assembly, chromosome, position, distance);
        
        JLabel linkTo = new JLabel("Link to:");
        //linkTo.setFont(new Font("PLAIN", Font.BOLD, 12));
        //linkTo.setForeground(Color.blue);
        p.add(linkTo);
        
        JButton bEnsembl = new JButton("Ensembl");
        bEnsembl.setActionCommand("Ensembl");
        bEnsembl.addActionListener(listener);
        bEnsembl.setForeground(Color.BLUE);
        p.add(bEnsembl);
        
        JButton bHapMap = new JButton("HapMap");
        bHapMap.setActionCommand("HapMap");
        bHapMap.addActionListener(listener);
        bHapMap.setForeground(Color.BLUE);
        p.add(bHapMap);
        
        JButton bUCSC = new JButton("  UCSC  ");
        bUCSC.setActionCommand("UCSC");
        bUCSC.addActionListener(listener);
        bUCSC.setForeground(Color.BLUE);
        p.add(bUCSC);
    }
    
    protected double getThreshold(String pValue) {
        if (pValue.equalsIgnoreCase("No limit"))
            return 1;
        return new Double(pValue);
    }
    
    /**
     * @author Tsun-Po Yang <tpy@sanger.ac.uk>
     * @link   http://www.sanger.ac.uk/Teams/Team16/
     */
    public class LinkToListener implements ActionListener
    {
    	int assemblyId;
        String chromosome;
        int position;
        int distance;
        
        public LinkToListener(int assemblyId, String chromosome, int position, int distance) {
            this.assemblyId = assemblyId;
        	this.chromosome = chromosome;
            this.position = position;
            this.distance = distance;
        }
        
        public void actionPerformed(ActionEvent e) {
            int start = position - distance;
            int stop = position + distance;

            if (e.getActionCommand() == "Ensembl")
            	if (assemblyId == 1)   //TODO
                    BrowserLauncher.openURL(Message.URL_ENSEMBL_36 + chromosome + ":" + start + "-" + stop);
            	else if (assemblyId == 2)
                    BrowserLauncher.openURL(Message.URL_ENSEMBL_37 + chromosome + ":" + start + "-" + stop);
            
            else if (e.getActionCommand() == "UCSC")
              	if (assemblyId == 1)
                    BrowserLauncher.openURL(Message.URL_UCSC_36 + chromosome + ":" + start + "-" + stop);
            	else if (assemblyId == 2)
                    BrowserLauncher.openURL(Message.URL_UCSC_37 + chromosome + ":" + start + "-" + stop);
            
            else if (e.getActionCommand() == "HapMap")
              	if (assemblyId == 1)
                    BrowserLauncher.openURL(Message.URL_HAPMAP_36 + chromosome + ":" + start + ".." + stop);
            	else if (assemblyId == 2)
                    BrowserLauncher.openURL(Message.URL_HAPMAP_37 + chromosome + ":" + start + "-" + stop);
        }
    }

    /*
     * CisEQTLGenePane, EQTLSNPGenePane
     */
    protected JLabel getJLabeledSelectedItem(JComboBox cb, boolean searched) {
        if (searched == true) {
            if (cb.getSelectedIndex() == 0)
                return new BaseJLabel("N/A", Color.gray, new Font("Arial", Font.BOLD, 13));
            return new BaseJLabel(cb.getSelectedItem().toString(), new Font("Arial", Font.BOLD, 13));
        } else
            return new BaseJLabel("N/A", Color.gray, new Font("Arial", Font.BOLD, 13));
    }

    /*
     * CisEQTLSNPPane
     */
    protected JLabel getJLabeledSelectedItem(JComboBox cb) {
        if (cb.getSelectedIndex() == 0)
            return new BaseJLabel("N/A", Color.gray, new Font("Arial", Font.BOLD, 13));
        else
            return new BaseJLabel(cb.getSelectedItem().toString(), new Font("Arial", Font.BOLD, 13));
    }
    
    /*
     * cbStatistic, cbAlgorithm
     */
    protected int getStatisticId(Statistic statistic, JComboBox cbAlgorithm) {
        if (statistic.getId() < Statistic.EXTERNAL_ALGORITHM)
            return statistic.getId();
        return ((Algorithm) cbAlgorithm.getSelectedItem()).getId();
    }
    
    protected void setAlgorithms(Vector<Algorithm> algorithms) {
        cbStatistic.setSelectedIndex(cbStatistic.getComponentCount());
        
        cbAlgorithm = new JComboBox(algorithms);
        cbAlgorithm.setEnabled(true);
    }
    
    protected void setNullAlgorithms(int statisticIndex) {
        Vector<Algorithm> algorithms = new Vector<Algorithm>();
        algorithms.add(0, new Algorithm("Preloaded analysis"));
        
        cbStatistic.setSelectedIndex(statisticIndex);
        
        cbAlgorithm = new JComboBox(algorithms);
        cbAlgorithm.setEnabled(false);
    }
    
    protected ArrayList<String> convertTextAreaToArrayList(String textArea) {
        ArrayList<String> lists = new ArrayList<String>();
        
        String[] st = textArea.split("\n");
        for (int i=0 ; i<st.length ; i++)
            lists.add(st[i]);
        
        return lists;
    }
 
    protected String convertArrayListToTextAreaString(ArrayList<String> lists) {
        String string = "";
        
        for (int i=0 ; i<lists.size() ; i++)
            string += lists.get(i) + "\n";
        
        return string;
    }
    
    protected ArrayList<String> readFromFile(String fileName) {
        ArrayList<String> lists = new ArrayList<String>();
        
        try {
            if (!fileName.equals("")) {
                BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
                
                String line;
                while ((line = in.readLine()) != null) {
                    String[] st = line.split("\t", 2);
                    lists.add(st[0]);
                }
                
                in.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return lists;
    }

    protected List<String> getInputSNPs(String snpString, String fileName) {
        List<String> snps = new ArrayList<String>();
        
        if (!snpString.equals(""))
            snps.addAll(convertTextAreaToArrayList(snpString));
        if (!fileName.equals(""))
            snps.addAll(readFromFile(fileName));
        
        return snps;
    }
    
    /**
     * VariationRetrieval
     */
    protected Variation getSNP(int studyId, String variationName) {
        if (this.ui.isServices()) {
            // Web Services //
            VariationRetrievalService client = new VariationRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            return client.getSNP(studyId, variationName);
        } else
            // Hibernate //
            return new VariationRetrieval(this.ui.getAddress()).getSNP(studyId, variationName);
    }

    /**
     * Algorithm and AnalysisFeature
     */
    protected Vector<Algorithm> getQTLAlgorithms(int studyId, String type) {
        Vector<Algorithm> algorithms = null;
        
        if (this.ui.isServices()) {
            // Web Services //
            CoreRetrievalService client = new CoreRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            algorithms = client.getQTLAlgorithmsWhereStudyId(studyId, type);
        } else
            // Hibernate //
            if (this.ui.getAddress() != Message.DB_2_0_0)   //ADD 23/11/10
                algorithms = new CoreRetrieval(this.ui.getAddress()).getQTLAlgorithmsWhereStudyId(studyId, type);

        if (algorithms == null)   //ADD 02/12/10
            algorithms = new Vector<Algorithm>();   //TODO
        return algorithms;
    }
}

/*  //BAD BAD BAD
    //eQTL - SNP-Gene (multiple SNPs from "cis-eQTL - Gene")
    protected List<EQTLTuple> getEQTLTuples(int genotypeFeatureId, List<Variation> variations, int expressionFeatureId, int transcriptId, int statisticId, int permutation) {
        List<EQTLTuple> tuples = new ArrayList<EQTLTuple>();

        for (int i=0 ; i<variations.size() ; i++) {
            Variation variation = variations.get(i);
            //if (this.ui.isServices()) {
                // Web Services //
            //    VariationRetrievalService client = new VariationRetrievalClient(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            //    snp = client.getSNPWhereId(variationIds.get(i));
            //} else
                // Hibernate //
            //    snp = new VariationRetrieval(this.ui.getAddress()).getSNPWhereId(variationIds.get(i));
            
            tuples.add(this.getEQTLTuple(variation.getName(), genotypeFeatureId, variation, expressionFeatureId, transcriptId, statisticId, permutation));
        }                              //Population name differs from "eQTL - SNP-Gene"

        return tuples;
    }
 */

/*
protected List<Integer> getEQTLAlgorithmIds(int populationId, int methodId, int genotypeFeatureId, int methylationFeatureId, int expressionFeatureId) {
    List<Integer> featureIds;
    
    if (this.ui.isServices()()) {
        // Web Services //
        PrimaryKeyRetrievalService client = new PrimaryKeyRetrievalClient(address).create();
        featureIds = client.getExternalAnalysisIds(populationId, methodId, genotypeFeatureId, methylationFeatureId, expressionFeatureId);
    } else
        // Hibernate //
        featureIds = new PrimaryKeyRetrieval().setAddress(address).getExternalAnalysisIds(populationId, methodId, genotypeFeatureId, methylationFeatureId, expressionFeatureId);

    return featureIds;
}*/

/*
protected Object[][] getMSEPopulations(int datasetId, int columnSize) {
    List<MatchedFeature> matchedFeatures = new ArrayList<MatchedFeature>();;
    Object[][] populations = this.initialData(columnSize + 2);
    
    if (this.ui.isServices()()) {
        // Web Services //
        PrimaryKeyRetrievalService client = new PrimaryKeyRetrievalClient(address).create();
        matchedFeatures = client.getMSEMatchedFeatures(datasetId);
    } else
        // Hibernate //
        matchedFeatures = new PrimaryKeyRetrieval(address).getMSEMatchedFeatures(datasetId);
    
    if (matchedFeatures != null && matchedFeatures.size() != 0 && datasetId != 0) {   // new PrimaryKeyRetrieval().getPopulationSamples(platformId) returns not null
        populations = new Object[matchedFeatures.size()][columnSize + 2];
        
        for (int i=0 ; i<matchedFeatures.size() ; i++) {
            MatchedFeature populationSample = matchedFeatures.get(i);

            populations[i][0] = true;
            populations[i][1] = populationSample.getPopulationName();
            populations[i][2] = populationSample.getExpressionFeatureDescription();
            populations[i][3] = populationSample.getGenotypeFeatureDescription();   //TODO
            populations[i][4] = populationSample.getExpressionFeatureId();
            populations[i][5] = populationSample.getGenotypeFeatureId();            //TODO
        }   
    }
    
    return populations;
}*/
/**
 * ModificationRetrieval
 *//*   //TODO
protected Modification getCpG(int datasetId, String probeId) {
    Modification cpg;
    
    if (this.ui.isServices()()) {
        // Web Services //
        ModificationRetrievalService client = new ModificationRetrievalClient(address).create();
        cpg = client.getCpG(datasetId, probeId);
    } else
        // Hibernate //
        cpg = new ModificationRetrieval().getCpG(datasetId, probeId);

    return cpg;
}
*/