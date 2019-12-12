/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.typeexplorer.services;


import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.viewservices.tex.properties.*;
import org.odpi.openmetadata.viewservices.tex.responses.MetadataServersResponse;
import org.odpi.openmetadata.viewservices.tex.responses.TypeExplorerOMVSAPIResponse;
import org.odpi.openmetadata.viewservices.tex.responses.TypeExplorerResponse;
import org.odpi.openmetadata.viewservices.typeexplorer.admin.handlers.TypeExplorerViewInstanceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * The TypeExplorerRESTServicesInstance provides the server-side implementation
 * of the TypeExplorer UI-component
 */

public class TypeExplorerRESTServices 
{

    private static String className = TypeExplorerRESTServices.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger(className);

    protected static TypeExplorerViewInstanceHandler instanceHandler = new TypeExplorerViewInstanceHandler();

    /**
     * This method retrieves all the types from the server in a TypeExplorer object.
     * In the RequestBody:
     *   serverName is the name of the repository server to be interrogated.
     *   serverURLRoot is the root of the URL to use to connect to the server.
     *   enterpriseOption is a string "true" or "false" indicating whether to include results from the cohorts to which the server belongs
     * @param localServerName  local server name
     * @param userId user identifier
     * @param body supplied body with the request.
     */
    public TypeExplorerOMVSAPIResponse typeExplorer(String localServerName, String userId, Map<String,String> body)
    {
        // Look up types in server and construct TEX
        TypeExplorerOMVSAPIResponse texResp = null;
        String exceptionMessage           = null;
        String requestedServerName        = body.get("serverName");
        boolean enterpriseOption          = body.get("enterpriseOption").equals("true");

        try {

            TypeExplorer tex = instanceHandler.getTypeExplorer(localServerName, userId, requestedServerName, enterpriseOption);

            if (tex != null) {
                texResp = new TypeExplorerOMVSAPIResponse(200, "");
            } else {
                texResp = new TypeExplorerOMVSAPIResponse(400, "Could not retrieve type information");
            }
            texResp = new TypeExplorerResponse(200,"",tex);
        }
        catch (ConnectionCheckedException | ConnectorCheckedException e) {

            exceptionMessage = "Connector error occurred, please check the server name and URL root parameters";
        }
        catch (UserNotAuthorizedException e) {

            exceptionMessage = "Sorry - this username was not authorized to perform the request";
        }
        catch (RepositoryErrorException e) {

            exceptionMessage = "The repository could not be reached, please check the server name and URL root and verify that the server is running ";
        }
        catch (InvalidParameterException e) {

            exceptionMessage = "The request to load type information reported an invalid parameter, please check the server name and URL root parameters";
        }
        // If there was an exception, incorporate the exception message into a response object
        if (exceptionMessage != null) {
            texResp = new TypeExplorerOMVSAPIResponse(400, exceptionMessage);
        }
        if (texResp == null) {
            // should not happen
            exceptionMessage = "Unexpected, no tex object and no obvious error. Logic error - please raise an issue on github";
            texResp = new TypeExplorerOMVSAPIResponse(400, exceptionMessage);
        }

        return texResp;

    }

    /**
     * Get the metadata server names that can be type explored
     * @param localServerName
     * @return metadata server names
     */
    public TypeExplorerOMVSAPIResponse getMetadataServerNames(String localServerName, String userId) {
        Set<String> serverNames = instanceHandler.getMetadataServerNames(localServerName, userId);
        return new MetadataServersResponse(200,"",serverNames);



    }
}
