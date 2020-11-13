/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * AttributeTypeDefResponse provides a response structure for an OMRS REST API call that returns
 * an AttributeTypeDef object.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AttributeTypeDefResponse extends OMRSAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private AttributeTypeDef attributeTypeDef = null;


    /**
     * Default constructor
     */
    public AttributeTypeDefResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AttributeTypeDefResponse(AttributeTypeDefResponse template)
    {
        super(template);

        if (template != null)
        {
            attributeTypeDef = template.getAttributeTypeDef();
        }
    }


    /**
     * Return the resulting AttributeTypeDef object.
     *
     * @return AttributeTypeDef object
     */
    public AttributeTypeDef getAttributeTypeDef()
    {
        if (attributeTypeDef == null)
        {
            return null;
        }
        else
        {
            return attributeTypeDef.cloneFromSubclass();
        }
    }


    /**
     * Set up the resulting AttributeTypeDef object.
     *
     * @param attributeTypeDef AttributeTypeDef object
     */
    public void setAttributeTypeDef(AttributeTypeDef attributeTypeDef)
    {
        this.attributeTypeDef = attributeTypeDef;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AttributeTypeDefResponse{" +
                "attributeTypeDef=" + attributeTypeDef +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", actionDescription='" + actionDescription + '\'' +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionCausedBy='" + exceptionCausedBy + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionErrorMessageId='" + exceptionErrorMessageId + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(exceptionErrorMessageParameters) +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                ", exceptionProperties=" + exceptionProperties +
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
        if (!(objectToCompare instanceof AttributeTypeDefResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AttributeTypeDefResponse
                that = (AttributeTypeDefResponse) objectToCompare;
        return Objects.equals(getAttributeTypeDef(), that.getAttributeTypeDef());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), getAttributeTypeDef());
    }
}
