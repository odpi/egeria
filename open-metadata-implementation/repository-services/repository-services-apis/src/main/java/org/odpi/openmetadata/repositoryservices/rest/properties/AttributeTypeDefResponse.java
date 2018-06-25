/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.rest.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * AttributeTypeDefResponse provides a response structure for an OMRS REST API call that returns an AttributeTypeDef
 * object.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AttributeTypeDefResponse extends OMRSRESTAPIResponse
{
    private AttributeTypeDef attributeTypeDef = null;


    /**
     * Default constructor
     */
    public AttributeTypeDefResponse()
    {
    }


    /**
     * Return the resulting AttributeTypeDef object.
     *
     * @return AttributeTypeDef object
     */
    public AttributeTypeDef getAttributeTypeDef()
    {
        return attributeTypeDef;
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


    @Override
    public String toString()
    {
        return "AttributeTypeDefResponse{" +
                "attributeTypeDef=" + attributeTypeDef +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                '}';
    }
}
