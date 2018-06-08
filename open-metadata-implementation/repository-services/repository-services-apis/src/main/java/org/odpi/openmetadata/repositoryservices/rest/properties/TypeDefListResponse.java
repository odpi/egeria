/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TypeDefListResponse provides a simple bean for returning a list of TypeDefs (or information to create
 * a valid OMRS exception).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeDefListResponse extends OMRSRESTAPIResponse
{
    private List<TypeDef> typeDefs = null;

    /**
     * Default Constructor
     */
    public TypeDefListResponse()
    {
    }


    /**
     * Return the list of typeDefs
     *
     * @return - list of typeDefs
     */
    public List<TypeDef> getTypeDefs()
    {
        return typeDefs;
    }


    /**
     * Set up the list of typeDefs
     *
     * @param typeDefs - list of typeDefs
     */
    public void setTypeDefs(List<TypeDef> typeDefs)
    {
        this.typeDefs = new ArrayList<>(typeDefs);
    }


    @Override
    public String toString()
    {
        return "TypeDefListResponse{" +
                "typeDefs=" + typeDefs +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                '}';
    }
}
