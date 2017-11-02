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
package sanger.team16.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import sanger.team16.common.hbm.TranscriptMapping;
import sanger.team16.common.hbm.Variation;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@WebService(name="geneRetrieval")
public interface GeneRetrievalService
{
    @WebMethod
    public List<TranscriptMapping> getGenes(@WebParam(name="platformId") int platformId, @WebParam(name="referenceId") int referenceId, @WebParam(name="queries") List<String> queries, @WebParam(name="matched") boolean matched);

    @WebMethod
    public List<TranscriptMapping> getTranscripts(@WebParam(name="platformId") int platformId, @WebParam(name="referenceId") int referenceId, @WebParam(name="query") String query);

    @WebMethod
    public List<TranscriptMapping> getTranscriptsWhereSNP(@WebParam(name="platformId") int platformId, @WebParam(name="referenceId") int referenceId, @WebParam(name="snp") Variation snp, @WebParam(name="distance") int distance);
}
