/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.7.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2013
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package org.hoteia.qalingo.core.dao;

import java.util.List;

import org.hoteia.qalingo.core.domain.CatalogMaster;
import org.hoteia.qalingo.core.domain.CatalogVirtual;

public interface CatalogDao {

    // MASTER CATALOG

    CatalogMaster getMasterCatalogById(Long masterCatalogId);

    List<CatalogMaster> findAllCatalogMasters();

    CatalogMaster saveOrUpdateCatalogMaster(CatalogMaster productCatalog);

    void deleteCatalogMaster(CatalogMaster productCatalog);

    // VIRTUAL CATALOG
    
    CatalogVirtual getVirtualCatalogById(Long virtualCatalogId);

    CatalogVirtual getVirtualCatalogByMarketAreaId(Long marketAreaId);

}