package org.odpi.openmetadata.accessservices.dataengine.server.util;

import org.odpi.openmetadata.accessservice.assetcatalog.exception.AssetCatalogException;
import org.odpi.openmetadata.accessservice.assetcatalog.responses.AssetCatalogOMASAPIResponse;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSCheckedExceptionBase;

public class ExceptionHandler {
    public void captureOMRSCheckedExceptionBase(AssetCatalogOMASAPIResponse response, OMRSCheckedExceptionBase e) {
        captureException(response, e.getReportedHTTPCode(), e.getClass().getName(), e.getErrorMessage(),
                e.getReportedSystemAction(), e.getReportedUserAction(), e);
    }

    public void captureAssetCatalogExeption(AssetCatalogOMASAPIResponse response,
                                            AssetCatalogException e) {
        captureException(response, e.getReportedHTTPCode(), e.getClass().getName(), e.getErrorMessage(),
                e.getReportedSystemAction(), e.getReportedUserAction(), e);
    }

    private void captureException(AssetCatalogOMASAPIResponse response,
                                  int reportedHTTPCode,
                                  String name,
                                  String errorMessage,
                                  String reportedSystemAction,
                                  String reportedUserAction,
                                  Exception e) {
        response.setRelatedHTTPCode(reportedHTTPCode);
        response.setExceptionClassName(name);
        response.setExceptionErrorMessage(errorMessage);
        response.setExceptionSystemAction(reportedSystemAction);
        response.setExceptionUserAction(reportedUserAction);
    }
}
