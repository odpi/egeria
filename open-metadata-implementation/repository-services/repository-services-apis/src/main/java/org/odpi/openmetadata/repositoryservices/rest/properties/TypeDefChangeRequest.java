/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TypeDefChangeRequest carries details of the old and new TypeDef used to re-type an instance.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeDefChangeRequest extends OMRSAPIRequest
{
    private static final long    serialVersionUID = 1L;

    private TypeDefSummary currentTypeDef = null;
    private TypeDefSummary newTypeDef     = null;


    /**
     * Default constructor
     */
    public TypeDefChangeRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TypeDefChangeRequest(TypeDefChangeRequest template)
    {
        super(template);

        if (template != null)
        {
            this.currentTypeDef = template.getCurrentTypeDef();
            this.newTypeDef = template.getNewTypeDef();
        }
    }


    /**
     * Return the current type.
     *
     * @return TypeDef summary
     */
    public TypeDefSummary getCurrentTypeDef()
    {
        return currentTypeDef;
    }


    /**
     * Set up the current type
     *
     * @param currentTypeDef TypeDef summary
     */
    public void setCurrentTypeDef(TypeDefSummary currentTypeDef)
    {
        this.currentTypeDef = currentTypeDef;
    }


    /**
     * Return the new type.
     *
     * @return TypeDef summary
     */
    public TypeDefSummary getNewTypeDef()
    {
        return newTypeDef;
    }


    /**
     * Set up the new type.
     *
     * @param newTypeDef TypeDef summary
     */
    public void setNewTypeDef(TypeDefSummary newTypeDef)
    {
        this.newTypeDef = newTypeDef;
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
                "currentTypeDef=" + currentTypeDef +
                "newTypeDef=" + newTypeDef +
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
        if (!(objectToCompare instanceof TypeDefChangeRequest))
        {
            return false;
        }
        TypeDefChangeRequest
                that = (TypeDefChangeRequest) objectToCompare;
        return Objects.equals(getCurrentTypeDef(), that.getCurrentTypeDef()) &&
                Objects.equals(getNewTypeDef(), that.getNewTypeDef());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getCurrentTypeDef(), getNewTypeDef());
    }
}
