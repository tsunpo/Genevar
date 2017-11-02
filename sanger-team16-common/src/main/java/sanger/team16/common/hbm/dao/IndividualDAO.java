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

import sanger.team16.common.hbm.Individual;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class IndividualDAO extends AbstractDAO
{
    public IndividualDAO(String address) {
        super(address);
    }

    public int uniqueResult(String name) throws RuntimeException {
        Integer id = (Integer) this.getSession().createQuery("SELECT id FROM Individual WHERE name = :name")
        .setParameter("name", name)
        .uniqueResult();
   
        this.commit();
        return this.returnId(id);
    }
    
    public int uniqueResultOrSave(int populationId, String name) throws RuntimeException {
        Integer id = (Integer) this.getSession().createQuery("SELECT id FROM Individual WHERE populationId = :populationId AND name = :name")
        .setParameter("populationId", populationId)
        .setParameter("name", name)
        .uniqueResult();
   
        if (id == null)
            id = save(new Individual(populationId, name), true);
        return this.returnId(id);
    }
}

/*   //BUG 18/02/10
public int uniqueResultOrSaveNotClose(int populationId, String name) throws RuntimeException {
    Integer id = (Integer) session.createQuery("SELECT i.id FROM Individual i WHERE i.name = :name")
    .setParameter("name", name)
    .uniqueResult();

    if (id == null)
        id = saveNotClose(new Individual(populationId, name));
    return this.returnId(id);
}*/