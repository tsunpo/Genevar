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

import sanger.team16.common.hbm.TissueType;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class TissueTypeDAO extends AbstractDAO
{
    public TissueTypeDAO(String address) {
        super(address);
    }

    @SuppressWarnings("unchecked")
    public List<TissueType> list() throws RuntimeException {
        List<TissueType> objects = this.getSession().createQuery("FROM TissueType ORDER BY name").list();
        
        this.commit();
        return objects;
    }
    
    public int uniqueResult(String name) {
        Integer id = (Integer) this.getSession().createQuery("SELECT id FROM TissueType WHERE name = :name")
        .setParameter("name", name)
        .uniqueResult();
        
        this.commit();
        return this.returnId(id);
    }
}
