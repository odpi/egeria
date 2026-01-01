/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.specificationproperties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GuardType describes a guard.  Guards are produced by governance services when they complete.
 * They indicate whether the action completed successfully or not, and they type of result.
 * This definition is used in the connector provider of a governance service to help
 * tools understand the operations of a service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ProducedGuard.class, name = "ProducedGuard"),
        })
public class GuardType extends SpecificationProperty
{

    /**
     * The typical completion status used with this guard.
     */
    private CompletionStatus completionStatus = null;


    /**
     * Default constructor
     */
    public GuardType()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GuardType(GuardType template)
    {
        super(template);

        if (template != null)
        {
            this.completionStatus = template.getCompletionStatus();
        }
    }


    /**
     * Return the completion status typically used with this guard.
     *
     * @return name of an open metadata type
     */
    public CompletionStatus getCompletionStatus()
    {
        return completionStatus;
    }


    /**
     * Set up the completion status typically used with this guard.
     *
     * @param completionStatus name of an open metadata type
     */
    public void setCompletionStatus(CompletionStatus completionStatus)
    {
        this.completionStatus = completionStatus;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GuardType{" +
                "completionStatus=" + completionStatus +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        GuardType guardType = (GuardType) objectToCompare;
        return completionStatus == guardType.completionStatus;
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), completionStatus);
    }
}
