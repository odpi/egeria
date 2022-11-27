/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.handlers.OpenLineageHandler;
import org.odpi.openmetadata.governanceservers.openlineage.model.NodeNamesSearchCriteria;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.requests.ElementHierarchyRequest;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageNodeNamesResponse;
import org.odpi.openmetadata.governanceservers.openlineage.requests.LineageSearchRequest;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageTypesResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageSearchResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageVertexResponse;
import org.odpi.openmetadata.governanceservers.openlineage.util.OpenLineageExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OpenLineageRestServices {

    private static final Logger log = LoggerFactory.getLogger(OpenLineageRestServices.class);
    private final OpenLineageInstanceHandler instanceHandler = new OpenLineageInstanceHandler();
    private final OpenLineageExceptionHandler openLineageExceptionHandler = new OpenLineageExceptionHandler();


    public LineageResponse lineage(String serverName, String userId, Scope scope, String guid,
                                   boolean includeProcesses) {
        LineageResponse response = new LineageResponse();
        final String methodName = "OpenLineageRestServices.lineage";
        final String debugMessage = "An exception occurred during a lineage HTTP request";
        try {
            OpenLineageHandler openLineageHandler = instanceHandler.getOpenLineageHandler(userId,
                    serverName,
                    methodName);
            response = openLineageHandler.lineage(scope, guid, includeProcesses);
        } catch (InvalidParameterException e) {
            openLineageExceptionHandler.captureInvalidParameterException(response, e);
            log.debug(debugMessage, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            openLineageExceptionHandler.capturePropertyServerException(response, e);
            log.debug(debugMessage, e);
        } catch (UserNotAuthorizedException e) {
            openLineageExceptionHandler.captureUserNotAuthorizedException(response, e);
            log.debug(debugMessage, e);
        } catch (OpenLineageException e) {
            openLineageExceptionHandler.captureOpenLineageException(response, e);
            log.debug(debugMessage, e);
        }catch (Exception e) {
            openLineageExceptionHandler.captureExceptions(response, e, methodName);
            log.debug(debugMessage, e);
        }
        return response;
    }

    public LineageVertexResponse getEntityDetails(String serverName, String userId, String guid) {
        LineageVertexResponse response = new LineageVertexResponse();
        final String methodName = "OpenLineageRestServices.getEntityDetails";
        final String debugMessage = "An exception occurred during a getEntityDetails HTTP request";
        try {
            OpenLineageHandler openLineageHandler = instanceHandler.getOpenLineageHandler(userId, serverName, methodName);
            response = openLineageHandler.getEntityDetails(guid);
        } catch (InvalidParameterException e) {
            openLineageExceptionHandler.captureInvalidParameterException(response, e);
            log.debug(debugMessage, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            openLineageExceptionHandler.capturePropertyServerException(response, e);
            log.debug(debugMessage, e);
        } catch (UserNotAuthorizedException e) {
            openLineageExceptionHandler.captureUserNotAuthorizedException(response, e);
            log.debug(debugMessage, e);
        } catch (OpenLineageException e) {
            openLineageExceptionHandler.captureOpenLineageException(response, e);
            log.debug(debugMessage, e);
        }catch (Exception e) {
            openLineageExceptionHandler.captureExceptions(response, e, methodName);
            log.debug(debugMessage, e);
        }
        return response;
    }

    public LineageTypesResponse getTypes(String serverName, String userId) {
        LineageTypesResponse response = new LineageTypesResponse();
        final String methodName = "OpenLineageRestServices.getTypes";
        final String debugMessage = "An exception occurred during a getTypes HTTP request";
        try {
            OpenLineageHandler openLineageHandler = instanceHandler.getOpenLineageHandler(userId, serverName, methodName);
            response = openLineageHandler.getTypes();
        } catch (InvalidParameterException e) {
            openLineageExceptionHandler.captureInvalidParameterException(response, e);
            log.debug(debugMessage, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            openLineageExceptionHandler.capturePropertyServerException(response, e);
            log.debug(debugMessage, e);
        } catch (UserNotAuthorizedException e) {
            openLineageExceptionHandler.captureUserNotAuthorizedException(response, e);
            log.debug(debugMessage, e);
        } catch (OpenLineageException e) {
            openLineageExceptionHandler.captureOpenLineageException(response, e);
            log.debug(debugMessage, e);
        }catch (Exception e) {
            openLineageExceptionHandler.captureExceptions(response, e, methodName);
            log.debug(debugMessage, e);
        }
        return response;
    }

    public LineageNodeNamesResponse getNodes(String serverName, String userId, NodeNamesSearchCriteria searchCriteria) {
        LineageNodeNamesResponse response = new LineageNodeNamesResponse();
        final String methodName = "OpenLineageRestServices.getNodes";
        final String debugMessage = "An exception occurred during a getNodes HTTP request";
        try {
            OpenLineageHandler openLineageHandler = instanceHandler.getOpenLineageHandler(userId, serverName, methodName);
            response = openLineageHandler.getNodes(searchCriteria);
        } catch (InvalidParameterException e) {
            openLineageExceptionHandler.captureInvalidParameterException(response, e);
            log.debug(debugMessage, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            openLineageExceptionHandler.capturePropertyServerException(response, e);
            log.debug(debugMessage, e);
        } catch (UserNotAuthorizedException e) {
            openLineageExceptionHandler.captureUserNotAuthorizedException(response, e);
            log.debug(debugMessage, e);
        } catch (OpenLineageException e) {
            openLineageExceptionHandler.captureOpenLineageException(response, e);
            log.debug(debugMessage, e);
        }catch (Exception e) {
            openLineageExceptionHandler.captureExceptions(response, e, methodName);
            log.debug(debugMessage, e);
        }
        return response;
    }

    public LineageSearchResponse search(String serverName, String userId, LineageSearchRequest lineageSearchRequest) {
        LineageSearchResponse response = new LineageSearchResponse();
        final String methodName = "OpenLineageRestServices.search";
        final String debugMessage = "An exception occurred during a getEntityDetails HTTP request";
        try {
            OpenLineageHandler openLineageHandler = instanceHandler.getOpenLineageHandler(userId, serverName, methodName);
            response = openLineageHandler.search(lineageSearchRequest);
        } catch (InvalidParameterException e) {
            openLineageExceptionHandler.captureInvalidParameterException(response, e);
            log.debug(debugMessage, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            openLineageExceptionHandler.capturePropertyServerException(response, e);
            log.debug(debugMessage, e);
        } catch (UserNotAuthorizedException e) {
            openLineageExceptionHandler.captureUserNotAuthorizedException(response, e);
            log.debug(debugMessage, e);
        } catch (OpenLineageException e) {
            openLineageExceptionHandler.captureOpenLineageException(response, e);
            log.debug(debugMessage, e);
        }catch (Exception e) {
            openLineageExceptionHandler.captureExceptions(response, e, methodName);
            log.debug(debugMessage, e);
        }
        return response;
    }

    public LineageResponse getElementHierarchy(String serverName, String userId, ElementHierarchyRequest elementHierarchyRequest) {
        LineageResponse response = new LineageResponse();
        final String methodName = "OpenLineageRestServices.search";
        final String debugMessage = "An exception occurred during a hierarchy HTTP request";
        try {
            OpenLineageHandler openLineageHandler = instanceHandler.getOpenLineageHandler(userId, serverName, methodName);
            response = openLineageHandler.getElementHierarchy(elementHierarchyRequest);
        } catch (InvalidParameterException e) {
            openLineageExceptionHandler.captureInvalidParameterException(response, e);
            log.debug(debugMessage, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            openLineageExceptionHandler.capturePropertyServerException(response, e);
            log.debug(debugMessage, e);
        } catch (UserNotAuthorizedException e) {
            openLineageExceptionHandler.captureUserNotAuthorizedException(response, e);
            log.debug(debugMessage, e);
        } catch (OpenLineageException e) {
            openLineageExceptionHandler.captureOpenLineageException(response, e);
            log.debug(debugMessage, e);
        }catch (Exception e) {
            openLineageExceptionHandler.captureExceptions(response, e, methodName);
            log.debug(debugMessage, e);
        }
        return response;
    }
}