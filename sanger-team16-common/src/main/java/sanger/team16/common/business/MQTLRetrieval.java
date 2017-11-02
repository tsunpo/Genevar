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
package sanger.team16.common.business;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import sanger.team16.common.business.dao.Tuple;
import sanger.team16.common.business.dao.Statistic;
import sanger.team16.common.business.dao.TupleDAO;
import sanger.team16.common.hbm.Analysis;
import sanger.team16.common.hbm.AnalysisFeature;
import sanger.team16.common.hbm.ModificationMapping;
import sanger.team16.common.hbm.Variation;
import sanger.team16.common.hbm.VariationInfo;
import sanger.team16.common.hbm.dao.AnalysisDAO;
import sanger.team16.common.hbm.dao.AnalysisFeatureDAO;
import sanger.team16.common.hbm.dao.VariationDAO;
import sanger.team16.common.hbm.dao.VariationInfoDAO;
import sanger.team16.common.xqtl.QTL;
import sanger.team16.common.xqtl.QTLList;
import sanger.team16.common.xqtl.QTLStatistics;
import sanger.team16.service.MQTLRetrievalService;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 * @date   21/09/13
 */
@WebService(endpointInterface = "sanger.team16.service.MQTLRetrievalService", serviceName = "MQTLRetrieval")
public class MQTLRetrieval extends AbstractRetrieval implements MQTLRetrievalService
{  
    public MQTLRetrieval() {
        super();
    }
    
    public MQTLRetrieval(String address) {
        super(address);
    }

    /**
     * cis-mQTL - Gene
     */
    @WebMethod
    public QTLList getMQTLsWhereCpG(int studyId, int genotypeFeatureId, int methylationFeatureId, int modificationId, String chromosome, int probeStart, int statisticId, double threshold, int distance) {        
        QTLList qtls = new QTLList();
        List<Variation> snps = new VariationDAO(address).list(studyId, chromosome, probeStart, distance);

        //if (snps != null) {
        if (snps != null && snps.size() != 0) {   //ADD 09/06/12 - Caused by: java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        	genotypeFeatureId = this.getGenotypeFeatureId(genotypeFeatureId, snps.get(0));   //ADD 13/12/11
        	
            /**
             * If pre-upload from external algorithms is available
             */
            if (statisticId > Statistic.EXTERNAL_ALGORITHM) {
                List<AnalysisFeature> analysisFeatures = new AnalysisFeatureDAO(address).listMQTL(statisticId, genotypeFeatureId, methylationFeatureId);
                //AnalysisDAO analysisDAO = new AnalysisDAO(address);   //REMOVED 23/09/13 //ADD 23/11/10
                
                for (int i=0 ; i<snps.size() ; i++) {
                    Variation snp = snps.get(i);
                    VariationInfo snpInfo = new VariationInfoDAO(address).list(snp.getId());
                    snp.setInfo(snpInfo.getInfo());
                    snp.setFreq1(snpInfo.getFreq1());
                    
                    if (statisticId == Statistic.EXTERNAL_ALGORITHM_ProbABEL) {
                        if (analysisFeatures != null) {
                            Analysis analysis = new AnalysisDAO(address).listQTLProbABEL(analysisFeatures.get(0).getId(), modificationId, snp.getId());  //TODO

                            if (analysis != null) {
                                double pValue = analysis.getValue();
                                
                                if (pValue <= threshold)
                                    qtls.significances.add(new QTL(snp, analysis.getValue1(), analysis.getValue2(), analysis.getValue3(), analysis.getValue4(), pValue));
                                else
                                    qtls.insignificances.add(new QTL(snp, pValue));               
                            }
                        }
                    }
                }
                
            /**
             * Otherwise, calculate it on-the-fly
             */
            } else {
            	//TupleDAO tupleDAO = new TupleDAO(address);   //REMOVED 23/09/13 //ADD 15/06/12
            	
                for (int i=0 ; i<snps.size() ; i++) {
                    Variation snp = snps.get(i);
                    Tuple tuple = new TupleDAO(address).getTupleGxM(genotypeFeatureId, snp.getId(), methylationFeatureId, modificationId);

                    QTLStatistics stats = new QTLStatistics();
                    if (stats.calculate(tuple, snp.getAlleles(), statisticId, threshold, 0)) {
                        if (stats.nominalP <= threshold) {
                           	if (statisticId == Statistic.SPEARMANS)
                                qtls.significances.add(new QTL(snp, stats.correlation, stats.nominalP));
                            else if (statisticId == Statistic.LINEAR)
                                qtls.significances.add(new QTL(snp, stats.correlation, stats.nominalP, stats.adjRSq, stats.gradient));
                        } else
                            qtls.insignificances.add(new QTL(snp, stats.nominalP));
                    }
                }
            }
        }

        return qtls;
    }
    
    /**
     * cis-mQTL - SNP
     */
    @WebMethod
    public List<QTL> getMQTLsWhereSNP(int genotypeFeatureId, Variation snp, int methylationFeatureId, List<ModificationMapping> modificationMappings, int statisticId, double threshold) {
        List<QTL> qtls = new ArrayList<QTL>();

        if (modificationMappings != null) {
            /**
             * If pre-upload from external algorithms is available
             */
            if (statisticId > Statistic.EXTERNAL_ALGORITHM) {
                List<AnalysisFeature> analysisFeatures = new AnalysisFeatureDAO(address).listMQTL(statisticId, genotypeFeatureId, methylationFeatureId);
                //AnalysisDAO analysisDAO = new AnalysisDAO(address);   //REMOVED 23/09/13 //ADD 23/11/10
                
                for (int i=0 ; i<modificationMappings.size() ; i++) {
                	ModificationMapping modificationMapping = modificationMappings.get(i);
                    int transcriptId = modificationMapping.getModificationId();   //transcriptId
                    
                    if (statisticId == Statistic.EXTERNAL_ALGORITHM_ProbABEL) {
                        if (analysisFeatures != null) {
                            Analysis analysis = new AnalysisDAO(address).listQTLProbABEL(analysisFeatures.get(0).getId(), transcriptId, snp.getId());
                            if (analysis != null)
                            	qtls.add(new QTL(modificationMapping, analysis.getValue()));
                        }
                    }
                }
                
            /**
             * Otherwise, calculate it on-the-fly
             */
            } else {
            	//TupleDAO tupleDAO = new TupleDAO(address);   //REMOVED 23/09/13 //ADD 15/06/12

                for (int i=0 ; i<modificationMappings.size() ; i++) {
                	ModificationMapping modificationMapping = modificationMappings.get(i);
                    Tuple tuple = new TupleDAO(address).getTupleGxM(genotypeFeatureId, snp.getId(), methylationFeatureId, modificationMapping.getModificationId());
 
                    QTLStatistics stats = new QTLStatistics();
                    if (stats.calculate(tuple, snp.getAlleles(), statisticId, threshold, 0))
                        qtls.add(new QTL(modificationMapping, stats.nominalP));
                }
            }
        }

        return qtls;
    }

    /*
     * When GenotypeFeature.parent_id != 0 or for Database 2.0.0
     */
    private int getGenotypeFeatureId(int genotypeFeatureId, Variation snp) {   //TODO
        if (address.equals("tpy_team16_genevar_2_0_0.cfg.xml") || snp.getGenotypeFeatureId() == 0)
        	return genotypeFeatureId;
        return snp.getGenotypeFeatureId();   //BUG 08/12/11
    }
}
