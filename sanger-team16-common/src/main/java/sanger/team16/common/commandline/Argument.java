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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class Argument
{
    protected Map<String, String> argMap;
    public static final String CIS_EQTL = "-cis-eqtl";
    public static final String CIS_MEQTL = "-cis-meqtl";
    public static final String TRANS_EQTL = "-trans-eqtl";
    public static final String STUDY = "-study";
    public static final String TISSUE_TYPE = "-type";
    public static final String REFERENCE = "-ref";
    public static final String STATISTIC = "-a";
    public static final String DISTANCE = "-d";
    public static final String PVALUE = "-p";
    public static final String PERMUTATIONS = "-perms";
    public static final String FILE = "-file";
    public static final String CHR = "-chr";
    public static final String LOAD = "-load";
    public static final String FEATURE_ID = "-feature";
    
    /**
     * Arguments
     */
    protected void setArgumentMap(String[] args) {
        if (args.length == 0)
            showCommandLineErrorMessage("Please input your command line arguments!");

        Set<String> cisArgs = this.getCisArguments();
        Set<String> transArgs = this.getTransArguments();        
    	for (String arg : args)
            if (arg.equals(CIS_EQTL) || arg.equals(CIS_MEQTL)) {
    	       	for (String cis : args)
                    if (cis.startsWith("-") && !cisArgs.contains(cis))
                        showCommandLineErrorMessage("Cannot recognise argument '" + cis + "' in cis analysis");
                break;

    		} else if (arg.equals(TRANS_EQTL)) {
            	for (String trans : args)
            		if (trans.startsWith("-") && !transArgs.contains(trans))
                        showCommandLineErrorMessage("Cannot recognise argument '" + trans + "' in trans analysis");    			
    		    break;
    		    
    		} else if (arg.equals(LOAD) || arg.equals(FEATURE_ID)) {
    		    break;
    			
            } else
            	showCommandLineErrorMessage("Cannot recognise argument '" + arg + "' for any analysis");

        this.argMap = new HashMap<String, String>();
        for (int i=0 ; i<args.length ; )
            if (args[i].startsWith("-"))
                if (!args[i+1].startsWith("-")) {
                    this.argMap.put(args[i], args[i+1]);
                    i = i+2;
                } else
                    showCommandLineErrorMessage("Please assign a parameter for '" + args[i] + "'");    
            else
                showCommandLineErrorMessage("Please assign an argument for '" + args[i] + "'");
    }
    
    private Set<String> getCisArguments() {
        Set<String> args = new HashSet<String>();
        args.add(CIS_EQTL);
        args.add(CIS_MEQTL);
        args.add(STUDY);
        args.add(TISSUE_TYPE);
        args.add(REFERENCE);
        args.add(STATISTIC);
        args.add(DISTANCE);   // Only needed in cis
        args.add(PVALUE);
        args.add(PERMUTATIONS);
        args.add(FILE);
        args.add(CHR);
        
        return args;
    }
    
    private Set<String> getTransArguments() {
        Set<String> args = new HashSet<String>();
        args.add(TRANS_EQTL);
        args.add(STUDY);
        args.add(TISSUE_TYPE);
        args.add(REFERENCE);
        args.add(STATISTIC);
        args.add(PVALUE);
        args.add(PERMUTATIONS);
        args.add(FILE);
        
        return args;
    }
    
    protected boolean containsKeySet(String argument, String parameter) {
        if (argMap.containsKey(argument))
            if (argMap.get(argument).contains(parameter))
                return true;
        return false;
    }
    
    protected boolean containsKey(String argument) {
        if (this.argMap.containsKey(argument)) {
            showCommandLineErrorMessage("Cannot recognise '-chr' in cis-snp analysis");
            return true;
        }
        return false;
    }
    
    /**
     * Messages
     */
    protected void showCommandLineMessage(String info) {
        System.out.println("INFO: " + info);
    }
    
    protected void showCommandLineWarningMessage(String warning) {
        System.out.println("WARNING: " + warning);
    }
    
    protected void showCommandLineErrorMessage(String error) {
        System.out.println("ERROR: " + error);
        showCommandLineTerminateMessage();
    }
    
    protected void showCommandLineTerminateMessage() {
        System.out.println("INFO: Programme terminated");
        System.exit(0);
    }
}
