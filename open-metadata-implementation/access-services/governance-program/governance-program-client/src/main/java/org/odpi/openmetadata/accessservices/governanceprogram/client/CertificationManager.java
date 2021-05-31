/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.accessservices.governanceprogram.api.CertificationManagementInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.client.rest.GovernanceProgramRESTClient;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.CertificationTypeElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.CertificationProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.CertificationTypeProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * CertificationManager is the java client for managing certification types and the certification of elements.
 */
public class CertificationManager implements CertificationManagementInterface
{
    private String                      serverName;               /* Initialized in constructor */
    private String                      serverPlatformURLRoot;    /* Initialized in constructor */
    private GovernanceProgramRESTClient restClient;               /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private NullRequestBody         nullRequestBody         = new NullRequestBody();

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     *
     * @throws InvalidParameterException bad input parameters
     */
    public CertificationManager(String serverName,
                                String serverPlatformURLRoot) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = new GovernanceProgramRESTClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
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
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = new GovernanceProgramRESTClient(serverName, serverPlatformURLRoot, userId, password);
    }



    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
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
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = new GovernanceProgramRESTClient(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
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
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = new GovernanceProgramRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that uses the supplied rest client.  This is typically used when called fro manother OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
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
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = restClient;
    }


    /* ========================================
     * Certification Types
     */

    /**
     * Create a description of the certification type.
     *
     * @param userId calling user
     * @param properties certification properties
     *
     * @return unique identifier of new definition
     *
     * @throws InvalidParameterException documentIdentifier or userId is null; documentIdentifier is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String createCertificationType(String                      userId,
                                          CertificationTypeProperties properties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String   methodName = "createCertificationType";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/certification-types";

        final String   docIdParameterName = "documentIdentifier";
        final String   titleParameterName = "title";
        final String   propertiesParameterName = "properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getDocumentIdentifier(), docIdParameterName, methodName);
        invalidParameterHandler.validateName(properties.getTitle(), titleParameterName, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformURLRoot + urlTemplate,
                                                                  properties,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
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
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/certification-types/{2}/update?isMergeUpdate={3}";

        final String guidParameterName = "certificationTypeGUID";
        final String docIdParameterName = "documentIdentifier";
        final String titleParameterName = "title";
        final String propertiesParameterName = "properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(certificationTypeGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(properties.getDocumentIdentifier(), docIdParameterName, methodName);
            invalidParameterHandler.validateName(properties.getTitle(), titleParameterName, methodName);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        properties,
                                        serverName,
                                        userId,
                                        certificationTypeGUID,
                                        isMergeUpdate);
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
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/certification-types/{2}/delete";
        final String guidParameterName = "certificationTypeGUID";

        invalidParameterHandler.validateGUID(certificationTypeGUID, guidParameterName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        certificationTypeGUID);
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
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/certification-types/{2}";

        final String guidParameterName = "certificationTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(certificationTypeGUID, guidParameterName, methodName);

        CertificationTypeResponse restResult = restClient.callCertificationTypeGetRESTCall(methodName,
                                                                                     serverPlatformURLRoot + urlTemplate,
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
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/certification-types/by-document-id/{2}";

        final String   documentIdParameterName = "documentId";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(documentIdentifier, documentIdParameterName, methodName);

        CertificationTypeResponse restResult = restClient.callCertificationTypeGetRESTCall(methodName,
                                                                                           serverPlatformURLRoot + urlTemplate,
                                                                                           serverName,
                                                                                           userId,
                                                                                           documentIdentifier);

        return restResult.getElement();
    }


    /**
     * Retrieve all of the certification types for a particular title.  The title can include regEx wildcards.
     *
     * @param userId calling user
     * @param title short description of the role
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of matching roles (null if no matching elements)
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
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/certification-types/by-title?startFrom={2}&pageSize={3}";
        final String titleParameterName = "title";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(title, titleParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setSearchString(title);
        requestBody.setSearchStringParameterName(titleParameterName);

        CertificationTypeListResponse restResult = restClient.callCertificationTypeListPostRESTCall(methodName,
                                                                                                    serverPlatformURLRoot + urlTemplate,
                                                                                                    requestBody,
                                                                                                    serverName,
                                                                                                    userId,
                                                                                                    startFrom,
                                                                                                    queryPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve all of the certification type definitions for a specific governance domain.
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
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/certification-types/by-domain/{2}?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        CertificationTypeListResponse restResult = restClient.callCertificationTypeListGetRESTCall(methodName,
                                                                                                   serverPlatformURLRoot + urlTemplate,
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
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void certifyElement(String                  userId,
                               String                  elementGUID,
                               String                  certificationTypeGUID,
                               CertificationProperties properties) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName = "certifyElement";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/certification-types/{2}/elements/{3}/certify";

        final String elementGUIDParameterName = "elementGUID";
        final String certificationTypeGUIDParameterName = "certificationTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(certificationTypeGUID, certificationTypeGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        properties,
                                        serverName,
                                        userId,
                                        certificationTypeGUID,
                                        elementGUID);
    }


    /**
     * Update the properties of a certification.  Remember to include the certificationId in the properties if the element has multiple
     * certifications for the same certification type.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element being certified
     * @param certificationTypeGUID unique identifier for the certification type
     * @param isMergeUpdate should the supplied properties overlay the existing properties or replace them
     * @param properties the properties of the certification
     *
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void updateCertification(String                  userId,
                                    String                  elementGUID,
                                    String                  certificationTypeGUID,
                                    boolean                 isMergeUpdate,
                                    CertificationProperties properties)  throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "updateCertification";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/certification-types/{2}/elements/{3}/update?isMergeUpdate={4}";

        final String elementGUIDParameterName = "elementGUID";
        final String certificationTypeGUIDParameterName = "certificationTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(certificationTypeGUID, certificationTypeGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        properties,
                                        serverName,
                                        userId,
                                        certificationTypeGUID,
                                        elementGUID,
                                        isMergeUpdate);
    }


    /**
     * Remove the certification for an element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element being certified
     * @param certificationTypeGUID unique identifier for the certification type
     * @param certificateId optional unique identifier from the certification authority - it is used to disambiguate the certifications for the element.
     *
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void decertifyElement(String userId,
                                 String elementGUID,
                                 String certificationTypeGUID,
                                 String certificateId)  throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName = "decertifyElement";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/certification-types/{2}/elements/{3}/decertify";

        final String elementGUIDParameterName = "elementGUID";
        final String certificationTypeGUIDParameterName = "certificationTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(certificationTypeGUID, certificationTypeGUIDParameterName, methodName);

        CertificateIdRequestBody requestBody = new CertificateIdRequestBody();

        requestBody.setCertificateId(certificateId);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        certificationTypeGUID,
                                        elementGUID);
    }
}
