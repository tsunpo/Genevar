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
package sanger.team16.common.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.ranking.NaturalRanking;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 * @date   07/06/12
 */
public class SpearmansRank
{   
	private int n;
    private double[] rankX, rankY;
    
    /**
     * rankX represents sequence variations and rankY expression/methylation levels
     */
    public SpearmansRank(double[] x, double[] y) {
        n = x.length;
    	rankX = new NaturalRanking().rank(x);
    	rankY = new NaturalRanking().rank(y);
    }
    
    public double getCorrelation() {
        return new PearsonsCorrelation().correlation(rankX, rankY);
    }
    
    public double getCorrelationPValue() {
    	double[][] data = new double[n][2];
    	for (int i=0 ; i<n ; i++) {
    		data[i][0] = rankX[i];
    		data[i][1] = rankY[i];
    	}
    	
    	return new PearsonsCorrelation(data).getCorrelationPValues().getEntry(0,1);
    }

    public double permute(int permutation, double nominalP) {
        List<Integer> index = initIndex();
        double count=0;   //N of Pemp lower than

        for (int p=0 ; p<permutation ; p++) {
            Collections.shuffle(index);
            
        	double[][] data = new double[n][2];
        	for (int i=0 ; i<n ; i++) {
        		data[i][0] = rankX[i];
        		data[i][1] = rankY[index.get(i)];
        	}
        	
        	double empiricalP = new PearsonsCorrelation(data).getCorrelationPValues().getEntry(0,1);
            if (empiricalP <= nominalP)
            	count++;
        }

        return (double) count / permutation;
    }
    
    private List<Integer> initIndex() {
        List<Integer> index = new ArrayList<Integer>();
        for (int i=0 ; i<n ; i++)
    	    index.add(i);
        
        return index;
    }
}
