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

import java.util.Vector;

import sanger.team16.common.business.dao.Statistic;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
public class Message
{
    public static final String GENEVAR = "Genevar 3.3.0";
    public static final String GENEVAR_DEFAULT = "Wellcome Trust Sanger Institute";
    public static final String GENEVAR_INTERNAL = "Sanger Institute Internal";
    public static final String GENEVAR_MUTHER = "MuTHER Consortium";
    public static final String GENEVAR_CARDIO = "Cardiogenics Project";
    
    public static final String NEW_WS = "Web Services";
    public static final String NEW_DB = "Database";    
    public static final String NEW_EXIT = "Exit";
    
    public static final String WS_SANGER_DEFAULT = "http://services.sanger.ac.uk/genevar/services";
    public static final String WS_SANGER_INTERNAL = "http://services.sanger.ac.uk/genevar/internal/services";
    //public static final String WS_SANGER_INTERNAL = "http://t16tomcat.internal.sanger.ac.uk:8180/genevar-internal/services";
    public static final String WS_SANGER_MUTHER = "http://services.sanger.ac.uk/genevar/muther/services";
    //public static final String WS_SANGER_MUTHER = "http://t16tomcat.internal.sanger.ac.uk:8180/genevar-muther/services";
    public static final String WS_SANGER_CARDIO = "http://services.sanger.ac.uk/genevar/cardio/services";
    //public static final String WS_SANGER_CARDIO = "http://t16tomcat.internal.sanger.ac.uk:8180/genevar-cardio/services";
    public static final String WS_LOCALHOST = "http://localhost:8080/genevar/services";
    
    public static final String DB_2_0_0 = "tpy_team16_genevar_2_0_0.cfg.xml";
    public static final String DB_3_0_0 = "tpy_team16_genevar_3_0_0.cfg.xml";
    
    public static final String NEW_GTM = "Genotype Manager";
    public static final String NEW_MEM = "Methylation Manager";
    public static final String NEW_EXM = "Expression Manager";
    public static final String NEW_EAM = "External Algorithm Manager";
    public static final String EDIT_ATT = "Edit Attributes";
    public static final String LOAD_DATA = "Load Data";
    
    public static final String NEW_EQTL = "eQTL Analysis";
    public static final String EQTL_GENE = "cis-eQTL - Gene";
    public static final String EQTL_SNP = "cis-eQTL - SNP";
    public static final String EQTL_QUERY = "eQTL - SNP-Gene";

    public static final String NEW_MQTL = "mQTL Analysis";
    public static final String MQTL_CPG = "cis-mQTL - CpG";
    public static final String MQTL_SNP = "cis-mQTL - SNP";
    public static final String MQTL_QUERY = "mQTL - SNP-CpG";
    
    public static final String HELP_QUICKSTART = "Genevar Quick Starts";
    public static final String HELP_DATABASE = "Database Installation";
    public static final String HELP_FORMAT = "File Formats";
    public static final String HELP_ABOUT = "About Genevar";
    
    public static final String TXT = "txt";
    public static final String BIM = "bim";
    public static final String PED = "ped";
    
    public static final String URL_ENSEMBL_36 = "http://may2009.archive.ensembl.org/Homo_sapiens/Location/View?r=";
    public static final String URL_ENSEMBL_37 = "http://www.ensembl.org/Homo_sapiens/Location/View?r=";
    public static final String URL_HAPMAP_36 = "http://www.hapmap.org/cgi-perl/gbrowse/hapmap3r2_B36/?name=Chr";
    public static final String URL_HAPMAP_37 = "";
    public static final String URL_UCSC_36 = "http://genome.ucsc.edu/cgi-bin/hgTracks?org=Human&db=hg18&position=chr";
    public static final String URL_UCSC_37 = "http://genome.ucsc.edu/cgi-bin/hgTracks?org=Human&db=hg19&position=chr";
    
    public static final String URL_QUICKSTART = "http://www.sanger.ac.uk/resources/software/genevar/#t_1";
    public static final String URL_DATABASE = "http://www.sanger.ac.uk/resources/software/genevar/#t_3";
    public static final String URL_UPLOAD = "http://www.sanger.ac.uk/resources/software/genevar/#t_4";
    public static final String URL_ABOUT = "http://www.sanger.ac.uk/resources/software/genevar/#t_6";

    public static Vector<Statistic> getStatistics(String address) {
        Vector<Statistic> statistics = new Vector<Statistic>();
        
        statistics.add(new Statistic(Statistic.SPEARMANS, "Spearman's rank correlation coefficient (rho)", "SRC"));
        statistics.add(new Statistic(Statistic.LINEAR, "Simple linear regression (r)", "LM"));
        //if (address != Message.DB_2_0_0)   //BUG 07/12/10
        statistics.add(new Statistic(Statistic.EXTERNAL_ALGORITHM, "External algorithm (Preloaded)", "LMM"));   //TODO
        
        return statistics;
    }
}
