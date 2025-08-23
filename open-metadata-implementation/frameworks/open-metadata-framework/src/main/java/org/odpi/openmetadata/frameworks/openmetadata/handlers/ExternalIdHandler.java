/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.ExternalIdMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ExternalIdElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

/**
 * ExternalIdHandler describes how to maintain and query external identifiers.
 */
public class ExternalIdHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public ExternalIdHandler(String             localServerName,
                             AuditLog           auditLog,
                             String             serviceName,
                             OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName,openMetadataClient, OpenMetadataType.EXTERNAL_ID.typeName);
    }


    /**
     * Create a new externalId.  It should be anchored to its scope (external source GUID)
     *
     * @param userId                       userId of user making request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createExternalId(String                                userId,
                                   NewElementOptions                     newElementOptions,
                                   Map<String, ClassificationProperties> initialClassifications,
                                   ExternalIdProperties                  properties,
                                   RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        final String methodName = "createExternalId";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent an externalId using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new externalId.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createExternalIdFromTemplate(String                 userId,
                                               TemplateOptions        templateOptions,
                                               String                 templateGUID,
                                               ElementProperties      replacementProperties,
                                               Map<String, String>    placeholderProperties,
                                               RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * Update the properties of an externalId.
     *
     * @param userId                 userId of user making request.
     * @param externalIdGUID      unique identifier of the externalId (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateExternalId(String             userId,
                                 String             externalIdGUID,
                                 UpdateOptions      updateOptions,
                                 ExternalIdProperties properties) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "updateExternalId";
        final String guidParameterName = "externalIdGUID";

        super.updateElement(userId,
                            externalIdGUID,
                            guidParameterName,
                            updateOptions,
                            properties,
                            methodName);
    }


    /**
     * Attach an externalId to an element.
     *
     * @param userId                  userId of user making request
     * @param externalIdGUID            unique identifier of the externalId
     * @param elementGUID             unique identifier of the element
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkExternalIdToElement(String                   userId,
                                        String                   elementGUID,
                                        String                   externalIdGUID,
                                        MetadataSourceOptions    metadataSourceOptions,
                                        ExternalIdLinkProperties relationshipProperties) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName = "linkExternalIdToElement";
        final String end2GUIDParameterName = "externalIdGUID";
        final String end1GUIDParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(externalIdGUID, end2GUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        externalIdGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an externalId from an element.
     *
     * @param userId                 userId of user making request.
     * @param elementGUID            unique identifier of the infrastructure asset
     * @param externalIdGUID       unique identifier of the externalId
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachExternalIdFromElement(String        userId,
                                            String        elementGUID,
                                            String        externalIdGUID,
                                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "detachExternalIdFromITAsset";

        final String end2GUIDParameterName = "externalIdGUID";
        final String end1GUIDParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(externalIdGUID, end2GUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        externalIdGUID,
                                                        deleteOptions);
    }


    /**
     * Delete an externalId.
     *
     * @param userId                 userId of user making request.
     * @param externalIdGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteExternalId(String        userId,
                                 String        externalIdGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName = "deleteExternalId";
        final String guidParameterName = "externalIdGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(externalIdGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, externalIdGUID, deleteOptions);
    }


    /**
     * Returns the list of externalIds with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getExternalIdsByName(String       userId,
                                                              String       name,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName = "getExternalIdsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.USER_ID.name,
                                                   OpenMetadataProperty.NETWORK_ADDRESS.name,
                                                   OpenMetadataProperty.DISTINGUISHED_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific externalId.
     *
     * @param userId                 userId of user making request
     * @param externalIdGUID      unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getExternalIdByGUID(String     userId,
                                                       String     externalIdGUID,
                                                       GetOptions getOptions) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName = "getExternalIdByGUID";

        return super.getRootElementByGUID(userId, externalIdGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of externalId metadata elements with a matching networkAddress.
     * There are no wildcards supported on this request.
     *
     * @param userId         userId of user making request
     * @param networkAddress networkAddress to search for
     * @param queryOptions           multiple options to control the query
     *
     * @return a list of elements
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getExternalIdsByNetworkAddress(String       userId,
                                                                        String       networkAddress,
                                                                        QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        final String methodName = "getExternalIdsByNetworkAddress";
        final String nameParameterName = "networkAddress";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(networkAddress, nameParameterName, methodName);

        List<String> propertyNames = Collections.singletonList(OpenMetadataProperty.NETWORK_ADDRESS.name);

        return super.getRootElementsByName(userId, networkAddress, propertyNames, queryOptions, methodName);
    }


    /**
     * Retrieve the list of externalId metadata elements that are attached to a specific infrastructure element.
     *
     * @param userId         userId of user making request
     * @param infrastructureGUID element to search for
     * @param queryOptions multiple options to control the query
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> getExternalIdsForInfrastructure(String       userId,
                                                                         String       infrastructureGUID,
                                                                         QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String methodName = "getExternalIdsForInfrastructure";
        final String parentGUIDParameterName = "infrastructureGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(infrastructureGUID, parentGUIDParameterName, methodName);

        return super.getRelatedRootElements(userId,
                                            infrastructureGUID,
                                            parentGUIDParameterName,
                                            1,
                                            OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName,
                                            queryOptions,
                                            methodName);
    }


    /**
     * Retrieve the list of externalIds metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findExternalIds(String        userId,
                                                         String        searchString,
                                                         SearchOptions searchOptions) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "findExternalIds";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }


    /**
     * Add relevant relationships and mermaid graph to the returned element.
     *
     * @param rootElement new root element
     * @return root element with graph
     */
    protected OpenMetadataRootElement addMermaidToRootElement(OpenMetadataRootElement rootElement)
    {
        if (rootElement != null)
        {
            ExternalIdElement externalIdElement = new ExternalIdElement(rootElement);

            List<RelatedMetadataElementSummary> deployedAPIs = new ArrayList<>();
            List<RelatedMetadataElementSummary> connections  = new ArrayList<>();
            List<RelatedMetadataElementSummary> visibleInNetworks = new ArrayList<>();
            List<RelatedMetadataElementSummary> others                = new ArrayList<>();

            if (externalIdElement.getOtherRelatedElements() != null)
            {
                for (RelatedMetadataElementSummary relatedMetadataElement : externalIdElement.getOtherRelatedElements())
                {
                    if (relatedMetadataElement != null)
                    {
                        if ((propertyHelper.isTypeOf(relatedMetadataElement.getRelationshipHeader(), OpenMetadataType.API_ENDPOINT_RELATIONSHIP.typeName) && (relatedMetadataElement.getRelatedElementAtEnd1())))
                        {
                            deployedAPIs.add(relatedMetadataElement);
                        }
                        else
                        {
                            others.add(relatedMetadataElement);
                        }
                    }
                }
            }

            if (! deployedAPIs.isEmpty())
            {
                externalIdElement.setExternalIdScope(deployedAPIs);
            }
            if (! others.isEmpty())
            {
                externalIdElement.setOtherRelatedElements(others);
            }
            else
            {
                externalIdElement.setOtherRelatedElements(null);
            }

            ExternalIdMermaidGraphBuilder graphBuilder = new ExternalIdMermaidGraphBuilder(externalIdElement);

            externalIdElement.setMermaidGraph(graphBuilder.getMermaidGraph());

            return externalIdElement;
        }

        return null;
    }
}
