/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
    /*
     * Held as the concrete OMRSAuditLogReportSeverity rather than as the AuditLogRecordSeverity interface the
     * accessors use, and that is what makes this response readable at all.
     *
     * The severities the server supplies are AuditLogRecordSeverityLevel enum constants.  Jackson serializes
     * an enum as its constant name, so the body used to be ["UNKNOWN","INFO",...] - which dropped the ordinal
     * and the description, the two things an endpoint called "severity-definitions" exists to provide, and
     * which no client could read back: deserializing a bare string into the AuditLogRecordSeverity interface
     * fails with "abstract types either need to be mapped to concrete types".  AuditLogServicesClient
     * .getSeverityList() therefore could not succeed against any server.
     *
     * Converting on the way in gives a body of {"ordinal":..,"name":..,"description":..} objects, which
     * carries the whole definition and which Jackson can construct on the way back.  The accessors still take
     * and return the interface, so callers on both sides are unaffected.
     */
    private List<OMRSAuditLogReportSeverity> severities = null;


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
            this.setSeverities(template.getSeverities());
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
            return new ArrayList<>();
        }

        return new ArrayList<>(severities);
    }


    /**
     * Set up the list of severity values.
     * <br><br>
     * The {@code contentAs} is what makes the response readable.  Jackson picks the deserializer from this
     * setter's declared parameter type, and {@link AuditLogRecordSeverity} is an interface it cannot
     * construct - so without this the whole response fails with "abstract types either need to be mapped to
     * concrete types", however the elements were serialized.
     *
     * @param severities severities list
     */
    @JsonDeserialize(contentAs = OMRSAuditLogReportSeverity.class)
    public void setSeverities(List<AuditLogRecordSeverity> severities)
    {
        if (severities == null)
        {
            this.severities = null;
        }
        else
        {
            this.severities = new ArrayList<>();

            for (AuditLogRecordSeverity severity : severities)
            {
                if (severity != null)
                {
                    this.severities.add(new OMRSAuditLogReportSeverity(severity));
                }
            }
        }
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
        if (!(objectToCompare instanceof AuditLogSeveritiesResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
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
