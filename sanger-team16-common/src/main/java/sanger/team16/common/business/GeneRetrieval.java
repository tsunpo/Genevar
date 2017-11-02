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

import sanger.team16.common.hbm.TranscriptMapping;
import sanger.team16.common.hbm.Variation;
import sanger.team16.common.hbm.dao.TranscriptMappingDAO;
import sanger.team16.service.GeneRetrievalService;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@WebService(endpointInterface = "sanger.team16.service.GeneRetrievalService", serviceName = "GeneRetrieval")
public class GeneRetrieval extends AbstractRetrieval implements GeneRetrievalService
{ 
    public GeneRetrieval() {
        super();
    }
    
    public GeneRetrieval(String address) {
        super(address);
    }

    /**
     * cis-eQTL - Gene
     */
    @WebMethod
    public List<TranscriptMapping> getGenes(int platformId, int referenceId, List<String> queries, boolean matched) {
        return new TranscriptMappingDAO(address).listWhereGene(platformId, referenceId, queries, matched);
    }
    
    @WebMethod
    public List<TranscriptMapping> getTranscripts(int platformId, int referenceId, String query) {
        return new TranscriptMappingDAO(address).list(platformId, referenceId, query);
    }
    
    /**
     * cis-eQTL - SNP
     */
    @WebMethod
    public List<TranscriptMapping> getTranscriptsWhereSNP(int platformId, int referenceId, Variation snp, int distance) {
        // From both sense and anti-sense strands
        List<TranscriptMapping> transcriptMappings = new TranscriptMappingDAO(address).listWhereSNP(platformId, referenceId, snp.getChromosome(), snp.getPosition(), distance, 1);
        transcriptMappings.addAll(new TranscriptMappingDAO(address).listWhereSNP(platformId, referenceId, snp.getChromosome(), snp.getPosition(), distance, -1));

        return transcriptMappings;
    }
}
