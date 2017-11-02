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

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 * @date   07/06/12
 */
public class SimpleLinear
{   
	private int n;
    private double[] sampleX, sampleY;
    private double r;
    private double sigmaX = 0;
    private double sigmaY = 0;
    private double meanX = 0;
    private double meanY = 0;
    private double ssX = 0;
    private double ssY = 0;
    private double ssXY = 0;
    
    /**
     * rankX represents sequence variations and rankY is expression/methylation levels
     */
    public SimpleLinear(double[] sampleX, double[] sampleY) {
        n = sampleX.length;
        this.sampleX = sampleX;
        this.sampleY = sampleY;
    }
    
    public double getCorrelation() {
        for (int i=0 ; i<n ; i++) {
            sigmaX += sampleX[i];
            sigmaY += sampleY[i];
        }
        meanX = sigmaX / n;
        meanY = sigmaY / n;
        
        for (int i=0 ; i<n ; i++) {
            double deviationX = sampleX[i] - meanX;
            double deviationY = sampleY[i] - meanY;
            
            ssX += (deviationX * deviationX);    // Sum of squares: (X-Xbar)^2 - "the sum of the squared deviations"
            ssY += (deviationY * deviationY);    // Sum of squares: (Y-Ybar)^2 
            ssXY += (deviationX * deviationY);   // (X-Xbar)(Y-Ybar)
        }
        
        r = ssXY / Math.sqrt(ssX * ssY);
        return r;
    }
    
    public double getCorrelationPValue() {
    	double[][] data = new double[n][2];
    	for (int i=0 ; i<n ; i++) {
    		data[i][0] = sampleX[i];
    		data[i][1] = sampleY[i];
    	}

    	return new PearsonsCorrelation(data).getCorrelationPValues().getEntry(0,1);
    }
    
    public double getAdjRSq() {
        return 1 - ( ((n - 1)/(n - 2))*(1 - r*r) );
    }
    
    /*
     * y = a + bx
     */
    public double getGradient() {
        //return ( n*sigmaXY - sigmaX*sigmaY ) / ( n*sigmaXX - sigmaX*sigmaX );
        return ssXY / ssX;
    }
    
    public double permute(int permutation, double nominalP) {
        List<Integer> index = initIndex();
        double count=0;   //N of Pemp lower than

        for (int p=0 ; p<permutation ; p++) {
            Collections.shuffle(index);
            
        	double[][] data = new double[n][2];
        	for (int i=0 ; i<n ; i++) {
        		data[i][0] = sampleX[i];
        		data[i][1] = sampleY[index.get(i)];
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
