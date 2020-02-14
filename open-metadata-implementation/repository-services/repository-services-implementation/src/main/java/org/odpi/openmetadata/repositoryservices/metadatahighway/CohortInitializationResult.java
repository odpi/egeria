/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.metadatahighway;

/**
 * The result of initializing a cohort
 * 
 * @see OMRSCohortManagerInitializer#start()
 * @see OMRSCohortManager#initialize(String, String, String, String, String, String, org.odpi.openmetadata.repositoryservices.localrepository.OMRSLocalRepository, org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentManager, org.odpi.openmetadata.repositoryservices.enterprise.connectormanager.OMRSConnectionConsumer, org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector, org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.OMRSCohortRegistryStore, org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection, org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector, org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventExchangeRule)
 */
public class CohortInitializationResult
{

    public static final CohortInitializationResult SUCCESS = new CohortInitializationResult();

    private final Throwable initError;

    /**
     * Constructor
     * 
     * @param error
     */
    public CohortInitializationResult(Throwable error)
    {

        this.initError = error;
    }

    private CohortInitializationResult()
    {

        initError = null;
    }

    /**
     * Returns true if the initialization was successful
     * @return
     */
    public boolean isSuccess()
    {

        return initError == null;
    }

    /**
     * Returns the error that occurred during initialization, if there was one.  Returns null otherwise.
     * 
     * @return
     */
    public Throwable getInitializationError()
    {

        return initError;
    }

    /**
     * Checks whether the initialization error, if there was one, 
     * contains the specified error
     * 
     * @param <T> the exception to check
     * @param exceptionType
     * 
     * @return
     */
    public <T extends Throwable> boolean hasError(Class<T> exceptionType)
    {

        return findCause(initError, exceptionType) != null;
    }

    /**
     * Returns the first occurrence of <code>exceptionType</code> in the initError
     * chain for the given exception. Returns null if it was not found.
     * 
     * @param <T> the exception to check
     * @param exceptionType
     * 
     * @return
     */
    private static <T extends Throwable> T findCause(Throwable t, Class<T> exceptionType)
    {

        Throwable current = t;
        while (current != null)
        {
            if (exceptionType.isAssignableFrom(current.getClass()))
            {
                return exceptionType.cast(current);
            }
            current = current.getCause();
        }
        return null;
    }
}
