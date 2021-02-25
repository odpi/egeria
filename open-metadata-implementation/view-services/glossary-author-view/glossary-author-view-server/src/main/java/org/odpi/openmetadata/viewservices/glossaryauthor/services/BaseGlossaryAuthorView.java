/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.services;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.viewservices.glossaryauthor.auditlog.GlossaryAuthorViewAuditCode;
import org.odpi.openmetadata.viewservices.glossaryauthor.initialization.GlossaryAuthorViewInstanceHandler;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * An abstract base class providing common methods for glossary authoring services.
 */
abstract public class BaseGlossaryAuthorView {

    protected static final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    protected static GlossaryAuthorViewInstanceHandler instanceHandler = new GlossaryAuthorViewInstanceHandler();
    protected static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(GlossaryAuthorViewGlossaryRESTServices.class),
            instanceHandler.getServiceName());

    /**
     * Get the appropriate response from the supplied Exception
     *
     * @param exception      - supplied exception
     * @param auditLog   - auditlog (may be null if unable to initialize)
     * @param className  - calling class's Name
     * @param methodName - calling method's name
     * @return response corresponding to the exception.
     */
    protected <T> SubjectAreaOMASAPIResponse<T> getResponseForException(Exception exception, AuditLog auditLog, String className, String methodName) {
        SubjectAreaOMASAPIResponse<T> response = new SubjectAreaOMASAPIResponse<>();
        if (exception instanceof OCFCheckedExceptionBase) {
            response.setExceptionInfo((OCFCheckedExceptionBase) exception, className);
        } else {
            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition();
            messageDefinition.setMessageParameters(exception.getMessage());
            SubjectAreaCheckedException checkedException = new SubjectAreaCheckedException(messageDefinition, className, methodName, exception);
            response.setExceptionInfo(checkedException, className);
            if (auditLog != null) {
                auditLog.logException(methodName,
                        GlossaryAuthorViewAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(exception.getClass().getName(), methodName, exception.getMessage()),
                        exception);
            }
        }
        return response;
    }
    protected FindRequest getFindRequest(String searchCriteria, Date asOfTime, Integer startingFrom, Integer pageSize, String sequencingOrderName, String sequencingProperty, Integer handlerMaxPageSize) {
        FindRequest findRequest = new FindRequest();
        SequencingOrder sequencingOrder = SequencingOrder.ANY;
        for (SequencingOrder possibleSequence: SequencingOrder.values()) {
            if (possibleSequence.name().equals(sequencingOrderName)) {
                sequencingOrder=possibleSequence;
            }

        }
        findRequest.setSearchCriteria(searchCriteria);
        findRequest.setAsOfTime(asOfTime);
        findRequest.setStartingFrom(startingFrom);
        if (pageSize == null){
            findRequest.setPageSize(handlerMaxPageSize);
        } else {
            findRequest.setPageSize(pageSize);
        }
        findRequest.setSequencingOrder(sequencingOrder);
        findRequest.setSequencingProperty(sequencingProperty);
        return findRequest;
    }
}