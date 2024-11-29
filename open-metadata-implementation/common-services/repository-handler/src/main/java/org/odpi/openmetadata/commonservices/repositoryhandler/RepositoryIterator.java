/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;

import java.util.Date;
import java.util.List;

/**
 * RepositoryIterator is the shared interface of all repository helper iterators that retrieve entity or relationship
 * details from the repository.
 */
public class RepositoryIterator
{
    /**
     * Page size used when retrieving metadata.
     */
    public static final int MAX_PAGE_SIZE = 500;

    protected int                     startingFrom;
    protected int                     pageSize;

    protected final RepositoryHandler       repositoryHandler;
    protected final InvalidParameterHandler invalidParameterHandler;
    protected final String                  userId;
    protected final String                  methodName;
    protected final List<InstanceStatus>    limitResultsByStatus;
    protected final Date                    asOfTime;
    protected final SequencingOrder         sequencingOrder;
    protected final String                  sequencingPropertyName;
    protected final boolean                 forLineage;
    protected final boolean                 forDuplicateProcessing;
    protected final Date                    effectiveTime;


    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param invalidParameterHandler invalid parameter handler
     * @param userId  user making the request
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
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
                              List<InstanceStatus>    limitResultsByStatus,
                              Date                    asOfTime,
                              SequencingOrder         sequencingOrder,
                              String                  sequencingPropertyName,
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
        this.limitResultsByStatus = limitResultsByStatus;
        this.asOfTime = asOfTime;
        if (sequencingOrder == null)
        {
            this.sequencingOrder = SequencingOrder.CREATION_DATE_RECENT;
        }
        else
        {
            this.sequencingOrder = sequencingOrder;
        }
        this.sequencingPropertyName = sequencingPropertyName;
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
