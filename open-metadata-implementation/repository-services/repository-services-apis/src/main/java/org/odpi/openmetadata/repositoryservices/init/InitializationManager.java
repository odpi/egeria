/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.init;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.properties.OMRSConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to initialize a component with background retries
 * if the initialization fails
 */
public class InitializationManager {
    
    private static final Logger log = LoggerFactory.getLogger(InitializationManager.class);
    
    private final int                   maxInitAttempts;
    private final long                  initAttemptRetryIntervalMs;
    
    private final AtomicInteger         nAttempts = new AtomicInteger(0);
    private final InitializationMethod  initializationMethod;
    
    private final AuditLog              auditLog;
    /**
     * Constructor
     * 
     * @param maxInitAttempts
     * @param initAttemptRetryIntervalMs
     * @param function
     */
    public InitializationManager(AuditLog auditLog, int maxInitAttempts, long initAttemptRetryIntervalMs, InitializationMethod function) {
        this.maxInitAttempts = maxInitAttempts;
        this.initAttemptRetryIntervalMs = initAttemptRetryIntervalMs;
        this.initializationMethod = function;
        this.auditLog = auditLog;
    }
    
    private final InitializationRetryTask retryTask = new InitializationRetryTask();

    /**
     * Task to retry the initialization.  This task gets scheduled in the event
     * of an initialization retry failure.
     * 
     */
    class InitializationRetryTask implements Runnable
    {
      

        @Override
        public void run()
        {
            start();
        }
    }
    
    /**
     * Begins the initialization.  An initialization attempt is made.
     * If that fails in a non-permanent way and the maximum number of retries has not been
     * exhausted, an additional initialization attempt is scheduled to be run in 
     * a background thread.
     * 
     */
    public void start()
    {

        int attemptNumber = nAttempts.incrementAndGet();
        if (attemptNumber > 1)
        {
            log.info("Retrying initialization of " + initializationMethod.getObjectName() + " (try # " + attemptNumber + " of "
                    + maxInitAttempts + ")");
        }
        InitializationResult initResult = initializationMethod.attemptInitialization();
        if (!initResult.isSuccess())
        {
            // Make an attempt at filtering out errors such as missing SSL certificates
            // where we know what retries will not help.
            boolean retryable = initializationMethod.isRetryNeeded(initResult);
             
            if (!retryable)
            {
                logInAuditLog(
                       OMRSAuditCode.INITIALIZATION_FAILURE,
                       initResult.getInitializationError(),
                       initializationMethod.getObjectName(),
                       initResult.getInitializationError().toString());
                
                log.error(
                        "Initialization of " + initializationMethod.getObjectName() + " has failed in a way that cannot eventually fix itself.  Skipping retries.",
                        initResult.getInitializationError());
                return;
            }
            
            // Initialization attempt failed. Schedule another attempt if needed
            if (maxInitAttempts < 0 || attemptNumber < maxInitAttempts)
            {
                String maxAttemptsStr = maxInitAttempts < 0 ? "unlimited" : String.valueOf(maxInitAttempts);
                logInAuditLog(OMRSAuditCode.INITIALIZATION_RETRY, 
                        initResult.getInitializationError(),
                        initializationMethod.getObjectName(),
                        initResult.getInitializationError().toString(),
                        String.valueOf(attemptNumber),
                        maxAttemptsStr);
                
                log.warn("Initialization of " + initializationMethod.getObjectName() + " failed after try " + attemptNumber + " of " + maxAttemptsStr + ".  Scheduling retry.",
                        initResult.getInitializationError());
                
                getRetryThreadPool().schedule(retryTask, initAttemptRetryIntervalMs, TimeUnit.MILLISECONDS);
            }
            else
            {
                logInAuditLog(OMRSAuditCode.INITIALIZATION_FAILURE, 
                        initResult.getInitializationError(),
                        initializationMethod.getObjectName(),
                        initResult.getInitializationError().toString());
                
                log.error("Initialization of " + initializationMethod.getObjectName() + " has failed and all retries have been exhausted.",
                        initResult.getInitializationError());
            }
        } 
        else
        {
            logInAuditLog(OMRSAuditCode.INITIALIZATION_SUCCESS, initializationMethod.getObjectName());
            log.info("Initialization of " + initializationMethod.getObjectName() + " succeeded.");
        }
    }
    
    private void logInAuditLog(OMRSAuditCode code, String... params)
    {

        auditLog.logMessage(initializationMethod.getObjectName() + " initialization",
                code.getMessageDefinition(params));
    }
    
    private void logInAuditLog(OMRSAuditCode auditCode, Throwable error, String... params)
    {
        auditLog.logException(initializationMethod.getObjectName() + " initialization",
                auditCode.getMessageDefinition(params),
                error);
    }
    
    // retry thread pool, shared by all cohort managers
    private static volatile ScheduledExecutorService RETRY_THREAD_POOL;
    
    private static ScheduledExecutorService getRetryThreadPool()
    {

        if (RETRY_THREAD_POOL == null)
        {
            synchronized (InitializationManager.class)
            {
                if (RETRY_THREAD_POOL == null)
                {
                    // initialize thread pool on demand to give applications time
                    // to configure the size of the thread pool before it gets created
                    RETRY_THREAD_POOL = Executors.newScheduledThreadPool(
                            OMRSConfiguration.getInstance().getNumberOfInitRetryThreads(),
                            RetryThreadFactory.INSTANCE);
                }
            }
        }
        return RETRY_THREAD_POOL;
    }
    
   
   
    

}