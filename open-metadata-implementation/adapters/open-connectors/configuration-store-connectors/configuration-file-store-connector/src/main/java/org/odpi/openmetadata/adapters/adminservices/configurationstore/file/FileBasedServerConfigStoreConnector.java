/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.adminservices.configurationstore.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.adminservices.store.OMAGServerConfigStoreConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileBasedServerConfigStoreConnector extends OMAGServerConfigStoreConnectorBase
{
    /*
     * This is the name of the configuration file that is used if there is no file name in the connection.
     */
    private static final String defaultFilenameTemplate = "omag.server.{0}.config";

    /*
     * Variables used in writing to the file.
     */
    private String           configStoreName  = null;

    /*
     * Variables used for logging and debug.
     */
    private static final Logger log = LoggerFactory.getLogger(FileBasedServerConfigStoreConnector.class);


    /**
     * Default constructor
     */
    public FileBasedServerConfigStoreConnector()
    {
    }


    /**
     * Set up the name of the file store
     *
     * @throws ConnectorCheckedException something went wrong
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        EndpointProperties endpoint = connectionProperties.getEndpoint();

        String configStoreTemplateName = null;
        if (endpoint != null)
        {
            configStoreTemplateName = endpoint.getAddress();
        }

        if (configStoreTemplateName == null)
        {
            configStoreTemplateName = defaultFilenameTemplate;
        }

        configStoreName = super.getStoreName(configStoreTemplateName, serverName);
    }


    /**
     * Save the server configuration.
     *
     * @param omagServerConfig - configuration properties to save
     */
    public void saveServerConfig(OMAGServerConfig omagServerConfig)
    {
        File    configStoreFile = new File(configStoreName);

        try
        {
            log.debug("Writing server config store properties: " + omagServerConfig);

            if (omagServerConfig == null)
            {
                configStoreFile.delete();
            }
            else
            {
                ObjectMapper objectMapper = new ObjectMapper();

                String configStoreFileContents = objectMapper.writeValueAsString(omagServerConfig);

                FileUtils.writeStringToFile(configStoreFile, configStoreFileContents, (String)null,false);
            }
        }
        catch (IOException   ioException)
        {
            log.debug("Unusable Server config Store :(", ioException);
        }
    }


    /**
     * Retrieve the configuration saved from a previous run of the server.
     *
     * @return server configuration
     */
    public OMAGServerConfig  retrieveServerConfig()
    {
        File             configStoreFile     = new File(configStoreName);
        OMAGServerConfig newConfigProperties;

        try
        {
            log.debug("Retrieving server configuration properties");

            String configStoreFileContents = FileUtils.readFileToString(configStoreFile, "UTF-8");

            ObjectMapper objectMapper = new ObjectMapper();

            newConfigProperties = objectMapper.readValue(configStoreFileContents, OMAGServerConfig.class);
        }
        catch (IOException ioException)
        {
            /*
             * The config file is not found, create a new one ...
             */
            log.debug("New server config Store", ioException);

            newConfigProperties = null;
        }

        return newConfigProperties;
    }


    /**
     * Remove the server configuration.
     */
    public void removeServerConfig()
    {
        File    configStoreFile = new File(configStoreName);

        configStoreFile.delete();
    }


    /**
     * Close the config file
     */
    public void disconnect()
    {
        log.debug("Closing Config Store.");
    }
}
