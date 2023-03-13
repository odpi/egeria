/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;

/**
 * CloneableRepositoryExecutor describes the interface for a repository executor that can be cloned to run
 * in parallel.  They use a shared accumulator to gather and assemble the result.
 */
public interface CloneableRepositoryExecutor
{
    /**
     * Return a clone of this executor with the same command parameters and accumulator instance.
     *
     * @return clone of this executor
     */
    CloneableRepositoryExecutor getClone();
}
