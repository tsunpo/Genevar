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
package sanger.team16.common.business.dao;

import sanger.team16.common.hbm.ExpressionFeature;
import sanger.team16.common.hbm.GenotypeFeature;
import sanger.team16.common.hbm.MethylationFeature;
import sanger.team16.common.hbm.TissueType;
import sanger.team16.common.hbm.Variation;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
public class MatchedFeature
{
    private String populationName = "";   //BLOODY BUG 13/07/11
    public TissueType tissueType;
    public GenotypeFeature genotypeFeature;
    public MethylationFeature methylationFeature;
    public ExpressionFeature expressionFeature;
    private int platformId;
    
    public MatchedFeature() {}   //MUST HAVE!

    public MatchedFeature(String populationName, TissueType tissueType, 
            GenotypeFeature genotypeFeature, MethylationFeature methylationFeature, ExpressionFeature expressionFeature, int platformId) {
        this.populationName = populationName;
        this.tissueType = tissueType;
        this.genotypeFeature = genotypeFeature;
        this.methylationFeature = methylationFeature;
        this.expressionFeature = expressionFeature;
        this.platformId = platformId;
    }

    public String getPopulationName() {
		return populationName;
	}

	public void setPopulationName(String populationName) {
		this.populationName = populationName;
	}

	public int getPlatformId() {
		return platformId;
	}

	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}

	public String getTissueTypeName() {
        return tissueType.getName();
    }

    public int getTissueTypeId() {
        return tissueType.getId();
    }

    public String getGenotypeFeatureDescription() {
        return genotypeFeature.getDescription();
    }
    
    public int getGenotypeFeatureId() {
        return genotypeFeature.getId();
    }
    
    /*
     * When GenotypeFeature.parent_id != 0 or for Database 2.0.0
     */
    public int getGenotypeFeatureId(String address, Variation snp) {
        if (address.equals("tpy_team16_genevar_2_0_0.cfg.xml") || snp.getGenotypeFeatureId() == 0)
        	return genotypeFeature.getId();
        return snp.getGenotypeFeatureId();   //BUG 08/12/11
    }

    public String getMethylationFeatureDescription() {
        return methylationFeature.getDescription();
    }
    
    public int getMethylationFeatureId() {
        return methylationFeature.getId();
    }

    public String getExpressionFeatureDescription() {
        return expressionFeature.getDescription();
    }
    
    public int getExpressionFeatureId() {
        return expressionFeature.getId();
    }
}
