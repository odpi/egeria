/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.auditlog;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSAuditLogDestinationReport is a container for properties about the logging destinations
 * supported by this audit log.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMRSAuditLogDestinationsReport implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private List<OMRSAuditLogStoreReport> logStoreReports = null;


    /**
     * Default constructor
     */
    public OMRSAuditLogDestinationsReport()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OMRSAuditLogDestinationsReport(OMRSAuditLogDestinationsReport template)
    {
        if (template != null)
        {
            logStoreReports = template.getLogStoreReports();
        }
    }


    /**
     * Return the report for each audit log store.
     *
     * @return list of properties for each audit log store
     */
    public List<OMRSAuditLogStoreReport> getLogStoreReports()
    {
        if (logStoreReports == null)
        {
            return null;
        }
        else if (logStoreReports.isEmpty())
        {
            return null;
        }

        return logStoreReports;
    }


    /**
     * Set up the report for each audit log store.
     *
     * @param logStoreReports list of properties for each audit log store
     */
    public void setLogStoreReports(List<OMRSAuditLogStoreReport> logStoreReports)
    {
        this.logStoreReports = logStoreReports;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OMRSAuditLogDestinationsReport{" +
                "logStoreReports=" + logStoreReports +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
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
        OMRSAuditLogDestinationsReport that = (OMRSAuditLogDestinationsReport) objectToCompare;
        return Objects.equals(logStoreReports, that.logStoreReports);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(logStoreReports);
    }
}
