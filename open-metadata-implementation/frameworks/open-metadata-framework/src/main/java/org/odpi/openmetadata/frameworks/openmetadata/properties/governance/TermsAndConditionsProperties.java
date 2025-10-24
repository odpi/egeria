/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TermsAndConditionsProperties describe terms and conditions, typically associated with an agreement.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = CertificationTypeProperties.class, name = "CertificationTypeProperties"),
                @JsonSubTypes.Type(value = LicenseTypeProperties.class, name = "LicenseTypeProperties"),
                @JsonSubTypes.Type(value = ServiceLevelObjectiveProperties.class, name = "ServiceLevelObjectiveProperties"),
        })
public class TermsAndConditionsProperties extends GovernanceControlProperties
{
    private Map<String, String> entitlements = null;
    private Map<String, String> restrictions = null;
    private Map<String, String> obligations  = null;


    /**
     * Default Constructor
     */
    public TermsAndConditionsProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.TERMS_AND_CONDITIONS.typeName);
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public TermsAndConditionsProperties(TermsAndConditionsProperties template)
    {
        super(template);

        if (template != null)
        {
            this.entitlements = template.getEntitlements();
            this.restrictions = template.getRestrictions();
            this.obligations = template.getObligations();
        }
    }


    /**
     * Return the entitlements granted by the license.
     *
     * @return name value pairs
     */
    public Map<String, String> getEntitlements()
    {
        return entitlements;
    }


    /**
     * Set up the entitlements granted by the license.
     *
     * @param entitlements name value pairs
     */
    public void setEntitlements(Map<String, String> entitlements)
    {
        this.entitlements = entitlements;
    }


    /**
     * Return the restrictions imposed by the license.
     *
     * @return name value pairs
     */
    public Map<String, String> getRestrictions()
    {
        return restrictions;
    }


    /**
     * Set up the restrictions imposed by the license.
     *
     * @param restrictions name value pairs
     */
    public void setRestrictions(Map<String, String> restrictions)
    {
        this.restrictions = restrictions;
    }


    /**
     * Return the obligations stipulated by the license.
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
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "TermsAndConditionsProperties{" +
                "entitlements=" + entitlements +
                ", restrictions=" + restrictions +
                ", obligations=" + obligations +
                "} " + super.toString();
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     *
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof TermsAndConditionsProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(entitlements, that.entitlements) &&
                       Objects.equals(restrictions, that.restrictions) &&
                       Objects.equals(obligations, that.obligations);
    }


    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), obligations, restrictions, entitlements);
    }
}
