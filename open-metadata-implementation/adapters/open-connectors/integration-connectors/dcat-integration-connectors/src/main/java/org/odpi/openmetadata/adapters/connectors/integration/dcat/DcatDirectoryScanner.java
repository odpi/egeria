/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.dcat;

import org.odpi.openmetadata.adapters.connectors.integration.dcat.ffdc.DcatIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecorder;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.dcat.DcatDocumentFormatter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * DcatDirectoryScanner watches a directory tree (for example a drop folder) for DCAT documents.
 * On each scan it looks for JSON-LD files that describe DCAT catalogs, datasets, dataset series or data services and
 * publishes those that are new, or have changed since they were last published.  Hidden directories (such as .git)
 * are skipped.  The scanner remembers the last modified time of each file it has processed so that a document is only
 * published once per change.  A file that is not a DCAT document (for example some other JSON file) is remembered too,
 * so it is only re-examined when it changes.
 */
public class DcatDirectoryScanner
{
    /**
     * Callback used to publish a document found by the scanner.
     */
    @FunctionalInterface
    public interface DocumentPublisher
    {
        /**
         * Publish a document.
         *
         * @param rawDocument document content (JSON-LD)
         */
        void publish(String rawDocument);
    }

    private final File              directory;
    private final String            sourceName;
    private final Map<String, Long> lastModifiedByPath = new HashMap<>();


    /**
     * Constructor.
     *
     * @param directory directory to scan
     * @param sourceName where the directory name came from (for messages)
     */
    public DcatDirectoryScanner(File   directory,
                                String sourceName)
    {
        this.directory  = directory;
        this.sourceName = sourceName;
    }


    /**
     * Return the directory being scanned.
     *
     * @return file
     */
    public File getDirectory()
    {
        return directory;
    }


    /**
     * Return whether a file name has one of the extensions used for DCAT documents in JSON-LD.
     *
     * @param fileName name of file
     * @return boolean
     */
    static boolean isCandidateFile(String fileName)
    {
        String lowerCaseName = fileName.toLowerCase(Locale.ROOT);

        return lowerCaseName.endsWith(".jsonld") || lowerCaseName.endsWith(".json");
    }


    /**
     * Scan the directory tree and publish the new and changed DCAT documents.
     *
     * @param publisher callback that receives each document
     * @return the files published by this scan
     * @throws IOException the directory can not be read
     */
    public synchronized List<File> scan(DocumentPublisher publisher) throws IOException
    {
        final List<File> publishedFiles = new ArrayList<>();

        if ((directory == null) || (! directory.isDirectory()) || (! directory.canRead()))
        {
            throw new IOException("Not a readable directory: " + directory);
        }

        Files.walkFileTree(directory.toPath(), new SimpleFileVisitor<>()
        {
            @Override
            public FileVisitResult preVisitDirectory(Path                dir,
                                                     BasicFileAttributes attrs)
            {
                Path name = dir.getFileName();

                if ((name != null) && (name.toString().startsWith(".")) && (! dir.equals(directory.toPath())))
                {
                    return FileVisitResult.SKIP_SUBTREE;
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path                path,
                                             BasicFileAttributes attrs)
            {
                Path name = path.getFileName();

                if ((attrs.isRegularFile()) && (name != null) && (isCandidateFile(name.toString())) && (! name.toString().startsWith(".")))
                {
                    String pathName     = path.toString();
                    long   lastModified = attrs.lastModifiedTime().toMillis();
                    Long   previous     = lastModifiedByPath.get(pathName);

                    if ((previous == null) || (previous != lastModified))
                    {
                        lastModifiedByPath.put(pathName, lastModified);

                        try
                        {
                            String rawDocument = Files.readString(path, StandardCharsets.UTF_8);

                            if (isDcatDocument(rawDocument))
                            {
                                publisher.publish(rawDocument);
                                publishedFiles.add(path.toFile());
                            }
                        }
                        catch (IOException error)
                        {
                            /*
                             * Forget the file so that it is retried on the next scan.
                             */
                            lastModifiedByPath.remove(pathName);
                        }
                    }
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path        file,
                                                   IOException error)
            {
                return FileVisitResult.CONTINUE;
            }
        });

        return publishedFiles;
    }


    /**
     * Return whether the content of a file is a DCAT document.
     *
     * @param rawDocument content of the file
     * @return boolean
     */
    private boolean isDcatDocument(String rawDocument)
    {
        try
        {
            return DcatDocumentFormatter.hasContent(DcatDocumentFormatter.parseDocument(rawDocument));
        }
        catch (Exception error)
        {
            /*
             * Not valid JSON-LD - it is not a DCAT document.
             */
            return false;
        }
    }


    /**
     * Scan the directory tree, publish the new and changed documents through the integration context and write the
     * outcome to the audit log.  Problems reading the directory are logged rather than thrown so that the connector
     * carries on with its other directories and retries on the next refresh.
     *
     * @param integrationContext context used to publish the documents
     * @param auditLog logging destination
     * @param connectorName name of the calling connector (for messages)
     */
    public void scanAndPublish(IntegrationContext integrationContext,
                               AuditLog           auditLog,
                               String             connectorName)
    {
        final String methodName = "scanAndPublish";

        try
        {
            for (File publishedFile : this.scan(integrationContext::publishDcatDocument))
            {
                AuditLogRecorder.logRecord(auditLog, methodName,
                                           DcatIntegrationConnectorAuditCode.DOCUMENT_PUBLISHED.getMessageDefinition(connectorName,
                                                                                                                     publishedFile.getPath()));
            }
        }
        catch (IOException error)
        {
            AuditLogRecorder.logRecord(auditLog, methodName,
                                       DcatIntegrationConnectorAuditCode.DIRECTORY_NOT_ACCESSIBLE.getMessageDefinition(connectorName,
                                                                                                                       String.valueOf(directory),
                                                                                                                       sourceName,
                                                                                                                       error.getMessage()));
        }
        catch (Exception error)
        {
            AuditLogRecorder.logExceptionRecord(auditLog, methodName,
                                                DcatIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                            error.getClass().getName(),
                                                                                                                            methodName,
                                                                                                                            error.getMessage()),
                                                error);
        }
    }
}
