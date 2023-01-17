/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;

public abstract class RepositoryExecutorBase implements RepositoryExecutor
{
    final String repositoryName = "Enterprise";

    protected String methodName;
    protected String userId;

    /**
     * Simple constructor - all methods have the userId in them
     *
     * @param userId calling user
     */
    RepositoryExecutorBase(String   userId,
                           String   methodName)
    {
        this.userId = userId;
        this.methodName = methodName;
    }


    /**
     * Return the method name for the executor.
     *
     * @return calling method name
     */
    public String getMethodName()
    {
        return methodName;
    }


    /**
     * Perform the required action for the supplied repository.
     *
     * @param metadataCollectionId unique identifier for the metadata collection for the repository
     * @param metadataCollection metadata collection object for the repository
     * @return boolean true means that the required results have been achieved
     */
    public abstract boolean issueRequestToRepository(String                 metadataCollectionId,
                                                     OMRSMetadataCollection metadataCollection);
}
