/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.AssetLineageException;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.AssetLineageEntityEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.GlossaryTerm;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.ASSET_LINEAGE_OMAS;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.GLOSSARY_TERM_TYPE_NAME;

public class GlossaryHandler {

    private static final Logger log = LoggerFactory.getLogger(GlossaryHandler.class);

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
    public GlossaryTerm getGlossaryTerm(EntityProxy entityProxy, String userID){


        final  String   guidParameter = "guid";
        final  String   methodName = "getGlossaryTerm";

        String GUID = entityProxy.getGUID();
        GlossaryTerm glossaryTerm = new GlossaryTerm();

        String qualifiedName = repositoryHelper.getStringProperty(ASSET_LINEAGE_OMAS,"qualifiedName",entityProxy.getUniqueProperties(),methodName);


        EntityDetail entityDetail = null;
        try {
            entityDetail = repositoryHandler.getEntityByGUID(userID, GUID, guidParameter, GLOSSARY_TERM_TYPE_NAME, methodName);
        } catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e) {

            throw new AssetLineageException(e.getReportedHTTPCode(),e.getReportingClassName(),e.getReportingActionDescription(),e.getErrorMessage(),e.getReportedSystemAction(),e.getReportedUserAction());
        }

        String displayName = repositoryHelper.getStringProperty(ASSET_LINEAGE_OMAS,"displayName",entityDetail.getProperties(),methodName);
        List<Classification> classifications = entityDetail.getClassifications();

        glossaryTerm.setGuid(GUID);
        glossaryTerm.setQualifiedName(qualifiedName);

        glossaryTerm.setType(entityProxy.getType().getTypeDefName());
        glossaryTerm.setDisplayName(displayName);

        glossaryTerm.setClassifications(classifications);

        return glossaryTerm;
    }
}