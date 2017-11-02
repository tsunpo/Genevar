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
package sanger.team16.common.hbm.dao;

import java.util.ArrayList;
import java.util.List;

import sanger.team16.common.hbm.ModificationMapping;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 * @date   09/06/12
 */
public class ModificationMappingDAO extends AbstractDAO
{
    private String sql = "SELECT m, t.id FROM ModificationMapping m, Modification t" +
        " WHERE m.platformId = :platformId AND m.referenceId = :referenceId" +
        " AND t.platformId = :platformId AND m.probeId = t.probeId";
        
    public ModificationMappingDAO(String address) {
        super(address);
    }

    /*
     * cis-meQTL - Gene
     */
    @SuppressWarnings("unchecked")
    public List<ModificationMapping> list(int platformId, int referenceId, String query) throws RuntimeException {
        if (query.startsWith("cg") || query.startsWith("ch.") || query.startsWith("rs"))   // Bug fix for non-CpG probes
            sql += " AND m.probeId = :query";
        else if (query.startsWith("chr"))
            sql += " AND m.cpgIslands = :query";
        else
            sql += " AND m.geneSymbol = :query";
        sql += " ORDER BY m.probeId";
        
        List<Object> objects = this.getSession().createQuery(sql)
        .setParameter("platformId", platformId)
        .setParameter("referenceId", referenceId)
        .setParameter("query", query)
        .list();
        
        this.commit();
        return convertToModificationMappings(objects);
    }

    @SuppressWarnings("unchecked")
    public List<ModificationMapping> listWhereCpG(int platformId, int referenceId, List<String> queries, boolean matched) throws RuntimeException {
    	List<ModificationMapping> objects = new ArrayList<ModificationMapping>();
        String match = "=";
        
        if (queries != null) {
            for (int i=0 ; i<queries.size() ; i++) {
            	String sql = "FROM ModificationMapping WHERE platformId = :platformId AND referenceId = :referenceId";
            	String query = queries.get(i);
                if (!matched) {
                	match = "LIKE";
                    query = "%" + query + "%";
                }
                
                if (query.startsWith("cg") || query.startsWith("ch.") || query.startsWith("rs"))   // Bug fix for non-CpG probes
                    sql += " AND probeId " + match + " :query";
                else if (query.startsWith("chr"))
                    sql += " AND cpgIslands " + match + " :query";
                else
                    sql += " AND geneSymbol " + match + " :query";
                sql += " GROUP BY geneSymbol";
                
                objects.addAll(this.convertToModificationMappings(this.getSession().createQuery(sql)
                .setParameter("platformId", platformId)
                .setParameter("referenceId", referenceId)
                .setParameter("query", query)        
                .list(), query));
            }   
        }

        this.commit();
        return objects;
    }
    
    /*
     * cis-meQTL - SNP
     */
    @SuppressWarnings("unchecked")
    public List<ModificationMapping> listWhereSNP(int platformId, int referenceId, String chromosome, int position, int distance) throws RuntimeException {        
        int threePrime = position + distance;
        int fivePrime = position - distance;
        
        sql += " AND m.chromosome = :chromosome";
        sql += " AND (m.probeStart >= :fivePrime AND m.probeStart <= :threePrime)";
        /*if (strand == 1) {
            threePrime = position + distance;
            fivePrime = position - distance;
            sql += " AND (m.probeStart >= :fivePrime AND m.probeStart <= :threePrime)";
        } else if (strand == -1) {
            threePrime = position - distance;
            fivePrime = position + distance;
            sql += " AND (m.probeStart >= :threePrime AND m.probeStart <= :fivePrime)";
        }*/
        //sql += " GROUP BY ensembl_gene ORDER BY gene_start";
        sql += " ORDER BY m.probeStart";
        //if (strand == -1)
        //    sql += " DESC";
        
        List<Object> objects = this.getSession().createQuery(sql)
        .setParameter("platformId", platformId)
        .setParameter("referenceId", referenceId)
        .setParameter("chromosome", chromosome)
        //.setParameter("strand", strand)
        .setParameter("threePrime", threePrime)
        .setParameter("fivePrime", fivePrime)
        .list();
        
        this.commit();
        return convertToModificationMappings(objects);
    }
    
    private List<ModificationMapping> convertToModificationMappings(List<Object> objects) {
        List<ModificationMapping> modificationMappings = new ArrayList<ModificationMapping>();
        
        if (objects.size() != 0)
            for (int i=0 ; i<objects.size() ; i++) {
                Object[] columns = (Object[]) objects.get(i);
                
                ModificationMapping modificationMapping = (ModificationMapping) columns[0];
                modificationMapping.setModificationId((Integer) columns[1]);

                modificationMappings.add(modificationMapping);
            }
        
        return modificationMappings;
    }
    
    private List<ModificationMapping> convertToModificationMappings(List<ModificationMapping> objects, String query) {
        List<ModificationMapping> modificationMappings = new ArrayList<ModificationMapping>();
    	
    	if (objects.size() != 0)
            for (int i=0 ; i<objects.size() ; i++) {
                ModificationMapping modificationMapping = objects.get(i);
                if (!(query.startsWith("cg") || query.startsWith("ch.") || query.startsWith("rs"))) {
                    modificationMapping.setProbeId("");
                    modificationMapping.setProbeStart(0);
                }
                
                modificationMappings.add(modificationMapping);
            }

        return modificationMappings;
    }
}
