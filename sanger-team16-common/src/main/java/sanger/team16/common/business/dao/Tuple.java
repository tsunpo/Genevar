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
public class Tuple
{
    public String[] individuals; 
    public String[] genotypes;
    public double[] phenotypes;   //MODIFY 06/10/11
    public String populationName, a1 = "", a2 = "", subtitle = "";

    public Tuple() {}
    
    public Tuple(String[] individuals, String[] genotypes, double[] phenotypes) {
        this.individuals = individuals;
        this.genotypes = genotypes;
        this.phenotypes = phenotypes; 
    }

    public void setPopulationName(String populationName) {
        this.populationName = populationName;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
