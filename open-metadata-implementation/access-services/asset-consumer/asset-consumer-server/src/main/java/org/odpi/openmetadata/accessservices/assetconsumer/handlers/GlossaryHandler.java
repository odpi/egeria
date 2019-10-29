/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.handlers;


import org.odpi.openmetadata.accessservices.assetconsumer.api.AssetConsumerGlossaryInterface;
import org.odpi.openmetadata.accessservices.assetconsumer.builders.GlossaryTermBuilder;
import org.odpi.openmetadata.accessservices.assetconsumer.converters.GlossaryTermConverter;
import org.odpi.openmetadata.accessservices.assetconsumer.mappers.GlossaryTermMapper;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.GlossaryTerm;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * GlossaryHandler retrieves Glossary Term objects from the property server.  It runs server-side in the AssetConsumer
 * OMAS and retrieves Glossary Term entities through the OMRSRepositoryConnector.
 */
public class GlossaryHandler implements AssetConsumerGlossaryInterface
{
    private String                  serviceName;
    private String                  serverName;
    private RepositoryHandler       repositoryHandler;
    private OMRSRepositoryHelper    repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;


    /**
     * Construct the discovery engine configuration handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param serviceName name of the consuming service
     * @param serverName name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper helper used by the converters
     * @param repositoryHandler handler for calling the repository services
     */
    public GlossaryHandler(String                  serviceName,
                           String                  serverName,
                           InvalidParameterHandler invalidParameterHandler,
                           OMRSRepositoryHelper    repositoryHelper,
                           RepositoryHandler       repositoryHandler)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
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
                                               int       pageSize) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final  String   methodName = "getMeaningsByName";
        final  String   nameParameter = "name";

        List<GlossaryTerm>  results = new ArrayList<>();

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameter, methodName);

        GlossaryTermBuilder glossaryTermBuilder = new GlossaryTermBuilder(name,
                                                                          name,
                                                                          repositoryHelper,
                                                                          serviceName,
                                                                          serverName);

        List<EntityDetail> glossaryTerms = repositoryHandler.getEntitiesByName(userId,
                                                                               glossaryTermBuilder.getSearchInstanceProperties(methodName),
                                                                               GlossaryTermMapper.GLOSSARY_TERM_TYPE_GUID,
                                                                               startFrom,
                                                                               pageSize,
                                                                               methodName);

        if (glossaryTerms != null)
        {
            for (EntityDetail entity : glossaryTerms)
            {
                if (entity != null)
                {
                    GlossaryTermConverter  converter = new GlossaryTermConverter(entity,
                                                                                 repositoryHelper,
                                                                                 serviceName);
                    results.add(converter.getBean());
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
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
    public List<GlossaryTerm> findMeanings(String    userId,
                                           String    name,
                                           int       startFrom,
                                           int       pageSize) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        final  String   methodName = "findMeanings";
        final  String   nameParameter = "name";

        List<GlossaryTerm>  results = new ArrayList<>();

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameter, methodName);

        GlossaryTermBuilder glossaryTermBuilder = new GlossaryTermBuilder(name,
                                                                          name,
                                                                          repositoryHelper,
                                                                          serviceName,
                                                                          serverName);

        List<EntityDetail> glossaryTerms = repositoryHandler.getEntitiesByName(userId,
                                                                             glossaryTermBuilder.getSearchInstanceProperties(methodName),
                                                                             GlossaryTermMapper.GLOSSARY_TERM_TYPE_GUID,
                                                                             startFrom,
                                                                             pageSize,
                                                                             methodName);

        if (glossaryTerms != null)
        {
            for (EntityDetail entity : glossaryTerms)
            {
                if (entity != null)
                {
                    GlossaryTermConverter  converter = new GlossaryTermConverter(entity,
                                                                                 repositoryHelper,
                                                                                 serviceName);
                    results.add(converter.getBean());
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
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

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameter, methodName);

        EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                guid,
                                                                guidParameter,
                                                                GlossaryTermMapper.GLOSSARY_TERM_TYPE_NAME,
                                                                methodName);

        GlossaryTermConverter  converter = new GlossaryTermConverter(entity,
                                                                     repositoryHelper,
                                                                     serviceName);
        return converter.getBean();
    }
}
