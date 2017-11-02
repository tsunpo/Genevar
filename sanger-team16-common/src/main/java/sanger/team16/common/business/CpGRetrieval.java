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
package sanger.team16.common.business;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import sanger.team16.common.hbm.ModificationMapping;
import sanger.team16.common.hbm.Variation;
import sanger.team16.common.hbm.dao.ModificationMappingDAO;
import sanger.team16.service.CpGRetrievalService;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@WebService(endpointInterface = "sanger.team16.service.CpGRetrievalService", serviceName = "CpGRetrieval")
public class CpGRetrieval extends AbstractRetrieval implements CpGRetrievalService
{ 
    public CpGRetrieval() {
        super();
    }
    
    public CpGRetrieval(String address) {
        super(address);
    }

    /**
     * cis-eQTL - Gene
     */
    @WebMethod
    public List<ModificationMapping> getCpGs(int platformId, int referenceId, List<String> queries, boolean matched) {
        return new ModificationMappingDAO(address).listWhereCpG(platformId, referenceId, queries, matched);
    }
    
    @WebMethod
    public List<ModificationMapping> getModifications(int platformId, int referenceId, String query) {
        return new ModificationMappingDAO(address).list(platformId, referenceId, query);
    }
    
    /**
     * cis-eQTL - SNP
     */
    @WebMethod
    public List<ModificationMapping> getModificationsWhereSNP(int platformId, int referenceId, Variation snp, int distance) {
        // From both sense and anti-sense strands
        List<ModificationMapping> modificationMappings = new ModificationMappingDAO(address).listWhereSNP(platformId, referenceId, snp.getChromosome(), snp.getPosition(), distance);

        return modificationMappings;
    }
}
