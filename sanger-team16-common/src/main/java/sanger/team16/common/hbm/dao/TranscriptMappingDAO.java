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

import sanger.team16.common.hbm.TranscriptMapping;
import sanger.team16.common.hbm.dao.AbstractDAO;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 * @date   09/06/12
 */
public class TranscriptMappingDAO extends AbstractDAO
{
    private String sql = "SELECT m, t.id FROM TranscriptMapping m, Transcript t" +
            " WHERE m.platformId = :platformId AND m.referenceId = :referenceId" +
            " AND t.platformId = :platformId AND m.probeId = t.probeId";
    
    public TranscriptMappingDAO(String address) {
        super(address);
    }

    /*
     * Command line mode
     */
    @SuppressWarnings("unchecked")
    public List<TranscriptMapping> listAll(int platformId, int referenceId) throws RuntimeException {
        List<Object> objects = this.getSession().createQuery(sql +
        " ORDER BY m.chromosome")
        .setParameter("platformId", platformId)
        .setParameter("referenceId", referenceId)
        .list();
        
        this.commit();
        return convertToTranscriptMappings(objects);
    }

    @SuppressWarnings("unchecked")
    public List<TranscriptMapping> listWhereChromosome(int platformId, int referenceId, String chromosome) throws RuntimeException {
        List<Object> objects = this.getSession().createQuery(sql +
        " AND m.chromosome = :chromosome")
        .setParameter("platformId", platformId)
        .setParameter("referenceId", referenceId)
        .setParameter("chromosome", chromosome)
        .list();

        this.commit();
        return convertToTranscriptMappings(objects);
    }
    
    /*
     * cis-eQTL - Gene
     */
    @SuppressWarnings("unchecked")
    public List<TranscriptMapping> list(int platformId, int referenceId, String query) throws RuntimeException {
        if (query.startsWith("ILMN_"))
            sql += " AND m.probeId = :query";
        else if (query.startsWith("ENSG"))
            sql += " AND m.ensemblGene = :query";
        else
            sql += " AND m.geneSymbol = :query";
        sql += " ORDER BY m.probeId";
        
        List<Object> objects = this.getSession().createQuery(sql)
        .setParameter("platformId", platformId)
        .setParameter("referenceId", referenceId)
        .setParameter("query", query)
        .list();
        
        this.commit();
        return convertToTranscriptMappings(objects);
    }

    @SuppressWarnings("unchecked")
    public List<TranscriptMapping> listWhereGene(int platformId, int referenceId, List<String> queries, boolean matched) throws RuntimeException {
        List<TranscriptMapping> objects = new ArrayList<TranscriptMapping>();
        String match = "=";
        
        if (queries != null) {
            for (int i=0 ; i<queries.size() ; i++) {
            	String sql = "FROM TranscriptMapping WHERE platformId = :platformId AND referenceId = :referenceId";
            	String query = queries.get(i);
                if (!matched) {
                	match = "LIKE";
                    query = "%" + query + "%";
                }
                
                if (query.startsWith("ILMN_"))
                    sql += " AND probeId " + match + " :query";                        
                else if (query.startsWith("ENSG"))
                    sql += " AND ensemblGene " + match + " :query";                          
                else
                    sql += " AND geneSymbol " + match + " :query";
                sql += " GROUP BY geneSymbol";
                
                objects.addAll(this.getSession().createQuery(sql)
                .setParameter("platformId", platformId)
                .setParameter("referenceId", referenceId)
                .setParameter("query", query)        
                .list());
            }   
        }

        this.commit();
        return objects;
    }
    
    /*
     * cis-eQTL - SNP
     * 
     * SELECT t.transcript_id, m.platform_id, m.reference_id, m.probe_id, m.ensembl_gene, m.gene_symbol, m.chromosome, m.gene_start, m.gene_end, m.strand, m.probe_start
     * FROM transcript_mapping m, transcript t
     * WHERE m.platform_id = 4 AND m.reference_id = 5
     * AND t.platform_id = 4 AND m.probe_id = t.probe_id
     * AND m.chromosome = 1 AND m.strand = -1
     * AND ((m.gene_start >= 72108699-10000 AND m.gene_start <= 72108699+10000)
     *      OR (m.gene_end >= 72108699-10000 AND m.gene_end <= 72108699+10000)
     *      OR (m.gene_start < 72108699 AND m.gene_end > 72108699))
     */
    @SuppressWarnings("unchecked")
    public List<TranscriptMapping> listWhereSNP(int platformId, int referenceId, String chromosome, int position, int distance, int strand) throws RuntimeException {        
        int threePrime = 0;
        int fivePrime = 0;
        
        sql += " AND m.chromosome = :chromosome AND m.strand = :strand";
        if (strand == 1) {
            threePrime = position + distance;
            fivePrime = position - distance;
            sql += " AND ((m.geneStart >= :fivePrime AND m.geneStart <= :threePrime)" +
                        " OR (m.geneEnd >= :fivePrime AND m.geneEnd <= :threePrime)" +
                        " OR (m.geneStart >= :position AND m.geneEnd <= :position))";   //ADD 22/06/11
        } else if (strand == -1) {
            threePrime = position - distance;
            fivePrime = position + distance;
            sql += " AND ((m.geneStart >= :threePrime AND m.geneStart <= :fivePrime)" +
                        " OR (m.geneEnd >= :threePrime AND m.geneEnd <= :fivePrime)" +
                        " OR (m.geneStart <= :position AND m.geneEnd >= :position))";   //ADD 22/06/11
        }
        //sql += " GROUP BY ensembl_gene ORDER BY gene_start";
        sql += " ORDER BY m.geneStart";
        if (strand == -1)
            sql += " DESC";
        
        List<Object> objects = this.getSession().createQuery(sql)
        .setParameter("platformId", platformId)
        .setParameter("referenceId", referenceId)
        .setParameter("chromosome", chromosome)
        .setParameter("strand", strand)
        .setParameter("threePrime", threePrime)
        .setParameter("fivePrime", fivePrime)
        .setParameter("position", position)   //ADD 22/06/11
        .list();
        
        this.commit();
        return convertToTranscriptMappings(objects);
    }
    
    private List<TranscriptMapping> convertToTranscriptMappings(List<Object> objects) {
        List<TranscriptMapping> transcriptMappings = new ArrayList<TranscriptMapping>();
        
        if (objects.size() != 0)
            for (int i=0 ; i<objects.size() ; i++) {
                Object[] columns = (Object[]) objects.get(i);
                
                TranscriptMapping transcriptMapping = (TranscriptMapping) columns[0];
                transcriptMapping.setTranscriptId((Integer) columns[1]);   //KEY!!!
                
                transcriptMappings.add(transcriptMapping);
            }
        
        return transcriptMappings;
    }
}

/*
    private String sql =
        "SELECT t.id, m.platformId, m.referenceId, m.probeId, m.ensemblGene, m.geneSymbol, m.chromosome, m.geneStart, m.geneEnd, m.strand, m.probeStart FROM TranscriptMapping m, Transcript t" +
        " WHERE m.platformId = :platformId AND m.referenceId = :referenceId" +
        " AND t.platformId = :platformId AND m.probeId = t.probeId";

private List<TranscriptMapping> convertToTranscriptMappings(List<Object> objects) {
    List<TranscriptMapping> transcriptMappings = new ArrayList<TranscriptMapping>();
    
    if (objects.size() != 0)
        for (int i=0 ; i<objects.size() ; i++) {
            TranscriptMapping transcriptMapping = new TranscriptMapping();
            Object[] columns = (Object[]) objects.get(i);
            
            //transcriptMapping.setId((Integer) columns[0]);
            transcriptMapping.setTranscriptId((Integer) columns[0]);
            transcriptMapping.setPlatformId((Integer) columns[1]);
            transcriptMapping.setReferenceId((Integer) columns[2]);
            transcriptMapping.setProbeId((String) columns[3]);
            transcriptMapping.setEnsemblGene((String) columns[4]);
            transcriptMapping.setGeneSymbol((String) columns[5]);
            transcriptMapping.setChromosome((String) columns[6]);
            transcriptMapping.setGeneStart((Integer) columns[7]);
            transcriptMapping.setGeneEnd((Integer) columns[8]);
            transcriptMapping.setStrand((Integer) columns[9]);
            transcriptMapping.setProbeStart((Integer) columns[10]);
            
            transcriptMappings.add(transcriptMapping);
        }
    
    return transcriptMappings;
}*/