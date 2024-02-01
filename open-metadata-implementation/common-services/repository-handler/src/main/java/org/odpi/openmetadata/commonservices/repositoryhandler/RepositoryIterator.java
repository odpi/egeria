/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

import java.util.Date;

/**
 * RepositoryIterator is the shared interface of all repository helper iterators that retrieve entity or relationship
 * details from the repository.
 */
public class RepositoryIterator
{
    /**
     * Page size used when retrieving metadataion
     */
    public static final int MAX_PAGE_SIZE = 500;

    protected RepositoryHandler       repositoryHandler;
    protected InvalidParameterHandler invalidParameterHandler;
    protected String                  userId;
    protected int                     startingFrom;
    protected int                     pageSize;
    protected String                  methodName;
    protected boolean                 forLineage;
    protected boolean                 forDuplicateProcessing;
    protected Date                    effectiveTime;

    /**
     * Constructor takes the parameters used to call the repository handler.
     * @param repositoryHandler interface to the open metadata repositories.
     * @param invalidParameterHandler invalid parameter handler
     * @param userId  user making the request
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName  name of calling method
     * @throws InvalidParameterException when page size or start from parameters do not meet criteria
     */
    public RepositoryIterator(RepositoryHandler       repositoryHandler,
                              InvalidParameterHandler invalidParameterHandler,
                              String                  userId,
                              int                     startingFrom,
                              int                     pageSize,
                              boolean                 forLineage,
                              boolean                 forDuplicateProcessing,
                              Date                    effectiveTime,
                              String                  methodName) throws InvalidParameterException
    {
        this.repositoryHandler = repositoryHandler;
        this.invalidParameterHandler = invalidParameterHandler;
        this.userId = userId;
        this.startingFrom = startingFrom;
        this.methodName = methodName;
        this.forLineage = forLineage;
        this.forDuplicateProcessing = forDuplicateProcessing;
        this.effectiveTime = effectiveTime;
        this.pageSize = invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);

        if (this.pageSize == 0)
        {
            this.pageSize = MAX_PAGE_SIZE;
        }
    }
}
