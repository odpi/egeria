/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AttributeTypeDefListResponse provides a simple baen for returning an array of AttributeTypeDefs
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AttributeTypeDefListResponse extends OMRSRESTAPIResponse
{
    private List<AttributeTypeDef> attributeTypeDefs = null;


    /**
     * Default constructor
     */
    public AttributeTypeDefListResponse()
    {
    }


    /**
     * Return the list of AttributeTypeDefs.
     *
     * @return a list of AttributeTypeDefs
     */
    public List<AttributeTypeDef> getAttributeTypeDefs()
    {
        return attributeTypeDefs;
    }


    /**
     * Set up the list of AttributeTypeDefs.
     *
     * @param attributeTypeDefs - a list of AttributeTypeDefs
     */
    public void setAttributeTypeDefs(List<AttributeTypeDef> attributeTypeDefs)
    {
        this.attributeTypeDefs = new ArrayList<>(attributeTypeDefs);
    }


    @Override
    public String toString()
    {
        return "AttributeTypeDefListResponse{" +
                "attributeTypeDefs=" + attributeTypeDefs +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                '}';
    }
}
