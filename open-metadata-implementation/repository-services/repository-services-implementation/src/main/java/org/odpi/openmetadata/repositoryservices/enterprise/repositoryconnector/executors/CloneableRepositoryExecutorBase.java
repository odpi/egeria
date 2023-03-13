/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;

import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.QueryAccumulatorBase;


/**
 * CloneableRepositoryExecutorBase provides a base class for executors that can be cloned and run in
 * parallel so the call to each repository runs in a different thread.
 */
abstract class CloneableRepositoryExecutorBase extends RepositoryExecutorBase implements CloneableRepositoryExecutor
{
    CloneableRepositoryExecutorBase(String               userId,
                                    String               methodName,
                                    QueryAccumulatorBase accumulator)
    {
        super(userId, methodName);

        accumulator.registerExecutor();
    }
}
