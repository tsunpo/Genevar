/**
 *  This file is part of Genevar (GENe Expression VARiation)
 *  Copyright (C) 2012  Genome Research Ltd.
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

import sanger.team16.common.business.dao.Tuple;
import sanger.team16.common.hbm.TranscriptMapping;
import sanger.team16.common.hbm.Variation;
import sanger.team16.common.xqtl.QTL;
import sanger.team16.common.xqtl.QTLList;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 * @date   08/06/12
 */
@WebService(name="eqtlRetrieval")
public interface EQTLRetrievalService
{
    @WebMethod
    public QTLList getEQTLsWhereGene(@WebParam(name="studyId") int studyId, @WebParam(name="genotypeFeatureId") int genotypeFeatureId, @WebParam(name="expressionFeatureId") int expressionFeatureId, @WebParam(name="transcriptId") int transcriptId, @WebParam(name="chromosome") String chromosome, @WebParam(name="transcriptionStartSite") int transcriptionStartSite, @WebParam(name="statisticId") int statisticId, @WebParam(name="threshold") double threshold, @WebParam(name="distance") int distance);

    @WebMethod
    public List<QTL> getEQTLsWhereSNP(@WebParam(name="genotypeFeatureId") int genotypeFeatureId, @WebParam(name="snp") Variation snp, @WebParam(name="expressionFeatureId") int expressionFeatureId, @WebParam(name="transcriptMappings") List<TranscriptMapping> transcriptMappings, @WebParam(name="statisticId") int statisticId, @WebParam(name="threshold") double threshold);

    @WebMethod
    public Tuple getEQTLTuple(@WebParam(name="genotypeFeatureId") int genotypeFeatureId, @WebParam(name="snp") Variation snp, @WebParam(name="expressionFeatureId") int expressionFeatureId, @WebParam(name="transcriptId") int transcriptId, @WebParam(name="statisticId") int statisticId, @WebParam(name="permutation") int permutation);
}
