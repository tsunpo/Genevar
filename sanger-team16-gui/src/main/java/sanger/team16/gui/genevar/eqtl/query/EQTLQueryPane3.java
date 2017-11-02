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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import sanger.team16.common.business.dao.Tuple;
import sanger.team16.common.business.dao.MatchedFeature;
import sanger.team16.common.hbm.TranscriptMapping;
import sanger.team16.common.hbm.Variation;
import sanger.team16.gui.genevar.UI;
import sanger.team16.gui.genevar.eqtl.EQTLAnalysisPane;
import sanger.team16.gui.jface.BaseJPane;
import sanger.team16.gui.jface.table.BaseJTable;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class EQTLQueryPane3 extends EQTLAnalysisPane implements ActionListener
{   
    //private boolean filteredMissingGenotype = true;
  
    public EQTLQueryPane3(UI ui, BaseJTable matchedFeatureTable, Variation snp, BaseJTable transcriptTable, int statisticId, int permutation) {
        super(ui);
        
        for (int i=0 ; i<transcriptTable.getRowCount() ; i++) {
            if (((Boolean) transcriptTable.getValueAt(i, 0)).booleanValue()) {
                TranscriptMapping transcriptMapping = (TranscriptMapping) transcriptTable.getValueAt(i, 9);   //TODO
                String probeId = transcriptMapping.getProbeId();
                String ensemblGene = transcriptMapping.getEnsemblGene();
                String geneSymbol = transcriptMapping.getGeneSymbol();
                int transcriptId = transcriptMapping.getTranscriptId();
                
                String title = snp.getName() + " / " + probeId + " / " + ensemblGene + " / " + geneSymbol;
                
                this.setSNPGenePanel(title, matchedFeatureTable, snp, transcriptId, statisticId, permutation);
                this.setBlankPanel();
            }
        }
        //this.setBlankPanel();
        this.setRemovePanel(this);
    }

    /**
     * eQTL - SNP-Gene (from cis-eQTL - SNP)
     */    
    public EQTLQueryPane3(UI ui, String title, BaseJTable matchedFeatureTable, Variation snp, int transcriptId, int statisticId, int permutation) {
        super(ui);

        this.setSNPGenePanel(title, matchedFeatureTable, snp, transcriptId, statisticId, permutation);
        this.setBlankPanel();
        this.setRemovePanel(this);
    }

    /**
     * eQTL - SNP-Gene (from cis-eQTL - Gene)
     */
    public EQTLQueryPane3(UI ui, String title, MatchedFeature matchedFeature, List<Variation> snps, int transcriptId, int statisticId, int permutation) {
        super(ui);

        this.setSNPProbePanel(title, matchedFeature, snps, transcriptId, statisticId, permutation);
        this.setBlankPanel();
        this.setRemovePanel(this);
    }
    
    public EQTLQueryPane3(UI ui, String variationName) {
        super(ui);
        
        this.setErrorPanel(variationName);
        this.setBlankPanel();
        this.setRemovePanel(this);
    }
    
    /**
     * eQTL - SNP-Gene
     */
    private void setSNPGenePanel(String title, BaseJTable matchedFeatureTable, Variation snp, int transcriptId, int statisticId, int permutation) {
        BaseJPane panel = new BaseJPane(title,0,0,0,0);

        //EQTLStats eqtlStats = new TempEQTLStats(ui.isServices(), ui.getAddress());
        List<Tuple> tuples = new ArrayList<Tuple>();
        for (int i=0 ; i<matchedFeatureTable.getRowCount() ; i++) {
            if (((Boolean) matchedFeatureTable.getValueAt(i, 0)).booleanValue()) {
                String populationName = (String) matchedFeatureTable.getValueAt(i, 1);
                MatchedFeature matchedFeature = (MatchedFeature) matchedFeatureTable.getValueAt(i, 4);
                
                tuples.add(this.getEQTLTuple(populationName, matchedFeature, snp, transcriptId, statisticId, permutation));
            }
        }

        panel.add(new SNPGeneAssocPlot(tuples, 0, 0));   //eqtlStats.max, eqtlStats.min));
        
        panel.setBaseSpringBox();
        this.add(panel);
    }

    /**
     * eQTL - SNP-Gene (from cis-eQTL - Gene)
     */
    private void setSNPProbePanel(String title, MatchedFeature matchedFeature, List<Variation> snps, int transcriptId, int statisticId, int permutation) {
        BaseJPane panel = new BaseJPane(title,0,0,0,0);

        //EQTLStats eqtlStats = new EQTLStats(ui.isServices(), ui.getAddress());
        List<Tuple> tuples = this.getEQTLTuples(matchedFeature, snps, transcriptId, statisticId, permutation);

        panel.add(new SNPGeneAssocPlot(tuples, 0, 0));   //eqtlStats.max, eqtlStats.min));               

        panel.setBaseSpringBox();
        this.add(panel);
    }
    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == bRemove)
            this.ui.removeCurrentNode();
    }
}

/*
private double initialseMax(Object[][] populations, int transcriptId, int variationId, String allele) {
    double max = 0;
    QTLRetrieval qtlRetrieval = new QTLRetrieval();
    
    for (int i=0 ; i<populations.length ; i++) {
        if (((Boolean) populations[i][0]).booleanValue()) {
            Integer populationExpressionId = (Integer) populations[i][4];
            Integer populationGenotypeId = (Integer) populations[i][5];
            //System.out.println(populationExpressionId + "\t" + populationGenotypeId + "\t" + transcriptId+ "\t" + variationId);
            
            QTL qtl = qtlRetrieval.getQTL(populationExpressionId, transcriptId, populationGenotypeId, variationId, allele, filteredMissingGenotype, orderedByExpression);  
            if (qtl.expressions.length != 0) {
                max = qtl.expressions[0];
                break;
            }
        }
    }
    
    return max;
}
*/