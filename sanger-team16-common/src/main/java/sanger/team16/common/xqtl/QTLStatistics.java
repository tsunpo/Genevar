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

import java.text.DecimalFormat;

import sanger.team16.common.business.dao.Statistic;
import sanger.team16.common.business.dao.Tuple;
import sanger.team16.common.stats.SimpleLinear;
import sanger.team16.common.stats.SpearmansRank;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 * @date   08/06/12
 */
public class QTLStatistics
{
    public String a1 = "", a2 = "";
    public double correlation, nominalP, empiricalP, adjRSq, gradient;
    
    public QTLStatistics() {}
    
    public boolean calculate(Tuple tuple, String allelesString, int statisticId, double threshold, int permutation) {
        if (tuple.genotypes != null && tuple.genotypes.length > 2) {
            double[] alleles = this.getAlleles(tuple.genotypes, allelesString);

            if (statisticId == Statistic.SPEARMANS) {
            	SpearmansRank spearmans = new SpearmansRank(alleles, tuple.phenotypes);

            	correlation = spearmans.getCorrelation();
                nominalP = spearmans.getCorrelationPValue();
                
                if (nominalP <= threshold)
                    if (permutation != 0)	   
                        empiricalP = spearmans.permute(permutation, nominalP);

            } else if (statisticId == Statistic.LINEAR) {
            	SimpleLinear linear = new SimpleLinear(alleles, tuple.phenotypes);
                
            	correlation = linear.getCorrelation();   //linear.r(qtl.genotypeRanks, qtl.expressionRanks) is the same as spearmansRank.rhoTiedRank(qtl.genotypeRanks, qtl.expressionRanks)
            	nominalP = linear.getCorrelationPValue();
                adjRSq = linear.getAdjRSq();
                gradient = linear.getGradient();
                
                if (nominalP <= threshold)
                    if (permutation != 0)
                    	empiricalP = linear.permute(permutation, nominalP);
            }
   
            return true;
        }
        return false;
    }
    
    private double[] getAlleles(String[] genotypes, String allelesString) {
        double[] alleles = new double[genotypes.length];
        String[] st = allelesString.split("/");
        a1 = st[0]; a2 = st[1];

        for (int i=0 ; i<genotypes.length ; i++) {
            if (genotypes[i].equals(a1 + a1)) {          //e.g. TT
                alleles[i] = 0;
            } else if (genotypes[i].equals(a1 + a2)) {   //e.g. TC
                alleles[i] = 1;
            } else if (genotypes[i].equals(a2 + a1)) {   //e.g. CT   //ADD 20/05/10
                genotypes[i] = a1 + a2;
                alleles[i] = 1;
            } else if (genotypes[i].equals(a2 + a2)) {   //e.g. CC
                alleles[i] = 2;
            } else
                alleles[i] = 3;
        }
        
        return alleles;
    }
    
    /**
     * eQTL - SNP-Gene
     */
    public String getSubtitle(int statisticId, int permutation) {
        String subtitle = "";

        if (statisticId == Statistic.SPEARMANS)
            subtitle = "rho = ";
        else if (statisticId == Statistic.LINEAR)
            subtitle = "r = ";
        subtitle += getSubtitleCorrelation() + getSubtitleP();

        if (permutation != 0)
            subtitle += getSubtitleEmpiricalP();
            
        return subtitle;
    }
    
    private String getSubtitleCorrelation() {
        return Math.rint(correlation * 1000.0) / 1000.0 + "   ";
    }
    
    private String getSubtitleP() {
        if (nominalP <=  (double) 1e-4)
            return "P = " + new DecimalFormat("0.#E0").format(nominalP);   //.replaceAll("E", "e");
        else
            return "P = " + Math.rint(nominalP * 10000.0) / 10000.0;
    }
    
    private String getSubtitleEmpiricalP() {
        if (empiricalP == 0.0)
            return "   Pemp < 1.0E-4";
        else
            if (empiricalP <= 1.0E-4)
                return "   Pemp = " + new DecimalFormat("0.#E0").format(empiricalP);   //.replaceAll("E", "e").replaceAll("1e", "1.0e");
            else
                return "   Pemp = " + Math.rint(empiricalP * 10000.0) / 10000.0; 
    }
}
