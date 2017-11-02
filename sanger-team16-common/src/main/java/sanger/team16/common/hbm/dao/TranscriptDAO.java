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
package sanger.team16.common.hbm.dao;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/resources/software/genevar/
 */
public class TranscriptDAO extends AbstractDAO
{   
    public TranscriptDAO(String address) {
        super(address);
    }

    public int uniqueResult(int platformId, String probeId, boolean isCommitted) {
        Integer id = (Integer) this.getSession().createQuery("SELECT t.id FROM Transcript t WHERE t.platformId = :platformId AND t.probeId = :probeId")
        .setParameter("platformId", platformId)
        .setParameter("probeId", probeId)
        .uniqueResult();

        if (isCommitted)
            this.commit();
        return this.returnId(id);
    }
}

//private boolean isProbeId = false;   // false for HapMap3
/*    
public int uniqueResultNotClose(int platformId, String probeId) {
    Integer id = (Integer) session.createQuery("SELECT t.id FROM Transcript t WHERE t.platformId = :platformId AND t.probeId = :probeId")
    .setParameter("platformId", platformId)
    .setParameter("probeId", probeId)
    .uniqueResult();

    return this.returnId(id);
}*/
/*
public int uniqueResult(int platformId, String probeIdOrSearchKeyArrayAddressId) {
    if (this.isProbeId == true)
        return uniqueResultWhereProbeId(platformId, probeIdOrSearchKeyArrayAddressId);
    else
        return uniqueResultWhereSearchKeyArrayAddressId(platformId, probeIdOrSearchKeyArrayAddressId);
}

public int uniqueResultWhereSearchKeyArrayAddressId(int platformId, String searchKeyArrayAddressId) {
    String[] st = searchKeyArrayAddressId.split("_");
    String searchKey = "ILMN_" + st[1];
    String arrayAddressId = st[2];
    
    Integer id = (Integer) session.createQuery("SELECT t.id FROM Transcript t WHERE t.platformId = :platformId AND t.searchKey = :searchKey AND t.arrayAddressId = :arrayAddressId")
    .setParameter("platformId", platformId)
    .setParameter("searchKey", searchKey)
    .setParameter("arrayAddressId", arrayAddressId)
    .uniqueResult(); 

    transaction.commit();
    return this.returnId(id);
}

public int uniqueResultWhereArrayAddressId(int platformId, String arrayAddressId) {
    Integer id = (Integer) session.createQuery("SELECT t.id FROM Transcript t WHERE t.platformId = :platformId AND t.arrayAddressId = :arrayAddressId")
    .setParameter("platformId", platformId)
    .setParameter("arrayAddressId", arrayAddressId)
    .uniqueResult(); 

    transaction.commit();
    return this.returnId(id);
}

public String uniqueSearchKeyWhereArrayAddressId(int platformId, String arrayAddressId) {
    String serchKey = (String) session.createQuery("SELECT t.searchKey FROM Transcript t WHERE t.platformId = :platformId AND t.arrayAddressId = :arrayAddressId")
    .setParameter("platformId", new Integer(platformId).toString())
    .setParameter("arrayAddressId", arrayAddressId)
    .uniqueResult();

    transaction.commit();
    return serchKey;
}
*/