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

import sanger.team16.common.business.dao.MatchedFeature;
import sanger.team16.common.business.dao.Tuple;
import sanger.team16.common.business.dao.TupleDAO;
import sanger.team16.common.hbm.ModificationMapping;
import sanger.team16.common.hbm.Variation;
import sanger.team16.common.hbm.dao.ModificationMappingDAO;
import sanger.team16.common.hbm.dao.VariationDAO;
import sanger.team16.common.xqtl.QTLStatistics;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class MQTLAnalysis extends AbstractAnalysis
{
    public MQTLAnalysis(String address, Map<String, String> argMap, String mode) throws IOException {
        super(address, argMap, mode);
    }

    /**
     * Gene-centred cis-meQTL analysis
     * @param java -jar genevar.jar -cis-meqtl gene -study cardio-expr -ref cardio -a src -d 1000000 -p 0.001 -file 450k_cardio_smoking_top100+lt3_genevar.txt
     * @throws IOException 
     */
    public void cisGene() throws IOException {
        for (int m=0 ; m<matchedFeatures.size() ; m++) {
            MatchedFeature matchedFeature = matchedFeatures.get(m);
            int platformId = matchedFeature.getPlatformId();
            int genotypeFeatureId = matchedFeature.getGenotypeFeatureId();
            int methylationFeatureId = matchedFeature.getMethylationFeatureId();
            
            setOutput(matchedFeature);
            
            List<ModificationMapping> modificationMappings;
            if (argMap.containsKey(Argument.FILE)) {
                for (int i=0 ; i<lines.size() ; i++) {
                    String[] st = lines.get(i).split("\t");
                    
                    //System.out.println(platformId + "\t" + referenceId + "\t" + query + "\t" + modificationMappings.size());
                    modificationMappings = new ModificationMappingDAO(address).list(platformId, referenceId, st[0]);
                    this.cisGeneAnalysis(genotypeFeatureId, methylationFeatureId, modificationMappings);
                }
            } else {
                //modificationMappings = checkEmptyModificationMappings(getModificationMappings(platformId));
                //this.cisGeneAnalysis(genotypeFeatureId, methylationFeatureId, modificationMappings);
            }

            this.close();
        }
    }
    
    private void cisGeneAnalysis(int genotypeFeatureId, int methylationFeatureId, List<ModificationMapping> modificationMappings) throws IOException {
    	if (modificationMappings != null) {
        	//String currentGeneSymbol = "";   //BAD DESIGN
            //List<Variation> snps = new ArrayList<Variation>();
            
            for (int t=0 ; t<modificationMappings.size() ; t++) {
                ModificationMapping modificationMapping = modificationMappings.get(t);
                int modificationId = modificationMapping.getModificationId();

                List<Variation> snps = new VariationDAO(address).list(studyId, modificationMapping.getChromosome(), modificationMapping.getProbeStart(), distance);
            
                for (int i=0 ; i<snps.size() ; i++) {
                    Variation snp = snps.get(i);
                    Tuple tuple = new TupleDAO(address).getTupleGxM(genotypeFeatureId, snp.getId(), methylationFeatureId, modificationId);

                    QTLStatistics stats = new QTLStatistics();
                    if (stats.calculate(tuple, snp.getAlleles(), statisticId, threshold, permutation))
                     	output(stats, snp, modificationMapping);
                }
            }	
    	}
    }
    /*
    private List<ModificationMapping> getModifacationMappings(int platformId) {
        if (argMap.get(Argument.CHR).toUpperCase().equals("ALL"))
            return new ModificationMappingDAO(address).listAll(platformId, referenceId);
        else
            return new ModificationMappingDAO(address).listWhereChromosome(platformId, referenceId, argMap.get(Argument.CHR));
    }

    private List<ModificationMapping> checkEmptyModifications(List<ModificationMapping> modificationMappings) {
        if (modificationMappings == null || modificationMappings.size() == 0)
            if (argMap.containsKey(Argument.CHR))
                showCommandLineErrorMessage("Cannot find matched transcripts for chromosome '" + argMap.get(Argument.CHR) + "'");
            else
                showCommandLineErrorMessage("Cannot find matched transcripts for study '" + argMap.get(Argument.STUDY) + "' and reference '" + argMap.get(Argument.REFERENCE) + "'");
        
        return modificationMappings;
    }*/

    private void setOutput(MatchedFeature matchedFeature) throws IOException {
        String filename = getFileName(matchedFeature.getPopulationName());
    	
    	out = this.getFileWriter(filename, "txt");
        log = this.getFileWriter(filename, "log");
        
        out.write("SNP_ID\tProbe_Id\tCpg_Islands\tGene_Symol\tSNP_chr\tGene_chr\tSNP_Position\tGene_Position\tDistance\trho\tP_value\tlog10P\tP_permuted\n");
        log.write("Genotype: " + matchedFeature.getGenotypeFeatureDescription() + "\n");
        log.write("Methylation: " + matchedFeature.getMethylationFeatureDescription() + "\n");
        //log.write("Reference: " + argMap.get(Argument.REFERENCE) + " with " + transcriptMappings.size() + " probes");
    }
    
    private void output(QTLStatistics stats, Variation snp, ModificationMapping modificationMapping) throws IOException {
        if (stats.nominalP <= threshold) {
            out.write(snp.getName() + "\t" + modificationMapping.getProbeId() + "\t" + modificationMapping.getCpgIslands() + "\t" + modificationMapping.getGeneSymbol() + "\t" + snp.getChromosome() + "\t" + modificationMapping.getChromosome() + "\t" + snp.getPosition() + "\t" + modificationMapping.getProbeStart()+ "\t" + Math.abs(snp.getPosition() - modificationMapping.getProbeStart()) + "\t");
            out.write(stats.correlation + "\t" + stats.nominalP + "\t" + getMinusLog10P(stats.nominalP));
            
            if (permutation != 0)
                out.write("\t" + stats.empiricalP + "\n");
            else
                out.write("\t\t\n");
        }
    }
}

/*
//SNP-centred cis analysis
//@param java -jar genevar.jar -cis-meqtl snp -study cardio -type mono -ref cardio -a src -d 1000000 -p 0.001 -f cad_snp.txt 
//@throws IOException 
public void cisSNP() throws IOException {
    for (int m=0 ; m<matchedFeatures.size() ; m++) {
        MatchedFeature matchedFeature = matchedFeatures.get(m);
        int platformId = matchedFeature.getPlatformId();
        int genotypeFeatureId = matchedFeature.getGenotypeFeatureId();
        int methylationFeatureId = matchedFeature.getMethylationFeatureId();
        
        String filename = getFileName(matchedFeature.getPopulationName());
        setOutput(filename, matchedFeature);

        for (int i=0 ; i<lines.size() ; i++) {
            String[] st = lines.get(i).split("\t");
            Variation snp = new VariationDAO(address).uniqueResult(studyId, st[0]);

            if (snp != null) {
                //TODO
                //List<ModificationMapping> modificationMappings = new GeneRetrieval(address).getTranscriptsWhereSNP(platformId, referenceId, snp, distance);
                List<ModificationMapping> modificationMappings = new ModificationMappingDAO(address).listWhereSNP(platformId, referenceId, snp.getChromosome(), snp.getPosition(), distance);
                //modificationMappings.addAll(new ModificationMappingDAO(address).listWhereSNP(platformId, referenceId, snp.getChromosome(), snp.getPosition(), distance, -1));
                boolean hasEQTL = false;
                
                for (int t=0 ; t<modificationMappings.size() ; t++) {
                    ModificationMapping modificationMapping = modificationMappings.get(t);
                    int modificationId = modificationMapping.getModificationId();
                    Tuple tuple = new TupleDAO(address).getTupleGxM(genotypeFeatureId, snp.getId(), methylationFeatureId, modificationId);

                    QTLStatistics stats = new QTLStatistics();
                    if (stats.calculate(tuple, snp.getAlleles(), statisticId, threshold, permutation)) {
                        if (stats.nominalP <= threshold) {
                            out.write(snp.getName() + "\t" + modificationMapping.getProbeId() + "\t" + modificationMapping.getCpgIslands() + "\t" + modificationMapping.getGeneSymbol() + "\t" + snp.getChromosome() + "\t" + modificationMapping.getChromosome() + "\t" + snp.getPosition() + "\t" + modificationMapping.getProbeStart() + "\t" + Math.abs(snp.getPosition() - modificationMapping.getProbeStart()) + "\t");
                            out.write(stats.correlation + "\t" + stats.nominalP + "\t" + getMinusLog10P(stats.nominalP));
                            if (permutation != 0)
                                out.write("\t" + stats.empiricalP + "\n");
                            else
                                out.write("\t\t\n");
                            
                            hasEQTL = true;
                        }
                    }
                }
                
                if (!hasEQTL)
                    out.write(snp.getName() + "\t\t\t\t\t\t\t\t\t\t\t\t\n");
                
            } else {}
        }
        
        this.close();
    }
}
*/