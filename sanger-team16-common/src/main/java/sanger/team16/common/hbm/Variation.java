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
public class Variation
{
    private int id;
    private int studyId;
    private int genotypeFeatureId;
    private String name;
    private String chromosome;
    private int position;
    private String alleles;
    private double info;
    private double freq1;
    
    public Variation() {}

    /*
     * For Upload
     */
    public Variation(int studyId, int genotypeFeatureId, String name, String chromosome, int position, String alleles) {
        this.studyId = studyId;
        this.genotypeFeatureId = genotypeFeatureId;
        this.name = name;
        this.chromosome = chromosome;
        this.position = position;
        this.alleles = alleles;
    }
    
    /*
     * For QTL (Insignificance)
     */
    public Variation(int id, String name, int position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }    
/*
    public Variation(int id, String name, int position, String alleles) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.alleles = alleles;
    }*/
    
    /*
     * For QTL / ProbABEL
     */
    public Variation(int studyId, int genotypeFeatureId, String name, String chromosome, int position, String alleles, double info, double freq1) {
        this.studyId = studyId;
        this.genotypeFeatureId = genotypeFeatureId;
        this.name = name;
        this.chromosome = chromosome;
        this.position = position;
        this.alleles = alleles;
        this.info = info;
        this.freq1 = freq1;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudyId() {
        return studyId;
    }

    public void setStudyId(int studyId) {
        this.studyId = studyId;
    }

    public int getGenotypeFeatureId() {
		return genotypeFeatureId;
	}

    /*
     * When GenotypeFeature.parent_id != 0 or for Database 2.0.0
     */
    public int getGenotypeFeatureId(String address, int genotypeFeatureId) {   //TODO
        if (address.equals("tpy_team16_genevar_2_0_0.cfg.xml") || this.getGenotypeFeatureId() == 0)
        	return genotypeFeatureId;
        return this.getGenotypeFeatureId();   //BUG 08/12/11
    }
    
	public void setGenotypeFeatureId(int genotypeFeatureId) {
		this.genotypeFeatureId = genotypeFeatureId;
	}
	
	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getAlleles() {
        return alleles;
    }

    public void setAlleles(String alleles) {
        this.alleles = alleles;
    }
    
	public double getInfo() {
        return info;
    }

    public void setInfo(double info) {
        this.info = info;
    }

    public double getFreq1() {
        return freq1;
    }

    public void setFreq1(double freq1) {
        this.freq1 = freq1;
    }
}
