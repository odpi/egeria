/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.rex;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RexTraversalRequestBody {


    /*
     * The RexTraversalRequestBody class provides a body for REST requests to perform a rex-traversal
     */

    private String                    serverName;                    // must be non-null
    private String                    serverURLRoot;                 // must be non-null
    private String                    entityGUID;                    // must be non-null, GUID of root of traversal
    private Boolean                   enterpriseOption;
    private List<String>              entityTypeGUIDs;               // a list of type guids or null
    private List<String>              relationshipTypeGUIDs;         // a list of type guids or null
    private List<String>              classificationNames;           // a list of names or null
    private Integer                   depth;                         // the depth of traversal
    private Integer                   gen;                           // indicator of the current gen of the traversal


    public RexTraversalRequestBody() {
       // No initialization yet
    }

    /*
     * Getters for Jackson
     */

    public String getServerName() { return serverName; }

    public String getServerURLRoot() { return serverURLRoot; }

    public String getEntityGUID() { return entityGUID; }

    public Boolean getEnterpriseOption() { return enterpriseOption; }

    public List<String> getEntityTypeGUIDs() { return entityTypeGUIDs; }

    public List<String> getRelationshipTypeGUIDs() {
        return relationshipTypeGUIDs;
    }

    public List<String> getClassificationNames() {
        return classificationNames;
    }

    public Integer getDepth() { return depth; }

    public Integer getGen() { return gen; }


    public void setServerName(String serverName) { this.serverName = serverName; }

    public void setServerURLRoot(String serverURLRoot) { this.serverURLRoot = serverURLRoot; }

    public void setEntityGUID(String entityGUID) { this.entityGUID = entityGUID; }

    public void setEnterpriseOption(Boolean enterpriseOption) { this.enterpriseOption = enterpriseOption; }

    public void setEntityTypeGUIDs(List<String> entityTypeGUIDs) { this.entityTypeGUIDs = entityTypeGUIDs; }

    public void setRelationshipTypeGUIDs(List<String> relationshipTypeGUIDs) {
        this.relationshipTypeGUIDs = relationshipTypeGUIDs;
    }

    public void setClassificationNames(List<String> classificationNames) {
        this.classificationNames = classificationNames;
    }

    public void setDepth(Integer depth) { this.depth = depth; }

    public void setGen(Integer gen) { this.gen = gen; }





    @Override
    public String toString()
    {
        return "RexTraversalRequestBody{" +
                ", serverName=" + serverName +
                ", serverURLRoot=" + serverURLRoot +
                ", entityGUID=" + entityGUID +
                ", depth=" + depth +
                ", enterpriseOption=" + enterpriseOption +
                ", entityTypeGUIDs=" + entityTypeGUIDs +
                ", relationshipTypeGUIDs=" + relationshipTypeGUIDs +
                ", classificationNames=" + classificationNames +
                ", gen=" + gen +
                '}';
    }



}
