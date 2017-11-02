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
package sanger.team16.common.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import sanger.team16.common.hbm.dao.ModificationDAO;
import sanger.team16.common.hbm.dao.VariationDAO;
import sanger.team16.common.jdbc.AnalysisJDBC;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
public class AnalysisLoader extends AbstractLoader
{
	private Map<String, Integer> transcriptMap;
	private Map<String, Integer> variationMap;
	
    public AnalysisLoader(String address) {
        super(address);
        
    	transcriptMap = new HashMap<String, Integer>();
    	variationMap = new HashMap<String, Integer>();
    }
    
    /**
     * MuTHER ProbABEL Format (Methylation, fit commit)
     */
    public void loadMuTHERProbABEL(int genotypeFeatureId, int newAnalysisFeatureId, String fileName, int probeIdColumn, String chromosome, int snpIdColumn, int infoColumn, int posColumn, int a1Column, int freq1Column, int v1Column, int v2Column, int v3Column, int v4Column, int v5Column) throws IOException {
        String loadFile = fileName + ".load";   //TODO chrColumn
        String varFile = fileName + ".var";
        String infoFile = fileName + ".info";
        
        if (!fileName.equals("")) {
            BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
            BufferedWriter load = new BufferedWriter(new FileWriter(new File(loadFile)));
            BufferedWriter var = new BufferedWriter(new FileWriter(new File(varFile)));
            BufferedWriter info = new BufferedWriter(new FileWriter(new File(infoFile)));
  
            String line = in.readLine();   //First line (titles);

            int currentVariationId = new VariationDAO(address).getMaxVariationId() + 1;   
            while ((line = in.readLine()) != null) {
                String[] st = line.split("\t");

                if (!st[v1Column].equals("NA")) {   //ADD 12/11/10
                    String probeId = st[probeIdColumn];
                    int transcriptId = 0;
                    if (transcriptMap.containsKey(probeId))
                    	transcriptId = transcriptMap.get(probeId);
                    else {
                        //plaotformId = 5
                        transcriptId = new ModificationDAO(address).uniqueResult(5, probeId, true);
                        transcriptMap.put(probeId, transcriptId);
                    }
                    
                    String variation = st[snpIdColumn];
                    int variationId = 0;
                    if (variationMap.containsKey(variation))
                    	variationId = variationMap.get(variation);
                    else {
                    	variationId = currentVariationId;
                        variationMap.put(variation, variationId);
                        
                        var.write(variation + "\t" + chromosome + "\t" + new Integer(st[posColumn].replace(" ", "")).intValue() + "\t" + st[a1Column] + "\n");
                        info.write(variationId + "\t" + new Double(st[infoColumn]).doubleValue() + "\t" + new Double(st[freq1Column]).doubleValue() + "\n");
                        currentVariationId ++;
                    }
                    /*else {
                        //studyId = 5
                        variationId = variationDAO.uniqueResultOrSaveNotCommitted(5, genotypeFeatureId, st[snpIdColumn], chromosome, new Integer(st[posColumn].replace(" ", "")).intValue(), st[a1Column], new Double(st[infoColumn]).doubleValue(), new Double(st[freq1Column]).doubleValue());
                        if (++count % 50 == 0) {   //flush a batch of saves and release memory                            //TODO chrColumn             //Exception in thread "main" java.lang.NumberFormatException: For input string: " 31532519"
                            variationDAO.flushAndClear();
                        	variationDAO.commit();
                        }
                        variationMap.put(variation, variationId);
                    }*/
                    
                    load.write(transcriptId + "\t" + variationId + "\t" + new Double(st[v5Column].replace(" ", "")).doubleValue() + "\t" + new Double(st[v1Column]).doubleValue() + "\t" + new Double(st[v2Column]).doubleValue() + "\t" + new Double(st[v3Column].replace(" ", "")).doubleValue()+ "\t" + new Double(st[v4Column].replace(" ", "")).doubleValue() + "\n");
                }
            }
            //variationDAO.commit();
            load.close();
            var.close();
            info.close();
            in.close();
            
            new AnalysisJDBC(address).loadDataLocalInfile(newAnalysisFeatureId, loadFile);
            new AnalysisJDBC(address).varDataLocalInfile(5, genotypeFeatureId, varFile);
            new AnalysisJDBC(address).infoDataLocalInfile(infoFile);
        }
    }
    
    /**
     * MuTHER ProbABEL Format (Expression)
     *//*
    public void loadMuTHERProbABEL(int genotypeFeatureId, int newAnalysisFeatureId, String fileName, int probeIdColumn, String chromosome, int snpIdColumn, int infoColumn, int posColumn, int a1Column, int freq1Column, int v1Column, int v2Column, int v3Column, int v4Column, int v5Column) throws IOException {
        String loadFile = fileName + ".load";   //TODO chrColumn
        
        if (!fileName.equals("")) {
            BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
            BufferedWriter load = new BufferedWriter(new FileWriter(new File(loadFile)));
            VariationDAO variationDAO = new VariationDAO(address);
            
            String line = in.readLine();   //First line (titles);
            int count=0;
            while ((line = in.readLine()) != null) {
                String[] st = line.split("\t");

                if (!st[v1Column].equals("NA")) {   //ADD 12/11/10
                    String probeId = st[probeIdColumn];
                    int transcriptId = 0;
                    if (transcriptMap.containsKey(probeId))
                    	transcriptId = transcriptMap.get(probeId);
                    else {
                        transcriptId = new TranscriptDAO(address).uniqueResult(4, probeId, false);
                        transcriptMap.put(probeId, transcriptId);
                    }
                    
                    String variation = st[snpIdColumn];
                    int variationId = 0;
                    if (variationMap.containsKey(variation))
                    	variationId = variationMap.get(variation);
                    else {
                        //studyId = 4, genotypeFeatureId = 3
                        variationId = variationDAO.uniqueResultOrSaveNotCommitted(4, genotypeFeatureId, st[snpIdColumn], chromosome, new Integer(st[posColumn].replace(" ", "")).intValue(), st[a1Column], new Double(st[infoColumn]).doubleValue(), new Double(st[freq1Column]).doubleValue());
                        if (++count % 20 == 0)   //flush a batch of saves and release memory                             //TODO chrColumn             //Exception in thread "main" java.lang.NumberFormatException: For input string: " 31532519"
                            variationDAO.flushAndClear();
                        variationMap.put(variation, variationId);
                    }
                    
                    load.write(transcriptId + "\t" + variationId + "\t" + new Double(st[v5Column].replace(" ", "")).doubleValue() + "\t" + new Double(st[v1Column]).doubleValue() + "\t" + new Double(st[v2Column]).doubleValue() + "\t" + new Double(st[v3Column].replace(" ", "")).doubleValue()+ "\t" + new Double(st[v4Column].replace(" ", "")).doubleValue() + "\n");
                }
            }
            variationDAO.commit();
            load.close();
            in.close();
            
            new AnalysisJDBC(address).loadDataLocalInfile(newAnalysisFeatureId, loadFile);
        }
    }*/
}

/* // WORKS BUT SLOW
    public void loadMuTHERProbABEL(int genotypeFeatureId, int newAnalysisFeatureId, String fileName, int probeIdColumn, String chromosome, int snpIdColumn, int infoColumn, int posColumn, int a1Column, int freq1Column, int v1Column, int v2Column, int v3Column, int v4Column, int v5Column) throws IOException {
        String loadFile = fileName + ".load";   //TODO chrColumn
        
        if (!fileName.equals("")) {
            BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
            BufferedWriter load = new BufferedWriter(new FileWriter(new File(loadFile)));
            VariationDAO variationDAO = new VariationDAO(address);
            
            String line = in.readLine();   //First line (titles);
            String probeId = "";
            int transcriptId = 0;
            int cnt=0;
            while ((line = in.readLine()) != null) {
                String[] st = line.split("\t");

                if (!st[v1Column].equals("NA")) {   //ADD 12/11/10
                    String probeIdCurrent = st[probeIdColumn];
                    if (probeId != probeIdCurrent) {
                        probeId = probeIdCurrent;
                        transcriptId = new TranscriptDAO(address).uniqueResult(4, probeId);
                    }
                                                                             //studyId = 4, genotypeFeatureId = 3
                    int variationId = variationDAO.uniqueResultOrSaveNotClose(4, genotypeFeatureId, st[snpIdColumn], chromosome, new Integer(st[posColumn].replace(" ", "")).intValue(), st[a1Column], new Double(st[infoColumn]).doubleValue(), new Double(st[freq1Column]).doubleValue());
                    if (++cnt % 50 == 0)   //flush a batch of saves and release memory              //TODO chrColumn             //Exception in thread "main" java.lang.NumberFormatException: For input string: " 31532519"
                        variationDAO.flushAndClear();
                    
                    load.write(transcriptId + "\t" + variationId + "\t" + new Double(st[v5Column].replace(" ", "")).doubleValue() + "\t" + new Double(st[v1Column]).doubleValue() + "\t" + new Double(st[v2Column]).doubleValue() + "\t" + new Double(st[v3Column].replace(" ", "")).doubleValue()+ "\t" + new Double(st[v4Column].replace(" ", "")).doubleValue() + "\n");
                }
            }
            variationDAO.commit();
            load.close();
            in.close();
            
            new AnalysisJDBC(address).loadDataLocalInfile(newAnalysisFeatureId, loadFile);
        }
    }
    */