/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.avrofile;

import org.odpi.openmetadata.adapters.connectors.avrofile.ffdc.AvroFileConnectorErrorCode;
import org.odpi.openmetadata.adapters.connectors.avrofile.ffdc.exception.FileException;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


/**
 * AvroFileStoreConnector works with avro files to retrieve simple objects.
 */
public class AvroFileStoreConnector extends ConnectorBase implements AvroFileStore
{
    private String         fileStoreName     = null;

    /*
     * Variables used for logging and debug.
     */
    private static final Logger log = LoggerFactory.getLogger(AvroFileStoreConnector.class);


    /**
     * Initialize the connector.
     *
     * @param connectorInstanceId - unique id for the connector instance - useful for messages etc
     * @param connectionProperties - POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String connectorInstanceId, ConnectionProperties connectionProperties)
    {
        super.initialize(connectorInstanceId, connectionProperties);

        EndpointProperties  endpoint                = connectionProperties.getEndpoint();

        if (endpoint != null)
        {
            fileStoreName = endpoint.getAddress();
        }
        else
        {
            log.error("Null endpoint");
        }

    }


    /**
     * Throw a standard exception based on the supplied error code.
     *
     * @param errorCode error code describing the problem
     * @param methodName calling method
     * @param fileStoreName name of file
     * @throws FileException exception that is generated
     */
    private void throwException(AvroFileConnectorErrorCode errorCode,
                                String                     methodName,
                                String                     fileStoreName,
                                Throwable                  caughtException) throws FileException
    {
        String  errorMessage;

        if (fileStoreName == null)
        {
            errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(super.connectionBean.getQualifiedName());
        }
        else
        {
            errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(fileStoreName,
                                                                                              super.connectionBean.getQualifiedName());
        }

        if (caughtException == null)
        {
            throw new FileException(errorCode.getHTTPErrorCode(),
                                    this.getClass().getName(),
                                    methodName,
                                    errorMessage,
                                    errorCode.getSystemAction(),
                                    errorCode.getUserAction(),
                                    fileStoreName);
        }
        else
        {
            throw new FileException(errorCode.getHTTPErrorCode(),
                                    this.getClass().getName(),
                                    methodName,
                                    errorMessage,
                                    errorCode.getSystemAction(),
                                    errorCode.getUserAction(),
                                    caughtException,
                                    fileStoreName);
        }
    }


    /**
     * Return the Java File object that provides access to the Avro file.
     *
     * @return File object
     */
    public File  getAvroFile() throws FileException
    {
        final String  methodName = "getAvroFile";

        try
        {
            if (fileStoreName == null)
            {
                this.throwException(AvroFileConnectorErrorCode.FILE_NOT_SPECIFIED, methodName, null, null);
            }

            File  fileStore = new File(fileStoreName);

            if (! fileStore.exists())
            {
                this.throwException(AvroFileConnectorErrorCode.FILE_NOT_FOUND, methodName, fileStoreName, null);
            }

            if (fileStore.isDirectory())
            {
                this.throwException(AvroFileConnectorErrorCode.DIRECTORY_SPECIFIED, methodName, fileStoreName, null);
            }

            if (! fileStore.canRead())
            {
                this.throwException(AvroFileConnectorErrorCode.FILE_NOT_READABLE, methodName, fileStoreName, null);
            }

            return fileStore;
        }
        catch (SecurityException  error)
        {
            this.throwException(AvroFileConnectorErrorCode.UNEXPECTED_SECURITY_EXCEPTION, methodName, fileStoreName, error);
        }
        catch (Throwable error)
        {
            this.throwException(AvroFileConnectorErrorCode.UNEXPECTED_IO_EXCEPTION, methodName, fileStoreName, error);
        }

        return null;
    }


    /**
     * Close the file
     */
    public void disconnect()
    {
        try
        {
            super.disconnect();
        }
        catch (Throwable  exec)
        {
            log.debug("Ignoring unexpected exception " + exec.getClass().getSimpleName() + " with message " + exec.getMessage());
        }

        log.debug("Closing Avro File");
    }
}
