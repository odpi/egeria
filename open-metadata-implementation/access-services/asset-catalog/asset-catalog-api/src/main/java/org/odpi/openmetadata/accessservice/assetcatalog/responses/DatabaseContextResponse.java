/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservice.assetcatalog.model.DatabaseContext;

import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DatabaseContextResponse is the response structure used on the Asset Catalog OMAS REST API calls that
 * returns a the whole context for a database object as a response.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatabaseContextResponse extends AssetCatalogOMASAPIResponse implements Serializable {

    private List<DatabaseContext> databaseContexts;

    public List<DatabaseContext> getDatabaseContexts() {
        return databaseContexts;
    }

    public void setDatabaseContexts(List<DatabaseContext> databaseContexts) {
        this.databaseContexts = databaseContexts;
    }
}