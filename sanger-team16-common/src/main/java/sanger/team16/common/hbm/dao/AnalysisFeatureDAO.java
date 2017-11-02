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
package sanger.team16.common.hbm.dao;

import java.util.List;

import sanger.team16.common.hbm.AnalysisFeature;
import sanger.team16.common.hbm.ExpressionFeature;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class AnalysisFeatureDAO extends AbstractDAO
{
    public AnalysisFeatureDAO(String address) {
        super(address);
    }

    @SuppressWarnings("unchecked")
    public List<AnalysisFeature> listEQTL(int algorithmId, int genotypeFeatureId, int expressionFeatureId) throws RuntimeException {
        List<AnalysisFeature> objects = this.getSession().createQuery("SELECT f FROM AnalysisFeature f, Study s WHERE s.publicity = 'Y' AND f.studyId = s.id AND f.algorithmId = :algorithmId AND f.genotypeFeatureId = :genotypeFeatureId AND f.methylationFeatureId = 0 AND f.expressionFeatureId = :expressionFeatureId AND f.phenotypeFeatureId = 0 AND f.parentFeatureId = 0 ORDER BY f.algorithmId")
        .setParameter("algorithmId", algorithmId)
        .setParameter("genotypeFeatureId", genotypeFeatureId)
        .setParameter("expressionFeatureId", expressionFeatureId)
        .list();
        
        this.commit();
        return objects;
    }
    
    @SuppressWarnings("unchecked")
    public List<AnalysisFeature> listMQTL(int algorithmId, int genotypeFeatureId, int methylationFeatureId) throws RuntimeException {
        List<AnalysisFeature> objects = this.getSession().createQuery("SELECT f FROM AnalysisFeature f, Study s WHERE s.publicity = 'Y' AND f.studyId = s.id AND f.algorithmId = :algorithmId AND f.genotypeFeatureId = :genotypeFeatureId AND f.methylationFeatureId = :methylationFeatureId AND f.expressionFeatureId = 0 AND f.phenotypeFeatureId = 0 AND f.parentFeatureId = 0 ORDER BY f.algorithmId")
        .setParameter("algorithmId", algorithmId)
        .setParameter("genotypeFeatureId", genotypeFeatureId)
        .setParameter("methylationFeatureId", methylationFeatureId)
        .list();
        
        this.commit();
        return objects;
    } 

    public void updateAnalysisFeature(int analysisFeatureId, String fileName) {
    	AnalysisFeature analysisFeature = (AnalysisFeature) this.getSession().get(AnalysisFeature.class, new Integer(analysisFeatureId));
        analysisFeature.setFileName(fileName);
        analysisFeature.setParentFeatureId(0);
        
        this.commit();        
    }
    
    
    
    
    
    
    @SuppressWarnings("unchecked")
    public List<Integer> list(int populationId, int platformId, int tissueTypeId, int methodId) {
        List<Integer> lists =  this.getSession().createQuery("SELECT id FROM ExpressionFeature WHERE parentFeatureId = 0 AND populationId = :populationId AND platformId = :platformId AND tissueTypeId = :tissueTypeId ORDER BY populationId, platformId, tissueTypeId")
        .setParameter("populationId", new Integer(populationId))
        .setParameter("platformId", new Integer(platformId))
        .setParameter("tissueTypeId", new Integer(tissueTypeId))
        .list();
       
        this.commit();   //BUG FIX 04/02/10
        return lists;
    }

    public void updateParentFeatureId(List<Integer> expressionFeatureIds) {
        for (int i=0 ; i<expressionFeatureIds.size() ; i++) {
            ExpressionFeature populationExpression = (ExpressionFeature) this.getSession().get(ExpressionFeature.class, expressionFeatureIds.get(i));
            populationExpression.setParentFeatureId(-1);
        }
        
        this.commit();        
    }
    
    public void updateParentFeatureId(List<Integer> expressionFeatureIds, int parentFeatureId) {
        for (int i=0 ; i<expressionFeatureIds.size() ; i++) {
            ExpressionFeature populationExpression = (ExpressionFeature) this.getSession().get(ExpressionFeature.class, expressionFeatureIds.get(i));
            populationExpression.setParentFeatureId(parentFeatureId);
        }
        
        this.commit();        
    }
}
