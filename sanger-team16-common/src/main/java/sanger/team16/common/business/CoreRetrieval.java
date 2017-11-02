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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.jws.WebMethod;
import javax.jws.WebService;

import sanger.team16.common.business.dao.Core;
import sanger.team16.common.business.dao.MatchedFeature;
import sanger.team16.common.business.dao.MatchedFeatureDAO;
import sanger.team16.common.hbm.Algorithm;
import sanger.team16.common.hbm.AnalysisFeature;
import sanger.team16.common.hbm.Assembly;
import sanger.team16.common.hbm.MethylationFeature;
import sanger.team16.common.hbm.Study;
import sanger.team16.common.hbm.ExpressionFeature;
import sanger.team16.common.hbm.GenotypeFeature;
import sanger.team16.common.hbm.Platform;
import sanger.team16.common.hbm.Population;
import sanger.team16.common.hbm.TissueType;
import sanger.team16.common.hbm.Method;
import sanger.team16.common.hbm.Reference;
import sanger.team16.common.hbm.dao.AlgorithmDAO;
import sanger.team16.common.hbm.dao.AnalysisFeatureDAO;
import sanger.team16.common.hbm.dao.AssemblyDAO;
import sanger.team16.common.hbm.dao.MethylationFeatureDAO;
import sanger.team16.common.hbm.dao.StudyDAO;
import sanger.team16.common.hbm.dao.PlatformDAO;
import sanger.team16.common.hbm.dao.PopulationDAO;
import sanger.team16.common.hbm.dao.ExpressionFeatureDAO;
import sanger.team16.common.hbm.dao.GenotypeFeatureDAO;
import sanger.team16.common.hbm.dao.TissueTypeDAO;
import sanger.team16.common.hbm.dao.MethodDAO;
import sanger.team16.common.hbm.dao.ReferenceDAO;
import sanger.team16.service.CoreRetrievalService;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@WebService(endpointInterface = "sanger.team16.service.CoreRetrievalService", serviceName = "CoreRetrieval")
public class CoreRetrieval extends AbstractRetrieval implements CoreRetrievalService
{
    public CoreRetrieval() {
        super();
    }
    
    public CoreRetrieval(String address) {
        super(address);
    }

    @WebMethod
    public Core getCore() {
		return new Core(getStudies(), getPopulations(), getPlatforms(), getTissueTypes(), getMethods(), getReferences(), getAssemblies());
	}

    @WebMethod
    public List<Population> getPopulationsWhereStudyId(int studyId) {
        return new PopulationDAO(address).listWhereStudyId(studyId);
    }
    
    /**
     * Core stores primary keys
     */
    private Vector<Study> getStudies() {
        List<Study> lists = new StudyDAO(address).list();
        int size = lists.size();
        
        Vector<Study> studies = new Vector<Study>();
        studies.add(new Study("Project"));
        for (int i=0 ; i<size ; i++)
            studies.add(lists.get(i));
        
        return studies;
    }
    
    private Vector<Population> getPopulations() {
        List<Population> lists = new PopulationDAO(address).list();
        int size = lists.size();
        
        Vector<Population> populations = new Vector<Population>();
        populations.add(new Population(0, "Population"));
        for (int i=0 ; i<size ; i++)
            populations.add(lists.get(i));
        
        return populations;
    }
    
    private Vector<Platform> getPlatforms() {
        List<Platform> lists = new PlatformDAO(address).list();
        int size = lists.size();
        
        Vector<Platform> platforms = new Vector<Platform>();
        platforms.add(new Platform("Platform"));
        for (int i=0 ; i<size ; i++)
            platforms.add(lists.get(i));
        
        return platforms;
    }
 
    private Vector<TissueType> getTissueTypes() {
        List<TissueType> lists = new TissueTypeDAO(address).list();
        int size = lists.size();
        
        Vector<TissueType> tissueTypes = new Vector<TissueType>();
        tissueTypes.add(new TissueType("Tissue Type", ""));
        for (int i=0 ; i<size ; i++)
            tissueTypes.add(lists.get(i));
        
        return tissueTypes;
    }

    private Vector<Method> getMethods() {
        List<Method> lists = new MethodDAO(address).list();
        int size = lists.size();
        
        Vector<Method> methods = new Vector<Method>();
        //methods.add(new Method("Method", ""));
        for (int i=0 ; i<size ; i++)
            methods.add(lists.get(i));
        
        return methods;
    }
    
    private Vector<Reference> getReferences() {
        List<Reference> lists = new ReferenceDAO(address).list();
        int size = lists.size();
        
        Vector<Reference> references = new Vector<Reference>();
        //xrefSources.add(new XrefSource(0, "Source", ""));
        for (int i=0 ; i<size ; i++)
            references.add(lists.get(i));
        
        return references;
    }
     
    private Vector<Assembly> getAssemblies() {
        List<Assembly> lists = new AssemblyDAO(address).list();
        int size = lists.size();
        
        Vector<Assembly> assemblies = new Vector<Assembly>();
        assemblies.add(new Assembly(0, "Genome Assembly"));
        for (int i=0 ; i<size ; i++)
            assemblies.add(lists.get(i));
        
        return assemblies;
    }
    
    /**
     * MatchedFeature
     */
    @WebMethod
    public List<MatchedFeature> getMatchedFeaturesGxE(int studyId) {
        return this.setPopulationNames(new MatchedFeatureDAO(address).listGxE(studyId));
    }

    @WebMethod
    public List<MatchedFeature> getMatchedFeaturesGxM(int studyId) {
        return this.setPopulationNames(new MatchedFeatureDAO(address).listGxM(studyId));
    }
    
    private List<MatchedFeature> setPopulationNames(List<MatchedFeature> matchedFeatures) {
        Map<String, String> tissueMap = new HashMap<String, String>();
        Map<String, String> initialMap = new HashMap<String, String>();
        for (int i=0 ; i<matchedFeatures.size() ; i++) {
            String tissueType = matchedFeatures.get(i).getTissueTypeName();
            
            tissueMap.put(tissueType, tissueType);
            initialMap.put(Character.toString(tissueType.charAt(0)), Character.toString(tissueType.charAt(0)));
        }
        
        if (tissueMap.size() != 1) {
            if (initialMap.size() != 1)
                for (int i=0 ; i<matchedFeatures.size() ; i++) {
                    MatchedFeature matchedFeature = matchedFeatures.get(i);
                    String populationName = matchedFeature.getPopulationName() + "-" + Character.toString(matchedFeature.getTissueTypeName().charAt(0));
                    matchedFeature.setPopulationName(populationName);
                }
            else
                for (int i=0 ; i<matchedFeatures.size() ; i++) {
                    MatchedFeature matchedFeature = matchedFeatures.get(i);
                    String populationName = matchedFeature.getPopulationName() + "-" + matchedFeature.getTissueTypeName();
                    matchedFeature.setPopulationName(populationName);
                }
        }
        
        return matchedFeatures;
    }

    /**
     * GenotypePane & GenotypeUploadPane
     */
    @WebMethod
    public List<GenotypeFeature> getGenotypeFeaturesWhereStudyId(int studyId) {
        return new GenotypeFeatureDAO(address).listWhereStudyId(studyId);
    }
    
    @WebMethod
    public List<Integer> getGenotypeFeatureIds(int populationId, int referenceId, int methodId) {
        return new GenotypeFeatureDAO(address).list(populationId, referenceId, methodId);
    }

    /**
     * MethylationPane & MethylationUploadPane
     */
    @WebMethod
    public List<MethylationFeature> getMethylationFeaturesWhereStudyId(int studyId) {
        return new MethylationFeatureDAO(address).listWhereStudyId(studyId);
    }

    @WebMethod
    public List<Integer> getMethylationFeatureIds(int populationId, int platformId, int tissueTypeId, int methodId) {
        return new MethylationFeatureDAO(address).list(populationId, platformId, tissueTypeId, methodId);
    }
    
    /**
     * ExpressionPane & ExpressionUploadPane
     */
    @WebMethod
    public List<ExpressionFeature> getExpressionFeaturesWhereStudyId(int studyId) {
        return new ExpressionFeatureDAO(address).listWhereStudyId(studyId);
    }
    
    @WebMethod
    public List<Integer> getExpressionFeatureIds(int populationId, int platformId, int tissueTypeId, int methodId) {
        return new ExpressionFeatureDAO(address).list(populationId, platformId, tissueTypeId, methodId);
    }
    
    /**
     * Algorithm & AnalysisFeature
     */
    @WebMethod
    public Vector<Algorithm> getQTLAlgorithmsWhereStudyId(int studyId, String type) {
    	List<Algorithm> lists = null;
    	
        if (type.equals("E"))
        	lists = new AlgorithmDAO(address).listEQTL(studyId);
        else if (type.equals("M"))
        	lists = new AlgorithmDAO(address).listMQTL(studyId);
        int size = lists.size();
        
        Vector<Algorithm> algorithms = new Vector<Algorithm>();
        for (int i=0 ; i<size ; i++)
            algorithms.add(lists.get(i));
        
        return algorithms;
    }

    @WebMethod
    public List<AnalysisFeature> getQTLAnalysisFeatures(int algorithmId, int genotypeFeatureId, int traitFeatureId, String type) {
        if (type.equals("E"))
        	return new AnalysisFeatureDAO(address).listEQTL(algorithmId, genotypeFeatureId, traitFeatureId);
        else if (type.equals("M"))
            return new AnalysisFeatureDAO(address).listMQTL(algorithmId, genotypeFeatureId, traitFeatureId);
		return null;  //TODO
    }
    
    /*
    @WebMethod
    public List<Integer> getAnalysisFeatureIds(int populationId, int platformId, int tissueTypeId, int methodId) {
        return new ExpressionFeatureDAO(address).list(populationId, platformId, tissueTypeId, methodId);
    }*/
}

/*@WebMethod
public List<MatchedFeature> getMSEMatchedFeatures(int datasetId) {
    List<MatchedFeature> matchedFeatures = new MatchedFeatureDAO(address).listMSE(datasetId);
    
    return this.modifyPopulationName(matchedFeatures);
}*/
