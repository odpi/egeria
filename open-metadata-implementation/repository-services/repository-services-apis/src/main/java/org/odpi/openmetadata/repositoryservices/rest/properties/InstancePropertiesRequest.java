/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InstancePropertiesRequest carries the properties needed to update an entity, relationship or classification.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InstancePropertiesRequest extends OMRSAPIRequest
{
    private static final long    serialVersionUID = 1L;

    private InstanceProperties   instanceProperties     = null;


    /**
     * Default constructor
     */
    public InstancePropertiesRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public InstancePropertiesRequest(InstancePropertiesRequest template)
    {
        super(template);

        if (template != null)
        {
            this.instanceProperties = template.getInstanceProperties();
        }
    }


    /**
     * Return the list of properties for the new entity.
     *
     * @return instance properties object
     */
    public InstanceProperties getInstanceProperties()
    {
        if (instanceProperties == null)
        {
            return null;
        }
        else
        {
            return new InstanceProperties(instanceProperties);
        }
    }


    /**
     * Set up the initial properties for the entity.
     *
     * @param instanceProperties InstanceProperties object
     */
    public void setInstanceProperties(InstanceProperties instanceProperties)
    {
        this.instanceProperties = instanceProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "InstancePropertiesRequest{" +
                "instanceProperties=" + instanceProperties +
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
        if (!(objectToCompare instanceof InstancePropertiesRequest))
        {
            return false;
        }
        InstancePropertiesRequest
                that = (InstancePropertiesRequest) objectToCompare;
        return Objects.equals(getInstanceProperties(), that.getInstanceProperties());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getInstanceProperties());
    }
}
