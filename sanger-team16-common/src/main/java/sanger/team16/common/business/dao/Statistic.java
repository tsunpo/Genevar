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
package sanger.team16.common.business.dao;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
public class Statistic
{ 
    private int id;
    public String name;
    public String abbreviation; 
    public static final int EXTERNAL_ALGORITHM_ProbABEL = 1;
    public static final int EXTERNAL_ALGORITHM_PLINK = 2;
    public static final int EXTERNAL_ALGORITHM = 0;
    public static final int SPEARMANS = -1;
    public static final int LINEAR = -2;
    public static final int LINEAR_MIXED = -3;
    
    public Statistic() {}
    
    public Statistic(int id, String name, String abbreviation) {
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
    }
    
    public int getId() {
        return id;
    }
    
    /*
     * For AbstractAnalysisPane
     */
    public static boolean isExternalAlgorithm(int statisticId) {
        if (statisticId < Statistic.EXTERNAL_ALGORITHM)
            return false;
        return true;
    }

    public static int getStatisticIndex(int statisticId) {
        return statisticId * -1 - 1;
    }
    
    public String toString() {
        return this.name;
    }
}
