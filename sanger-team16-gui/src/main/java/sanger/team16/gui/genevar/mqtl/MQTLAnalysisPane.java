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
package sanger.team16.gui.genevar.mqtl;

import java.util.ArrayList;
import java.util.List;

import sanger.team16.common.business.CoreRetrieval;
import sanger.team16.common.business.CpGRetrieval;
import sanger.team16.common.business.MQTLRetrieval;
import sanger.team16.common.business.dao.MatchedFeature;
import sanger.team16.common.hbm.ModificationMapping;
import sanger.team16.common.hbm.Variation;
import sanger.team16.common.xqtl.QTL;
import sanger.team16.common.xqtl.QTLList;
import sanger.team16.gui.genevar.AbstractAnalysisPane;
import sanger.team16.gui.genevar.Input;
import sanger.team16.gui.genevar.UI;
import sanger.team16.service.CoreRetrievalService;
import sanger.team16.service.CpGRetrievalService;
import sanger.team16.service.MQTLRetrievalService;
import sanger.team16.service.client.CoreRetrievalFactory;
import sanger.team16.service.client.CpGRetrievalFactory;
import sanger.team16.service.client.MQTLRetrievalFactory;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
@SuppressWarnings("serial")
public class MQTLAnalysisPane extends AbstractAnalysisPane 
{
    protected String[] distances = {"100,000"};
    protected String[] pValues = {"1e-05", "1e-04", "0.001", "0.01", "0.1", "No limit"};

    public MQTLAnalysisPane(UI ui) {
        super(ui);
    }
 
    /**
     * CpGRetrieval
     */
    protected List<ModificationMapping> getCpGs(int platformId, int referenceId, List<Input> inputs, boolean matched) {
        List<String> queries = new ArrayList<String>();
        if (inputs != null)
            for (int i=0 ; i<inputs.size() ; i++)
                queries.add(inputs.get(i).query);
        
        if (this.ui.isServices()) {
            // Web Services //
        	CpGRetrievalService client = new CpGRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            return client.getCpGs(platformId, referenceId, queries, matched);     
        } else
            // Hibernate //
            return new CpGRetrieval(this.ui.getAddress()).getCpGs(platformId, referenceId, queries, matched);
    }
    
    protected List<ModificationMapping> getModificationMappings(int platformId, int referenceId, String gene) {
        if (this.ui.isServices()) {
            // Web Services //
            CpGRetrievalService client = new CpGRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            return client.getModifications(platformId, referenceId, gene);
        } else
            // Hibernate //
            return new CpGRetrieval(this.ui.getAddress()).getModifications(platformId, referenceId, gene);
    }
    
    protected List<ModificationMapping> getModificationMappingsWhereSNP(int platformId, int referenceId, Variation snp, int distance) {        
    	if (this.ui.isServices()) {
            // Web Services //
            CpGRetrievalService client = new CpGRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            return client.getModificationsWhereSNP(platformId, referenceId, snp, distance);
        } else
            // Hibernate //
            return new CpGRetrieval(this.ui.getAddress()).getModificationsWhereSNP(platformId, referenceId, snp, distance);
    }
    
    /**
     * CoreRetrieval
     */
    protected List<MatchedFeature> getMatchedFeaturesGxM(int studyId) {
        if (this.ui.isServices()) {
            // Web Services //
            CoreRetrievalService client = new CoreRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            return client.getMatchedFeaturesGxM(studyId);
        } else
            // Hibernate //
            return new CoreRetrieval(this.ui.getAddress()).getMatchedFeaturesGxM(studyId);
    }
    
    /**
     * mQTL Analysis
     * cis-mQTL - Gene
     */
    protected QTLList getMQTLsWhereCpG(int studyId, MatchedFeature matchedFeature, int modificationId, String chromosome, int probeStart, int statisticId, double threshold, int distance) {
        QTLList qtls = new QTLList();
        int genotypeFeatureId = matchedFeature.getGenotypeFeatureId();
        int methylationFeatureId = matchedFeature.getMethylationFeatureId();
        
        if (this.ui.isServices()) {
            // Web Services //
            MQTLRetrievalService client = new MQTLRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            qtls = client.getMQTLsWhereCpG(studyId, genotypeFeatureId, methylationFeatureId, modificationId, chromosome, probeStart, statisticId, threshold, distance);
        } else
            // Hibernate //
            qtls = new MQTLRetrieval(this.ui.getAddress()).getMQTLsWhereCpG(studyId, genotypeFeatureId, methylationFeatureId, modificationId, chromosome, probeStart, statisticId, threshold, distance);
        
        //if (qtlList.significances.isEmpty() && qtlList.nonSignificances.isEmpty())   //ADD 01/12/10
            //return null;   //REMOVE 30/11/11
        return qtls;
    }
    
    /**
     * mQTL Analysis
     * cis-mQTL - SNP
     */
    protected List<QTL> getMQTLsWhereSNP(MatchedFeature matchedFeature, Variation snp, List<ModificationMapping> modificationMappings, int statisticId, double threshold) {
        List<QTL> mqtls = new ArrayList<QTL>();
        int genotypeFeatureId = matchedFeature.getGenotypeFeatureId(this.ui.getAddress(), snp);
        int methylationFeatureId = matchedFeature.getMethylationFeatureId();
        
        if (this.ui.isServices()) {
            // Web Services //
            MQTLRetrievalService client = new MQTLRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            mqtls = client.getMQTLsWhereSNP(genotypeFeatureId, snp, methylationFeatureId, modificationMappings, statisticId, threshold);
        } else
            // Hibernate //
            mqtls = new MQTLRetrieval(this.ui.getAddress()).getMQTLsWhereSNP(genotypeFeatureId, snp, methylationFeatureId, modificationMappings, statisticId, threshold);
        
        //if (eqtls.isEmpty())   //ADD 01/12/10, REMOVE 03/12/10
        //    return null;       //REMOVE 03/12/10
        return mqtls;
    }
}
