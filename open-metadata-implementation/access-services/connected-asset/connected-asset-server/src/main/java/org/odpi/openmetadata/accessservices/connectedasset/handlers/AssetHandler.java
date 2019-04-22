/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.handlers;

import org.odpi.openmetadata.accessservices.connectedasset.converters.TypeConverter;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.UnrecognizedAssetGUIDException;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.UnrecognizedConnectionGUIDException;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.UnrecognizedGUIDException;
import org.odpi.openmetadata.accessservices.connectedasset.mappers.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.*;

/**
 * AssetHandler retrieves basic information about an Asset and caches it to allow the
 * the ConnectedAssetRESTServices to retrieve all it needs.  Each instance serves a single REST request.
 *
 * The calls to the metadata repositories happen in the constructor.  Then getters are available to
 * populate the response to the REST request.
 */
public class AssetHandler
{
    private static final String connectionTypeGUID                      = "114e9f8f-5ff3-4c32-bd37-a7eb42712253";
    private static final String connectionConnectorTypeRelationshipGUID = "e542cfc1-0b4b-42b9-9921-f0a5a88aaf96";
    private static final String connectionEndpointRelationshipGUID      = "887a7132-d6bc-4b92-a483-e80b60c86fb2";
    private static final String connectionToAssetRelationshipGUID       = "e777d660-8dbe-453e-8b83-903771f054c0";
    private static final String qualifiedNamePropertyName               = "qualifiedName";
    private static final String displayNamePropertyName                 = "displayName";
    private static final String additionalPropertiesName                = "additionalProperties";
    private static final String securePropertiesName                    = "securedProperties";
    private static final String descriptionPropertyName                 = "description";
    private static final String connectorProviderPropertyName           = "connectorProviderClassName";
    private static final String ownerPropertyName                       = "owner";
    private static final String shortDescriptionPropertyName            = "assetSummary";
    private static final String endpointProtocolPropertyName            = "protocol";
    private static final String endpointEncryptionPropertyName          = "encryptionMethod";

    private static final int    MAX_PAGE_SIZE = 25;

    private String               serviceName;
    private OMRSRepositoryHelper repositoryHelper = null;
    private String               serverName       = null;
    private ErrorHandler         errorHandler;
    private TypeConverter        typeHandler      = new TypeConverter();

    private EntityDetail         assetEntity;
    private String               connectionGUID;
    private RepositoryHandler    repositoryHandler;

    private int        annotationCount            = 0;
    private int        certificationCount         = 0;
    private int        commentCount               = 0;
    private int        connectionCount            = 0;
    private int        externalIdentifierCount    = 0;
    private int        externalReferencesCount    = 0;
    private int        informalTagCount           = 0;
    private int        licenseCount               = 0;
    private int        likeCount                  = 0;
    private int        knownLocationsCount        = 0;
    private int        noteLogsCount              = 0;
    private int        ratingsCount               = 0;
    private int        relatedAssetCount          = 0;
    private int        relatedMediaReferenceCount = 0;
    private SchemaType schemaType                 = null;


    /**
     * Construct the asset handler with a link to the property server's connector and this access service's
     * official name.  Then retrieve the asset and its relationships.
     *
     * @param serviceName  name of this service
     * @param serverName  name of this server
     * @param repositoryConnector  connector to the property server.
     * @param userId        userId of user making request.
     * @param assetGUID     unique id for asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedAssetGUIDException the supplied GUID is not recognized by the metadata repository.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public AssetHandler(String                  serviceName,
                        String                  serverName,
                        OMRSRepositoryConnector repositoryConnector,
                        String                  userId,
                        String                  assetGUID) throws InvalidParameterException,
                                                                  UnrecognizedAssetGUIDException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.repositoryHelper = repositoryConnector.getRepositoryHelper();
        this.errorHandler = new ErrorHandler(repositoryConnector);

        this.repositoryHandler = new RepositoryHandler(serviceName, serverName, repositoryConnector);

        try
        {
            this.assetEntity = repositoryHandler.retrieveEntity(userId, AssetMapper.TYPE_NAME, assetGUID);
            this.countAssetAttachments(userId, assetGUID);
        }
        catch (UnrecognizedGUIDException  error)
        {
            throw new UnrecognizedAssetGUIDException(error.getReportedHTTPCode(),
                                                     error.getReportingClassName(),
                                                     error.getReportingActionDescription(),
                                                     error.getErrorMessage(),
                                                     error.getReportedSystemAction(),
                                                     error.getReportedUserAction(),
                                                     assetGUID);
        }
    }


    /**
     * Construct the asset handler with a link to the property server's connector and this access service's
     * official name.  Then retrieve the asset and its relationships.
     *
     * @param serviceName  name of this service
     * @param serverName  name of this server
     * @param repositoryConnector  connector to the property server.
     * @param userId        userId of user making request.
     * @param assetGUID     unique id for asset.
     * @param connectionGUID unique id for connection used to access asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedAssetGUIDException the supplied GUID is not recognized by the metadata repository.
     * @throws UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the metadata repository.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public AssetHandler(String                  serviceName,
                        String                  serverName,
                        OMRSRepositoryConnector repositoryConnector,
                        String                  userId,
                        String                  assetGUID,
                        String                  connectionGUID) throws InvalidParameterException,
                                                                       UnrecognizedAssetGUIDException,
                                                                       UnrecognizedConnectionGUIDException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        this(serviceName, serverName, repositoryConnector, userId, assetGUID);


        this.connectionGUID = connectionGUID;
    }


    /**
     * Return the asset bean.   This is extracted from the asset entity detail object retrieved from
     * one of the repositories.
     *
     * @return unique identifier
     */
    public Asset getAsset()
    {
        final  String   methodName = "getAsset";

        if (assetEntity != null)
        {
            Asset        asset = new Asset();

            asset.setType(typeHandler.getElementType(assetEntity.getType(),
                                                     assetEntity.getInstanceProvenanceType(),
                                                     assetEntity.getMetadataCollectionId(),
                                                     serverName,
                                                     assetEntity.getInstanceLicense()));
            asset.setGUID(assetEntity.getGUID());
            asset.setURL(assetEntity.getInstanceURL());

            InstanceProperties instanceProperties = new InstanceProperties(assetEntity.getProperties());
            if (instanceProperties != null)
            {
                asset.setQualifiedName(repositoryHelper.removeStringProperty(serviceName, qualifiedNamePropertyName, instanceProperties, methodName));
                asset.setDisplayName(repositoryHelper.removeStringProperty(serviceName, displayNamePropertyName, instanceProperties, methodName));
                asset.setDescription(repositoryHelper.removeStringProperty(serviceName, descriptionPropertyName, instanceProperties, methodName));
                asset.setOwner(repositoryHelper.removeStringProperty(serviceName, ownerPropertyName, instanceProperties, methodName));
                asset.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName, additionalPropertiesName, instanceProperties, methodName));
                asset.setZoneMembership(repositoryHelper.removeStringArrayProperty(serviceName, ownerPropertyName, instanceProperties, methodName));
                asset.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));

                  /*  protected List<Meaning>       meanings             = null;
                    protected String              shortDescription     = null;
                    classifications  */
            }

            return asset;
        }
        else
        {
            return null;
        }
    }


    /**
     * Calculate the number of attachments to this asset and their types.  The results are saved in the instance variables.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException the supplied GUID is not recognized by the metadata repository.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void countAssetAttachments(String userId,
                                       String assetGUID) throws InvalidParameterException,
                                                                UnrecognizedGUIDException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        int                   elementCount = 0;
        List<Relationship>    retrievedRelationships;

        do
        {
            retrievedRelationships = repositoryHandler.retrieveAllRelationships(userId, AssetMapper.TYPE_NAME, assetGUID, elementCount, MAX_PAGE_SIZE);

            if (retrievedRelationships != null)
            {
                if (retrievedRelationships.isEmpty())
                {
                    retrievedRelationships = null;
                }
                else
                {
                    certificationCount         = certificationCount         + countRelationshipsOfACertainType(retrievedRelationships, CertificationMapper.RELATIONSHIP_TYPE_NAME);
                    commentCount               = commentCount               + countRelationshipsOfACertainType(retrievedRelationships, CommentMapper.RELATIONSHIP_TYPE_NAME);
                    connectionCount            = connectionCount            + countRelationshipsOfACertainType(retrievedRelationships, ConnectionMapper.RELATIONSHIP_TYPE_NAME);
                    externalIdentifierCount    = externalIdentifierCount    + countRelationshipsOfACertainType(retrievedRelationships, ExternalIdentifierMapper.RELATIONSHIP_TYPE_NAME);
                    externalReferencesCount    = externalReferencesCount    + countRelationshipsOfACertainType(retrievedRelationships, ExternalReferenceMapper.RELATIONSHIP_TYPE_NAME);
                    informalTagCount           = informalTagCount           + countRelationshipsOfACertainType(retrievedRelationships, InformalTagMapper.RELATIONSHIP_TYPE_NAME);
                    licenseCount               = licenseCount               + countRelationshipsOfACertainType(retrievedRelationships, LicenseMapper.RELATIONSHIP_TYPE_NAME);
                    likeCount                  = likeCount                  + countRelationshipsOfACertainType(retrievedRelationships, LikeMapper.RELATIONSHIP_TYPE_NAME);
                    knownLocationsCount        = knownLocationsCount        + countRelationshipsOfACertainType(retrievedRelationships, LocationMapper.RELATIONSHIP_TYPE_NAME);
                    noteLogsCount              = noteLogsCount              + countRelationshipsOfACertainType(retrievedRelationships, NoteLogMapper.RELATIONSHIP_TYPE_NAME);
                    ratingsCount               = ratingsCount               + countRelationshipsOfACertainType(retrievedRelationships, RatingMapper.RELATIONSHIP_TYPE_NAME);
                    relatedAssetCount          = relatedAssetCount          + countRelationshipsOfACertainType(retrievedRelationships, AssetMapper.RELATIONSHIP_TYPE_NAME);
                    relatedMediaReferenceCount = relatedMediaReferenceCount + countRelationshipsOfACertainType(retrievedRelationships, RelatedMediaReferenceMapper.RELATIONSHIP_TYPE_NAME);

                    if (schemaType == null)
                    {
                        schemaType = getSchemaType(userId, retrievedRelationships);
                    }

                    if (retrievedRelationships.size() == MAX_PAGE_SIZE)
                    {
                        /*
                         * There may be more relationships to retrieve.
                         */
                        elementCount = elementCount + MAX_PAGE_SIZE;
                    }
                }
            }

        }  while (retrievedRelationships != null);
    }


    /**
     * Return the number of relationships (attachments) of a requested type on the asset.
     *
     * @param relationships list of asset relationships retrieved from the repository.
     * @param relationshipTypeName type name of interest.
     * @return count of the relationships.
     */
    private int countRelationshipsOfACertainType(List<Relationship>   relationships,
                                                 String               relationshipTypeName)
    {
        List<Relationship>   classifiedRelationships = repositoryHandler.getRelationshipsOfACertainType(relationships, relationshipTypeName);

        if (classifiedRelationships == null)
        {
            return 0;
        }
        else
        {
            return classifiedRelationships.size();
        }
    }


    /**
     * Retrieve the schema type (if any) attached to the asset.
     *
     * @param userId userId of the call to retrieve the schema type entity.
     * @param relationships list of relationships attached to the asset.
     * @return SchemaType bean or null.
     */
    private SchemaType getSchemaType(String               userId,
                                     List<Relationship>   relationships)
    {
        return null;
    }


    /**
     * Return the count of attached certification.
     *
     * @return count
     */
    public int getCertificationCount()
    {
        return certificationCount;
    }


    /**
     * Return the count of attached comments.
     *
     * @return count
     */
    public int getCommentCount()
    {
        return commentCount;
    }


    /**
     * Return the count of connections for the asset.
     *
     * @return count
     */
    public int getConnectionCount()
    {
        return connectionCount;
    }


    /**
     * Return the count of external identifiers for this asset.
     *
     * @return count
     */
    public int getExternalIdentifierCount()
    {
        return externalIdentifierCount;
    }


    /**
     * Return the count of attached external references.
     *
     * @return count
     */
    public int getExternalReferencesCount()
    {
        return externalReferencesCount;
    }


    /**
     * Return the count of attached informal tags.
     *
     * @return count
     */
    public int getInformalTagCount()
    {
        return informalTagCount;
    }


    /**
     * Return the count of license for this asset.
     *
     * @return count
     */
    public int getLicenseCount()
    {
        return licenseCount;
    }


    /**
     * Return the number of likes for the asset.
     *
     * @return count
     */
    public int getLikeCount()
    {
        return likeCount;
    }


    /**
     * Return the count of known locations.
     *
     * @return count
     */
    public int getKnownLocationsCount()
    {
        return knownLocationsCount;
    }


    /**
     * Return the count of attached note logs.
     *
     * @return count
     */
    public int getNoteLogsCount()
    {
        return noteLogsCount;
    }


    /**
     * Return the count of attached ratings.
     *
     * @return count
     */
    public int getRatingsCount()
    {
        return ratingsCount;
    }


    /**
     * Return the count of related assets.
     *
     * @return count
     */
    public int getRelatedAssetCount()
    {
        return relatedAssetCount;
    }


    /**
     * Return the count of related media references.
     *
     * @return count
     */
    public int getRelatedMediaReferenceCount()
    {
        return relatedMediaReferenceCount;
    }


    /**
     * Is there an attached schema?
     *
     * @return schema type bean
     */
    public SchemaType getSchemaType()
    {
        return schemaType;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */



    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */

}
