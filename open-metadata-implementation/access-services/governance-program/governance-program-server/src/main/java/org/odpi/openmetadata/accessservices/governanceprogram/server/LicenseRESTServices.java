/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.generichandlers.LicenseHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.LicenseTypeElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LicenseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LicenseTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * LicenseRESTServices is the java client for managing license types and the license of elements.
 */
public class LicenseRESTServices
{
    private static final GovernanceProgramInstanceHandler instanceHandler = new GovernanceProgramInstanceHandler();

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
     * Create a description of the license type.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody license properties and initial status
     *
     * @return unique identifier of new definition or
     *  InvalidParameterException documentIdentifier or userId is null; documentIdentifier is not unique
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public GUIDResponse createLicenseType(String                          serverName,
                                          String                          userId,
                                          GovernanceDefinitionRequestBody requestBody)
    {
        final String methodName = "createLicenseType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LicenseTypeProperties properties)
                {
                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                    LicenseHandler<LicenseTypeElement> handler = instanceHandler.getLicenseTypeHandler(userId, serverName, methodName);

                    String setGUID = handler.createGovernanceDefinition(userId,
                                                                        properties.getDocumentIdentifier(),
                                                                        properties.getTitle(),
                                                                        properties.getSummary(),
                                                                        properties.getDescription(),
                                                                        properties.getScope(),
                                                                        properties.getDomainIdentifier(),
                                                                        properties.getImportance(),
                                                                        properties.getImplications(),
                                                                        properties.getOutcomes(),
                                                                        properties.getResults(),
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        properties.getDetails(),
                                                                        null,
                                                                        properties.getAdditionalProperties(),
                                                                        properties.getTypeName(),
                                                                        properties.getExtendedProperties(),
                                                                        null,
                                                                        null,
                                                                        new Date(),
                                                                        methodName);

                    response.setGUID(setGUID);
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update the properties of the license type.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param licenseTypeGUID identifier of the governance definition to change
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param requestBody license properties
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse updateLicenseType(String                          serverName,
                                                String                          userId,
                                                String                          licenseTypeGUID,
                                                boolean                         isMergeUpdate,
                                                GovernanceDefinitionRequestBody requestBody)
    {
        final String methodName = "updateLicenseType";
        final String guidParameterName = "licenseTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LicenseTypeProperties properties)
                {
                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                    LicenseHandler<LicenseTypeElement> handler = instanceHandler.getLicenseTypeHandler(userId, serverName, methodName);

                    handler.updateGovernanceDefinition(userId,
                                                       licenseTypeGUID,
                                                       guidParameterName,
                                                       properties.getDocumentIdentifier(),
                                                       properties.getTitle(),
                                                       properties.getSummary(),
                                                       properties.getDescription(),
                                                       properties.getScope(),
                                                       properties.getDomainIdentifier(),
                                                       properties.getImportance(),
                                                       properties.getImplications(),
                                                       properties.getOutcomes(),
                                                       properties.getResults(),
                                                       null,
                                                       null,
                                                       null,
                                                       null,
                                                       properties.getDetails(),
                                                       null,
                                                       properties.getAdditionalProperties(),
                                                       properties.getTypeName(),
                                                       properties.getExtendedProperties(),
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
                    restExceptionHandler.handleInvalidPropertiesObject(LicenseTypeProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Delete the properties of the license type.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param licenseTypeGUID identifier of the governance definition to delete
     * @param requestBody external source request body
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse deleteLicenseType(String                    serverName,
                                                String                    userId,
                                                String                    licenseTypeGUID,
                                                ExternalSourceRequestBody requestBody)
    {
        final String methodName = "deleteLicenseType";
        final String guidParameterName = "licenseTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            LicenseHandler<LicenseTypeElement> handler = instanceHandler.getLicenseTypeHandler(userId, serverName, methodName);

            handler.removeGovernanceDefinition(userId,
                                               licenseTypeGUID,
                                               guidParameterName,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
                                                                                                  OpenMetadataType.LICENSE_TYPE.typeName,
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
    public LicenseTypesResponse getLicenseTypesByTitle(String                  serverName,
                                                       String                  userId,
                                                       int                     startFrom,
                                                       int                     pageSize,
                                                       SearchStringRequestBody requestBody)
    {
        final String methodName = "getLicenseTypesByTitle";
        final String titleParameterName = "title";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LicenseTypesResponse response = new LicenseTypesResponse();
        AuditLog                auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                LicenseHandler<LicenseTypeElement> handler = instanceHandler.getLicenseTypeHandler(userId, serverName, methodName);

                response.setElements(handler.findGovernanceDefinitions(userId,
                                                                       OpenMetadataType.LICENSE_TYPE.typeName,
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
    public LicenseTypesResponse getLicenseTypeByDomainId(String serverName,
                                                         String userId,
                                                         int    domainIdentifier,
                                                         int    startFrom,
                                                         int    pageSize)
    {
        final String methodName = "getLicenseTypeByDomainId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LicenseTypesResponse response = new LicenseTypesResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LicenseHandler<LicenseTypeElement> handler = instanceHandler.getLicenseTypeHandler(userId, serverName, methodName);

            response.setElements(handler.getGovernanceDefinitionsByDomain(userId,
                                                                          OpenMetadataType.LICENSE_TYPE.typeName,
                                                                          domainIdentifier,
                                                                          startFrom,
                                                                          pageSize,
                                                                          false,
                                                                          false,
                                                                          new Date(),
                                                                          methodName));

        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
                                                            requestBody.getExternalSourceGUID(),
                                                            requestBody.getExternalSourceName(),
                                                            elementGUID,
                                                            elementGUIDParameterName,
                                                            OpenMetadataType.REFERENCEABLE.typeName,
                                                            licenseTypeGUID,
                                                            licenseTypeGUIDParameterName,
                                                            OpenMetadataType.LICENSE_TYPE.typeName,
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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

            if (requestBody != null)
            {
                handler.unlicenseElement(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         licenseGUID,
                                         licenseGUIDParameterName,
                                         false,
                                         false,
                                         new Date(),
                                         methodName);
            }
            else
            {
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
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
    public RelatedElementsResponse getLicensedElements(String serverName,
                                                       String userId,
                                                       String licenseTypeGUID,
                                                       int    startFrom,
                                                       int    pageSize)
    {
        final String methodName = "getLicensedElements";
        final String guidParameter = "licenseTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementsResponse response = new RelatedElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElements(handler.getAttachedElements(userId,
                                                             licenseTypeGUID,
                                                             guidParameter,
                                                             OpenMetadataType.LICENSE_TYPE.typeName,
                                                             OpenMetadataType.LICENSE_RELATIONSHIP.typeGUID,
                                                             OpenMetadataType.LICENSE_RELATIONSHIP.typeName,
                                                             OpenMetadataType.REFERENCEABLE.typeName,
                                                             null,
                                                             null,
                                                             1,
                                                             null,
                                                             null,
                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                             null,
                                                             false,
                                                             false,
                                                             startFrom,
                                                             pageSize,
                                                             new Date(),
                                                             methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
    public RelatedElementsResponse getLicenses(String serverName,
                                               String userId,
                                               String elementGUID,
                                               int    startFrom,
                                               int    pageSize)
    {
        final String methodName = "getLicences";
        final String guidParameterName = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementsResponse response = new RelatedElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElements(handler.getAttachedElements(userId,
                                                             elementGUID,
                                                             guidParameterName,
                                                             OpenMetadataType.REFERENCEABLE.typeName,
                                                             OpenMetadataType.LICENSE_RELATIONSHIP.typeGUID,
                                                             OpenMetadataType.LICENSE_RELATIONSHIP.typeName,
                                                             OpenMetadataType.LICENSE_TYPE.typeName,
                                                             null,
                                                             null,
                                                             2,
                                                             null,
                                                             null,
                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                             null,
                                                             false,
                                                             false,
                                                             startFrom,
                                                             pageSize,
                                                             new Date(),
                                                             methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
