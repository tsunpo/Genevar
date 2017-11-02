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
package sanger.team16.common.hbm;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class Analysis
{
    private int id;
    private int analysisFeatureId;
    private int probePrimaryId;
    private int variationId;
    private double value;
    private double value1;
    private double value2;
    private double value3;
    private double value4;

    public Analysis() {}

    public Analysis(double value, double value1, double value2, double value3, double value4) {
        this.value = value;
    	this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnalysisFeatureId() {
        return analysisFeatureId;
    }

    public void setAnalysisFeatureId(int analysisFeatureId) {
        this.analysisFeatureId = analysisFeatureId;
    }

    public int getProbePrimaryId() {
        return probePrimaryId;
    }

    public void setProbePrimaryId(int probePrimaryId) {
        this.probePrimaryId = probePrimaryId;
    }

    public int getVariationId() {
        return variationId;
    }

    public void setVariationId(int variationId) {
        this.variationId = variationId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
    public double getValue1() {
        return value1;
    }

    public void setValue1(double value1) {
        this.value1 = value1;
    }

    public double getValue2() {
        return value2;
    }

    public void setValue2(double value2) {
        this.value2 = value2;
    }

    public double getValue3() {
        return value3;
    }

    public void setValue3(double value3) {
        this.value3 = value3;
    }

    public double getValue4() {
        return value4;
    }

    public void setValue4(double value4) {
        this.value4 = value4;
    }
}
