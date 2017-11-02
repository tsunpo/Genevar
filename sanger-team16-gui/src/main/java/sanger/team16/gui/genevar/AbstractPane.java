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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import sanger.team16.common.business.dao.Statistic;
import sanger.team16.gui.jface.BaseJPane;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class AbstractPane extends BaseJPane 
{
    protected UI ui;
    //protected String address;
    protected JButton bRemove, bReset, bSubmit;
    
    public AbstractPane() {
        super(10,10,10,10);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public AbstractPane(UI ui) {
        super(10,10,10,10);
        this.ui = ui;
        //this.address = ui.address;
        
        //this.setLayout(new GridLayout(1,1));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
    
    protected void setErrorPanel(String variationName) {
        BaseJPane panel = new BaseJPane("Error",0,0,10,0);
        
        BaseJPane pError = new BaseJPane();
        pError.add(new JLabel(variationName + " is not a valid ID or cannot be found in our databases"));
        panel.add(pError);
        
        this.add(panel);
    }
    
    protected void setBlankPanel() {
        this.add(new JLabel(" "));
    }
    
    protected void setRemovePanel(ActionListener listener) {
        BaseJPane panel = new BaseJPane(5,5,5,5);
        panel.setLayout(new BorderLayout());   // * HERE HERE
        
        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();
        this.setRemoveButton(listener);
        p0.add(bRemove);        

        p0.add(Box.createHorizontalGlue());
        
        p0.setBaseSpringBox();
        panel.add(p0);
        // ------------------ Panel p0 (end) ------------------ //
        
        this.add(panel);
        bRemove.setVisible(true);   // MUST HAVE!!!
    }
    
    protected void setResetPanel(ActionListener listener) {
        BaseJPane panel = new BaseJPane(5,5,5,5);
        panel.setLayout(new BorderLayout());   // * HERE HERE
        
        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();
        p0.add(Box.createHorizontalGlue());    
        
        this.setResetButton(listener);
        bReset = new JButton("      Refresh      ");
        bReset.setForeground(Color.GREEN.darker());   // new Color(34,139,34)
        bReset.addActionListener(listener);
        //bReset.setEnabled(false);
        //bReset.setFont(new Font("", Font.BOLD, 12));
        //bReset.setVisible(false);   // MUST HAVE!!!
        p0.add(bReset);
        
        p0.setBaseSpringBox();
        panel.add(p0);
        //root.add(next, BorderLayout.LINE_END);
        // ------------------ Panel p0 (end) ------------------ //
        
        this.add(panel);
        bReset.setVisible(true);   // MUST HAVE!!!
    }
    
    protected void setResetSubmitPanel(ActionListener listener, String text) {
        BaseJPane panel = new BaseJPane(5,5,5,5);
        panel.setLayout(new BorderLayout());   // * HERE HERE
        
        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();
        p0.add(Box.createHorizontalGlue());    
        
        this.setResetButton(listener);
        p0.add(bReset);
        
        this.setSubmitButton(listener, text);
        p0.add(bSubmit);
        
        p0.setBaseSpringBox();
        panel.add(p0);
        //root.add(next, BorderLayout.LINE_END);
        // ------------------ Panel p0 (end) ------------------ //
        
        this.add(panel);
        bReset.setVisible(true);   // MUST HAVE!!!
    }
    
    protected void setRemoveSubmitPanel(ActionListener listener, String text) {
        BaseJPane panel = new BaseJPane(5,5,5,5);
        panel.setLayout(new BorderLayout());   // * HERE HERE
        
        // ------------------ Panel p0 (start) ------------------ //
        BaseJPane p0 = new BaseJPane();
        this.setRemoveButton(listener);
        //bRemove.setVisible(false);   // MUST HAVE!!! BUG 10/11/10
        p0.add(bRemove);

        p0.add(Box.createHorizontalGlue());
        
        this.setSubmitButton(listener, text);
        bSubmit.setEnabled(true);
        p0.add(bSubmit);
        
        p0.setBaseSpringBox();
        panel.add(p0);
        //root.add(next, BorderLayout.LINE_END);
        // ------------------ Panel p0 (end) ------------------ //
        
        this.add(panel);
        bRemove.setVisible(true);   // MUST HAVE!!!
    }
    
    protected void setSubmitButton(ActionListener listener, String text) {
        bSubmit = new JButton(text);
        bSubmit.addActionListener(listener);
        bSubmit.setEnabled(false);
        bSubmit.setFont(new Font("", Font.BOLD, 13));
        //bSubmit.setForeground(Color.BLUE);
    }
    
    protected void setRemoveButton(ActionListener listener) {
        bRemove = new JButton("  Remove Node  ");
        bRemove.setForeground(Color.RED);
        bRemove.addActionListener(listener);
        bRemove.setEnabled(true);
        //bRemove.setFont(new Font("", Font.BOLD, 12));
        bRemove.setVisible(false);   // MUST HAVE!!!
    }
    
    protected void setResetButton(ActionListener listener) {
        bReset = new JButton("    Reset All    ");
        bReset.setForeground(Color.GREEN.darker());   // new Color(34,139,34)
        bReset.addActionListener(listener);
        bReset.setEnabled(true);
        //bReset.setFont(new Font("", Font.BOLD, 12));
        bReset.setVisible(false);   // MUST HAVE!!!
    }

    protected void setSubmitEnabled(boolean enabled) {
        bSubmit.setEnabled(enabled);
    }
    
    /**
     * Same as BaseTableModel   //TODO
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
    protected void setTableEnabled(Object[][] data, JTable table) {
        System.out.println("HERE 1: " + data.length);
        System.out.println("HERE 2: \"" + data[0][1] + "\"");
        
        //if (data.length != 0 && data[0][1].equals("")) {   //BUG 17/11/10
        if (data.length != 0 && data[0][2].equals("")) {
            table.setBackground(new Color(220, 220, 220));
            table.setEnabled(false);
        } 
    }*/
    
    protected String getTreeNoteName(String populationName, Statistic statistic, double pValueThreshold, int distance) {
        DecimalFormat df0 = new DecimalFormat();
        df0.setMaximumFractionDigits(0);
        
        String treeNoteName = populationName + "_" + statistic.abbreviation;
        if (distance == 1000000) 
            treeNoteName += "_1MB";
        else if (distance == 100000) 
            treeNoteName += "_100KB";
        else if (distance <= 1000)
            if (distance == 1000)
                treeNoteName += "_1KB";
            else
                treeNoteName += "_" + distance + "BP";
        else
            treeNoteName += "_" + ((float) distance / 1000000) + "MB";

        //if (pValueThreshold % 0.1 == 1)   //TODO
        if (pValueThreshold == 0.1 || pValueThreshold == 0.01 || pValueThreshold == 0.001 || pValueThreshold == 0.0001 || pValueThreshold == 0.00001 || pValueThreshold == 0.000001 || pValueThreshold == 0.0000001 || pValueThreshold == 0.00000001 || pValueThreshold == 0.000000001)
            treeNoteName += "_P1.0E-0" + df0.format(- Math.log10(pValueThreshold));
        else if (pValueThreshold == 1)
            treeNoteName += "_ALL";
        else
            treeNoteName += "_P" + pValueThreshold;
        
        return treeNoteName;
    }

    /**
     * Error Messages (Same as in UI)   //TODO
     */
    protected void showConnectionErrorMessage(Exception e) {
        ui.showConnectionErrorMessage(e);
    }
    
    protected void showMessageDialogConnectionFailure() {
        ui.showMessageDialogConnectionFailure();
    }
    
    protected void showMessageDialogServicesFailure() {
        ui.showMessageDialogServicesFailure();
    }
    
    /*
     * 
     */
    protected void showMessageDialogError(String error) {
        JOptionPane.showMessageDialog(ui, error,
                "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    protected void showMessageDialogWarning(String warning) {
        JOptionPane.showMessageDialog(ui, warning,
                "Warning", JOptionPane.WARNING_MESSAGE);
    }
}

/*
protected boolean isServices() {
    return ui.isServices();
}

protected String getAddress() {
    return ui.getAddress();
}

protected String getUsername() {
    return ui.getUsername();
}

protected String getPassword() {
    return ui.getPassword();
}

/*   //10/07/11
protected UI getUi() {
    return ui;
}

protected Core getCore() {
    return ui.core;
}

protected void setCore() {
    ui.setCore();
}

protected void setCore(Core core) {
    ui.core = core;
}*/