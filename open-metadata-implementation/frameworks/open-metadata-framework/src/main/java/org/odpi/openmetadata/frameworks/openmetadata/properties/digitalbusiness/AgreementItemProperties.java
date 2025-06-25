/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AgreementActorProperties describes a relationship between an actor and an agreement.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgreementItemProperties extends RelationshipProperties
{
    private String              agreementItemId   = null;
    private Date                agreementStart    = null;
    private Date                agreementEnd      = null;
    private Map<String, String> entitlements      = null;
    private Map<String, String> restrictions      = null;
    private Map<String, String> obligations       = null;
    private Map<String, String> usageMeasurements = null;

    /**
     * Default constructor
     */
    public AgreementItemProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public AgreementItemProperties(AgreementItemProperties template)
    {
        super(template);

        if (template != null)
        {
            this.agreementItemId = template.getAgreementItemId();
            this.agreementStart = template.getAgreementStart();
            this.agreementEnd = template.getAgreementEnd();
            this.entitlements = template.getEntitlements();
            this.restrictions = template.getRestrictions();
            this.obligations = template.getObligations();
            this.usageMeasurements = template.getUsageMeasurements();
        }
    }


    /**
     * Return the unique identifier for the agreement item.
     *
     * @return string
     */
    public String getAgreementItemId()
    {
        return agreementItemId;
    }


    /**
     * Set up the unique identifier for the agreement item.
     *
     * @param agreementItemId string
     */
    public void setAgreementItemId(String agreementItemId)
    {
        this.agreementItemId = agreementItemId;
    }


    /**
     * Return the start time for the agreement.
     *
     * @return date
     */
    public Date getAgreementStart()
    {
        return agreementStart;
    }


    /**
     * Set up the start time for the agreement.
     *
     * @param agreementStart date
     */
    public void setAgreementStart(Date agreementStart)
    {
        this.agreementStart = agreementStart;
    }


    /**
     * Return the expected end date for the subscription.
     *
     * @return date
     */
    public Date getAgreementEnd()
    {
        return agreementEnd;
    }


    /**
     * Set up the expected end date for the agreement.
     *
     * @param agreementEnd date
     */
    public void setAgreementEnd(Date agreementEnd)
    {
        this.agreementEnd = agreementEnd;
    }

    /**
     * Return the entitlements granted by the agreement.
     *
     * @return name value pairs
     */
    public Map<String, String> getEntitlements()
    {
        return entitlements;
    }


    /**
     * Set up the entitlements granted by the agreement.
     *
     * @param entitlements name value pairs
     */
    public void setEntitlements(Map<String, String> entitlements)
    {
        this.entitlements = entitlements;
    }


    /**
     * Return the restrictions imposed by the agreement.
     *
     * @return name value pairs
     */
    public Map<String, String> getRestrictions()
    {
        return restrictions;
    }


    /**
     * Set up the restrictions imposed by the agreement.
     *
     * @param restrictions name value pairs
     */
    public void setRestrictions(Map<String, String> restrictions)
    {
        this.restrictions = restrictions;
    }


    /**
     * Return the obligations stipulated by the agreement.
     *
     * @return name value pairs
     */
    public Map<String, String> getObligations()
    {
        return obligations;
    }


    /**
     * Set up the obligations stipulated by the license.
     *
     * @param obligations name value pairs
     */
    public void setObligations(Map<String, String> obligations)
    {
        this.obligations = obligations;
    }


    /**
     * Return any captured measurements of the actual use of this item under the agreement.
     *
     * @return map of values
     */
    public Map<String, String> getUsageMeasurements()
    {
        return usageMeasurements;
    }


    /**
     * Set up any captured measurements of the actual use of this item under the agreement.
     *
     * @param usageMeasurements map of values
     */
    public void setUsageMeasurements(Map<String, String> usageMeasurements)
    {
        this.usageMeasurements = usageMeasurements;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AgreementItemProperties{" +
                "agreementItemId='" + agreementItemId + '\'' +
                ", agreementStart=" + agreementStart +
                ", agreementEnd=" + agreementEnd +
                ", entitlements=" + entitlements +
                ", restrictions=" + restrictions +
                ", obligations=" + obligations +
                ", usageMeasurements=" + usageMeasurements +
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
        AgreementItemProperties that = (AgreementItemProperties) objectToCompare;
        return Objects.equals(agreementItemId, that.agreementItemId) && Objects.equals(agreementStart, that.agreementStart) && Objects.equals(agreementEnd, that.agreementEnd) && Objects.equals(entitlements, that.entitlements) && Objects.equals(restrictions, that.restrictions) && Objects.equals(obligations, that.obligations) && Objects.equals(usageMeasurements, that.usageMeasurements);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), agreementItemId, agreementStart, agreementEnd, entitlements, restrictions, obligations, usageMeasurements);
    }
}
