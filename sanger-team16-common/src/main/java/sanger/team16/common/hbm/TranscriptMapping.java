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
package sanger.team16.common.hbm;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class TranscriptMapping
{
    private int id;
    private int transcriptId;
    private int platformId;
    private int referenceId;
    private String probeId;
    private String ensemblGene;
    private String geneSymbol;
    private String chromosome;
    private int geneStart;
    private int geneEnd;
    private int strand;
    private int probeStart;
    //private int transcriptionStartSite;   //REMOVE 15/02/11
    //private String snpStrings;   //REMOVE 21/02/11

    public TranscriptMapping() {} // !!!!!!

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTranscriptId() {
        return transcriptId;
    }

    public void setTranscriptId(int transcriptId) {
        this.transcriptId = transcriptId;
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

    public String getEnsemblGene() {
        return ensemblGene;
    }

    public void setEnsemblGene(String EnsemblGene) {
        this.ensemblGene = EnsemblGene;
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

    public int getGeneStart() {
        return geneStart;
    }

    public void setGeneStart(int geneStart) {
        this.geneStart = geneStart;
    }

    public int getGeneEnd() {
        return geneEnd;
    }

    public void setGeneEnd(int geneEnd) {
        this.geneEnd = geneEnd;
    }

    public int getStrand() {
        return strand;
    }

    public void setStrand(int strand) {
        this.strand = strand;
    }

    public int getProbeStart() {
        return probeStart;
    }

    public void setProbeStart(int probeStart) {
        this.probeStart = probeStart;
    }

    public int getTranscriptionStartSite() {
        //if (getStrand() == 1)   //BUG 02/12/11
        //    return getGeneStart();
        //return getGeneEnd();
    	
    	if (this.strand == 1)
            return this.geneStart;
        return this.geneEnd;
    }
}

/*
public void setTranscriptionStartSite(int transcriptionStartSite) {
    this.transcriptionStartSite = transcriptionStartSite;
}

public String getSNPStrings() {
    return snpStrings;
}

public void setSNPStrings(String snpStrings) {
    this.snpStrings = snpStrings;
}*/