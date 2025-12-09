/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AttributeTypeDefListResponse provides a simple bean for returning an array of AttributeTypeDefs
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AttributeTypeDefListResponse extends OMRSAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private List<AttributeTypeDef> attributeTypeDefs = null;


    /**
     * Default constructor
     */
    public AttributeTypeDefListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AttributeTypeDefListResponse(AttributeTypeDefListResponse template)
    {
        super(template);

        if (template != null)
        {
            attributeTypeDefs = template.getAttributeTypeDefs();
        }
    }


    /**
     * Return the list of AttributeTypeDefs.
     *
     * @return a list of AttributeTypeDefs
     */
    public List<AttributeTypeDef> getAttributeTypeDefs()
    {
        if (attributeTypeDefs == null)
        {
            return null;
        }
        else if (attributeTypeDefs.isEmpty())
        {
            return null;
        }
        else
        {
            List<AttributeTypeDef>  clonedTypeDefs = new ArrayList<>();

            for (AttributeTypeDef  attributeTypeDef : attributeTypeDefs)
            {
                clonedTypeDefs.add(attributeTypeDef.cloneFromSubclass());
            }

            return clonedTypeDefs;
        }
    }


    /**
     * Set up the list of AttributeTypeDefs.
     *
     * @param attributeTypeDefs a list of AttributeTypeDefs
     */
    public void setAttributeTypeDefs(List<AttributeTypeDef> attributeTypeDefs)
    {
        this.attributeTypeDefs = attributeTypeDefs;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AttributeTypeDefListResponse{" +
                "attributeTypeDefs=" + attributeTypeDefs +
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
        if (!(objectToCompare instanceof AttributeTypeDefListResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AttributeTypeDefListResponse
                that = (AttributeTypeDefListResponse) objectToCompare;
        return Objects.equals(getAttributeTypeDefs(), that.getAttributeTypeDefs());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getAttributeTypeDefs());
    }
}
