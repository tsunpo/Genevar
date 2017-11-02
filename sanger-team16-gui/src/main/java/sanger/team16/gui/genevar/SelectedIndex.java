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

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class SelectedIndex
{
    public int studyIndex;// = 0;   //CHANGE 07/12/11
    public int referencesIndex;// = 0;
    public int statisticIndex;// = 0;
    public String textArea1;// = "";
    public String textArea2;// = "";
    public String textField1;// = "";
    public String textField2;// = "";
    public boolean matched;// = true;
    
    public SelectedIndex() {
    	this.studyIndex = 0;
    	this.referencesIndex = 0;
    	this.statisticIndex = 0;
    	this.textArea1 = "";
    	this.textArea2 = "";
    	this.textField1 = "";
    	this.textField2 = "";
    	this.matched = true;
    }
    
    /*
     * CisEQTLGenePane
     */
    public SelectedIndex(int studyIndex, int referenceIndex, String textArea1, String textField1, boolean matched) {
        this.studyIndex = studyIndex;
        this.referencesIndex = referenceIndex;
        this.textArea1 = textArea1;
        this.textField1 = textField1;
        this.matched = matched;
    }

    /*
     * EQTLSNPGenePane
     */
    public SelectedIndex(int studyIndex, int referenceIndex, String textArea1, String textField1, String textArea2, String textField2, boolean matched) {
        this.studyIndex = studyIndex;
        this.referencesIndex = referenceIndex;
        this.textArea1 = textArea1;
        this.textField1 = textField1;
        this.textArea2 = textArea2;
        this.textField2 = textField2;
        this.matched = matched;
    }
    
    /*
     * CisEQTLGenePane2
     */
    public SelectedIndex(int statisticIndex) {
        this.studyIndex = 1;
        this.statisticIndex = statisticIndex;
    }
    
    /*
     * CisEQTLSNPPane
     */
    public SelectedIndex(int studyIndex, int referenceIndex, int statisticIndex, String textArea1) {
        this.studyIndex = studyIndex;
        this.referencesIndex = referenceIndex;
        this.statisticIndex = statisticIndex;
        this.textArea1 = textArea1;
    }
}
