/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.odpi.openmetadata.accessservices.assetlineage.model.event.GlossaryTerm;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;

import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.GLOSSARY_TERM_TYPE_NAME;

public class GlossaryHandler {

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
     * Returns the glossary term object corresponding to the supplied glossary term GUID.
     *
     * @param entityProxy entityProxy
     * @param userID  String - userId of user making request.
     * @return Glossary Term retrieved from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GlossaryTerm getGlossaryTerm(EntityProxy entityProxy, String userID) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final  String   guidParameter = "guid";
        final  String   methodName = "getGlossaryTerm";
        GlossaryTerm glossaryTerm = new GlossaryTerm();
        String GUID = entityProxy.getGUID();

        InstancePropertyValue qualifiedNameInstancePropertyValue = entityProxy.getUniqueProperties().getInstanceProperties().get("qualifiedName");
        PrimitivePropertyValue qualifiedNamePrimitivePropertyValue = (PrimitivePropertyValue) qualifiedNameInstancePropertyValue;
        String qualifiedName = qualifiedNamePrimitivePropertyValue.getPrimitiveValue().toString();

        glossaryTerm.setGuid(GUID);
        glossaryTerm.setQualifiedName(qualifiedName);
        EntityDetail entityDetail = repositoryHandler.getEntityByGUID(userID, GUID, guidParameter, GLOSSARY_TERM_TYPE_NAME, methodName);
        InstancePropertyValue displayNameInstancePropertyValue = entityDetail.getProperties().getPropertyValue("displayName");
        PrimitivePropertyValue displayNamePrimitivePropertyValue = (PrimitivePropertyValue) displayNameInstancePropertyValue;
        String displayName = displayNamePrimitivePropertyValue.getPrimitiveValue().toString();

        List<Classification> classifications = entityDetail.getClassifications();

        glossaryTerm.setType(entityProxy.getType().getTypeDefName());
        glossaryTerm.setDisplayName(displayName);

        glossaryTerm.setClassifications(classifications);
        return glossaryTerm;
    }
}