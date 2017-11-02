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
package sanger.team16.common.jdbc;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class GenotypeJDBC extends AbstractJDBC
{
    public GenotypeJDBC(String address) {
        super(address);
    }

    public void loadDataLocalInfile(int featureId, String fileName) {
        if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1)
            fileName = replaceBackslashWithDoubleBackslash(fileName);
        
        String sql = "LOAD DATA LOCAL INFILE '" + fileName + "'" +
            " INTO TABLE genotype (variation_id, individual_id, value)";
        if (this.address.equals("tpy_team16_genevar_2_0_0.cfg.xml"))
            sql += " SET population_genotype_id = ";
        else
            sql += " SET genotype_feature_id = ";
        sql += featureId;
        
        this.execute(sql);
    }
}
