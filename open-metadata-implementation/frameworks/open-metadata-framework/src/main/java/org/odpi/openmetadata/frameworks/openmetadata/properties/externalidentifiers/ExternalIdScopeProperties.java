/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalIdScopeProperties links an external identifier to the software capability that describes the
 * external source.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalIdScopeProperties extends RelationshipBeanProperties
{
    private PermittedSynchronization permittedSynchronization = null;


    /**
     * Default constructor
     */
    public ExternalIdScopeProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.EXTERNAL_ID_SCOPE_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public ExternalIdScopeProperties(ExternalIdScopeProperties template)
    {
        super(template);

        if (template != null)
        {
            permittedSynchronization = template.getSynchronizationDirection();
        }
    }


    /**
     * Return details of the synchronization direction.
     *
     * @return enum
     */
    public PermittedSynchronization getSynchronizationDirection()
    {
        return permittedSynchronization;
    }


    /**
     * Set up details of the synchronization direction.
     *
     * @param permittedSynchronization enum
     */
    public void setSynchronizationDirection(PermittedSynchronization permittedSynchronization)
    {
        this.permittedSynchronization = permittedSynchronization;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ExternalIdScopeProperties{" +
                "permittedSynchronization=" + permittedSynchronization +
                ", synchronizationDirection=" + getSynchronizationDirection() +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ExternalIdScopeProperties that = (ExternalIdScopeProperties) objectToCompare;
        return permittedSynchronization == that.permittedSynchronization;
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), permittedSynchronization);
    }
}
