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

import sanger.team16.service.GeneRetrievalService;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
public final class GeneRetrievalFactory extends AbstractFactory
{
    //@WebServiceRef(wsdlLocation="http://localhost:8080/genevar/services/GeneRetrievalWS?wsdl")
    //static GeneRetrievalService service;
    
    public GeneRetrievalFactory(String address, String username, String password) {
        super(address, username, password);
    }
    
    public GeneRetrievalService create() {
        proxyFactory.setServiceClass(GeneRetrievalService.class);
        proxyFactory.setAddress(address + "/GeneRetrieval");

        return (GeneRetrievalService) proxyFactory.create();
    }
}

/*  
URL wsdlURL = new URL("http://localhost:8080/genevar/services/GeneRetrievalWS?wsdl");
QName SERVICE_NAME = new QName("http://apache.org/hello_world_soap_http", "SOAPService");
Service service = Service.create(wsdlURL, SERVICE_NAME);
GeneRetrievalService client = service.getPort(GeneRetrievalService.class);

return client;
*/

/*
ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
        new String[]{"sanger/team16/client/GeneRetrievalClient.xml"}); 

GeneRetrievalService client = (GeneRetrievalService) context.getBean("geneRetrievalClient");
return client;*/