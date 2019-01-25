/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.server;

import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.UnrecognizedAssetGUIDException;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.UnrecognizedGUIDException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Meaning;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;

/**
 * MeaningsHandler returns the list of meanings for a Referenceable.
 */
public class MeaningsHandler
{
    private String               serviceName;
    private OMRSRepositoryHelper repositoryHelper;
    private String               serverName;
    private ErrorHandler         errorHandler;
    private TypeHandler          typeHandler = new TypeHandler();


    /**
     * Construct the asset handler with a link to the property server's connector and this access service's
     * official name.  Then retrieve the asset and its relationships.
     *
     * @param serviceName  name of this service
     * @param repositoryConnector  connector to the property server.
     * @param userId        userId of user making request.
     * @param assetGUID     unique id for asset.
     */
    MeaningsHandler(String                  serviceName,
                    OMRSRepositoryConnector repositoryConnector,
                    String                  userId,
                    String                  assetGUID) throws InvalidParameterException,
                                                              UnrecognizedGUIDException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        this.serviceName = serviceName;
        this.repositoryHelper = repositoryConnector.getRepositoryHelper();
        this.serverName = repositoryConnector.getServerName();
        this.errorHandler = new ErrorHandler(repositoryConnector);
    }



    List<Meaning>  getMeaningsForReferenceable(String     referenceableGUID)
    {
        // todo
        return null;
    }


    List<Meaning> getMeaningsForReferenceable(List<Relationship>   relationships)
    {
        EntityDetail         glossaryTermEntity;

        // todo
        return null;
    }
}
