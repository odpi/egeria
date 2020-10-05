/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceWorkbenchStatus;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * WorkbenchStatusResponse defines the response structure that includes the status of a workbench.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class WorkbenchStatusResponse extends ConformanceServicesAPIResponse
{
    private static final long     serialVersionUID = 1L;

    private OpenMetadataConformanceWorkbenchStatus workbenchStatus = null;

    /**
     * Default constructor
     */
    public WorkbenchStatusResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public WorkbenchStatusResponse(WorkbenchStatusResponse template)
    {
        super(template);

        if (template != null)
        {
            workbenchStatus = template.getWorkbenchStatus();
        }
    }


    /**
     * Return the results from running the tests.
     *
     * @return results report
     */
    public OpenMetadataConformanceWorkbenchStatus getWorkbenchStatus()
    {
        return workbenchStatus;
    }


    /**
     * Set up the results from running the tests.
     *
     * @param workbenchStatus results report
     */
    public void setWorkbenchStatus(OpenMetadataConformanceWorkbenchStatus workbenchStatus)
    {
        this.workbenchStatus = workbenchStatus;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "WorkbenchStatusResponse{" +
                "workbenchStatus=" + workbenchStatus +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", successMessage='" + getSuccessMessage() + '\'' +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }
}
