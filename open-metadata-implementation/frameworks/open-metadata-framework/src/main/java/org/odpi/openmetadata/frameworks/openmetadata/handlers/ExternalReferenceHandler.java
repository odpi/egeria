/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ExternalReferenceHandler provides methods to define external references and their relationships.
 */
public class ExternalReferenceHandler extends OpenMetadataHandlerBase
{

    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     */
    public ExternalReferenceHandler(String             localServerName,
                                    AuditLog           auditLog,
                                    String             localServiceName,
                                    OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.EXTERNAL_REFERENCE.typeName);
    }


    /**
     * Create a new external reference.
     *
     * @param userId                       userId of the user making the request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createExternalReference(String                                userId,
                                          NewElementOptions                     newElementOptions,
                                          Map<String, ClassificationProperties> initialClassifications,
                                          ExternalReferenceProperties properties,
                                          RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                     PropertyServerException,
                                                                                                                     UserNotAuthorizedException
    {
        final String methodName = "createExternalReference";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent an external reference using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new external reference.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing element to copy
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param replacementClassifications map of classification names to classification properties to include in the entity creation request. These override the template values.
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createExternalReferenceFromTemplate(String                                userId,
                                                      TemplateOptions                       templateOptions,
                                                      String                                templateGUID,
                                                      EntityProperties                      replacementProperties,
                                                      Map<String, ClassificationProperties> replacementClassifications,
                                                      Map<String, String>                   placeholderProperties,
                                                      RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                                 UserNotAuthorizedException,
                                                                                                                                 PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               replacementClassifications,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * Update the properties of an external reference.
     *
     * @param userId                 userId of the user making the request.
     * @param externalReferenceGUID       unique identifier of the external reference (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateExternalReference(String                      userId,
                                           String                      externalReferenceGUID,
                                           UpdateOptions               updateOptions,
                                           ExternalReferenceProperties properties) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName        = "updateExternalReference";
        final String guidParameterName = "externalReferenceGUID";

        return super.updateElement(userId,
                                   externalReferenceGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Attach an external reference to an element.
     *
     * @param userId                 userId of the user making the request
     * @param elementGUID          unique identifier of the element
     * @param externalReferenceGUID          unique identifier of the external reference
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @return unique identifier of the new relationship
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String linkExternalReference(String                          userId,
                                      String                          elementGUID,
                                      String                          externalReferenceGUID,
                                      MakeAnchorOptions               makeAnchorOptions,
                                      ExternalReferenceLinkProperties relationshipProperties) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String methodName            = "linkExternalReference";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "externalReferenceGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(externalReferenceGUID, end2GUIDParameterName, methodName);

        return openMetadataClient.createRelatedElementsInStore(userId,
                                                               OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName,
                                                               elementGUID,
                                                               externalReferenceGUID,
                                                               makeAnchorOptions,
                                                               relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Update the properties of a external reference link relationship.
     *
     * @param userId                 userId of the user making the request
     * @param externalReferenceLinkRelationshipGUID unique identifier of the relationship
     * @param updateOptions provides a structure for the additional options when updating a relationship.
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateExternalReferenceLink(String                           userId,
                                            String                           externalReferenceLinkRelationshipGUID,
                                            UpdateOptions                    updateOptions,
                                            ExternalReferenceLinkProperties  relationshipProperties) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        final String methodName        = "updateExternalReferenceLink";
        final String guidParameterName = "externalReferenceLinkRelationshipGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(externalReferenceLinkRelationshipGUID, guidParameterName, methodName);

        openMetadataClient.updateRelationshipInStore(userId,
                                                     externalReferenceLinkRelationshipGUID,
                                                     updateOptions,
                                                     relationshipBuilder.getElementProperties(relationshipProperties));
    }


    /**
     * Remove a external reference link relationship.
     *
     * @param userId                 userId of the user making the request.
     * @param externalReferenceLinkRelationshipGUID unique identifier of the relationship
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachExternalReference(String        userId,
                                        String        externalReferenceLinkRelationshipGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName        = "detachExternalReference";
        final String guidParameterName = "externalReferenceLinkRelationshipGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(externalReferenceLinkRelationshipGUID, guidParameterName, methodName);

        openMetadataClient.deleteRelationshipInStore(userId, externalReferenceLinkRelationshipGUID, deleteOptions);
    }


    /**
     * Attach an external media reference to an element.
     *
     * @param userId                 userId of the user making the request
     * @param elementGUID          unique identifier of the first external reference
     * @param externalReferenceGUID          unique identifier of the second external reference
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @return unique identifier of the new relationship
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String linkMediaReference(String                   userId,
                                   String                   elementGUID,
                                   String                   externalReferenceGUID,
                                   MakeAnchorOptions        makeAnchorOptions,
                                   MediaReferenceProperties relationshipProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName            = "linkMediaReference";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "externalReferenceGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(externalReferenceGUID, end2GUIDParameterName, methodName);

        return openMetadataClient.createRelatedElementsInStore(userId,
                                                               OpenMetadataType.MEDIA_REFERENCE_RELATIONSHIP.typeName,
                                                               elementGUID,
                                                               externalReferenceGUID,
                                                               makeAnchorOptions,
                                                               relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Update the properties of a media reference relationship.
     *
     * @param userId                 userId of the user making the request
     * @param mediaReferenceRelationshipGUID unique identifier of the relationship
     * @param updateOptions provides a structure for the additional options when updating a relationship.
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateMediaReference(String                    userId,
                                     String                    mediaReferenceRelationshipGUID,
                                     UpdateOptions             updateOptions,
                                     MediaReferenceProperties  relationshipProperties) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName        = "updateMediaReference";
        final String guidParameterName = "mediaReferenceRelationshipGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(mediaReferenceRelationshipGUID, guidParameterName, methodName);

        openMetadataClient.updateRelationshipInStore(userId,
                                                     mediaReferenceRelationshipGUID,
                                                     updateOptions,
                                                     relationshipBuilder.getElementProperties(relationshipProperties));
    }


    /**
     * Remove a media reference relationship.
     *
     * @param userId                 userId of the user making the request.
     * @param mediaReferenceRelationshipGUID unique identifier of the relationship
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachMediaReference(String        userId,
                                     String        mediaReferenceRelationshipGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName        = "detachMediaReference";
        final String guidParameterName = "mediaReferenceRelationshipGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(mediaReferenceRelationshipGUID, guidParameterName, methodName);

        openMetadataClient.deleteRelationshipInStore(userId, mediaReferenceRelationshipGUID, deleteOptions);
    }


    /**
     * Attach an element to its external document reference.
     *
     * @param userId                 userId of the user making the request
     * @param elementGUID       unique identifier of the element to connect
     * @param externalReferenceGUID            unique identifier of the external reference
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @return unique identifier of the new relationship
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String linkCitedDocumentReference(String                      userId,
                                           String                      elementGUID,
                                           String                      externalReferenceGUID,
                                           MakeAnchorOptions           makeAnchorOptions,
                                           CitedDocumentLinkProperties relationshipProperties) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        final String methodName            = "linkCitedDocumentReference";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "externalReferenceGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(externalReferenceGUID, end2GUIDParameterName, methodName);

        return openMetadataClient.createRelatedElementsInStore(userId,
                                                               OpenMetadataType.CITED_DOCUMENT_LINK_RELATIONSHIP.typeName,
                                                               elementGUID,
                                                               externalReferenceGUID,
                                                               makeAnchorOptions,
                                                               relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Update the properties of a cited document link relationship.
     *
     * @param userId                 userId of the user making the request
     * @param citedDocumentLinkRelationshipGUID unique identifier of the relationship
     * @param updateOptions provides a structure for the additional options when updating a relationship.
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateCitedDocumentReference(String                       userId,
                                             String                       citedDocumentLinkRelationshipGUID,
                                             UpdateOptions                updateOptions,
                                             CitedDocumentLinkProperties  relationshipProperties) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String methodName        = "updateCitedDocumentReference";
        final String guidParameterName = "citedDocumentLinkRelationshipGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(citedDocumentLinkRelationshipGUID, guidParameterName, methodName);

        openMetadataClient.updateRelationshipInStore(userId,
                                                     citedDocumentLinkRelationshipGUID,
                                                     updateOptions,
                                                     relationshipBuilder.getElementProperties(relationshipProperties));
    }


    /**
     * Remove a cited document link relationship.
     *
     * @param userId                 userId of the user making the request.
     * @param citedDocumentLinkRelationshipGUID unique identifier of the relationship
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachCitedDocumentReference(String        userId,
                                             String        citedDocumentLinkRelationshipGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName        = "detachCitedDocumentReference";
        final String guidParameterName = "citedDocumentLinkRelationshipGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(citedDocumentLinkRelationshipGUID, guidParameterName, methodName);

        openMetadataClient.deleteRelationshipInStore(userId, citedDocumentLinkRelationshipGUID, deleteOptions);
    }


    /**
     * Delete an external reference.
     *
     * @param userId                 userId of the user making the request.
     * @param externalReferenceGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteExternalReference(String        userId,
                                        String        externalReferenceGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName        = "deleteExternalReference";
        final String guidParameterName = "externalReferenceGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(externalReferenceGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, externalReferenceGUID, deleteOptions);
    }


    /**
     * Returns the list of external references with a particular name.
     *
     * @param userId                 userId of the user making the request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getExternalReferencesByName(String       userId,
                                                                     String       name,
                                                                     QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String methodName = "getExternalReferencesByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.REFERENCE_TITLE.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Return the properties of a specific external reference.
     *
     * @param userId                 userId of the user making the request
     * @param externalReferenceGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getExternalReferenceByGUID(String     userId,
                                                              String     externalReferenceGUID,
                                                              GetOptions getOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName = "getExternalReferenceByGUID";

        return super.getRootElementByGUID(userId,
                                          externalReferenceGUID,
                                          getOptions,
                                          methodName);
    }



    /**
     * Retrieve the list of external references metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned external references include a list of the components that are associated with it.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findExternalReferences(String        userId,
                                                                String        searchString,
                                                                SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName  = "findExternalReferences";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }

    /**
     * Detach an element from its external document reference.
     *
     * @param userId                 userId of the user making the request.
     * @param elementGUID              unique identifier of the element
     * @param externalReferenceGUID          unique identifier of the external reference
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     *
     * This is a multi-link relationship, so this request removes every cited document link relationship
     * between the two elements.  Use the request that takes the relationship's own unique identifier to
     * remove just one of them.
     */
    public void detachCitedDocumentReference(String        userId,
                                             String        elementGUID,
                                             String        externalReferenceGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName = "detachKnownExternalReference";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "externalReferenceGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(externalReferenceGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.CITED_DOCUMENT_LINK_RELATIONSHIP.typeName,
                                                        externalReferenceGUID,
                                                        externalReferenceGUID,
                                                        deleteOptions);
    }

    /**
     * Detach an external reference from an element.
     *
     * @param userId                 userId of the user making the request.
     * @param elementGUID          unique identifier of the first external reference
     * @param externalReferenceGUID          unique identifier of the second external reference
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     *
     * This is a multi-link relationship, so this request removes every external reference link relationship
     * between the two elements.  Use the request that takes the relationship's own unique identifier to
     * remove just one of them.
     */
    public void detachExternalReference(String        userId,
                                        String        elementGUID,
                                        String        externalReferenceGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName = "detachExternalReference";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "externalReferenceGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(externalReferenceGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        externalReferenceGUID,
                                                        deleteOptions);
    }

    /**
     * Detach an external media reference from an element.
     *
     * @param userId                 userId of the user making the request.
     * @param elementGUID          unique identifier of the first external reference
     * @param externalReferenceGUID          unique identifier of the second external reference
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     *
     * This is a multi-link relationship, so this request removes every media reference relationship
     * between the two elements.  Use the request that takes the relationship's own unique identifier to
     * remove just one of them.
     */
    public void detachMediaReference(String        userId,
                                     String        elementGUID,
                                     String        externalReferenceGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "detachMediaReference";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "externalReferenceGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(externalReferenceGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.MEDIA_REFERENCE_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        externalReferenceGUID,
                                                        deleteOptions);
    }
}
