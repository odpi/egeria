/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.security;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssociatedGroupProperties identified an operation that the security groups map to.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssociatedGroupProperties extends RelationshipProperties
{
    String operationName = null;

    /**
     * Default constructor
     */
    public AssociatedGroupProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.ASSOCIATED_GROUP_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssociatedGroupProperties(AssociatedGroupProperties template)
    {
        super(template);

        if (template != null)
        {
            this.operationName = template.getOperationName();
        }
    }


    /**
     * Return the name of the associated operation.
     *
     * @return name
     */
    public String getOperationName()
    {
        return operationName;
    }


    /**
     * Set up the name of the associated operation.
     *
     * @param operationName name
     */
    public void setOperationName(String operationName)
    {
        this.operationName = operationName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssociatedGroupProperties{" +
                "operationName='" + operationName + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        AssociatedGroupProperties that = (AssociatedGroupProperties) objectToCompare;
        return Objects.equals(operationName, that.operationName);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), operationName);
    }
}
