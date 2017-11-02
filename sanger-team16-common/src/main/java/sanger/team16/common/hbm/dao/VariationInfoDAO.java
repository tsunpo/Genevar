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
package sanger.team16.common.hbm.dao;

import java.util.List;

import sanger.team16.common.hbm.VariationInfo;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class VariationInfoDAO extends AbstractDAO
{
    public VariationInfoDAO(String address) {
        super(address);
    }
    
    @SuppressWarnings("unchecked")
    public VariationInfo list(int variationId) throws RuntimeException {
        List<VariationInfo> objects = this.getSession().createQuery("FROM VariationInfo WHERE variationId = :variationId")
        .setParameter("variationId", variationId)
        .list();
        
        this.commit();
        if (objects.size() == 0)   //TODO
            return null;
        else
            return objects.get(0);
    }
/*
    public int uniqueResultOrSaveNotClose(int variationId, double info, double freq1) {
        Integer id = (Integer) this.getSession().createQuery("SELECT id FROM VariationInfo WHERE variationId = :variationId AND name = :name")
        .setParameter("variationId", variationId)
        .setParameter("info", info)
        .setParameter("freq1", freq1)
        .uniqueResult();
        
        if (id == null) 
            return saveNotClose(new Variation(studyId, name, chromosome, position, alleles));
        return id.intValue();
    }*/
}
