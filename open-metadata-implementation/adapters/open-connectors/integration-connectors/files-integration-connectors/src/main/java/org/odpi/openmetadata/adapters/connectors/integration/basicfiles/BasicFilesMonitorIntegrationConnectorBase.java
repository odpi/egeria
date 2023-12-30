/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.FileFolderElement;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsErrorCode;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.exception.ConfigException;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.exception.FileException;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.integrationservices.files.connector.FilesIntegratorConnector;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * BasicFilesMonitorIntegrationConnectorBase provides common methods for the connectors
 * in this module.
 */
public abstract class BasicFilesMonitorIntegrationConnectorBase extends FilesIntegratorConnector
{
    String  templateQualifiedName = null;
    boolean allowCatalogDelete    = false;

    private String            fileDirectoryName = null;
    private FileFolderElement dataFolderElement = null;
    private File              dataFolderFile    = null;


    private final Map<String, FileAlterationMonitor> monitors = new HashMap<>();

    private static final int POLL_INTERVAL = 500; // milliseconds


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        /*
         * Extract the configuration
         */
        EndpointProperties  endpoint = connectionProperties.getEndpoint();

        if (endpoint != null)
        {
            fileDirectoryName = endpoint.getAddress();
        }

        Map<String, Object> configurationProperties = connectionProperties.getConfigurationProperties();

        if (configurationProperties != null)
        {
            if (configurationProperties.containsKey(BasicFilesMonitorIntegrationProviderBase.ALLOW_CATALOG_DELETE_CONFIGURATION_PROPERTY))
            {
                allowCatalogDelete = true;
            }

            templateQualifiedName = configurationProperties.get(BasicFilesMonitorIntegrationProviderBase.FILE_TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY).toString();
        }

        /*
         * Record the configuration
         */
        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                BasicFilesIntegrationConnectorsAuditCode.CONNECTOR_CONFIGURATION.getMessageDefinition(connectorName,
                                                                                                                      fileDirectoryName,
                                                                                                                      Boolean.toString(allowCatalogDelete),
                                                                                                                      templateQualifiedName));
        }

        /*
         * Start listening
         */
        this.initiateDirectoryMonitoring(this.getRootDirectoryFile(), methodName);
    }


    /**
     * Return the Java File object that provides access to the directory.
     *
     * @return File object
     * @throws ConfigException problem accessing the directory
     */
    synchronized File getRootDirectoryFile() throws ConfigException
    {
        if (dataFolderFile != null)
        {
            return dataFolderFile;
        }

        try
        {
            if (fileDirectoryName == null)
            {
                this.throwConfigException(BasicFilesIntegrationConnectorsErrorCode.FILES_LOCATION_NOT_SPECIFIED, null, null);
            }

            File  fileStore = new File(fileDirectoryName);

            if (! fileStore.exists())
            {
                this.throwConfigException(BasicFilesIntegrationConnectorsErrorCode.FILES_LOCATION_NOT_FOUND, fileDirectoryName, null);
            }

            if (! fileStore.isDirectory())
            {
                this.throwConfigException(BasicFilesIntegrationConnectorsErrorCode.FILES_LOCATION_NOT_DIRECTORY, fileDirectoryName, null);
            }

            if (! fileStore.canRead())
            {
                this.throwConfigException(BasicFilesIntegrationConnectorsErrorCode.FILES_LOCATION_NOT_READABLE, fileDirectoryName, null);
            }

            this.dataFolderFile = fileStore;

            return fileStore;
        }
        catch (ConfigException  error)
        {
            throw error;
        }
        catch (SecurityException  error)
        {
            this.throwConfigException(BasicFilesIntegrationConnectorsErrorCode.UNEXPECTED_SECURITY_EXCEPTION, fileDirectoryName, error);
        }
        catch (Exception error)
        {
            this.throwConfigException(BasicFilesIntegrationConnectorsErrorCode.UNEXPECTED_IO_EXCEPTION, fileDirectoryName, error);
        }

        return null;
    }


    /**
     * Retrieve the Folder element from the open metadata repositories.  If it does not exist it means
     * there are no files defined in the folder.  The connector waits for the folder to be created.
     *
     * @throws ConnectorCheckedException there is a problem retrieving the folder element.
     */
    synchronized FileFolderElement getFolderElement() throws ConnectorCheckedException
    {
        final String methodName = "getFolderElement";

        if (dataFolderElement != null)
        {
            return dataFolderElement;
        }

        File dataFolderFile = this.getRootDirectoryFile();

        try
        {
            FileFolderElement folderElement = this.getContext().getFolderByPathName(dataFolderFile.getAbsolutePath());

            if (folderElement == null)
            {
                return null;
            }

            if ((folderElement.getElementHeader() == null) ||
                        (folderElement.getElementHeader().getGUID() == null) ||
                        (folderElement.getFileFolderProperties() == null))
            {
                if (auditLog != null)
                {
                    auditLog.logMessage(methodName,
                                        BasicFilesIntegrationConnectorsAuditCode.BAD_FOLDER_ELEMENT.getMessageDefinition(connectorName,
                                                                                                                         fileDirectoryName,
                                                                                                                         folderElement.toString()));
                }
            }
            else
            {
                dataFolderElement = folderElement;
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_RETRIEVING_FOLDER.getMessageDefinition(error.getClass().getName(),
                                                                                                                                   connectorName,
                                                                                                                                   methodName,
                                                                                                                                   fileDirectoryName,
                                                                                                                                   dataFolderFile.getAbsolutePath(),
                                                                                                                                   error.getMessage()));
            }

            throw new FileException(
                    BasicFilesIntegrationConnectorsErrorCode.UNEXPECTED_EXC_RETRIEVING_FOLDER.getMessageDefinition(error.getClass().getName(),
                                                                                                                   connectorName,
                                                                                                                   methodName,
                                                                                                                   fileDirectoryName,
                                                                                                                   dataFolderFile.getAbsolutePath(),
                                                                                                                   error.getMessage()),
                    error.getClass().getName(),
                    methodName,
                    error,
                    fileDirectoryName);
        }

        return dataFolderElement;
    }


    /**
     * Throw a config exception based on the supplied error code.
     *
     * @param errorCode error code describing the problem
     * @param fileLocationName name of file
     * @throws ConfigException exception that is generated
     */
    private void throwConfigException(BasicFilesIntegrationConnectorsErrorCode errorCode,
                                      String                                   fileLocationName,
                                      Exception                                caughtException) throws ConfigException
    {
        final String methodName = "getRootDirectoryFile";

        ExceptionMessageDefinition messageDefinition;

        if (fileLocationName == null)
        {
            messageDefinition = errorCode.getMessageDefinition(super.connectionBean.toString());
        }
        else
        {
            messageDefinition = errorCode.getMessageDefinition(fileLocationName);
        }

        ConfigException error;


        if (caughtException == null)
        {
            error = new ConfigException(messageDefinition,
                                        this.getClass().getName(),
                                        methodName,
                                        fileLocationName);
        }
        else
        {
            error = new ConfigException(messageDefinition,
                                        this.getClass().getName(),
                                        methodName,
                                        caughtException,
                                        fileLocationName);
        }

        if (auditLog != null)
        {
            auditLog.logException(methodName,
                                  BasicFilesIntegrationConnectorsAuditCode.BAD_CONFIGURATION.getMessageDefinition(connectorName,
                                                                                                                  ConfigException.class.getName(),
                                                                                                                  fileDirectoryName,
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                  error);
        }

        throw error;
    }


    /**
     * Register a listener for a particular directory (folder).  This results in events whenever there are changes to the files and
     * folders immediately in this directory.
     *
     * @param directory directory to monitor
     * @param methodName calling method
     */
    synchronized void initiateDirectoryMonitoring(File   directory,
                                                  String methodName)
    {
        FileAlterationObserver observer = new FileAlterationObserver(fileDirectoryName);
        FileAlterationMonitor  monitor  = new FileAlterationMonitor(POLL_INTERVAL);
        FileAlterationListener listener = this.getListener();

        observer.addListener(listener);
        monitor.addObserver(observer);

        monitors.put(directory.getName(), monitor);

        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                BasicFilesIntegrationConnectorsAuditCode.DIRECTORY_MONITORING_STARTING.getMessageDefinition(connectorName,
                                                                                                                            directory.getAbsolutePath()));
        }

        try
        {
            monitor.start();
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_MONITOR_START.getMessageDefinition(error.getClass().getName(),
                                                                                                                                 connectorName,
                                                                                                                                 directory.getAbsolutePath(),
                                                                                                                                 error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Set up the file listener class - this is implemented by the subclasses
     *
     * @return file alteration listener implementation
     */
    abstract FileAlterationListenerAdaptor getListener();


    synchronized void stopDirectoryMonitoring(String fileName,
                                              String methodName)
    {
        FileAlterationMonitor monitor = monitors.get(fileName);

        if (monitor != null)
        {
            monitors.remove(fileName, monitor);

            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    BasicFilesIntegrationConnectorsAuditCode.DIRECTORY_MONITORING_STOPPING.getMessageDefinition(connectorName,
                                                                                                                                fileName));
            }

            try
            {
                monitor.stop(POLL_INTERVAL * 2);
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_MONITOR_STOP.getMessageDefinition(error.getClass().getName(),
                                                                                                                                    connectorName,
                                                                                                                                    fileName,
                                                                                                                                    error.getMessage()),
                                          error);
                }
            }
        }
    }


    /**
     * Shutdown file monitoring
     *
     * @throws ConnectorCheckedException something failed in the super class
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        final String methodName = "disconnect";

        for (String fileName : monitors.keySet())
        {
            this.stopDirectoryMonitoring(fileName, methodName);
        }

        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                BasicFilesIntegrationConnectorsAuditCode.CONNECTOR_STOPPING.getMessageDefinition(connectorName));
        }

        super.disconnect();
    }
}
