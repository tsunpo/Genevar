package sanger.team16.common.io;

import java.io.IOException;
import java.util.Map;

import sanger.team16.common.commandline.AbstractAnalysis;
import sanger.team16.common.commandline.Argument;
import sanger.team16.common.hbm.AnalysisFeature;
import sanger.team16.common.hbm.dao.AnalysisFeatureDAO;

public class LoadAnalysis extends AbstractAnalysis  //TODO
{
	public LoadAnalysis(String address, Map<String, String> argMap) throws IOException {
		super(address, argMap);
	}

    public void loadMuTHERProbABEL() throws IOException {
        int probeIdColumn = (Integer) 1 - 1;
        //int chrColumn = (Integer) chrSpinner.getValue() - 1;
        int genotypeFeatureId = new Integer(argMap.get(Argument.FEATURE_ID)).intValue();
        String chromosome = argMap.get(Argument.CHR);
        //String type = argMap.get(Argument.TISSUE_TYPE);
        String file = argMap.get(Argument.FILE);
        int snpIdColumn = (Integer) 2 - 1;
        int infoColumn = (Integer) 3 - 1;
        int posColumn = (Integer) 4 - 1;
        int a1Column = (Integer) 5 - 1;
        int freq1Column = (Integer) 6 - 1;
        int v1Column = (Integer) 7 - 1;
        int v2Column = (Integer) 8 - 1;
        int v3Column = (Integer) 9 - 1;
        int v4Column = (Integer) 10 - 1;
        int v5Column = (Integer) 11 - 1;

        // 1. Create a new AnalysisFeature                           // studyId = 4, algorithmId = 1,
        int newAnalysisFeatureId = 0;

        // 2. Load BIM and PED 
        AnalysisLoader analysisLoader = new AnalysisLoader(address);
        
        // Study = 5, Algorithm = 1, MethylationFeature = 1, ExpressionFeature = 0, PhenotypeFeature = 0
        newAnalysisFeatureId = new AnalysisFeatureDAO(address).save(new AnalysisFeature(5, 1, genotypeFeatureId, 1, 0, 0, "TwinsUK || ProbABEL / MuTHER format || GRCh37 / HapMap2 Imputation / chr1-22 (MAF > 5%; INFO > 0.8) || Illumina HumanMethylation450 / Adipose / QN", "tpy"), true);
        analysisLoader.loadMuTHERProbABEL(genotypeFeatureId, newAnalysisFeatureId, file, probeIdColumn, chromosome, snpIdColumn, infoColumn, posColumn, a1Column, freq1Column, v1Column, v2Column, v3Column, v4Column, v5Column);
        new AnalysisFeatureDAO(address).updateAnalysisFeature(newAnalysisFeatureId, file);
        
        // 3. Update the log part of Analysis
        //new AnalysisFeatureDAO(address).updateAnalysisFeature(newAnalysisFeatureId, file);
        //if (genotypeFeatureIds != null)
        //    new GenotypeFeatureDAO(address).updateParentFeatureId(genotypeFeatureIds);
    }
    
	/*
    // For eQTLs
    public void loadMuTHERProbABEL() throws IOException {
        int probeIdColumn = (Integer) 1 - 1;
        //int chrColumn = (Integer) chrSpinner.getValue() - 1;
        int genotypeFeatureId = new Integer(argMap.get(Argument.FEATURE_ID)).intValue();
        String chromosome = argMap.get(Argument.CHR);
        String type = argMap.get(Argument.TISSUE_TYPE);
        String file = argMap.get(Argument.FILE);
        int snpIdColumn = (Integer) 5 - 1;
        int infoColumn = (Integer) 6 - 1;
        int posColumn = (Integer) 7 - 1;
        int a1Column = (Integer) 8 - 1;
        int freq1Column = (Integer) 9 - 1;
        int v1Column = (Integer) 10 - 1;
        int v2Column = (Integer) 11 - 1;
        int v3Column = (Integer) 12 - 1;
        int v4Column = (Integer) 13 - 1;
        int v5Column = (Integer) 14 - 1;

        // 1. Create a new AnalysisFeature                           // studyId = 4, algorithmId = 1,
        int newAnalysisFeatureId = 0;

        // 2. Load BIM and PED 
        AnalysisLoader analysisLoader = new AnalysisLoader(address);
        // FAT
        String fileF = file + "_chr" + chromosome + "_FAT.txt";
        newAnalysisFeatureId = new AnalysisFeatureDAO(address).save(new AnalysisFeature(4, 1, genotypeFeatureId, 0, 18, 0, "TwinsUK || ProbABEL / MuTHER format || NCBI36 / HapMap2 Imputation / chr1-23 (MAF > 5%; INFO > 0.8) || Illumina HumanHT-12 v3 / Adipose / QN", "tpy"), true);
        analysisLoader.loadMuTHERProbABEL(genotypeFeatureId, newAnalysisFeatureId, fileF, probeIdColumn, chromosome, snpIdColumn, infoColumn, posColumn, a1Column, freq1Column, v1Column, v2Column, v3Column, v4Column, v5Column);
        new AnalysisFeatureDAO(address).updateAnalysisFeature(newAnalysisFeatureId, fileF);
        // LCL
        String fileL = file + "_chr" + chromosome + "_LCL.txt";
        newAnalysisFeatureId = new AnalysisFeatureDAO(address).save(new AnalysisFeature(4, 1, genotypeFeatureId, 0, 19, 0, "TwinsUK || ProbABEL / MuTHER format || NCBI36 / HapMap2 Imputation / chr1-23 (MAF > 5%; INFO > 0.8) || Illumina HumanHT-12 v3 / Lymphoblastoid cell line / QN", "tpy"), true);
        analysisLoader.loadMuTHERProbABEL(genotypeFeatureId, newAnalysisFeatureId, fileL, probeIdColumn, chromosome, snpIdColumn, infoColumn, posColumn, a1Column, freq1Column, v1Column, v2Column, v3Column, v4Column, v5Column);
        new AnalysisFeatureDAO(address).updateAnalysisFeature(newAnalysisFeatureId, fileL);
        // SKIN
        String fileS = file + "_chr" + chromosome + "_SKIN.txt";
        newAnalysisFeatureId = new AnalysisFeatureDAO(address).save(new AnalysisFeature(4, 1, genotypeFeatureId, 0, 20, 0, "TwinsUK || ProbABEL / MuTHER format || NCBI36 / HapMap2 Imputation / chr1-23 (MAF > 5%; INFO > 0.8) || Illumina HumanHT-12 v3 / Skin / QN", "tpy"), true);
        analysisLoader.loadMuTHERProbABEL(genotypeFeatureId, newAnalysisFeatureId, fileS, probeIdColumn, chromosome, snpIdColumn, infoColumn, posColumn, a1Column, freq1Column, v1Column, v2Column, v3Column, v4Column, v5Column);
        new AnalysisFeatureDAO(address).updateAnalysisFeature(newAnalysisFeatureId, fileS);
        
        // 3. Update the log part of Analysis
        //new AnalysisFeatureDAO(address).updateAnalysisFeature(newAnalysisFeatureId, file);
        //if (genotypeFeatureIds != null)
        //    new GenotypeFeatureDAO(address).updateParentFeatureId(genotypeFeatureIds);
    }
    */
    
    /*
    private void appendMuTHERProbABEL(int genotypeFeatureId) throws IOException, NumberFormatException {
        // 3. Update the log part of Analysis
        new GenotypeFeatureDAO(ui.getAddress()).updateGenotypeFeature(newGenotypeFeatureId, tfPED.getText(), individual, variation, genotypeFeatureId);
    }
	*/
}

/*   // WORKS BUT SLOW
public void loadMuTHERProbABEL() throws IOException {
    int probeIdColumn = (Integer) 1 - 1;
    //int chrColumn = (Integer) chrSpinner.getValue() - 1;
    int genotypeFeatureId = new Integer(argMap.get(Argument.FEATURE_ID)).intValue();
    String chromosome = argMap.get(Argument.CHR);
    String type = argMap.get(Argument.TISSUE_TYPE);
    String file = argMap.get(Argument.FILE);
    int snpIdColumn = (Integer) 5 - 1;
    int infoColumn = (Integer) 6 - 1;
    int posColumn = (Integer) 7 - 1;
    int a1Column = (Integer) 8 - 1;
    int freq1Column = (Integer) 9 - 1;
    int v1Column = (Integer) 10 - 1;
    int v2Column = (Integer) 11 - 1;
    int v3Column = (Integer) 12 - 1;
    int v4Column = (Integer) 13 - 1;
    int v5Column = (Integer) 14 - 1;

    // 1. Create a new AnalysisFeature                           // studyId = 4, algorithmId = 1,
    int newAnalysisFeatureId = 0;
    if (type.equals("fat"))
        newAnalysisFeatureId = new AnalysisFeatureDAO(address).save(new AnalysisFeature(4, 1, genotypeFeatureId, 0, 18, 0, "TwinsUK || ProbABEL / MuTHER format || NCBI36 / HapMap2 Imputation / chr1-23 (MAF > 5%; INFO > 0.8) || Illumina HumanHT-12 v3 / Adipose / QN", "tpy"));
    else if (type.equals("lcl"))
        newAnalysisFeatureId = new AnalysisFeatureDAO(address).save(new AnalysisFeature(4, 1, genotypeFeatureId, 0, 19, 0, "TwinsUK || ProbABEL / MuTHER format || NCBI36 / HapMap2 Imputation / chr1-23 (MAF > 5%; INFO > 0.8) || Illumina HumanHT-12 v3 / Lymphoblastoid cell line / QN", "tpy"));
    else if (type.equals("skin"))
        newAnalysisFeatureId = new AnalysisFeatureDAO(address).save(new AnalysisFeature(4, 1, genotypeFeatureId, 0, 20, 0, "TwinsUK || ProbABEL / MuTHER format || NCBI36 / HapMap2 Imputation / chr1-23 (MAF > 5%; INFO > 0.8) || Illumina HumanHT-12 v3 / Skin / QN", "tpy"));
    
    // 2. Load BIM and PED 
    AnalysisLoader analysisLoader = new AnalysisLoader(address);
    analysisLoader.loadMuTHERProbABEL(genotypeFeatureId, newAnalysisFeatureId, file, probeIdColumn, chromosome, snpIdColumn, infoColumn, posColumn, a1Column, freq1Column, v1Column, v2Column, v3Column, v4Column, v5Column);

    // 3. Update the log part of Analysis
    new AnalysisFeatureDAO(address).updateAnalysisFeature(newAnalysisFeatureId, file);
    //if (genotypeFeatureIds != null)
    //    new GenotypeFeatureDAO(address).updateParentFeatureId(genotypeFeatureIds);
}
 */