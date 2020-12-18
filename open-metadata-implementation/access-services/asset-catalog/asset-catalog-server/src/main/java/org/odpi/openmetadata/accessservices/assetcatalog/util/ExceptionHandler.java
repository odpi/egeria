/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetcatalog.util;

import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogException;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetCatalogOMASAPIResponse;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSCheckedExceptionBase;

/**
 * Exception handler used to capture OMRSCheckedExceptionBase and AssetCatalogException
 */
public class ExceptionHandler {

    /**
     * Capture the OMRSCheckedExceptionBase exception and set on the AssetCatalogOMASAPIResponse corresponding HTTP code
     *
     * @param response - AssetCatalogOMASAPIResponse response
     * @param e        - OMRSCheckedExceptionBase exception
     */
    public void captureOMRSCheckedExceptionBase(AssetCatalogOMASAPIResponse response, OMRSCheckedExceptionBase e) {
        response.setRelatedHTTPCode(e.getReportedHTTPCode());
        response.setExceptionClassName(e.getClass().getName());
        response.setExceptionErrorMessage(e.getReportedErrorMessage());
        response.setExceptionSystemAction(e.getReportedSystemAction());
        response.setExceptionUserAction(e.getReportedUserAction());
    }

    /**
     * Capture the AssetCatalogException and set on the AssetCatalogOMASAPIResponse corresponding HTTP code
     *
     * @param response AssetCatalogOMASAPIResponse
     * @param e        AssetCatalogException exception
     */
    public void captureAssetCatalogExeption(AssetCatalogOMASAPIResponse response, AssetCatalogException e) {
        response.setRelatedHTTPCode(e.getReportedHTTPCode());
        response.setExceptionClassName(e.getClass().getName());
        response.setExceptionErrorMessage(e.getReportedErrorMessage());
        response.setExceptionSystemAction(e.getReportedSystemAction());
        response.setExceptionUserAction(e.getReportedUserAction());
    }

}
