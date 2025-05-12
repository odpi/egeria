/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.accessservices.governanceprogram.api.SubjectAreasInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.client.rest.GovernanceProgramRESTClient;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;

import java.util.List;


/**
 * The SubjectAreasInterface is used by the governance team to define the subject area for topic related governance definitions.
 */
public class SubjectAreaManager extends GovernanceProgramBaseClient implements SubjectAreasInterface
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public SubjectAreaManager(String serverName,
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
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public SubjectAreaManager(String serverName,
                              String serverPlatformURLRoot,
                              String userId,
                              String password) throws InvalidParameterException
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
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public SubjectAreaManager(String   serverName,
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
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public SubjectAreaManager(String   serverName,
                              String   serverPlatformURLRoot,
                              String   userId,
                              String   password,
                              int      maxPageSize,
                              AuditLog auditLog) throws InvalidParameterException
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
    public SubjectAreaManager(String                      serverName,
                              String                      serverPlatformURLRoot,
                              GovernanceProgramRESTClient restClient,
                              int                         maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /**
     * Create a definition of a subject area.
     *
     * @param userId calling user
     * @param properties properties for a subject area
     *
     * @return unique identifier of subject area
     *
     * @throws InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String createSubjectArea(String                userId,
                                    SubjectAreaProperties properties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "createSubjectArea";

        final String propertiesParameter = "properties";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas";

        return super.createReferenceable(userId, properties, propertiesParameter, urlTemplate, methodName);
    }


    /**
     * Update the definition of a subject area.
     *
     * @param userId calling user
     * @param subjectAreaGUID unique identifier of subject area
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     *
     * @throws InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void updateSubjectArea(String                userId,
                                  String                subjectAreaGUID,
                                  boolean               isMergeUpdate,
                                  SubjectAreaProperties properties) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "updateSubjectArea";

        final String guidParameter = "subjectAreaGUID";
        final String propertiesParameter = "properties";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas/{2}?isMergeUpdate={3}";

        super.updateReferenceable(userId, subjectAreaGUID, guidParameter, isMergeUpdate, properties, propertiesParameter, urlTemplate, methodName);
    }


    /**
     * Remove the definition of a subject area.
     *
     * @param userId calling user
     * @param subjectAreaGUID unique identifier of subject area
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void deleteSubjectArea(String userId,
                                  String subjectAreaGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName = "deleteSubjectArea";

        final String guidParameter = "subjectAreaGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas/{2}/delete}";

        super.removeReferenceable(userId, subjectAreaGUID, guidParameter, urlTemplate, methodName);
    }


    /**
     * Link two related subject areas together as part of a hierarchy.
     * A subject area can only have one parent but many child subject areas.
     *
     * @param userId calling user
     * @param parentSubjectAreaGUID unique identifier of the parent subject area
     * @param childSubjectAreaGUID unique identifier of the child subject area
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void linkSubjectAreasInHierarchy(String userId,
                                            String parentSubjectAreaGUID,
                                            String childSubjectAreaGUID) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "linkSubjectAreasInHierarchy";

        final String parentSubjectAreaGUIDParameterName = "parentSubjectAreaGUID";
        final String childSubjectAreaGUIDParameterName = "childSubjectAreaGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas/{2}/nested-subject-area/{3}/link";

        super.setupRelationship(userId,
                                parentSubjectAreaGUID,
                                parentSubjectAreaGUIDParameterName,
                                null,
                                null,
                                childSubjectAreaGUID,
                                childSubjectAreaGUIDParameterName,
                                urlTemplate,
                                methodName);
    }


    /**
     * Remove the link between two subject areas in the subject area hierarchy.
     *
     * @param userId calling user
     * @param parentSubjectAreaGUID unique identifier of the parent subject area
     * @param childSubjectAreaGUID unique identifier of the child subject area
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void unlinkSubjectAreasInHierarchy(String userId,
                                              String parentSubjectAreaGUID,
                                              String childSubjectAreaGUID) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "unlinkSubjectAreasInHierarchy";

        final String parentSubjectAreaGUIDParameterName = "parentSubjectAreaGUID";
        final String childSubjectAreaGUIDParameterName = "childSubjectAreaGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas/{2}/nested-subject-area/{3}/unlink";

        super.clearRelationship(userId,
                                parentSubjectAreaGUID,
                                parentSubjectAreaGUIDParameterName,
                                null,
                                childSubjectAreaGUID,
                                childSubjectAreaGUIDParameterName,
                                urlTemplate,
                                methodName);
    }


    /**
     * Return information about a specific subject area.
     *
     * @param userId calling user
     * @param subjectAreaGUID unique identifier for the subject area
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException subjectAreaGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public SubjectAreaElement getSubjectAreaByGUID(String userId,
                                                   String subjectAreaGUID) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "getSubjectAreaByGUID";

        final String guidParameter = "subjectAreaGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(subjectAreaGUID, guidParameter, methodName);

        SubjectAreaResponse restResult = restClient.callSubjectAreaGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               subjectAreaGUID);
        return restResult.getElement();
    }


    /**
     * Return information about a specific subject area.
     *
     * @param userId calling user
     * @param qualifiedName unique name for the subject area
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public SubjectAreaElement getSubjectAreaByName(String userId,
                                                   String qualifiedName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "getSubjectAreaByName";

        final String qualifiedNameParameter = "qualifiedName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas/name/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        SubjectAreaResponse restResult = restClient.callSubjectAreaGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               qualifiedName);

        return restResult.getElement();
    }


    /**
     * Return information about the defined subject areas.
     *
     * @param userId calling user
     * @param domainIdentifier identifier for the desired governance domain - zero for all
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
    public List<SubjectAreaElement> getSubjectAreasForDomain(String userId,
                                                             int    domainIdentifier,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "getSubjectAreasForDomain";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas/for-domain?domainIdentifier={2}&startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SubjectAreasResponse restResult = restClient.callSubjectAreaListGetRESTCall(methodName,
                                                                                    urlTemplate,
                                                                                    serverName,
                                                                                    userId,
                                                                                    domainIdentifier,
                                                                                    startFrom,
                                                                                    queryPageSize);

        return restResult.getElements();
    }


    /**
     * Return information about a specific subject area and its linked governance definitions.
     *
     * @param userId calling user
     * @param subjectAreaGUID unique identifier for the subject area
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException subjectAreaGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public SubjectAreaDefinition getSubjectAreaDefinitionByGUID(String userId,
                                                                String subjectAreaGUID) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "getSubjectAreaDefinitionByGUID";
        final String guidParameter = "subjectAreaGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas/{2}/with-definitions";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(subjectAreaGUID, guidParameter, methodName);

        SubjectAreaDefinitionResponse restResult = restClient.callSubjectAreaDefinitionGetRESTCall(methodName,
                                                                                                   urlTemplate,
                                                                                                   serverName,
                                                                                                   userId,
                                                                                                   subjectAreaGUID);
        return restResult.getProperties();
    }


    /**
     * Add a subject area classification to a referenceable element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier for the element
     * @param properties identifier for a subject area
     *
     * @throws InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void addSubjectAreaMemberClassification(String                              userId,
                                                   String                              elementGUID,
                                                   SubjectAreaClassificationProperties properties) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        final String methodName = "addSubjectAreaMemberClassification";

        final String guidParameter = "elementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/elements/{2}/subject-area";

        super.setReferenceableClassification(userId, elementGUID, guidParameter, properties, urlTemplate, methodName);
    }


    /**
     * Remove a subject area classification from a referenceable.
     *
     * @param userId calling user
     * @param elementGUID unique identifier for the element
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void deleteSubjectAreaMemberClassification(String userId,
                                                      String elementGUID) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "deleteSubjectAreaMemberClassification";
        final String guidParameter = "elementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/elements/{2}/subject-area/delete";

        super.removeReferenceableClassification(userId, elementGUID, guidParameter, urlTemplate, methodName);
    }


    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param userId calling user
     * @param subjectAreaName unique identifier for the subject area
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the subject area members
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ElementStub> getMembersOfSubjectArea(String userId,
                                                     String subjectAreaName,
                                                     int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "getMembersOfSubjectArea";
        final String nameParameter = "subjectAreaName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas/{2}/members?startFrom={3}&pageSize={4}";

        return super.getElementStubsByName(userId, subjectAreaName, nameParameter, urlTemplate, startFrom, pageSize, methodName);
    }
}
