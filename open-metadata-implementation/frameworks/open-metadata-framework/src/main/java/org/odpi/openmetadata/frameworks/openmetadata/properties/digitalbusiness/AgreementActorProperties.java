/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AgreementActorProperties describes a relationship between an actor and an agreement.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgreementActorProperties extends RelationshipBeanProperties
{
    private String agreementPartyName = null;


    /**
     * Default constructor
     */
    public AgreementActorProperties()
    {
        super();
        super.typeName = OpenMetadataType.AGREEMENT_ACTOR_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public AgreementActorProperties(AgreementActorProperties template)
    {
        super(template);

        if (template != null)
        {
            agreementPartyName = template.getAgreementPartyName();
        }
    }


    /**
     * Set up the name of the party from the agreement text.
     *
     * @param agreementPartyName String name
     */
    public void setAgreementPartyName(String agreementPartyName)
    {
        this.agreementPartyName = agreementPartyName;
    }


    /**
     * Returns the name of the party from the agreement text.
     *
     * @return String name
     */
    public String getAgreementPartyName()
    {
        return agreementPartyName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AgreementActorProperties{" +
                "agreementPartyName'" + agreementPartyName + '\'' +
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
        if (! (objectToCompare instanceof AgreementActorProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(agreementPartyName, that.agreementPartyName);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), agreementPartyName);
    }
}
