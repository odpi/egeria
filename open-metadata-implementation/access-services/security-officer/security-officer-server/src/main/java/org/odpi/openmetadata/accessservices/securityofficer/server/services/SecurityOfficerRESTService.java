/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.server.services;

import org.odpi.openmetadata.accessservices.securityofficer.api.model.SecurityClassification;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.rest.SecurityOfficerSchemaElementListResponse;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.rest.SecurityOfficerSecurityTagListResponse;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.rest.SecurityOfficerSecurityTagResponse;
import org.odpi.openmetadata.accessservices.securityofficer.server.handler.SecurityOfficerRequestHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;

/**
 * The SecurityOfficerRESTService provides the server-side implementation of the Security Officer Open Metadata Assess Service (OMAS).
 */
public class SecurityOfficerRESTService
{
    private static SecurityOfficerInstanceHandler instanceHandler = new SecurityOfficerInstanceHandler();

    private        RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private static RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(SecurityOfficerRESTService.class),
                                                                                  instanceHandler.getServiceName());




    /**
     * Returns the security tag for the given schema element
     *
     * @param serverName      name of the server instances for this request
     * @param userId          String - userId of user making request.
     * @param schemaElementId unique identifier of the schema element
     */
    public SecurityOfficerSecurityTagResponse getSecurityTagBySchemaElementId(String serverName, String userId, String schemaElementId) {
        
        final String   methodName = "getSecurityTagBySchemaElementId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        AuditLog     auditLog = null;
        SecurityOfficerSecurityTagResponse response = new SecurityOfficerSecurityTagResponse();

        try
        {
            SecurityOfficerRequestHandler handler = instanceHandler.getSecurityOfficerRequestHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setSecurityTag(handler.getSecurityTagBySchemaElementId(userId, schemaElementId, methodName));
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Returns the security tags available
     *
     * @param serverName name of the server instances for this request
     * @param userId     String - userId of user making request.
     */
    public SecurityOfficerSecurityTagListResponse getSecurityTags(String serverName, String userId) {

        final String   methodName = "getSecurityTags";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        AuditLog     auditLog = null;
        SecurityOfficerSecurityTagListResponse response = new SecurityOfficerSecurityTagListResponse();
        try {
            SecurityOfficerRequestHandler handler = instanceHandler.getSecurityOfficerRequestHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setSecurityTags(handler.getAvailableSecurityTags(userId, methodName));
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Save or update the security tag for the given schema element
     *
     * @param serverName             name of the server instances for this request
     * @param userId                 String - userId of user making request.
     * @param securityClassification security tag assigned to the schema element
     * @param schemaElementId        unique identifier of the schema element
     */
    public SecurityOfficerSchemaElementListResponse updateSecurityTag(String serverName, String userId, String schemaElementId, SecurityClassification securityClassification) {

        final String   methodName = "updateSecurityTag";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog     auditLog = null;
        SecurityOfficerSchemaElementListResponse response = new SecurityOfficerSchemaElementListResponse();

        try {
            SecurityOfficerRequestHandler handler = instanceHandler.getSecurityOfficerRequestHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setSchemaElementEntityList(handler.updateSecurityTagBySchemaElementId(userId, schemaElementId, securityClassification, methodName));
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Delete the security tag for the given schema element
     *
     * @param serverName      name of the server instances for this request
     * @param userId          String - userId of user making request.
     * @param schemaElementId unique identifier of the schema element
     */
    public SecurityOfficerSchemaElementListResponse deleteSecurityTag(String serverName, String userId, String schemaElementId) {

        final String   methodName = "deleteSecurityTag";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog     auditLog = null;
        SecurityOfficerSchemaElementListResponse response = new SecurityOfficerSchemaElementListResponse();

        try {
            SecurityOfficerRequestHandler handler = instanceHandler.getSecurityOfficerRequestHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setSchemaElementEntityList(handler.deleteSecurityTagBySchemaElementId(userId, schemaElementId, methodName));
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}