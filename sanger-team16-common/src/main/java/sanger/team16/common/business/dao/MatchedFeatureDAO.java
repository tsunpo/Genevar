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
package sanger.team16.common.business.dao;

import java.util.ArrayList;
import java.util.List;

import sanger.team16.common.hbm.ExpressionFeature;
import sanger.team16.common.hbm.GenotypeFeature;
import sanger.team16.common.hbm.MethylationFeature;
import sanger.team16.common.hbm.TissueType;
import sanger.team16.common.hbm.dao.AbstractDAO;

public class MatchedFeatureDAO extends AbstractDAO
{
    public MatchedFeatureDAO(String address) {
        super(address);
    }
    
    /**
       SELECT p.name, t.name, g.description, e.description, g.genotype_feature_id, e.expression_feature_id
       FROM genotype_feature g, expression_feature e, study s, population p, tissue_type t
       WHERE s.publicity = 'Y'
       AND s.study_id = 1
       AND p.study_id = s.study_id
       AND s.genotype_assembly_id = g.assembly_id
       AND s.expression_platform_id = e.platform_id
       AND e.population_id = g.population_id
       AND g.parent_feature_id = 0
       AND e.parent_feature_id = 0
       AND e.population_id = p.population_id
       AND t.tissue_type_id = e.tissue_type_id
       ORDER BY p.name, t.name
     */
    @SuppressWarnings("unchecked")
    public List<MatchedFeature> listGxE(int studyId) {
        List<MatchedFeature> matchedFeatures = new ArrayList<MatchedFeature>();
        
        List<Object> objects = this.getSession().createQuery(
        "SELECT g.id, g.description, e.id, e.description, p.name, e.tissueTypeId, t.name, e.platformId" +
        " FROM GenotypeFeature g, ExpressionFeature e, Study s, Population p, TissueType t" +
        " WHERE s.publicity = 'Y'" +
        " AND s.id = :studyId" +
        " AND p.studyId = s.id" +
        " AND s.genotypeAssemblyId = g.assemblyId" +
        " AND s.expressionPlatformId = e.platformId" +
        " AND e.populationId = g.populationId" +
        " AND g.parentFeatureId = 0" +
        " AND e.parentFeatureId = 0" +
        " AND e.populationId = p.id" +
        " AND t.id = e.tissueTypeId" +
        " ORDER BY p.name, t.name")
        .setParameter("studyId", studyId)
        .list();

        if (objects.size() != 0)
            for (int i=0 ; i<objects.size() ; i++) {
                Object[] columns = (Object[]) objects.get(i);
                //System.out.println((String) columns[0] +"\t"+ (String) columns[1] +"\t"+ (String) columns[2] +"\t"+ (String) columns[3]);
                
                GenotypeFeature genotypeFeature = new GenotypeFeature((Integer) columns[0], (String) columns[1]);
                ExpressionFeature expressionFeature = new ExpressionFeature((Integer) columns[2], (String) columns[3]);
                String populationName = (String) columns[4];
                TissueType tissueType = new TissueType((Integer) columns[5], (String) columns[6]);
                int platformId = (Integer) columns[7];
                matchedFeatures.add(new MatchedFeature(populationName, tissueType, genotypeFeature, new MethylationFeature(), expressionFeature, platformId));
            }
        
        this.commit();
        return matchedFeatures;
    }
    
    /**
    SELECT p.name, t.name, g.description, m.description, g.genotype_feature_id, m.methylation_feature_id
    FROM genotype_feature g, methylation_feature m, study s, population p, tissue_type t
    WHERE s.publicity = 'Y'
    AND s.study_id = 1
    AND p.study_id = s.study_id
    AND s.genotype_assembly_id = g.assembly_id
    AND s.methylation_platform_id = m.platform_id
    AND m.population_id = g.population_id
    AND g.parent_feature_id = 0
    AND m.parent_feature_id = 0
    AND m.population_id = p.population_id
    AND t.tissue_type_id = m.tissue_type_id
    ORDER BY p.name, t.name
    */
    @SuppressWarnings("unchecked")
    public List<MatchedFeature> listGxM(int studyId) {
        List<MatchedFeature> matchedFeatures = new ArrayList<MatchedFeature>();
        
        List<Object> objects = this.getSession().createQuery(
        "SELECT g.id, g.description, m.id, m.description, p.name, m.tissueTypeId, t.name, m.platformId" +
        " FROM GenotypeFeature g, MethylationFeature m, Study s, Population p, TissueType t" +
        " WHERE s.publicity = 'Y'" +
        " AND s.id = :studyId" +
        " AND p.studyId = s.id" +
        " AND s.genotypeAssemblyId = g.assemblyId" +
        " AND s.methylationPlatformId = m.platformId" +
        " AND m.populationId = g.populationId" +
        " AND g.parentFeatureId = 0" +
        " AND m.parentFeatureId = 0" +
        " AND m.populationId = p.id" +
        " AND t.id = m.tissueTypeId" +
        " ORDER BY p.name, t.name")
        .setParameter("studyId", studyId)
        .list();

        if (objects.size() != 0)
            for (int i=0 ; i<objects.size() ; i++) {
                Object[] columns = (Object[]) objects.get(i);
                //System.out.println((String) columns[0] +"\t"+ (String) columns[1] +"\t"+ (String) columns[2] +"\t"+ (String) columns[3]);
                
                GenotypeFeature genotypeFeature = new GenotypeFeature((Integer) columns[0], (String) columns[1]);
                MethylationFeature methylationFeature = new MethylationFeature((Integer) columns[2], (String) columns[3]);
                String populationName = (String) columns[4];
                TissueType tissueType = new TissueType((Integer) columns[5], (String) columns[6]);
                int platformId = (Integer) columns[7];
                matchedFeatures.add(new MatchedFeature(populationName, tissueType, genotypeFeature, methylationFeature, new ExpressionFeature(), platformId));
            }
        
        this.commit();
        return matchedFeatures;
    }
}

/*
 * MSE
 *//*
@SuppressWarnings("unchecked")
public List<MatchedFeature> listMSE(int datasetId) {
    List<MatchedFeature> matchedFeatures = new ArrayList<MatchedFeature>();
    
    List<Object> lists = session.createQuery(
    "SELECT p.name, e.description, m.description, e.id, m.id, t.name" +
    " FROM PopulationExpression e, PopulationMethylation m, Dataset d, Population p, TissueType t" +
    " WHERE prj.id = :datasetId" +
    " AND p.datasetId = prj.id" +
    " AND prj.expressionPlatformId = e.platformId" +
    " AND prj.methylationPlatformId = m.platformId" +
    " AND e.populationId = m.populationId" +
    " AND e.parentFeatureId = 0" +
    " AND m.parentFeatureId = 0" +        
    " AND e.populationId = p.id" +
    " AND t.id = e.tissueTypeId" +
    " ORDER BY p.name")
    .setParameter("datasetId", datasetId)
    .list();

    if (lists.size() != 0)
        for (int i=0 ; i<lists.size() ; i++) {
            Object[] columns = (Object[]) lists.get(i);
            
            matchedFeatures.add(new MatchedFeature((String) columns[0], (String) columns[1], (String) columns[2], (Integer) columns[3], (Integer) columns[4], (String) columns[5]));
        }
    
    session.getTransaction().commit();
    return matchedFeatures;
}*/