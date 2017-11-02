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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
public class InputKeeper
{
    public ArrayList<Input> inputs;
    
    public InputKeeper() {
        this.inputs = new ArrayList<Input>();
    }
    
    public InputKeeper(String geneSymbolString, String geneFile) {
        this.inputs = new ArrayList<Input>();
        
        try {
            if (!geneSymbolString.equals(""))
                this.addFromTextArea(geneSymbolString);
            
            if (!geneFile.equals(""))
                this.addFromFile(geneFile);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void addFromTextArea(String geneSymbolString) {
        String[] lines = geneSymbolString.split("\n");
        
        for (int i=0 ; i<lines.length ; i++ ) {
            String[] st = lines[i].split("\t", 2);
            String geneSymbol = st[0];
            //String queryGeneSymbol = st[0];   //REMOVE 21/02/11
            String snpStrings = "";
            
            if (st.length > 1) {
                String[] st1 = st[1].split("\t");
                if (st1.length > 0) {
                    for (int j=0 ; j<st1.length - 1 ; j++)
                        snpStrings += st1[j] + ", ";
                    snpStrings += st1[st1.length - 1] + "\n";                         
                } 
            }
            
            this.inputs.add(new Input(geneSymbol, snpStrings));
        }
    }

    public void addFromFile(String fileName) throws FileNotFoundException, IOException {
        if (!fileName.equals("")) {
            BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
            
            String line;
            while ((line = in.readLine()) != null) {
                String[] st = line.split("\t", 2);
                String geneSymbol = st[0];
                //String queryGeneSymbol = st[0];   //REMOVE 21/02/11
                String snpStrings = "";
                
                if (st.length > 1) {
                    String[] st1 = st[1].split("\t");
                    if (st1.length > 0) {
                        for (int i=0 ; i<st1.length - 1 ; i++)
                            snpStrings += st1[i] + ", ";
                        snpStrings += st1[st1.length - 1] + "\n";                         
                    }  
                }
                
                this.inputs.add(new Input(geneSymbol, snpStrings));
            }
            
            in.close();
        }
    }
}

/*
public void addFromSNPFile(String fileName) throws FileNotFoundException, IOException {
    if (!fileName.equals("")) {
        BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
        
        String snpStrings = "";
        String line;
        while ((line = in.readLine()) != null)
            snpStrings += line + "\n";
        
        in.close();
        
        for (int i=0 ; i<inputs.size() ; i++) {
            Input input = inputs.get(i);
            input.snpStrings += snpStrings;
        }
    }
}*/