/**
 *  This file is part of Genevar (GENe Expression VARiation)
 *  Copyright (C) 2011  Genome Research Ltd.
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
package sanger.team16.common.hbm;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class VariationInfo
{
    private int id;
    private int variationId;
    private double info;
    private double freq1;

    public VariationInfo() {}
    
    public VariationInfo(int variationId, double info, double freq1) {
        this.variationId = variationId;
        this.info = info;
        this.freq1 = freq1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getVariationId() {
		return variationId;
	}

	public void setVariationId(int variationId) {
		this.variationId = variationId;
	}

	public double getInfo() {
        return info;
    }

    public void setInfo(double info) {
        this.info = info;
    }

    public double getFreq1() {
        return freq1;
    }

    public void setFreq1(double freq1) {
        this.freq1 = freq1;
    }
}
