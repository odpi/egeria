/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.adminservices.configurationstore.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.odpi.openmetadata.adminservices.store.OMAGServerConfigStoreConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.properties.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.Endpoint;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileBasedServerConfigStoreConnector extends OMAGServerConfigStoreConnectorBase
{
    /*
     * This is the name of the configuration file that is used if there is no file name in the connection.
     */
    private static final String defaultFilename = "omag.server.config";

    /*
     * Variables used in writing to the file.
     */
    private String           configStoreName  = null;

    /*
     * Variables used for logging and debug.
     */
    private static final Logger log = Logger.getLogger(FileBasedServerConfigStoreConnector.class);


    /**
     * Default constructor
     */
    public FileBasedServerConfigStoreConnector()
    {
    }


    @Override
    public void initialize(String connectorInstanceId, Connection connection)
    {
        super.initialize(connectorInstanceId, connection);

        Endpoint endpoint = connection.getEndpoint();

        if (endpoint != null)
        {
            configStoreName = endpoint.getAddress();
        }

        if (configStoreName == null)
        {
            configStoreName = defaultFilename;
        }
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
            if (log.isDebugEnabled())
            {
                log.debug("Writing server config store properties: " + omagServerConfig);
            }

            if (omagServerConfig == null)
            {
                configStoreFile.delete();
            }
            else
            {
                ObjectMapper objectMapper = new ObjectMapper();

                String configStoreFileContents = objectMapper.writeValueAsString(omagServerConfig);

                FileUtils.writeStringToFile(configStoreFile, configStoreFileContents, false);
            }
        }
        catch (IOException   ioException)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Unusable Server config Store :(", ioException);
            }
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
            if (log.isDebugEnabled())
            {
                log.debug("Retrieving server configuration properties");
            }

            String configStoreFileContents = FileUtils.readFileToString(configStoreFile, "UTF-8");

            ObjectMapper objectMapper = new ObjectMapper();

            newConfigProperties = objectMapper.readValue(configStoreFileContents, OMAGServerConfig.class);
        }
        catch (IOException ioException)
        {
            /*
             * The config file is not found, create a new one ...
             */

            if (log.isDebugEnabled())
            {
                log.debug("New server config Store", ioException);
            }

            newConfigProperties = new OMAGServerConfig();
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
        if (log.isDebugEnabled())
        {
            log.debug("Closing Config Store.");
        }
    }
}
