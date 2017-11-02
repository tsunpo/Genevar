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

import sanger.team16.common.hbm.Modification;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class ModificationDAO extends AbstractDAO
{
    public ModificationDAO(String address) {
        super(address);
    }
    
    @SuppressWarnings("unchecked")
    public Modification list(int platformId, String probeId) throws RuntimeException {
        List<Modification> objects = this.getSession().createQuery("FROM Modification WHERE platformId = :platformId AND probeId = :probeId")
        .setParameter("platformId", platformId)
        .setParameter("probeId", probeId)
        .list();
        
        this.commit();
        if (objects.size() == 0)
            return null;
        else
            return objects.get(0);
    }
    
    @SuppressWarnings("unchecked")
    public List<Modification> list(int platformId, int xrefAssemblyId, String chromosome, int transcriptionStartSite, int distance) throws RuntimeException {
        List<Modification> lists = this.getSession().createQuery("FROM Modification WHERE platformId = :platformId AND xrefAssemblyId = :xrefAssemblyId AND chromosome = :chromosome AND position > :lowerLimit AND position < :upperLimit")
        .setParameter("platformId", platformId)
        .setParameter("xrefAssemblyId", xrefAssemblyId)
        .setParameter("chromosome", chromosome)
        .setParameter("lowerLimit", transcriptionStartSite - distance)
        .setParameter("upperLimit", transcriptionStartSite + distance)
        .list();
        
        this.commit();
        return lists;
    }
    
    public int uniqueResult(int platformId, String probeId, boolean isCommitted) {
        Integer id = (Integer) this.getSession().createQuery("SELECT t.id FROM Modification t WHERE t.platformId = :platformId AND t.probeId = :probeId")
        .setParameter("platformId", platformId)
        .setParameter("probeId", probeId)
        .uniqueResult();

        if (isCommitted)
            this.commit();
        return this.returnId(id);
    }
}
