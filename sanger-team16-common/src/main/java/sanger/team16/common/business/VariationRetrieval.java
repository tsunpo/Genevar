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

import sanger.team16.common.hbm.Variation;
import sanger.team16.common.hbm.dao.VariationDAO;
import sanger.team16.service.VariationRetrievalService;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@WebService(endpointInterface = "sanger.team16.service.VariationRetrievalService", serviceName = "VariationRetrieval")
public class VariationRetrieval extends AbstractRetrieval implements VariationRetrievalService
{  
    public VariationRetrieval() {
        super();
    }
    
    public VariationRetrieval(String address) {
        super(address);
    }
    
    @WebMethod
    public Variation getSNP(int studyId, String variaitonName) {
        return new VariationDAO(address).list(studyId, variaitonName);
    }
    
    @WebMethod
    public List<Variation> getSNPs(int studyId, String chromosome, int transcriptionStartSite, int distance) {
        return new VariationDAO(address).list(studyId, chromosome, transcriptionStartSite, distance);
    }
}

/*
@WebMethod
public Variation getSNPWhereId(int variationId) {
    return new VariationDAO(address).load(variationId);
}*/