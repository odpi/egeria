/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;


import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InstanceGraphResponse is the response structure for an OMRS REST API call that returns an instance graph object.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InstanceGraphResponse extends OMRSRESTAPIResponse
{
    private List<EntityDetail> entityElementList       = null;
    private List<Relationship> relationshipElementList = null;


    /**
     * Default constructor
     */
    public InstanceGraphResponse()
    {
    }

    public List<EntityDetail> getEntityElementList()
    {
        return entityElementList;
    }

    public void setEntityElementList(List<EntityDetail> entityElementList)
    {
        this.entityElementList = new ArrayList<>(entityElementList);
    }


    /**
     * Return the list of relationships that are part of this instance graph.
     *
     * @return - list of relationships
     */
    public List<Relationship> getRelationshipElementList()
    {
        return relationshipElementList;
    }


    /**
     * Set up the list of relationships that are part of this instance graph.
     *
     * @param relationshipElementList - list of relationships
     */
    public void setRelationshipElementList(List<Relationship> relationshipElementList)
    {
        this.relationshipElementList = new ArrayList<>(relationshipElementList);
    }
}
