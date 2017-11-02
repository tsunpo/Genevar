/**
 *  This file is part of Genevar (GENe Expression VARiation)
 *  Copyright (C) 2011  Genome Research Ltd.
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

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar
 */
@SuppressWarnings("serial")
public class BaseJTable extends JTable
{
    public BaseJTable(AbstractTableModel tableModel) {
        super(tableModel);
        
        this.getTableHeader().setReorderingAllowed(false);
    }
/*
    public BaseJTable(TranscriptTableModel tableModel, boolean autoCreateRowSorter, boolean cellSelectionEnabled) {
        super(tableModel);
        
        if (autoCreateRowSorter) {
            TableRowSorter<TranscriptTableModel> sorter = new TableRowSorter<TranscriptTableModel>(tableModel);
            this.setRowSorter(sorter);
            //this.setAutoCreateRowSorter(autoCreateRowSorter);   // Java SE 6 and above
        }
        this.setCellSelectionEnabled(cellSelectionEnabled);
        this.getTableHeader().setReorderingAllowed(false);
    }
    
    public BaseJTable(TestTableModel tableModel, boolean autoCreateRowSorter, boolean cellSelectionEnabled) {
        super(tableModel);
        
        if (autoCreateRowSorter) {
            TableRowSorter<TestTableModel> sorter = new TableRowSorter<TestTableModel>(tableModel);
            this.setRowSorter(sorter);
            //this.setAutoCreateRowSorter(autoCreateRowSorter);   // Java SE 6 and above
        }
        this.setCellSelectionEnabled(cellSelectionEnabled);
        this.getTableHeader().setReorderingAllowed(false);
    }
    */
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.dataModel.getValueAt(rowIndex, columnIndex);
    }
}
