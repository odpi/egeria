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
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ConceptModelElementHandler provides methods to define the elements of a concept model - the concept beads,
 * their attributes and the relationships between them - along with the link from a concept model to the
 * elements whose concepts it describes.
 */
public class ConceptModelElementHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     */
    public ConceptModelElementHandler(String             localServerName,
                                      AuditLog           auditLog,
                                      String             localServiceName,
                                      OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.CONCEPT_MODEL_ELEMENT.typeName);
    }


    /**
     * Create a new concept model element.  The subtype (for example ConceptBead or ConceptBeadAttribute) is
     * taken from the type name carried by the supplied properties.
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
    public String createConceptModelElement(String                                userId,
                                            NewElementOptions                     newElementOptions,
                                            Map<String, ClassificationProperties> initialClassifications,
                                            ConceptModelElementProperties         properties,
                                            RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                       PropertyServerException,
                                                                                                                       UserNotAuthorizedException
    {
        final String methodName = "createConceptModelElement";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a concept model element using an existing element as a template.
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
    public String createConceptModelElementFromTemplate(String                                userId,
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
     * Update the properties of a concept model element.
     *
     * @param userId                 userId of the user making the request.
     * @param conceptModelElementGUID unique identifier of the concept model element (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateConceptModelElement(String                        userId,
                                             String                        conceptModelElementGUID,
                                             UpdateOptions                 updateOptions,
                                             ConceptModelElementProperties properties) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName        = "updateConceptModelElement";
        final String guidParameterName = "conceptModelElementGUID";

        return super.updateElement(userId,
                                   conceptModelElementGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Attach a concept model to the element whose concepts it describes.
     *
     * @param userId                 userId of the user making the request
     * @param elementGUID unique identifier of the element that the concept model describes
     * @param conceptModelGUID unique identifier of the concept model
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkConceptDesign(String                   userId,
                                  String                   elementGUID,
                                  String                   conceptModelGUID,
                                  MakeAnchorOptions        makeAnchorOptions,
                                  ConceptDesignProperties  relationshipProperties) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName            = "linkConceptDesign";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "conceptModelGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(conceptModelGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONCEPT_DESIGN_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        conceptModelGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a concept model from the element whose concepts it described.
     *
     * @param userId                 userId of the user making the request.
     * @param elementGUID unique identifier of the element that the concept model describes
     * @param conceptModelGUID unique identifier of the concept model
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachConceptDesign(String        userId,
                                    String        elementGUID,
                                    String        conceptModelGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName            = "detachConceptDesign";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "conceptModelGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(conceptModelGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONCEPT_DESIGN_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        conceptModelGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a concept bead to one of the ends of a concept bead relationship.
     *
     * @param userId                 userId of the user making the request
     * @param conceptBeadRelationshipGUID unique identifier of the concept bead relationship
     * @param conceptBeadGUID unique identifier of the concept bead at this end of the relationship
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkConceptBeadRelationshipEnd(String                                userId,
                                               String                                conceptBeadRelationshipGUID,
                                               String                                conceptBeadGUID,
                                               MakeAnchorOptions                     makeAnchorOptions,
                                               ConceptBeadRelationshipEndProperties  relationshipProperties) throws InvalidParameterException,
                                                                                                                    PropertyServerException,
                                                                                                                    UserNotAuthorizedException
    {
        final String methodName            = "linkConceptBeadRelationshipEnd";
        final String end1GUIDParameterName = "conceptBeadRelationshipGUID";
        final String end2GUIDParameterName = "conceptBeadGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(conceptBeadRelationshipGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(conceptBeadGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONCEPT_BEAD_RELATIONSHIP_END_RELATIONSHIP.typeName,
                                                        conceptBeadRelationshipGUID,
                                                        conceptBeadGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a concept bead from one of the ends of a concept bead relationship.
     *
     * @param userId                 userId of the user making the request.
     * @param conceptBeadRelationshipGUID unique identifier of the concept bead relationship
     * @param conceptBeadGUID unique identifier of the concept bead at this end of the relationship
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachConceptBeadRelationshipEnd(String        userId,
                                                 String        conceptBeadRelationshipGUID,
                                                 String        conceptBeadGUID,
                                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName            = "detachConceptBeadRelationshipEnd";
        final String end1GUIDParameterName = "conceptBeadRelationshipGUID";
        final String end2GUIDParameterName = "conceptBeadGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(conceptBeadRelationshipGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(conceptBeadGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONCEPT_BEAD_RELATIONSHIP_END_RELATIONSHIP.typeName,
                                                        conceptBeadRelationshipGUID,
                                                        conceptBeadGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a concept bead attribute to the concept bead that acts as its type.
     *
     * @param userId                 userId of the user making the request
     * @param conceptBeadAttributeGUID unique identifier of the concept bead attribute
     * @param conceptBeadGUID unique identifier of the concept bead that provides the attribute's type
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkTypedByConceptBead(String                        userId,
                                       String                        conceptBeadAttributeGUID,
                                       String                        conceptBeadGUID,
                                       MakeAnchorOptions             makeAnchorOptions,
                                       TypedByConceptBeadProperties  relationshipProperties) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String methodName            = "linkTypedByConceptBead";
        final String end1GUIDParameterName = "conceptBeadAttributeGUID";
        final String end2GUIDParameterName = "conceptBeadGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(conceptBeadAttributeGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(conceptBeadGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.TYPED_BY_CONCEPT_BEAD_RELATIONSHIP.typeName,
                                                        conceptBeadAttributeGUID,
                                                        conceptBeadGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a concept bead attribute from the concept bead that acted as its type.
     *
     * @param userId                 userId of the user making the request.
     * @param conceptBeadAttributeGUID unique identifier of the concept bead attribute
     * @param conceptBeadGUID unique identifier of the concept bead that provides the attribute's type
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachTypedByConceptBead(String        userId,
                                         String        conceptBeadAttributeGUID,
                                         String        conceptBeadGUID,
                                         DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName            = "detachTypedByConceptBead";
        final String end1GUIDParameterName = "conceptBeadAttributeGUID";
        final String end2GUIDParameterName = "conceptBeadGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(conceptBeadAttributeGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(conceptBeadGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.TYPED_BY_CONCEPT_BEAD_RELATIONSHIP.typeName,
                                                        conceptBeadAttributeGUID,
                                                        conceptBeadGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a concept bead to the concept bead that it inherits from.
     *
     * @param userId                 userId of the user making the request
     * @param inheritingBeadGUID unique identifier of the concept bead that inherits
     * @param inheritedBeadGUID unique identifier of the concept bead that is inherited from
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkIsAConceptBead(String                    userId,
                                   String                    inheritingBeadGUID,
                                   String                    inheritedBeadGUID,
                                   MakeAnchorOptions         makeAnchorOptions,
                                   IsAConceptBeadProperties  relationshipProperties) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName            = "linkIsAConceptBead";
        final String end1GUIDParameterName = "inheritingBeadGUID";
        final String end2GUIDParameterName = "inheritedBeadGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(inheritingBeadGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(inheritedBeadGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.IS_A_CONCEPT_BEAD_RELATIONSHIP.typeName,
                                                        inheritingBeadGUID,
                                                        inheritedBeadGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a concept bead from the concept bead that it inherited from.
     *
     * @param userId                 userId of the user making the request.
     * @param inheritingBeadGUID unique identifier of the concept bead that inherits
     * @param inheritedBeadGUID unique identifier of the concept bead that is inherited from
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachIsAConceptBead(String        userId,
                                     String        inheritingBeadGUID,
                                     String        inheritedBeadGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName            = "detachIsAConceptBead";
        final String end1GUIDParameterName = "inheritingBeadGUID";
        final String end2GUIDParameterName = "inheritedBeadGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(inheritingBeadGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(inheritedBeadGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.IS_A_CONCEPT_BEAD_RELATIONSHIP.typeName,
                                                        inheritingBeadGUID,
                                                        inheritedBeadGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a concept bead attribute to its parent concept bead.
     *
     * @param userId                 userId of the user making the request
     * @param conceptBeadGUID unique identifier of the parent concept bead
     * @param conceptBeadAttributeGUID unique identifier of the concept bead attribute
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkConceptBeadAttributeLink(String                              userId,
                                             String                              conceptBeadGUID,
                                             String                              conceptBeadAttributeGUID,
                                             MakeAnchorOptions                   makeAnchorOptions,
                                             ConceptBeadAttributeLinkProperties  relationshipProperties) throws InvalidParameterException,
                                                                                                                PropertyServerException,
                                                                                                                UserNotAuthorizedException
    {
        final String methodName            = "linkConceptBeadAttributeLink";
        final String end1GUIDParameterName = "conceptBeadGUID";
        final String end2GUIDParameterName = "conceptBeadAttributeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(conceptBeadGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(conceptBeadAttributeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONCEPT_BEAD_ATTRIBUTE_LINK_RELATIONSHIP.typeName,
                                                        conceptBeadGUID,
                                                        conceptBeadAttributeGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a concept bead attribute from its parent concept bead.
     *
     * @param userId                 userId of the user making the request.
     * @param conceptBeadGUID unique identifier of the parent concept bead
     * @param conceptBeadAttributeGUID unique identifier of the concept bead attribute
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachConceptBeadAttributeLink(String        userId,
                                               String        conceptBeadGUID,
                                               String        conceptBeadAttributeGUID,
                                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName            = "detachConceptBeadAttributeLink";
        final String end1GUIDParameterName = "conceptBeadGUID";
        final String end2GUIDParameterName = "conceptBeadAttributeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(conceptBeadGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(conceptBeadAttributeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONCEPT_BEAD_ATTRIBUTE_LINK_RELATIONSHIP.typeName,
                                                        conceptBeadGUID,
                                                        conceptBeadAttributeGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a concept bead to a concept bead that extends it.
     *
     * @param userId                 userId of the user making the request
     * @param extendedBeadGUID unique identifier of the concept bead that is extended
     * @param extensionBeadGUID unique identifier of the concept bead that provides the extension
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkConceptBeadExtension(String                          userId,
                                         String                          extendedBeadGUID,
                                         String                          extensionBeadGUID,
                                         MakeAnchorOptions               makeAnchorOptions,
                                         ConceptBeadExtensionProperties  relationshipProperties) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        final String methodName            = "linkConceptBeadExtension";
        final String end1GUIDParameterName = "extendedBeadGUID";
        final String end2GUIDParameterName = "extensionBeadGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(extendedBeadGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(extensionBeadGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONCEPT_BEAD_EXTENSION_RELATIONSHIP.typeName,
                                                        extendedBeadGUID,
                                                        extensionBeadGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a concept bead from a concept bead that extended it.
     *
     * @param userId                 userId of the user making the request.
     * @param extendedBeadGUID unique identifier of the concept bead that is extended
     * @param extensionBeadGUID unique identifier of the concept bead that provides the extension
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachConceptBeadExtension(String        userId,
                                           String        extendedBeadGUID,
                                           String        extensionBeadGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName            = "detachConceptBeadExtension";
        final String end1GUIDParameterName = "extendedBeadGUID";
        final String end2GUIDParameterName = "extensionBeadGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(extendedBeadGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(extensionBeadGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONCEPT_BEAD_EXTENSION_RELATIONSHIP.typeName,
                                                        extendedBeadGUID,
                                                        extensionBeadGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a concept model element.
     *
     * @param userId                 userId of the user making the request.
     * @param conceptModelElementGUID unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteConceptModelElement(String        userId,
                                          String        conceptModelElementGUID,
                                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName        = "deleteConceptModelElement";
        final String guidParameterName = "conceptModelElementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(conceptModelElementGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, conceptModelElementGUID, deleteOptions);
    }


    /**
     * Returns the list of concept model elements with a particular name.
     *
     * @param userId                 userId of the user making the request
     * @param name                   name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getConceptModelElementsByName(String       userId,
                                                                       String       name,
                                                                       QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String methodName = "getConceptModelElementsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Return the properties of a specific concept model element.
     *
     * @param userId                 userId of the user making the request
     * @param conceptModelElementGUID unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getConceptModelElementByGUID(String     userId,
                                                                String     conceptModelElementGUID,
                                                                GetOptions getOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName = "getConceptModelElementByGUID";

        return super.getRootElementByGUID(userId,
                                          conceptModelElementGUID,
                                          getOptions,
                                          methodName);
    }


    /**
     * Retrieve the list of concept model element metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findConceptModelElements(String        userId,
                                                                   String        searchString,
                                                                   SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName  = "findConceptModelElements";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
