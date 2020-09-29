/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.services;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.viewservices.glossaryauthor.auditlog.GlossaryAuthorViewAuditCode;
import org.odpi.openmetadata.viewservices.glossaryauthor.initialization.GlossaryAuthorViewInstanceHandler;
import org.slf4j.LoggerFactory;

/**
 * An abstract base class providing common methods for glossary authoring services.
 */
abstract public class BaseGlossaryAuthorView {
    public static final String PAGE_OFFSET_DEFAULT_VALUE = "0";
    public static final String PAGE_SIZE_DEFAULT_VALUE = "0";

    protected static GlossaryAuthorViewInstanceHandler instanceHandler = new GlossaryAuthorViewInstanceHandler();
    protected static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(GlossaryAuthorViewGlossaryRESTServices.class),
            instanceHandler.getServiceName());

    /**
     * Get the appropriate response from the supplied Exception
     *
     * @param error      - supplied exception
     * @param auditLog   - auditlog (may be null if unable to initialize)
     * @param className  - calling class's Name
     * @param methodName - calling method's name
     * @return response corresponding to the error.
     */
    protected <T> SubjectAreaOMASAPIResponse<T> getResponseForError(Throwable error, AuditLog auditLog, String className, String methodName) {
        SubjectAreaOMASAPIResponse<T> response = new SubjectAreaOMASAPIResponse<>();
        if (error instanceof OCFCheckedExceptionBase) {
            response.setExceptionInfo((OCFCheckedExceptionBase) error, className);
        } else {
            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition();
            messageDefinition.setMessageParameters(error.getMessage());
            SubjectAreaCheckedException checkedException = new SubjectAreaCheckedException(messageDefinition, className, methodName, error);
            response.setExceptionInfo(checkedException, className);
            if (auditLog != null) {
                auditLog.logException(methodName,
                        GlossaryAuthorViewAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(), methodName, error.getMessage()),
                        error);
            }
        }
        return response;
    }
}