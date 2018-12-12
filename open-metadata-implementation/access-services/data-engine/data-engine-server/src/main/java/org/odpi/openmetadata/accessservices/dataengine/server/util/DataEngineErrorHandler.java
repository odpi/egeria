package org.odpi.openmetadata.accessservices.dataengine.server.util;

import org.odpi.openmetadata.accessservice.assetcatalog.exception.AssetCatalogException;
import org.odpi.openmetadata.accessservice.assetcatalog.responses.AssetCatalogOMASAPIResponse;
import org.odpi.openmetadata.accessservices.dataengine.exception.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.exception.NewInstanceException;
import org.odpi.openmetadata.accessservices.dataengine.exception.PropertyServerException;
import org.odpi.openmetadata.accessservices.dataengine.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSCheckedExceptionBase;

public class DataEngineErrorHandler {

    public void handleNewInstanceException(DataEngineErrorCode errorCode,
                                           String methodName) throws NewInstanceException {
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

        throw new NewInstanceException(errorCode.getHttpErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    public void handlePropertyServerException(DataEngineErrorCode errorCode, String methodName) throws PropertyServerException {
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

        throw new PropertyServerException(errorCode.getHttpErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

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

    /**
     * Throw an exception if the supplied userId is null
     *
     * @param userId  user name to validate
     * @param methodName  name of the method making the call.
     * @throws UserNotAuthorizedException the userId is null
     */
    public void validateUserId(String userId,
                               String methodName) throws UserNotAuthorizedException {
        if (userId == null)
        {
            DataEngineErrorCode errorCode = DataEngineErrorCode.NULL_USER_ID;
            String                     errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new UserNotAuthorizedException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

    }
}
