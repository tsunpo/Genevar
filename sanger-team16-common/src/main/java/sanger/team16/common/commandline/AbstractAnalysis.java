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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sanger.team16.common.business.CoreRetrieval;
import sanger.team16.common.business.dao.MatchedFeature;
import sanger.team16.common.business.dao.Tuple;
import sanger.team16.common.hbm.dao.AbstractDAO;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class AbstractAnalysis extends Argument
{
    protected String address;
    protected Map<String, String> argMap;
    private String currentDir = System.getProperty("user.dir");
    private String description = "";
    protected List<String> lines;   //ADD 06/10/11
    protected BufferedReader in;
    protected BufferedWriter out, log;
    protected int studyId, tissueTypeId, referenceId, statisticId, distance, permutation;
    protected double threshold;
    protected List<MatchedFeature> matchedFeatures;

    public AbstractAnalysis(String address, Map<String, String> argMap) throws IOException {
        this.address = address;
        this.argMap = argMap;
    }
    
    public AbstractAnalysis(String address, Map<String, String> argMap, String module) throws IOException {
        this.address = address;
        this.argMap = argMap;
        this.setDescription(module);
        this.setIO();
        
        studyId = getUniqueId(Argument.STUDY, "Study");
        tissueTypeId = 0;
        if (argMap.containsKey(Argument.TISSUE_TYPE))
            tissueTypeId = getUniqueId(Argument.TISSUE_TYPE, "TissueType");
        referenceId = getUniqueId(Argument.REFERENCE, "Reference");
        
        statisticId = -1;   //TODO Spearmans Rank
        if (!argMap.containsKey(Argument.TRANS_EQTL))
            distance = getIntegerVaule(Argument.DISTANCE);
        
        threshold = getDoubleVaule(Argument.PVALUE);
        permutation = 0;
        if (argMap.containsKey(Argument.PERMUTATIONS))
            permutation = getIntegerVaule(Argument.PERMUTATIONS);
        
        matchedFeatures = getMatchedFeatures(studyId, tissueTypeId);
    }
    
    /**
     * DAOs
     */
    private List<MatchedFeature> getMatchedFeatures(int studyId, int tissueTypeId) {
        List<MatchedFeature> matchedFeatures = null;
        if (argMap.containsKey(Argument.CIS_EQTL) || argMap.containsKey(Argument.TRANS_EQTL))
            matchedFeatures = new CoreRetrieval(address).getMatchedFeaturesGxE(studyId);
        else if (argMap.containsKey(Argument.CIS_MEQTL))
            matchedFeatures = new CoreRetrieval(address).getMatchedFeaturesGxM(studyId);

        if (matchedFeatures == null || matchedFeatures.size() == 0)
            showCommandLineErrorMessage("There is no matched features for study '" + argMap.get(Argument.STUDY) + "'");
        
        if (tissueTypeId != 0)
            for (int m=0 ; m<matchedFeatures.size() ; m++) {
                MatchedFeature matchedFeature = matchedFeatures.get(m);

                if (matchedFeature.getTissueTypeId() != tissueTypeId)
                    matchedFeatures.remove(m);
            }

        return matchedFeatures;
    }
    
    private int getUniqueId(String argu, String table) {
        String parameter = this.checkEmptyParameter(argu);
        
        int id = new AbstractDAO(address).uniqueResult(parameter, table, true);
        if (id == 0) {
            showCommandLineWarningMessage("Parameter of " + argu + " is not unique in the database");
            showCommandLineMessage("Specify '" + parameter + "' by combining more information with '-' e.g. '" + parameter + "-pilot'");
            showCommandLineTerminateMessage();
        }
        return id;
    }
    
    /**
     * Parameters
     */
    protected int getStatisticId() {
        String statistic = argMap.get(Argument.STATISTIC);
    
        if (statistic.equalsIgnoreCase("SRC"))
            return -1;
        else if (statistic.equalsIgnoreCase("LR"))
        	return -2;
        else if (statistic.equalsIgnoreCase("LMM"))
        	return 1;
        return -1;    //default SRC
    }
    
    protected double getMinusLog10P(double pValue) {
        return -Math.log10(pValue);
    }
    
    protected Double getDoubleVaule(String argu) {
        return new Double(this.checkEmptyParameter(argu)).doubleValue();
    }

    protected Integer getIntegerVaule(String argu) {
        return new Integer(this.checkEmptyParameter(argu)).intValue();
    }
    
    protected String checkEmptyParameter(String argu) {
        String parameter = this.argMap.get(argu);
        
        if (parameter == null)
            showCommandLineErrorMessage("Parameter of '" + argu + "' is empty");
        
        return parameter;
    }
    
    private void setDescription(String module) {
        description += module.replaceFirst("-", "") + "-" + argMap.get(module);
        description += "_" + argMap.get(Argument.STUDY);
        if (argMap.containsKey(Argument.TISSUE_TYPE))
            description += "-" + argMap.get(Argument.TISSUE_TYPE);
        description += "_" + argMap.get(Argument.REFERENCE);
        description += "_" + argMap.get(Argument.PVALUE);
        if (argMap.containsKey(Argument.PERMUTATIONS))
            description += "_" + argMap.get(Argument.PERMUTATIONS);
    }

    /**
     * I/O
     */
    private void setIO() throws IOException {
        if (System.getProperty("os.name").toLowerCase().indexOf("windows") == -1)
            currentDir += "/" + description;
        else
            currentDir += "\\" + description;
        
        if (argMap.containsKey(Argument.FILE) && !argMap.containsKey(Argument.CHR)) {
            currentDir += "_" + argMap.get(Argument.FILE);
            in = this.getFileReader(Argument.FILE);
            
            lines = new ArrayList<String>();
            String line;
            while ((line = in.readLine()) != null)
                lines.add(line);
            in.close();
            
        } else if (argMap.containsKey(Argument.CHR) && !argMap.containsKey(Argument.FILE)) {
            currentDir += "_chr" + argMap.get(Argument.CHR);
        } else {
            showCommandLineErrorMessage("Please specify either '-file' or '-chr' (but not both) in your command line arguments");
            return;
        }
        
        new File(currentDir).mkdir();
    }
    
    protected String getFileName(String population) throws IOException {
        String fileName = currentDir;
        
        if (System.getProperty("os.name").toLowerCase().indexOf("windows") == -1)
            fileName += "/";
        else
            fileName += "\\";
        fileName += population + "_" + description;
        
        return fileName;
    }

    protected BufferedReader getFileReader(String argu) {
        String parameter = this.checkEmptyParameter(argu);
        
        try {
            showCommandLineMessage("Reading '" + parameter + "'");
            return new BufferedReader(new FileReader(new File(parameter)));
            
        } catch (Exception e) {
            showCommandLineErrorMessage("Reading '" + parameter + "' failed");
            return null;
        }
    }
    
    protected BufferedWriter getFileWriter(String fileName, String extention) throws IOException {
        return new BufferedWriter(new FileWriter(new File(fileName + "." + extention)));
    }

    protected void outputTuple(Tuple tuple, String filename) throws IOException {
        BufferedWriter out = this.getFileWriter(filename, "txt");
        int size = tuple.genotypes.length;
        
        out.write("individuals" + "\t" + "genotype" + "\t" + "trait" + "\n");
        for (int i=0 ; i<size ; i++) {
            out.write(tuple.individuals[i] + "\t" + tuple.genotypes[i] + "\t" + tuple.phenotypes[i] + "\n");
        }
        out.close();
    }
    
    protected void close() throws IOException {
        out.close();
        log.close();
    }
}
