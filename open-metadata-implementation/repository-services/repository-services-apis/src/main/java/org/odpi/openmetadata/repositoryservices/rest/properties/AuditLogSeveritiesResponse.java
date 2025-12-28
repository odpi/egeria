/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogReportSeverity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AuditLogSeveritiesResponse support an OMRS REST API response that returns a list of Audit log severity definition objects.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AuditLogSeveritiesResponse extends OMRSAPIResponse
{
    private List<AuditLogRecordSeverity> severities = null;


    /**
     * Default constructor
     */
    public AuditLogSeveritiesResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AuditLogSeveritiesResponse(AuditLogSeveritiesResponse template)
    {
        super(template);

        if (template != null)
        {
            severities = template.getSeverities();
        }
    }


    /**
     * Return the list of severity values.
     *
     * @return severities list
     */
    public List<AuditLogRecordSeverity> getSeverities()
    {
        if (severities == null)
        {
            return null;
        }
        else if (severities.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(severities);
        }
    }


    /**
     * Set up the list of severity values.
     *
     * @param severities severities list
     */
    public void setSeverities(List<AuditLogRecordSeverity> severities)
    {
        this.severities = severities;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AuditLogSeveritiesResponse{" +
                "severities=" + severities +
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
        if (!(objectToCompare instanceof AuditLogSeveritiesResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AuditLogSeveritiesResponse
                that = (AuditLogSeveritiesResponse) objectToCompare;
        return Objects.equals(getSeverities(), that.getSeverities());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getSeverities());
    }
}
