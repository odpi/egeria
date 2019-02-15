package org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime;

public class InformationViewUncheckedExceptionBase extends RuntimeException{

    private String reportingClassName;
    private String reportingActionDescription;
    private String reportedErrorMessage;
    private String reportedSystemAction;
    private String reportedUserAction;
    private Throwable reportedCaughtException = null;

    public InformationViewUncheckedExceptionBase(
            String className,
            String actionDescription,
            String errorMessage,
            String systemAction,
            String userAction,
            Throwable caughtError) {
        super(errorMessage, caughtError);
        this.reportingClassName = className;
        this.reportingActionDescription = actionDescription;
        this.reportedErrorMessage = errorMessage;
        this.reportedSystemAction = systemAction;
        this.reportedUserAction = userAction;
        this.reportedCaughtException = caughtError;
    }



}
