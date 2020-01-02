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
 * TypeDefReIdentifyRequest carries the TypeDef properties for changing the identity of a TypeDef.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeDefReIdentifyRequest extends OMRSAPIRequest
{
    private static final long    serialVersionUID = 1L;

    private String originalTypeDefName = null;
    private String newTypeDefGUID = null;
    private String newTypeDefName = null;


    /**
     * Default constructor
     */
    public TypeDefReIdentifyRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TypeDefReIdentifyRequest(TypeDefReIdentifyRequest template)
    {
        super(template);

        if (template != null)
        {
            this.originalTypeDefName = template.getOriginalTypeDefName();
            this.newTypeDefGUID = template.getNewTypeDefGUID();
            this.newTypeDefName = template.getNewTypeDefName();
        }
    }


    /**
     * Return the TypeDef's original name.
     *
     * @return String Name
     */
    public String getOriginalTypeDefName()
    {
        return originalTypeDefName;
    }


    /**
     * Set up the TypeDef's original name.
     *
     * @param originalTypeDefName String name
     */
    public void setOriginalTypeDefName(String originalTypeDefName)
    {
        this.originalTypeDefName = originalTypeDefName;
    }



    /**
     * Return the TypeDef's new unique identifier.
     *
     * @return String guid
     */
    public String getNewTypeDefGUID()
    {
        return newTypeDefGUID;
    }


    /**
     * Set up the TypeDef's new unique identifier.
     *
     * @param newTypeDefGUID String guid
     */
    public void setNewTypeDefGUID(String newTypeDefGUID)
    {
        this.newTypeDefGUID = newTypeDefGUID;
    }


    /**
     * Return the TypeDef's new name.
     *
     * @return String Name
     */
    public String getNewTypeDefName()
    {
        return newTypeDefName;
    }


    /**
     * Set up the TypeDef's new name.
     *
     * @param newTypeDefName String name
     */
    public void setNewTypeDefName(String newTypeDefName)
    {
        this.newTypeDefName = newTypeDefName;
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
                "originalTypeDefName=" + originalTypeDefName +
                "newTypeDefGUID=" + newTypeDefGUID +
                "newTypeDefName=" + newTypeDefName +
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
        if (!(objectToCompare instanceof TypeDefReIdentifyRequest))
        {
            return false;
        }
        TypeDefReIdentifyRequest
                that = (TypeDefReIdentifyRequest) objectToCompare;
        return Objects.equals(getOriginalTypeDefName(), that.getOriginalTypeDefName()) &&
               Objects.equals(getNewTypeDefGUID(), that.getNewTypeDefGUID()) &&
                Objects.equals(getNewTypeDefName(), that.getNewTypeDefName());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getOriginalTypeDefName(), getNewTypeDefGUID(), getNewTypeDefName());
    }
}
