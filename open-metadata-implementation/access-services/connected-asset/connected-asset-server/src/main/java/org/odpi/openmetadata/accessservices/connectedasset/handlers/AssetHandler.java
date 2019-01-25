/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.server;

import org.odpi.openmetadata.accessservices.connectedasset.ffdc.ConnectedAssetErrorCode;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.UnrecognizedAssetGUIDException;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.UnrecognizedConnectionGUIDException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
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
class AssetHandler
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
    private static final String shortDescfriptionPropertyName           = "assetSummary";
    private static final String endpointProtocolPropertyName            = "protocol";
    private static final String endpointEncryptionPropertyName          = "encryptionMethod";

    private String               serviceName;
    private OMRSRepositoryHelper repositoryHelper = null;
    private String               serverName       = null;
    private ErrorHandler         errorHandler;
    private TypeHandler          typeHandler      = new TypeHandler();

    private EntityDetail         assetEntity;
    private List<Relationship>   assetRelationships;
    private String               connectionGUID;


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
    AssetHandler(String                  serviceName,
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
        this.serverName = repositoryConnector.getServerName();
        this.errorHandler = new ErrorHandler(repositoryConnector);
        this.assetEntity = retrieveAssetEntity(userId, assetGUID);
        this.assetRelationships = retrieveAssetRelationships(userId, assetGUID);
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
    AssetHandler(String                  serviceName,
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
     * Returns the entity object corresponding to the supplied asset GUID.
     *
     * @param userId  String - userId of user making request.
     * @param assetGUID  the unique id for the connection within the property server.
     *
     * @return Connection retrieved from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedAssetGUIDException the supplied GUID is not recognized by the metadata repository.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private EntityDetail retrieveAssetEntity(String     userId,
                                             String     assetGUID) throws InvalidParameterException,
                                                                          UnrecognizedAssetGUIDException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final  String   methodName = "retrieveAssetEntity";
        final  String   guidParameter = "assetGUID";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(assetGUID, guidParameter, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        try
        {
            return metadataCollection.getEntityDetail(userId, assetGUID);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
        {
            ConnectedAssetErrorCode errorCode = ConnectedAssetErrorCode.ASSET_NOT_FOUND;
            String                  errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(assetGUID,
                                                                                                                      serverName,
                                                                                                                      error.getErrorMessage());

            throw new UnrecognizedAssetGUIDException(errorCode.getHTTPErrorCode(),
                                                          this.getClass().getName(),
                                                          methodName,
                                                          errorMessage,
                                                          errorCode.getSystemAction(),
                                                          errorCode.getUserAction(),
                                                          assetGUID);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException error)
        {
            ConnectedAssetErrorCode errorCode = ConnectedAssetErrorCode.PROXY_CONNECTION_FOUND;
            String                  errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(assetGUID,
                                                                                                                      serverName,
                                                                                                                      error.getErrorMessage());

            throw new UnrecognizedAssetGUIDException(errorCode.getHTTPErrorCode(),
                                                     this.getClass().getName(),
                                                     methodName,
                                                     errorMessage,
                                                     errorCode.getSystemAction(),
                                                     errorCode.getUserAction(),
                                                     assetGUID);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName, serverName, serviceName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, serverName, serviceName);
        }

        return null;
    }


    /**
     * Returns the list of relationships attached to the identified asset entity from the metadata collections.
     *
     * @param userId  String - userId of user making request.
     * @param assetGUID  the unique id for the connection within the property server.
     *
     * @return Connection retrieved from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedAssetGUIDException the supplied GUID is not recognized by the metadata repository.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private List<Relationship> retrieveAssetRelationships(String     userId,
                                                          String     assetGUID) throws InvalidParameterException,
                                                                                       UnrecognizedAssetGUIDException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final  String   methodName = "retrieveAssetRelationships";
        final  String   guidParameter = "assetGUID";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(assetGUID, guidParameter, methodName);

        OMRSMetadataCollection  metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        try
        {
            return metadataCollection.getRelationshipsForEntity(userId,
                                                                assetGUID,
                                                                null,
                                                                0,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                0);

        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
        {
            ConnectedAssetErrorCode errorCode = ConnectedAssetErrorCode.ASSET_NOT_FOUND;
            String                  errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(assetGUID,
                                                                                                                      serverName,
                                                                                                                      error.getErrorMessage());

            throw new UnrecognizedAssetGUIDException(errorCode.getHTTPErrorCode(),
                                                     this.getClass().getName(),
                                                     methodName,
                                                     errorMessage,
                                                     errorCode.getSystemAction(),
                                                     errorCode.getUserAction(),
                                                     assetGUID);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName, serverName, serviceName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, serverName, serviceName);
        }

        return null;
    }





    /**
     * Extract an additional properties object from the instance properties within a map property value.
     *
     * @param propertyName  name of the property that is a map
     * @param properties  instance properties containing the map property
     * @param methodName  calling method
     * @return an AdditionalProperties object or null
     */
    private Map<String, Object> getAdditionalPropertiesFromEntity(String              propertyName,
                                                                  InstanceProperties  properties,
                                                                  String              methodName)
    {
        /*
         * Extract the map property
         */
        InstanceProperties mapProperty = repositoryHelper.getMapProperty(serviceName,
                                                                         propertyName,
                                                                         properties,
                                                                         methodName);

        if (mapProperty != null)
        {
            /*
             * The contents should be primitives.  Need to step through all of the property names
             * and add each primitive value to a map.  This map is then used to set up the additional properties
             * object for return
             */
            Iterator<String> additionalPropertyNames = mapProperty.getPropertyNames();

            if (additionalPropertyNames != null)
            {
                Map<String,Object> additionalPropertiesMap = new HashMap<>();

                while (additionalPropertyNames.hasNext())
                {
                    String                additionalPropertyName  = additionalPropertyNames.next();
                    InstancePropertyValue additionalPropertyValue = mapProperty.getPropertyValue(additionalPropertyName);

                    if (additionalPropertyValue != null)
                    {
                        /*
                         * If the property is not primitive it is ignored.
                         */
                        if (additionalPropertyValue.getInstancePropertyCategory() == InstancePropertyCategory.PRIMITIVE)
                        {
                            PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) additionalPropertyValue;

                            additionalPropertiesMap.put(additionalPropertyName, primitivePropertyValue.getPrimitiveValue());
                        }
                    }
                }

                if (additionalPropertiesMap.isEmpty())
                {
                    return null;
                }
                else
                {
                    return additionalPropertiesMap;
                }
            }
        }

        return null;
    }


    /**
     * Return the asset bean.   This is extracted from the asset entity detail object retrieved from
     * one of the repositories.
     *
     * @return unique identifier
     */
    Asset getAsset()
    {
        final  String   methodName = "getAsset";

        if (assetEntity != null)
        {
            Asset        asset = new Asset();

            asset.setType(typeHandler.getElementType(assetEntity.getType(),
                                                     assetEntity.getInstanceProvenanceType(),
                                                     assetEntity.getMetadataCollectionId(),
                                                     serverName));
            asset.setGUID(assetEntity.getGUID());
            asset.setURL(assetEntity.getInstanceURL());

            InstanceProperties instanceProperties = assetEntity.getProperties();
            if (instanceProperties != null)
            {
                Iterator<String> propertyNames = instanceProperties.getPropertyNames();

                while (propertyNames.hasNext())
                {
                    String propertyName = propertyNames.next();

                    if (propertyName != null)
                    {
                        asset.setQualifiedName(repositoryHelper.getStringProperty(serviceName, qualifiedNamePropertyName, instanceProperties, methodName));
                        asset.setDisplayName(repositoryHelper.getStringProperty(serviceName, displayNamePropertyName, instanceProperties, methodName));
                        asset.setDescription(repositoryHelper.getStringProperty(serviceName, descriptionPropertyName, instanceProperties, methodName));
                        asset.setOwner(repositoryHelper.getStringProperty(serviceName, ownerPropertyName, instanceProperties, methodName));
                        asset.setAdditionalProperties(repositoryHelper.getMapFromProperty(serviceName, additionalPropertiesName, instanceProperties, methodName));
                        asset.setZoneMembership(repositoryHelper.getStringArrayProperty(serviceName, ownerPropertyName, instanceProperties, methodName));
                    }
                }

                  /*  protected List<Meaning>       meanings             = null;
                    protected String              shortDescription     = null; */
            }

            return asset;
        }
        else
        {
            return null;
        }
    }


    /**
     * Return the count of attached annotations.
     *
     * @return count
     */
    int getAnnotationCount()
    {
        return 0;
    }


    /**
     * Return the count of attached certification.
     *
     * @return count
     */
    int getCertificationCount()
    {
        return 0;
    }


    /**
     * Return the count of attached comments.
     *
     * @return count
     */
    public int getCommentCount()
    {
        return 0;
    }


    /**
     * Return the count of connections for the asset.
     *
     * @return count
     */
    public int getConnectionCount()
    {
        return 0;
    }


    /**
     * Return the count of external identifiers for this asset.
     *
     * @return count
     */
    public int getExternalIdentifierCount()
    {
        return 0;
    }


    /**
     * Return the count of attached external references.
     *
     * @return count
     */
    int getExternalReferencesCount()
    {
        return 0;
    }


    /**
     * Return the count of attached informal tags.
     *
     * @return count
     */
    int getInformalTagCount()
    {
        return 0;
    }


    /**
     * Return the count of license for this asset.
     *
     * @return count
     */
    int getLicenseCount()
    {
        return 0;
    }


    /**
     * Return the number of likes for the asset.
     *
     * @return count
     */
    int getLikeCount()
    {
        return 0;
    }


    /**
     * Return the count of known locations.
     *
     * @return count
     */
    int getKnownLocationsCount()
    {
        return 0;
    }


    /**
     * Return the count of attached note logs.
     *
     * @return count
     */
    int getNoteLogsCount()
    {
        return 0;
    }


    /**
     * Return the count of attached ratings.
     *
     * @return count
     */
    int getRatingsCount()
    {
        return 0;
    }


    /**
     * Return the count of related assets.
     *
     * @return count
     */
    int getRelatedAssetCount()
    {
        return 0;
    }


    /**
     * Return the count of related media references.
     *
     * @return count
     */
    int getRelatedMediaReferenceCount()
    {
        return 0;
    }


    /**
     * Is there an attached schema?
     *
     * @return schema type bean
     */
    SchemaType getSchemaType()
    {
        return null;
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
