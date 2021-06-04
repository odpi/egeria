/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.handlers.OpenLineageHandler;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageVertexResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;
import org.odpi.openmetadata.governanceservers.openlineage.util.OpenLineageExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OpenLineageRestServices {

    private static final Logger log = LoggerFactory.getLogger(OpenLineageRestServices.class);
    private final OpenLineageInstanceHandler instanceHandler = new OpenLineageInstanceHandler();
    private OpenLineageExceptionHandler openLineageExceptionHandler = new OpenLineageExceptionHandler();


    public LineageResponse lineage(String serverName, String userId, Scope scope, String guid, String displayNameMustContain,
                                   boolean includeProcesses) {
        LineageResponse response = new LineageResponse();
        final String methodName = "OpenLineageRestServices.lineage";
        final String debugMessage = "An exception occurred during a lineage HTTP request";
        try {
            OpenLineageHandler openLineageHandler = instanceHandler.getOpenLineageHandler(userId,
                    serverName,
                    methodName);
            response = openLineageHandler.lineage(scope, guid, displayNameMustContain, includeProcesses);
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
            openLineageExceptionHandler.captureThrowable(response, e, methodName);
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
            openLineageExceptionHandler.captureThrowable(response, e, methodName);
            log.debug(debugMessage, e);
        }
        return response;
    }
}