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
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceZoneProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ZoneHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Maintain and query governance zone definitions.
 */
public class GovernanceZoneHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public GovernanceZoneHandler(String             localServerName,
                                 AuditLog           auditLog,
                                 String             serviceName,
                                 OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.GOVERNANCE_ZONE.typeName);
    }


    /**
     * Create a new governance zone.
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
    public String createGovernanceZone(String                                userId,
                                       NewElementOptions                     newElementOptions,
                                       Map<String, ClassificationProperties> initialClassifications,
                                       GovernanceZoneProperties              properties,
                                       RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                  PropertyServerException,
                                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "createGovernanceZone";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a governance zone using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new governance zone.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc).
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
    public String createGovernanceZoneFromTemplate(String                 userId,
                                                   TemplateOptions templateOptions,
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
     * Update the properties of a governance zone.
     *
     * @param userId                 userId of user making request.
     * @param governanceZoneGUID      unique identifier of the governance zone (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateGovernanceZone(String                   userId,
                                     String                   governanceZoneGUID,
                                     UpdateOptions            updateOptions,
                                     GovernanceZoneProperties properties) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName = "updateGovernanceZone";
        final String guidParameterName = "governanceZoneGUID";

        super.updateElement(userId,
                            governanceZoneGUID,
                            guidParameterName,
                            updateOptions,
                            properties,
                            methodName);
    }


    /**
     * Attach a profile to a governance zone.
     *
     * @param userId                  userId of user making request
     * @param governanceZoneGUID        unique identifier of the parent
     * @param nestedGovernanceZoneGUID             unique identifier of the actor profile
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkGovernanceZones(String                  userId,
                                    String                  governanceZoneGUID,
                                    String                  nestedGovernanceZoneGUID,
                                    MetadataSourceOptions   metadataSourceOptions,
                                    ZoneHierarchyProperties relationshipProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "linkGovernanceZones";
        final String end1GUIDParameterName = "governanceZoneGUID";
        final String end2GUIDParameterName = "nestedGovernanceZoneGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceZoneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(nestedGovernanceZoneGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.ZONE_HIERARCHY_RELATIONSHIP.typeName,
                                                        governanceZoneGUID,
                                                        nestedGovernanceZoneGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an actor profile from a governance zone.
     *
     * @param userId                 userId of user making request.
     * @param governanceZoneGUID       unique identifier of the parent actor profile
     * @param nestedGovernanceZoneGUID            unique identifier of the nested actor profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachGovernanceZones(String        userId,
                                      String        governanceZoneGUID,
                                      String        nestedGovernanceZoneGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "detachGovernanceZones";

        final String end1GUIDParameterName = "governanceZoneGUID";
        final String end2GUIDParameterName = "nestedGovernanceZoneGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceZoneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(nestedGovernanceZoneGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.ZONE_HIERARCHY_RELATIONSHIP.typeName,
                                                        governanceZoneGUID,
                                                        nestedGovernanceZoneGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a governance zone.
     *
     * @param userId                 userId of user making request.
     * @param governanceZoneGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteGovernanceZone(String        userId,
                                     String        governanceZoneGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "deleteGovernanceZone";
        final String guidParameterName = "governanceZoneGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceZoneGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, governanceZoneGUID, deleteOptions);
    }


    /**
     * Returns the list of governance zones with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getGovernanceZonesByName(String       userId,
                                                                  String       name,
                                                                  QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String methodName = "getGovernanceZonesByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                   OpenMetadataProperty.ZONE_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific governance zone.
     *
     * @param userId                 userId of user making request
     * @param governanceZoneGUID      unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getGovernanceZoneByGUID(String     userId,
                                                           String     governanceZoneGUID,
                                                           GetOptions getOptions) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName = "getGovernanceZoneByGUID";

        return super.getRootElementByGUID(userId, governanceZoneGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of governance zones metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findGovernanceZones(String        userId,
                                                             String        searchString,
                                                             SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName = "findGovernanceZones";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
