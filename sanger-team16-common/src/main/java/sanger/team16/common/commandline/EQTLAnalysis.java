/**
 *  This file is part of Genevar (GENe Expression VARiation)
 *  Copyright (C) 2012  Genome Research Ltd.
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
package sanger.team16.common.commandline;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import sanger.team16.common.business.GeneRetrieval;
import sanger.team16.common.business.dao.MatchedFeature;
import sanger.team16.common.business.dao.Statistic;
import sanger.team16.common.business.dao.Tuple;
import sanger.team16.common.business.dao.TupleDAO;
import sanger.team16.common.hbm.Analysis;
import sanger.team16.common.hbm.AnalysisFeature;
import sanger.team16.common.hbm.TranscriptMapping;
import sanger.team16.common.hbm.Variation;
import sanger.team16.common.hbm.VariationInfo;
import sanger.team16.common.hbm.dao.AnalysisDAO;
import sanger.team16.common.hbm.dao.AnalysisFeatureDAO;
import sanger.team16.common.hbm.dao.TranscriptMappingDAO;
import sanger.team16.common.hbm.dao.VariationDAO;
import sanger.team16.common.hbm.dao.VariationInfoDAO;
import sanger.team16.common.xqtl.QTLStatistics;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 * @date   23/11/12
 */
public class EQTLAnalysis extends AbstractAnalysis
{
    public EQTLAnalysis(String address, Map<String, String> argMap, String mode) throws IOException {
        super(address, argMap, mode);
    }
    
    /**
     * SNP-centred cis analysis
     * @param java -jar genevar.jar -cis-eqtl snp -study cardio -type mono -ref cardio -a src -d 1000000 -p 0.001 -f cad_snp.txt 
     * @throws IOException 
     */
    public void cisSNP() throws IOException {
        for (int m=0 ; m<matchedFeatures.size() ; m++) {
            MatchedFeature matchedFeature = matchedFeatures.get(m);
            int platformId = matchedFeature.getPlatformId();
            //int genotypeFeatureId = matchedFeature.getGenotypeFeatureId();   //ADD 25/11/12
            int expressionFeatureId = matchedFeature.getExpressionFeatureId();

            if (statisticId == Statistic.EXTERNAL_ALGORITHM_ProbABEL)
                setOutputProbABLE(matchedFeature);
            else
                setOutput(matchedFeature);
            
            for (int i=0 ; i<lines.size() ; i++) {
                String[] st = lines.get(i).split("\t");
                Variation snp = new VariationDAO(address).uniqueResult(studyId, st[0]);

                if (snp != null) {
                   	int genotypeFeatureId = snp.getGenotypeFeatureId(address, matchedFeature.getGenotypeFeatureId());   //ADD 25/11/12
                   	//System.out.println("matchedFeature.getGenotypeFeatureId():\t" + matchedFeature.getGenotypeFeatureId());
                   	//System.out.println("snp.getGenotypeFeatureId():\t" + snp.getGenotypeFeatureId());   	
                   	//System.out.println("genotypeFeatureId:\t" + genotypeFeatureId);
                   	// Tres impotant!!!
                   	// matchedFeature.getGenotypeFeatureId():	12
                   	// snp.getGenotypeFeatureId():	15
                   	// genotypeFeatureId:	15
                	
                    List<TranscriptMapping> transcriptMappings = new GeneRetrieval(address).getTranscriptsWhereSNP(platformId, referenceId, snp, distance);
                    VariationInfo snpInfo = new VariationInfoDAO(address).list(snp.getId());
                    snp.setInfo(snpInfo.getInfo());
                    snp.setFreq1(snpInfo.getFreq1());
                    
                    //TODO for cmdlind 23/11/12 (same as EQTLRetrieval)
                    statisticId = 1;   //ProbABEL
                    List<AnalysisFeature> analysisFeatures = new AnalysisFeatureDAO(address).listEQTL(statisticId, genotypeFeatureId, expressionFeatureId);
                    AnalysisDAO analysisDAO = new AnalysisDAO(address);   //ADD 23/11/10
                    
                    for (int t=0 ; t<transcriptMappings.size() ; t++) {
                        TranscriptMapping transcriptMapping = transcriptMappings.get(t);
                        int transcriptId = transcriptMapping.getTranscriptId();   //transcriptId

                        if (statisticId == Statistic.EXTERNAL_ALGORITHM_ProbABEL) {
                            if (analysisFeatures != null) {
                                Analysis analysis = analysisDAO.listQTLProbABEL(analysisFeatures.get(0).getId(), transcriptId, snp.getId());
                                
                                //if (transcriptMapping.getProbeId().equalsIgnoreCase("ILMN_1659215"))
                                //    System.out.println("ILMN_1659215\n" + analysisFeatures.get(0).getId() + "\t" + transcriptId + "\t" + snp.getId());
                              
                                if (analysis != null) {
                                	outputProbABLE(analysis, snp, transcriptMapping);
                                	// TODO just for cmdline //23/11/12
                                	//qtls.add(new QTL(snp, analysis.getValue1(), analysis.getValue2(), analysis.getValue3(), analysis.getValue4(), analysis.getValue()));
                                	//qtls.add(new QTL(transcriptMapping, analysis.getValue()));
                                }
                            }
                        }
                    }

                    /*
                    for (int t=0 ; t<transcriptMappings.size() ; t++) {
                        TranscriptMapping transcriptMapping = transcriptMappings.get(t);
                        int transcriptId = transcriptMapping.getTranscriptId();
                        
                        Tuple tuple = new TupleDAO(address).getTupleGxE(genotypeFeatureId, snp.getId(), expressionFeatureId, transcriptId);
                        QTLStatistics stats = new QTLStatistics();
                        if (stats.calculate(tuple, snp.getAlleles(), statisticId, threshold, permutation))
                         	output(stats, snp, transcriptMapping);
                    }
                        */
                } else {}
            }
            
            this.close();
        }
    }
    
    /**
     * Gene-centred cis-eQTL analysis
     * @param java -jar genevar.jar -cis-eqtl gene -study cardio -type mono -ref cardio -a src -d 1000000 -p 0.001 -f cad_gene.txt
     * @throws IOException 
     */
    public void cisGene() throws IOException {
        for (int m=0 ; m<matchedFeatures.size() ; m++) {
            MatchedFeature matchedFeature = matchedFeatures.get(m);
            int platformId = matchedFeature.getPlatformId();
            int genotypeFeatureId = matchedFeature.getGenotypeFeatureId();
            int expressionFeatureId = matchedFeature.getExpressionFeatureId();
            
            setOutput(matchedFeature);
            
            if (argMap.containsKey(Argument.FILE)) {
                for (int i=0 ; i<lines.size() ; i++) {
                    String[] st = lines.get(i).split("\t");
                    
                    List<TranscriptMapping> transcriptMappings = new TranscriptMappingDAO(address).list(platformId, referenceId, st[0]);
                    this.cisGeneAnalysis(genotypeFeatureId, expressionFeatureId, transcriptMappings);
                }
            } else {
                List<TranscriptMapping> transcriptMappings = checkEmptyTranscripts(getTranscriptMappings(platformId));
                this.cisGeneAnalysis(genotypeFeatureId, expressionFeatureId, transcriptMappings);
            }
            
            this.close();
        }
    }
    
    private void cisGeneAnalysis(int genotypeFeatureId, int expressionFeatureId, List<TranscriptMapping> transcriptMappings) throws IOException {
        if (transcriptMappings != null) {
        	//List<Variation> snps = new ArrayList<Variation>();   //BUG 22/08/11
        	int statisticId = this.getStatisticId();

            for (int t=0 ; t<transcriptMappings.size() ; t++) {
                TranscriptMapping transcriptMapping = transcriptMappings.get(t);
                int transcriptId = transcriptMapping.getTranscriptId();

                List<Variation> snps = new VariationDAO(address).list(studyId, transcriptMapping.getChromosome(), transcriptMapping.getTranscriptionStartSite(), distance);

                for (int i=0 ; i<snps.size() ; i++) {
                    Variation snp = snps.get(i);
                    Tuple tuple = new TupleDAO(address).getTupleGxE(genotypeFeatureId, snp.getId(), expressionFeatureId, transcriptId);

                    QTLStatistics stats = new QTLStatistics();
                    if (stats.calculate(tuple, snp.getAlleles(), statisticId, threshold, 0))
                     	output(stats, snp, transcriptMapping);
                }
            }
        }
    }
    
    private List<TranscriptMapping> getTranscriptMappings(int platformId) {
        if (argMap.get(Argument.CHR).toUpperCase().equals("ALL"))
            return new TranscriptMappingDAO(address).listAll(platformId, referenceId);
        else
            return new TranscriptMappingDAO(address).listWhereChromosome(platformId, referenceId, argMap.get(Argument.CHR));
    }

    private List<TranscriptMapping> checkEmptyTranscripts(List<TranscriptMapping> transcriptMappings) {
        if (transcriptMappings == null || transcriptMappings.size() == 0)
            if (argMap.containsKey(Argument.CHR))
                showCommandLineErrorMessage("Cannot find matched transcripts for chromosome '" + argMap.get(Argument.CHR) + "'");
            else
                showCommandLineErrorMessage("Cannot find matched transcripts for study '" + argMap.get(Argument.STUDY) + "' and reference '" + argMap.get(Argument.REFERENCE) + "'");
        
        return transcriptMappings;
    }
    
    /**
     * SNP-centred trans analysis
     * Usage: java -jar genevar.jar -trans snp -study dimas -ref dimas -stats src -p 0.001 -perms 10000 -snpFile dp5.txt
     */
    public void transSNP() throws IOException {
        for (int m=0 ; m<matchedFeatures.size() ; m++) {
            MatchedFeature matchedFeature = matchedFeatures.get(m);
            int platformId = matchedFeature.getPlatformId();
            int genotypeFeatureId = matchedFeature.getGenotypeFeatureId();
            int expressionFeatureId = matchedFeature.getExpressionFeatureId();
            List<TranscriptMapping> transcriptMappings = new TranscriptMappingDAO(address).listAll(platformId, referenceId);
            
            setOutput(matchedFeature);

            for (int i=0 ; i<lines.size() ; i++) {
                String[] st = lines.get(i).split("\t");
                Variation snp = new VariationDAO(address).uniqueResult(studyId, st[0]);

                if (snp != null) {
                    for (int t=0 ; t<transcriptMappings.size() ; t++) {
                        TranscriptMapping transcriptMapping = transcriptMappings.get(t);
                        int transcriptId = transcriptMapping.getTranscriptId();
                        Tuple tuple = new TupleDAO(address).getTupleGxE(genotypeFeatureId, snp.getId(), expressionFeatureId, transcriptId);

                        QTLStatistics stats = new QTLStatistics();
                        if (stats.calculate(tuple, snp.getAlleles(), statisticId, threshold, permutation))
                        	output(stats, snp, transcriptMapping);
                    }
                    
                } else {}
            }
        }
    	
        this.close();
    }

    private void setOutput(MatchedFeature matchedFeature) throws IOException {
        String filename = getFileName(matchedFeature.getPopulationName());
        
    	out = this.getFileWriter(filename, "txt");
        log = this.getFileWriter(filename, "log");
        
        out.write("SNP_ID\tProbe_Id\tEnsembl_Gene\tGene_Symol\tSNP_chr\tGene_chr\tSNP_Position\tGene_Position\tDistance\trho\tP_value\tlog10P\tP_permuted\n");
        log.write("Genotype: " + matchedFeature.getGenotypeFeatureDescription() + "\n");
        log.write("Expression: " + matchedFeature.getExpressionFeatureDescription() + "\n");
        //log.write("Reference: " + argMap.get(Argument.REFERENCE) + " with " + transcriptMappings.size() + " probes");
    }

    private void output(QTLStatistics stats, Variation snp, TranscriptMapping transcriptMapping) throws IOException {
        if (stats.nominalP <= threshold) {
            out.write(snp.getName() + "\t" + transcriptMapping.getProbeId() + "\t" + transcriptMapping.getEnsemblGene() + "\t" + transcriptMapping.getGeneSymbol() + "\t" + snp.getChromosome() + "\t" + transcriptMapping.getChromosome() + "\t" + snp.getPosition() + "\t" + transcriptMapping.getTranscriptionStartSite()+ "\t" + Math.abs(snp.getPosition() - transcriptMapping.getTranscriptionStartSite()) + "\t");
            out.write(stats.correlation + "\t" + stats.nominalP + "\t" + getMinusLog10P(stats.nominalP));
            
            if (permutation != 0)
                out.write("\t" + stats.empiricalP + "\n");
            else
                out.write("\t\t\n");
        }// else
         //   out.write(snp.getName() + "\t\t\t\t\t\t\t\t\t\t\t\t\n");
    }
    
    //TODO
    private void setOutputProbABLE(MatchedFeature matchedFeature) throws IOException {
        String filename = getFileName(matchedFeature.getPopulationName());
        
    	out = this.getFileWriter(filename, "txt");
        log = this.getFileWriter(filename, "log");
        
        out.write("SNP_ID\tProbe_Id\tEnsembl_Gene\tGene_Symol\tSNP_chr\tGene_chr\tSNP_Position\tGene_Position\tDistance\tA1\tINFO\tFreq1\tmean_predictor_allele\tbeta_SNP\tsebeta_SNP\tchi\tnominalP\n");
        log.write("Genotype: " + matchedFeature.getGenotypeFeatureDescription() + "\n");
        log.write("Expression: " + matchedFeature.getExpressionFeatureDescription() + "\n");
        //log.write("Reference: " + argMap.get(Argument.REFERENCE) + " with " + transcriptMappings.size() + " probes");
    }

    private void outputProbABLE(Analysis analysis, Variation snp, TranscriptMapping transcriptMapping) throws IOException {
    	if (analysis.getValue() <= threshold) {
            out.write(snp.getName() + "\t" + transcriptMapping.getProbeId() + "\t" + transcriptMapping.getEnsemblGene() + "\t" + transcriptMapping.getGeneSymbol() + "\t" + snp.getChromosome() + "\t" + transcriptMapping.getChromosome() + "\t" + snp.getPosition() + "\t" + transcriptMapping.getTranscriptionStartSite()+ "\t" + Math.abs(snp.getPosition() - transcriptMapping.getTranscriptionStartSite()) + "\t");
            out.write(snp.getAlleles() + "\t" + snp.getInfo() + "\t" + snp.getFreq1() + "\t");
            out.write(analysis.getValue1() + "\t" + analysis.getValue2() + "\t" + analysis.getValue3() + "\t" + analysis.getValue4() + "\t" + analysis.getValue() + "\t" + getMinusLog10P(analysis.getValue()) + "\n");
            
            //if (permutation != 0)
            //    out.write("\t" + stats.empiricalP + "\n");
            //else
            //    out.write("\t\t\n");
        }// else
         //   out.write(snp.getName() + "\t\t\t\t\t\t\t\t\t\t\t\t\n");
    }
}
