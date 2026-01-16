/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.CollectionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AnchorsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataDescriptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.CanonicalVocabularyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.TaxonomyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionDesignProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with collection elements.
 */
public class CollectionClient extends ConnectorContextClientBase
{
    private final CollectionHandler collectionHandler;


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
    public CollectionClient(ConnectorContextBase     parentContext,
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

        this.collectionHandler = new CollectionHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }

    /**
     * Copy/clone constructor.
     *
     * @param template client to copy
     * @param specificTypeName type name override
     */
    public CollectionClient(CollectionClient template,
                       String      specificTypeName)
    {
        super(template);

        this.collectionHandler = new CollectionHandler(template.collectionHandler, specificTypeName);
    }


    /**
     * Create a new collection.
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
    public String createCollection(NewElementOptions                     newElementOptions,
                                   Map<String, ClassificationProperties> initialClassifications,
                                   CollectionProperties                  properties,
                                   RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        String elementGUID = collectionHandler.createCollection(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a collection using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new collection.
     *
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing collection to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createCollectionFromTemplate(TemplateOptions        templateOptions,
                                               String                 templateGUID,
                                               EntityProperties       replacementProperties,
                                               Map<String, String>    placeholderProperties,
                                               RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        String elementGUID = collectionHandler.createCollectionFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a collection.
     *
     * @param collectionGUID       unique identifier of the collection (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateCollection(String               collectionGUID,
                                    UpdateOptions        updateOptions,
                                    CollectionProperties properties) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        boolean updateOccurred = collectionHandler.updateCollection(connectorUserId, collectionGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(collectionGUID);
        }

        return updateOccurred;
    }



    /**
     * Connect an existing collection to an element using the ResourceList relationship (0019).
     *
     * @param collectionGUID    unique identifier of the collection
     * @param parentGUID        unique identifier of referenceable object that the collection should be attached to
     * @param makeAnchorOptions options to control access to open metadata
     * @param properties        description of how the collection will be used.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void attachCollection(String                 collectionGUID,
                                 String                 parentGUID,
                                 MakeAnchorOptions      makeAnchorOptions,
                                 ResourceListProperties properties) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        collectionHandler.attachCollection(connectorUserId, collectionGUID, parentGUID, makeAnchorOptions, properties);
    }


    /**
     * Detach an existing collection from an element.  If the collection is anchored to the element, it is deleted.
     *
     * @param collectionGUID unique identifier of the collection.
     * @param parentGUID     unique identifier of referenceable object that the collection should be attached to.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachCollection(String        collectionGUID,
                                 String        parentGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        collectionHandler.detachCollection(connectorUserId, collectionGUID, parentGUID, deleteOptions);
    }




    /**
     * Connect a data describing collection to an element using the DataDescription relationship (0580).
     *
     * @param dataDescriptionCollectionGUID    unique identifier of the collection
     * @param parentGUID        unique identifier of referenceable object that the collection should be attached to
     * @param makeAnchorOptions options to control access to open metadata
     * @param properties        description of how the collection will be used.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void attachDataDescription(String                    parentGUID,
                                      String                    dataDescriptionCollectionGUID,
                                      MakeAnchorOptions         makeAnchorOptions,
                                      DataDescriptionProperties properties) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        collectionHandler.attachDataDescription(connectorUserId, parentGUID, dataDescriptionCollectionGUID, makeAnchorOptions, properties);
    }


    /**
     * Detach an existing data describing collection from an element.  If the collection is anchored to the element, it is deleted.
     *
     * @param dataDescriptionCollectionGUID unique identifier of the collection.
     * @param parentGUID     unique identifier of referenceable object that the collection should be attached to.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDataDescription(String        parentGUID,
                                      String        dataDescriptionCollectionGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        collectionHandler.detachDataDescription(connectorUserId, parentGUID, dataDescriptionCollectionGUID, deleteOptions);
    }



    /**
     * Attach a solution blueprint to the element it describes.
     *
     * @param parentGUID  unique identifier of the element being described
     * @param solutionBlueprintGUID      unique identifier of the  solution blueprint
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSolutionDesign(String                   parentGUID,
                                   String                   solutionBlueprintGUID,
                                   MakeAnchorOptions        makeAnchorOptions,
                                   SolutionDesignProperties relationshipProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        collectionHandler.linkSolutionDesign(connectorUserId, parentGUID, solutionBlueprintGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a solution blueprint from the element it describes.
     *
     * @param parentGUID  unique identifier of the element being described
     * @param solutionBlueprintGUID      unique identifier of the  solution blueprint
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSolutionDesign(String        parentGUID,
                                     String        solutionBlueprintGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        collectionHandler.detachSolutionDesign(connectorUserId, parentGUID, solutionBlueprintGUID, deleteOptions);
    }



    /**
     * Link two dependent products.
     *
     * @param consumerDigitalProductGUID unique identifier of the digital product that has the dependency.
     * @param consumedDigitalProductGUID unique identifier of the digital product that it is using.
     * @param makeAnchorOptions      options to control access to open metadata
     * @param relationshipProperties     description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDigitalProductDependency(String                             consumerDigitalProductGUID,
                                             String                             consumedDigitalProductGUID,
                                             MakeAnchorOptions                  makeAnchorOptions,
                                             DigitalProductDependencyProperties relationshipProperties) throws InvalidParameterException,
                                                                                                               PropertyServerException,
                                                                                                               UserNotAuthorizedException
    {
        collectionHandler.linkDigitalProductDependency(connectorUserId, consumerDigitalProductGUID, consumedDigitalProductGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Unlink dependent products.
     *
     * @param consumerDigitalProductGUID unique identifier of the digital product that has the dependency.
     * @param consumedDigitalProductGUID unique identifier of the digital product that it is using.
     * @param deleteOptions              options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDigitalProductDependency(String        consumerDigitalProductGUID,
                                               String        consumedDigitalProductGUID,
                                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        collectionHandler.detachDigitalProductDependency(connectorUserId, consumerDigitalProductGUID, consumedDigitalProductGUID, deleteOptions);
    }


    /**
     * Attach a subscriber to a subscription.
     *
     * @param digitalSubscriberGUID   unique identifier of the subscriber (referenceable)
     * @param digitalSubscriptionGUID unique identifier of the  digital subscription agreement
     * @param makeAnchorOptions   options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSubscriber(String                      digitalSubscriberGUID,
                               String                      digitalSubscriptionGUID,
                               MakeAnchorOptions           makeAnchorOptions,
                               DigitalSubscriberProperties relationshipProperties) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        collectionHandler.linkSubscriber(connectorUserId, digitalSubscriberGUID, digitalSubscriptionGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a subscriber from a subscription.
     *
     * @param digitalSubscriberGUID   unique identifier of the subscriber (referenceable)
     * @param digitalSubscriptionGUID unique identifier of the  digital subscription agreement
     * @param deleteOptions           options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSubscriber(String        digitalSubscriberGUID,
                                 String        digitalSubscriptionGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        collectionHandler.detachSubscriber(connectorUserId, digitalSubscriberGUID, digitalSubscriptionGUID, deleteOptions);
    }


    /**
     * Attach a product manager to a digital product.
     *
     * @param digitalProductGUID            unique identifier of the digital product
     * @param digitalProductManagerRoleGUID unique identifier of the product manager role
     * @param makeAnchorOptions         options to control access to open metadata
     * @param relationshipProperties        description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkProductManager(String                    digitalProductGUID,
                                   String                    digitalProductManagerRoleGUID,
                                   MakeAnchorOptions         makeAnchorOptions,
                                   AssignmentScopeProperties relationshipProperties) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        collectionHandler.linkProductManager(connectorUserId, digitalProductGUID, digitalProductManagerRoleGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a product manager from a digital product.
     *
     * @param digitalProductGUID        unique identifier of the digital product
     * @param digitalProductManagerGUID unique identifier of the product manager role
     * @param deleteOptions             options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachProductManager(String        digitalProductGUID,
                                     String        digitalProductManagerGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        collectionHandler.detachProductManager(connectorUserId, digitalProductGUID, digitalProductManagerGUID, deleteOptions);
    }


    /**
     * Attach an actor to an agreement.
     *
     * @param agreementGUID          unique identifier of the agreement
     * @param actorGUID              unique identifier of the actor
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @return unique identifier of the relationship
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String linkAgreementActor(String                   agreementGUID,
                                     String                   actorGUID,
                                     MakeAnchorOptions        makeAnchorOptions,
                                     AgreementActorProperties relationshipProperties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        return collectionHandler.linkAgreementActor(connectorUserId, agreementGUID, actorGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach an actor from an agreement.
     *
     * @param agreementActorRelationshipGUID unique identifier of the element being described
     * @param deleteOptions                  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAgreementActor(String        agreementActorRelationshipGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        collectionHandler.detachAgreementActor(connectorUserId, agreementActorRelationshipGUID, deleteOptions);
    }


    /**
     * Attach an agreement to an element involved in its definition.
     *
     * @param agreementGUID          unique identifier of the agreement
     * @param agreementItemGUID      unique identifier of the agreement item
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAgreementItem(String                  agreementGUID,
                                  String                  agreementItemGUID,
                                  MakeAnchorOptions       makeAnchorOptions,
                                  AgreementItemProperties relationshipProperties) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        collectionHandler.linkAgreementItem(connectorUserId, agreementGUID, agreementItemGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach an agreement from an element involved in its definition.
     *
     * @param agreementGUID     unique identifier of the agreement
     * @param agreementItemGUID unique identifier of the agreement item
     * @param deleteOptions     options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAgreementItem(String        agreementGUID,
                                    String        agreementItemGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        collectionHandler.detachAgreementItem(connectorUserId, agreementGUID, agreementItemGUID, deleteOptions);
    }


    /**
     * Attach an agreement to an external reference element that describes the location of the contract documents.
     *
     * @param agreementGUID          unique identifier of the agreement
     * @param externalReferenceGUID  unique identifier of the external reference describing the location of the contract
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkContract(String                 agreementGUID,
                             String                 externalReferenceGUID,
                             MakeAnchorOptions      makeAnchorOptions,
                             ContractLinkProperties relationshipProperties) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        collectionHandler.linkContract(connectorUserId, agreementGUID, externalReferenceGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach an agreement from an external reference describing the location of the contract documents.
     *
     * @param agreementGUID         unique identifier of the agreement
     * @param externalReferenceGUID unique identifier of the external reference describing the location of the contract
     * @param deleteOptions         options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachContract(String        agreementGUID,
                               String        externalReferenceGUID,
                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        collectionHandler.detachContract(connectorUserId, agreementGUID, externalReferenceGUID, deleteOptions);
    }


    /**
     * Link dependent business capabilities.
     *
     * @param supportedBusinessCapabilityGUID          unique identifier of the business capability that is dependent on another
     * @param supportingBusinessCapabilityGUID  unique identifier of the business capability that is supporting
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkBusinessCapabilityDependency(String                                 supportedBusinessCapabilityGUID,
                                                 String                                 supportingBusinessCapabilityGUID,
                                                 MakeAnchorOptions                      makeAnchorOptions,
                                                 BusinessCapabilityDependencyProperties relationshipProperties) throws InvalidParameterException,
                                                                                                                       PropertyServerException,
                                                                                                                       UserNotAuthorizedException
    {
        collectionHandler.linkBusinessCapabilityDependency(connectorUserId, supportedBusinessCapabilityGUID, supportingBusinessCapabilityGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach dependent business capabilities.
     *
     * @param supportedBusinessCapabilityGUID          unique identifier of the business capability that is dependent on another
     * @param supportingBusinessCapabilityGUID  unique identifier of the business capability that is supporting
     * @param deleteOptions         options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachBusinessCapabilityDependency(String        supportedBusinessCapabilityGUID,
                                                   String        supportingBusinessCapabilityGUID,
                                                   DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        collectionHandler.detachBusinessCapabilityDependency(connectorUserId, supportedBusinessCapabilityGUID, supportingBusinessCapabilityGUID, deleteOptions);
    }


    /**
     * Attach a business capability to an element that provides digital support.
     *
     * @param businessCapabilityGUID          unique identifier of the business capability
     * @param elementGUID  unique identifier of the element
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDigitalSupport(String                   businessCapabilityGUID,
                                   String                   elementGUID,
                                   MakeAnchorOptions        makeAnchorOptions,
                                   DigitalSupportProperties relationshipProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        collectionHandler.linkDigitalSupport(connectorUserId, businessCapabilityGUID, elementGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a business capability from an element that provides digital support.
     *
     * @param businessCapabilityGUID          unique identifier of the business capability
     * @param elementGUID  unique identifier of the element
     * @param deleteOptions         options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDigitalSupport(String        businessCapabilityGUID,
                                     String        elementGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        collectionHandler.detachDigitalSupport(connectorUserId, businessCapabilityGUID, elementGUID, deleteOptions);
    }


    /**
     * Classify an element to indicate that it is significant to a particular business capability.
     *
     * @param elementGUID    unique identifier of the element.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setBusinessSignificant(String                        elementGUID,
                                       BusinessSignificantProperties properties,
                                       MetadataSourceOptions         metadataSourceOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        collectionHandler.setBusinessSignificant(connectorUserId, elementGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the business significant classification from the element.
     *
     * @param elementGUID    unique identifier of the element.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearBusinessSignificance(String                elementGUID,
                                          MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        collectionHandler.clearBusinessSignificance(connectorUserId, elementGUID, metadataSourceOptions);
    }


    /**
     * Classify the collection to indicate that it is an editing collection - this means it is
     * a collection of element copies that will eventually be merged back into .
     *
     * @param collectionGUID    unique identifier of the collection.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setEditingCollection(String                      collectionGUID,
                                     EditingCollectionProperties properties,
                                     MetadataSourceOptions       metadataSourceOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        collectionHandler.setEditingCollection(connectorUserId, collectionGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the editing collection classification from the collection.
     *
     * @param collectionGUID    unique identifier of the collection.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearEditingCollection(String                collectionGUID,
                                       MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        collectionHandler.clearEditingCollection(connectorUserId, collectionGUID, metadataSourceOptions);
    }



    /**
     * Classify the collection to indicate that it is a staging collection.
     *
     * @param collectionGUID    unique identifier of the collection.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setStagingCollection(String                      collectionGUID,
                                     StagingCollectionProperties properties,
                                     MetadataSourceOptions       metadataSourceOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        collectionHandler.setStagingCollection(connectorUserId, collectionGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the staging collection classification from the collection.
     *
     * @param collectionGUID    unique identifier of the collection.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearStagingCollection(String                collectionGUID,
                                       MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        collectionHandler.clearStagingCollection(connectorUserId, collectionGUID, metadataSourceOptions);
    }


    /**
     * Classify the collection to indicate that it is a scoping collection.
     *
     * @param collectionGUID    unique identifier of the collection.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setScopingCollection(String                      collectionGUID,
                                     ScopingCollectionProperties properties,
                                     MetadataSourceOptions       metadataSourceOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        collectionHandler.setScopingCollection(connectorUserId, collectionGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the scoping collection classification from the collection.
     *
     * @param collectionGUID    unique identifier of the collection.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearScopingCollection(String                collectionGUID,
                                       MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        collectionHandler.clearScopingCollection(connectorUserId, collectionGUID, metadataSourceOptions);
    }


    /**
     * Classify the glossary to indicate that it can be used as a taxonomy.
     * This means each term is attached to one, and only one category and the categories are organized as a hierarchy
     * with a single root category.
     * Taxonomies are used as a way of organizing assets and other related metadata.  The terms in the taxonomy
     * are linked to the assets etc. and as such they are logically categorized by the linked category.
     *
     * @param glossaryGUID    unique identifier of the glossary.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setGlossaryAsTaxonomy(String                glossaryGUID,
                                      TaxonomyProperties    properties,
                                      MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        collectionHandler.setGlossaryAsTaxonomy(connectorUserId, glossaryGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the taxonomy glossary classification from the glossary.
     *
     * @param glossaryGUID    unique identifier of the glossary.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearGlossaryAsTaxonomy(String                glossaryGUID,
                                        MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        collectionHandler.clearGlossaryAsTaxonomy(connectorUserId, glossaryGUID, metadataSourceOptions);
    }



    /**
     * Classify a glossary to declare that it has no two GlossaryTerm definitions with
     * the same name.  This means there is only one definition for each term.  Typically, the terms are also of a similar
     * level of granularity and are limited to a specific scope of use.
     * Canonical vocabularies are used to semantically classify assets in an unambiguous way.
     *
     * @param glossaryGUID    unique identifier of the glossary.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setGlossaryAsCanonical(String                        glossaryGUID,
                                       CanonicalVocabularyProperties properties,
                                       MetadataSourceOptions         metadataSourceOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        collectionHandler.setGlossaryAsCanonical(connectorUserId, glossaryGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the canonical designation from the glossary.
     *
     * @param glossaryGUID    unique identifier of the glossary.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearGlossaryAsCanonical(String                glossaryGUID,
                                         MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        collectionHandler.clearGlossaryAsCanonical(connectorUserId, glossaryGUID, metadataSourceOptions);
    }


    /**
     * Delete a collection.
     *
     * @param collectionGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteCollection(String        collectionGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        collectionHandler.deleteCollection(connectorUserId, collectionGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(collectionGUID);
        }
    }


    /**
     * Returns the list of collections with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, resourceName or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getCollectionsByName(String       name,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        return collectionHandler.getCollectionsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific collection.
     *
     * @param collectionGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getCollectionByGUID(String     collectionGUID,
                                                       GetOptions getOptions) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        return collectionHandler.getCollectionByGUID(connectorUserId, collectionGUID, getOptions);
    }


    /**
     * Retrieve the list of collections metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned collections include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findCollections(String        searchString,
                                                         SearchOptions searchOptions) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return collectionHandler.findCollections(connectorUserId, searchString, searchOptions);
    }


    /**
     * Retrieve the digital products that match the search string and optional status.
     *
     * @param searchString string to search for (may include RegExs)
     * @param deploymentStatus   optional  status
     * @param searchOptions   multiple options to control the query
     * @return list of action beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<OpenMetadataRootElement> findDigitalProducts(String           searchString,
                                                             DeploymentStatus deploymentStatus,
                                                             SearchOptions    searchOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        return collectionHandler.findDigitalProducts(connectorUserId, searchString, deploymentStatus, searchOptions);
    }


    /**
     * Retrieve the digital products that match the category name and status.
     *
     * @param category   type to search for
     * @param deploymentStatus optional status
     * @param queryOptions multiple options to control the query
     * @return list of action beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<OpenMetadataRootElement> getDigitalProductByCategory(String           category,
                                                                     DeploymentStatus deploymentStatus,
                                                                     QueryOptions     queryOptions) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        return collectionHandler.getDigitalProductByCategory(connectorUserId, category, deploymentStatus, queryOptions);
    }


    /**
     * Extract the glossary from a term.
     *
     * @param glossaryTerm glossary term root element.
     *
     * @return glossary root element or null
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElement getGlossaryForTerm(OpenMetadataRootElement glossaryTerm) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String methodName = "getGlossaryFromTerm";
        final String parameterName = "glossaryTerm";

        propertyHelper.validateObject(glossaryTerm, parameterName, methodName);

        /*
         * Typically the glossary is the anchorScope of a glossary term.
         */
        if ((glossaryTerm.getElementHeader().getAnchor() != null) &&
                (glossaryTerm.getElementHeader().getAnchor().getClassificationProperties() instanceof AnchorsProperties anchorsProperties) &&
                (anchorsProperties.getAnchorScopeGUID() != null))
        {
            return this.getCollectionByGUID(anchorsProperties.getAnchorScopeGUID(), this.getGetOptions());
        }

        /*
         * See if the glossary term is a member of a glossary.  Return that glossary if the term is
         * only a member of one glossary.
         */
        if (glossaryTerm.getMemberOfCollections() != null)
        {
            RelatedMetadataElementSummary savedGlossary = null;
            int                           glossaryCount = 0;

            for (RelatedMetadataElementSummary parentCollection : glossaryTerm.getMemberOfCollections())
            {
                if ((parentCollection != null) && (propertyHelper.isTypeOf(parentCollection.getRelatedElement().getElementHeader(), OpenMetadataType.GLOSSARY.typeName)))
                {
                    if (glossaryCount == 0)
                    {
                        savedGlossary = parentCollection;
                    }

                    glossaryCount++;
                }
            }

            if (glossaryCount == 1)
            {
                return this.getCollectionByGUID(savedGlossary.getRelatedElement().getElementHeader().getGUID(), this.getGetOptions());
            }
        }

        return null;
    }


    /**
     * Return a list of elements that are a member of a collection.
     *
     * @param collectionGUID unique identifier of the collection.
     * @param queryOptions   options for query
     * @return list of member details
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getCollectionMembers(String collectionGUID,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        return collectionHandler.getCollectionMembers(connectorUserId, collectionGUID, queryOptions);
    }



    /**
     * Add an element to a collection.
     *
     * @param collectionGUID        unique identifier of the collection.
     * @param makeAnchorOptions options to control access to open metadata
     * @param membershipProperties  properties describing the membership characteristics.
     * @param elementGUID           unique identifier of the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addToCollection(String                         collectionGUID,
                                String                         elementGUID,
                                MakeAnchorOptions              makeAnchorOptions,
                                CollectionMembershipProperties membershipProperties) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        collectionHandler.addToCollection(connectorUserId, collectionGUID, elementGUID, makeAnchorOptions, membershipProperties);
    }


    /**
     * Update an element's membership to a collection.
     *
     * @param collectionGUID       unique identifier of the collection.
     * @param elementGUID          unique identifier of the element.
     * @param updateOptions        provides a structure for the additional options when updating a relationship.
     * @param membershipProperties properties describing the membership characteristics.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateCollectionMembership(String                         collectionGUID,
                                           String                         elementGUID,
                                           UpdateOptions                  updateOptions,
                                           CollectionMembershipProperties membershipProperties) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        collectionHandler.updateCollectionMembership(connectorUserId, collectionGUID, elementGUID, updateOptions, membershipProperties);
    }


    /**
     * Update the properties of a collection membership relationship.
     *
     * @param relationshipGUID     unique identifier of the collection
     * @param updateOptions        provides a structure for the additional options when updating a relationship.
     * @param membershipProperties properties describing the membership characteristics.
     */
    private void updateCollectionMembership(String                         relationshipGUID,
                                            UpdateOptions                  updateOptions,
                                            CollectionMembershipProperties membershipProperties) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        collectionHandler.updateCollectionMembership(connectorUserId, relationshipGUID, updateOptions, membershipProperties);
    }


    /**
     * Remove an element from a collection.
     *
     * @param collectionGUID unique identifier of the collection.
     * @param elementGUID    unique identifier of the element.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeFromCollection(String        collectionGUID,
                                     String        elementGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        collectionHandler.removeFromCollection(connectorUserId, collectionGUID, elementGUID, deleteOptions);
    }
}
