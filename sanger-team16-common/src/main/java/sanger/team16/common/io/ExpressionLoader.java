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

import sanger.team16.common.hbm.dao.TranscriptDAO;
import sanger.team16.common.jdbc.ExpressionJDBC;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
public class ExpressionLoader extends AbstractLoader
{
    private int platformId;
    public int transcript;
    
    public ExpressionLoader(String address, int expressionFeatureId, int populationId, int platformId) {
        super(address, expressionFeatureId, populationId);

        this.platformId = platformId;
    }
    
    public void load(String fileName, int probeIdColumn, int individualColumn) throws IOException {
        String loadFile = fileName + ".load";
        transcript = 0;
        
        if (!fileName.equals("")) {
            BufferedWriter out = new BufferedWriter(new FileWriter(new File(loadFile)));
            BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
            String line = in.readLine();   //First line (titles);
            
            this.setIndividualIds(individualColumn, line);   // First line (titles)
            
            while ((line = in.readLine()) != null) {
                String[] st = line.split("\t");

                int transcriptId = new TranscriptDAO(address).uniqueResult(platformId, st[probeIdColumn], true);
                if (transcriptId == 0) {
                    System.out.println("Cannot map probe " + st[probeIdColumn] + " for expressionFeatureId " + this.featureId);
                } else {
                    for (int i=individualColumn ; i<st.length ; i++)
                        out.write(transcriptId + "\t" + individualIds.get(i - individualColumn) + "\t" + new Double(st[i]).doubleValue() + "\n");
                    transcript++;
                }
            }
            out.close();
            in.close();
            
            new ExpressionJDBC(address).loadDataLocalInfile(this.featureId, loadFile);
        }
    }
}
