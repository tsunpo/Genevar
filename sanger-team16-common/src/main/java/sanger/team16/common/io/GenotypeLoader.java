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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sanger.team16.common.hbm.dao.IndividualDAO;
import sanger.team16.common.hbm.dao.VariationDAO;
import sanger.team16.common.jdbc.GenotypeJDBC;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
public class GenotypeLoader extends AbstractLoader
{
    private int studyId;
    public List<Integer> variationIds;
    
    public GenotypeLoader(String address, int studyId, int genotypeFeatureId, int populationId) {
        super(address, genotypeFeatureId, populationId);
        
        this.studyId = studyId;
        this.variationIds = new ArrayList<Integer>();
    }

    /**
     * PLINK Format
     */
    public void loadBIM(String fileName) throws IOException {
        if (!fileName.equals("")) {
            BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
            VariationDAO variationDAO = new VariationDAO(address);
            
            String line = "";
            int cnt=0;
            while ((line = in.readLine()) != null) {
                String[] st = line.split("\t");
                                                                                  //ADD genotypeFeatureId 28/11/11
                variationIds.add(variationDAO.uniqueResultOrSaveNotCommitted(studyId, featureId, st[1], st[0], new Integer(st[3]).intValue(), this.getSortedAlleles(st[4], st[5])));
                if (++cnt % 50 == 0) {   //flush a batch of saves and release memory
                    variationDAO.flushAndClear();
                    variationDAO.commit();
                }
            }
            variationDAO.commit();
            in.close();   
        }
    }    
    
    public void loadPED(String fileName) throws IOException {
        //String logFile = fileName + ".log";
        String loadFile = fileName + ".load";

        if (!fileName.equals("")) {
            BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
            BufferedWriter load = new BufferedWriter(new FileWriter(new File(loadFile)));
            //BufferedWriter log = new BufferedWriter(new FileWriter(new File(logFile)));
            
            String line = in.readLine();
            this.setRegex(line);
            if (this.isTabDelimited) {   //ADD 22/07/11 Support space-delimited
                String[] st = line.split("\t");
                int individualId = new IndividualDAO(address).uniqueResultOrSave(populationId, st[1]);
                individualIds.add(individualId);
                
                for (int v=0 ; v<variationIds.size() ; v++) {
                    String alleles = st[v+6].replace(" ", "").replace("0", "N");   //ADD 18/05/10
                    load.write(variationIds.get(v) + "\t" + individualId + "\t" + alleles + "\n");
                }
                
            } else {
                String[] st = line.split(" ");
                int individualId = new IndividualDAO(address).uniqueResultOrSave(populationId, st[1]);
                individualIds.add(individualId);
                
                int s = 0;
                for (int v=0 ; v<variationIds.size() ; v++) {
                    String alleles = (st[v+s+6] + st[v+s+7]).replace("0", "N");   //ADD 18/05/10
                    load.write(variationIds.get(v) + "\t" + individualId + "\t" + alleles + "\n");
                    s++;
                }
            }
            
            if (this.isTabDelimited) {   //ADD 22/07/11 Support space-delimited
                while ((line = in.readLine()) != null) {
                    String[] st = line.split("\t");
                    int individualId = new IndividualDAO(address).uniqueResultOrSave(populationId, st[1]);
                    individualIds.add(individualId);
                    
                    for (int v=0 ; v<variationIds.size() ; v++) {
                        String alleles = st[v+6].replace(" ", "").replace("0", "N");   //ADD 18/05/10
                        load.write(variationIds.get(v) + "\t" + individualId + "\t" + alleles + "\n");
                    }
                }
                
            } else {
                while ((line = in.readLine()) != null) {
                    String[] st = line.split(" ");
                    int individualId = new IndividualDAO(address).uniqueResultOrSave(populationId, st[1]);
                    individualIds.add(individualId);
                    
                    int s = 0;
                    for (int v=0 ; v<variationIds.size() ; v++) {
                        String alleles = (st[v+s+6] + st[v+s+7]).replace("0", "N");   //ADD 18/05/10
                        load.write(variationIds.get(v) + "\t" + individualId + "\t" + alleles + "\n");
                        s++;
                    }
                }
            }
            
            in.close();
            load.close();
            //log.close();

            new GenotypeJDBC(address).loadDataLocalInfile(this.featureId, loadFile);
        }
    }
    
    /**
     * Team16 Format
     */
    public void loadTeam16(String fileName, int chrColumn, int posColumn, int snpIdColumn, int allelesColumn, int individualColumn) throws IOException {
        String loadFile = fileName + ".load";
        
        this.initTeam16(fileName, chrColumn, posColumn, snpIdColumn, allelesColumn, individualColumn);
        
        if (!fileName.equals("")) {
            BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
            BufferedWriter load = new BufferedWriter(new FileWriter(new File(loadFile)));
            
            String line = in.readLine();   //First line (titles);
            int s=0;
            while ((line = in.readLine()) != null) {
                String[] st = line.split("\t");
  
                for (int i=individualColumn ; i<st.length ; i++)
                    load.write(variationIds.get(s) + "\t" + individualIds.get(i - individualColumn) + "\t" + st[i] + "\n");
                s++;
            }
            load.close();
            in.close();
            
            new GenotypeJDBC(address).loadDataLocalInfile(this.featureId, loadFile);
        }
    }
    
    private void initTeam16(String fileName, int chrColumn, int posColumn, int snpIdColumn, int allelesColumn, int individualColumn) throws IOException {
        if (!fileName.equals("")) {
            BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
            String line = in.readLine();
            
            this.setIndividualIds(individualColumn, line);   // First line (titles)
            
            VariationDAO variationDAO = new VariationDAO(address);
            int cnt=0;
            while ((line = in.readLine()) != null) {
                String[] st = line.split("\t");
                                                                                  // genotypeFeatureId = 0  //CHANGED 08/12/11
                variationIds.add(variationDAO.uniqueResultOrSaveNotCommitted(studyId, 0, st[snpIdColumn], st[chrColumn], new Integer(st[posColumn]).intValue(), this.getSortedAlleles(st[allelesColumn])));
                if (++cnt % 50 == 0)   //flush a batch of saves and release memory
                    variationDAO.flushAndClear();
            }
            variationDAO.commit();
            in.close();
        }
    }
    
    private String getSortedAlleles(String a1, String a2) {
        //TODO indels?
        return this.sort(a1 + a2);
    }
    
    private String getSortedAlleles(String alleles) {
        //TODO indels?
        return this.sort(alleles.replace("/", ""));
    }
    
    private String sort(String alleles) {
        char[] array = alleles.toCharArray();
        Arrays.sort(array);
        
        return new String(array[0] + "/" + array[1]);
    }
}

/*
public int loadDirectly(int datasetId, int refDbsnpId, int populationGenotypeId, int[] individuals, String fileName, int snpIdPosition, int individualPosition) throws IOException {
    int row = 0;
    
    if (!fileName.equals("")) {
        BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
        String line = in.readLine();   // First line (titles)
        
        GenotypeDAO genotypeDAO = new GenotypeDAO();   //ADD 05/02/10 For better memory usage
        while ((line = in.readLine()) != null) {
            String[] st = line.split("\t");
            
            int variationId = new VariationDAO().uniqueResult(datasetId, refDbsnpId, st[snpIdPosition]);
            if (variationId == 0) {
                variationId = new VariationDAO().save(new Variation(0, datasetId, refDbsnpId, st[snpIdPosition], st[0], new Integer(st[1]).intValue(), st[3]));
                newVariationCnt++;
            }

            //for (int i=individualPosition ; i<individuals.size() ; i++)   //BUG 30/01/10
            //    new GenotypeDAO().persist(new Genotype(populationGenotypeId, variationId, individuals.get(i), st[i]));
            //GenotypeDAO genotypeDAO = new GenotypeDAO();   //ADD 04/02/10
            for (int i=individualPosition ; i<st.length ; i++)
                genotypeDAO.persist(new Genotype(populationGenotypeId, variationId, individuals[i - individualPosition], st[i]));

            row++;
        }
        
        genotypeDAO.close();
        in.close();
    }
    
    return row;
}
*/