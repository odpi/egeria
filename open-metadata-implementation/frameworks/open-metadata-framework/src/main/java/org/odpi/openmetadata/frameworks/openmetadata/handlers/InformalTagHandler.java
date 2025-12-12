/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.InformalTagProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * InformalTagHandler is the handler for managing informal tags.
 */
public class InformalTagHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public InformalTagHandler(String             localServerName,
                              AuditLog           auditLog,
                              String             serviceName,
                              OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.INFORMAL_TAG.typeName);
    }



    /**
     * Creates a new informal tag and returns the unique identifier for it.
     *
     * @param userId           userId of user making request.
     * @param requestedNewElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties       name of the tag and (optional) description.  Setting a description, particularly in a public tag
     * makes the tag more valuable to other users and can act as an embryonic note.
     *
     * @return GUID for new tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createInformalTag(String                                userId,
                                    NewElementOptions                     requestedNewElementOptions,
                                    Map<String, ClassificationProperties> initialClassifications,
                                    InformalTagProperties                 properties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "createInformalTag";
        final String propertiesParameterName = "properties";
        final String nameParameterName = "properties.name";

        propertyHelper.validateObject(properties, propertiesParameterName, methodName);
        propertyHelper.validateMandatoryName(properties.getDisplayName(), nameParameterName, methodName);

        NewElementOptions newElementOptions = new NewElementOptions(requestedNewElementOptions);

        newElementOptions.setInitialStatus(ElementStatus.ACTIVE);

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               OpenMetadataType.INFORMAL_TAG.typeName,
                                                               requestedNewElementOptions,
                                                               classificationBuilder.getInitialClassifications(initialClassifications),
                                                               elementBuilder.getNewElementProperties(properties),
                                                               null);
    }

    /**
     * Create a new metadata element to represent an informal tag using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new community.
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
    public String createTagFromTemplate(String                 userId,
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
     * Updates the description of an existing tag (either private or public).
     *
     * @param userId          userId of user making request.
     * @param tagGUID         unique identifier for the tag.
     * @param properties  description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     *
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateTagDescription(String                userId,
                                       String                tagGUID,
                                       UpdateOptions         updateOptions,
                                       InformalTagProperties properties) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        return openMetadataClient.updateMetadataElementInStore(userId,
                                                               tagGUID,
                                                               updateOptions,
                                                               elementBuilder.getNewElementProperties(properties));
    }


    /**
     * Removes a tag from the repository.
     * A private tag can be deleted by its creator and all the references are lost;
     * a public tag can be deleted by anyone, but only if it is not attached to any referenceable.
     *
     * @param userId    userId of user making request.
     * @param tagGUID   unique id for the tag.
     * @param deleteOptions options for a delete request
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   deleteTag(String        userId,
                            String        tagGUID,
                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        openMetadataClient.deleteMetadataElementInStore(userId, tagGUID, deleteOptions);
    }


    /**
     * Return the tag for the supplied unique identifier (guid).
     *
     * @param userId userId of the user making the request.
     * @param tagGUID unique identifier of the tag.
     * @param getOptions multiple options to control the query
     *
     * @return tag
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getTag(String     userId,
                                          String     tagGUID,
                                          GetOptions getOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName = "getTag";

        return super.getRootElementByGUID(userId, tagGUID, getOptions, methodName);
    }


    /**
     * Return the list of tags exactly matching the supplied name.
     *
     * @param userId the name of the calling user.
     * @param tag name of tag.
     * @param queryOptions multiple options to control the query
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getTagsByName(String       userId,
                                                       String       tag,
                                                       QueryOptions queryOptions) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName = "getTagsByName";
        final String nameParameterName = "tag";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(tag, nameParameterName, methodName);
        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        List<String> propertyNames = Collections.singletonList(OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId,
                                           tag,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Return the list of tags containing the supplied string in either the name or description.
     *
     * @param userId the name of the calling user.
     * @param tag name of tag.  This may include wild card characters.
     * @param searchOptions multiple options to control the query
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> findTags(String        userId,
                                                  String        tag,
                                                  SearchOptions searchOptions) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName = "findTags";

        return super.findRootElements(userId, tag, searchOptions, methodName);
    }


    /**
     * Return the list of the tags created by the calling user containing the supplied string in either the name or description.
     *
     * @param userId the name of the calling user.
     * @param tag name of tag.  This may include wild card characters.
     * @param searchOptions multiple options to control the query
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> findMyTags(String        userId,
                                                    String        tag,
                                                    SearchOptions searchOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        List<OpenMetadataRootElement> allMatchingTags = this.findTags(userId, tag, searchOptions);

        if (allMatchingTags != null)
        {
            List<OpenMetadataRootElement> myTags = new ArrayList<>();

            for (OpenMetadataRootElement tagElement : allMatchingTags)
            {
                if ((tagElement == null) || (userId.equals(tagElement.getElementHeader().getVersions().getCreatedBy())))
                {
                    myTags.add(tagElement);
                }
            }

            return myTags;
        }

        return null;
    }


    /**
     * Adds a tag (either private or public) to an element.
     *
     * @param userId           userId of user making request.
     * @param elementGUID        unique id for the element.
     * @param tagGUID          unique id of the tag.
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   addTagToElement(String                userId,
                                  String                elementGUID,
                                  String                tagGUID,
                                  MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        tagGUID,
                                                        new MakeAnchorOptions(metadataSourceOptions),
                                                        null);
    }


    /**
     * Removes a tag from the element that was added by this user.
     *
     * @param userId    userId of user making request.
     * @param elementGUID unique id for the element.
     * @param tagGUID   unique id for the tag.
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   removeTagFromElement(String        userId,
                                       String        elementGUID,
                                       String        tagGUID,
                                       DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        OpenMetadataRelationshipList relationships = openMetadataClient.getMetadataElementRelationships(userId,
                                                                                                        elementGUID,
                                                                                                        tagGUID,
                                                                                                        OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName,
                                                                                                        new QueryOptions(deleteOptions));

        if (relationships != null)
        {
            for (OpenMetadataRelationship relationship : relationships.getElementList())
            {
                if (relationship != null)
                {
                    openMetadataClient.deleteRelationshipInStore(userId, relationship.getRelationshipGUID(), deleteOptions);
                }
            }
        }
    }


    /**
     * Return the list of unique identifiers for elements that are linked to a specific tag either directly, or via one
     * of its schema elements.  An Element's GUID may appear multiple times in the results if it is tagged multiple times
     * with the requested tag.
     *
     * @param userId the name of the calling user.
     * @param tagGUID unique identifier of tag.
     * @param queryOptions multiple options to control the query
     *
     * @return element guid list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getElementsByTag(String       userId,
                                                          String       tagGUID,
                                                          QueryOptions queryOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName = "getAttachedTags";
        final String guidParameterName = "tagGUID";

        return super.getRelatedRootElements(userId,
                                            tagGUID,
                                            guidParameterName,
                                            2,
                                            OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName,
                                            OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                            queryOptions,
                                            methodName);
    }



    /**
     * Return the informal tags attached to an element.
     *
     * @param userId       userId of user making request.
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param queryOptions multiple options to control the query
     * @return list of tags
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public List<OpenMetadataRootElement>  getAttachedTags(String       userId,
                                                          String       elementGUID,
                                                          QueryOptions queryOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName = "getAttachedTags";
        final String guidParameterName = "elementGUID";

        return super.getRelatedRootElements(userId,
                                            elementGUID,
                                            guidParameterName,
                                            1,
                                            OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName,
                                            OpenMetadataType.INFORMAL_TAG.typeName,
                                            queryOptions,
                                            methodName);
    }
}
