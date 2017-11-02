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
package sanger.team16.common.hbm;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class ModificationMapping
{
    private int id;
    private int modificationId;
    private int platformId;
    private int referenceId;
    private String probeId;
    private String cpgIslands;
    private String geneSymbol;
    private String chromosome;
    private int probeStart;
    
    public ModificationMapping() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModificationId() {
        return modificationId;
    }

    public void setModificationId(int modificationId) {
        this.modificationId = modificationId;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public String getProbeId() {
        return probeId;
    }

    public void setProbeId(String probeId) {
        this.probeId = probeId;
    }

    public String getCpgIslands() {
        return cpgIslands;
    }

    public void setCpgIslands(String cpgIslands) {
        this.cpgIslands = cpgIslands;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public int getProbeStart() {
        return probeStart;
    }

    public void setProbeStart(int probeStart) {
        this.probeStart = probeStart;
    }
    
    public String toString() {
    	if (probeId == "")
    	    if (geneSymbol != "")
       	        return geneSymbol;
    	    else if (cpgIslands != "")
    	    	return cpgIslands;
    	
        return probeId;
    }
}
