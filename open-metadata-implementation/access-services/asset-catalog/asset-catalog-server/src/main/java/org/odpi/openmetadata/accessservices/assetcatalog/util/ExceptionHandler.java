/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetcatalog.util;

import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogException;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetCatalogOMASAPIResponse;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSCheckedExceptionBase;

public class ExceptionHandler {

    public void captureOMRSCheckedExceptionBase(AssetCatalogOMASAPIResponse response, OMRSCheckedExceptionBase e) {
        response.setRelatedHTTPCode(e.getReportedHTTPCode());
        response.setExceptionClassName(e.getClass().getName());
        response.setExceptionErrorMessage(e.getErrorMessage());
        response.setExceptionSystemAction(e.getReportedSystemAction());
        response.setExceptionUserAction(e.getReportedUserAction());
    }

    public void captureAssetCatalogExeption(AssetCatalogOMASAPIResponse response, AssetCatalogException e) {
        response.setRelatedHTTPCode(e.getReportedHTTPCode());
        response.setExceptionClassName(e.getClass().getName());
        response.setExceptionErrorMessage(e.getErrorMessage());
        response.setExceptionSystemAction(e.getReportedSystemAction());
        response.setExceptionUserAction(e.getReportedUserAction());
    }

}
