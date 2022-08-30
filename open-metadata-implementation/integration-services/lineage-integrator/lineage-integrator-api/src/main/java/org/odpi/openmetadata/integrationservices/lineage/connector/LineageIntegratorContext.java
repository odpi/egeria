/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.lineage.connector;

import org.odpi.openmetadata.accessservices.assetmanager.api.AssetManagerEventListener;
import org.odpi.openmetadata.accessservices.assetmanager.client.AssetManagerEventClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.DataAssetExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.GovernanceExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.LineageExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.StewardshipExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageRunEvent;


import java.util.Date;
import java.util.List;

/**
 * LineageIntegratorContext provides a wrapper around the Asset Manager OMAS client.
 * It provides the simplified interface to open metadata needed by the LineageIntegratorConnector.
 */
public class LineageIntegratorContext implements OpenLineageListenerManager
{
    private final OpenLineageListenerManager openLineageListenerManager;
    private final DataAssetExchangeClient    dataAssetExchangeClient;
    private final LineageExchangeClient      lineageExchangeClient;
    private final GovernanceExchangeClient   governanceExchangeClient;
    private final StewardshipExchangeClient  stewardshipExchangeClient;
    private final AssetManagerEventClient    eventClient;
    private final String                     userId;
    private final String                     assetManagerGUID;
    private final String                     assetManagerName;
    private final String                     connectorName;
    private final String                     integrationServiceName;
    private final AuditLog                   auditLog;

    private boolean       forLineage             = true;
    private final boolean forDuplicateProcessing = false;

    /**
     * Create a new context for a connector.
     *
     * @param openLineageListenerManager object responsible for managing open lineage listeners
     * @param dataAssetExchangeClient client for data asset requests
     * @param lineageExchangeClient client for lineage requests
     * @param governanceExchangeClient client for governance actions and related elements
     * @param stewardshipExchangeClient client for attaching governance metadata
     * @param eventClient client managing listeners for the OMAS OutTopic
     * @param userId integration daemon's userId
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param connectorName name of the connector using this context
     * @param integrationServiceName name of this service
     * @param auditLog logging destination
     */
    public LineageIntegratorContext(OpenLineageListenerManager openLineageListenerManager,
                                    DataAssetExchangeClient    dataAssetExchangeClient,
                                    LineageExchangeClient      lineageExchangeClient,
                                    GovernanceExchangeClient   governanceExchangeClient,
                                    StewardshipExchangeClient  stewardshipExchangeClient,
                                    AssetManagerEventClient    eventClient,
                                    String                     userId,
                                    String                     assetManagerGUID,
                                    String                     assetManagerName,
                                    String                     connectorName,
                                    String                     integrationServiceName,
                                    AuditLog                   auditLog)
    {
        this.openLineageListenerManager = openLineageListenerManager;
        this.dataAssetExchangeClient    = dataAssetExchangeClient;
        this.lineageExchangeClient      = lineageExchangeClient;
        this.governanceExchangeClient   = governanceExchangeClient;
        this.stewardshipExchangeClient  = stewardshipExchangeClient;
        this.eventClient                = eventClient;
        this.userId                     = userId;
        this.assetManagerGUID           = assetManagerGUID;
        this.assetManagerName           = assetManagerName;
        this.connectorName              = connectorName;
        this.integrationServiceName     = integrationServiceName;
        this.auditLog                   = auditLog;
    }

    /* ========================================================
     * Returning the integration service name from the configuration
     */

    /**
     * Return the qualified name of the integration services that is supplied in the configuration
     * document.
     *
     * @return string name
     */
    public String getIntegrationServiceName()
    {
        return integrationServiceName;
    }


    /* ========================================================
     * Set up the forLineage flag
     */

    /**
     * Return whether retrieval requests from this service are to include elements with the Memento classification attached or not.
     *
     * @return boolean flag
     */
    public boolean isForLineage()
    {
        return forLineage;
    }


    /**
     * Set up whether retrieval requests from this service are to include elements with the Memento classification attached or not.
     *
     * @param forLineage boolean flag
     */
    public void setForLineage(boolean forLineage)
    {
        this.forLineage = forLineage;
    }


    /* ======================================================================================
     * Register a listener to receive open lineage events.
     */

    /**
     * The listener is implemented by the integration connector.  Once it is registered with the context, its processOpenLineageRunEvent()
     * method is called each time an open lineage event is published to the Lineage Integrator OMIS.
     *
     * @param listener listener to call
     */
    public void registerListener(OpenLineageEventListener listener)
    {
        openLineageListenerManager.registerListener(listener);
    }


    /**
     * Called each time an integration connector wishes to publish an open lineage run event.  The event is formatted and passed to each of the
     * registered open lineage event listeners.
     *
     * @param rawEvent json payload to send for the event
     */
    public void publishOpenLineageRunEvent(String rawEvent)
    {
        openLineageListenerManager.publishOpenLineageRunEvent(rawEvent);
    }


    /**
     * Called each time an open lineage run event is published to the Lineage Integrator OMIS.  The integration connector is able to
     * work with the formatted event using the Egeria beans or reformat the open lineage run event using the supplied open lineage backend beans
     * or another set of beans.
     *
     * @param event bean for the event
     */
    public void publishOpenLineageRunEvent(OpenLineageRunEvent event) { openLineageListenerManager.publishOpenLineageRunEvent(event); }


    /* ========================================================
     * Register for inbound events from the Asset Manager OMAS OutTopic
     */

    /**
     * Register a listener object that will be passed each of the events published by the Asset Manager OMAS.
     *
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerListener(AssetManagerEventListener listener) throws InvalidParameterException,
                                                                            ConnectionCheckedException,
                                                                            ConnectorCheckedException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        eventClient.registerListener(userId, listener);
    }


    /* ======================================================================================
     * The Asset entity is the top level element to describe an implemented data asset such as a data store or data set.
     */

    /**
     * Create a new metadata element to represent the root of a data asset.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param assetProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDataAsset(boolean             assetManagerIsHome,
                                  DataAssetProperties assetProperties) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return dataAssetExchangeClient.createDataAsset(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       assetManagerIsHome,
                                                       null,
                                                       assetProperties);
    }


    /**
     * Create a new metadata element to represent an asset using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new asset.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDataAssetFromTemplate(boolean            assetManagerIsHome,
                                              String             templateGUID,
                                              TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return dataAssetExchangeClient.createDataAssetFromTemplate(userId,
                                                                   assetManagerGUID,
                                                                   assetManagerName,
                                                                   assetManagerIsHome,
                                                                   templateGUID,
                                                                   null,
                                                                   templateProperties);
    }


    /**
     * Update the metadata element representing a data source.
     *
     * @param assetGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param assetProperties new properties for this element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDataAsset(String              assetGUID,
                                boolean             isMergeUpdate,
                                DataAssetProperties assetProperties,
                                Date                effectiveTime) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        dataAssetExchangeClient.updateDataAsset(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                assetGUID,
                                                null,
                                                isMergeUpdate,
                                                assetProperties,
                                                effectiveTime,
                                                forLineage,
                                                forDuplicateProcessing);
    }


    /**
     * Update the zones for the data asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Asset Manager OMAS).
     *
     * @param assetGUID unique identifier of the metadata element to publish
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishDataAsset(String assetGUID,
                                 Date   effectiveTime) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        dataAssetExchangeClient.publishDataAsset(userId,
                                                 assetManagerGUID,
                                                 assetManagerName,
                                                 assetGUID,
                                                 effectiveTime,
                                                 forLineage,
                                                 forDuplicateProcessing);
    }


    /**
     * Update the zones for the data asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Asset Manager OMAS.  This is the setting when the database is first created).
     *
     * @param assetGUID unique identifier of the metadata element to withdraw
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawDataAsset(String assetGUID,
                                  Date   effectiveTime) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        dataAssetExchangeClient.withdrawDataAsset(userId,
                                                  assetManagerGUID,
                                                  assetManagerName,
                                                  assetGUID,
                                                  effectiveTime,
                                                  forLineage,
                                                  forDuplicateProcessing);
    }


    /**
     * Remove the metadata element representing a data asset.  This will delete the asset and all anchored
     * elements such as schema and comments.
     *
     * @param assetGUID unique identifier of the metadata element to remove
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDataAsset(String assetGUID,
                                Date   effectiveTime) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        dataAssetExchangeClient.removeDataAsset(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                assetGUID,
                                                null,
                                                effectiveTime,
                                                forLineage,
                                                forDuplicateProcessing);
    }


    /**
     * Classify the data asset to indicate that it can be used as reference data.
     *
     * @param assetGUID unique identifier of the metadata element to update
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setDataAssetAsReferenceData(String assetGUID,
                                            Date   effectiveTime) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        dataAssetExchangeClient.setDataAssetAsReferenceData(userId,
                                                            assetManagerGUID,
                                                            assetManagerName,
                                                            assetGUID,
                                                            null,
                                                            effectiveTime,
                                                            forLineage,
                                                            forDuplicateProcessing);
    }


    /**
     * Remove the reference data designation from the asset.
     *
     * @param assetGUID unique identifier of the metadata element to update
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearDataAssetAsReferenceData(String assetGUID,
                                              Date   effectiveTime) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        dataAssetExchangeClient.clearDataAssetAsReferenceData(userId,
                                                              assetManagerGUID,
                                                              assetManagerName,
                                                              assetGUID,
                                                              null,
                                                              effectiveTime,
                                                              forLineage,
                                                              forDuplicateProcessing);
    }


    /**
     * Retrieve the list of data asset metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataAssetElement> findDataAssets(String searchString,
                                                 int    startFrom,
                                                 int    pageSize,
                                                 Date   effectiveTime) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return dataAssetExchangeClient.findDataAssets(userId, assetManagerGUID, assetManagerName, searchString, startFrom, pageSize, effectiveTime, forLineage,
                                                      forDuplicateProcessing);
    }


    /**
     * Retrieve the list of data asset metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataAssetElement> getDataAssetsByName(String name,
                                                      int    startFrom,
                                                      int    pageSize,
                                                      Date   effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return dataAssetExchangeClient.getDataAssetsByName(userId, assetManagerGUID, assetManagerName, name, startFrom, pageSize, effectiveTime, forLineage,
                                                           forDuplicateProcessing);
    }


    /**
     * Retrieve the list of assets created on behalf of this asset manager.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataAssetElement> getDataAssetsForAssetManager(int  startFrom,
                                                               int  pageSize,
                                                               Date effectiveTime) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return dataAssetExchangeClient.getDataAssetsForAssetManager(userId, assetManagerGUID, assetManagerName, startFrom, pageSize, effectiveTime, forLineage,
                                                                    forDuplicateProcessing);
    }


    /**
     * Retrieve the asset metadata element with the supplied unique identifier.
     *
     * @param openMetadataGUID unique identifier of the requested metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataAssetElement getDataAssetByGUID(String openMetadataGUID,
                                               Date   effectiveTime) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return dataAssetExchangeClient.getDataAssetByGUID(userId, assetManagerGUID, assetManagerName, openMetadataGUID, effectiveTime, forLineage,
                                                          forDuplicateProcessing);
    }




    /* =====================================================================================================================
     * A schemaType describes the structure of a data asset, process or port
     */

    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this schema element
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaType(boolean              assetManagerIsHome,
                                   SchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return dataAssetExchangeClient.createSchemaType(userId,
                                                        assetManagerGUID,
                                                        assetManagerName,
                                                        assetManagerIsHome,
                                                        null,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        schemaTypeProperties);
    }


    /**
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this schema element
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaTypeFromTemplate(boolean            assetManagerIsHome,
                                               String             templateGUID,
                                               TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return dataAssetExchangeClient.createSchemaTypeFromTemplate(userId,
                                                                    assetManagerGUID,
                                                                    assetManagerName,
                                                                    assetManagerIsHome,
                                                                    templateGUID,
                                                                    null,
                                                                    templateProperties);
    }


    /**
     * Update the metadata element representing a schema type.
     *
     * @param schemaTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param schemaTypeProperties new properties for the metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaType(String               schemaTypeGUID,
                                 boolean              isMergeUpdate,
                                 SchemaTypeProperties schemaTypeProperties,
                                 Date                 effectiveTime) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        dataAssetExchangeClient.updateSchemaType(userId,
                                                 assetManagerGUID,
                                                 assetManagerName,
                                                 schemaTypeGUID,
                                                 null,
                                                 isMergeUpdate,
                                                 schemaTypeProperties,
                                                 effectiveTime,
                                                 forLineage,
                                                 forDuplicateProcessing);
    }


    /**
     * Connect a schema type to a data asset, process or port.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param schemaTypeGUID unique identifier of the schema type to connect
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param properties properties for the relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSchemaTypeParent(boolean                assetManagerIsHome,
                                      String                 schemaTypeGUID,
                                      String                 parentElementGUID,
                                      String                 parentElementTypeName,
                                      RelationshipProperties properties,
                                      Date                   effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        dataAssetExchangeClient.setupSchemaTypeParent(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      assetManagerIsHome,
                                                      schemaTypeGUID,
                                                      parentElementGUID,
                                                      parentElementTypeName,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing,
                                                      properties);
    }


    /**
     * Remove the relationship between a schema type and its parent data asset, process or port.
     *
     * @param schemaTypeGUID unique identifier of the schema type to connect
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaTypeParent(String schemaTypeGUID,
                                      String parentElementGUID,
                                      String parentElementTypeName,
                                      Date   effectiveTime) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        dataAssetExchangeClient.clearSchemaTypeParent(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      schemaTypeGUID,
                                                      parentElementGUID,
                                                      parentElementTypeName,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
    }


    /**
     * Remove the metadata element representing a schema type.
     *
     * @param schemaTypeGUID unique identifier of the metadata element to remove
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSchemaType(String schemaTypeGUID,
                                 Date   effectiveTime) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        dataAssetExchangeClient.removeSchemaType(userId,
                                                 assetManagerGUID,
                                                 assetManagerName,
                                                 schemaTypeGUID,
                                                 null,
                                                 effectiveTime,
                                                 forLineage,
                                                 forDuplicateProcessing);
    }


    /**
     * Retrieve the list of schema type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement> findSchemaType(String searchString,
                                                  int    startFrom,
                                                  int    pageSize,
                                                  Date   effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return dataAssetExchangeClient.findSchemaType(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      searchString,
                                                      startFrom,
                                                      pageSize,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
    }


    /**
     * Return the schema type associated with a specific open metadata element (data asset, process or port).
     *
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return metadata element describing the schema type associated with the requested parent element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElement getSchemaTypeForElement(String parentElementGUID,
                                                     String parentElementTypeName,
                                                     Date   effectiveTime) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        return dataAssetExchangeClient.getSchemaTypeForElement(userId,
                                                               assetManagerGUID,
                                                               assetManagerName,
                                                               parentElementGUID,
                                                               parentElementTypeName,
                                                               effectiveTime,
                                                               forLineage,
                                                               forDuplicateProcessing);
    }


    /**
     * Retrieve the list of schema type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement>   getSchemaTypeByName(String name,
                                                         int    startFrom,
                                                         int    pageSize,
                                                         Date   effectiveTime) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return dataAssetExchangeClient.getSchemaTypeByName(userId,
                                                           assetManagerGUID,
                                                           assetManagerName,
                                                           name,
                                                           startFrom,
                                                           pageSize,
                                                           effectiveTime,
                                                           forLineage,
                                                           forDuplicateProcessing);
    }


    /**
     * Retrieve the schema type metadata element with the supplied unique identifier.
     *
     * @param schemaTypeGUID unique identifier of the requested metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElement getSchemaTypeByGUID(String schemaTypeGUID,
                                                 Date   effectiveTime) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return dataAssetExchangeClient.getSchemaTypeByGUID(userId, assetManagerGUID, assetManagerName, schemaTypeGUID, effectiveTime, forLineage,
                                                           forDuplicateProcessing);
    }


    /**
     * Retrieve the header of the metadata element connected to a schema type.
     *
     * @param schemaTypeGUID unique identifier of the requested metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return header for parent element (data asset, process, port)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ElementHeader getSchemaTypeParent(String schemaTypeGUID,
                                             Date   effectiveTime) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return dataAssetExchangeClient.getSchemaTypeParent(userId, assetManagerGUID, assetManagerName, schemaTypeGUID, effectiveTime, forLineage,
                                                           forDuplicateProcessing);
    }


    /* ===============================================================================
     * A schemaType typically contains many schema attributes, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a schema attribute.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this schema attribute
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param schemaAttributeProperties properties for the schema attribute
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaAttribute(boolean                   assetManagerIsHome,
                                        String                    schemaElementGUID,
                                        SchemaAttributeProperties schemaAttributeProperties,
                                        Date                      effectiveTime) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return dataAssetExchangeClient.createSchemaAttribute(userId,
                                                             assetManagerGUID,
                                                             assetManagerName,
                                                             assetManagerIsHome,
                                                             schemaElementGUID,
                                                             null,
                                                             schemaAttributeProperties,
                                                             effectiveTime,
                                                             forLineage,
                                                             forDuplicateProcessing);
    }


    /**
     * Create a new metadata element to represent a schema attribute using an existing metadata element as a template.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this schema attribute
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaAttributeFromTemplate(boolean            assetManagerIsHome,
                                                    String             schemaElementGUID,
                                                    String             templateGUID,
                                                    TemplateProperties templateProperties,
                                                    Date               effectiveTime) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return dataAssetExchangeClient.createSchemaAttributeFromTemplate(userId,
                                                                         assetManagerGUID,
                                                                         assetManagerName,
                                                                         assetManagerIsHome,
                                                                         schemaElementGUID,
                                                                         templateGUID,
                                                                         null,
                                                                         templateProperties,
                                                                         effectiveTime,
                                                                         forLineage,
                                                                         forDuplicateProcessing);
    }


    /**
     * Update the properties of the metadata element representing a schema attribute.
     *
     * @param schemaAttributeGUID unique identifier of the schema attribute to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param schemaAttributeProperties new properties for the schema attribute
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaAttribute(String                    schemaAttributeGUID,
                                      boolean                   isMergeUpdate,
                                      SchemaAttributeProperties schemaAttributeProperties,
                                      Date                      effectiveTime) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        dataAssetExchangeClient.updateSchemaAttribute(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      schemaAttributeGUID,
                                                      null,
                                                      isMergeUpdate,
                                                      schemaAttributeProperties,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
    }


    /**
     * Classify the schema type (or attribute if type is embedded) to indicate that it is a calculated value.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this schema attribute
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param formula description of how the value is calculated
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setSchemaElementAsCalculatedValue(boolean assetManagerIsHome,
                                                  String  schemaElementGUID,
                                                  String  formula,
                                                  Date    effectiveTime) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        dataAssetExchangeClient.setSchemaElementAsCalculatedValue(userId,
                                                                  assetManagerGUID,
                                                                  assetManagerName,
                                                                  assetManagerIsHome,
                                                                  schemaElementGUID,
                                                                  null,
                                                                  formula,
                                                                  effectiveTime,
                                                                  forLineage,
                                                                  forDuplicateProcessing);
    }


    /**
     * Remove the calculated value designation from the schema element.
     *
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaElementAsCalculatedValue(String schemaElementGUID,
                                                    Date   effectiveTime) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        dataAssetExchangeClient.clearSchemaElementAsCalculatedValue(userId,
                                                                    assetManagerGUID,
                                                                    assetManagerName,
                                                                    schemaElementGUID,
                                                                    null,
                                                                    effectiveTime,
                                                                    forLineage,
                                                                    forDuplicateProcessing);
    }


    /**
     * Classify the column schema attribute to indicate that it describes a primary key.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this classification
     * @param schemaAttributeGUID unique identifier of the metadata element to update
     * @param schemaAttributeExternalIdentifier unique identifier of the schema attribute in the external asset manager
     * @param primaryKeyName name of the primary key (if different from the column name)
     * @param primaryKeyPattern key pattern used to maintain the primary key
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupColumnAsPrimaryKey(boolean    assetManagerIsHome,
                                        String     schemaAttributeGUID,
                                        String     schemaAttributeExternalIdentifier,
                                        String     primaryKeyName,
                                        KeyPattern primaryKeyPattern,
                                        Date       effectiveTime) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        dataAssetExchangeClient.setupColumnAsPrimaryKey(userId,
                                                        assetManagerGUID,
                                                        assetManagerName,
                                                        assetManagerIsHome,
                                                        schemaAttributeGUID,
                                                        schemaAttributeExternalIdentifier,
                                                        primaryKeyName,
                                                        primaryKeyPattern,
                                                        effectiveTime,
                                                        forLineage,
                                                        forDuplicateProcessing);
    }


    /**
     * Remove the primary key designation from the schema attribute.
     *
     * @param schemaAttributeGUID unique identifier of the metadata element to update
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearColumnAsPrimaryKey(String schemaAttributeGUID,
                                        Date   effectiveTime) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        dataAssetExchangeClient.clearColumnAsPrimaryKey(userId,
                                                        assetManagerGUID,
                                                        assetManagerName,
                                                        schemaAttributeGUID,
                                                        null,
                                                        effectiveTime,
                                                        forLineage,
                                                        forDuplicateProcessing);
    }


    /**
     * Link two schema attributes together to show a foreign key relationship.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param primaryKeyGUID unique identifier of the derived schema element
     * @param foreignKeyGUID unique identifier of the query target schema element
     * @param foreignKeyProperties properties for the foreign key relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupForeignKeyRelationship(boolean              assetManagerIsHome,
                                            String               primaryKeyGUID,
                                            String               foreignKeyGUID,
                                            ForeignKeyProperties foreignKeyProperties,
                                            Date                 effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        dataAssetExchangeClient.setupForeignKeyRelationship(userId,
                                                            assetManagerGUID,
                                                            assetManagerName,
                                                            assetManagerIsHome,
                                                            primaryKeyGUID,
                                                            foreignKeyGUID,
                                                            foreignKeyProperties,
                                                            effectiveTime,
                                                            forLineage,
                                                            forDuplicateProcessing);
    }


    /**
     * Update the relationship properties for the query target.
     *
     * @param primaryKeyGUID unique identifier of the derived schema element
     * @param foreignKeyGUID unique identifier of the query target schema element
     * @param foreignKeyProperties properties for the foreign key relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateForeignKeyRelationship(String               primaryKeyGUID,
                                             String               foreignKeyGUID,
                                             ForeignKeyProperties foreignKeyProperties,
                                             Date                 effectiveTime) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        dataAssetExchangeClient.updateForeignKeyRelationship(userId,
                                                             assetManagerGUID,
                                                             assetManagerName,
                                                             primaryKeyGUID,
                                                             foreignKeyGUID,
                                                             foreignKeyProperties,
                                                             effectiveTime,
                                                             forLineage,
                                                             forDuplicateProcessing);
    }


    /**
     * Remove the foreign key relationship between two schema elements.
     *
     * @param primaryKeyGUID unique identifier of the derived schema element
     * @param foreignKeyGUID unique identifier of the query target schema element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearForeignKeyRelationship(String primaryKeyGUID,
                                            String foreignKeyGUID,
                                            Date   effectiveTime) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        dataAssetExchangeClient.clearForeignKeyRelationship(userId,
                                                            assetManagerGUID,
                                                            assetManagerName,
                                                            primaryKeyGUID,
                                                            foreignKeyGUID,
                                                            effectiveTime,
                                                            forLineage,
                                                            forDuplicateProcessing);
    }


    /**
     * Remove the metadata element representing a schema attribute.
     *
     * @param schemaAttributeGUID unique identifier of the metadata element to remove
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSchemaAttribute(String schemaAttributeGUID,
                                      Date   effectiveTime) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        dataAssetExchangeClient.removeSchemaAttribute(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      schemaAttributeGUID,
                                                      null,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
    }


    /**
     * Retrieve the list of schema attribute metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement>   findSchemaAttributes(String searchString,
                                                               int    startFrom,
                                                               int    pageSize,
                                                               Date   effectiveTime) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return dataAssetExchangeClient.findSchemaAttributes(userId, assetManagerGUID, assetManagerName, searchString, startFrom, pageSize, effectiveTime, forLineage,
                                                            forDuplicateProcessing);
    }


    /**
     * Retrieve the list of schema attributes associated with a schema element.
     *
     * @param parentSchemaElementGUID unique identifier of the schema element of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement>    getNestedSchemaAttributes(String parentSchemaElementGUID,
                                                                     int    startFrom,
                                                                     int    pageSize,
                                                                     Date   effectiveTime) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return dataAssetExchangeClient.getNestedSchemaAttributes(userId, assetManagerGUID, assetManagerName, parentSchemaElementGUID, startFrom, pageSize, effectiveTime, forLineage,
                                                                 forDuplicateProcessing);
    }


    /**
     * Retrieve the list of schema attribute metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement>   getSchemaAttributesByName(String name,
                                                                    int    startFrom,
                                                                    int    pageSize,
                                                                    Date   effectiveTime) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return dataAssetExchangeClient.getSchemaAttributesByName(userId, assetManagerGUID, assetManagerName, name, startFrom, pageSize, effectiveTime, forLineage,
                                                                 forDuplicateProcessing);
    }


    /**
     * Retrieve the schema attribute metadata element with the supplied unique identifier.
     *
     * @param schemaAttributeGUID unique identifier of the requested metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributeElement getSchemaAttributeByGUID(String schemaAttributeGUID,
                                                           Date   effectiveTime) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return dataAssetExchangeClient.getSchemaAttributeByGUID(userId, assetManagerGUID, assetManagerName, schemaAttributeGUID, effectiveTime, forLineage,
                                                                forDuplicateProcessing);
    }


    /* =====================================================================================================================
     * A process describes a well defined series of steps that gets something done.
     */

    /**
     * Create a new metadata element to represent a process.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this process
     * @param processStatus initial status of the process
     * @param processProperties properties about the process to store
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createProcess(boolean           assetManagerIsHome,
                                ProcessStatus     processStatus,
                                ProcessProperties processProperties) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return lineageExchangeClient.createProcess(userId,
                                                   assetManagerGUID,
                                                   assetManagerName,
                                                   assetManagerIsHome,
                                                   null,
                                                   processStatus,
                                                   processProperties);
    }


    /**
     * Create a new metadata element to represent a process using an existing metadata element as a template.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this process
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createProcessFromTemplate(boolean            assetManagerIsHome,
                                            String             templateGUID,
                                            TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return lineageExchangeClient.createProcessFromTemplate(userId,
                                                               assetManagerGUID,
                                                               assetManagerName,
                                                               assetManagerIsHome,
                                                               templateGUID,
                                                               null,
                                                               templateProperties);
    }


    /**
     * Update the metadata element representing a process.
     *
     * @param processGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param processProperties new properties for the metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateProcess(String            processGUID,
                              boolean           isMergeUpdate,
                              ProcessProperties processProperties,
                              Date              effectiveTime) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        lineageExchangeClient.updateProcess(userId,
                                            assetManagerGUID,
                                            assetManagerName,
                                            processGUID,
                                            null,
                                            isMergeUpdate,
                                            processProperties,
                                            effectiveTime,
                                            forLineage,
                                            forDuplicateProcessing);
    }


    /**
     * Update the status of the metadata element representing a process.
     *
     * @param processGUID unique identifier of the process to update
     * @param processStatus new status for the process
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateProcessStatus(String        processGUID,
                                    ProcessStatus processStatus,
                                    Date          effectiveTime) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        lineageExchangeClient.updateProcessStatus(userId,
                                                  assetManagerGUID,
                                                  assetManagerName,
                                                  processGUID,
                                                  null,
                                                  processStatus,
                                                  effectiveTime,
                                                  forLineage,
                                                  forDuplicateProcessing);
    }


    /**
     * Create a parent-child relationship between two processes.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param parentProcessGUID unique identifier of the process in the external asset manager that is to be the parent process
     * @param childProcessGUID unique identifier of the process in the external asset manager that is to be the nested sub-process
     * @param containmentProperties describes the ownership of the sub-process
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupProcessParent(String                       userId,
                                   String                       assetManagerGUID,
                                   String                       assetManagerName,
                                   boolean                      assetManagerIsHome,
                                   String                       parentProcessGUID,
                                   String                       childProcessGUID,
                                   ProcessContainmentProperties containmentProperties,
                                   Date                         effectiveTime) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        lineageExchangeClient.setupProcessParent(userId,
                                                 assetManagerGUID,
                                                 assetManagerName,
                                                 assetManagerIsHome,
                                                 parentProcessGUID,
                                                 childProcessGUID,
                                                 containmentProperties,
                                                 effectiveTime,
                                                 forLineage,
                                                 forDuplicateProcessing);
    }


    /**
     * Remove a parent-child relationship between two processes.
     *
     * @param parentProcessGUID unique identifier of the process in the external asset manager that is to be the parent process
     * @param childProcessGUID unique identifier of the process in the external asset manager that is to be the nested sub-process
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProcessParent(String parentProcessGUID,
                                   String childProcessGUID,
                                   Date   effectiveTime) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        lineageExchangeClient.clearProcessParent(userId,
                                                 assetManagerGUID,
                                                 assetManagerName,
                                                 parentProcessGUID,
                                                 childProcessGUID,
                                                 effectiveTime,
                                                 forLineage,
                                                 forDuplicateProcessing);
    }


    /**
     * Update the zones for the asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Asset Manager OMAS).
     *
     * @param processGUID unique identifier of the metadata element to publish
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishProcess(String processGUID,
                               Date   effectiveTime) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        lineageExchangeClient.publishProcess(userId,
                                             assetManagerGUID,
                                             assetManagerName,
                                             processGUID,
                                             effectiveTime,
                                             forLineage,
                                             forDuplicateProcessing);
    }


    /**
     * Update the zones for the asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Asset Manager OMAS.  This is the setting when the process is first created).
     *
     * @param processGUID unique identifier of the metadata element to withdraw
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawProcess(String processGUID,
                                Date   effectiveTime) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        lineageExchangeClient.withdrawProcess(userId,
                                              assetManagerGUID,
                                              assetManagerName,
                                              processGUID,
                                              effectiveTime,
                                              forLineage,
                                              forDuplicateProcessing);
    }


    /**
     * Remove the metadata element representing a process.
     *
     * @param processGUID unique identifier of the metadata element to remove
     * @param processExternalIdentifier unique identifier of the process in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeProcess(String processGUID,
                              String processExternalIdentifier,
                              Date   effectiveTime) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        lineageExchangeClient.removeProcess(userId,
                                            assetManagerGUID,
                                            assetManagerName,
                                            processGUID,
                                            processExternalIdentifier,
                                            effectiveTime,
                                            forLineage,
                                            forDuplicateProcessing);
    }


    /**
     * Retrieve the list of process metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessElement> findProcesses(String searchString,
                                              int    startFrom,
                                              int    pageSize,
                                              Date   effectiveTime) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return lineageExchangeClient.findProcesses(userId, assetManagerGUID, assetManagerName, searchString, startFrom, pageSize, effectiveTime, forLineage,
                                                   forDuplicateProcessing);
    }


    /**
     * Return the list of processes associated with the asset manager.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of metadata elements describing the processes associated with the requested asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessElement>   getProcessesForAssetManager(int    startFrom,
                                                              int    pageSize,
                                                              Date   effectiveTime) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return lineageExchangeClient.getProcessesForAssetManager(userId, assetManagerGUID, assetManagerName, startFrom, pageSize, effectiveTime, forLineage,
                                                                 forDuplicateProcessing);
    }


    /**
     * Retrieve the list of process metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessElement>   getProcessesByName(String name,
                                                     int    startFrom,
                                                     int    pageSize,
                                                     Date   effectiveTime) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        return lineageExchangeClient.getProcessesByName(userId, assetManagerGUID, assetManagerName, name, startFrom, pageSize, effectiveTime, forLineage,
                                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param processGUID unique identifier of the requested metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessElement getProcessByGUID(String processGUID,
                                           Date   effectiveTime) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        return lineageExchangeClient.getProcessByGUID(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      processGUID,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param processGUID unique identifier of the requested metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return parent process element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessElement getProcessParent(String processGUID,
                                           Date   effectiveTime) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        return lineageExchangeClient.getProcessParent(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      processGUID,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param processGUID unique identifier of the requested metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of process element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessElement> getSubProcesses(String processGUID,
                                                int    startFrom,
                                                int    pageSize,
                                                Date   effectiveTime) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return lineageExchangeClient.getSubProcesses(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     processGUID,
                                                     startFrom,
                                                     pageSize,
                                                     effectiveTime,
                                                     forLineage,
                                                     forDuplicateProcessing);
    }


    /* ===============================================================================
     * A process typically contains ports that show the flow of data and control to and from it.
     */

    /**
     * Create a new metadata element to represent a port.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this port
     * @param processGUID unique identifier of the process where the port is located
     * @param portProperties properties for the port
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier of the new metadata element for the port
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createPort(boolean             assetManagerIsHome,
                             String              processGUID,
                             PortProperties      portProperties,
                             Date                effectiveTime) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return lineageExchangeClient.createPort(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                assetManagerIsHome,
                                                processGUID,
                                                null,
                                                portProperties,
                                                effectiveTime,
                                                forLineage,
                                                forDuplicateProcessing);
    }


    /**
     * Update the properties of the metadata element representing a port.  This call replaces
     * all existing properties with the supplied properties.
     *
     * @param portGUID unique identifier of the port to update
     * @param portProperties new properties for the port
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updatePort(String         portGUID,
                           PortProperties portProperties,
                           Date           effectiveTime) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        lineageExchangeClient.updatePort(userId,
                                         assetManagerGUID,
                                         assetManagerName,
                                         portGUID,
                                         null,
                                         portProperties,
                                         effectiveTime,
                                         forLineage,
                                         forDuplicateProcessing);
    }


    /**
     * Link a port to a process.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param processGUID unique identifier of the process
     * @param portGUID unique identifier of the port
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupProcessPort(boolean assetManagerIsHome,
                                 String  processGUID,
                                 String  portGUID,
                                 Date    effectiveTime) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        lineageExchangeClient.setupProcessPort(userId,
                                               assetManagerGUID,
                                               assetManagerName,
                                               assetManagerIsHome,
                                               processGUID,
                                               portGUID,
                                               effectiveTime,
                                               forLineage,
                                               forDuplicateProcessing);
    }


    /**
     * Unlink a port from a process.
     *
     * @param processGUID unique identifier of the process
     * @param portGUID unique identifier of the port
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProcessPort(String processGUID,
                                 String portGUID,
                                 Date   effectiveTime) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        lineageExchangeClient.clearProcessPort(userId,
                                               assetManagerGUID,
                                               assetManagerName,
                                               processGUID,
                                               portGUID,
                                               effectiveTime,
                                               forLineage,
                                               forDuplicateProcessing);
    }


    /**
     * Link two ports together to show that portTwo is an implementation of portOne. (That is, portOne delegates to
     * portTwo.)
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param portOneGUID unique identifier of the port at end 1
     * @param portTwoGUID unique identifier of the port at end 2
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupPortDelegation(boolean assetManagerIsHome,
                                    String  portOneGUID,
                                    String  portTwoGUID,
                                    Date    effectiveTime) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        lineageExchangeClient.setupPortDelegation(userId,
                                                  assetManagerGUID,
                                                  assetManagerName,
                                                  assetManagerIsHome,
                                                  portOneGUID,
                                                  portTwoGUID,
                                                  effectiveTime,
                                                  forLineage,
                                                  forDuplicateProcessing);
    }


    /**
     * Remove the port delegation relationship between two ports.
     *
     * @param portOneGUID unique identifier of the port at end 1
     * @param portTwoGUID unique identifier of the port at end 2
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearPortDelegation(String portOneGUID,
                                    String portTwoGUID,
                                    Date   effectiveTime) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        lineageExchangeClient.clearPortDelegation(userId,
                                                  assetManagerGUID,
                                                  assetManagerName,
                                                  portOneGUID,
                                                  portTwoGUID,
                                                  effectiveTime,
                                                  forLineage,
                                                  forDuplicateProcessing);
    }


    /**
     * Link a schema type to a port to show the structure of data it accepts.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param portGUID unique identifier of the port
     * @param schemaTypeGUID unique identifier of the schemaType
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupPortSchemaType(boolean assetManagerIsHome,
                                    String  portGUID,
                                    String  schemaTypeGUID,
                                    Date    effectiveTime) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        lineageExchangeClient.setupPortSchemaType(userId,
                                                  assetManagerGUID,
                                                  assetManagerName,
                                                  assetManagerIsHome,
                                                  portGUID,
                                                  schemaTypeGUID,
                                                  effectiveTime,
                                                  forLineage,
                                                  forDuplicateProcessing);
    }


    /**
     * Remove the schema type from a port.
     *
     * @param portGUID unique identifier of the port
     * @param schemaTypeGUID unique identifier of the schemaType
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearPortSchemaType(String portGUID,
                                    String schemaTypeGUID,
                                    Date   effectiveTime) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        lineageExchangeClient.clearPortSchemaType(userId,
                                                  assetManagerGUID,
                                                  assetManagerName,
                                                  portGUID,
                                                  schemaTypeGUID,
                                                  effectiveTime,
                                                  forLineage,
                                                  forDuplicateProcessing);
    }


    /**
     * Remove the metadata element representing a port.
     *
     * @param portGUID unique identifier of the metadata element to remove
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removePort(String portGUID,
                           Date   effectiveTime) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        lineageExchangeClient.removePort(userId,
                                         assetManagerGUID,
                                         assetManagerName,
                                         portGUID,
                                         null,
                                         effectiveTime,
                                         forLineage,
                                         forDuplicateProcessing);
    }


    /**
     * Retrieve the list of port metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<PortElement>   findPorts(String searchString,
                                         int    startFrom,
                                         int    pageSize,
                                         Date   effectiveTime) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return lineageExchangeClient.findPorts(userId, assetManagerGUID, assetManagerName, searchString, startFrom, pageSize, effectiveTime, forLineage,
                                               forDuplicateProcessing);
    }


    /**
     * Retrieve the list of ports associated with a process.
     *
     * @param processGUID unique identifier of the process of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<PortElement>    getPortsForProcess(String processGUID,
                                                   int    startFrom,
                                                   int    pageSize,
                                                   Date   effectiveTime) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return lineageExchangeClient.getPortsForProcess(userId, assetManagerGUID, assetManagerName, processGUID, startFrom, pageSize, effectiveTime, forLineage,
                                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the list of ports that delegate to this port.
     *
     * @param portGUID unique identifier of the starting port
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<PortElement>  getPortUse(String portGUID,
                                         int    startFrom,
                                         int    pageSize,
                                         Date   effectiveTime) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return lineageExchangeClient.getPortUse(userId, assetManagerGUID, assetManagerName, portGUID, startFrom, pageSize, effectiveTime, forLineage,
                                                forDuplicateProcessing);
    }


    /**
     * Retrieve the port that this port delegates to.
     *
     * @param portGUID unique identifier of the starting port alias
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public PortElement getPortDelegation(String portGUID,
                                         Date   effectiveTime) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return lineageExchangeClient.getPortDelegation(userId, assetManagerGUID, assetManagerName, portGUID, effectiveTime, forLineage,
                                                       forDuplicateProcessing);
    }


    /**
     * Retrieve the list of port metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<PortElement>   getPortsByName(String name,
                                              int    startFrom,
                                              int    pageSize,
                                              Date   effectiveTime) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return lineageExchangeClient.getPortsByName(userId, assetManagerGUID, assetManagerName, name, startFrom, pageSize, effectiveTime, forLineage,
                                                    forDuplicateProcessing);
    }


    /**
     * Retrieve the port metadata element with the supplied unique identifier.
     *
     * @param portGUID unique identifier of the requested metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public PortElement getPortByGUID(String portGUID,
                                     Date   effectiveTime) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        return lineageExchangeClient.getPortByGUID(userId, assetManagerGUID, assetManagerName, portGUID, effectiveTime, forLineage,
                                                   forDuplicateProcessing);
    }


    /* ===============================================================================
     * General linkage and classifications
     */


    /**
     * Classify a port, process or asset as "BusinessSignificant" (this may affect the way that lineage is displayed).
     *
     * @param elementGUID unique identifier of the metadata element to update
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setBusinessSignificant(String elementGUID,
                                       Date   effectiveTime) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        lineageExchangeClient.setBusinessSignificant(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     elementGUID,
                                                     null,
                                                     effectiveTime,
                                                     forLineage,
                                                     forDuplicateProcessing);
    }


    /**
     * Remove the "BusinessSignificant" designation from the element.
     *
     * @param elementGUID unique identifier of the metadata element to update
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearBusinessSignificant(String elementGUID,
                                         Date   effectiveTime) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        lineageExchangeClient.clearBusinessSignificant(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       elementGUID,
                                                       null,
                                                       effectiveTime,
                                                       forLineage,
                                                       forDuplicateProcessing);
    }


    /**
     * Link two elements together to show that data flows from one to the other.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String setupDataFlow(boolean            assetManagerIsHome,
                                String             dataSupplierGUID,
                                String             dataConsumerGUID,
                                DataFlowProperties properties,
                                Date               effectiveTime) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return lineageExchangeClient.setupDataFlow(userId,
                                                   assetManagerGUID,
                                                   assetManagerName,
                                                   assetManagerIsHome,
                                                   dataSupplierGUID,
                                                   dataConsumerGUID,
                                                   properties,
                                                   effectiveTime,
                                                   forLineage,
                                                   forDuplicateProcessing);
    }


    /**
     * Retrieve the data flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one data flow relationships between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param qualifiedName unique identifier for this relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataFlowElement getDataFlow(String dataSupplierGUID,
                                       String dataConsumerGUID,
                                       String qualifiedName,
                                       Date   effectiveTime) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        return lineageExchangeClient.getDataFlow(userId, assetManagerGUID, assetManagerName, dataSupplierGUID, dataConsumerGUID, qualifiedName, effectiveTime, forLineage,
                                                 forDuplicateProcessing);
    }


    /**
     * Update relationship between two elements that shows that data flows from one to the other.
     *
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDataFlow(String             dataFlowGUID,
                               DataFlowProperties properties,
                               Date               effectiveTime) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        lineageExchangeClient.updateDataFlow(userId,
                                             assetManagerGUID,
                                             assetManagerName,
                                             dataFlowGUID,
                                             properties,
                                             effectiveTime,
                                             forLineage,
                                             forDuplicateProcessing);
    }


    /**
     * Remove the data flow relationship between two elements.
     *
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearDataFlow(String dataFlowGUID,
                              Date   effectiveTime) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        lineageExchangeClient.clearDataFlow(userId,
                                            assetManagerGUID,
                                            assetManagerName,
                                            dataFlowGUID,
                                            effectiveTime,
                                            forLineage,
                                            forDuplicateProcessing);
    }



    /**
     * Retrieve the data flow relationships linked from a specific element to the downstream consumers.
     *
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataFlowElement> getDataFlowConsumers(String dataSupplierGUID,
                                                      int    startFrom,
                                                      int    pageSize,
                                                      Date   effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return lineageExchangeClient.getDataFlowConsumers(userId, assetManagerGUID, assetManagerName, dataSupplierGUID, startFrom, pageSize, effectiveTime, forLineage,
                                                          forDuplicateProcessing);
    }


    /**
     * Retrieve the data flow relationships linked from a specific element to the upstream suppliers.
     *
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataFlowElement> getDataFlowSuppliers(String dataConsumerGUID,
                                                      int    startFrom,
                                                      int    pageSize,
                                                      Date   effectiveTime) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return lineageExchangeClient.getDataFlowSuppliers(userId, assetManagerGUID, assetManagerName, dataConsumerGUID, startFrom, pageSize, effectiveTime, forLineage,
                                                          forDuplicateProcessing);
    }


    /**
     * Link two elements to show that when one completes the next is started.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param currentStepGUID unique identifier of the previous step
     * @param nextStepGUID unique identifier of the next step
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier for the control flow relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String setupControlFlow(boolean               assetManagerIsHome,
                                   String                currentStepGUID,
                                   String                nextStepGUID,
                                   ControlFlowProperties properties,
                                   Date                  effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return lineageExchangeClient.setupControlFlow(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      assetManagerIsHome,
                                                      currentStepGUID,
                                                      nextStepGUID,
                                                      properties,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
    }


    /**
     * Retrieve the control flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one control flow relationships between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param currentStepGUID unique identifier of the previous step
     * @param nextStepGUID unique identifier of the next step
     * @param qualifiedName unique identifier for this relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ControlFlowElement getControlFlow(String currentStepGUID,
                                             String nextStepGUID,
                                             String qualifiedName,
                                             Date   effectiveTime) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return lineageExchangeClient.getControlFlow(userId, assetManagerGUID, assetManagerName, currentStepGUID, nextStepGUID, qualifiedName, effectiveTime, forLineage,
                                                    forDuplicateProcessing);
    }


    /**
     * Update the relationship between two elements that shows that when one completes the next is started.
     *
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateControlFlow(String                controlFlowGUID,
                                  ControlFlowProperties properties,
                                  Date                  effectiveTime) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        lineageExchangeClient.updateControlFlow(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                controlFlowGUID,
                                                properties,
                                                effectiveTime,
                                                forLineage,
                                                forDuplicateProcessing);
    }


    /**
     * Remove the control flow relationship between two elements.
     *
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearControlFlow(String controlFlowGUID,
                                 Date   effectiveTime) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        lineageExchangeClient.clearControlFlow(userId,
                                               assetManagerGUID,
                                               assetManagerName,
                                               controlFlowGUID,
                                               effectiveTime,
                                               forLineage,
                                               forDuplicateProcessing);
    }



    /**
     * Retrieve the control relationships linked from a specific element to the possible next elements in the process.
     *
     * @param currentStepGUID unique identifier of the current step
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ControlFlowElement> getControlFlowNextSteps(String currentStepGUID,
                                                            int    startFrom,
                                                            int    pageSize,
                                                            Date   effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        return lineageExchangeClient.getControlFlowNextSteps(userId, assetManagerGUID, assetManagerName, currentStepGUID, startFrom, pageSize, effectiveTime, forLineage,
                                                             forDuplicateProcessing);
    }


    /**
     * Retrieve the control relationships linked from a specific element to the possible previous elements in the process.
     *
     * @param currentStepGUID unique identifier of the current step
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ControlFlowElement> getControlFlowPreviousSteps(String currentStepGUID,
                                                                int    startFrom,
                                                                int    pageSize,
                                                                Date   effectiveTime) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return lineageExchangeClient.getControlFlowPreviousSteps(userId, assetManagerGUID, assetManagerName, currentStepGUID, startFrom, pageSize, effectiveTime, forLineage,
                                                                 forDuplicateProcessing);
    }


    /**
     * Link two elements together to show a request-response call between them.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param callerGUID unique identifier of the element that is making the call
     * @param calledGUID unique identifier of the element that is processing the call
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String setupProcessCall(boolean               assetManagerIsHome,
                                   String                callerGUID,
                                   String                calledGUID,
                                   ProcessCallProperties properties,
                                   Date                  effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return lineageExchangeClient.setupProcessCall(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      assetManagerIsHome,
                                                      callerGUID,
                                                      calledGUID,
                                                      properties,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
    }


    /**
     * Retrieve the process call relationship between two elements.  The qualifiedName is optional unless there
     * is more than one process call relationships between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param callerGUID unique identifier of the element that is making the call
     * @param calledGUID unique identifier of the element that is processing the call
     * @param qualifiedName unique identifier for this relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessCallElement getProcessCall(String callerGUID,
                                             String calledGUID,
                                             String qualifiedName,
                                             Date   effectiveTime) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return lineageExchangeClient.getProcessCall(userId, assetManagerGUID, assetManagerName, callerGUID, calledGUID, qualifiedName, effectiveTime, forLineage,
                                                    forDuplicateProcessing);
    }


    /**
     * Update the relationship between two elements that shows a request-response call between them.
     *
     * @param processCallGUID unique identifier of the process call relationship
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateProcessCall(String                processCallGUID,
                                  ProcessCallProperties properties,
                                  Date                  effectiveTime) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        lineageExchangeClient.updateProcessCall(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                processCallGUID,
                                                properties,
                                                effectiveTime,
                                                forLineage,
                                                forDuplicateProcessing);
    }


    /**
     * Remove the process call relationship.
     *
     * @param processCallGUID unique identifier of the process call relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProcessCall(String processCallGUID,
                                 Date   effectiveTime) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        lineageExchangeClient.clearProcessCall(userId,
                                               assetManagerGUID,
                                               assetManagerName,
                                               processCallGUID,
                                               effectiveTime,
                                               forLineage,
                                               forDuplicateProcessing);
    }


    /**
     * Retrieve the process call relationships linked from a specific element to the elements it calls.
     *
     * @param callerGUID unique identifier of the element that is making the call
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessCallElement> getProcessCalled(String callerGUID,
                                                     int    startFrom,
                                                     int    pageSize,
                                                     Date   effectiveTime) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        return lineageExchangeClient.getProcessCalled(userId, assetManagerGUID, assetManagerName, callerGUID, startFrom, pageSize, effectiveTime, forLineage,
                                                      forDuplicateProcessing);
    }


    /**
     * Retrieve the process call relationships linked from a specific element to its callers.
     *
     * @param calledGUID unique identifier of the element that is processing the call
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessCallElement> getProcessCallers(String calledGUID,
                                                      int    startFrom,
                                                      int    pageSize,
                                                      Date   effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return lineageExchangeClient.getProcessCallers(userId, assetManagerGUID, assetManagerName, calledGUID, startFrom, pageSize, effectiveTime, forLineage,
                                                       forDuplicateProcessing);
    }


    /**
     * Link to elements together to show that they are part of the lineage of the data that is moving
     * between the processes.  Typically, the lineage relationships stitch together processes and data assets
     * supported by different technologies.
     *
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupLineageMapping(String                   sourceElementGUID,
                                    String                   destinationElementGUID,
                                    LineageMappingProperties properties,
                                    Date                     effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        lineageExchangeClient.setupLineageMapping(userId,
                                                  assetManagerGUID,
                                                  assetManagerName,
                                                  sourceElementGUID,
                                                  destinationElementGUID,
                                                  properties,
                                                  effectiveTime,
                                                  forLineage,
                                                  forDuplicateProcessing);
    }


    /**
     * Update the lineage mapping relationship between two elements.
     *
     * @param lineageMappingGUID unique identifier of the lineage mapping relationship
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateLineageMapping(String                   lineageMappingGUID,
                                     LineageMappingProperties properties,
                                     Date                     effectiveTime) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
         lineageExchangeClient.updateLineageMapping(userId,
                                                    assetManagerGUID,
                                                    assetManagerName,
                                                    lineageMappingGUID,
                                                    properties,
                                                    effectiveTime,
                                                    forLineage,
                                                    forDuplicateProcessing);
    }


    /**
     * Remove the lineage mapping between two elements.
     *
     * @param lineageMappingGUID unique identifier of the source
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearLineageMapping(String lineageMappingGUID,
                                    Date   effectiveTime) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        lineageExchangeClient.clearLineageMapping(userId,
                                                  assetManagerGUID,
                                                  assetManagerName,
                                                  lineageMappingGUID,
                                                  effectiveTime,
                                                  forLineage,
                                                  forDuplicateProcessing);
    }


    /**
     * Retrieve the lineage mapping relationships linked from a specific source element to its destinations.
     *
     * @param sourceElementGUID unique identifier of the source
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of lineage mapping relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<LineageMappingElement> getDestinationLineageMappings(String sourceElementGUID,
                                                                     int    startFrom,
                                                                     int    pageSize,
                                                                     Date   effectiveTime) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        return lineageExchangeClient.getDestinationLineageMappings(userId, assetManagerGUID, assetManagerName, sourceElementGUID, startFrom, pageSize, effectiveTime, forLineage,
                                                                   forDuplicateProcessing);
    }


    /**
     * Retrieve the lineage mapping relationships linked from a specific destination element to its sources.
     *
     * @param destinationElementGUID unique identifier of the destination
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
      *
     * @return list of lineage mapping relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<LineageMappingElement> getSourceLineageMappings(String destinationElementGUID,
                                                                int    startFrom,
                                                                int    pageSize,
                                                                Date   effectiveTime) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return lineageExchangeClient.getSourceLineageMappings(userId, assetManagerGUID, assetManagerName, destinationElementGUID, startFrom, pageSize, effectiveTime, forLineage,
                                                              forDuplicateProcessing);
    }



    /**
     * Classify the element with the Memento classification to indicate that it has been logically deleted for by lineage requests.
     *
     * @param elementGUID unique identifier of the metadata element to update
     * @param elementExternalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addMementoClassification(String elementGUID,
                                         String elementExternalIdentifier,
                                         Date   effectiveTime) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        stewardshipExchangeClient.addMementoClassification(userId,
                                                           assetManagerGUID,
                                                           assetManagerName,
                                                           elementGUID,
                                                           elementExternalIdentifier,
                                                           effectiveTime,
                                                           forLineage,
                                                           forDuplicateProcessing);
    }


    /**
     * Remove the memento designation from the element.
     *
     * @param elementGUID unique identifier of the metadata element to update
     * @param elementExternalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearMementoClassification(String elementGUID,
                                           String elementExternalIdentifier,
                                           Date   effectiveTime) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        stewardshipExchangeClient.clearMementoClassification(userId,
                                                             assetManagerGUID,
                                                             assetManagerName,
                                                             elementGUID,
                                                             elementExternalIdentifier,
                                                             effectiveTime,
                                                             forLineage,
                                                             forDuplicateProcessing);
    }


    /**
     * Classify the element with the Incomplete classification to indicate that it has more details to come.
     *
     * @param elementGUID unique identifier of the metadata element to update
     * @param elementExternalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addIncompleteClassification(String elementGUID,
                                            String elementExternalIdentifier,
                                            Date   effectiveTime) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        stewardshipExchangeClient.addIncompleteClassification(userId,
                                                              assetManagerGUID,
                                                              assetManagerName,
                                                              elementGUID,
                                                              elementExternalIdentifier,
                                                              effectiveTime,
                                                              forLineage,
                                                              forDuplicateProcessing);
    }


    /**
     * Remove the Incomplete designation from the element.
     *
     * @param elementGUID unique identifier of the metadata element to update
     * @param elementExternalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearIncompleteClassification(String elementGUID,
                                              String elementExternalIdentifier,
                                              Date   effectiveTime) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        stewardshipExchangeClient.clearIncompleteClassification(userId,
                                                                assetManagerGUID,
                                                                assetManagerName,
                                                                elementGUID,
                                                                elementExternalIdentifier,
                                                                effectiveTime,
                                                                forLineage,
                                                                forDuplicateProcessing);
    }


    /**
     * Retrieve the list of governance action process metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GovernanceActionProcessElement> findGovernanceActionProcesses(String searchString,
                                                                              int    startFrom,
                                                                              int    pageSize) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        return governanceExchangeClient.findGovernanceActionProcesses(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of governance action process metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GovernanceActionProcessElement> getGovernanceActionProcessesByName(String name,
                                                                                   int    startFrom,
                                                                                   int    pageSize) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        return governanceExchangeClient.getGovernanceActionProcessesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the governance action process metadata element with the supplied unique identifier.
     *
     * @param processGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessElement getGovernanceActionProcessByGUID(String processGUID) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        return governanceExchangeClient.getGovernanceActionProcessByGUID(userId, processGUID);
    }


    /* =====================================================================================================================
     * A governance action type describes a step in a governance action process
     */


    /**
     * Retrieve the list of governance action type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GovernanceActionTypeElement> findGovernanceActionTypes(String searchString,
                                                                       int    startFrom,
                                                                       int    pageSize) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        return governanceExchangeClient.findGovernanceActionTypes(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of governance action type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GovernanceActionTypeElement> getGovernanceActionTypesByName(String name,
                                                                            int    startFrom,
                                                                            int    pageSize) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        return governanceExchangeClient.getGovernanceActionTypesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the governance action type metadata element with the supplied unique identifier.
     *
     * @param actionTypeGUID unique identifier of the governance action type
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionTypeElement getGovernanceActionTypeByGUID(String actionTypeGUID) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        return governanceExchangeClient.getGovernanceActionTypeByGUID(userId, actionTypeGUID);
    }


    /**
     * Return the governance action type that is the first step in a governance action process.
     *
     * @param processGUID unique identifier of the governance action process
     *
     * @return properties of the governance action type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionTypeElement getFirstActionType(String processGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return governanceExchangeClient.getFirstActionType(userId, processGUID);
    }


    /**
     * Return the lust of next action type defined for the governance action process.
     *
     * @param actionTypeGUID unique identifier of the current governance action type
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return return the list of relationships and attached governance action types.
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<NextGovernanceActionTypeElement> getNextGovernanceActionTypes(String actionTypeGUID,
                                                                              int    startFrom,
                                                                              int    pageSize) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        return governanceExchangeClient.getNextGovernanceActionTypes(userId, actionTypeGUID, startFrom, pageSize);
    }


    /**
     * Request the status of an executing governance action request.
     *
     * @param governanceActionGUID identifier of the governance action request.
     *
     * @return status enum
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public GovernanceActionElement getGovernanceAction(String governanceActionGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return governanceExchangeClient.getGovernanceAction(userId, governanceActionGUID);
    }


    /**
     * Retrieve the governance actions known to the server.
     *
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @return list of governance action elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public List<GovernanceActionElement>  getGovernanceActions(int    startFrom,
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        return governanceExchangeClient.getGovernanceActions(userId, startFrom, pageSize);
    }


    /**
     * Retrieve the governance actions that are still in process.
     *
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @return list of governance action elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public List<GovernanceActionElement>  getActiveGovernanceActions(int    startFrom,
                                                                     int    pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return governanceExchangeClient.getActiveGovernanceActions(userId, startFrom, pageSize);
    }
}
