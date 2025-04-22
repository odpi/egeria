/*  SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.accessservices.governanceprogram.api.CertificationManagementInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.client.rest.GovernanceProgramRESTClient;
import org.odpi.openmetadata.commonservices.ffdc.rest.CertificationTypeResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.CertificationTypesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.RelatedElementsResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GovernanceDefinitionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;

import java.util.List;

/**
 * CertificationManager is the java client for managing certification types and the certification of elements.
 */
public class CertificationManager extends GovernanceProgramBaseClient implements CertificationManagementInterface
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     *
     * @throws InvalidParameterException bad input parameters
     */
    public CertificationManager(String serverName,
                                String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException bad input parameters
     */
    public CertificationManager(String     serverName,
                                String     serverPlatformURLRoot,
                                String     userId,
                                String     password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }



    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException bad input parameters
     */
    public CertificationManager(String   serverName,
                                String   serverPlatformURLRoot,
                                int      maxPageSize,
                                AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException bad input parameters
     */
    public CertificationManager(String     serverName,
                                String     serverPlatformURLRoot,
                                String     userId,
                                String     password,
                                int        maxPageSize,
                                AuditLog   auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize, auditLog);
    }


    /**
     * Create a new client that uses the supplied rest client.  This is typically used when called from another OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient internal client for rest calls
     * @param maxPageSize pre-initialized parameter limit
     *
     * @throws InvalidParameterException bad input parameters
     */
    public CertificationManager(String                      serverName,
                                String                      serverPlatformURLRoot,
                                GovernanceProgramRESTClient restClient,
                                int                         maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /* ========================================
     * Certification Types
     */

    /**
     * Create a description of the certification type.
     *
     * @param userId calling user
     * @param properties certification properties
     * @param initialStatus what is the initial status for the certification type - default value is DRAFT
     *
     * @return unique identifier of new definition
     *
     * @throws InvalidParameterException documentIdentifier or userId is null; documentIdentifier is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String createCertificationType(String                      userId,
                                          CertificationTypeProperties properties,
                                          GovernanceDefinitionStatus  initialStatus) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "createCertificationType";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/certification-types";
        final String propertiesParameterName = "properties";

        return super.createGovernanceDefinition(userId, properties, propertiesParameterName, initialStatus, urlTemplate, methodName);
    }


    /**
     * Update the properties of the certification type.
     *
     * @param userId calling user
     * @param certificationTypeGUID identifier of the governance definition to change
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties certification properties
     *
     * @throws InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void updateCertificationType(String                      userId,
                                        String                      certificationTypeGUID,
                                        boolean                     isMergeUpdate,
                                        CertificationTypeProperties properties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "updateCertificationType";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/certification-types/{2}/update?isMergeUpdate={3}";
        final String guidParameterName = "certificationTypeGUID";
        final String propertiesParameterName = "properties";

        super.updateGovernanceDefinition(userId,
                                         certificationTypeGUID,
                                         guidParameterName,
                                         isMergeUpdate,
                                         properties,
                                         propertiesParameterName,
                                         urlTemplate,
                                         methodName);
    }


    /**
     * Delete the properties of the certification type.
     *
     * @param userId calling user
     * @param certificationTypeGUID identifier of the governance definition to delete
     *
     * @throws InvalidParameterException guid or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void deleteCertificationType(String userId,
                                        String certificationTypeGUID) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "deleteCertificationType";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/certification-types/{2}/delete";
        final String guidParameterName = "certificationTypeGUID";

        super.removeReferenceable(userId, certificationTypeGUID, guidParameterName, urlTemplate, methodName);
    }


    /**
     * Retrieve the certification type by the unique identifier assigned by this service when it was created.
     *
     * @param userId calling user
     * @param certificationTypeGUID identifier of the governance definition to retrieve
     *
     * @return properties of the certification type
     *
     * @throws InvalidParameterException guid or userId is null; guid is not recognized
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public CertificationTypeElement getCertificationTypeByGUID(String userId,
                                                               String certificationTypeGUID) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "getCertificationTypeByGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/certification-types/{2}";

        final String guidParameterName = "certificationTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(certificationTypeGUID, guidParameterName, methodName);

        CertificationTypeResponse restResult = restClient.callCertificationTypeGetRESTCall(methodName,
                                                                                           urlTemplate,
                                                                                           serverName,
                                                                                           userId,
                                                                                           certificationTypeGUID);

        return restResult.getElement();
    }


    /**
     * Retrieve the certification type by its assigned unique document identifier.
     *
     * @param userId calling user
     * @param documentIdentifier identifier to search for
     *
     * @return properties of the matching certification type
     *
     * @throws InvalidParameterException documentIdentifier or userId is null; documentIdentifier is not recognized
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public CertificationTypeElement getCertificationTypeByDocId(String userId,
                                                                String documentIdentifier) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String   methodName = "getCertificationTypeByDocId";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/certification-types/by-document-id/{2}";

        final String   documentIdParameterName = "documentId";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(documentIdentifier, documentIdParameterName, methodName);

        CertificationTypeResponse restResult = restClient.callCertificationTypeGetRESTCall(methodName,
                                                                                           urlTemplate,
                                                                                           serverName,
                                                                                           userId,
                                                                                           documentIdentifier);

        return restResult.getElement();
    }


    /**
     * Retrieve all the certification types for a particular title.  The title can include regEx wildcards.
     *
     * @param userId calling user
     * @param title short description of the certification
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of matching certification types (null if no matching elements)
     *
     * @throws InvalidParameterException title or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<CertificationTypeElement> getCertificationTypesByTitle(String userId,
                                                                       String title,
                                                                       int    startFrom,
                                                                       int    pageSize) throws UserNotAuthorizedException,
                                                                                               InvalidParameterException,
                                                                                               PropertyServerException
    {
        final String methodName = "getCertificationTypesByTitle";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/certification-types/by-title?startFrom={2}&pageSize={3}";
        final String titleParameterName = "title";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(title, titleParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setSearchString(title);
        requestBody.setSearchStringParameterName(titleParameterName);

        CertificationTypesResponse restResult = restClient.callCertificationTypeListPostRESTCall(methodName,
                                                                                                 urlTemplate,
                                                                                                 requestBody,
                                                                                                 serverName,
                                                                                                 userId,
                                                                                                 startFrom,
                                                                                                 queryPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve all the certification type definitions for a specific governance domain.
     *
     * @param userId calling user
     * @param domainIdentifier identifier to search for
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return properties of the matching certification type definitions
     *
     * @throws InvalidParameterException domainIdentifier or userId is null; domainIdentifier is not recognized
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<CertificationTypeElement> getCertificationTypeByDomainId(String userId,
                                                                         int    domainIdentifier,
                                                                         int    startFrom,
                                                                         int    pageSize) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName = "getCertificationTypeByDomainId";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/certification-types/by-domain/{2}?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        CertificationTypesResponse restResult = restClient.callCertificationTypeListGetRESTCall(methodName,
                                                                                                urlTemplate,
                                                                                                serverName,
                                                                                                userId,
                                                                                                domainIdentifier,
                                                                                                startFrom,
                                                                                                queryPageSize);

        return restResult.getElements();
    }


    /* =======================================
     * Certifications
     */

    /**
     * Link an element to a certification type and include details of the certification in the relationship properties.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element being certified
     * @param certificationTypeGUID unique identifier for the certification type
     * @param properties the properties of the certification
     *
     * @return unique identifier of the new relationship
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String certifyElement(String                  userId,
                                 String                  elementGUID,
                                 String                  certificationTypeGUID,
                                 CertificationProperties properties) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "certifyElement";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/elements/{2}/certification-types/{3}/certify";

        final String elementGUIDParameterName = "elementGUID";
        final String certificationTypeGUIDParameterName = "certificationTypeGUID";

        return super.setupMultiLinkRelationship(userId,
                                                elementGUID,
                                                elementGUIDParameterName,
                                                null,
                                                properties,
                                                certificationTypeGUID,
                                                certificationTypeGUIDParameterName,
                                                urlTemplate,
                                                methodName);
    }


    /**
     * Update the properties of a certification.
     *
     * @param userId calling user
     * @param certificationGUID unique identifier of the certification relationship being updated
     * @param isMergeUpdate should the supplied properties overlay the existing properties or replace them
     * @param properties the properties of the certification
     *
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void updateCertification(String                  userId,
                                    String                  certificationGUID,
                                    boolean                 isMergeUpdate,
                                    CertificationProperties properties)  throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "updateCertification";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/certifications/{2}/update?isMergeUpdate={3}";

        final String certificationGUIDParameterName = "certificationGUID";

        super.updateRelationship(userId,
                                 certificationGUID,
                                 certificationGUIDParameterName,
                                 isMergeUpdate,
                                 null,
                                 properties,
                                 urlTemplate,
                                 methodName);
    }


    /**
     * Remove the certification for an element.
     *
     * @param userId calling user
     * @param certificationGUID unique identifier of the certification relationship
     *
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void decertifyElement(String userId,
                                 String certificationGUID)  throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "decertifyElement";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/certifications/{2}/delete";

        final String certificationGUIDParameterName = "certificationGUID";

        super.clearRelationship(userId,
                                certificationGUID,
                                certificationGUIDParameterName,
                                null,
                                urlTemplate,
                                methodName);
    }



    /**
     * Return information about the elements linked to a certification.
     *
     * @param userId calling user
     * @param certificationTypeGUID unique identifier for the certification
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the certification
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<RelatedElementStub> getCertifiedElements(String userId,
                                                         String certificationTypeGUID,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "getCertifiedElements";
        final String guidParameter = "certificationTypeGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/elements/certifications/{2}?&startFrom={3}&pageSize={4}";

        return super.getRelatedElements(userId, certificationTypeGUID, guidParameter, urlTemplate, startFrom, pageSize, methodName);
    }


    /**
     * Return information about the certifications linked to an element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier for the certification
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the certification
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<RelatedElementStub> getCertifications(String userId,
                                                      String elementGUID,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "getLicences";
        final String guidParameterName = "elementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/elements/{2}/certifications?&startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        RelatedElementsResponse restResult = restClient.callRelatedElementsGetRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       serverName,
                                                                                       userId,
                                                                                       elementGUID,
                                                                                       startFrom,
                                                                                       queryPageSize);

        return restResult.getElements();
    }
}
