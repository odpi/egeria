/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports;

import org.odpi.openmetadata.frameworks.openmetadata.enums.IncidentReportStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

/**
 * An incident report describes an event of situation that has/had an impact on the operation of the organization.
 */
public class IncidentReportProperties extends ReportProperties
{
    private String               background          = null;
    private IncidentReportStatus incidentStatus      = null;
    private Map<String, Integer> incidentClassifiers = null;
    private int                  domainIdentifier    = 0;


    /**
     * Default constructor
     */
    public IncidentReportProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.INCIDENT_REPORT.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public IncidentReportProperties(IncidentReportProperties template)
    {
        super(template);

        if (template != null)
        {
            this.background          = template.getBackground();
            this.incidentStatus      = template.getIncidentStatus();
            this.incidentClassifiers = template.getIncidentClassifiers();
            this.domainIdentifier    = template.getDomainIdentifier();
        }
    }


    /**
     * Return the background information that helps to understand the cause or impact of this incident.
     *
     * @return string name
     */
    public String getBackground()
    {
        return background;
    }


    /**
     * Set up the background information that helps to understand the cause or impact of this incident.
     *
     * @param background string name
     */
    public void setBackground(String background)
    {
        this.background = background;
    }


    /**
     * Return the status of the incident
     *
     * @return enum
     */
    public IncidentReportStatus getIncidentStatus()
    {
        return incidentStatus;
    }


    /**
     * Set up the status of the incident.
     *
     * @param incidentStatus enum
     */
    public void setIncidentStatus(IncidentReportStatus incidentStatus)
    {
        this.incidentStatus = incidentStatus;
    }


    /**
     * Return the map of classifiers (name to identifier) that help to classify this incident.
     *
     * @return map
     */
    public Map<String, Integer> getIncidentClassifiers()
    {
        return incidentClassifiers;
    }


    /**
     * Set up the map of classifiers (name to identifier) that help to classify this incident.
     *
     * @param incidentClassifiers string definition
     */
    public void setIncidentClassifiers(Map<String, Integer>  incidentClassifiers)
    {
        this.incidentClassifiers = incidentClassifiers;
    }


    /**
     * Return the identifier of the governance domain that this incident related to (or zero for none/all).
     *
     * @return int identifier
     */
    public int getDomainIdentifier()
    {
        return domainIdentifier;
    }


    /**
     * Set up the identifier of the governance domain that this incident related to (or zero for none/all).
     *
     * @param domainIdentifier int identifier
     */
    public void setDomainIdentifier(int domainIdentifier)
    {
        this.domainIdentifier = domainIdentifier;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "IncidentReportProperties{" +
                "background='" + background + '\'' +
                ", incidentStatus=" + incidentStatus +
                ", incidentClassifiers=" + incidentClassifiers +
                ", domainIdentifier=" + domainIdentifier +
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
        IncidentReportProperties that = (IncidentReportProperties) objectToCompare;
        return domainIdentifier == that.domainIdentifier &&
                       Objects.equals(background, that.background) &&
                       Objects.equals(incidentStatus, that.incidentStatus) &&
                       Objects.equals(incidentClassifiers, that.incidentClassifiers);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), background, incidentStatus, incidentClassifiers, domainIdentifier);
    }
}
