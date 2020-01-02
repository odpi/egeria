/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TypeDefValidationForRequest carries the TypeDef validation properties for destructive requests (such as delete).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeDefValidationForRequest extends OMRSAPIRequest
{
    private static final long    serialVersionUID = 1L;

    private String    typeDefGUID = null;
    private String    typeDefName = null;


    /**
     * Default constructor
     */
    public TypeDefValidationForRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TypeDefValidationForRequest(TypeDefValidationForRequest template)
    {
        super(template);

        if (template != null)
        {
            this.typeDefGUID = template.getTypeDefGUID();
            this.typeDefName = template.getTypeDefName();
        }
    }


    /**
     * Return the TypeDef's unique identifier.
     *
     * @return String guid
     */
    public String getTypeDefGUID()
    {
        return typeDefGUID;
    }


    /**
     * Set up the TypeDef's unique identifier.
     *
     * @param typeDefGUID String guid
     */
    public void setTypeDefGUID(String typeDefGUID)
    {
        this.typeDefGUID = typeDefGUID;
    }


    /**
     * Return the TypeDef's name.
     *
     * @return String Name
     */
    public String getTypeDefName()
    {
        return typeDefName;
    }


    /**
     * Set up the TypeDef's name.
     *
     * @param typeDefName String name
     */
    public void setTypeDefName(String typeDefName)
    {
        this.typeDefName = typeDefName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "TypeDefCategoryFindRequest{" +
                "typeDefGUID=" + typeDefGUID +
                "typeDefName=" + typeDefName +
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
        if (!(objectToCompare instanceof TypeDefValidationForRequest))
        {
            return false;
        }
        TypeDefValidationForRequest
                that = (TypeDefValidationForRequest) objectToCompare;
        return Objects.equals(getTypeDefGUID(), that.getTypeDefGUID()) &&
                Objects.equals(getTypeDefName(), that.getTypeDefName());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getTypeDefGUID(), getTypeDefName());
    }
}
