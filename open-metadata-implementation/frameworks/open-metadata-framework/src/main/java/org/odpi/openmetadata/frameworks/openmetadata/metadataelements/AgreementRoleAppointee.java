/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementActorProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ActorRoleAppointee is the bean used to return a role and current appointee(s).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AgreementRoleAppointee extends ActorRoleAppointee
{
    private AgreementActorProperties agreementActorProperties = null;


    /**
     * Default constructor
     */
    public AgreementRoleAppointee()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AgreementRoleAppointee(AgreementRoleAppointee template)
    {
        super(template);

        if (template != null)
        {
            agreementActorProperties = template.getAgreementRoleProperties();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AgreementRoleAppointee(ActorRoleElement template)
    {
        super(template);
    }


    /**
     * Return the role information from the agreement.
     *
     * @return agreement role properties
     */
    public AgreementActorProperties getAgreementRoleProperties()
    {
        return agreementActorProperties;
    }


    /**
     * Set up the role information from the agreement.
     *
     * @param agreementActorProperties agreement role properties
     */
    public void setAgreementRoleProperties(AgreementActorProperties agreementActorProperties)
    {
        this.agreementActorProperties = agreementActorProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AgreementRoleAppointee{" +
                "agreementActorProperties=" + agreementActorProperties +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AgreementRoleAppointee that = (AgreementRoleAppointee) objectToCompare;
        return Objects.equals(agreementActorProperties, that.agreementActorProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), agreementActorProperties);
    }
}
