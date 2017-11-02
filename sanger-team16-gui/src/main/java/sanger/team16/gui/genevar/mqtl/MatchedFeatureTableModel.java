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
package sanger.team16.gui.genevar.mqtl;

import java.awt.Color;
import java.util.List;

import sanger.team16.common.business.dao.MatchedFeature;
import sanger.team16.gui.jface.table.BaseJTable;
import sanger.team16.gui.jface.table.BaseTableModel;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class MatchedFeatureTableModel extends BaseTableModel
{
    private static String[] columnNames = {" ", "Population", "Genotype", "Methylation"};
    
    public MatchedFeatureTableModel(List<MatchedFeature> matchedFeatures) {
    	super(columnNames);
    	
        Object[][] data = this.initialData(columnNames.length + 1);
        if (matchedFeatures != null && matchedFeatures.size() != 0) {   // new PrimaryKeyRetrieval().getPopulationSamples(platformId) returns not null
            data = new Object[matchedFeatures.size()][columnNames.length + 1];   //2]; CHANGE 10/11/10
            
            for (int i=0 ; i<matchedFeatures.size() ; i++) {
                MatchedFeature matchedFeature = matchedFeatures.get(i);

                data[i][0] = true;
                data[i][1] = matchedFeature.getPopulationName();
                data[i][2] = matchedFeature.getGenotypeFeatureDescription();
                data[i][3] = matchedFeature.getMethylationFeatureDescription();
                data[i][4] = matchedFeature;
            }
        }
		this.data = data;
	}

    public BaseJTable getTable() {
        BaseJTable table = new BaseJTable(this);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(10);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(250);
        table.getColumnModel().getColumn(3).setPreferredWidth(250);
        if (this.data[0][1].equals("")) {
            table.setBackground(new Color(220, 220, 220));
            table.setEnabled(false);
        }
        
        return table;
    }
}
