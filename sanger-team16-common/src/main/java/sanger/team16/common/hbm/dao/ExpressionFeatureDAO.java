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

import sanger.team16.common.hbm.ExpressionFeature;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class ExpressionFeatureDAO extends AbstractDAO
{
    public ExpressionFeatureDAO(String address) {
        super(address);
    }

    @SuppressWarnings("unchecked")
    public List<ExpressionFeature> listWhereStudyId(int studyId) throws RuntimeException {
        List<ExpressionFeature> objects = this.getSession().createQuery("SELECT f FROM ExpressionFeature f, Study s, Population p WHERE s.publicity = 'Y' AND s.id = :studyId AND p.studyId = s.id AND f.populationId = p.id AND f.parentFeatureId = 0 ORDER BY f.populationId, f.platformId, f.tissueTypeId")
        .setParameter("studyId", studyId)
        .list();
        
        this.commit();
        return objects;
    } 

    @SuppressWarnings("unchecked")
    public List<Integer> list(int populationId, int platformId, int tissueTypeId, int methodId) {
        List<Integer> objects =  this.getSession().createQuery("SELECT id FROM ExpressionFeature WHERE parentFeatureId = 0 AND populationId = :populationId AND platformId = :platformId AND tissueTypeId = :tissueTypeId AND methodId = :methodId ORDER BY populationId, platformId, tissueTypeId")
        .setParameter("populationId", new Integer(populationId))
        .setParameter("platformId", new Integer(platformId))
        .setParameter("tissueTypeId", new Integer(tissueTypeId))
        .setParameter("methodId", new Integer(methodId))
        .list();
       
        this.commit();   //BUG FIX 04/02/10
        return objects;
    }
    
    public void updateExpressionFeature(int expressionFeatureId, String fileName, int fileCol, int fileRow) {
        ExpressionFeature feature = (ExpressionFeature) this.getSession().get(ExpressionFeature.class, new Integer(expressionFeatureId));
        feature.setFileName(fileName);
        feature.setFileCol(fileCol);
        feature.setFileRow(fileRow); 
        feature.setParentFeatureId(0);
        
        this.commit();
    }

    public void updateParentFeatureId(List<Integer> expressionFeatureIds) {
        for (int i=0 ; i<expressionFeatureIds.size() ; i++) {
            ExpressionFeature feature = (ExpressionFeature) this.getSession().get(ExpressionFeature.class, expressionFeatureIds.get(i));
            feature.setParentFeatureId(-1);
        }
        
        this.commit(); 
    }
    
    public void updateParentFeatureId(List<Integer> expressionFeatureIds, int parentFeatureId) {
        for (int i=0 ; i<expressionFeatureIds.size() ; i++) {
            ExpressionFeature feature = (ExpressionFeature) this.getSession().get(ExpressionFeature.class, expressionFeatureIds.get(i));
            feature.setParentFeatureId(parentFeatureId);
        }
        
        this.commit();
    }
}

/*
    public void updateNextExpressionFeatureId(List<Integer> expressionFeatureIds, int nextExpressionFeatureId) {
        for (int i=0 ; i<expressionFeatureIds.size() ; i++) {
            ExpressionFeature populationExpression = (ExpressionFeature) session.get(ExpressionFeature.class, expressionFeatureIds.get(i));
            populationExpression.setParentFeatureId(nextExpressionFeatureId);
        }
        
        session.getTransaction().commit();        
    }
*/
/*
    @SuppressWarnings("unchecked")
    private List<ExpressionFeature> list() throws RuntimeException {
        List<ExpressionFeature> lists = session.createQuery("FROM ExpressionFeature WHERE parentFeatureId = 0 ORDER BY populationId, platformId, tissueTypeId").list();
        
        session.getTransaction().commit();
        return lists;
    }    
    
    public List<ExpressionFeature> listWherePopulationAndPlatform(int populationId, int size) {
        List<ExpressionFeature> expressionFeatures = this.list();
        
        List<ExpressionFeature> arrayList = new ArrayList<ExpressionFeature>();
        for (int i=0 ; i<expressionFeatures.size() ; i++) {
            ExpressionFeature expressionFeature = expressionFeatures.get(i);
            
            for (int j=1 ; j<size ; j++) {
                if (((expressionFeature.getPopulationId() == populationId) && (expressionFeature.getPlatformId() == j)))
                    arrayList.add(expressionFeatures.get(i));
            }
        }

        return arrayList;
    }

public int uniqueResult(int populationId, int platformId, int tissueTypeId, int normalizationId) {
    List<Integer> lists =  session.createQuery("SELECT id FROM PopulationExpression WHERE nextPopulationExpressionId = 0 AND populationId = :populationId AND platformId = :platformId AND tissueTypeId = :tissueTypeId AND normalizationId = :normalizationId ORDER BY populationId, platformId, tissueTypeId")
    .setParameter("populationId", new Integer(populationId))
    .setParameter("platformId", new Integer(platformId))
    .setParameter("tissueTypeId", new Integer(tissueTypeId))
    .setParameter("normalizationId", new Integer(normalizationId))
    .list();
   
    transaction.commit();   //BUG FIX 04/02/10
    int size = lists.size();   //ADD 04/02/10 To clean up previous uploads which are failed
    
    if (size != 0) {
        int lastPopulationExpression = this.returnId(lists.get(size - 1));
        if (size != 1) {
            for (int i=0 ; i<lists.size() - 1 ; i++)
                new PopulationExpressionDAO().updateNextPopulationExpressionId(this.returnId(lists.get(i)), lastPopulationExpression);
        }
        return lastPopulationExpression;
    }
    return 0;
}

public int createOrUniqueResult(int populationId, int platformId, int tissueTypeId, int methodId, String description) {
    int id = this.uniqueResult(populationId, platformId, tissueTypeId, methodId);

    if (id == 0)
        return this.save(populationId, platformId, tissueTypeId, methodId, description);
    else
        return id;   
}
*/