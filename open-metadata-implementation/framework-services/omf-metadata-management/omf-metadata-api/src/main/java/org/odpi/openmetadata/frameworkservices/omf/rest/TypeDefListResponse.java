/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDef;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TypeDefListResponse provides a simple bean for returning a list of TypeDefs (or information to create
 * a valid exception).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeDefListResponse extends OMAGOMFAPIResponse
{
    private List<OpenMetadataTypeDef> typeDefs = null;

    /**
     * Default Constructor
     */
    public TypeDefListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TypeDefListResponse(TypeDefListResponse template)
    {
        super(template);

        if (template != null)
        {
            typeDefs = template.getTypeDefs();
        }
    }


    /**
     * Return the list of typeDefs
     *
     * @return list of typeDefs
     */
    public List<OpenMetadataTypeDef> getTypeDefs()
    {
        return typeDefs;
    }


    /**
     * Set up the list of typeDefs
     *
     * @param typeDefs list of typeDefs
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
        return "TypeDefListResponse{" +
                "typeDefs=" + typeDefs +
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
        if (!(objectToCompare instanceof TypeDefListResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(getTypeDefs(), that.getTypeDefs());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getTypeDefs());
    }
}
