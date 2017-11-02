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
package sanger.team16.common.commandline;

import java.io.IOException;

import sanger.team16.common.io.LoadAnalysis;


/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class CommandLineMode extends Argument
{
    private String address = "tpy_team16_genevar_3_0_0.cfg.xml";

    public static void main(String[] args) throws IOException {
        new CommandLineMode(args);
    }

    protected CommandLineMode(String[] args) throws IOException {
        this.showCommandLineMessage("Starting Genevar command line mode...");
        this.setArgumentMap(args);
        
        // eQTL Analysis //
        if (this.containsKeySet(Argument.CIS_EQTL, "snp")) {
            if (this.containsKey(Argument.CHR))
                return;
            new EQTLAnalysis(address, argMap, Argument.CIS_EQTL).cisSNP();
        
        } else if (this.containsKeySet(Argument.CIS_EQTL, "gene")) {
            new EQTLAnalysis(address, argMap, Argument.CIS_EQTL).cisGene();
            
        } else if (this.containsKeySet(Argument.TRANS_EQTL, "snp")) {
        	new EQTLAnalysis(address, argMap, Argument.TRANS_EQTL).transSNP();
            
        // methylationQTL Analysis //
        } else if (this.containsKeySet(Argument.CIS_MEQTL, "gene")) {
            new MQTLAnalysis(address, argMap, Argument.CIS_MEQTL).cisGene();
            
        //} else if (this.containsKeySet(Argument.CIS_MEQTL, "snp")) {
            //new MQTLAnalysis(address, argMap, Argument.CIS_MEQTL).cisSNP();
            
        // Upload //
        } else if (this.containsKeySet(Argument.LOAD, "muther")) {   // GenABEL/ProbABEL
        	new LoadAnalysis(address, argMap).loadMuTHERProbABEL();
        }
    }
}
