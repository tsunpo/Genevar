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

import java.util.Vector;

import sanger.team16.common.hbm.Assembly;
import sanger.team16.common.hbm.Study;
import sanger.team16.common.hbm.Platform;
import sanger.team16.common.hbm.Population;
import sanger.team16.common.hbm.TissueType;
import sanger.team16.common.hbm.Method;
import sanger.team16.common.hbm.Reference;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
public class Core
{
    public Vector<Study> studies;
    public Vector<Population> populations;
    public Vector<Platform> platforms;
    public Vector<TissueType> tissueTypes;
    public Vector<Method> methods;
    public Vector<Reference> references;
    public Vector<Assembly> assemblies;
    //public Vector<Algorithm> algorithms;
    
    public Core() {}
    
    public Core(Vector<Study> studies, Vector<Population> populations, Vector<Platform> platforms, Vector<TissueType> tissueTypes, Vector<Method> methods, Vector<Reference> references, Vector<Assembly> assemblies) {
		this.studies = studies;
		this.populations = populations;
		this.platforms = platforms;
		this.tissueTypes = tissueTypes;
		this.methods = methods;
		this.references = references;
		this.assemblies = assemblies;   //ADD 02/08/11
    }
}
