/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.basicfile;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.BasicFileConnectorErrorCode;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileException;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileReadException;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;


/**
 * BasicFileStoreConnector works with files to retrieve simple objects.
 */
public class BasicFileStoreConnector extends ConnectorBase implements BasicFileStore
{
    protected String         fileStoreName     = null;

    /*
     * Variables used for logging and debug.
     */
    private static final Logger log = LoggerFactory.getLogger(BasicFileStoreConnector.class);


    /**
     * Initialize the connector.
     *
     * @param connectorInstanceId - unique id for the connector instance - useful for messages etc
     * @param connectionDetails - POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String connectorInstanceId, Connection connectionDetails)
    {
        super.initialize(connectorInstanceId, connectionDetails);

        Endpoint endpoint = connectionDetails.getEndpoint();

        if (endpoint != null)
        {
            if (endpoint.getAddress() == null)
            {
                log.error("Null endpoint address");
            }
            else if (endpoint.getAddress().startsWith("file://"))
            {
                fileStoreName = endpoint.getAddress().substring(7);
            }
            else
            {
                fileStoreName = endpoint.getAddress();
            }
        }
        else
        {
            log.error("Null endpoint");
        }

    }


    /**
     * Return the name of the file to read.
     *
     * @return file name
     * @throws FileException problem accessing the file
     */
    public String   getFileName() throws FileException
    {
        final String  methodName = "getFileName";

        getFile(methodName);

        return fileStoreName;
    }


    /**
     * Return the number of bytes in the file
     *
     * @return number
     * @throws FileException unable to locate the file
     */
    public long  getFileLength() throws FileException
    {
        final String  methodName = "getFileLength";

        File file = this.getFile(methodName);

        return file.length();
    }


    /**
     * Return the creation date for the file.
     *
     * @return Date object
     * @throws FileException problem accessing the file
     */
    public Date getCreationDate() throws FileException, FileReadException
    {
        final String  methodName = "getCreationDate";

        File fileStore = getFile(methodName);

        try
        {
            BasicFileAttributes attr = Files.readAttributes(fileStore.toPath(), BasicFileAttributes.class);

            return new Date(attr.creationTime().toMillis());
        }
        catch (IOException ioException)
        {
            throw new FileReadException(BasicFileConnectorErrorCode.UNEXPECTED_IO_EXCEPTION.getMessageDefinition(fileStoreName,
                                                                                                                 ioException.getMessage()),
                                        this.getClass().getName(),
                                        methodName,
                                        ioException,
                                        fileStoreName);
        }
    }


    /**
     * Return the last update date for the file.
     *
     * @return Date object
     * @throws FileException problem accessing the file
     */
    public Date getLastUpdateDate() throws FileException, FileReadException
    {
        final String  methodName = "getLastUpdateDate";

        File fileStore = getFile(methodName);

        try
        {
            BasicFileAttributes attr = Files.readAttributes(fileStore.toPath(), BasicFileAttributes.class);

            return new Date(attr.lastModifiedTime().toMillis());
        }
        catch (IOException ioException)
        {
            throw new FileReadException(BasicFileConnectorErrorCode.UNEXPECTED_IO_EXCEPTION.getMessageDefinition(fileStoreName,
                                                                                                                 ioException.getMessage()),
                                        this.getClass().getName(),
                                        methodName,
                                        ioException,
                                        fileStoreName);
        }
    }


    /**
     * Return the last accessed date for the file.
     *
     * @return Date object
     * @throws FileException problem accessing the file
     */
    public Date getLastAccessDate() throws FileException, FileReadException
    {
        final String  methodName = "getLastAccessedDate";

        File fileStore = getFile(methodName);

        return new Date(fileStore.lastModified());
    }


    /**
     * Throw a standard exception based on the supplied error code.
     *
     * @param errorCode error code describing the problem
     * @param methodName calling method
     * @param fileStoreName name of file
     * @throws FileException exception that is generated
     */
    private void throwException(BasicFileConnectorErrorCode errorCode,
                                String                      methodName,
                                String                      fileStoreName,
                                Exception                   caughtException) throws FileException
    {
        ExceptionMessageDefinition messageDefinition;

        if (fileStoreName == null)
        {
            messageDefinition = errorCode.getMessageDefinition(super.connectionBean.getQualifiedName());
        }
        else
        {
            messageDefinition = errorCode.getMessageDefinition(fileStoreName, super.connectionBean.getQualifiedName());
        }

        if (caughtException == null)
        {
            throw new FileException(messageDefinition,
                                    this.getClass().getName(),
                                    methodName,
                                    fileStoreName);
        }
        else
        {
            throw new FileException(messageDefinition,
                                    this.getClass().getName(),
                                    methodName,
                                    caughtException,
                                    fileStoreName);
        }
    }


    /**
     * Return the Java File object that provides access to the file.
     *
     * @return File object
     * @throws FileException problem accessing the file
     */
    public File  getFile() throws FileException
    {
        final String  methodName = "getFile";

        return this.getFile(methodName);
    }


    /**
     * Return the Java File object that provides access to the file.
     *
     * @param methodName, calling method
     * @return File object
     * @throws FileException problem accessing the file
     */
    protected File  getFile(String methodName) throws FileException
    {
        try
        {
            if (fileStoreName == null)
            {
                this.throwException(BasicFileConnectorErrorCode.FILE_NOT_SPECIFIED, methodName, null, null);
            }

            File  fileStore = new File(fileStoreName);

            if (! fileStore.exists())
            {
                this.throwException(BasicFileConnectorErrorCode.FILE_NOT_FOUND, methodName, fileStoreName, null);
            }

            if (fileStore.isDirectory())
            {
                this.throwException(BasicFileConnectorErrorCode.DIRECTORY_SPECIFIED, methodName, fileStoreName, null);
            }

            if (! fileStore.canRead())
            {
                this.throwException(BasicFileConnectorErrorCode.FILE_NOT_READABLE, methodName, fileStoreName, null);
            }

            return fileStore;
        }
        catch (FileException  error)
        {
            log.debug("Throwing error " + error.getClass().getSimpleName() + " with message " + error.getMessage());
            throw error;
        }
        catch (SecurityException  error)
        {
            this.throwException(BasicFileConnectorErrorCode.UNEXPECTED_SECURITY_EXCEPTION, methodName, fileStoreName, error);
        }
        catch (Exception error)
        {
            this.throwException(BasicFileConnectorErrorCode.UNEXPECTED_IO_EXCEPTION, methodName, fileStoreName, error);
        }

        return null;
    }


    /**
     * Close the file
     */
    @Override
    public void disconnect()
    {
        try
        {
            super.disconnect();
        }
        catch (Exception  exec)
        {
            log.debug("Ignoring unexpected exception " + exec.getClass().getSimpleName() + " with message " + exec.getMessage());
        }

        log.debug("Closing File");
    }
}
