/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.HashMap;
import java.util.Map;

/**
 * QueryAccumulatorBase acts as a base class to the accumulators that need to visit each repository and combine results.
 */
public class QueryAccumulatorBase extends ExceptionAccumulatorBase
{
    protected String                  localMetadataCollectionId;
    protected OMRSRepositoryValidator repositoryValidator;

    private int                        responsesRequired  = 0;
    private final Map<String, Integer> resultsContributed = new HashMap<>();
    private boolean                    resultsReturned = false;


    /**
     * Construct a query accumulator.  This base class manages the common variables needed to
     * control the execution of requests across all members of the cohort(s).
     *
     * @param localMetadataCollectionId collection Id of local repository - null means no local repository
     * @param auditLog audit log provides destination for log messages
     * @param repositoryValidator validator provides common validation routines
     */
    QueryAccumulatorBase(String                  localMetadataCollectionId,
                         AuditLog auditLog,
                         OMRSRepositoryValidator repositoryValidator)
    {
        super(auditLog);

        this.localMetadataCollectionId = localMetadataCollectionId;
        this.repositoryValidator = repositoryValidator;

        this.registerExecutor();
    }


    /**
     * Return whether results from all repositories have been received.
     *
     * @return boolean flag - true means results were returned by at least one repositories
     */
    public synchronized boolean areResultsComplete()
    {
        return (responsesRequired == resultsContributed.size());
    }


    /**
     * Return a flag indicating whether at least one of the repositories produced a valid answer.
     *
     * @return true if a request succeeded.
     */
    public boolean resultsReturned()
    {
        return (resultsReturned);
    }


    /**
     * An executor is created for each repository that needs to be called.  Each executor should provide either an
     * exception response or a set of the desired elements.
     */
    public synchronized void registerExecutor()
    {
        responsesRequired++;
    }


    /**
     * This records the completion of the request to a single repository.
     *
     * @param metadataCollectionId identifier of repository's metadata collection
     * @param numberOfElements number of elements (zero could mean nothing was found or an exception was returned.
     *                         Exceptions are recorded by the superclass.
     */
    synchronized void setResultsReturned(String      metadataCollectionId,
                                         int         numberOfElements)
    {
        setRequestReturned(metadataCollectionId, numberOfElements);
        resultsReturned = true;
    }


    /**
     * This records the completion of the request to a single repository.
     *
     * @param metadataCollectionId identifier of repository's metadata collection
     * @param numberOfElements number of elements (zero could mean nothing was found or an exception was returned.
     *                         Exceptions are recorded by the superclass.
     */
    private void setRequestReturned(String      metadataCollectionId,
                                    int         numberOfElements)
    {
        resultsContributed.put(metadataCollectionId, numberOfElements);
    }



    /**
     * Save the supplied exception.
     *
     * @param metadataCollectionId unique identifier for metadata collection this issued this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureException(String                       metadataCollectionId,
                                              ClassificationErrorException exception)
    {
        setRequestReturned(metadataCollectionId, 0);
        classificationErrorException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param metadataCollectionId unique identifier for metadata collection this issued this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureException(String                  metadataCollectionId,
                                              EntityNotKnownException exception)
    {
        setRequestReturned(metadataCollectionId, 0);
        entityNotKnownException = exception;
    }



    /**
     * Save the supplied exception.
     *
     * @param metadataCollectionId unique identifier for metadata collection this issued this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureException(String                        metadataCollectionId,
                                              FunctionNotSupportedException exception)
    {
        setRequestReturned(metadataCollectionId, 0);
        functionNotSupportedException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param metadataCollectionId unique identifier for metadata collection this issued this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureException(String                    metadataCollectionId,
                                              InvalidParameterException exception)
    {
        setRequestReturned(metadataCollectionId, 0);
        invalidParameterException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param metadataCollectionId unique identifier for metadata collection this issued this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureException(String               metadataCollectionId,
                                              PagingErrorException exception)
    {
        setRequestReturned(metadataCollectionId, 0);
        pagingErrorException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param metadataCollectionId unique identifier for metadata collection this issued this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureException(String                 metadataCollectionId,
                                              PropertyErrorException exception)
    {
        setRequestReturned(metadataCollectionId, 0);
        propertyErrorException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param metadataCollectionId unique identifier for metadata collection this issued this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureException(String                        metadataCollectionId,
                                              RelationshipNotKnownException exception)
    {
        setRequestReturned(metadataCollectionId, 0);
        relationshipNotKnownException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param metadataCollectionId unique identifier for metadata collection this issued this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureException(String                   metadataCollectionId,
                                              RepositoryErrorException exception)
    {
        setRequestReturned(metadataCollectionId, 0);
        repositoryErrorException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param metadataCollectionId unique identifier for metadata collection this issued this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureException(String             metadataCollectionId,
                                              TypeErrorException exception)
    {
        setRequestReturned(metadataCollectionId, 0);
        typeErrorException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param metadataCollectionId unique identifier for metadata collection this issued this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureException(String                     metadataCollectionId,
                                              UserNotAuthorizedException exception)
    {
        setRequestReturned(metadataCollectionId, 0);
        userNotAuthorizedException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param actionDescription caller,s activity description
     * @param metadataCollectionId unique identifier for metadata collection this issued this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureGenericException(String     actionDescription,
                                                     String     metadataCollectionId,
                                                     Exception  exception)
    {
        setRequestReturned(metadataCollectionId, 0);
        anotherException = exception;

        auditLog.logException(actionDescription,
                              OMRSAuditCode.UNEXPECTED_EXCEPTION_FROM_REPOSITORY.getMessageDefinition(exception.getClass().getName(),
                                                                                                      metadataCollectionId,
                                                                                                      actionDescription,
                                                                                                      exception.getMessage()),
                              exception);
    }
}
