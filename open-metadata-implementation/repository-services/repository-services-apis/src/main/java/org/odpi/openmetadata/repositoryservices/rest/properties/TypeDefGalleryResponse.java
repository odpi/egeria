/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TypeDefGalleryResponse provides the response structure for an OMRS REST API call that returns a TypeDefGallery.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeDefGalleryResponse extends OMRSRESTAPIResponse
{
    private List<AttributeTypeDef> attributeTypeDefs = null;
    private List<TypeDef>          typeDefs          = null;


    /**
     * Default constructor
     */
    public TypeDefGalleryResponse()
    {
    }


    /**
     * Return the list of attribute type definitions from this gallery.
     *
     * @return list of AttributeTypeDefs
     */
    public List<AttributeTypeDef> getAttributeTypeDefs()
    {
        return attributeTypeDefs;
    }


    /**
     * Set up the list of attribute type definitions from this gallery.
     *
     * @param attributeTypeDefs list of AttributeTypeDefs
     */
    public void setAttributeTypeDefs(List<AttributeTypeDef> attributeTypeDefs)
    {
        this.attributeTypeDefs = new ArrayList<>(attributeTypeDefs);
    }


    /**
     * Return the list of type definitions from this gallery
     *
     * @return list of TypeDefs
     */
    public List<TypeDef> getTypeDefs()
    {
        return typeDefs;
    }


    /**
     * Set up the list of type definitions for this gallery.
     *
     * @param typeDefs list of type definitions
     */
    public void setTypeDefs(List<TypeDef> typeDefs)
    {
        this.typeDefs = new ArrayList<>(typeDefs);
    }


    @Override
    public String toString()
    {
        return "TypeDefGalleryResponse{" +
                "attributeTypeDefs=" + attributeTypeDefs +
                ", typeDefs=" + typeDefs +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                '}';
    }
}
