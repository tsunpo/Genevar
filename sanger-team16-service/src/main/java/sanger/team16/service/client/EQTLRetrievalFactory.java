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
package sanger.team16.service.client;

import sanger.team16.service.EQTLRetrievalService;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
public final class EQTLRetrievalFactory extends AbstractFactory
{
    public EQTLRetrievalFactory(String address, String username, String password) {
        super(address, username, password);
    }

    public EQTLRetrievalService create() {
        proxyFactory.setServiceClass(EQTLRetrievalService.class);
        proxyFactory.setAddress(address + "/EQTLRetrieval");
        
        return (EQTLRetrievalService) proxyFactory.create();
    }
}
