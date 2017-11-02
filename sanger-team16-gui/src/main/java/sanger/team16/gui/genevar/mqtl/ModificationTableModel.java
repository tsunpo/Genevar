/**
 *  This file is part of Genevar (GENe Expression VARiation)
 *  Copyright (C) 2013  Genome Research Ltd.
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

import sanger.team16.common.hbm.ModificationMapping;
import sanger.team16.gui.jface.table.BaseJTable;
import sanger.team16.gui.jface.table.BaseTableModel;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
@SuppressWarnings("serial")
public class ModificationTableModel extends BaseTableModel
{
    private static final String[] columnNames = {" ", "Probe ID", "CpG Islands", "Gene Symbol", "Chr", "Probe Start"};

    public ModificationTableModel(boolean selected, List<ModificationMapping> modificationMappings) {
        super(columnNames);
        
        Object[][] data = this.initialData(columnNames.length + 1);
        if (modificationMappings != null && modificationMappings.size() != 0) {   //ADD 20/05/10
            data = new Object[modificationMappings.size()][columnNames.length + 1];

            for (int i=0 ; i<modificationMappings.size() ; i++) {
            	ModificationMapping modificationMapping = modificationMappings.get(i);

                data[i][0] = selected;
                data[i][1] = modificationMapping.getProbeId();
                data[i][2] = modificationMapping.getCpgIslands();
                data[i][3] = modificationMapping.getGeneSymbol();
                data[i][4] = modificationMapping.getChromosome();
                data[i][5] = modificationMapping.getProbeStart();
                data[i][6] = modificationMapping;
            }                                                     
        }
        this.data = data;
    }
    
    public BaseJTable getTable() {
        BaseJTable table = new BaseJTable(this);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(10);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(5);
        table.getColumnModel().getColumn(5).setPreferredWidth(50);
        if (this.data[0][1].equals("")) {
            table.setBackground(new Color(220, 220, 220));
            table.setEnabled(false);
        }
        
        return table;
    }
    
    public BaseJTable getSortedTable() {   //TODO
        BaseJTable table = new BaseJTable(this);   //WHY? 15/12/11
        table.setAutoCreateRowSorter(true);
        table.setCellSelectionEnabled(true);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(10);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(5);
        table.getColumnModel().getColumn(5).setPreferredWidth(50);
        if (this.data == null || this.data[0][1].equals("")) {
            table.setBackground(new Color(220, 220, 220));
            table.setEnabled(false);
            //bSelectAll.setEnabled(false);
            //bExport.setEnabled(false);
        }
        
        return table;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class<? extends Object> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    
    /*
     * Don't need to implement this method unless your table's
     * editable.
     */

    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col == 0) {
            return true;
        } else {
            return false;
        }
    }
  
    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}
