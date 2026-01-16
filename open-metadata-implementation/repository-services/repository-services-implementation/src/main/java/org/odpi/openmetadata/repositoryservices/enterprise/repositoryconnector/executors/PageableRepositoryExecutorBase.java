/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.QueryInstanceAccumulatorBase;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.Date;
import java.util.List;

/**
 * CloneableRepositoryExecutorBase provides a base class for executors that can be cloned and run in
 * parallel so the call to each repository runs in a different thread.
 */
abstract class PageableRepositoryExecutorBase extends CloneableRepositoryExecutorBase
{
    private final QueryInstanceAccumulatorBase queryInstanceAccumulator;

    int  startingElement;
    int  pageSize;

    String               instanceTypeGUID;
    List<InstanceStatus> limitResultsByStatus;
    String               sequencingProperty;
    SequencingOrder      sequencingOrder;
    Date                 asOfTime;


    /**
     * Create the executor.  The parameters provide the parameters for issuing the requests and
     * combining the results.
     *
     * @param userId calling user
     * @param instanceTypeGUID unique identifier of type that all returned instances must be
     * @param startingElement page start pointer
     * @param limitResultsByStatus status that the returning instances must be in.
     * @param sequencingProperty property used for ordering (optional)
     * @param sequencingOrder ordering of results
     * @param pageSize maximum number of elements that can be returned
     * @param asOfTime historical query
     * @param accumulator location for results
     * @param methodName calling method
     */
    PageableRepositoryExecutorBase(String                       userId,
                                   String                       instanceTypeGUID,
                                   int                          startingElement,
                                   List<InstanceStatus>         limitResultsByStatus,
                                   String                       sequencingProperty,
                                   SequencingOrder              sequencingOrder,
                                   int                          pageSize,
                                   Date                         asOfTime,
                                   QueryInstanceAccumulatorBase accumulator,
                                   String                       methodName)
    {
        super(userId, methodName, accumulator);

        this.instanceTypeGUID = instanceTypeGUID;
        this.startingElement = startingElement;
        this.limitResultsByStatus = limitResultsByStatus;
        this.sequencingProperty = sequencingProperty;
        this.sequencingOrder = sequencingOrder;
        this.pageSize = pageSize;
        this.asOfTime = asOfTime;

        this.queryInstanceAccumulator = accumulator;
    }


    /**
     * Return any of the exceptions that are common to paging requests.
     *

     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    void handleCommonPagingRequestExceptions() throws InvalidParameterException,
                                                      TypeErrorException,
                                                      RepositoryErrorException,
                                                      PropertyErrorException,
                                                      PagingErrorException,
                                                      FunctionNotSupportedException,
                                                      UserNotAuthorizedException
    {
        queryInstanceAccumulator.throwCapturedUserNotAuthorizedException();
        queryInstanceAccumulator.throwCapturedRepositoryErrorException();
        queryInstanceAccumulator.throwCapturedGenericException(methodName);
        queryInstanceAccumulator.throwCapturedPropertyErrorException();
        queryInstanceAccumulator.throwCapturedInvalidParameterException();
        queryInstanceAccumulator.throwCapturedFunctionNotSupportedException();
        queryInstanceAccumulator.throwCapturedTypeErrorException();
        queryInstanceAccumulator.throwCapturedPagingErrorException();
    }
}
