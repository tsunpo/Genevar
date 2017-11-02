package sanger.team16.common.io;

import java.io.IOException;

public class LoadGenotype
{
    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        // 1. Create a new Analysis
        //int newGenotypeFeatureId = new GenotypeFeatureDAO(ui.getAddress()).save(new GenotypeFeature(populationId, assemblyId, methodId, description, "tpy"));
        
        // 2. Load BIM and PED 
        //GenotypeLoader genotypeLoader = new GenotypeLoader(ui.getAddress(), study, genotypeFeatureId, populationId);   //NOTE: not newPopulationGenotypeId
        GenotypeLoader genotypeLoader = new GenotypeLoader("tpy_team16_genevar_3_0_0.cfg.xml", 2, 24, 2);
        genotypeLoader.loadBIM("/nfs/team147/Cardiogenics/imputed_genotype/cambridge/maf3/chr6_maf3.bim");                                   // CHANGE TO newGenotypeFeatureId 28/11/11
        genotypeLoader.loadPED("/nfs/team147/Cardiogenics/imputed_genotype/cambridge/maf3/chr6_maf3.ped");
        
        //variation = genotypeLoader.variationIds.size();
        //individual = genotypeLoader.individualIds.size();
        System.out.println("variation: " + genotypeLoader.variationIds.size());
        System.out.println("individual: " + genotypeLoader.individualIds.size());
        
        // 3. Update the log part of Analysis
        //new GenotypeFeatureDAO(ui.getAddress()).updateGenotypeFeature(newGenotypeFeatureId, tfPED.getText(), individual, variation, genotypeFeatureId);
    }
}
