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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FVT resource to call subject area client APIs to test the config API
 */
public class ConfigFVT
{
    private String serverName = null;
    private String userId = null;
    private String url = null;
    private static Logger log = LoggerFactory.getLogger(ConfigFVT.class);

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
            log.error("ERROR: " + e.getMessage() );
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            log.error("ERROR: " + e.getReportedErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        }

    }
    public ConfigFVT(String url, String serverName, String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        if (log.isDebugEnabled()) {
            log.debug("Config FVT");
        }
        this.url =url;
        this.userId = userId;
        this.serverName = serverName;
    }

    public static void runWith2Servers(String url) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        runIt(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        runIt(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
    }
    synchronized public static void runIt(String url, String serverName, String userId) throws InvalidParameterException, SubjectAreaFVTCheckedException, PropertyServerException, UserNotAuthorizedException {
        try {
            System.out.println("ConfigFVT runIt started");
            ConfigFVT fvt =new ConfigFVT(url, serverName, userId);
            fvt.run();
            System.out.println("ConfigFVT runIt stopped");
        }
        catch (Exception error) {
            log.error("The FVT Encountered an Exception", error);
            throw error;
        }
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
