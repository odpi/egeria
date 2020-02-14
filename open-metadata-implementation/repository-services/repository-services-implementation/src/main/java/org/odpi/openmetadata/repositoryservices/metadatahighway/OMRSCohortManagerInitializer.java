/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.metadatahighway;

import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.OMRSCohortRegistryStore;
import org.odpi.openmetadata.repositoryservices.enterprise.connectormanager.OMRSConnectionConsumer;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventExchangeRule;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;
import org.odpi.openmetadata.repositoryservices.localrepository.OMRSLocalRepository;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentManager;
import org.odpi.openmetadata.repositoryservices.properties.CohortConnectionStatus;
import org.odpi.openmetadata.repositoryservices.properties.OMRSConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the initialization process for a cohort, including scheduling and managing
 * retries if the connection initially fails.
 */
public class OMRSCohortManagerInitializer
{

    private static final Logger log = LoggerFactory.getLogger(OMRSCohortManagerInitializer.class);

    private final OMRSCohortManager               cohortManager;
    private final String                          cohortName;
    private final String                          localMetadataCollectionId;
    private final String                          localMetadataCollectionName;
    private final String                          localServerName;
    private final String                          localServerType;
    private final String                          localOrganizationName;
    private final OMRSLocalRepository             localRepository;
    private final OMRSRepositoryContentManager    localRepositoryContentManager;
    private final OMRSConnectionConsumer          connectionConsumer;
    private final OMRSTopicConnector              enterpriseTopicConnector;
    private final OMRSCohortRegistryStore         cohortRegistryStore;
    private final Connection                      cohortTopicConnection;
    private final OMRSTopicConnector              cohortTopicConnector;
    private final OMRSRepositoryEventExchangeRule inboundEventExchangeRule;
    private final int                             maxInitAttempts;
    private final long                            initAttemptRetryIntervalMs;

    private final AtomicInteger                   nAttempts = new AtomicInteger(0);

    
    private final InitializationRetryTask retryTask = new InitializationRetryTask();


    // retry thread pool, shared by all cohort managers
    private static volatile ScheduledExecutorService RETRY_THREAD_POOL;
    
    // List of exceptions that, when in the cause list, prevent an initialization
    // retry
    private static final List<Class<? extends Throwable>> NON_RETRYABLE_EXCEPTIONS = buildImmutableList(
            // file system exceptions, such as java.nio.file.NoSuchFileException
            // should not trigger a retry. NoSuchFileException is thrown
            // if the Kafka SSL certificate file is not found.
            FileSystemException.class,

            // OMRSLogicErrorException indicates that there is a bug somewhere
            // in the code. We should not retry if that happens.
            OMRSLogicErrorException.class);
    
    /**
     * Constructor
     * 
     * @param cohortManager
     * @param cohortName
     * @param localMetadataCollectionId
     * @param localMetadataCollectionName
     * @param localServerName
     * @param localServerType
     * @param localOrganizationName
     * @param localRepository
     * @param localRepositoryContentManager
     * @param connectionConsumer
     * @param enterpriseTopicConnector
     * @param cohortRegistryStore
     * @param cohortTopicConnection
     * @param cohortTopicConnector
     * @param inboundEventExchangeRule
     * @param maxInitAttempts
     * @param minInitAttemptRetryIntervalMs
     */
    public OMRSCohortManagerInitializer(OMRSCohortManager cohortManager,
            String cohortName,
            String localMetadataCollectionId,
            String localMetadataCollectionName,
            String localServerName,
            String localServerType,
            String localOrganizationName,
            OMRSLocalRepository localRepository,
            OMRSRepositoryContentManager localRepositoryContentManager,
            OMRSConnectionConsumer connectionConsumer,
            OMRSTopicConnector enterpriseTopicConnector,
            OMRSCohortRegistryStore cohortRegistryStore,
            Connection cohortTopicConnection,
            OMRSTopicConnector cohortTopicConnector,
            OMRSRepositoryEventExchangeRule inboundEventExchangeRule,
            int maxInitAttempts,
            long minInitAttemptRetryIntervalMs)
    {

        super();
        this.cohortManager = cohortManager;
        this.cohortName = cohortName;
        this.localMetadataCollectionId = localMetadataCollectionId;
        this.localMetadataCollectionName = localMetadataCollectionName;
        this.localServerName = localServerName;
        this.localServerType = localServerType;
        this.localOrganizationName = localOrganizationName;
        this.localRepository = localRepository;
        this.localRepositoryContentManager = localRepositoryContentManager;
        this.connectionConsumer = connectionConsumer;
        this.enterpriseTopicConnector = enterpriseTopicConnector;
        this.cohortRegistryStore = cohortRegistryStore;
        this.cohortTopicConnection = cohortTopicConnection;
        this.cohortTopicConnector = cohortTopicConnector;
        this.inboundEventExchangeRule = inboundEventExchangeRule;
        this.maxInitAttempts = maxInitAttempts;
        this.initAttemptRetryIntervalMs = minInitAttemptRetryIntervalMs;
    }

    /**
     * Begins the cohort initialization.  An initialization attempt is made.
     * If that fails in a non-permanent way and the maximum number of retries has not been
     * exhausted, an additional initialization attempt is scheduled to be run in 
     * a background thread.
     * 
     * @see OMRSConfiguration
     * 
     */
    public void start()
    {

        int attemptNumber = nAttempts.incrementAndGet();
        if (attemptNumber > 1)
        {
            log.info("Retrying initialization of cohort " + cohortName + " (try # " + attemptNumber + " of "
                    + maxInitAttempts + ")");
        }
        CohortInitializationResult initResult = cohortManager.initialize(cohortName,
                localMetadataCollectionId,
                localMetadataCollectionName,
                localServerName,
                localServerType,
                localOrganizationName,
                localRepository,
                localRepositoryContentManager,
                connectionConsumer,
                enterpriseTopicConnector,
                cohortRegistryStore,
                cohortTopicConnection,
                cohortTopicConnector,
                inboundEventExchangeRule);

        if (!initResult.isSuccess())
        {
            // Make an attempt at filtering out errors such as missing SSL certificates
            // where we know what retries will not help.
            boolean retryable = isRetryable(initResult);

            if (!retryable)
            {
                log.error(
                        "Initialization of cohort " + cohortName
                                + " has failed in a way that cannot eventually fix itself.  Skipping retries.",
                        initResult.getInitializationError());
                return;
            }
            // Initialization attempt failed. Schedule another attempt if needed
            OMRSConfiguration config = OMRSConfiguration.getInstance();
            if (maxInitAttempts < 0 || attemptNumber < maxInitAttempts)
            {
                cohortManager.setStatus(CohortConnectionStatus.AWAITING_RETRY);
                log.warn("Initialization of cohort " + cohortName + " failed.  Scheduling retry.",
                        initResult.getInitializationError());
                getRetryThreadPool().schedule(retryTask, initAttemptRetryIntervalMs, TimeUnit.MILLISECONDS);
            } else
            {
                log.error("Initialization of cohort " + cohortName + " has failed and all retries have been exhausted.",
                        initResult.getInitializationError());
            }
        } else
        {
            log.info("Initialization of cohort " + cohortName + " succeeded.");
        }
    }

    /**
     * Returns true if the cohort initialization failed in a way that
     * is retryable
     * 
     * @param result
     * @return
     */
    private static boolean isRetryable(CohortInitializationResult result)
    {

        if (result.isSuccess())
        {
            return false;
        }
        for (Class<? extends Throwable> exceptionClass : NON_RETRYABLE_EXCEPTIONS)
        {
            if (result.hasError(exceptionClass))
            {
                return false;
            }
        }
        return true;
    }
    
    private static ScheduledExecutorService getRetryThreadPool()
    {

        if (RETRY_THREAD_POOL == null)
        {
            synchronized (OMRSCohortManagerInitializer.class)
            {
                if (RETRY_THREAD_POOL == null)
                {
                    // initialize thread pool on demand to give applications time
                    // to configure the size of the thread pool before it gets created
                    RETRY_THREAD_POOL = Executors.newScheduledThreadPool(
                            OMRSConfiguration.getInstance().getNumberOfCohortInitRetryThreads(),
                            RetryThreadFactory.INSTANCE);
                }
            }
        }
        return RETRY_THREAD_POOL;
    }

    private static List<Class<? extends Throwable>> buildImmutableList(Class<? extends Throwable>... items)
    {

        List<Class<? extends Throwable>> result = new ArrayList<>();
        for (Class<? extends Throwable> item : items)
        {
            result.add(item);
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Task to retry the cohort initialization.  This task gets scheduled in the event
     * of an initialization retry failure.
     * 
     */
    private class InitializationRetryTask implements Runnable
    {

        @Override
        public void run()
        {

            start();
        }

    }
}
