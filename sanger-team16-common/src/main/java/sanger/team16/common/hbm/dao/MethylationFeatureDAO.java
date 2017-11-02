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

import sanger.team16.common.hbm.MethylationFeature;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class MethylationFeatureDAO extends AbstractDAO
{
    public MethylationFeatureDAO(String address) {
        super(address);
    }

    @SuppressWarnings("unchecked")
    public List<MethylationFeature> listWhereStudyId(int studyId) throws RuntimeException {
        List<MethylationFeature> objects = this.getSession().createQuery("SELECT f FROM MethylationFeature f, Study s, Population p WHERE s.publicity = 'Y' AND s.id = :studyId AND p.studyId = s.id AND f.populationId = p.id AND f.parentFeatureId = 0 ORDER BY f.populationId, f.platformId, f.tissueTypeId")
        .setParameter("studyId", studyId)
        .list();
        
        this.commit();
        return objects;
    } 

    @SuppressWarnings("unchecked")
    public List<Integer> list(int populationId, int platformId, int tissueTypeId, int methodId) {
        List<Integer> objects =  this.getSession().createQuery("SELECT id FROM MethylationFeature WHERE parentFeatureId = 0 AND populationId = :populationId AND platformId = :platformId AND tissueTypeId = :tissueTypeId AND methodId = :methodId ORDER BY populationId, platformId, tissueTypeId")
        .setParameter("populationId", new Integer(populationId))
        .setParameter("platformId", new Integer(platformId))
        .setParameter("tissueTypeId", new Integer(tissueTypeId))
        .setParameter("methodId", new Integer(methodId))
        .list();
       
        this.commit();   //BUG FIX 04/02/10
        return objects;
    }
    
    public void updateMethylationFeature(int methylationFeatureId, String fileName, int fileCol, int fileRow) {
        MethylationFeature feature = (MethylationFeature) this.getSession().get(MethylationFeature.class, new Integer(methylationFeatureId));
        feature.setFileName(fileName);
        feature.setFileCol(fileCol);
        feature.setFileRow(fileRow); 
        feature.setParentFeatureId(0);
        
        this.commit();
    }
    
    public void updateParentFeatureId(List<Integer> methylationFeatureIds) {
        for (int i=0 ; i<methylationFeatureIds.size() ; i++) {
            MethylationFeature feature = (MethylationFeature) this.getSession().get(MethylationFeature.class, methylationFeatureIds.get(i));
            feature.setParentFeatureId(-1);
        }
        
        this.commit();
    }
}
