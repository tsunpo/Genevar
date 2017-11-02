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
package sanger.team16.gui.genevar.eqtl;

import java.awt.Color;
import java.util.List;

import sanger.team16.common.hbm.TranscriptMapping;
import sanger.team16.gui.genevar.Input;
import sanger.team16.gui.jface.table.BaseJTable;
import sanger.team16.gui.jface.table.BaseTableModel;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class GeneTableModel extends BaseTableModel
{
    private static final String[] columnNames = {" ", "Ensembl Gene", "Chr", "Gene Start", "Gene End", "Strand", "Gene Symbol"};
    private static final String[] columnNames2 = {" ", "Ensembl Gene", "Chr", "Gene Start", "Gene End", "Strand", "Gene Symbol", "Paired SNP(s)"};

    public GeneTableModel() {
        super(columnNames);
    }
    
    public GeneTableModel(String[] columnNames) {
        super(columnNames);
    }
    
    public GeneTableModel(List<TranscriptMapping> transcriptMappings) {
        super(columnNames);
        
        Object[][] data = this.initialData(columnNames.length + 1);
        if (transcriptMappings != null && transcriptMappings.size() != 0) {
            data = new Object[transcriptMappings.size()][columnNames.length + 1];
         
            for (int i=0 ; i<transcriptMappings.size() ; i++) {
                TranscriptMapping transcriptMapping = transcriptMappings.get(i);
                
                data[i][0] = true;
                data[i][1] = transcriptMapping.getEnsemblGene();
                data[i][2] = transcriptMapping.getChromosome();
                data[i][3] = transcriptMapping.getGeneStart();
                data[i][4] = transcriptMapping.getGeneEnd();
                data[i][5] = transcriptMapping.getStrand();
                data[i][6] = transcriptMapping.getGeneSymbol();
            }   
        }
        this.data = data;
	}
    
    public GeneTableModel(List<TranscriptMapping> transcriptMappings, List<Input> inputs) {
        super(columnNames2);
        
        Object[][] data = this.initialData(columnNames2.length);
        if (transcriptMappings != null && transcriptMappings.size() != 0) {
            data = new Object[transcriptMappings.size()][columnNames2.length];
         
            for (int i=0 ; i<transcriptMappings.size() ; i++) {
                TranscriptMapping transcriptMapping = transcriptMappings.get(i);
                
                data[i][0] = true;
                data[i][1] = transcriptMapping.getEnsemblGene();
                data[i][2] = transcriptMapping.getChromosome();
                data[i][3] = transcriptMapping.getGeneStart();
                data[i][4] = transcriptMapping.getGeneEnd();
                data[i][5] = transcriptMapping.getStrand();
                data[i][6] = transcriptMapping.getGeneSymbol();
                for (int j=0 ; j<inputs.size() ; j++)
                    if (transcriptMapping.getGeneSymbol().matches(inputs.get(j).query)) {
                        data[i][7] = inputs.get(j).snpStrings;
                        break;
                    } else
                        data[i][7] = "";
            }   
        }
        this.data = data;
    }
    
    public BaseJTable getTable() {
        BaseJTable table = new BaseJTable(this);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(10);
        table.getColumnModel().getColumn(1).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(5);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(50);
        table.getColumnModel().getColumn(5).setPreferredWidth(5);
        //if (data.length != 0 && data[0][1].equals("")) {   //BUG 17/11/10
        if (data.length != 0 && data[0][2].equals("")) {
            table.setBackground(new Color(220, 220, 220));
            table.setEnabled(false);
        }
        
        return table;
    }
    
    public BaseJTable getTable2() {   //TODO
        BaseJTable table = new BaseJTable(new GeneTableModel(columnNames2));
        
        table.getColumnModel().getColumn(0).setPreferredWidth(10);
        table.getColumnModel().getColumn(1).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(5);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(50);
        table.getColumnModel().getColumn(5).setPreferredWidth(5);
        //if (data.length != 0 && data[0][1].equals("")) {   //BUG 17/11/10
        if (data.length != 0 && data[0][2].equals("")) {
            table.setBackground(new Color(220, 220, 220));
            table.setEnabled(false);
        }
        
        return table;
    }
}
