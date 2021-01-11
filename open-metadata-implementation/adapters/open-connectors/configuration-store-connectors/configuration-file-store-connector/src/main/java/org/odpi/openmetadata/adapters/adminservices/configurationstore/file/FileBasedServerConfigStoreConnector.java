/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.adminservices.configurationstore.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.adminservices.store.OMAGServerConfigStoreRetrieveAll;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.adminservices.store.OMAGServerConfigStoreConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * FileBasedServerConfigStoreConnector provides a connector that manages a configuration document for an OMAG Server in a file
 */
public class FileBasedServerConfigStoreConnector extends OMAGServerConfigStoreConnectorBase implements OMAGServerConfigStoreRetrieveAll
{
    /*
     * This is the name of the configuration file that is used if there is no file name in the connection.
     */
    private static final String defaultFilenameTemplate = "data/servers/{0}/config/{0}.config";

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

        String configStoreTemplateName = getStoreTemplateName();

        configStoreName = super.getStoreName(configStoreTemplateName, serverName);
    }

    /**
     * Get the store template name
     * @return the store template name
     */
    private String getStoreTemplateName()
    {
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
        return configStoreTemplateName;
    }



    /**
     * Save the server configuration.
     *
     * @param omagServerConfig - configuration properties to save
     */
    @Override
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
    @Override
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
    @Override
    public void removeServerConfig()
    {
        File    configStoreFile = new File(configStoreName);

        configStoreFile.delete();
    }


    /**
     * Retrieve all the stored server configurations
     *
     * @return the set of server configurations present in this OMAG Server Config store
     */
    @Override
    public Set<OMAGServerConfig> retrieveAllServerConfigs()
    {
        final String methodName = "retrieveAllServerConfigs";
        Set<OMAGServerConfig> omagServerConfigSet = new HashSet<>();

        try (Stream<Path> list = Files.list(Paths.get(".")))
        {
            // we need to use the configStoreTemplateName to pick up any files that match this shape.
            // this template might have inserts in

            String templateString = getStoreTemplateName();
            Set<String> fileNames = list.map(x -> x.toString())
                    .filter(f -> isFileNameAConfig(f, templateString)).collect(Collectors.toSet());

            for (String fileName:fileNames)
            {
                configStoreName=fileName;
                OMAGServerConfig config = retrieveServerConfig();
                omagServerConfigSet.add(config);
            }
        }
        catch (IOException e)
        {
            throw new OCFRuntimeException(DocStoreErrorCode.CONFIG_RETRIEVE_ALL_ERROR.getMessageDefinition(e.getClass().getName(), e.getMessage(), configStoreName),
                                          this.getClass().getName(),
                                          methodName, e);
        }
        return omagServerConfigSet;
    }


    /**
     * Check whether the file name is an OMAG Server configuration name by checking it against the template.
     *
     * @param textToCheck filename to check
     * @param templateString string containing the template to fill out
     *
     * @return true if the supplied file name is a valid configuration file name
     */
    static boolean isFileNameAConfig(String textToCheck, String templateString)
    {
        boolean isConfig= false;
        MessageFormat mf = new MessageFormat(templateString);

        try
        {
            mf.parse(textToCheck);
            isConfig = true;
        }
        catch (ParseException e)
        {
            // the template did not successfully parse the file name, so is not a config file.
        }
        return isConfig;
    }


    /**
     * Close the config file
     */
    public void disconnect()
    {
        log.debug("Closing Config Store.");
    }
}
