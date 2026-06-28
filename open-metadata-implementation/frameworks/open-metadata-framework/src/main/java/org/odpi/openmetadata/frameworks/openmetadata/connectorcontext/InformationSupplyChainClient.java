/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.*;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.InformationSupplyChainHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.InformationSupplyChainElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;

/**
 * Provides services for connectors to work with lineage relationships.
 */
public class InformationSupplyChainClient extends ConnectorContextClientBase
{
    private final InformationSupplyChainHandler informationSupplyChainHandler;


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
    public InformationSupplyChainClient(ConnectorContextBase     parentContext,
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

        this.informationSupplyChainHandler = new InformationSupplyChainHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }



    /**
     * Connect two peers in an information supply chain.  The linked elements are of the type 'Referenceable' to allow significant data stores to be included in the definition of the information supply chain.
     *
     * @param peerOneGUID  unique identifier of the end one element in the relationship
     * @param peerTwoGUID  unique identifier of the end two element in the relationship
     * @param makeAnchorOptions  options to control access to open metadata
     * @param linkProperties   description of the relationship.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkPeersInInformationSupplyChain(String                               peerOneGUID,
                                                  String                               peerTwoGUID,
                                                  MakeAnchorOptions                    makeAnchorOptions,
                                                  InformationSupplyChainLinkProperties linkProperties) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        informationSupplyChainHandler.linkPeersInInformationSupplyChain(connectorUserId, peerOneGUID, peerTwoGUID, makeAnchorOptions, linkProperties);
    }


    /**
     * Detach two peers in an information supply chain from one another.    The linked elements are of type 'Referenceable' to allow significant data stores to be included in the definition of the information supply chain.
     *
     * @param peerOneGUID  unique identifier of the end one element in the relationship
     * @param peerTwoGUID  unique identifier of the end two element in the relationship
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void unlinkPeersInInformationSupplyChain(String        peerOneGUID,
                                                    String        peerTwoGUID,
                                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        informationSupplyChainHandler.unlinkPeersInInformationSupplyChain(connectorUserId, peerOneGUID, peerTwoGUID, deleteOptions);
    }


    /**
     * Returns the list of information supply chains with a particular name.
     *
     * @param name       name of the element to return - match is full text match in qualifiedName or name
     * @param addImplementation    should details of the implementation of the information supply chain be extracted too?
     * @param queryOptions           multiple options to control the query
     *
     * @return a list of elements
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getInformationSupplyChainsByName(String       name,
                                                                          QueryOptions queryOptions,
                                                                          boolean      addImplementation) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException
    {
        return informationSupplyChainHandler.getInformationSupplyChainsByName(connectorUserId, name, queryOptions, addImplementation);
    }


    /**
     * Return the properties of a specific information supply chain.
     *
     * @param informationSupplyChainGUID    unique identifier of the required element
     * @param addImplementation    should details of the implementation of the information supply chain be extracted too?
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public InformationSupplyChainElement getInformationSupplyChainByGUID(String     informationSupplyChainGUID,
                                                                         boolean    addImplementation,
                                                                         GetOptions getOptions) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        return informationSupplyChainHandler.getInformationSupplyChainByGUID(connectorUserId, informationSupplyChainGUID, addImplementation, getOptions);
    }



    /**
     * Retrieve the list of information supply chains metadata elements that contain the search string.
     *
     * @param searchString         string to find in the properties
     * @param addImplementation    should details of the implementation of the information supply chain be extracted too?
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findInformationSupplyChains(String        searchString,
                                                                     boolean       addImplementation,
                                                                     SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        return informationSupplyChainHandler.findInformationSupplyChains(connectorUserId, searchString, addImplementation, searchOptions);
    }
}
