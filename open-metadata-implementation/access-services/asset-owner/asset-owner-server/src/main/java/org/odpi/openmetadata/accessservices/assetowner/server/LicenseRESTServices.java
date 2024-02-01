/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.server;

import org.odpi.openmetadata.accessservices.assetowner.metadataelements.LicenseTypeElement;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.assetowner.properties.LicenseProperties;
import org.odpi.openmetadata.accessservices.assetowner.properties.LicenseTypeProperties;
import org.odpi.openmetadata.accessservices.assetowner.rest.LicenseTypeListResponse;
import org.odpi.openmetadata.accessservices.assetowner.rest.LicenseTypeResponse;
import org.odpi.openmetadata.accessservices.assetowner.rest.RelatedElementListResponse;
import org.odpi.openmetadata.accessservices.assetowner.rest.RelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.LicenseHandler;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * LicenseRESTServices is the java client for managing license types and the license of elements.
 */
public class LicenseRESTServices
{
    private static final AssetOwnerInstanceHandler instanceHandler = new AssetOwnerInstanceHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(LicenseRESTServices.class),
                                                                            instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public LicenseRESTServices()
    {
    }
 

    /* ========================================
     * License Types
     */

    /**
     * Retrieve the license type by the unique identifier assigned by this service when it was created.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param licenseTypeGUID identifier of the governance definition to retrieve
     *
     * @return properties of the license type or
     *  InvalidParameterException guid or userId is null; guid is not recognized
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public LicenseTypeResponse getLicenseTypeByGUID(String serverName,
                                                                String userId,
                                                                String licenseTypeGUID)
    {
        final String methodName = "getLicenseTypeByGUID";
        final String guidParameterName = "licenseTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LicenseTypeResponse response = new LicenseTypeResponse();
        AuditLog                  auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            LicenseHandler<LicenseTypeElement> handler = instanceHandler.getLicenseTypeHandler(userId, serverName, methodName);

            response.setElement(handler.getGovernanceDefinitionByGUID(userId,
                                                                      licenseTypeGUID,
                                                                      guidParameterName,
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
     * Retrieve the license type by its assigned unique document identifier.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param documentIdentifier identifier to search for
     *
     * @return properties of the matching license type or
     *  InvalidParameterException documentIdentifier or userId is null; documentIdentifier is not recognized
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public LicenseTypeResponse getLicenseTypeByDocId(String serverName,
                                                                 String userId,
                                                                 String documentIdentifier)
    {
        final String   methodName = "getLicenseTypeByDocId";

        final String   documentIdParameterName = "documentId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LicenseTypeResponse response = new LicenseTypeResponse();
        AuditLog                  auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LicenseHandler<LicenseTypeElement> handler = instanceHandler.getLicenseTypeHandler(userId, serverName, methodName);

            List<LicenseTypeElement> licenseTypeElements = handler.getGovernanceDefinitionsByName(userId,
                                                                                                  OpenMetadataType.LICENSE_TYPE_TYPE_NAME,
                                                                                                  documentIdentifier,
                                                                                                  documentIdParameterName,
                                                                                                  0,
                                                                                                  0,
                                                                                                  false,
                                                                                                  false,
                                                                                                  new Date(),
                                                                                                  methodName);

            if ((licenseTypeElements != null) && (! licenseTypeElements.isEmpty()))
            {
                response.setElement(licenseTypeElements.get(0));
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
     * Retrieve all the license types for a particular title.  The title can include regEx wildcards.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody short description of the license
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of matching license types (null if no matching elements) or
     *  InvalidParameterException title or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public LicenseTypeListResponse getLicenseTypesByTitle(String                  serverName,
                                                                      String                  userId,
                                                                      int                     startFrom,
                                                                      int                     pageSize,
                                                                      SearchStringRequestBody requestBody)
    {
        final String methodName = "getLicenseTypesByTitle";
        final String titleParameterName = "title";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LicenseTypeListResponse response = new LicenseTypeListResponse();
        AuditLog                      auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                LicenseHandler<LicenseTypeElement> handler = instanceHandler.getLicenseTypeHandler(userId, serverName, methodName);

                response.setElements(handler.findGovernanceDefinitions(userId,
                                                                       OpenMetadataType.LICENSE_TYPE_TYPE_NAME,
                                                                       requestBody.getSearchString(),
                                                                       titleParameterName,
                                                                       startFrom,
                                                                       pageSize,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName));
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
     * Retrieve all the license type definitions for a specific governance domain.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param domainIdentifier identifier to search for
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return properties of the matching license type definitions or
     *  InvalidParameterException domainIdentifier or userId is null; domainIdentifier is not recognized
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public LicenseTypeListResponse getLicenseTypeByDomainId(String serverName,
                                                                        String userId,
                                                                        int    domainIdentifier,
                                                                        int    startFrom,
                                                                        int    pageSize)
    {
        final String methodName = "getLicenseTypeByDomainId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LicenseTypeListResponse response = new LicenseTypeListResponse();
        AuditLog                      auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LicenseHandler<LicenseTypeElement> handler = instanceHandler.getLicenseTypeHandler(userId, serverName, methodName);

            response.setElements(handler.getGovernanceDefinitionsByDomain(userId,
                                                                          OpenMetadataType.LICENSE_TYPE_TYPE_NAME,
                                                                          domainIdentifier,
                                                                          startFrom,
                                                                          pageSize,
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


    /* =======================================
     * Licenses
     */

    /**
     * Link an element to a license type and include details of the license in the relationship properties.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the element being licensed
     * @param licenseTypeGUID unique identifier for the license type
     * @param requestBody the properties of the license
     *
     * @return guid or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public GUIDResponse licenseElement(String                  serverName,
                                       String                  userId,
                                       String                  elementGUID,
                                       String                  licenseTypeGUID,
                                       RelationshipRequestBody requestBody)
    {
        final String methodName = "licenseElement";

        final String elementGUIDParameterName = "elementGUID";
        final String licenseTypeGUIDParameterName = "licenseTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LicenseProperties properties)
                {
                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                    LicenseHandler<LicenseTypeElement> handler = instanceHandler.getLicenseTypeHandler(userId, serverName, methodName);

                    response.setGUID(handler.licenseElement(userId,
                                                            null,
                                                            null,
                                                            elementGUID,
                                                            elementGUIDParameterName,
                                                            OpenMetadataType.REFERENCEABLE.typeName,
                                                            licenseTypeGUID,
                                                            licenseTypeGUIDParameterName,
                                                            OpenMetadataType.LICENSE_TYPE_TYPE_NAME,
                                                            properties.getLicenseId(),
                                                            properties.getStartDate(),
                                                            properties.getEndDate(),
                                                            properties.getConditions(),
                                                            properties.getLicensedBy(),
                                                            properties.getLicensedByTypeName(),
                                                            properties.getLicensedByPropertyName(),
                                                            properties.getCustodian(),
                                                            properties.getCustodianTypeName(),
                                                            properties.getCustodianPropertyName(),
                                                            properties.getLicensee(),
                                                            properties.getLicenseeTypeName(),
                                                            properties.getLicenseePropertyName(),
                                                            properties.getNotes(),
                                                            properties.getEffectiveFrom(),
                                                            properties.getEffectiveTo(),
                                                            false,
                                                            false,
                                                            new Date(),
                                                            methodName));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(LicenseTypeProperties.class.getName(), methodName);
                }
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
     * Update the properties of a license.  Remember to include the licenseId in the properties if the element has multiple
     * licenses for the same license type.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param licenseGUID unique identifier for the license type
     * @param isMergeUpdate should the supplied properties overlay the existing properties or replace them
     * @param requestBody the properties of the license
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse updateLicense(String                  serverName,
                                            String                  userId,
                                            String                  licenseGUID,
                                            boolean                 isMergeUpdate,
                                            RelationshipRequestBody requestBody)
    {
        final String methodName = "updateLicense";
        final String licenseGUIDParameterName = "licenseGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LicenseProperties properties)
                {
                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                    LicenseHandler<LicenseTypeElement> handler = instanceHandler.getLicenseTypeHandler(userId, serverName, methodName);

                    handler.updateLicense(userId,
                                          null,
                                          null,
                                          licenseGUID,
                                          licenseGUIDParameterName,
                                          properties.getLicenseId(),
                                          properties.getStartDate(),
                                          properties.getEndDate(),
                                          properties.getConditions(),
                                          properties.getLicensedBy(),
                                          properties.getLicensedByTypeName(),
                                          properties.getLicensedByPropertyName(),
                                          properties.getCustodian(),
                                          properties.getCustodianTypeName(),
                                          properties.getCustodianPropertyName(),
                                          properties.getLicensee(),
                                          properties.getLicenseeTypeName(),
                                          properties.getLicenseePropertyName(),
                                          properties.getNotes(),
                                          isMergeUpdate,
                                          null,
                                          null,
                                          false,
                                          false,
                                          new Date(),
                                          methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(LicenseProperties.class.getName(), methodName);
                }
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
     * Remove the license for an element.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param licenseGUID unique identifier for the license type
     * @param requestBody external source information.
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value="unused")
    public VoidResponse unlicenseElement(String                  serverName,
                                         String                  userId,
                                         String                  licenseGUID,
                                         RelationshipRequestBody requestBody)
    {
        final String methodName = "unlicenseElement";
        final String licenseGUIDParameterName = "licenseGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            LicenseHandler<LicenseTypeElement> handler = instanceHandler.getLicenseTypeHandler(userId, serverName, methodName);

            handler.unlicenseElement(userId,
                                     null,
                                     null,
                                     licenseGUID,
                                     licenseGUIDParameterName,
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
     * Return information about the elements linked to a license.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param licenseTypeGUID unique identifier for the license
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the license or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public RelatedElementListResponse getLicensedElements(String serverName,
                                                           String userId,
                                                           String licenseTypeGUID,
                                                           int    startFrom,
                                                           int    pageSize)
    {
        final String methodName = "getLicensedElements";
        final String guidParameter = "licenseTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElementList(handler.getAttachedElements(userId,
                                                                licenseTypeGUID,
                                                                guidParameter,
                                                                OpenMetadataType.LICENSE_TYPE_TYPE_NAME,
                                                                OpenMetadataType.LICENSE_OF_REFERENCEABLE_TYPE_GUID,
                                                                OpenMetadataType.LICENSE_OF_REFERENCEABLE_TYPE_NAME,
                                                                OpenMetadataType.REFERENCEABLE.typeName,
                                                                null,
                                                                null,
                                                                1,
                                                                false,
                                                                false,
                                                                startFrom,
                                                                pageSize,
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
     * Return information about the licenses linked to an element.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier for the license
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the license or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public RelatedElementListResponse getLicenses(String serverName,
                                                        String userId,
                                                        String elementGUID,
                                                        int    startFrom,
                                                        int    pageSize)
    {
        final String methodName = "getLicences";
        final String guidParameterName = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElementList(handler.getAttachedElements(userId,
                                                                elementGUID,
                                                                guidParameterName,
                                                                OpenMetadataType.REFERENCEABLE.typeName,
                                                                OpenMetadataType.LICENSE_OF_REFERENCEABLE_TYPE_GUID,
                                                                OpenMetadataType.LICENSE_OF_REFERENCEABLE_TYPE_NAME,
                                                                OpenMetadataType.LICENSE_TYPE_TYPE_NAME,
                                                                null,
                                                                null,
                                                                2,
                                                                false,
                                                                false,
                                                                startFrom,
                                                                pageSize,
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
}
