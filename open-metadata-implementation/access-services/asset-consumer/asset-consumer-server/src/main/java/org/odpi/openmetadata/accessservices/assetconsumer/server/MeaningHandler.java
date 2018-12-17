/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.server;


import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.AssetConsumerErrorCode;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.GlossaryTerm;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * ConnectionHandler retrieves Connection objects from the property server.  It runs server-side in the AssetConsumer
 * OMAS and retrieves Connections through the OMRSRepositoryConnector.
 */
class MeaningHandler
{
    private static final String glossaryTermGUID          = "0db3e6ec-f5ef-4d75-ae38-b7ee6fd6ec0a";
    private static final String qualifiedNamePropertyName = "qualifiedName";
    private static final String additionalPropertiesName  = "additionalProperties";
    private static final String displayNamePropertyName   = "displayName";
    private static final String summaryPropertyName       = "summary";
    private static final String descriptionPropertyName   = "description";
    private static final String examplesPropertyName      = "usage";
    private static final String abbreviationPropertyName  = "abbreviation";
    private static final String usagePropertyName         = "usage";

    private String               serviceName;
    private OMRSRepositoryHelper repositoryHelper = null;
    private String               serverName       = null;
    private ErrorHandler         errorHandler     = null;

    /**
     * Construct the connection handler with a link to the property server's connector and this access service's
     * official name.
     *
     * @param serviceName  name of this service
     * @param repositoryConnector  connector to the property server.
     */
    MeaningHandler(String                  serviceName,
                   OMRSRepositoryConnector repositoryConnector)
    {
        this.serviceName = serviceName;
        if (repositoryConnector != null)
        {
            this.repositoryHelper = repositoryConnector.getRepositoryHelper();
            this.serverName = repositoryConnector.getServerName();
            errorHandler = new ErrorHandler(repositoryConnector);
        }
    }


    /**
     * Returns the connection object corresponding to the supplied connection name.
     *
     * @param userId  String - userId of user making request.
     * @param name  this may be the qualifiedName or displayName of the connection.
     *
     * @return List of glossary terms retrieved from property server
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<GlossaryTerm> getMeaningsByName(String   userId,
                                         String   name) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final  String   methodName = "getMeaningsByName";
        final  String   nameParameter = "name";

        List<GlossaryTerm>  results = new ArrayList<>();

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateName(name, nameParameter, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        try
        {
            InstanceProperties properties;

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      qualifiedNamePropertyName,
                                                                      name,
                                                                      methodName);

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      displayNamePropertyName,
                                                                      name,
                                                                      methodName);

            List<EntityDetail> glossaryTermEntities = metadataCollection.findEntitiesByProperty(userId,
                                                                                                glossaryTermGUID,
                                                                                                properties,
                                                                                                MatchCriteria.ANY,
                                                                                                0,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                2);

            if (glossaryTermEntities == null)
            {
                return null;
            }
            else if (glossaryTermEntities.isEmpty())
            {
                return null;
            }
            else
            {
                for (EntityDetail  glossaryTermEntity : glossaryTermEntities)
                {
                    if (glossaryTermEntity != null)
                    {
                        results.add(this.getMeaningFromRepository(glossaryTermEntity));
                    }
                }
            }
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId,
                                                methodName,
                                                serverName,
                                                serviceName);
        }
        catch (Throwable  error)
        {
            errorHandler.handleRepositoryError(error,
                                               methodName,
                                               serverName,
                                               serviceName);
        }

        return null;
    }


    /**
     * Returns the connection object corresponding to the supplied connection GUID.
     *
     * @param userId  String - userId of user making request.
     * @param guid  the unique id for the glossary term within the property server.
     *
     * @return Glossary Term retrieved from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    GlossaryTerm getMeaningByGUID(String     userId,
                                  String     guid) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        final  String   methodName = "getMeaningByGUID";
        final  String   guidParameter = "guid";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(guid, guidParameter, methodName);

        OMRSMetadataCollection  metadataCollection = errorHandler.validateRepositoryConnector(methodName);
        EntityDetail            termEntity = null;

        try
        {
            termEntity = metadataCollection.getEntityDetail(userId, guid);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
        {
            AssetConsumerErrorCode errorCode = AssetConsumerErrorCode.TERM_NOT_FOUND;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(guid,
                                                                                                            serverName,
                                                                                                            error.getErrorMessage());

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                guidParameter);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException error)
        {
            AssetConsumerErrorCode errorCode = AssetConsumerErrorCode.PROXY_TERM_FOUND;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(guid,
                                                                                                            serverName,
                                                                                                            error.getErrorMessage());

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                guidParameter);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName, serverName, serviceName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, serverName, serviceName);
        }

        if (termEntity != null)
        {
            return this.getMeaningFromRepository(termEntity);
        }

        return null;
    }


    /**
     * Return a Glossary Term bean filled with values retrieved from the property server.
     *
     * @param termEntity  root entity object
     * @return GlossaryTerm bean
     */
    private GlossaryTerm  getMeaningFromRepository(EntityDetail            termEntity)
    {
        final  String   methodName = "getMeaningFromRepository";

        GlossaryTerm  glossaryTerm    = new GlossaryTerm();

        if (termEntity != null)
        {
            glossaryTerm.setGUID(termEntity.getGUID());

            InstanceType instanceType = termEntity.getType();
            if (instanceType != null)
            {
                glossaryTerm.setTypeName(termEntity.getInstanceURL());
                glossaryTerm.setTypeDescription(termEntity.getInstanceURL());
            }

            List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> entityClassifications = termEntity.getClassifications();
            if ((entityClassifications != null) && (! entityClassifications.isEmpty()))
            {
                List<org.odpi.openmetadata.accessservices.assetconsumer.properties.Classification> classifications = new ArrayList<>();

                for (org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification  entityClassification : entityClassifications )
                {
                    if (entityClassification != null)
                    {
                        org.odpi.openmetadata.accessservices.assetconsumer.properties.Classification  classification = new org.odpi.openmetadata.accessservices.assetconsumer.properties.Classification();

                        classification.setClassificationName(entityClassification.getName());

                        InstanceProperties classificationProperties = entityClassification.getProperties();

                        if (classificationProperties != null)
                        {
                            // TODO
                        }
                    }
                }

                glossaryTerm.setClassifications(classifications);
            }

            glossaryTerm.setQualifiedName(repositoryHelper.getStringProperty(serviceName, qualifiedNamePropertyName, termEntity.getProperties(), methodName));
            glossaryTerm.setDisplayName(repositoryHelper.getStringProperty(serviceName, displayNamePropertyName, termEntity.getProperties(), methodName));
            glossaryTerm.setSummary(repositoryHelper.getStringProperty(serviceName, summaryPropertyName, termEntity.getProperties(), methodName));
            glossaryTerm.setDescription(repositoryHelper.getStringProperty(serviceName, descriptionPropertyName, termEntity.getProperties(), methodName));
            glossaryTerm.setExamples(repositoryHelper.getStringProperty(serviceName, examplesPropertyName, termEntity.getProperties(), methodName));
            glossaryTerm.setAbbreviation(repositoryHelper.getStringProperty(serviceName, abbreviationPropertyName, termEntity.getProperties(), methodName));
            glossaryTerm.setUsage(repositoryHelper.getStringProperty(serviceName, usagePropertyName, termEntity.getProperties(), methodName));
            glossaryTerm.setAdditionalProperties(repositoryHelper.getMapFromProperty(serviceName, additionalPropertiesName, termEntity.getProperties(), methodName));
        }

        return glossaryTerm;
    }
}
