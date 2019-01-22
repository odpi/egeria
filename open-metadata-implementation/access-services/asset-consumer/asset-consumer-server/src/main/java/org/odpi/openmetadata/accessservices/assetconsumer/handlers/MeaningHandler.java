/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.handlers;


import org.odpi.openmetadata.accessservices.assetconsumer.AssetConsumerGlossaryInterface;
import org.odpi.openmetadata.accessservices.assetconsumer.converters.GlossaryTermConverter;
import org.odpi.openmetadata.accessservices.assetconsumer.converters.ReferenceableHeaderConverter;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.AssetConsumerErrorCode;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.assetconsumer.handlers.ErrorHandler;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.GlossaryTerm;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * MeaningHandler retrieves Glossary Term objects from the property server.  It runs server-side in the AssetConsumer
 * OMAS and retrieves Glossary Term entities through the OMRSRepositoryConnector.
 */
public class MeaningHandler implements AssetConsumerGlossaryInterface
{
    private static final String glossaryTermGUID          = "0db3e6ec-f5ef-4d75-ae38-b7ee6fd6ec0a";


    private String               serviceName;
    private OMRSRepositoryHelper repositoryHelper = null;
    private String               serverName       = null;
    private ErrorHandler         errorHandler     = null;

    /**
     * Construct the glossary term handler with a link to the property server's connector and this access service's
     * official name.
     *
     * @param serviceName  name of this service
     * @param repositoryConnector  connector to the property server.
     */
    public MeaningHandler(String                  serviceName,
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
     * Returns the glossary term object corresponding to the supplied term name.
     *
     * @param userId  String - userId of user making request.
     * @param name  this may be the qualifiedName or displayName of the term.
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return List of glossary terms retrieved from property server
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<GlossaryTerm> getMeaningByName(String    userId,
                                               String    name,
                                               int       startFrom,
                                               int       pageSize) throws InvalidParameterException, PropertyServerException,
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
                                                                      ReferenceableHeaderConverter.QUALIFIED_NAME_PROPERTY_NAME,
                                                                      name,
                                                                      methodName);

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      GlossaryTermConverter.DISPLAY_NAME_PROPERTY_NAME,
                                                                      name,
                                                                      methodName);

            List<EntityDetail> glossaryTermEntities = metadataCollection.findEntitiesByProperty(userId,
                                                                                                glossaryTermGUID,
                                                                                                properties,
                                                                                                MatchCriteria.ANY,
                                                                                                startFrom,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                pageSize);

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
                for (EntityDetail  termEntity : glossaryTermEntities)
                {
                    if (termEntity != null)
                    {
                        GlossaryTermConverter   converter = new GlossaryTermConverter(termEntity, repositoryHelper, serviceName);
                        results.add(converter.getBean());
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
     * Returns the glossary term object corresponding to the supplied glossary term GUID.
     *
     * @param userId  String - userId of user making request.
     * @param guid  the unique id for the glossary term within the property server.
     *
     * @return Glossary Term retrieved from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GlossaryTerm getMeaning(String     userId,
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
            GlossaryTermConverter   converter = new GlossaryTermConverter(termEntity, repositoryHelper, serviceName);
            return converter.getBean();
        }

        return null;
    }
}
