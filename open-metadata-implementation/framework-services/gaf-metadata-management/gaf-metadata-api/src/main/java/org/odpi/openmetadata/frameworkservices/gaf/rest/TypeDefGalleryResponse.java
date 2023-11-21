/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataAttributeTypeDef;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataTypeDef;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TypeDefGalleryResponse provides the response structure for an OMRS REST API call that returns a TypeDefGallery.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeDefGalleryResponse extends OMAGGAFAPIResponse
{
    private List<OpenMetadataAttributeTypeDef> attributeTypeDefs = null;
    private List<OpenMetadataTypeDef>          typeDefs          = null;


    /**
     * Default constructor
     */
    public TypeDefGalleryResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TypeDefGalleryResponse(TypeDefGalleryResponse template)
    {
        super(template);

        if (template != null)
        {
            attributeTypeDefs = template.getAttributeTypeDefs();
            typeDefs = template.getTypeDefs();
        }
    }


    /**
     * Return the list of attribute type definitions from this gallery.
     *
     * @return list of AttributeTypeDefs
     */
    public List<OpenMetadataAttributeTypeDef> getAttributeTypeDefs()
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
            List<OpenMetadataAttributeTypeDef>  clonedTypeDefs = new ArrayList<>();

            for (OpenMetadataAttributeTypeDef  attributeTypeDef : attributeTypeDefs)
            {
                clonedTypeDefs.add(attributeTypeDef.cloneFromSubclass());
            }

            return clonedTypeDefs;
        }
    }


    /**
     * Set up the list of attribute type definitions from this gallery.
     *
     * @param attributeTypeDefs list of AttributeTypeDefs
     */
    public void setAttributeTypeDefs(List<OpenMetadataAttributeTypeDef> attributeTypeDefs)
    {
        this.attributeTypeDefs = attributeTypeDefs;
    }


    /**
     * Return the list of type definitions from this gallery
     *
     * @return list of TypeDefs
     */
    public List<OpenMetadataTypeDef> getTypeDefs()
    {
        if (typeDefs == null)
        {
            return null;
        }
        else if (typeDefs.isEmpty())
        {
            return null;
        }
        else
        {
            List<OpenMetadataTypeDef>  clonedTypeDefs = new ArrayList<>();

            for (OpenMetadataTypeDef  typeDef : typeDefs)
            {
                clonedTypeDefs.add(typeDef.cloneFromSubclass());
            }

            return clonedTypeDefs;
        }
    }


    /**
     * Set up the list of type definitions for this gallery.
     *
     * @param typeDefs list of type definitions
     */
    public void setTypeDefs(List<OpenMetadataTypeDef> typeDefs)
    {
        this.typeDefs = typeDefs;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "TypeDefGalleryResponse{" +
                       "attributeTypeDefs=" + attributeTypeDefs +
                       ", typeDefs=" + typeDefs +
                       ", exceptionClassName='" + getExceptionClassName() + '\'' +
                       ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                       ", actionDescription='" + getActionDescription() + '\'' +
                       ", relatedHTTPCode=" + getRelatedHTTPCode() +
                       ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                       ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                       ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                       ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                       ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                       ", exceptionProperties=" + getExceptionProperties() +
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
        if (!(objectToCompare instanceof TypeDefGalleryResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(getAttributeTypeDefs(), that.getAttributeTypeDefs()) &&
                Objects.equals(getTypeDefs(), that.getTypeDefs());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getAttributeTypeDefs(), getTypeDefs());
    }
}
