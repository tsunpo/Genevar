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
package sanger.team16.gui.genevar.db;

import java.awt.Color;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.table.TableColumn;

import sanger.team16.gui.jface.table.BaseJTable;
import sanger.team16.gui.jface.table.BaseTableModelNotEditable;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class ExpressionTableModel extends BaseTableModelNotEditable
{
    public static String[] columnNames = {"Platform", "Tissue Type", "Normalization Method", "File Name", "Individuals", "Transcripts", "Created"};
    public static String[] columnNames2 = {"Platform", "Population", "Normalization Method", "File Name", "Individuals", "Transcripts", "Created"};
    
    public ExpressionTableModel() {
        super(columnNames);
    }
    
    public ExpressionTableModel(Object[][] data) {
        super(columnNames, data);
    }

    public ExpressionTableModel(String[] columnNames, Object[][] data) {
        super(columnNames, data);
    }
    
    public BaseJTable getTable() {
        BaseJTable table = new BaseJTable(this);
        
        TableColumn column = table.getColumnModel().getColumn(0);
        column.setCellEditor(new DefaultCellEditor(new JCheckBox()));
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        //table.getColumnModel().getColumn(1).setPreferredWidth(30);
        table.getColumnModel().getColumn(2).setPreferredWidth(20);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(20);
        table.getColumnModel().getColumn(5).setPreferredWidth(20);
        table.getColumnModel().getColumn(6).setPreferredWidth(25);
        if (data.length != 0 && (data[0][3].equals("") || data[0][4].equals("") || data[0][5].equals(""))) {   // If "file_name" == ""
            table.setBackground(new Color(220, 220, 220));
            table.setEnabled(false);
        } 
        
        return table;
    }
}
