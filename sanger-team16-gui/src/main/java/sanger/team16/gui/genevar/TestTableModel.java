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
package sanger.team16.gui.genevar;

import javax.swing.table.AbstractTableModel;

import sanger.team16.common.business.dao.Statistic;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class TestTableModel extends AbstractTableModel
{
    public static String[] columnNamesSpearmans = {" ", "SNP ID", "Chr", "SNP Position", "Gene Position", "Distance", "rho", "P-value", "-log10(P)"};   //MODIFY 30/11/11
    public static String[] columnNamesLinear = {" ", "SNP ID", "Chr", "SNP Position", "Gene Position", "Distance", "r", "P-value", "-log10(P)", "Adj_r^2", "Gradient"};   //MODIFY 30/11/11
    public static String[] columnNamesProbABEL = {" ", "SNP ID", "Chr", "SNP Position", "Gene Position", "Distance", "A1", "INFO", "Freq1", "mean_predictor_allele", "beta_SNP", "sebeta_SNP", "chi", "P", "-log10(P)"};   //MODIFY 30/11/11
    public static String[] columnNames;
    public Object[][] data;
    
    public TestTableModel(int statisticId, Object[][] data) {
        this.data = data;
        
        if (statisticId == Statistic.SPEARMANS)
            columnNames = columnNamesSpearmans;
        else if (statisticId == Statistic.LINEAR)
            columnNames = columnNamesLinear;
        else if (statisticId == Statistic.EXTERNAL_ALGORITHM_ProbABEL)
            columnNames = columnNamesProbABEL;
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
