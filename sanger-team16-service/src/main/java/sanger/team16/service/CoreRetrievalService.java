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
import java.util.Vector;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import sanger.team16.common.business.dao.MatchedFeature;
import sanger.team16.common.business.dao.Core;
import sanger.team16.common.hbm.Algorithm;
import sanger.team16.common.hbm.AnalysisFeature;
import sanger.team16.common.hbm.ExpressionFeature;
import sanger.team16.common.hbm.GenotypeFeature;
import sanger.team16.common.hbm.MethylationFeature;
import sanger.team16.common.hbm.Population;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@WebService(name="coreRetrieval")
public interface CoreRetrievalService
{ 
    @WebMethod
    public Core getCore();

    @WebMethod
    public List<Population> getPopulationsWhereStudyId(@WebParam(name="studyId") int studyId);

    /**
     * MatchedFeature
     */
    @WebMethod
    public List<MatchedFeature> getMatchedFeaturesGxE(@WebParam(name="studyId") int studyId);

    @WebMethod
    public List<MatchedFeature> getMatchedFeaturesGxM(@WebParam(name="studyId") int studyId);
    
    /**
     * GenotypePane & GenotypeUploadPane
     */
    @WebMethod
    public List<GenotypeFeature> getGenotypeFeaturesWhereStudyId(@WebParam(name="studyId") int studyId);
    
    @WebMethod
    public List<Integer> getGenotypeFeatureIds(@WebParam(name="populationId") int populationId, @WebParam(name="referenceId") int referenceId, @WebParam(name="methodId") int methodId);

    /**
     * MethylationPane & MethylationUploadPane
     */
    @WebMethod
    public List<MethylationFeature> getMethylationFeaturesWhereStudyId(@WebParam(name="studyId") int studyId);

    @WebMethod
    public List<Integer> getMethylationFeatureIds(@WebParam(name="populationId") int populationId, @WebParam(name="platformId") int platformId, @WebParam(name="tissueTypeId") int tissueTypeId, @WebParam(name="methodId") int methodId);
    
    /**
     * ExpressionPane & ExpressionUploadPane
     */
    @WebMethod
    public List<ExpressionFeature> getExpressionFeaturesWhereStudyId(@WebParam(name="studyId") int studyId);
    
    @WebMethod
    public List<Integer> getExpressionFeatureIds(@WebParam(name="populationId") int populationId, @WebParam(name="platformId") int platformId, @WebParam(name="tissueTypeId") int tissueTypeId, @WebParam(name="methodId") int methodId);

    /**
     * Algorithm & AnalysisFeature
     */
    @WebMethod
    public Vector<Algorithm> getQTLAlgorithmsWhereStudyId(@WebParam(name="studyId") int studyId, @WebParam(name="type") String type);

    @WebMethod
    public List<AnalysisFeature> getQTLAnalysisFeatures(@WebParam(name="algorithmId") int algorithmId, @WebParam(name="genotypeFeatureId") int genotypeFeatureId, @WebParam(name="traitFeatureId") int traitFeatureId, @WebParam(name="type") String type);
}

//@WebMethod
//public List<ExpressionFeature> getExpressionFeatures(@WebParam(name="populationId") int populationId, @WebParam(name="size") int size);

//@WebMethod
//public List<GenotypeFeature> getGenotypeFeatures(@WebParam(name="populationId") int populationId, @WebParam(name="size") int size);

//@WebMethod
//public List<MethylationFeature> getMethylationFeatures(@WebParam(name="populationId") int populationId, @WebParam(name="size") int size);

/*
@WebMethod
public List<MatchedFeature> getMSEMatchedFeatures(@WebParam(name="studyId") int studyId);
*/