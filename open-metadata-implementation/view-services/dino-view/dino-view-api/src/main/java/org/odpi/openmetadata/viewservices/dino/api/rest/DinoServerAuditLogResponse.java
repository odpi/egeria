/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.rest;




import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogReport;

import java.util.Arrays;
import java.util.List;

public class DinoServerAuditLogResponse extends DinoViewOMVSAPIResponse {


    private OMRSAuditLogReport auditLog = null;

    /**
     * Default constructor
     */
    public DinoServerAuditLogResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DinoServerAuditLogResponse(DinoServerAuditLogResponse template)
    {
        super(template);

        if (template != null)
        {
            this.auditLog = template.getAuditLog();
        }
    }


    /**
     * Return the server audit log
     *
     * @return bean
     */
    public OMRSAuditLogReport getAuditLog()
    {
        return auditLog;
    }


    /**
     * Set the serverList.
     *
     * @param auditLog - bean
     */
    public void setAuditLog(OMRSAuditLogReport auditLog)
    {
        this.auditLog = auditLog;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DinoServerAuditLogResponse{" +
                "auditLog=" + auditLog +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }





}
