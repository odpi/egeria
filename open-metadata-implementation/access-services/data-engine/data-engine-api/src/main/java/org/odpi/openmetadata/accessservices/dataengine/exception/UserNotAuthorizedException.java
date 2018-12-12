package org.odpi.openmetadata.accessservices.dataengine.exception;

public class UserNotAuthorizedException extends DataEngineException {

    public UserNotAuthorizedException(int httpCode,
                                      String className,
                                      String actionDescription,
                                      String errorMessage,
                                      String systemAction,
                                      String userAction) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }
}
