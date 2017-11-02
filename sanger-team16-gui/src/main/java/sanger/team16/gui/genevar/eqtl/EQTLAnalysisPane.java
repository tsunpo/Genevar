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
package sanger.team16.gui.genevar.eqtl;

import java.util.ArrayList;
import java.util.List;

import sanger.team16.common.business.CoreRetrieval;
import sanger.team16.common.business.GeneRetrieval;
import sanger.team16.common.business.EQTLRetrieval;
import sanger.team16.common.business.dao.Tuple;
import sanger.team16.common.business.dao.MatchedFeature;
import sanger.team16.common.hbm.TranscriptMapping;
import sanger.team16.common.hbm.Variation;
import sanger.team16.common.xqtl.QTL;
import sanger.team16.common.xqtl.QTLList;
import sanger.team16.gui.genevar.AbstractAnalysisPane;
import sanger.team16.gui.genevar.Input;
import sanger.team16.gui.genevar.UI;
import sanger.team16.service.CoreRetrievalService;
import sanger.team16.service.GeneRetrievalService;
import sanger.team16.service.EQTLRetrievalService;
import sanger.team16.service.client.CoreRetrievalFactory;
import sanger.team16.service.client.GeneRetrievalFactory;
import sanger.team16.service.client.EQTLRetrievalFactory;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
@SuppressWarnings("serial")
public class EQTLAnalysisPane extends AbstractAnalysisPane 
{
    protected String[] distances = {"1,000,000"};
    protected String[] pValues = {"1e-05", "1e-04", "0.001", "0.01", "0.1", "No limit"};

    public EQTLAnalysisPane(UI ui) {
        super(ui);
    }
    
    /**
     * GeneRetrieval
     */
    protected List<TranscriptMapping> getGenes(int platformId, int referenceId, List<Input> inputs, boolean matched) {
        List<String> queries = new ArrayList<String>();
        if (inputs != null)
            for (int i=0 ; i<inputs.size() ; i++)
                queries.add(inputs.get(i).query);
        
        if (this.ui.isServices()) {
            // Web Services //
            GeneRetrievalService client = new GeneRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            return client.getGenes(platformId, referenceId, queries, matched);     
        } else
            // Hibernate //
            return new GeneRetrieval(this.ui.getAddress()).getGenes(platformId, referenceId, queries, matched);
    }
    
    protected List<TranscriptMapping> getTranscriptMappings(int platformId, int referenceId, String gene) {
        if (this.ui.isServices()) {
            // Web Services //
            GeneRetrievalService client = new GeneRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            return client.getTranscripts(platformId, referenceId, gene);
        } else
            // Hibernate //
            return new GeneRetrieval(this.ui.getAddress()).getTranscripts(platformId, referenceId, gene);
    }
    
    protected List<TranscriptMapping> getTranscriptMappingsWhereSNP(int platformId, int referenceId, Variation snp, int distance) {        
    	if (this.ui.isServices()) {
            // Web Services //
            GeneRetrievalService client = new GeneRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            return client.getTranscriptsWhereSNP(platformId, referenceId, snp, distance);
        } else
            // Hibernate //
            return new GeneRetrieval(this.ui.getAddress()).getTranscriptsWhereSNP(platformId, referenceId, snp, distance);
    }
    
    /**
     * CoreRetrieval
     */
    protected List<MatchedFeature> getMatchedFeaturesGxE(int studyId) {
        if (this.ui.isServices()) {
            // Web Services //
            CoreRetrievalService client = new CoreRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            return client.getMatchedFeaturesGxE(studyId);
        } else
            // Hibernate //
            return new CoreRetrieval(this.ui.getAddress()).getMatchedFeaturesGxE(studyId);
    }

    /**
     * eQTL Analysis
     * cis-eQTL - Gene
     */
    protected QTLList getEQTLsWhereGene(int studyId, MatchedFeature matchedFeature, int transcriptId, String chromosome, int transcriptionStartSite, int statisticId, double threshold, int distance) {
        QTLList qtls = new QTLList();   //ADD 30/11/11 Initialise qtlList
        int genotypeFeatureId = matchedFeature.getGenotypeFeatureId();   //ADDED 08/12/11
        int expressionFeatureId = matchedFeature.getExpressionFeatureId();   //ADD 13/12/11 Saint Lucy's Day!
        
        if (this.ui.isServices()) {
            // Web Services //
            EQTLRetrievalService client = new EQTLRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            qtls = client.getEQTLsWhereGene(studyId, genotypeFeatureId, expressionFeatureId, transcriptId, chromosome, transcriptionStartSite, statisticId, threshold, distance);
        } else
            // Hibernate //
            qtls = new EQTLRetrieval(this.ui.getAddress()).getEQTLsWhereGene(studyId, genotypeFeatureId, expressionFeatureId, transcriptId, chromosome, transcriptionStartSite, statisticId, threshold, distance);
        
        //if (qtlList.significances.isEmpty() && qtlList.nonSignificances.isEmpty())   //ADD 01/12/10
            //return null;   //REMOVE 30/11/11
        return qtls;
    }
    
    /**
     * cis-eQTL - SNP
     */
    protected List<QTL> getEQTLsWhereSNP(MatchedFeature matchedFeature, Variation snp, List<TranscriptMapping> transcriptMappings, int statisticId, double threshold) {
        List<QTL> eqtls = new ArrayList<QTL>();
        int genotypeFeatureId = matchedFeature.getGenotypeFeatureId(this.ui.getAddress(), snp);
        int expressionFeatureId = matchedFeature.getExpressionFeatureId();
        
        if (this.ui.isServices()) {
            // Web Services //
            EQTLRetrievalService client = new EQTLRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            eqtls = client.getEQTLsWhereSNP(genotypeFeatureId, snp, expressionFeatureId, transcriptMappings, statisticId, threshold);
        } else
            // Hibernate //
            eqtls = new EQTLRetrieval(this.ui.getAddress()).getEQTLsWhereSNP(genotypeFeatureId, snp, expressionFeatureId, transcriptMappings, statisticId, threshold);
        
        //if (eqtls.isEmpty())   //ADD 01/12/10, REMOVE 03/12/10
        //    return null;       //REMOVE 03/12/10
        return eqtls;
    }
    
    /**
     * eQTL - SNP-Gene
     */
    protected Tuple getEQTLTuple(String populationName, MatchedFeature matchedFeature, Variation snp, int transcriptId, int statisticId, int permutation) {
        Tuple tuple;
        int genotypeFeatureId = matchedFeature.getGenotypeFeatureId(this.ui.getAddress(), snp);
        int expressionFeatureId = matchedFeature.getExpressionFeatureId();   //ADD 13/12/11 Saint Lucy's Day!
        
        if (this.ui.isServices()) {
            // Web Services //
            EQTLRetrievalService client = new EQTLRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            tuple = client.getEQTLTuple(genotypeFeatureId, snp, expressionFeatureId, transcriptId, statisticId, permutation);
        } else
            // Hibernate //
            tuple = new EQTLRetrieval(this.ui.getAddress()).getEQTLTuple(genotypeFeatureId, snp, expressionFeatureId, transcriptId, statisticId, permutation);
        tuple.setPopulationName(populationName);

        return tuple;
    }
    
    /**
     * eQTL - SNP-Gene (For "cis-eQTL - Gene")
     */
    protected List<Tuple> getEQTLTuples(MatchedFeature matchedFeature, List<Variation> snps, int transcriptId, int statisticId, int permutation) {
        List<Tuple> tuples = new ArrayList<Tuple>();
        int genotypeFeatureId = matchedFeature.getGenotypeFeatureId(this.ui.getAddress(), snps.get(0));
        int expressionFeatureId = matchedFeature.getExpressionFeatureId();   //ADD 13/12/11 Saint Lucy's Day!
        
        if (this.ui.isServices()) {
            // Web Services //
            EQTLRetrievalService client = new EQTLRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
           
            for (int i=0 ; i<snps.size() ; i++) {
                Variation snp = snps.get(i);
                
                Tuple tuple = client.getEQTLTuple(genotypeFeatureId, snp, expressionFeatureId, transcriptId, statisticId, permutation);           
                tuple.setPopulationName(snp.getName());   //Population name differs from "eQTL - SNP-Gene"
                
                tuples.add(tuple);
            }
            
        } else {
            // Hibernate //
            for (int i=0 ; i<snps.size() ; i++) {
                Variation snp = snps.get(i);
                
                Tuple tuple = new EQTLRetrieval(this.ui.getAddress()).getEQTLTuple(genotypeFeatureId, snp, expressionFeatureId, transcriptId, statisticId, permutation);
                tuple.setPopulationName(snp.getName());
                
                tuples.add(tuple);
            }
        }
        
        return tuples;
    }
}
