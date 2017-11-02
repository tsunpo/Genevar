package sanger.team16.common.io;

import java.io.IOException;

import sanger.team16.common.hbm.dao.MethylationFeatureDAO;

public class LoadMethylation
{
    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        int probeIdColumn = 0;
        int individualColumn = 1;
        
        // 1. Create a new MethylationFeature
        int newMethylationFeatureId = 1;
        
        // 2. Load profile into Methylation
        MethylationLoader methylationLoader = new MethylationLoader("tpy_team16_genevar_3_0_0.cfg.xml", newMethylationFeatureId, 2, 6);
        methylationLoader.load("/nfs/team147/tpy/team147/meth/450k/smoking/Cardiogenics/450k_cardio_profiling_cam_omit_247.txt", probeIdColumn, individualColumn);
        //methylationLoader.load("/Users/tpy/usr/data/Cardiogenics/genevar/methylation/450k_cardio_profiling_cam_omit_247_example.txt", probeIdColumn, individualColumn);
        
        int modification = methylationLoader.modification;
        int individual = methylationLoader.individualIds.size();
        //System.out.println("HERE: " + modification + "\t" + individual);
        
        // 3. Update the log part of MethylationFeature
        new MethylationFeatureDAO("tpy_team16_genevar_3_0_0.cfg.xml").updateMethylationFeature(newMethylationFeatureId, "/nfs/team147/tpy/team147/meth/450k/smoking/Cardiogenics/450k_cardio_profiling_cam_omit_247.txt", individual, modification);
    }
}
