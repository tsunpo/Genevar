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
package sanger.team16.gui.jface;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class BaseJPane extends JPanel
{	
    public BaseJPane() {
        this.init();
    } 
    
    public BaseJPane(int top, int left, int bottom, int right) {
        this.init();
        
        Border margin = BorderFactory.createEmptyBorder(top,left,bottom,right); 
        this.setBorder(margin);
    }

    public BaseJPane(String title) {
        this.init();
        
        Border margin = BorderFactory.createEmptyBorder(10,20,10,20);  
        TitledBorder titled = BorderFactory.createTitledBorder(title);
        titled.setTitleFont(new Font("Arial", Font.BOLD, 14));
        titled.setTitleColor(Color.darkGray);
        this.setBorder(new CompoundBorder(titled, margin));
    }

    public BaseJPane(String title, int top, int left, int bottom, int right) {
        this.init();
        
        Border margin = BorderFactory.createEmptyBorder(top,left,bottom,right);  
        TitledBorder titled = BorderFactory.createTitledBorder(title);
        titled.setTitleFont(new Font("Arial", Font.BOLD, 14));
        titled.setTitleColor(Color.darkGray);
        this.setBorder(new CompoundBorder(titled, margin));
    }

    private void init() {
        this.setBackground(Color.white);
        //this.setAutoscrolls(false);        
    }
    
    public void setBaseSpringGrid(int cols) {
        int rows = this.getComponentCount() / cols;
        
        this.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(this,
                rows, cols,   //rows,  cols
                5, 5,         //initX, initY
                5, 5);        //xPad,  yPad
    }

    public void setBaseSpringGrid(int cols, int pad) {
        int rows = this.getComponentCount() / cols;
        
        this.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(this,
                rows, cols,   //rows,  cols
                pad, pad,     //initX, initY
                pad, pad);    //xPad,  yPad
    }
    
    public void setBaseSpringBox() {
        int cols = this.getComponentCount();
        
        this.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(this,
                1, cols,   //rows,  cols
                0, 0,      //initX, initY
                5, 0);     //xPad,  yPad
    }
    
    public void setBaseSpringBoxTrailing() {
        this.add(Box.createHorizontalGlue());
        this.setBaseSpringBox();
    }
    
    public void refresh() {}
}