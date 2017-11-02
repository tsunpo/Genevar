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
public class Study
{
    private int id;
    private String name;
    private int genotypeAssemblyId;
    private int methylationPlatformId;
    private int expressionPlatformId;
    private String description;
    private String publicity;

    public Study() {}

    public Study(String name) {
        this.name = name;
    }

    public Study(String name, int genotypeAssemblyId, int methylationPlatformId, int expressionPlatformId, String description) {
        this.name = name;
        this.genotypeAssemblyId = genotypeAssemblyId;
        this.methylationPlatformId = methylationPlatformId;
        this.expressionPlatformId = expressionPlatformId;
        this.description = description;
        this.publicity = "Y";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGenotypeAssemblyId() {
        return genotypeAssemblyId;
    }

    public void setGenotypeAssemblyId(int genotypeAssemblyId) {
        this.genotypeAssemblyId = genotypeAssemblyId;
    }

    public int getMethylationPlatformId() {
        return methylationPlatformId;
    }

    public void setMethylationPlatformId(int methylationPlatformId) {
        this.methylationPlatformId = methylationPlatformId;
    }

    public int getExpressionPlatformId() {
        return expressionPlatformId;
    }

    public void setExpressionPlatformId(int expressionPlatformId) {
        this.expressionPlatformId = expressionPlatformId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublicity() {
        return publicity;
    }

    public void setPublicity(String publicity) {
        this.publicity = publicity;
    }

    public String toString() {
        return this.name;
    }
}
