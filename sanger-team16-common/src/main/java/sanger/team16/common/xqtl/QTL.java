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
package sanger.team16.common.xqtl;

import sanger.team16.common.hbm.ModificationMapping;
import sanger.team16.common.hbm.TranscriptMapping;
import sanger.team16.common.hbm.Variation;
import sanger.team16.common.io.BigMath;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 * @date   21/09/13
 */
public class QTL
{
    public Variation snp;
    public TranscriptMapping transcriptMapping;
    public ModificationMapping modificationMapping;
    public double correlation, nominalP, empiricalP, adjRSq, slope;
    public double mean_predictor_allele, beta_SNP, sebeta_SNP, chi;   //For external algorithms

    public QTL() {}
    
    /*
     * Spearman's Rank
     */
    public QTL(Variation snp, double correlation, double nominalP) {
        this.snp = snp;
        this.correlation = correlation;
        this.nominalP = nominalP;
    }
    
    /*
     * Linear
     */
    public QTL(Variation snp, double correlation, double nominalP, double adjRSq, double slope) {
        this(snp, correlation, nominalP);
        this.adjRSq = adjRSq;
        this.slope = slope;
    }
    
    /*
     * ProbABEL
     */
    public QTL(Variation snp, double mean_predictor_allele, double beta_SNP, double sebeta_SNP, double chi, double nominalP) {
        this.snp = snp;
        this.mean_predictor_allele = mean_predictor_allele;
        this.beta_SNP = beta_SNP;
        this.sebeta_SNP = sebeta_SNP;
        this.chi = chi;
        this.nominalP = nominalP;
    }
    
    /*
     * Insignificance
     */
    public QTL(Variation snp, double nominalP) {
        this.snp = new Variation(snp.getId(), snp.getName(), snp.getPosition());
        this.nominalP = nominalP;
    }
    
    /*
     * For cis-eQTL - SNP
     */
    public QTL(TranscriptMapping transcriptMapping, double nominalP) {
        //this.transcriptMapping = new TranscriptMapping(transcriptMapping.getProbeId(), transcriptMapping.getEnsemblGene(), transcriptMapping.getGeneSymbol(), transcriptMapping.getProbeStart());
    	this.transcriptMapping = transcriptMapping;   //CHANGE 02/12/11
    	this.nominalP = nominalP;
    }
    
    /*
     * For cis-eQTL - SNP
     */
    public QTL(ModificationMapping modificationMapping, double nominalP) {
    	this.modificationMapping = modificationMapping;
    	this.nominalP = nominalP;
    }

    public double getCorrelation() {
        return BigMath.round(correlation, 3);
        //return Math.rint(this.r * 1000.0) / 1000.0;
    }
    
    public String getNominalP() {
        return BigMath.scientificNotation(nominalP);
    }
    
    public double getMinusLog10P() {
        return -Math.log10(nominalP);
    }
    
    public int getDistance(int transcriptionStartSite) {
        return Math.abs(snp.getPosition() - transcriptionStartSite);
    }
}
