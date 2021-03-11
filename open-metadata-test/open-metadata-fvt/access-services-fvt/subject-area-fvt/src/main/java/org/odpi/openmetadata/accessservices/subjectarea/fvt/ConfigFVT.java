/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.configs.SubjectAreaConfigClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.glossaries.SubjectAreaGlossaryClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.Config;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.io.IOException;

/**
 * FVT resource to call subject area client APIs to test the config API
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
        } catch (SubjectAreaFVTCheckedException e) {
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

    public static void runWith2Servers(String url) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        runIt(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        runIt(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
    }
    synchronized public static void runIt(String url, String serverName, String userId) throws InvalidParameterException, SubjectAreaFVTCheckedException, PropertyServerException, UserNotAuthorizedException {
        System.out.println("ConfigFVT runIt started");
        ConfigFVT fvt =new ConfigFVT(url, serverName, userId);
        fvt.run();
        System.out.println("ConfigFVT runIt stopped");
    }

    public void run() throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaRestClient client = new SubjectAreaRestClient(serverName, url);
        SubjectAreaConfigClient configClient =  new SubjectAreaConfigClient(client);
        Config config = configClient.getConfig(userId);
        if (config.getMaxPageSize() != 1000) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " + 1000 + " as the max page size got " + config.getMaxPageSize());
        }
    }
}
