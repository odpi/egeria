/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryauthor.fvt;


import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.Config;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.viewservices.glossaryauthor.server.GlossaryAuthorViewGonfigRESTResource;
import org.odpi.openmetadata.viewservices.glossaryauthor.server.GlossaryAuthorViewRelationshipRESTResource;
import org.odpi.openmetadata.viewservices.glossaryauthor.services.GlossaryAuthorViewConfigRESTServices;

import java.io.IOException;
import java.util.Optional;

/**
 * FVT resource to call glossary author client APIs to test the config API
 */
public class ConfigFVT
{
    private String serverName = null;
    private String userId = null;
    private String url = null;

    public static void main(String args[])
    {
        try
        {
            String url = RunAllFVTOn2Servers.getUrl(args);
            runWith2Servers(url);
        } catch (IOException e1)
        {
            System.out.println("Error getting user input");
        } catch (GlossaryAuthorFVTCheckedException e) {
            System.out.println("ERROR: " + e.getMessage() );
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            System.out.println("ERROR: " + e.getReportedErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        }

    }
    public ConfigFVT(String url, String serverName, String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        System.out.println("Config FVT");
        this.url =url;
        this.userId = userId;
        this.serverName = serverName;
    }

    public static void runWith2Servers(String url) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        runIt(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        runIt(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
    }
    synchronized public static void runIt(String url, String serverName, String userId) throws InvalidParameterException, GlossaryAuthorFVTCheckedException, PropertyServerException, UserNotAuthorizedException {
        try {
            System.out.println("ConfigFVT runIt started");
            ConfigFVT fvt =new ConfigFVT(url, serverName, userId);
            fvt.run();
            System.out.println("ConfigFVT runIt stopped");
        }
        catch (Exception error) {
            error.printStackTrace();
            throw error;
        }
    }

    public void run() throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        GlossaryAuthorViewConfigRESTServices clientREST = new GlossaryAuthorViewConfigRESTServices();
        //serverName, url
        SubjectAreaOMASAPIResponse<Config> configResp =  clientREST.getConfig(serverName, userId, "Dummyguid");
        //SubjectAreaConfigClient configClient =  new SubjectAreaConfigClient(client);
        Optional<Config> config;//configClient.getConfig(userId);
        config = configResp.head();
        if (config.isPresent())
            if (config.get().getMaxPageSize() != 1000) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected " + 1000 + " as the max page size got " + config.get().getMaxPageSize());
        }
    }
}
