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
public class AnalysisFeature
{
    private int id;
    private int studyId;
    private int algorithmId;
    private int genotypeFeatureId;
    private int methylationFeatureId;
    private int expressionFeatureId;
    private int phenotypeFeatureId;
    private String description;
    private String fileName;
    private String createdBy;
    private Date created;
    private int parentFeatureId;

    public AnalysisFeature() {}

    public AnalysisFeature(int studyId, int algorithmId, int genotypeFeatureId, int methylationFeatureId, int expressionFeatureId, int phenotypeFeatureId, String description, String createdBy) {
        this.setStudyId(studyId);
        this.setAlgorithmId(algorithmId);
        this.setGenotypeFeatureId(genotypeFeatureId);
        this.setMethylationFeatureId(methylationFeatureId);
        this.setExpressionFeatureId(expressionFeatureId);
        this.setPhenotypeFeatureId(phenotypeFeatureId);
        this.setDescription(description);
        this.createdBy = createdBy;
        this.created = new Date();
        this.parentFeatureId = -1;
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

    public int getAlgorithmId() {
        return algorithmId;
    }

    public void setAlgorithmId(int algorithmId) {
        this.algorithmId = algorithmId;
    }

    public int getGenotypeFeatureId() {
        return genotypeFeatureId;
    }

    public void setGenotypeFeatureId(int genotypeFeatureId) {
        this.genotypeFeatureId = genotypeFeatureId;
    }

    public int getMethylationFeatureId() {
        return methylationFeatureId;
    }

    public void setMethylationFeatureId(int methylationFeatureId) {
        this.methylationFeatureId = methylationFeatureId;
    }

    public int getExpressionFeatureId() {
        return expressionFeatureId;
    }

    public void setExpressionFeatureId(int expressionFeatureId) {
        this.expressionFeatureId = expressionFeatureId;
    }

    public int getPhenotypeFeatureId() {
        return phenotypeFeatureId;
    }

    public void setPhenotypeFeatureId(int phenotypeFeatureId) {
        this.phenotypeFeatureId = phenotypeFeatureId;
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
