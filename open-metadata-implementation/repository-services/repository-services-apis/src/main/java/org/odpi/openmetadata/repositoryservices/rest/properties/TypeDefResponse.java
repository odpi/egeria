/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TypeDefResponse provides the response structure for an OMRS API call that returns a TypeDef
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeDefResponse extends OMRSRESTAPIResponse
{
    private TypeDef typeDef = null;


    /**
     * Default constructor
     */
    public TypeDefResponse()
    {
    }


    /**
     * Return the resulting TypeDef object.
     *
     * @return TypeDef object
     */
    public TypeDef getTypeDef()
    {
        return typeDef;
    }


    /**
     * Set up the response Typedef object
     *
     * @param typeDef - Typedef object
     */
    public void setTypeDef(TypeDef typeDef)
    {
        this.typeDef = typeDef;
    }


    @Override
    public String toString()
    {
        return "TypeDefResponse{" +
                "typeDef=" + typeDef +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                '}';
    }
}
