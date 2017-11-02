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
package sanger.team16.gui.genevar;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;

import sanger.team16.common.business.CoreRetrieval;
import sanger.team16.common.hbm.Assembly;
import sanger.team16.common.hbm.ExpressionFeature;
import sanger.team16.common.hbm.GenotypeFeature;
import sanger.team16.common.hbm.MethylationFeature;
import sanger.team16.common.hbm.Platform;
import sanger.team16.common.hbm.Population;
import sanger.team16.gui.genevar.db.ExpressionTableModel;
import sanger.team16.gui.genevar.db.GenotypeTableModel;
import sanger.team16.gui.genevar.db.MethylationTableModel;
import sanger.team16.gui.jface.BaseJLabel;
import sanger.team16.service.CoreRetrievalService;
import sanger.team16.service.client.CoreRetrievalFactory;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class AbstractManagerPane extends AbstractPane 
{   
    protected Vector<Population> studyPopulations;
    protected Vector<Assembly> studyAssemblies;
    //protected Vector<Reference> studyReferences;
    protected Vector<Platform> studyPlatforms;
    
    public AbstractManagerPane(UI ui) {
        super(ui);
    }

    protected void init() {
        studyPopulations = new Vector<Population>();
        studyAssemblies = new Vector<Assembly>();
        //studyReferences = new Vector<Reference>();
        studyPlatforms = new Vector<Platform>();
        
        studyPopulations.add(new Population(0, "Population"));
        studyAssemblies.add(new Assembly(0, "Genome Assembly"));
        //studyReferences.add(new Reference("Reference"));
        studyPlatforms.add(new Platform("Platform"));
    }
    
    protected JLabel getJLabeledPopulationName(String populationName) {
        if (populationName == "N/A")
            return new BaseJLabel(populationName, Color.gray, new Font("Arial", Font.BOLD, 13));
        else
            return new BaseJLabel(populationName, new Font("Arial", Font.BOLD, 13));
    }

    /**
     * GenotypePane & GenotypeUploadPane
     */
    protected Object[][] getGenotypeFeaturesWherePopulationId(List<GenotypeFeature> features, int populationId) {
        int columnSize = GenotypeTableModel.columnNames.length;
        Object[][] data = this.initialData(1, columnSize);
        int size = 0;
        
        if (features != null && features.size() != 0) {
            for (int i=0 ; i<features.size() ; i++)
                if (features.get(i).getPopulationId() == populationId)
                    size++;
          
            data = new Object[size][columnSize];
            int j = 0;
            for (int i=0 ; i<features.size() ; i++) {
                GenotypeFeature feature = features.get(i);
 
                if (feature.getPopulationId() == populationId) {
                    data[j][0] = this.ui.getAssembly(feature.getAssemblyId()).getName();
                    data[j][1] = this.ui.getMethod(feature.getMethodId()).getName();
                    data[j][2] = this.getTrimmedFileName(feature.getFileName());
                    data[j][3] = this.getgetFileColRow(feature.getFileCol());
                    data[j][4] = this.getgetFileColRow(feature.getFileRow());
                    data[j][5] = feature.getCreated(); 
                    
                    j++;
                }
            }   
        }
        
        return data;
    }
    
    protected List<GenotypeFeature> getGenotypeFeaturesWhereStudyId(int studyId) {
        if (this.ui.isServices()) {
            // Web Services //
            CoreRetrievalService client = new CoreRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            return client.getGenotypeFeaturesWhereStudyId(studyId);
        } else 
            // Hibernate //
            return new CoreRetrieval(this.ui.getAddress()).getGenotypeFeaturesWhereStudyId(studyId);
    }
    
    protected List<Integer> getGenotypeFeatureIds(int populationId, int sourceId, int versionId) {
        if (this.ui.isServices()) {
            // Web Services //
            CoreRetrievalService client = new CoreRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            return client.getGenotypeFeatureIds(populationId, sourceId, versionId);
        } else
            // Hibernate //
            return new CoreRetrieval(this.ui.getAddress()).getGenotypeFeatureIds(populationId, sourceId, versionId);
    }
    
    /**
     * MethylationPane & MethylationUploadPane
     */
    protected Object[][] getMethylationFeaturesWherePopulationId(List<MethylationFeature> features, int populationId) {
        int columnSize = MethylationTableModel.columnNames.length;
        Object[][] data = this.initialData(1, columnSize);
        int size = 0;

        if (features != null && features.size() != 0) {
            for (int i=0 ; i<features.size() ; i++)
                if (features.get(i).getPopulationId() == populationId)
                    size++;
          
            data = new Object[size][columnSize];
            int j = 0;
            for (int i=0 ; i<features.size() ; i++) {
                MethylationFeature feature = features.get(i);

                if (feature.getPopulationId() == populationId) {
                    data[j][0] = this.ui.getPlatform(feature.getPlatformId()).getName();
                    data[j][1] = this.ui.getTissueType(feature.getTissueTypeId()).getName();
                    data[j][2] = this.ui.getMethod(feature.getMethodId()).getName();
                    data[j][3] = this.getTrimmedFileName(feature.getFileName());
                    data[j][4] = this.getgetFileColRow(feature.getFileCol());
                    data[j][5] = this.getgetFileColRow(feature.getFileRow());
                    data[j][6] = feature.getCreated(); 
                 
                    j++;
                }
            }   
        }
        
        return data; 
    }
    
    protected List<MethylationFeature> getMethylationFeaturesWhereStudyId(int studyId) {
        List<MethylationFeature> features;

        if (this.ui.isServices()) {
            // Web Services //
            CoreRetrievalService client = new CoreRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            features = client.getMethylationFeaturesWhereStudyId(studyId);
        } else
            // Hibernate //
            features = new CoreRetrieval(this.ui.getAddress()).getMethylationFeaturesWhereStudyId(studyId);

        return features;
    }
    /*
    protected List<Integer> getMethylationFeatureIds(int populationId, int platformId, int tissueTypeId, int normalizationId) {
        if (this.ui.isServices()) {
            // Web Services //
            CoreRetrievalService client = new CoreRetrievalClient(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            return client.getMethylationFeatureIds(populationId, platformId, tissueTypeId, normalizationId);  
        } else
            // Hibernate //
            return new CoreRetrieval(this.ui.getAddress()).getMethylationFeatureIds(populationId, platformId, tissueTypeId, normalizationId);
    }
*/
    /**
     * ExpressionPane & ExpressionUploadPane
     */
    protected Object[][] getExpressionFeaturesWherePopulationId(List<ExpressionFeature> features, int populationId) {
        int columnSize = ExpressionTableModel.columnNames.length;
        Object[][] data = this.initialData(1, columnSize);
        int size = 0;
        
        if (features != null && features.size() != 0) {  //MODIFY 12/10/10
            for (int i=0 ; i<features.size() ; i++)
                if (features.get(i).getPopulationId() == populationId)
                    size++;
          
            data = new Object[size][columnSize];
            int j = 0;
            for (int i=0 ; i<features.size() ; i++) {
                ExpressionFeature feature = features.get(i);
 
                if (feature.getPopulationId() == populationId) {
                    data[j][0] = this.ui.getPlatform(feature.getPlatformId()).getName();
                    data[j][1] = this.ui.getTissueType(feature.getTissueTypeId()).getName();
                    data[j][2] = this.ui.getMethod(feature.getMethodId()).getName();
                    data[j][3] = this.getTrimmedFileName(feature.getFileName());
                    data[j][4] = this.getgetFileColRow(feature.getFileCol());
                    data[j][5] = this.getgetFileColRow(feature.getFileRow());
                    data[j][6] = feature.getCreated(); 
                 
                    j++;
                }
            }   
        }
        
        return data; 
    }
    
    private String getgetFileColRow(int number) {
    	if (number == 0)
    		return "";
    	return new Integer(number).toString();
    }
      
    /**
     * Same as in AbstractAttributePane
     */
    protected Vector<Population> getPopulationsWhereStudyId(int studyId) {
        Vector<Population> populations = new Vector<Population>();
        List<Population> lists;

        if (this.ui.isServices()) {
            // Web Services //
            CoreRetrievalService client = new CoreRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            lists = client.getPopulationsWhereStudyId(studyId);
        } else
            // Hibernate //
            lists = new CoreRetrieval(this.ui.getAddress()).getPopulationsWhereStudyId(studyId);
        
        if (lists != null)
            for (int i=0 ; i<lists.size() ; i++)
                populations.add(lists.get(i));
        
        return populations;
    }
    
    protected List<ExpressionFeature> getExpressionFeaturesWhereStudyId(int studyId) {
        if (this.ui.isServices()) {
            // Web Services //
            CoreRetrievalService client = new CoreRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            return client.getExpressionFeaturesWhereStudyId(studyId);
        } else
            // Hibernate //
            return new CoreRetrieval(this.ui.getAddress()).getExpressionFeaturesWhereStudyId(studyId);
    }
    
    protected List<Integer> getMethylationFeatureIds(int populationId, int platformId, int tissueTypeId, int versionId) {
        if (this.ui.isServices()) {
            // Web Services //
            CoreRetrievalService client = new CoreRetrievalFactory(this.ui.getAddress(), this.ui.getUsername(), this.ui.getPassword()).create();
            return client.getExpressionFeatureIds(populationId, platformId, tissueTypeId, versionId);  
        } else
            // Hibernate //
            return new CoreRetrieval(this.ui.getAddress()).getExpressionFeatureIds(populationId, platformId, tissueTypeId, versionId);
    }

    /**
     * 
     */
    protected String getTrimmedFileName(String fileName) {
        //if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1)
        if (fileName.contains("\\\\") || fileName.contains("/")) {   //ADD 01/12/11 if filename == ""
            String[] st = fileName.split("\\\\");
            fileName = st[st.length - 1];
            
            st = fileName.split("/");
            return st[st.length - 1];        	
        }
        return fileName;
    }
}

/*
    protected Object[][] getExpressionFeatures(int populationId, int size, int columnSize) {
        List<ExpressionFeature> features;
        Object[][] data = this.initialData(1, columnSize);
        
        if (this.ui.isServices()()) {
            // Web Services //
            CoreRetrievalService client = new CoreRetrievalClient(address).create();
            features = client.getExpressionFeatures(populationId, size);
        } else
            // Hibernate //
            features = new CoreRetrieval(address).getExpressionFeatures(populationId, size);

        if (features != null) {
            data = new Object[features.size()][columnSize];
            
            for (int i=0 ; i<features.size() ; i++) {
                ExpressionFeature populationExpression = features.get(i);
 
                data[i][0] = this.ui.primaryKey.getPlatform(populationExpression.getPlatformId()).getName();
                data[i][1] = this.ui.primaryKey.getTissueType(populationExpression.getTissueTypeId()).getName();
                data[i][2] = this.ui.primaryKey.getVersion(populationExpression.getVersionId()).getName();
                data[i][3] = this.getTrimmedFileName(populationExpression.getFileName());
                data[i][4] = populationExpression.getFileCol();
                data[i][5] = populationExpression.getFileRow();
                data[i][6] = populationExpression.getCreated();  
            }            
        }
        
        return data;
    }
    
    protected Object[][] getGenotypeFeatures(int populationId, int size, int columnSize) {
        List<GenotypeFeature> features;
        Object[][] data = this.initialData(1, columnSize);
        
        if (this.ui.isServices()()) {
            // Web Services //
            CoreRetrievalService client = new CoreRetrievalClient(address).create();
            features = client.getGenotypeFeatures(populationId, size);
        } else
            // Hibernate //
            features = new CoreRetrieval(address).getGenotypeFeatures(populationId, size);

        if (features != null) {
            data = new Object[features.size()][columnSize];

            for (int i=0 ; i<features.size() ; i++) {
                GenotypeFeature feature = features.get(i);
 
                data[i][0] = this.ui.primaryKey.getXrefSource(feature.getXrefSourceId()).getName();
                data[i][1] = this.ui.primaryKey.getVersion(feature.getVersionId()).getName();
                data[i][2] = this.getTrimmedFileName(feature.getFileName());
                data[i][3] = feature.getFileCol();
                data[i][4] = feature.getFileRow();
                data[i][5] = feature.getCreated();  
            }            
        }
        
        return data;
    }
    
    protected Object[][] getMethylationFeatures(int populationId, int size, int columnSize) {
        List<MethylationFeature> features;
        Object[][] data = this.initialData(1, columnSize);
        
        if (this.ui.isServices()()) {
            // Web Services //
            CoreRetrievalService client = new CoreRetrievalClient(address).create();
            features = client.getMethylationFeatures(populationId, size);
        } else
            // Hibernate //
            features = new CoreRetrieval(address).getMethylationFeatures(populationId, size);

        if (features != null) {
            data = new Object[features.size()][columnSize];
            
            for (int i=0 ; i<features.size() ; i++) {
                MethylationFeature feature = features.get(i);
 
                data[i][0] = this.ui.primaryKey.getPlatform(feature.getPlatformId()).getName();
                data[i][1] = this.ui.primaryKey.getTissueType(feature.getTissueTypeId()).getName();
                data[i][2] = this.ui.primaryKey.getVersion(feature.getVersionId()).getName();
                data[i][3] = this.getTrimmedFileName(feature.getFileName());
                data[i][4] = feature.getFileCol();
                data[i][5] = feature.getFileRow();
                data[i][6] = feature.getCreated();  
            }            
        }
        
        return data;
    }
*/

/*
private boolean isFileBelongToPopulation(String filePath, String selectedPopulation) {
    String fileName = getTrimmedFileName(filePath);
    String population = fileName.split("\\.")[0].split("_")[0];

    if (population.equalsIgnoreCase(selectedPopulation))
        return true;
    else
        return false;
}
*/