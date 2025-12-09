/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

/**
 * MaintenanceAccumulator provides a simple accumulator that supports maintenance operations on the
 * metadata repositories.
 */
public class MaintenanceAccumulator extends ExceptionAccumulatorBase
{
    /**
     * Constructor
     *
     * @param auditLog logging destination
     */
    public MaintenanceAccumulator(AuditLog auditLog)
    {
        super(auditLog);
    }


    /**
     * Save the supplied exception.
     *
     * @param exception  exception from remote call
     */
    public synchronized void captureException(ClassificationErrorException exception)
    {
        classificationErrorException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param exception  exception from remote call
     */
    public synchronized void captureException(EntityNotDeletedException exception)
    {
        entityNotDeletedException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param exception  exception from remote call
     */
    public synchronized void captureException(EntityNotKnownException exception)
    {
        entityNotKnownException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param exception  exception from remote call
     */
    public synchronized void captureException(EntityProxyOnlyException exception)
    {
        entityProxyOnlyException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param exception  exception from remote call
     */
    public synchronized void captureException(FunctionNotSupportedException exception)
    {
        functionNotSupportedException = exception;
    }


     /**
     * Save the supplied exception.
     *
     * @param exception  exception from remote call
     */
    public synchronized void captureException(InvalidParameterException exception)
    {
        invalidParameterException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param exception  exception from remote call
     */
    public synchronized void captureException(InvalidTypeDefException exception)
    {
        invalidTypeDefException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param exception  exception from remote call
     */
    public synchronized void captureException(PropertyErrorException exception)
    {
        propertyErrorException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param exception  exception from remote call
     */
    public synchronized void captureException(RelationshipNotDeletedException exception)
    {
        relationshipNotDeletedException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param exception  exception from remote call
     */
    public synchronized void captureException(RelationshipNotKnownException exception)
    {
        relationshipNotKnownException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param exception  exception from remote call
     */
    public synchronized void captureException(RepositoryErrorException exception)
    {
        repositoryErrorException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param exception  exception from remote call
     */
    public synchronized void captureException(StatusNotSupportedException exception)
    {
        statusNotSupportedException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param actionDescription description of caller's activity eg methodName
     * @param metadataCollectionId metadata collection id of repository returning this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureGenericException(String     actionDescription,
                                                     String     metadataCollectionId,
                                                     Exception  exception)
    {
        anotherException = exception;

        auditLog.logException(actionDescription,
                              OMRSAuditCode.UNEXPECTED_EXCEPTION_FROM_REPOSITORY.getMessageDefinition(exception.getClass().getName(),
                                                                                                      metadataCollectionId,
                                                                                                      actionDescription,
                                                                                                      exception.getMessage()),
                              exception);
    }


    /**
     * Save the supplied exception.
     *
     * @param exception  exception from remote call
     */
    public void captureException(TypeDefConflictException exception)
    {
        typeDefConflictException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param exception  exception from remote call
     */
    public void captureException(TypeDefNotSupportedException exception)
    {
        typeDefNotSupportedException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param exception  exception from remote call
     */
    public void captureException(TypeDefNotKnownException exception)
    {
        typeDefNotKnownException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param exception  exception from remote call
     */
    public void captureException(TypeErrorException exception)
    {
        typeErrorException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param exception  exception from remote call
     */
    public void captureException(UserNotAuthorizedException exception)
    {
        userNotAuthorizedException = exception;
    }

}
