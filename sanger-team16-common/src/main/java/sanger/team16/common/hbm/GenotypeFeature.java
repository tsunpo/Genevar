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

import java.util.Date;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class GenotypeFeature {
    private int id = 0;
    private int populationId;
    private int assemblyId;
    private int methodId;
    private String description = "";
    private String fileName;
    private int fileCol;
    private int fileRow;
    private String createdBy;
    private Date created;
    private int parentFeatureId;

    public GenotypeFeature() {}

    public GenotypeFeature(int populationId, int assemblyId, int versionId, String description, String createdBy) {
        this.setPopulationId(populationId);
        this.setAssemblyId(assemblyId);
        this.setMethodId(versionId);
        this.setDescription(description);
        this.createdBy = createdBy;
        this.created = new Date();
        this.parentFeatureId = -1;
    }

    public GenotypeFeature(int id, String description) {   //For MatchFeature
        this.setId(id);
        this.setDescription(description);
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPopulationId() {
        return populationId;
    }

    public void setPopulationId(int populationId) {
        this.populationId = populationId;
    }

    public int getAssemblyId() {
        return assemblyId;
    }

    public void setAssemblyId(int assemblyId) {
        this.assemblyId = assemblyId;
    }

    public int getMethodId() {
        return methodId;
    }

    public void setMethodId(int methodId) {
        this.methodId = methodId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        if (fileName != null)
            return fileName;
        else
            return "";
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileCol() {
        return fileCol;
    }

    public void setFileCol(int fileCol) {
        this.fileCol = fileCol;
    }

    public int getFileRow() {
        return fileRow;
    }

    public void setFileRow(int fileRow) {
        this.fileRow = fileRow;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getParentFeatureId() {
        return parentFeatureId;
    }

    public void setParentFeatureId(int parentFeatureId) {
        this.parentFeatureId = parentFeatureId;
    }
}
