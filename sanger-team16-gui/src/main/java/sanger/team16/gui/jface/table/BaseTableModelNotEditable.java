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
package sanger.team16.gui.jface.table;

import javax.swing.table.AbstractTableModel;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class BaseTableModelNotEditable extends AbstractTableModel
{
	protected String[] columnNames;
    protected Object[][] data;
    
    public BaseTableModelNotEditable(String[] columnNames) {
        this.columnNames = columnNames;
        this.data = this.initialData(columnNames.length);
    }
    
    public BaseTableModelNotEditable(String[] columnNames, Object[][] data) {
        this.columnNames = columnNames;
        this.data = data;
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
    
    /**
     * From AbstractPane
     */
    protected Object[][] initialData(int length) {
        Object[][] data = new Object[1][length];
        
        data[0][0] = false;
        for (int j=1 ; j<length ; j++)
            data[0][j] = "";
        
        return data;
    }

    protected Object[][] initialData(int col, int row) {
        Object[][] data = new Object[col][row];
        
        for (int i=0 ; i<col ; i++)
            for (int j=0 ; j<row ; j++)
                data[i][j] = "";
        
        return data;
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
     *//*
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col == 0) {
            return true;
        } else {
            return false;
        }
    }*/
  
    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
/*    
    class ColumnListener extends MouseAdapter
    {
        protected JTable table;

        public ColumnListener(JTable t) {
            table = t;
        }

        public void mouseClicked(MouseEvent e) {
            TableColumnModel colModel = table.getColumnModel();
            int columnModelIndex = colModel.getColumnIndexAtX(e.getX());
            int modelIndex = colModel.getColumn(columnModelIndex).getModelIndex();

            if (modelIndex < 0)
                return;
          
            TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
            if (modelIndex == sortCol) {
            	Comparator<Float> comparator = new Comparator<Float>() {
                    public int compare(Float s1, Float s2) {
               	        if (s1 < 0) s1 *= (-1);
                        if (s2 < 0) s2 *= (-1);
                        return s1.compareTo(s2);    
                    }
                };
                sorter.setComparator(2, comparator);
                table.setRowSorter(sorter);
                
                TableColumn column = colModel.getColumn(sortCol);
                column.setHeaderValue("Score (Absolute)");
                table.getTableHeader().repaint();
            } else {

            }
        }
    }
    */
}
