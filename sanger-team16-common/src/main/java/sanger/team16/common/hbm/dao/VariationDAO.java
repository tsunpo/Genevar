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

import sanger.team16.common.hbm.Variation;
import sanger.team16.common.hbm.VariationInfo;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class VariationDAO extends AbstractDAO
{
    public VariationDAO(String address) {
        super(address);
    }
/*
    public Variation load(int variationId) {
        return (Variation) this.getSession().load(Variation.class, variationId);
    }*/

    @SuppressWarnings("unchecked")
    public Variation list(int studyId, String name) throws RuntimeException {
        List<Variation> objects = this.getSession().createQuery("FROM Variation WHERE studyId = :studyId AND name = :name")
        .setParameter("studyId", studyId)
        .setParameter("name", name)
        .list();
        
        this.commit();
        if (objects.size() == 0)   //TODO
            return null;
        else
            return objects.get(0);
    }
    
    @SuppressWarnings("unchecked")
    public List<Variation> list(int studyId, String chromosome, int transcriptionStartSite, int distance) throws RuntimeException {
        List<Variation> objects = this.getSession().createQuery("FROM Variation WHERE studyId = :studyId AND chromosome = :chromosome AND position > :lowerLimit AND position < :upperLimit")
        .setParameter("studyId", studyId)
        .setParameter("chromosome", chromosome)
        .setParameter("lowerLimit", transcriptionStartSite - distance)
        .setParameter("upperLimit", transcriptionStartSite + distance)
        .list();
        
        this.commit();
        return objects;
    }

    public int uniqueResultOrSaveNotCommitted(int studyId, int genotypeFeatureId, String name, String chromosome, int position, String alleles) {
        Integer id = (Integer) this.getSession().createQuery("SELECT id FROM Variation WHERE studyId = :studyId AND name = :name")
        .setParameter("studyId", studyId)
        .setParameter("name", name)
        .uniqueResult();
        
        if (id == null) 
            return save(new Variation(studyId, genotypeFeatureId, name, chromosome, position, alleles), false);
        return this.returnId(id);
    }
    
    public int uniqueResultOrSaveNotCommitted(int studyId, int genotypeFeatureId, String name, String chromosome, int position, String alleles, double info, double freq1) {
        Integer id = (Integer) this.getSession().createQuery("SELECT id FROM Variation WHERE studyId = :studyId AND name = :name")
        .setParameter("studyId", studyId)
        .setParameter("name", name)
        .uniqueResult();
        
        if (id == null) {
        	int variationId = save(new Variation(studyId, genotypeFeatureId, name, chromosome, position, alleles), false);
        	persist(new VariationInfo(variationId, info, freq1), false);
        	return variationId;
        }
        return this.returnId(id);
    }
    
    /**
     * For MuTHER 2013, >= chr10
     */
    public int getMaxVariationId() {
        Integer id = (Integer) this.getSession().createQuery("SELECT max(id) FROM Variation")
        .uniqueResult();
        
        this.commit();
        return this.returnId(id);
    }
    
    /**
     * Command line mode
     */
    public Variation uniqueResult(int studyId, String name) throws RuntimeException {
        Variation object = (Variation) this.getSession().createQuery("FROM Variation WHERE studyId = :studyId AND name = :name")
        .setParameter("studyId", studyId)
        .setParameter("name", name)
        .uniqueResult();
        
        this.commit();
        return object;
    }
}
