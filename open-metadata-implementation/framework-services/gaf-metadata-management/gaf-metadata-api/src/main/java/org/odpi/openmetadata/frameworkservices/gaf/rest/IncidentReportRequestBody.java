/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.gaf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IncidentDependency;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IncidentImpactedElement;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * IncidentReportRequestBody provides a structure for passing the properties for a new incident report.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IncidentReportRequestBody implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private String                        qualifiedName = null;
    private int                           domainIdentifier = 0;
    private String                        background = null;
    private List<IncidentImpactedElement> impactedResources = null;
    private List<IncidentDependency>      previousIncidents = null;
    private Map<String, Integer>          incidentClassifiers = null;
    private Map<String, String>           additionalProperties = null;
    private String                        originatorGUID = null;


    /**
     * Default constructor
     */
    public IncidentReportRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public IncidentReportRequestBody(IncidentReportRequestBody template)
    {
        if (template != null)
        {
            qualifiedName = template.getQualifiedName();
            domainIdentifier = template.getDomainIdentifier();
            background = template.getBackground();
            impactedResources = template.getImpactedResources();
            previousIncidents = template.getPreviousIncidents();
            incidentClassifiers = template.getIncidentClassifiers();
            additionalProperties = template.getAdditionalProperties();
            originatorGUID = template.getOriginatorGUID();
        }
    }


    /**
     * Return the unique name of the new incident report.
     *
     * @return string name
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Set up the unique name of the new incident report.
     *
     * @param qualifiedName string name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Return the identifier of the governance domain that this incident report belongs to (0=ALL/ANY).
     *
     * @return int
     */
    public int getDomainIdentifier()
    {
        return domainIdentifier;
    }


    /**
     * Set up the identifier of the governance domain that this incident report belongs to (0=ALL/ANY).
     *
     * @param domainIdentifier int
     */
    public void setDomainIdentifier(int domainIdentifier)
    {
        this.domainIdentifier = domainIdentifier;
    }


    /**
     * Return the background information that describes how the incident occurred.
     *
     * @return string text
     */
    public String getBackground()
    {
        return background;
    }


    /**
     * Set up the background information that describes how the incident occurred.
     *
     * @param background string text
     */
    public void setBackground(String background)
    {
        this.background = background;
    }


    /**
     * Return the list of elements that are affected by this incident.
     *
     * @return list of element identifiers
     */
    public List<IncidentImpactedElement> getImpactedResources()
    {
        if (impactedResources == null)
        {
            return null;
        }

        if (impactedResources.isEmpty())
        {
            return null;
        }

        return impactedResources;
    }


    /**
     * Set up the list of elements that are affected by this incident.
     *
     * @param impactedResources list of element identifiers
     */
    public void setImpactedResources(List<IncidentImpactedElement> impactedResources)
    {
        this.impactedResources = impactedResources;
    }


    /**
     * Return details of the previously raised incidents that relate to this situation.
     *
     * @return list of incident report details
     */
    public List<IncidentDependency> getPreviousIncidents()
    {
        if (previousIncidents == null)
        {
            return null;
        }

        if (previousIncidents.isEmpty())
        {
            return null;
        }
        return previousIncidents;
    }


    /**
     * Set up details of the previously raised incidents that relate to this situation.
     *
     * @param previousIncidents list of incident report details
     */
    public void setPreviousIncidents(List<IncidentDependency> previousIncidents)
    {
        this.previousIncidents = previousIncidents;
    }


    /**
     * Return the map of classifiers that help to organize and prioritize this incident report.
     *
     * @return map of classifiers and values
     */
    public Map<String, Integer> getIncidentClassifiers()
    {
        if (incidentClassifiers == null)
        {
            return null;
        }

        if (incidentClassifiers.isEmpty())
        {
            return null;
        }

        return incidentClassifiers;
    }


    /**
     * Set up the map of classifiers that help to organize and prioritize this incident report.
     *
     * @param incidentClassifiers map of classifiers and values
     */
    public void setIncidentClassifiers(Map<String, Integer> incidentClassifiers)
    {
        this.incidentClassifiers = incidentClassifiers;
    }


    /**
     * Return any additional properties.
     *
     * @return property map
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }

        if (additionalProperties.isEmpty())
        {
            return null;
        }

        return additionalProperties;
    }


    /**
     * Set up any additional properties.
     *
     * @param additionalProperties property map
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return the unique identifier of the person or process that created the incident.
     * This this is typically an ActorProfile or Process.
     *
     * @return string guid
     */
    public String getOriginatorGUID()
    {
        return originatorGUID;
    }


    /**
     * Set up the unique identifier of the person or process that created the incident.
     *
     * @param originatorGUID string guid
     */
    public void setOriginatorGUID(String originatorGUID)
    {
        this.originatorGUID = originatorGUID;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "IncidentReportRequestBody{" +
                       "qualifiedName='" + qualifiedName + '\'' +
                       ", domainIdentifier=" + domainIdentifier +
                       ", background='" + background + '\'' +
                       ", impactedResources=" + impactedResources +
                       ", previousIncidents=" + previousIncidents +
                       ", incidentClassifiers=" + incidentClassifiers +
                       ", additionalProperties=" + additionalProperties +
                       ", originatorGUID=" + originatorGUID +
                       '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        IncidentReportRequestBody that = (IncidentReportRequestBody) objectToCompare;
        return domainIdentifier == that.domainIdentifier &&
                       Objects.equals(qualifiedName, that.qualifiedName) &&
                       Objects.equals(background, that.background) &&
                       Objects.equals(impactedResources, that.impactedResources) &&
                       Objects.equals(previousIncidents, that.previousIncidents) &&
                       Objects.equals(incidentClassifiers, that.incidentClassifiers) &&
                       Objects.equals(originatorGUID, that.originatorGUID) &&
                       Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(qualifiedName, domainIdentifier, background, impactedResources, previousIncidents, incidentClassifiers,
                            originatorGUID, additionalProperties);
    }
}
