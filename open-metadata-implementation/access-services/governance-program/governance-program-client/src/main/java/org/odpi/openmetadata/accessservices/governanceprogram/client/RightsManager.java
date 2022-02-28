/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.accessservices.governanceprogram.api.RightsManagementInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.client.rest.GovernanceProgramRESTClient;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.LicenseTypeElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDefinitionStatus;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.LicenseProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.LicenseTypeProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.CertificateIdRequestBody;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.LicenseTypeListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.LicenseTypeRequestBody;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.LicenseTypeResponse;
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
 * RightsManager is the java client for managing license types and the licensing of elements.
 */
public class RightsManager implements RightsManagementInterface
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
    public RightsManager(String serverName,
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
    public RightsManager(String     serverName,
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
    public RightsManager(String   serverName,
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
    public RightsManager(String     serverName,
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
    public RightsManager(String                      serverName,
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
     * License Types
     */

    /**
     * Create a description of the license type.
     *
     * @param userId calling user
     * @param properties license properties
     * @param initialStatus what is the initial status for the license type definition - default value is DRAFT
     *
     * @return unique identifier of new definition
     *
     * @throws InvalidParameterException documentIdentifier or userId is null; documentIdentifier is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String createLicenseType(String                     userId,
                                    LicenseTypeProperties      properties,
                                    GovernanceDefinitionStatus initialStatus) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String   methodName = "createLicenseType";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/license-types";

        final String   docIdParameterName = "documentIdentifier";
        final String   titleParameterName = "title";
        final String   propertiesParameterName = "properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getDocumentIdentifier(), docIdParameterName, methodName);
        invalidParameterHandler.validateName(properties.getTitle(), titleParameterName, methodName);

        LicenseTypeRequestBody requestBody = new LicenseTypeRequestBody();

        requestBody.setProperties(properties);
        requestBody.setInitialStatus(initialStatus);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformURLRoot + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Update the properties of the license type.
     *
     * @param userId calling user
     * @param licenseTypeGUID identifier of the governance definition to change
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties license properties
     *
     * @throws InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void updateLicenseType(String                      userId,
                                  String                      licenseTypeGUID,
                                  boolean                     isMergeUpdate,
                                  LicenseTypeProperties properties) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "updateLicenseType";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/license-types/{2}/update?isMergeUpdate={3}";

        final String guidParameterName = "licenseTypeGUID";
        final String docIdParameterName = "documentIdentifier";
        final String titleParameterName = "title";
        final String propertiesParameterName = "properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(licenseTypeGUID, guidParameterName, methodName);
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
                                        licenseTypeGUID,
                                        isMergeUpdate);
    }


    /**
     * Delete the properties of the license type.
     *
     * @param userId calling user
     * @param licenseTypeGUID identifier of the governance definition to delete
     *
     * @throws InvalidParameterException guid or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void deleteLicenseType(String userId,
                                  String licenseTypeGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName = "deleteLicenseType";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/license-types/{2}/delete";
        final String guidParameterName = "licenseTypeGUID";

        invalidParameterHandler.validateGUID(licenseTypeGUID, guidParameterName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        licenseTypeGUID);
    }


    /**
     * Retrieve the license type by the unique identifier assigned by this service when it was created.
     *
     * @param userId calling user
     * @param licenseTypeGUID identifier of the governance definition to retrieve
     *
     * @return properties of the license type
     *
     * @throws InvalidParameterException guid or userId is null; guid is not recognized
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public LicenseTypeElement getLicenseTypeByGUID(String userId,
                                                   String licenseTypeGUID) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "getLicenseTypeByGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/license-types/{2}";

        final String guidParameterName = "licenseTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(licenseTypeGUID, guidParameterName, methodName);

        LicenseTypeResponse restResult = restClient.callLicenseTypeGetRESTCall(methodName,
                                                                               serverPlatformURLRoot + urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               licenseTypeGUID);

        return restResult.getElement();
    }


    /**
     * Retrieve the license type by its assigned unique document identifier.
     *
     * @param userId calling user
     * @param documentIdentifier identifier to search for
     *
     * @return properties of the matching license type
     *
     * @throws InvalidParameterException documentIdentifier or userId is null; documentIdentifier is not recognized
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public LicenseTypeElement getLicenseTypeByDocId(String userId,
                                                    String documentIdentifier) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String   methodName = "getLicenseTypeByDocId";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/license-types/by-document-id/{2}";

        final String   documentIdParameterName = "documentId";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(documentIdentifier, documentIdParameterName, methodName);

        LicenseTypeResponse restResult = restClient.callLicenseTypeGetRESTCall(methodName,
                                                                               serverPlatformURLRoot + urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               documentIdentifier);

        return restResult.getElement();
    }


    /**
     * Retrieve all of the license types for a particular title.  The title can include regEx wildcards.
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
    public List<LicenseTypeElement> getLicenseTypesByTitle(String userId,
                                                           String title,
                                                           int    startFrom,
                                                           int    pageSize) throws UserNotAuthorizedException,
                                                                                   InvalidParameterException,
                                                                                   PropertyServerException
    {
        final String methodName = "getLicenseTypesByTitle";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/license-types/by-title?startFrom={2}&pageSize={3}";
        final String titleParameterName = "title";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(title, titleParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setSearchString(title);
        requestBody.setSearchStringParameterName(titleParameterName);

        LicenseTypeListResponse restResult = restClient.callLicenseTypeListPostRESTCall(methodName,
                                                                                        serverPlatformURLRoot + urlTemplate,
                                                                                        requestBody,
                                                                                        serverName,
                                                                                        userId,
                                                                                        startFrom,
                                                                                        queryPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve all of the license type definitions for a specific governance domain.
     *
     * @param userId calling user
     * @param domainIdentifier identifier to search for
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return properties of the matching license type definitions
     *
     * @throws InvalidParameterException domainIdentifier or userId is null; domainIdentifier is not recognized
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<LicenseTypeElement> getLicenseTypeByDomainId(String userId,
                                                             int    domainIdentifier,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "getLicenseTypeByDomainId";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/license-types/by-domain/{2}?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        LicenseTypeListResponse restResult = restClient.callLicenseTypeListGetRESTCall(methodName,
                                                                                       serverPlatformURLRoot + urlTemplate,
                                                                                       serverName,
                                                                                       userId,
                                                                                       domainIdentifier,
                                                                                       startFrom,
                                                                                       queryPageSize);

        return restResult.getElements();
    }


    /* =======================================
     * Licenses
     */

    /**
     * Link an element to a license type and include details of the license in the relationship properties.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element being certified
     * @param licenseTypeGUID unique identifier for the license type
     * @param properties the properties of the license
     *
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void licenseElement(String            userId,
                               String            elementGUID,
                               String            licenseTypeGUID,
                               LicenseProperties properties) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "certifyElement";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/license-types/{2}/elements/{3}/license";

        final String elementGUIDParameterName = "elementGUID";
        final String licenseTypeGUIDParameterName = "licenseTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(licenseTypeGUID, licenseTypeGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        properties,
                                        serverName,
                                        userId,
                                        licenseTypeGUID,
                                        elementGUID);
    }


    /**
     * Update the properties of a license.  Remember to include the licenseId in the properties if the element has multiple
     * licenses for the same license type.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element being certified
     * @param licenseTypeGUID unique identifier for the license type
     * @param isMergeUpdate should the supplied properties overlay the existing properties or replace them
     * @param properties the properties of the license
     *
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void updateLicense(String                  userId,
                              String                  elementGUID,
                              String                  licenseTypeGUID,
                              boolean                 isMergeUpdate,
                              LicenseProperties properties)  throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "updateLicense";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/license-types/{2}/elements/{3}/update?isMergeUpdate={4}";

        final String elementGUIDParameterName = "elementGUID";
        final String licenseTypeGUIDParameterName = "licenseTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(licenseTypeGUID, licenseTypeGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        properties,
                                        serverName,
                                        userId,
                                        licenseTypeGUID,
                                        elementGUID,
                                        isMergeUpdate);
    }


    /**
     * Remove the license for an element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element being certified
     * @param licenseTypeGUID unique identifier for the license type
     * @param certificateId optional unique identifier from the license authority - it is used to disambiguate the licenses for the element.
     *
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void unlicenseElement(String userId,
                                 String elementGUID,
                                 String licenseTypeGUID,
                                 String certificateId)  throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName = "decertifyElement";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/license-types/{2}/elements/{3}/unlicense";

        final String elementGUIDParameterName = "elementGUID";
        final String licenseTypeGUIDParameterName = "licenseTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(licenseTypeGUID, licenseTypeGUIDParameterName, methodName);

        CertificateIdRequestBody requestBody = new CertificateIdRequestBody();

        requestBody.setCertificateId(certificateId);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        licenseTypeGUID,
                                        elementGUID);
    }
}
