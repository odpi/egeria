/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ConceptModelElementHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with the elements of a concept model.
 */
public class ConceptModelElementClient extends ConnectorContextClientBase
{
    private final ConceptModelElementHandler conceptModelElementHandler;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public ConceptModelElementClient(ConnectorContextBase     parentContext,
                                     String                   localServerName,
                                     String                   localServiceName,
                                     String                   connectorUserId,
                                     String                   connectorGUID,
                                     String                   externalSourceGUID,
                                     String                   externalSourceName,
                                     OpenMetadataClient       openMetadataClient,
                                     AuditLog                 auditLog,
                                     int                      maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.conceptModelElementHandler = new ConceptModelElementHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new concept model element.  The subtype is taken from the type name carried by the supplied properties.
     *
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createConceptModelElement(NewElementOptions                     newElementOptions,
                                            Map<String, ClassificationProperties> initialClassifications,
                                            ConceptModelElementProperties         properties,
                                            RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                       PropertyServerException,
                                                                                                                       UserNotAuthorizedException
    {
        String elementGUID = conceptModelElementHandler.createConceptModelElement(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a concept model element using an existing element as a template.
     *
     * @param templateOptions              details of the element to create
     * @param templateGUID                 the unique identifier of the existing element to copy
     * @param replacementProperties        properties of the new metadata element.  These override the template values
     * @param replacementClassifications map of classification names to classification properties to include in the entity creation request. These override the template values.
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the
     *                                     template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createConceptModelElementFromTemplate(TemplateOptions                       templateOptions,
                                                        String                                templateGUID,
                                                        EntityProperties                      replacementProperties,
                                                        Map<String, ClassificationProperties> replacementClassifications,
                                                        Map<String, String>                   placeholderProperties,
                                                        RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                                   UserNotAuthorizedException,
                                                                                                                                   PropertyServerException
    {
        String elementGUID = conceptModelElementHandler.createConceptModelElementFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, replacementClassifications, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a concept model element.
     *
     * @param conceptModelElementGUID unique identifier of the concept model element (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateConceptModelElement(String                        conceptModelElementGUID,
                                             UpdateOptions                 updateOptions,
                                             ConceptModelElementProperties properties) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        boolean updateOccurred = conceptModelElementHandler.updateConceptModelElement(connectorUserId, conceptModelElementGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getActivityReportWriter() != null))
        {
            parentContext.getActivityReportWriter().reportElementUpdate(conceptModelElementGUID);
        }

        return updateOccurred;
    }


    /**
     * Attach a concept model to the element whose concepts it describes.
     *
     * @param elementGUID unique identifier of the element that the concept model describes
     * @param conceptModelGUID unique identifier of the concept model
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkConceptDesign(String                   elementGUID,
                             String                   conceptModelGUID,
                             MakeAnchorOptions        makeAnchorOptions,
                             ConceptDesignProperties  relationshipProperties) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        conceptModelElementHandler.linkConceptDesign(connectorUserId, elementGUID, conceptModelGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a concept model from the element whose concepts it described.
     *
     * @param elementGUID unique identifier of the element that the concept model describes
     * @param conceptModelGUID unique identifier of the concept model
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachConceptDesign(String        elementGUID,
                               String        conceptModelGUID,
                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        conceptModelElementHandler.detachConceptDesign(connectorUserId, elementGUID, conceptModelGUID, deleteOptions);
    }


    /**
     * Attach a concept bead to one of the ends of a concept bead relationship.
     *
     * @param conceptBeadRelationshipGUID unique identifier of the concept bead relationship
     * @param conceptBeadGUID unique identifier of the concept bead at this end of the relationship
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkConceptBeadRelationshipEnd(String                                conceptBeadRelationshipGUID,
                                          String                                conceptBeadGUID,
                                          MakeAnchorOptions                     makeAnchorOptions,
                                          ConceptBeadRelationshipEndProperties  relationshipProperties) throws InvalidParameterException,
                                                                                                               PropertyServerException,
                                                                                                               UserNotAuthorizedException
    {
        conceptModelElementHandler.linkConceptBeadRelationshipEnd(connectorUserId, conceptBeadRelationshipGUID, conceptBeadGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a concept bead from one of the ends of a concept bead relationship.
     *
     * @param conceptBeadRelationshipGUID unique identifier of the concept bead relationship
     * @param conceptBeadGUID unique identifier of the concept bead at this end of the relationship
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachConceptBeadRelationshipEnd(String        conceptBeadRelationshipGUID,
                                            String        conceptBeadGUID,
                                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        conceptModelElementHandler.detachConceptBeadRelationshipEnd(connectorUserId, conceptBeadRelationshipGUID, conceptBeadGUID, deleteOptions);
    }


    /**
     * Attach a concept bead attribute to the concept bead that acts as its type.
     *
     * @param conceptBeadAttributeGUID unique identifier of the concept bead attribute
     * @param conceptBeadGUID unique identifier of the concept bead that provides the attribute's type
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkTypedByConceptBead(String                        conceptBeadAttributeGUID,
                                  String                        conceptBeadGUID,
                                  MakeAnchorOptions             makeAnchorOptions,
                                  TypedByConceptBeadProperties  relationshipProperties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        conceptModelElementHandler.linkTypedByConceptBead(connectorUserId, conceptBeadAttributeGUID, conceptBeadGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a concept bead attribute from the concept bead that acted as its type.
     *
     * @param conceptBeadAttributeGUID unique identifier of the concept bead attribute
     * @param conceptBeadGUID unique identifier of the concept bead that provides the attribute's type
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachTypedByConceptBead(String        conceptBeadAttributeGUID,
                                    String        conceptBeadGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        conceptModelElementHandler.detachTypedByConceptBead(connectorUserId, conceptBeadAttributeGUID, conceptBeadGUID, deleteOptions);
    }


    /**
     * Attach a concept bead to the concept bead that it inherits from.
     *
     * @param inheritingBeadGUID unique identifier of the concept bead that inherits
     * @param inheritedBeadGUID unique identifier of the concept bead that is inherited from
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkIsAConceptBead(String                    inheritingBeadGUID,
                              String                    inheritedBeadGUID,
                              MakeAnchorOptions         makeAnchorOptions,
                              IsAConceptBeadProperties  relationshipProperties) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        conceptModelElementHandler.linkIsAConceptBead(connectorUserId, inheritingBeadGUID, inheritedBeadGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a concept bead from the concept bead that it inherited from.
     *
     * @param inheritingBeadGUID unique identifier of the concept bead that inherits
     * @param inheritedBeadGUID unique identifier of the concept bead that is inherited from
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachIsAConceptBead(String        inheritingBeadGUID,
                                String        inheritedBeadGUID,
                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        conceptModelElementHandler.detachIsAConceptBead(connectorUserId, inheritingBeadGUID, inheritedBeadGUID, deleteOptions);
    }


    /**
     * Attach a concept bead attribute to its parent concept bead.
     *
     * @param conceptBeadGUID unique identifier of the parent concept bead
     * @param conceptBeadAttributeGUID unique identifier of the concept bead attribute
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkConceptBeadAttributeLink(String                              conceptBeadGUID,
                                        String                              conceptBeadAttributeGUID,
                                        MakeAnchorOptions                   makeAnchorOptions,
                                        ConceptBeadAttributeLinkProperties  relationshipProperties) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        conceptModelElementHandler.linkConceptBeadAttributeLink(connectorUserId, conceptBeadGUID, conceptBeadAttributeGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a concept bead attribute from its parent concept bead.
     *
     * @param conceptBeadGUID unique identifier of the parent concept bead
     * @param conceptBeadAttributeGUID unique identifier of the concept bead attribute
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachConceptBeadAttributeLink(String        conceptBeadGUID,
                                          String        conceptBeadAttributeGUID,
                                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        conceptModelElementHandler.detachConceptBeadAttributeLink(connectorUserId, conceptBeadGUID, conceptBeadAttributeGUID, deleteOptions);
    }


    /**
     * Attach a concept bead to a concept bead that extends it.
     *
     * @param extendedBeadGUID unique identifier of the concept bead that is extended
     * @param extensionBeadGUID unique identifier of the concept bead that provides the extension
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkConceptBeadExtension(String                          extendedBeadGUID,
                                    String                          extensionBeadGUID,
                                    MakeAnchorOptions               makeAnchorOptions,
                                    ConceptBeadExtensionProperties  relationshipProperties) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        conceptModelElementHandler.linkConceptBeadExtension(connectorUserId, extendedBeadGUID, extensionBeadGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a concept bead from a concept bead that extended it.
     *
     * @param extendedBeadGUID unique identifier of the concept bead that is extended
     * @param extensionBeadGUID unique identifier of the concept bead that provides the extension
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachConceptBeadExtension(String        extendedBeadGUID,
                                      String        extensionBeadGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        conceptModelElementHandler.detachConceptBeadExtension(connectorUserId, extendedBeadGUID, extensionBeadGUID, deleteOptions);
    }


    /**
     * Delete a concept model element.
     *
     * @param conceptModelElementGUID unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteConceptModelElement(String        conceptModelElementGUID,
                                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        conceptModelElementHandler.deleteConceptModelElement(connectorUserId, conceptModelElementGUID, deleteOptions);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementDelete(conceptModelElementGUID);
        }
    }


    /**
     * Returns the list of concept model elements with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getConceptModelElementsByName(String       name,
                                                                       QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        return conceptModelElementHandler.getConceptModelElementsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific concept model element.
     *
     * @param conceptModelElementGUID unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getConceptModelElementByGUID(String     conceptModelElementGUID,
                                                                GetOptions getOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        return conceptModelElementHandler.getConceptModelElementByGUID(connectorUserId, conceptModelElementGUID, getOptions);
    }


    /**
     * Retrieve the list of concept model element metadata elements that contain the search string.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findConceptModelElements(String        searchString,
                                                                   SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        return conceptModelElementHandler.findConceptModelElements(connectorUserId, searchString, searchOptions);
    }
}
