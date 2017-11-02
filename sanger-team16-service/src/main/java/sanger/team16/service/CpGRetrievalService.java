/**
 *  This file is part of Genevar (GENe Expression VARiation)
 *  Copyright (C) 2013  Genome Research Ltd.
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
package sanger.team16.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import sanger.team16.common.hbm.ModificationMapping;
import sanger.team16.common.hbm.Variation;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 * @date   21/09/13
 */
@WebService(name="cpgRetrieval")
public interface CpGRetrievalService
{
    @WebMethod
    public List<ModificationMapping> getCpGs(@WebParam(name="platformId") int platformId, @WebParam(name="referenceId") int referenceId, @WebParam(name="queries") List<String> queries, @WebParam(name="matched") boolean matched);

    @WebMethod
    public List<ModificationMapping> getModifications(@WebParam(name="platformId") int platformId, @WebParam(name="referenceId") int referenceId, @WebParam(name="query") String query);

    @WebMethod
    public List<ModificationMapping> getModificationsWhereSNP(@WebParam(name="platformId") int platformId, @WebParam(name="referenceId") int referenceId, @WebParam(name="snp") Variation snp, @WebParam(name="distance") int distance);
}
