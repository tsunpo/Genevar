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

import sanger.team16.common.hbm.Algorithm;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class AlgorithmDAO extends AbstractDAO
{
    public AlgorithmDAO(String address) {
        super(address);
    }

    @SuppressWarnings("unchecked")
    public List<Algorithm> listEQTL(int studyId) throws RuntimeException {
        List<Algorithm> objects = this.getSession().createQuery("SELECT a FROM Algorithm a, AnalysisFeature f, Study s WHERE s.publicity = 'Y' AND f.studyId = s.id AND f.studyId = :studyId AND f.genotypeFeatureId != 0 AND f.methylationFeatureId = 0 AND f.expressionFeatureId != 0 AND f.phenotypeFeatureId = 0 AND a.id = f.algorithmId GROUP BY f.studyId ORDER BY a.name")
        .setParameter("studyId", studyId)
        .list();
        
        this.commit();
        return objects;
    }
    
    @SuppressWarnings("unchecked")
    public List<Algorithm> listMQTL(int studyId) throws RuntimeException {
        List<Algorithm> objects = this.getSession().createQuery("SELECT a FROM Algorithm a, AnalysisFeature f, Study s WHERE s.publicity = 'Y' AND f.studyId = s.id AND f.studyId = :studyId AND f.genotypeFeatureId != 0 AND f.methylationFeatureId != 0 AND f.expressionFeatureId = 0 AND f.phenotypeFeatureId = 0 AND a.id = f.algorithmId GROUP BY f.studyId ORDER BY a.name")
        .setParameter("studyId", studyId)
        .list();
        
        this.commit();
        return objects;
    }
}
