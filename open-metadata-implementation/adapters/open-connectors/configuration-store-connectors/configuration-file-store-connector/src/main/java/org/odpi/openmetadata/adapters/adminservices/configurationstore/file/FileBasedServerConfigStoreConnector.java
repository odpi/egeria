/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.adminservices.configurationstore.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.odpi.openmetadata.adminservices.store.OMAGServerConfigStoreRetrieveAll;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFRuntimeException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.adminservices.store.OMAGServerConfigStoreConnectorBase;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
     * This is the insert string using in the file name template
     */
    private static final String INSERT_FOR_FILENAME_TEMPLATE = "{0}";
    /*
     * This is the name of the configuration file that is used if there is no file name in the connection.
     */
    private static final String DEFAULT_FILENAME_TEMPLATE    = "./data/servers/" + INSERT_FOR_FILENAME_TEMPLATE + "/config/" + INSERT_FOR_FILENAME_TEMPLATE + ".config";


    /*
     * Variables used in writing to the file.
     */
    private String           configStoreName  = null;

    /*
     * Variables used for logging and debug.
     */
    private static final Logger log = LoggerFactory.getLogger(FileBasedServerConfigStoreConnector.class);

    private  static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final ObjectWriter OBJECT_WRITER = OBJECT_MAPPER.writer();

    private static final ObjectReader OBJECT_READER = OBJECT_MAPPER.reader();


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
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
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
        Endpoint endpoint = connectionBean.getEndpoint();

        String configStoreTemplateName = null;
        if (endpoint != null)
        {
            configStoreTemplateName = endpoint.getAddress();
        }
        if (configStoreTemplateName == null)
        {
            configStoreTemplateName = DEFAULT_FILENAME_TEMPLATE;
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
                String configStoreFileContents = OBJECT_WRITER.writeValueAsString(omagServerConfig);

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

            newConfigProperties = OBJECT_READER.readValue(configStoreFileContents, OMAGServerConfig.class);
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
        String templateString = getStoreTemplateName();

        Set<String> fileNames = getFileNames(templateString, methodName);

        for (String fileName : fileNames)
        {
            configStoreName = fileName;
            OMAGServerConfig config = retrieveServerConfig();
            omagServerConfigSet.add(config);
        }

        return omagServerConfigSet;
    }


    /**
     * Get filenames from the file system that match the store template.
     * Only supports 1 or 2 inserts in the template and they need to be in different url segments.
     * When a file is matched on the file system, the match for the insert is the serverName.
     *
     * @param templateString string that defines the shape of the file name
     * @param methodName callers name for diagnostics
     * @return set of filenames from the file System that match the template
     */
    protected Set<String> getFileNames(String templateString, String methodName)
    {
        if (!isTemplateValid(templateString))
        {
            // bad template supplied - error
            throw new OMFRuntimeException(DocStoreErrorCode.CONFIG_RETRIEVE_ALL_ERROR_INVALID_TEMPLATE.getMessageDefinition(templateString),
                                          this.getClass().getName(),
                                          methodName);
        }

        Set<String> fileNames = new HashSet<>();

        int firstIndex = templateString.indexOf(INSERT_FOR_FILENAME_TEMPLATE);
        int secondIndex = -1;
        if (firstIndex != -1 && templateString.length() > firstIndex + 3)
        {
            String textAfter1stIndex = templateString.substring(firstIndex + 3);
            secondIndex = textAfter1stIndex.indexOf(INSERT_FOR_FILENAME_TEMPLATE);
        }

        if (log.isDebugEnabled())
        {
            log.debug("templateString " + templateString +",firstIndex="+ firstIndex+",secondIndex="+ secondIndex);
        }

        try
        {
            if (firstIndex != -1 && secondIndex == -1)
            {
                // only one insert
                String firstPartOfTemplate = templateString.substring(0, firstIndex);
                String secondPartOfTemplate = templateString.substring(firstIndex + 3);

                //  we need to know if the insert is part of a folder name or part of a file name

                //      - go back to the last slash
                int lastSlashIndex = firstPartOfTemplate.lastIndexOf('/');
                //      look for the next slash
                int nextSlashIndex = -1;

                if (templateString.length() > lastSlashIndex+1)
                {
                    nextSlashIndex = templateString.substring(lastSlashIndex + 1).indexOf("/");

                }

                Stream<Path> listOfFolders = Files.list(Paths.get(firstPartOfTemplate.substring(0, lastSlashIndex+1)));
                String pre = templateString.substring(0, firstIndex);

                if (nextSlashIndex == -1)
                {
                    // get its contents then pattern match the content before and after the insert in the file name
                    String post = templateString.substring(firstIndex + 3);
                    fileNames = listOfFolders.map(x -> x.toString())
                            .filter(f -> doesStringStartAndEndMatch(f, pre, post)).collect(Collectors.toSet());
                }
                else
                {
                    // amend  next slash index to be from the start of the template string.
                    nextSlashIndex = lastSlashIndex+nextSlashIndex+1;

                    // we are looking for folders
                    String restOfFolderName = templateString.substring(firstIndex + 3, nextSlashIndex);
                    Set<String> folderNames = listOfFolders.map(x -> x.toString())
                            .filter(f -> doesStringStartAndEndMatch(f, pre, restOfFolderName)).collect(Collectors.toSet());

                    // remove post and add secondPartOfTemplate then we have the matching filenames
                    for (String folderName : folderNames)
                    {
                        String fileName = folderName.substring(0, folderName.length() - restOfFolderName.length()) + secondPartOfTemplate;

                        File f =  new File(fileName);
                        if (f.exists() && !f.isDirectory())
                        {
                            fileNames.add(fileName);
                        }
                    }
                }
            }
            else
            {
                secondIndex = firstIndex + 3 + secondIndex;
                // 2 inserts - the first must be a folder name. hopefully the file name is not pathological with 2 inserts in the same segment.
                String firstPartOfTemplate = templateString.substring(0, firstIndex);
                String secondPartOfTemplate = templateString.substring(firstIndex + 3, secondIndex);
                String thirdPartOfTemplate = templateString.substring(secondIndex + 3);
                // take the serverName from the first insert and then look for its presence in the second insert position.
                // we need to find the parent folder name of the folder with the insert in and list those folders so we can match on them
                //      - go back to the last slash

                int lastSlashIndex = firstPartOfTemplate.lastIndexOf('/');
                //      look for the next slash
                int nextSlashIndex = -1;

                if (templateString.length() > lastSlashIndex+1)
                {
                    nextSlashIndex = templateString.substring(lastSlashIndex + 1).indexOf("/");
                    nextSlashIndex = nextSlashIndex + lastSlashIndex + 1;
                }

                Stream<Path> listOfFolders = Files.list(Paths.get(templateString.substring(0, lastSlashIndex )));

                String pre = templateString.substring(0, firstIndex);
                String restOfFolderName = "";
                if (nextSlashIndex > firstIndex)
                {
                    restOfFolderName = templateString.substring(firstIndex + 3, nextSlashIndex);
                }

                final String post = restOfFolderName;
                int postLength = post.length();
                Set<String> matchedFolderNames = listOfFolders.map(x -> x.toString())
                        .filter(f -> doesStringStartAndEndMatch(f, pre, post)).collect(Collectors.toSet());
                // for each folder name we need to amend to bring the folder name up to  the file name.
                // find the last / in the whole string and see if it is further in that the folder we have just matched, if so there are
                // folder(s) we need to add to the folder Names we have matched

                int lastSlashIndexFromWholeTemplate = templateString.lastIndexOf('/');
                // extract the serverName from the matchedFolderName
                Set<String> serverNames = new HashSet<>();

                if (lastSlashIndexFromWholeTemplate >= nextSlashIndex)
                {
                    for (String matchedFolderName : matchedFolderNames)
                    {
                        String serverName = matchedFolderName.substring(firstIndex , matchedFolderName.length() - postLength);

                        if (log.isDebugEnabled())
                        {
                            log.debug("serverName " + serverName);
                        }
                        serverNames.add(serverName);
                    }
                }

                for (String serverName : serverNames)
                {
                    String fileName = firstPartOfTemplate + serverName + secondPartOfTemplate + serverName + thirdPartOfTemplate;

                    if (log.isDebugEnabled())
                    {
                        log.debug("getFileNames with 2 inserts testing fileName " + fileName );
                    }

                    File f =  new File(fileName);
                    if (log.isDebugEnabled())
                    {
                        log.debug("see if fileName " + fileName + " exists" );
                    }

                    if (f.exists() && !f.isDirectory())
                    {
                        if (log.isDebugEnabled())
                        {
                            log.debug("fileName " + fileName + " exists");
                        }

                        fileNames.add(fileName);
                    }
                }
            }
        }
        catch (IOException error)
        {
            throw new OMFRuntimeException(DocStoreErrorCode.CONFIG_RETRIEVE_ALL_ERROR.getMessageDefinition(error.getClass().getName(), error.getMessage(), configStoreName),
                                          this.getClass().getName(),
                                          methodName, error);
        }

        return fileNames;
    }

    /**
     * check whether the supplied string starts and ends with the pre and post strings
     *
     * @param stringToTest string to test
     * @param pre          must start with this string - can be empty
     * @param post         must end with this starting - can be empty
     * @return whether the folder name starts and ends with the supplied strings.
     */
    private boolean doesStringStartAndEndMatch(String stringToTest, String pre, String post) {
        if (log.isDebugEnabled()) {
            log.debug("doesStringStartAndEndMatch " + stringToTest +",pre="+ pre+",post="+ post);
        }
        boolean isMatch = false;
        if (stringToTest.startsWith(pre) && stringToTest.endsWith(post)) {
            isMatch = true;
        }
        return isMatch;
    }

    /**
     * Check whether the template string is valid.
     *
     * @param templateString string to check
     * @return a flag indicating whether valid.
     */
    private boolean isTemplateValid(String templateString) {
        boolean isValid = true;
        int lastIndex = 0;
        int count = 0;
        int indexOfSecondInsert= -1;
        while (lastIndex != -1) {
            lastIndex = templateString.indexOf(INSERT_FOR_FILENAME_TEMPLATE, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += INSERT_FOR_FILENAME_TEMPLATE.length();
                if (count ==2) {
                    indexOfSecondInsert = lastIndex;
                }
            }
        }
        if (count == 0 || count > 2) {
            isValid = false;
        } else if (count == 2 && templateString.lastIndexOf('/') > indexOfSecondInsert) {
            // do not allow 2 folder inserts
            isValid = false;
        } else if (templateString.contains(INSERT_FOR_FILENAME_TEMPLATE + INSERT_FOR_FILENAME_TEMPLATE)) {
            // it does not make sense to have 2 inserts next to each other in the template
            isValid = false;
        }
        return isValid;
    }

    /**
     * Close the config file
     */
    public void disconnect()
    {
        log.debug("Closing Config Store.");
    }
}
