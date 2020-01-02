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
 * TypeDefDeleteRequest carries the TypeDef properties for deleting a TypeDef.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeDefDeleteRequest extends OMRSAPIRequest
{
    private static final long    serialVersionUID = 1L;

    private String obsoleteTypeDefName = null;


    /**
     * Default constructor
     */
    public TypeDefDeleteRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TypeDefDeleteRequest(TypeDefDeleteRequest template)
    {
        super(template);

        if (template != null)
        {
            this.obsoleteTypeDefName = template.getObsoleteTypeDefName();
        }
    }


    /**
     * Return the TypeDef's name.
     *
     * @return String Name
     */
    public String getObsoleteTypeDefName()
    {
        return obsoleteTypeDefName;
    }


    /**
     * Set up the TypeDef's name.
     *
     * @param obsoleteTypeDefName String name
     */
    public void setObsoleteTypeDefName(String obsoleteTypeDefName)
    {
        this.obsoleteTypeDefName = obsoleteTypeDefName;
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
                "obsoleteTypeDefName=" + obsoleteTypeDefName +
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
        if (!(objectToCompare instanceof TypeDefDeleteRequest))
        {
            return false;
        }
        TypeDefDeleteRequest
                that = (TypeDefDeleteRequest) objectToCompare;
        return Objects.equals(getObsoleteTypeDefName(), that.getObsoleteTypeDefName());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getObsoleteTypeDefName());
    }
}
