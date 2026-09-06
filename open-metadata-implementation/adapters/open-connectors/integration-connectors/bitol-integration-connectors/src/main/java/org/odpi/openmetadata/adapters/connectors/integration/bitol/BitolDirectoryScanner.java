/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.bitol;

import org.odpi.openmetadata.adapters.connectors.integration.bitol.ffdc.BitolIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentFormatter;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;

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
 * BitolDirectoryScanner watches a directory tree (for example a git checkout or a drop folder) for Bitol documents.
 * On each scan it looks for YAML and JSON files whose "kind" is DataContract or DataProduct and publishes those that
 * are new, or have changed since they were last published.  Hidden directories (such as .git) are skipped.
 * The scanner remembers the last modified time of each file it has processed so that a document is only published
 * once per change.  A file that is not a Bitol document (for example a build configuration in YAML) is remembered too,
 * so it is only re-examined when it changes.
 */
public class BitolDirectoryScanner
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
         * @param rawDocument document content (YAML or JSON)
         */
        void publish(String rawDocument);
    }


    /**
     * Description of a document published during a scan.
     *
     * @param file file that held the document
     * @param kind the kind of document (DataContract or DataProduct)
     */
    public record PublishedDocument(File file, String kind)
    {
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
    public BitolDirectoryScanner(File   directory,
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
     * Return where the directory name came from.
     *
     * @return string description
     */
    public String getSourceName()
    {
        return sourceName;
    }


    /**
     * Return whether a file name has one of the extensions used for Bitol documents.
     *
     * @param fileName name of file
     * @return boolean
     */
    static boolean isCandidateFile(String fileName)
    {
        String lowerCaseName = fileName.toLowerCase(Locale.ROOT);

        return lowerCaseName.endsWith(".yaml") || lowerCaseName.endsWith(".yml") || lowerCaseName.endsWith(".json");
    }


    /**
     * Scan the directory tree and publish the new and changed Bitol documents.
     *
     * @param publisher callback that receives each document
     * @return the documents published by this scan
     * @throws IOException the directory can not be read
     */
    public synchronized List<PublishedDocument> scan(DocumentPublisher publisher) throws IOException
    {
        final List<PublishedDocument> publishedDocuments = new ArrayList<>();

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

                if ((name != null) && (name.toString().startsWith(".")))
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

                        String kind = readKind(path);

                        if ((BitolDocument.DATA_CONTRACT_KIND.equals(kind)) || (BitolDocument.DATA_PRODUCT_KIND.equals(kind)))
                        {
                            try
                            {
                                publisher.publish(Files.readString(path, StandardCharsets.UTF_8));
                                publishedDocuments.add(new PublishedDocument(path.toFile(), kind));
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

        return publishedDocuments;
    }


    /**
     * Return the kind of Bitol document in a file, or null if the file is not a Bitol document.
     *
     * @param path file
     * @return kind or null
     */
    private String readKind(Path path)
    {
        try
        {
            return BitolDocumentFormatter.getKind(Files.readString(path, StandardCharsets.UTF_8));
        }
        catch (Exception error)
        {
            /*
             * Not valid YAML/JSON, or unreadable - it is not a Bitol document.
             */
            return null;
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
            List<PublishedDocument> publishedDocuments = this.scan(integrationContext::publishBitolDocument);

            for (PublishedDocument publishedDocument : publishedDocuments)
            {
                auditLog.logMessage(methodName,
                                    BitolIntegrationConnectorAuditCode.DOCUMENT_PUBLISHED.getMessageDefinition(connectorName,
                                                                                                               publishedDocument.kind(),
                                                                                                               publishedDocument.file().getPath()));
            }
        }
        catch (IOException error)
        {
            auditLog.logMessage(methodName,
                                BitolIntegrationConnectorAuditCode.DIRECTORY_NOT_ACCESSIBLE.getMessageDefinition(connectorName,
                                                                                                                 String.valueOf(directory),
                                                                                                                 sourceName,
                                                                                                                 error.getMessage()));
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  BitolIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                               error.getClass().getName(),
                                                                                                               methodName,
                                                                                                               error.getMessage()),
                                  error);
        }
    }
}
