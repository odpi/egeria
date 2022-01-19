/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.server;

import org.odpi.openmetadata.accessservices.governanceprogram.converters.ElementStubConverter;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.ElementStub;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceZoneDefinition;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceZoneElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceZoneProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceZoneDefinitionResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceZoneListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceZoneResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceZoneHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * GovernanceZoneRESTServices provides the server side logic for the Governance Zone Manager.
 * It manages the definitions of governance zones and their linkage to the rest of the
 * governance program.
 */
public class GovernanceZoneRESTServices
{
    private static GovernanceProgramInstanceHandler instanceHandler = new GovernanceProgramInstanceHandler();

    private static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(GovernanceZoneRESTServices.class),
                                                                      instanceHandler.getServiceName());

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public GovernanceZoneRESTServices()
    {
    }


    /**
     * Create a definition of a governance zone.  The qualified name of these governance zones can be added
     * to the supportedZones and defaultZones properties of an OMAS to control which assets are processed
     * and how they are set up.  In addition the qualified names of zones can be added to Asset definitions
     * to indicate which zone(s) they belong to.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody other properties for a governance zone
     *
     * @return unique identifier of the new zone or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public GUIDResponse createGovernanceZone(String                   serverName,
                                             String                   userId,
                                             GovernanceZoneProperties requestBody)
    {
        final String methodName = "createGovernanceZone";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                GovernanceZoneHandler<GovernanceZoneElement> handler = instanceHandler.getGovernanceZoneHandler(userId, serverName, methodName);

                String zoneGUID = handler.createGovernanceZone(userId,
                                                               null,
                                                               null,
                                                               requestBody.getQualifiedName(),
                                                               requestBody.getDisplayName(),
                                                               requestBody.getDescription(),
                                                               requestBody.getCriteria(),
                                                               requestBody.getScope(),
                                                               requestBody.getDomainIdentifier(),
                                                               requestBody.getAdditionalProperties(),
                                                               requestBody.getTypeName(),
                                                               requestBody.getExtendedProperties(),
                                                               methodName);

                response.setGUID(zoneGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update the definition of a zone.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param zoneGUID unique identifier of zone
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param requestBody properties to change
     *
     * @return void or
     *  InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse updateGovernanceZone(String                   serverName,
                                             String                   userId,
                                             String                   zoneGUID,
                                             boolean                  isMergeUpdate,
                                             GovernanceZoneProperties requestBody)
    {
        final String methodName = "updateGovernanceZone";
        final String guidParameter = "zoneGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                GovernanceZoneHandler<GovernanceZoneElement> handler = instanceHandler.getGovernanceZoneHandler(userId, serverName, methodName);

                handler.updateGovernanceZone(userId,
                                             null,
                                             null,
                                             zoneGUID,
                                             guidParameter,
                                             requestBody.getQualifiedName(),
                                             requestBody.getDisplayName(),
                                             requestBody.getDescription(),
                                             requestBody.getCriteria(),
                                             requestBody.getScope(),
                                             requestBody.getDomainIdentifier(),
                                             requestBody.getAdditionalProperties(),
                                             requestBody.getTypeName(),
                                             requestBody.getExtendedProperties(),
                                             isMergeUpdate,
                                             methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the definition of a zone.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param zoneGUID unique identifier of zone
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException guid or userId is null; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse deleteGovernanceZone(String serverName,
                                             String          userId,
                                             String          zoneGUID,
                                             NullRequestBody requestBody)
    {
        final String methodName = "deleteGovernanceZone";
        final String guidParameter = "zoneGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceZoneHandler<GovernanceZoneElement> handler = instanceHandler.getGovernanceZoneHandler(userId, serverName, methodName);

            handler.deleteBeanInRepository(userId,
                                           null,
                                           null,
                                           zoneGUID,
                                           guidParameter,
                                           OpenMetadataAPIMapper.ZONE_TYPE_GUID,
                                           OpenMetadataAPIMapper.ZONE_TYPE_NAME,
                                           null,
                                           null,
                                           false,
                                           false,
                                           new Date(),
                                           methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Link two related governance zones together as part of a hierarchy.
     * A zone can only have one parent but many child zones.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param parentZoneGUID unique identifier of the parent zone
     * @param childZoneGUID unique identifier of the child zone
     * @param requestBody null requestBody
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse linkZonesInHierarchy(String          serverName,
                                             String          userId,
                                             String          parentZoneGUID,
                                             String          childZoneGUID,
                                             NullRequestBody requestBody)
    {
        final String methodName = "linkZonesInHierarchy";

        final String parentZoneGUIDParameterName = "parentZoneGUID";
        final String childZoneGUIDParameterName = "childZoneGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceZoneHandler<GovernanceZoneElement> handler = instanceHandler.getGovernanceZoneHandler(userId, serverName, methodName);

            handler.linkElementToElement(userId,
                                         null,
                                         null,
                                         parentZoneGUID,
                                         parentZoneGUIDParameterName,
                                         OpenMetadataAPIMapper.ZONE_TYPE_NAME,
                                         childZoneGUID,
                                         childZoneGUIDParameterName,
                                         OpenMetadataAPIMapper.ZONE_TYPE_NAME,
                                         false,
                                         false,
                                         OpenMetadataAPIMapper.ZONE_HIERARCHY_TYPE_GUID,
                                         OpenMetadataAPIMapper.ZONE_HIERARCHY_TYPE_NAME,
                                         null,
                                         methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the link between two zones in the zone hierarchy.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param parentZoneGUID unique identifier of the parent zone
     * @param childZoneGUID unique identifier of the child zone
     * @param requestBody null requestBody
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse unlinkZonesInHierarchy(String          serverName,
                                               String          userId,
                                               String          parentZoneGUID,
                                               String          childZoneGUID,
                                               NullRequestBody requestBody)
    {
        final String methodName = "unlinkZonesInHierarchy";

        final String parentZoneGUIDParameterName = "parentZoneGUID";
        final String childZoneGUIDParameterName = "childZoneGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceZoneHandler<GovernanceZoneElement> handler = instanceHandler.getGovernanceZoneHandler(userId, serverName, methodName);

            handler.unlinkElementFromElement(userId,
                                             false,
                                             null,
                                             null,
                                             parentZoneGUID,
                                             parentZoneGUIDParameterName,
                                             OpenMetadataAPIMapper.ZONE_TYPE_NAME,
                                             childZoneGUID,
                                             childZoneGUIDParameterName,
                                             OpenMetadataAPIMapper.ZONE_TYPE_GUID,
                                             OpenMetadataAPIMapper.ZONE_TYPE_NAME,
                                             false,
                                             false,
                                             OpenMetadataAPIMapper.ZONE_HIERARCHY_TYPE_GUID,
                                             OpenMetadataAPIMapper.ZONE_HIERARCHY_TYPE_NAME,
                                             null,
                                             methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Link a governance zone to a governance definition that controls how the assets in the zone should be governed.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param zoneGUID unique identifier of the zone
     * @param definitionGUID unique identifier of the governance definition
     * @param requestBody null requestBody
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse linkZoneToGovernanceDefinition(String          serverName,
                                                       String          userId,
                                                       String          zoneGUID,
                                                       String          definitionGUID,
                                                       NullRequestBody requestBody)
    {
        final String methodName = "linkZoneToGovernanceDefinition";

        final String zoneGUIDParameterName = "zoneGUID";
        final String definitionGUIDParameterName = "definitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceZoneHandler<GovernanceZoneElement> handler = instanceHandler.getGovernanceZoneHandler(userId, serverName, methodName);

            handler.linkElementToElement(userId,
                                         null,
                                         null,
                                         definitionGUID,
                                         definitionGUIDParameterName,
                                         OpenMetadataAPIMapper.GOVERNANCE_DEFINITION_TYPE_NAME,
                                         zoneGUID,
                                         zoneGUIDParameterName,
                                         OpenMetadataAPIMapper.ZONE_TYPE_NAME,
                                         false,
                                         false,
                                         OpenMetadataAPIMapper.GOVERNED_BY_TYPE_GUID,
                                         OpenMetadataAPIMapper.GOVERNED_BY_TYPE_NAME,
                                         null,
                                         methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the link between a zone and a governance definition.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param zoneGUID unique identifier of the zone
     * @param definitionGUID unique identifier of the governance definition
     * @param requestBody null requestBody
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse unlinkZoneFromGovernanceDefinition(String          serverName,
                                                           String          userId,
                                                           String          zoneGUID,
                                                           String          definitionGUID,
                                                           NullRequestBody requestBody)
    {
        final String methodName = "unlinkZoneToGovernanceDefinition";

        final String zoneGUIDParameterName = "zoneGUID";
        final String definitionGUIDParameterName = "definitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceZoneHandler<GovernanceZoneElement> handler = instanceHandler.getGovernanceZoneHandler(userId, serverName, methodName);

            handler.unlinkElementFromElement(userId,
                                             false,
                                             null,
                                             null,
                                             definitionGUID,
                                             definitionGUIDParameterName,
                                             OpenMetadataAPIMapper.GOVERNANCE_DEFINITION_TYPE_NAME,
                                             zoneGUID,
                                             zoneGUIDParameterName,
                                             OpenMetadataAPIMapper.ZONE_TYPE_GUID,
                                             OpenMetadataAPIMapper.ZONE_TYPE_NAME,
                                             false,
                                             false,
                                             OpenMetadataAPIMapper.GOVERNED_BY_TYPE_GUID,
                                             OpenMetadataAPIMapper.GOVERNED_BY_TYPE_NAME,
                                             null,
                                             methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about a specific governance zone.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param zoneGUID unique identifier for the zone
     *
     * @return properties of the governance zone or
     *  InvalidParameterException zoneGUID or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public GovernanceZoneResponse getGovernanceZoneByGUID(String serverName,
                                                          String userId,
                                                          String zoneGUID)
    {
        final String methodName = "getGovernanceZoneByGUID";
        final String zoneGUIDParameterName = "zoneGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceZoneResponse response = new GovernanceZoneResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceZoneHandler<GovernanceZoneElement> handler = instanceHandler.getGovernanceZoneHandler(userId, serverName, methodName);

            response.setElement(handler.getBeanFromRepository(userId,
                                                              zoneGUID,
                                                              zoneGUIDParameterName,
                                                              OpenMetadataAPIMapper.ZONE_TYPE_NAME,
                                                              false,
                                                              false,
                                                              new Date(),
                                                              methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about a specific governance zone.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param qualifiedName unique name for the zone
     *
     * @return properties of the governance zone or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public GovernanceZoneResponse getGovernanceZoneByName(String serverName,
                                                          String userId,
                                                          String qualifiedName)
    {
        final String methodName = "getGovernanceZoneByName";
        final String qualifiedNameParameterName = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceZoneResponse response = new GovernanceZoneResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceZoneHandler<GovernanceZoneElement> handler = instanceHandler.getGovernanceZoneHandler(userId, serverName, methodName);

            response.setElement(handler.getGovernanceZone(userId,
                                                          qualifiedName,
                                                          qualifiedNameParameterName,
                                                          methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about the defined governance zones.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param domainIdentifier identifier for the desired governance domain - 0 for all domains
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the governance zone or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public GovernanceZoneListResponse getGovernanceZonesForDomain(String serverName,
                                                                  String userId,
                                                                  int    domainIdentifier,
                                                                  int    startFrom,
                                                                  int    pageSize)
    {
        final String methodName = "getGovernanceZonesForDomain";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceZoneListResponse response = new GovernanceZoneListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceZoneHandler<GovernanceZoneElement> handler = instanceHandler.getGovernanceZoneHandler(userId, serverName, methodName);

            List<GovernanceZoneElement> zones = handler.getGovernanceZonesByDomain(userId,
                                                                                   domainIdentifier,
                                                                                   startFrom,
                                                                                   pageSize,
                                                                                   methodName);

            response.setGovernanceZone(zones);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about a specific governance zone and its linked governance definitions.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param zoneGUID unique identifier for the zone
     *
     * @return properties of the governance zone linked to the associated governance definitions or
     *  InvalidParameterException zoneGUID or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public GovernanceZoneDefinitionResponse getGovernanceZoneDefinitionByGUID(String serverName,
                                                                              String userId,
                                                                              String zoneGUID)
    {
        final String methodName = "getGovernanceZoneDefinitionByGUID";
        final String zoneGUIDParameterName = "zoneGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceZoneDefinitionResponse response = new GovernanceZoneDefinitionResponse();
        AuditLog                         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceZoneHandler<GovernanceZoneElement> zoneHandler = instanceHandler.getGovernanceZoneHandler(userId, serverName, methodName);

            GovernanceZoneElement element = zoneHandler.getBeanFromRepository(userId,
                                                                              zoneGUID,
                                                                              zoneGUIDParameterName,
                                                                              OpenMetadataAPIMapper.ZONE_TYPE_NAME,
                                                                              false,
                                                                              false,
                                                                              new Date(),
                                                                              methodName);
            if (element != null)
            {
                GovernanceZoneDefinition zoneDefinition = new GovernanceZoneDefinition(element);

                ElementStubConverter<ElementStub> elementStubConverter = instanceHandler.getElementStubConverter(userId, serverName, methodName);

                Relationship relationship = zoneHandler.getGovernanceZoneParentGUID(userId,
                                                                                    zoneGUID,
                                                                                    zoneGUIDParameterName,
                                                                                    methodName);

                if (relationship != null)
                {
                    zoneDefinition.setParentGovernanceZone(elementStubConverter.getNewBean(ElementStub.class, relationship, true, methodName));
                }

                List<Relationship> relationships = zoneHandler.getGovernanceZoneChildrenGUIDs(userId,
                                                                                              zoneGUID,
                                                                                              zoneGUIDParameterName,
                                                                                              methodName);
                zoneDefinition.setNestedGovernanceZones(elementStubConverter.getNewBeans(ElementStub.class,
                                                                                         relationships,
                                                                                         false,
                                                                                         methodName));

                GovernanceDefinitionHandler<GovernanceDefinitionElement> definitionHandler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

                relationships = definitionHandler.getGoverningDefinitionLinks(userId,
                                                                              zoneGUID,
                                                                              zoneGUIDParameterName,
                                                                              OpenMetadataAPIMapper.ZONE_TYPE_NAME,
                                                                              null,
                                                                              0,
                                                                              0,
                                                                              methodName);

                zoneDefinition.setAssociatedGovernanceDefinitions(elementStubConverter.getNewBeans(ElementStub.class,
                                                                                                   relationships,
                                                                                                   true,
                                                                                                   methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
