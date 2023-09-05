/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.directory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.directory.ffdc.DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.RepositoryElementHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class DirectoryBasedOpenMetadataArchiveStoreIterator<T extends RepositoryElementHeader> implements Iterator<T>
{
    private static final Logger log = LoggerFactory.getLogger(DirectoryBasedOpenMetadataArchiveStoreIterator.class);

    private DirectoryBasedOpenMetadataArchiveStore archiveStore;
    private List<File>                             files;
    private int                                    length = 0;
    private int                                    pointer = 0;
    private AuditLog                               auditLog;

    private static final ObjectReader OBJECT_READER = new ObjectMapper().reader();


    public DirectoryBasedOpenMetadataArchiveStoreIterator(DirectoryBasedOpenMetadataArchiveStore archiveStore,
                                                          List<File>                             files,
                                                          AuditLog                               auditLog)
    {
        this.files = files;
        this.archiveStore = archiveStore;
        this.auditLog = auditLog;

        if (files != null)
        {
            length = files.size();
        }
    }


    @Override
    public boolean hasNext()
    {
        return (pointer < length);
    }

    @Override
    public T next()
    {
        final String methodName = "next";

        File elementFile = files.get(pointer);

        try
        {
            String archiveStoreFileContents = FileUtils.readFileToString(elementFile, "UTF-8");

            return (T) OBJECT_READER.readValue(archiveStoreFileContents, RepositoryElementHeader.class);
        }
        catch (Exception error)
        {
            log.error(methodName + " unusable Archive Store :(", error);

            if (auditLog != null)
            {
                final String actionDescription = "Unable to open archive directory";

                auditLog.logException(actionDescription,
                                      DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode.BAD_FILE.getMessageDefinition(elementFile.getName(),
                                                                                                                             error.getClass().getName(),
                                                                                                                             error.getMessage()),
                                      error);
            }
        }

        return null;
    }
}
