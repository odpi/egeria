/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.server;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.ffdc.LineageWarehouseException;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.handlers.LineageWarehouseHandler;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.model.NodeNamesSearchCriteria;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.model.Scope;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.requests.ElementHierarchyRequest;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.responses.LineageNodeNamesResponse;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.requests.LineageSearchRequest;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.responses.LineageResponse;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.responses.LineageTypesResponse;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.responses.LineageSearchResponse;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.responses.LineageVertexResponse;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.util.LineageWarehouseExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LineageWarehouseRESTServices
{

    private static final Logger                          log                         = LoggerFactory.getLogger(LineageWarehouseRESTServices.class);
    private final LineageWarehouseInstanceHandler  instanceHandler                  = new LineageWarehouseInstanceHandler();
    private final LineageWarehouseExceptionHandler lineageWarehouseExceptionHandler = new LineageWarehouseExceptionHandler();


    public LineageResponse lineage(String serverName, String userId, Scope scope, String guid,
                                   boolean includeProcesses) {
        LineageResponse response = new LineageResponse();
        final String methodName = "OpenLineageRestServices.lineage";
        final String debugMessage = "An exception occurred during a lineage HTTP request";
        try {
            LineageWarehouseHandler lineageWarehouseHandler = instanceHandler.getOpenLineageHandler(userId,
                                                                                                    serverName,
                                                                                                    methodName);
            response = lineageWarehouseHandler.lineage(scope, guid, includeProcesses);
        } catch (InvalidParameterException e) {
            lineageWarehouseExceptionHandler.captureInvalidParameterException(response, e);
            log.debug(debugMessage, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            lineageWarehouseExceptionHandler.capturePropertyServerException(response, e);
            log.debug(debugMessage, e);
        } catch (UserNotAuthorizedException e) {
            lineageWarehouseExceptionHandler.captureUserNotAuthorizedException(response, e);
            log.debug(debugMessage, e);
        } catch (LineageWarehouseException e) {
            lineageWarehouseExceptionHandler.captureOpenLineageException(response, e);
            log.debug(debugMessage, e);
        }catch (Exception e) {
            lineageWarehouseExceptionHandler.captureExceptions(response, e, methodName);
            log.debug(debugMessage, e);
        }
        return response;
    }

    public LineageVertexResponse getEntityDetails(String serverName, String userId, String guid) {
        LineageVertexResponse response = new LineageVertexResponse();
        final String methodName = "OpenLineageRestServices.getEntityDetails";
        final String debugMessage = "An exception occurred during a getEntityDetails HTTP request";
        try {
            LineageWarehouseHandler lineageWarehouseHandler = instanceHandler.getOpenLineageHandler(userId, serverName, methodName);
            response = lineageWarehouseHandler.getEntityDetails(guid);
        } catch (InvalidParameterException e) {
            lineageWarehouseExceptionHandler.captureInvalidParameterException(response, e);
            log.debug(debugMessage, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            lineageWarehouseExceptionHandler.capturePropertyServerException(response, e);
            log.debug(debugMessage, e);
        } catch (UserNotAuthorizedException e) {
            lineageWarehouseExceptionHandler.captureUserNotAuthorizedException(response, e);
            log.debug(debugMessage, e);
        } catch (LineageWarehouseException e) {
            lineageWarehouseExceptionHandler.captureOpenLineageException(response, e);
            log.debug(debugMessage, e);
        }catch (Exception e) {
            lineageWarehouseExceptionHandler.captureExceptions(response, e, methodName);
            log.debug(debugMessage, e);
        }
        return response;
    }

    public LineageTypesResponse getTypes(String serverName, String userId) {
        LineageTypesResponse response = new LineageTypesResponse();
        final String methodName = "OpenLineageRestServices.getTypes";
        final String debugMessage = "An exception occurred during a getTypes HTTP request";
        try {
            LineageWarehouseHandler lineageWarehouseHandler = instanceHandler.getOpenLineageHandler(userId, serverName, methodName);
            response = lineageWarehouseHandler.getTypes();
        } catch (InvalidParameterException e) {
            lineageWarehouseExceptionHandler.captureInvalidParameterException(response, e);
            log.debug(debugMessage, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            lineageWarehouseExceptionHandler.capturePropertyServerException(response, e);
            log.debug(debugMessage, e);
        } catch (UserNotAuthorizedException e) {
            lineageWarehouseExceptionHandler.captureUserNotAuthorizedException(response, e);
            log.debug(debugMessage, e);
        } catch (LineageWarehouseException e) {
            lineageWarehouseExceptionHandler.captureOpenLineageException(response, e);
            log.debug(debugMessage, e);
        }catch (Exception e) {
            lineageWarehouseExceptionHandler.captureExceptions(response, e, methodName);
            log.debug(debugMessage, e);
        }
        return response;
    }

    public LineageNodeNamesResponse getNodes(String serverName, String userId, NodeNamesSearchCriteria searchCriteria) {
        LineageNodeNamesResponse response = new LineageNodeNamesResponse();
        final String methodName = "OpenLineageRestServices.getNodes";
        final String debugMessage = "An exception occurred during a getNodes HTTP request";
        try {
            LineageWarehouseHandler lineageWarehouseHandler = instanceHandler.getOpenLineageHandler(userId, serverName, methodName);
            response = lineageWarehouseHandler.getNodes(searchCriteria);
        } catch (InvalidParameterException e) {
            lineageWarehouseExceptionHandler.captureInvalidParameterException(response, e);
            log.debug(debugMessage, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            lineageWarehouseExceptionHandler.capturePropertyServerException(response, e);
            log.debug(debugMessage, e);
        } catch (UserNotAuthorizedException e) {
            lineageWarehouseExceptionHandler.captureUserNotAuthorizedException(response, e);
            log.debug(debugMessage, e);
        } catch (LineageWarehouseException e) {
            lineageWarehouseExceptionHandler.captureOpenLineageException(response, e);
            log.debug(debugMessage, e);
        }catch (Exception e) {
            lineageWarehouseExceptionHandler.captureExceptions(response, e, methodName);
            log.debug(debugMessage, e);
        }
        return response;
    }

    public LineageSearchResponse search(String serverName, String userId, LineageSearchRequest lineageSearchRequest) {
        LineageSearchResponse response = new LineageSearchResponse();
        final String methodName = "OpenLineageRestServices.search";
        final String debugMessage = "An exception occurred during a getEntityDetails HTTP request";
        try {
            LineageWarehouseHandler lineageWarehouseHandler = instanceHandler.getOpenLineageHandler(userId, serverName, methodName);
            response = lineageWarehouseHandler.search(lineageSearchRequest);
        } catch (InvalidParameterException e) {
            lineageWarehouseExceptionHandler.captureInvalidParameterException(response, e);
            log.debug(debugMessage, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            lineageWarehouseExceptionHandler.capturePropertyServerException(response, e);
            log.debug(debugMessage, e);
        } catch (UserNotAuthorizedException e) {
            lineageWarehouseExceptionHandler.captureUserNotAuthorizedException(response, e);
            log.debug(debugMessage, e);
        } catch (LineageWarehouseException e) {
            lineageWarehouseExceptionHandler.captureOpenLineageException(response, e);
            log.debug(debugMessage, e);
        }catch (Exception e) {
            lineageWarehouseExceptionHandler.captureExceptions(response, e, methodName);
            log.debug(debugMessage, e);
        }
        return response;
    }

    public LineageResponse getElementHierarchy(String serverName, String userId, ElementHierarchyRequest elementHierarchyRequest) {
        LineageResponse response = new LineageResponse();
        final String methodName = "OpenLineageRestServices.search";
        final String debugMessage = "An exception occurred during a hierarchy HTTP request";
        try {
            LineageWarehouseHandler lineageWarehouseHandler = instanceHandler.getOpenLineageHandler(userId, serverName, methodName);
            response = lineageWarehouseHandler.getElementHierarchy(elementHierarchyRequest);
        } catch (InvalidParameterException e) {
            lineageWarehouseExceptionHandler.captureInvalidParameterException(response, e);
            log.debug(debugMessage, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            lineageWarehouseExceptionHandler.capturePropertyServerException(response, e);
            log.debug(debugMessage, e);
        } catch (UserNotAuthorizedException e) {
            lineageWarehouseExceptionHandler.captureUserNotAuthorizedException(response, e);
            log.debug(debugMessage, e);
        } catch (LineageWarehouseException e) {
            lineageWarehouseExceptionHandler.captureOpenLineageException(response, e);
            log.debug(debugMessage, e);
        }catch (Exception e) {
            lineageWarehouseExceptionHandler.captureExceptions(response, e, methodName);
            log.debug(debugMessage, e);
        }
        return response;
    }
}