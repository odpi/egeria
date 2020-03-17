/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.userinterface.uichassis.springboot.api.rex;


import java.util.Map;

public class RexRelationshipSearchResponse {

    private Integer       httpStatusCode;
    private String        exceptionText;
    private String        serverName;
    private String        searchOperation;
    private String        searchText;

    // Fields that contain the maps of instance summaries.
    // An instance summary is much smaller than the full instance.
    // The entities map is keyed by entityGUID and the value part consists of
    //    { entityGUID, label, gen }
    // The relationships map is keyed by relationshipGUID and the value part consists of
    //    { relationshipGUID, end1GUID, end2GUID, idx, label, gen }
    // The above value types are described by the RexEntityDigest and RexRelationshipDigest Java classes.
    private Map<String,RexEntityDigest>         entities;
    private Map<String,RexRelationshipDigest>   relationships;


    public RexRelationshipSearchResponse(Integer                             statusCode,
                                         String                              exceptionText,
                                         String                              serverName,
                                         String                              searchText,
                                         String                              searchOperation,
                                         Map<String,RexEntityDigest>         entities,
                                         Map<String,RexRelationshipDigest>   relationships) {

        this.httpStatusCode   = statusCode;
        this.exceptionText    = exceptionText;
        this.serverName       = serverName;
        this.searchText       = searchText;
        this.searchOperation  = searchOperation;
        this.entities         = entities;
        this.relationships    = relationships;
    }

    public Integer getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public String getExceptionText() {
        return this.exceptionText;
    }

    public String getServerName() { return this.serverName; }

    public String getSearchText() { return this.searchText; }

    public String getSearchOperation() { return this.searchOperation; }

    public Map<String,RexEntityDigest> getEntities() {  return this.entities;  }

    public Map<String,RexRelationshipDigest> getRelationships() {
        return this.relationships;
    }

    public void setHttpStatusCode(Integer httpStatusCode)
    {
        this.httpStatusCode = httpStatusCode;
    }

    public void setExceptionText(String exceptionText)
    {
        this.exceptionText = exceptionText;
    }

    public void setServerName(String serverName) { this.serverName = serverName; }

    public void setSearchText(String searchText) { this.searchText = searchText; }

    public void setSearchOperation(String searchOperation) { this.searchOperation = searchOperation; }

    public void setEntities(Map<String,RexEntityDigest>  entities) { this.entities = entities; }

    public void setRelationships(Map<String,RexRelationshipDigest>  relationships) { this.relationships = relationships; }
}
