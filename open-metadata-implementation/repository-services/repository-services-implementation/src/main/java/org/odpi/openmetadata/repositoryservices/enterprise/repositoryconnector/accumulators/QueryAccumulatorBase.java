/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators;


import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.HashMap;
import java.util.Map;

/**
 * QueryAccumulatorBase acts as a base class to the accumulators that need to visit each repository and
 * combine results.
 */
public class QueryAccumulatorBase extends ExceptionAccumulatorBase
{
    protected String                  localMetadataCollectionId;
    protected OMRSAuditLog            auditLog;
    protected OMRSRepositoryValidator repositoryValidator;

    private int                       resultsRequired = 0;
    private Map<String, Integer>      resultsContributed = new HashMap<>();


    /**
     * Construct a query accumulator.  This base class manages the common variables needed to
     * control the execution of requests across all members of the cohort(s).
     *
     * @param localMetadataCollectionId collection Id of local repository - null means no local repository
     * @param auditLog audit log provides destination for log messages
     * @param repositoryValidator validator provides common validation routines
     */
    QueryAccumulatorBase(String                  localMetadataCollectionId,
                         OMRSAuditLog            auditLog,
                         OMRSRepositoryValidator repositoryValidator)
    {
        this.localMetadataCollectionId = localMetadataCollectionId;
        this.auditLog = auditLog;
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
        return (resultsRequired == resultsContributed.size());
    }


    /**
     * An executor is created for each repository that needs to be called.  Each executor should provide either an
     * exception response or a set of the desired elements.
     */
    public synchronized void registerExecutor()
    {
        resultsRequired++;
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
        resultsContributed.put(metadataCollectionId, numberOfElements);
    }



    /**
     * Save the supplied exception.
     *
     * @param metadataCollectionId unique identifier for metadata collection this issued this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureException(String                          metadataCollectionId,
                                              ClassificationErrorException    exception)
    {
        setResultsReturned(metadataCollectionId, 0);
        classificationErrorException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param metadataCollectionId unique identifier for metadata collection this issued this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureException(String                     metadataCollectionId,
                                              EntityNotKnownException    exception)
    {
        setResultsReturned(metadataCollectionId, 0);
        entityNotKnownException = exception;
    }



    /**
     * Save the supplied exception.
     *
     * @param metadataCollectionId unique identifier for metadata collection this issued this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureException(String                           metadataCollectionId,
                                              FunctionNotSupportedException    exception)
    {
        setResultsReturned(metadataCollectionId, 0);
        functionNotSupportedException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param metadataCollectionId unique identifier for metadata collection this issued this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureException(String                     metadataCollectionId,
                                              InvalidParameterException  exception)
    {
        setResultsReturned(metadataCollectionId, 0);
        invalidParameterException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param metadataCollectionId unique identifier for metadata collection this issued this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureException(String                     metadataCollectionId,
                                              PagingErrorException     exception)
    {
        setResultsReturned(metadataCollectionId, 0);
        pagingErrorException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param metadataCollectionId unique identifier for metadata collection this issued this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureException(String                     metadataCollectionId,
                                              PropertyErrorException     exception)
    {
        setResultsReturned(metadataCollectionId, 0);
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
        setResultsReturned(metadataCollectionId, 0);
        relationshipNotKnownException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param metadataCollectionId unique identifier for metadata collection this issued this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureException(String                     metadataCollectionId,
                                              RepositoryErrorException   exception)
    {
        setResultsReturned(metadataCollectionId, 0);
        repositoryErrorException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param metadataCollectionId unique identifier for metadata collection this issued this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureException(String               metadataCollectionId,
                                              TypeErrorException   exception)
    {
        setResultsReturned(metadataCollectionId, 0);
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
        setResultsReturned(metadataCollectionId, 0);
        userNotAuthorizedException = exception;
    }


    /**
     * Save the supplied exception.
     *
     * @param metadataCollectionId unique identifier for metadata collection this issued this exception
     * @param exception  exception from remote call
     */
    public synchronized void captureGenericException(String     metadataCollectionId,
                                                     Throwable  exception)
    {
        setResultsReturned(metadataCollectionId, 0);
        anotherException = exception;
    }
}
