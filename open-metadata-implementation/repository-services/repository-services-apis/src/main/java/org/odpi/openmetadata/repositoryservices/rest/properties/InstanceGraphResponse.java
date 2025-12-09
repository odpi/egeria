/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InstanceGraphResponse is the response structure for an OMRS REST API call that
 * returns an instance graph object.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InstanceGraphResponse extends OMRSAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private List<EntityDetail> entityElementList       = null;
    private List<Relationship> relationshipElementList = null;


    /**
     * Default constructor
     */
    public InstanceGraphResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public InstanceGraphResponse(InstanceGraphResponse template)
    {
        super(template);

        if (template != null)
        {
            entityElementList = template.getEntityElementList();
            relationshipElementList = template.getRelationshipElementList();
        }
    }


    /**
     * Return the list of entities stored in the graph.
     *
     * @return list of entities
     */
    public List<EntityDetail> getEntityElementList()
    {
        if (entityElementList == null)
        {
            return null;
        }
        else if (entityElementList.isEmpty())
        {
            return null;
        }
        else
        {
            List<EntityDetail>  clonedEntities = new ArrayList<>();

            for (EntityDetail  attributeTypeDef : entityElementList)
            {
                clonedEntities.add(new EntityDetail(attributeTypeDef));
            }

            return clonedEntities;
        }
    }


    /**
     * Set up the list of entities stored in the graph.
     *
     * @param entityElementList list of entities
     */
    public void setEntityElementList(List<EntityDetail> entityElementList)
    {
        this.entityElementList = entityElementList;
    }


    /**
     * Return the list of relationships that are part of this instance graph.
     *
     * @return list of relationships
     */
    public List<Relationship> getRelationshipElementList()
    {
        if (relationshipElementList == null)
        {
            return null;
        }
        else if (relationshipElementList.isEmpty())
        {
            return null;
        }
        else
        {
            List<Relationship>  clonedRelationships = new ArrayList<>();

            for (Relationship  attributeTypeDef : relationshipElementList)
            {
                clonedRelationships.add(new Relationship(attributeTypeDef));
            }

            return clonedRelationships;
        }
    }


    /**
     * Set up the list of relationships that are part of this instance graph.
     *
     * @param relationshipElementList list of relationships
     */
    public void setRelationshipElementList(List<Relationship> relationshipElementList)
    {
        this.relationshipElementList = relationshipElementList;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "InstanceGraphResponse{" +
                "entityElementList=" + entityElementList +
                ", relationshipElementList=" + relationshipElementList +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof InstanceGraphResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        InstanceGraphResponse
                that = (InstanceGraphResponse) objectToCompare;
        return Objects.equals(getEntityElementList(), that.getEntityElementList()) &&
                Objects.equals(getRelationshipElementList(), that.getRelationshipElementList());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getEntityElementList(), getRelationshipElementList());
    }
}
