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
package sanger.team16.common.io;

import java.util.ArrayList;
import java.util.List;

import sanger.team16.common.hbm.dao.IndividualDAO;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
public class AbstractLoader
{
    protected String address;
    protected String regex = "\t";
    protected boolean isTabDelimited = true;
    protected int featureId;
    protected int populationId;
    public List<Integer> individualIds;

    public AbstractLoader(String address) {
        this.address = address;
    }
    
    public AbstractLoader(String address, int featureId, int populationId) {
        this.address = address;
        this.featureId = featureId;
        this.populationId = populationId;
        
        this.individualIds = new ArrayList<Integer>();
    }

    protected void setIndividualIds(int individualColumn, String firstLine) {
        String[] titles = firstLine.split("\t");
        
        //individuals = new int[titles.length];   // 75 -> 79 BUG
        //individuals = new int[titles.length - individualPosition];
        for (int i=individualColumn ; i<titles.length ; i++)
            individualIds.add(new IndividualDAO(address).uniqueResultOrSave(populationId, titles[i]));
    }
    
    protected void setRegex(String line) {
        String[] st = line.split("\t");
        
        if (st.length == 1) {
            regex = " ";
            isTabDelimited = false;
        }
    }
}
