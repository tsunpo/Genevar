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

import sanger.team16.common.hbm.Analysis;
import sanger.team16.common.hbm.ExpressionFeature;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class AnalysisDAO extends AbstractDAO
{
    public AnalysisDAO(String address) {
        super(address);
    }

    @SuppressWarnings("unchecked")
    public Analysis listQTLProbABEL(int analysisFeatureId, int probePrimaryId, int variationId) throws RuntimeException {
        List<Object> objects = this.getSession().createQuery("SELECT value, value1, value2, value3, value4 FROM Analysis WHERE analysisFeatureId = :analysisFeatureId AND probePrimaryId = :probePrimaryId AND variationId = :variationId")
        .setParameter("analysisFeatureId", analysisFeatureId)
        .setParameter("probePrimaryId", probePrimaryId)
        .setParameter("variationId", variationId)
        .list();
        
        this.commit();
        if (objects.size() == 0)
            return null;
        else {
            Object[] columns = (Object[]) objects.get(0);
            return new Analysis(((Double) columns[0]).doubleValue(), ((Double) columns[1]).doubleValue(), ((Double) columns[2]).doubleValue(), ((Double) columns[3]).doubleValue(), ((Double) columns[4]).doubleValue());    
        }
    } 

    
    
    
    
    
    @SuppressWarnings("unchecked")
    public List<Integer> list(int populationId, int platformId, int tissueTypeId, int methodId) {
        List<Integer> objects =  this.getSession().createQuery("SELECT id FROM ExpressionFeature WHERE parentFeatureId = 0 AND populationId = :populationId AND platformId = :platformId AND tissueTypeId = :tissueTypeId ORDER BY populationId, platformId, tissueTypeId")
        .setParameter("populationId", new Integer(populationId))
        .setParameter("platformId", new Integer(platformId))
        .setParameter("tissueTypeId", new Integer(tissueTypeId))
        .list();
       
        this.commit();   //BUG FIX 04/02/10
        return objects;
    }
    
    public void updateExpressionFeature(int expressionFeatureId, String fileName, int fileCol, int fileRow) {
        ExpressionFeature populationExpression = (ExpressionFeature) this.getSession().get(ExpressionFeature.class, new Integer(expressionFeatureId));
        populationExpression.setFileName(fileName);
        populationExpression.setFileCol(fileCol);
        populationExpression.setFileRow(fileRow); 
        populationExpression.setParentFeatureId(0);
        
        this.commit();        
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
