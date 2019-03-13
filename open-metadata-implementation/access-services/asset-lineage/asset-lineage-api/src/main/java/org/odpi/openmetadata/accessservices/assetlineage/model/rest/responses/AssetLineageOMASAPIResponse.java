/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetlineage.model.rest.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetLineageOMASAPIResponse provides a common header for Asset Lineage OMAS managed responses to its REST API.
 * It manages information about exceptions.  If no exception has been raised exceptionClassName is null.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetLineageOMASAPIResponse {

    protected AssetLineageOMASAPIResponse() {

    }

    /**
     * the HTTP Code to use if forwarding response to HTTP client.
     */
    protected int relatedHTTPCode = 200;

    /**
     * the fully-qualified Java class name to use to recreate the exception
     */
    protected String exceptionClassName;

    /**
     * the error message associated with the exception.
     */
    protected String exceptionErrorMessage;

    /**
     * the description of the action taken by the system as a result of the exception.
     */
    protected String exceptionSystemAction;

    /**
     * the action that a user should take to resolve the problem.
     */
    protected String exceptionUserAction;

    public int getRelatedHTTPCode() {
        return relatedHTTPCode;
    }

    public void setRelatedHTTPCode(int relatedHTTPCode) {
        this.relatedHTTPCode = relatedHTTPCode;
    }

    public String getExceptionClassName() {
        return exceptionClassName;
    }

    public void setExceptionClassName(String exceptionClassName) {
        this.exceptionClassName = exceptionClassName;
    }

    public String getExceptionErrorMessage() {
        return exceptionErrorMessage;
    }

    public void setExceptionErrorMessage(String exceptionErrorMessage) {
        this.exceptionErrorMessage = exceptionErrorMessage;
    }

    public String getExceptionSystemAction() {
        return exceptionSystemAction;
    }

    public void setExceptionSystemAction(String exceptionSystemAction) {
        this.exceptionSystemAction = exceptionSystemAction;
    }

    public String getExceptionUserAction() {
        return exceptionUserAction;
    }

    public void setExceptionUserAction(String exceptionUserAction) {
        this.exceptionUserAction = exceptionUserAction;
    }
}