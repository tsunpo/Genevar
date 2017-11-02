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
public class Methylation {
    private int id;
    private int methylationFeatureId;
    private int modificationId;
    private int individualId;
    private double value;

    public Methylation() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMethylationFeatureId() {
        return methylationFeatureId;
    }

    public void setMethylationFeatureId(int methylationFeatureId) {
        this.methylationFeatureId = methylationFeatureId;
    }

    public int getModificationId() {
        return modificationId;
    }

    public void setModificationId(int modificationId) {
        this.modificationId = modificationId;
    }

    public int getIndividualId() {
        return individualId;
    }

    public void setIndividualId(int individualId) {
        this.individualId = individualId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}

/*
 * public Methylation(int populationMethylationId, int modificationId, int
 * individualId, double value) {
 * this.setPopulationMethylationId(populationMethylationId);
 * this.setModificationId(modificationId); this.setIndividualId(individualId);
 * this.setValue(value); }
 */