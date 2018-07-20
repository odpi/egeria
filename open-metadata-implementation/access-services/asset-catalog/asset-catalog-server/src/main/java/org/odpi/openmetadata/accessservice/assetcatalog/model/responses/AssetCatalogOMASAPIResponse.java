/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.model.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetCatalogOMASAPIResponse provides a common header for Asset Catalog OMAS managed responses to its REST API.
 * It manages information about exceptions.  If no exception has been raised exceptionClassName is null.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public abstract class AssetCatalogOMASAPIResponse {

    /**
     * the HTTP Code to use if forwarding response to HTTP client.
     */
    protected int relatedHTTPCode = 200;

    /**
     * the fully-qualified Java class name to use to recreate the exception
     */
    protected String exceptionClassName = null;

    /**
     * the error message associated with the exception.
     */
    protected String exceptionErrorMessage = null;

    /**
     * the description of the action taken by the system as a result of the exception.
     */
    protected String exceptionSystemAction = null;

    /**
     * the action that a user should take to resolve the problem.
     */
    protected String exceptionUserAction = null;
}