/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.client;

import org.odpi.openmetadata.accessservices.assetowner.api.AssetLicenseInterface;
import org.odpi.openmetadata.accessservices.assetowner.client.rest.AssetOwnerRESTClient;
import org.odpi.openmetadata.commonservices.ffdc.rest.LicenseTypeResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.LicenseTypesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.LicensesResponse;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.LicenseElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.LicenseTypeElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedElement;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LicenseProperties;

import java.util.List;

/**
 * AssetLicenseManager is the java client for managing license types and the licensing of elements.
 */
public class AssetLicenseManager extends AssetOwnerBaseClient implements AssetLicenseInterface
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     *
     * @throws InvalidParameterException bad input parameters
     */
    public AssetLicenseManager(String serverName,
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
    public AssetLicenseManager(String     serverName,
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
    public AssetLicenseManager(String   serverName,
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
    public AssetLicenseManager(String     serverName,
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
    public AssetLicenseManager(String               serverName,
                               String               serverPlatformURLRoot,
                               AssetOwnerRESTClient restClient,
                               int                  maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /* ========================================
     * License Types
     */


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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/license-types/{2}";

        final String guidParameterName = "licenseTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(licenseTypeGUID, guidParameterName, methodName);

        LicenseTypeResponse restResult = restClient.callLicenseTypeGetRESTCall(methodName,
                                                                               urlTemplate,
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
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/license-types/by-document-id/{2}";

        final String   documentIdParameterName = "documentId";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(documentIdentifier, documentIdParameterName, methodName);

        LicenseTypeResponse restResult = restClient.callLicenseTypeGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               documentIdentifier);

        return restResult.getElement();
    }


    /**
     * Retrieve all the license types for a particular title.  The title can include regEx wildcards.
     *
     * @param userId calling user
     * @param title short description of the license
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/license-types/by-title?startFrom={2}&pageSize={3}";
        final String titleParameterName = "title";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(title, titleParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setSearchString(title);
        requestBody.setSearchStringParameterName(titleParameterName);

        LicenseTypesResponse restResult = restClient.callLicenseTypeListPostRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     requestBody,
                                                                                     serverName,
                                                                                     userId,
                                                                                     startFrom,
                                                                                     queryPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve all the license type definitions for a specific governance domain.
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/license-types/by-domain/{2}?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        LicenseTypesResponse restResult = restClient.callLicenseTypesGetRESTCall(methodName,
                                                                                 urlTemplate,
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
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String licenseElement(String            userId,
                                 String            elementGUID,
                                 String            licenseTypeGUID,
                                 LicenseProperties properties) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "licenseElement";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/elements/{2}/license-types/{3}";

        final String elementGUIDParameterName = "elementGUID";
        final String licenseTypeGUIDParameterName = "licenseTypeGUID";

        return super.setupMultiLinkRelationship(userId,
                                                elementGUID,
                                                elementGUIDParameterName,
                                                null,
                                                properties,
                                                licenseTypeGUID,
                                                licenseTypeGUIDParameterName,
                                                urlTemplate,
                                                methodName);
    }


    /**
     * Update the properties of a license.
     *
     * @param userId calling user
     * @param licenseGUID unique identifier for the license relationship
     * @param isMergeUpdate should the supplied properties overlay the existing properties or replace them
     * @param properties the properties of the license
     *
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void updateLicense(String            userId,
                              String            licenseGUID,
                              boolean           isMergeUpdate,
                              LicenseProperties properties)  throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "updateLicense";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/licenses/{2}/update?isMergeUpdate={3}";

        final String licenseGUIDParameterName = "licenseGUID";

        super.updateRelationship(userId,
                                 licenseGUID,
                                 licenseGUIDParameterName,
                                 isMergeUpdate,
                                 null,
                                 properties,
                                 urlTemplate,
                                 methodName);
    }


    /**
     * Remove the license for an element.
     *
     * @param userId calling user
     * @param licenseGUID unique identifier for the license relationship
     *
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void unlicenseElement(String userId,
                                 String licenseGUID)  throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName = "unlicenseElement";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/licenses/{2}/delete";

        final String licenseGUIDParameterName = "licenseGUID";

        super.clearRelationship(userId,
                                licenseGUID,
                                licenseGUIDParameterName,
                                null,
                                urlTemplate,
                                methodName);
    }


    /**
     * Return information about the elements linked to a license.
     *
     * @param userId calling user
     * @param licenseGUID unique identifier for the license
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<RelatedElement> getLicensedElements(String userId,
                                                    String licenseGUID,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "getLicencedElements";
        final String guidParameter = "licenseGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/elements/licenses/{2}?&startFrom={3}&pageSize={4}";

        return super.getRelatedElements(userId, licenseGUID, guidParameter, urlTemplate, startFrom, pageSize, methodName);
    }


    /**
     * Return information about the licenses linked to an element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier for the license
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<LicenseElement> getLicenses(String userId,
                                            String elementGUID,
                                            int    startFrom,
                                            int    pageSize) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "getLicences";
        final String guidParameterName = "elementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/elements/{2}/licenses?&startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        LicensesResponse restResult = restClient.callLicensesGetRESTCall(methodName,
                                                                         urlTemplate,
                                                                         serverName,
                                                                         userId,
                                                                         elementGUID,
                                                                         startFrom,
                                                                         queryPageSize);

        return restResult.getElements();
    }
}
