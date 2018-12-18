/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelationshipCreateRequest provides the bean to hold all of the properties needed to create a new
 * relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationshipCreateRequest extends OMRSAPIRequest
{
    private String             relationshipTypeGUID = null;
    private InstanceProperties initialProperties = null;
    private String             entityOneGUID = null;
    private String             entityTwoGUID = null;
    private InstanceStatus     initialStatus = null;


    /**
     * Default constructor
     */
    public RelationshipCreateRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RelationshipCreateRequest(RelationshipCreateRequest template)
    {
        super(template);

        if (template != null)
        {
            this.relationshipTypeGUID = template.getRelationshipTypeGUID();
            this.initialProperties = template.getInitialProperties();
            this.entityOneGUID = template.getEntityOneGUID();
            this.entityTwoGUID = template.getEntityTwoGUID();
            this.initialStatus = template.getInitialStatus();
        }
    }


    /**
     * Return the type of the new relationship.
     *
     * @return String guid
     */
    public String getRelationshipTypeGUID()
    {
        return relationshipTypeGUID;
    }


    /**
     * Set up the type of the new relationship.
     *
     * @param relationshipTypeGUID String guid
     */
    public void setRelationshipTypeGUID(String relationshipTypeGUID)
    {
        this.relationshipTypeGUID = relationshipTypeGUID;
    }


    /**
     * Return the list of properties for the new relationship.
     *
     * @return instance properties object
     */
    public InstanceProperties getInitialProperties()
    {
        if (initialProperties == null)
        {
            return null;
        }
        else
        {
            return new InstanceProperties(initialProperties);
        }
    }


    /**
     * Set up the initial properties for the relationship.
     *
     * @param initialProperties InstanceProperties object
     */
    public void setInitialProperties(InstanceProperties initialProperties)
    {
        this.initialProperties = initialProperties;
    }


    /**
     * Return the unique identifier (GUID) for the first entity linked by the relationship.
     *
     * @return entity guid
     */
    public String getEntityOneGUID()
    {
        return entityOneGUID;
    }


    /**
     * Set up the unique identifier (GUID) for the first entity linked by the relationship.
     *
     * @param entityOneGUID entity guid
     */
    public void setEntityOneGUID(String entityOneGUID)
    {
        this.entityOneGUID = entityOneGUID;
    }


    /**
     * Return the unique identifier (GUID) for the second entity linked by the relationship.
     *
     * @return entity guid
     */
    public String getEntityTwoGUID()
    {
        return entityTwoGUID;
    }


    /**
     * Set up the unique identifier (GUID) for the second entity linked by the relationship.
     *
     * @param entityTwoGUID entity guid
     */
    public void setEntityTwoGUID(String entityTwoGUID)
    {
        this.entityTwoGUID = entityTwoGUID;
    }


    /**
     * Return the initial status for the new relationship.
     *
     * @return instance status enum
     */
    public InstanceStatus getInitialStatus()
    {
        return initialStatus;
    }


    /**
     * Set up the initial status for the new relationship.
     *
     * @param initialStatus instance status enum
     */
    public void setInitialStatus(InstanceStatus initialStatus)
    {
        this.initialStatus = initialStatus;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RelationshipCreateRequest{" +
                "relationshipTypeGUID='" + relationshipTypeGUID + '\'' +
                ", initialProperties=" + initialProperties +
                ", entityOneGUID='" + entityOneGUID + '\'' +
                ", entityTwoGUID='" + entityTwoGUID + '\'' +
                ", initialStatus=" + initialStatus +
                '}';
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
        if (!(objectToCompare instanceof RelationshipCreateRequest))
        {
            return false;
        }
        RelationshipCreateRequest that = (RelationshipCreateRequest) objectToCompare;
        return Objects.equals(getRelationshipTypeGUID(), that.getRelationshipTypeGUID()) &&
                Objects.equals(getInitialProperties(), that.getInitialProperties()) &&
                Objects.equals(getEntityOneGUID(), that.getEntityOneGUID()) &&
                Objects.equals(getEntityTwoGUID(), that.getEntityTwoGUID()) &&
                getInitialStatus() == that.getInitialStatus();
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(getRelationshipTypeGUID(),
                            getInitialProperties(),
                            getEntityOneGUID(),
                            getEntityTwoGUID(),
                            getInitialStatus());
    }
}
