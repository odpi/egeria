/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.auditlog;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogRecordSeverity;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AuditLogReportSeverity provides information about the different types of severities defined for the audit log.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMRSAuditLogReportSeverity implements AuditLogRecordSeverity, Serializable
{
    private static final long    serialVersionUID = 1L;

    private  int    ordinal;
    private  String name;
    private  String description;


    /**
     * Default constructor
     */
    public OMRSAuditLogReportSeverity()
    {
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy.
     */
    public OMRSAuditLogReportSeverity(AuditLogRecordSeverity template)
    {
        if (template != null)
        {
            this.ordinal     = template.getOrdinal();
            this.name        = template.getName();
            this.description = template.getDescription();
        }
    }


    /**
     * Return the numerical code for this enum.
     *
     * @return int componentId
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Set up he numerical code for this enum.
     *
     * @param ordinal identifier
     */
    public void setOrdinal(int ordinal)
    {
        this.ordinal = ordinal;
    }


    /**
     * Return the name of the component.  This is the name used in the audit log records.
     *
     * @return String component name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the component.  This is the name used in the audit log records.
     *
     * @param name String component name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the short description of the component. This is an English description.  Natural language support for
     * these values can be added to UIs using a resource bundle indexed with the component Id.  This value is
     * provided as a default if the resource bundle is not available.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the short description of the component. This is an English description.  Natural language support for
     * these values can be added to UIs using a resource bundle indexed with the component Id.  This value is
     * provided as a default if the resource bundle is not available.
     *
     * @param description String description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }



    /**
     * toString, JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OMRSAuditLogReportSeverity{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        OMRSAuditLogReportSeverity that = (OMRSAuditLogReportSeverity) objectToCompare;
        return ordinal == that.ordinal &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(ordinal, name, description);
    }
}
