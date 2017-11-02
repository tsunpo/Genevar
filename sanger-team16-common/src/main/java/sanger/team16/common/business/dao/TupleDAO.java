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
package sanger.team16.common.business.dao;

import java.util.List;

import sanger.team16.common.hbm.dao.AbstractDAO;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 * @date   08/06/12
 */
public class TupleDAO extends AbstractDAO
{      
    public TupleDAO(String address) {
        super(address);
    }

    /**
       SELECT i.name, g.value, e.value
       FROM individual i, genotype g, expression e
       WHERE g.genotype_feature_id = 8
       AND g.variation_id = 37011
       AND e.expression_feature_id = 2
       AND e.transcript_id = 32965
       AND g.individual_id = e.individual_id
       AND i.individual_id = g.individual_id
       AND g.value != 'NN' (rs17433780-ILMN_1725314)
     */
    @SuppressWarnings("unchecked")
    public Tuple getTupleGxE(int genotypeFeatureId, int variationId, int expressionFeatureId, int transcriptId) {
        List<Object> objects = this.getSession().createQuery(
        "SELECT i.name, g.value, e.value" +
        " FROM Individual i, Genotype g, Expression e" +
        " WHERE g.genotypeFeatureId = :genotypeFeatureId" +
        " AND g.variationId = :variationId" +
        " AND e.expressionFeatureId = :expressionFeatureId" +
        " AND e.transcriptId = :transcriptId" +
        " AND g.individualId = e.individualId" +
        " AND i.id = g.individualId" +
        " AND g.value != 'NN'")   //" ORDER BY e.value")
        .setParameter("genotypeFeatureId", genotypeFeatureId)
        .setParameter("variationId", variationId)
        .setParameter("expressionFeatureId", expressionFeatureId)
        .setParameter("transcriptId", transcriptId)
        .list();
        
        int size = objects.size();
        String[] individuals = new String[size];
        String[] genotypes   = new String[size];
        double[] expressions = new double[size];
        
        for (int i=0 ; i<size ; i++) {
            Object[] columns = (Object[]) objects.get(i);
            //System.out.println(columns[0] + " " + columns[1] + " " + columns[2]);

            individuals[i] = (String) columns[0];
            genotypes[i]   = (String) columns[1];
            expressions[i] = ((Double) columns[2]).doubleValue();
        }
    
        this.commit();   //CHANGE BACK 25/07/12, SPEED UP 20/06/12, BAD 04/02/11, SPEED UP 23/11/10
        return new Tuple(individuals, genotypes, expressions);
    }
    

    /**
       SELECT i.name, g.value, m.value
       FROM individual i, genotype g, methylation m
       WHERE g.genotype_feature_id = 24
       AND g.variation_id = 4948319
       AND m.methylation_feature_id = 1
       AND m.modification_id = 338680
       AND g.individual_id = m.individual_id
       AND i.individual_id = g.individual_id
       AND g.value != 'NN' (rs7223632-cg16951108)
     */
    @SuppressWarnings("unchecked")
    public Tuple getTupleGxM(int genotypeFeatureId, int variationId, int methylationFeatureId, int modificationId) {
        List<Object> objects = this.getSession().createQuery(
        "SELECT i.name, g.value, m.value" +
        " FROM Individual i, Genotype g, Methylation m" +
        " WHERE g.genotypeFeatureId = :genotypeFeatureId" +
        " AND g.variationId = :variationId" +
        " AND m.methylationFeatureId = :methylationFeatureId" +
        " AND m.modificationId = :modificationId" +
        " AND g.individualId = m.individualId" +
        " AND i.id = g.individualId" +
        " AND g.value != 'NN'")   //" ORDER BY m.value")
        .setParameter("genotypeFeatureId", genotypeFeatureId)
        .setParameter("variationId", variationId)
        .setParameter("methylationFeatureId", methylationFeatureId)
        .setParameter("modificationId", modificationId)
        .list();
        
        int size = objects.size();
        String[] individuals = new String[size];
        String[] genotypes   = new String[size];
        double[] methylations = new double[size];
        
        for (int i=0 ; i<size ; i++) {
            Object[] columns = (Object[]) objects.get(i);

            individuals[i] = (String) columns[0];
            genotypes[i]   = (String) columns[1];
            methylations[i] = ((Double) columns[2]).doubleValue();
        }
    
        this.commit();
        return new Tuple(individuals, genotypes, methylations);
    }
}
