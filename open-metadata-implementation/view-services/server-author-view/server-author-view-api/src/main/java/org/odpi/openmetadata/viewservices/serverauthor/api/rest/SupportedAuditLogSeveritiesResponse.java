/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.serverauthor.api.rest;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogReportSeverity;
import java.util.Arrays;
import java.util.List;


/**
 * A supported severities for audit log response containing the known platforms
 */
public class SupportedAuditLogSeveritiesResponse extends ServerAuthorViewOMVSAPIResponse {
    /**
     * Associated platform
     */
    List<OMRSAuditLogReportSeverity> severities = null;

    /**
     * Default constructor
     */
    public SupportedAuditLogSeveritiesResponse() {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SupportedAuditLogSeveritiesResponse(SupportedAuditLogSeveritiesResponse template) {
        super(template);

        if (template != null) {
            this.severities = template.getSeverities();
        }
    }

    /**
     * Get the severities for the audit log
     *
     * @return severities
     */
    public List<OMRSAuditLogReportSeverity> getSeverities() {
        return severities;
    }

    /**
     * Set the severities for the audit log
     *
     * @param severities severities.
     */
    public void setSeverities(List<OMRSAuditLogReportSeverity> severities) {
        this.severities = severities;
    }

    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ServerAuthorResponse{");
        sb.append("severities= [");
        for (OMRSAuditLogReportSeverity omrsAuditLogReportSeverity : severities) {
            sb.append(omrsAuditLogReportSeverity).append(",");
        }
        sb.append("]");
        sb.append(", exceptionClassName=").append(getExceptionClassName());
        sb.append(", exceptionCausedBy=").append(getExceptionCausedBy());
        sb.append(", actionDescription=").append(getActionDescription());
        sb.append(", relatedHTTPCode=").append(getRelatedHTTPCode());
        sb.append(", exceptionErrorMessage=").append(getExceptionErrorMessage());
        sb.append(", exceptionErrorMessageId=").append(getExceptionErrorMessageId());
        sb.append(", exceptionErrorMessageParameters=").append(Arrays.toString(getExceptionErrorMessageParameters()));
        sb.append(", exceptionSystemAction=").append(getExceptionSystemAction());
        sb.append(", exceptionUserAction=").append(getExceptionUserAction());
        sb.append(", exceptionProperties=").append(getExceptionProperties());
        sb.append('}');
        return sb.toString();
    }
}
