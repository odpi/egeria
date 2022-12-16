/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.directory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.directory.ffdc.DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode;
import org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.directory.ffdc.DirectoryBasedOpenMetadataArchiveStoreConnectorErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.RepositoryElementHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationEntityExtension;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.CollectionDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DirectoryBasedOpenMetadataArchiveStore provides the mechanisms to store and retrieve files
 * from the directory based open metadata archive store.
 */
public class DirectoryBasedOpenMetadataArchiveStore
{
    private static final Logger log = LoggerFactory.getLogger(DirectoryBasedOpenMetadataArchiveStore.class);
    
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectReader OBJECT_READER = OBJECT_MAPPER.reader();
    private static final ObjectWriter OBJECT_WRITER = OBJECT_MAPPER.writer();

    private static final String archivePropertiesFileName = "/archiveProperties.json";
    private static final String typeStoreDirectoryName = "/typeStore";
    private static final String typeDefDirectoryName =  typeStoreDirectoryName + "/typeDefs";
    private static final String entityDefDirectoryName =  typeStoreDirectoryName + typeDefDirectoryName + "/entityDefs";
    private static final String relationshipDefDirectoryName =  typeStoreDirectoryName + typeDefDirectoryName + "/relationshipDefs";
    private static final String classificationDefDirectoryName =  typeStoreDirectoryName + typeDefDirectoryName + "/classificationDefs";
    private static final String attributeTypeDefDirectoryName = typeStoreDirectoryName + "/attributeTypeDefs";
    private static final String primitiveDefDirectoryName = typeStoreDirectoryName + attributeTypeDefDirectoryName + "/primitiveDefs";
    private static final String collectionDefDirectoryName = typeStoreDirectoryName + attributeTypeDefDirectoryName + "/collectionDefs";
    private static final String enumDefDirectoryName = typeStoreDirectoryName + attributeTypeDefDirectoryName + "/enumDefs";
    private static final String typeDefPatchesDirectoryName = typeStoreDirectoryName + "/typeDefPatches";

    private static final String instanceStoreDirectoryName = "/instanceStore";
    private static final String entitiesDirectoryName = instanceStoreDirectoryName + "/entities";
    private static final String relationshipsDirectoryName = instanceStoreDirectoryName + "/relationships";
    private static final String classificationsDirectoryName = instanceStoreDirectoryName + "/classifications";

    private String   archiveStoreName;
    private AuditLog auditLog;
    private boolean  keepVersionHistory;


    /**
     * Create a store for managing the contents of the files in the directory based open metadata archive.
     *
     * @param archiveStoreName  name of the archive store directory
     * @param auditLog logging destination
     * @param keepVersionHistory should all versions of each element be kept?
     */
    DirectoryBasedOpenMetadataArchiveStore(String   archiveStoreName,
                                           AuditLog auditLog,
                                           boolean  keepVersionHistory)
    {
        this.archiveStoreName = archiveStoreName;
        this.auditLog = auditLog;
        this.keepVersionHistory = keepVersionHistory;
    }



    void initializeArchive(String methodName) throws ConnectorCheckedException
    {
        try
        {
            File archiveStoreDirectory = new File(archiveStoreName);
            File typeDefStoreDirectory = new File(archiveStoreName + typeDefDirectoryName);
            File attributeTypeDefStoreDirectory = new File(archiveStoreName + attributeTypeDefDirectoryName);
            File typeDefPatchStoreDirectory = new File(archiveStoreName + typeDefPatchesDirectoryName);
            File entityStoreDirectory = new File(archiveStoreName + entitiesDirectoryName);
            File relationshipStoreDirectory = new File(archiveStoreName + relationshipsDirectoryName);
            File classificationStoreDirectory = new File(archiveStoreName + classificationsDirectoryName);

            FileUtils.forceMkdir(archiveStoreDirectory);
            FileUtils.forceMkdir(typeDefStoreDirectory);
            FileUtils.forceMkdir(attributeTypeDefStoreDirectory);
            FileUtils.forceMkdir(typeDefPatchStoreDirectory);
            FileUtils.forceMkdir(entityStoreDirectory);
            FileUtils.forceMkdir(relationshipStoreDirectory);
            FileUtils.forceMkdir(classificationStoreDirectory);
        }
        catch (IOException ioException)
        {
            final String actionDescription = "Creating open metadata archive directories";

            log.error("Unusable Audit Log Store :(", ioException);

            if (auditLog != null)
            {

                auditLog.logException(actionDescription,
                                      DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode.BAD_FILE.getMessageDefinition(archiveStoreName,
                                                                                                                             ioException.getClass().getName(),
                                                                                                                             ioException.getMessage()),
                                      ioException);
            }

            throw new ConnectorCheckedException(
                    DirectoryBasedOpenMetadataArchiveStoreConnectorErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(
                            ioException.getClass().getName(),
                            methodName,
                            ioException.getMessage()),
                    this.getClass().getName(),
                    actionDescription,
                    ioException);
        }
    }


    void removeArchive(String archiveStoreName,
                       String methodName)
    {
        File    archiveStoreFile = new File(archiveStoreName);

        try
        {
            log.debug("Removing current contents");

            FileUtils.forceDelete(archiveStoreFile);
        }
        catch (IOException ioException)
        {
            log.error(methodName + " unusable Archive Store :(", ioException);

            if (auditLog != null)
            {
                final String actionDescription = "Unable to open archive directory";

                auditLog.logException(actionDescription,
                                      DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode.BAD_FILE.getMessageDefinition(archiveStoreName,
                                                                                                                             ioException.getClass().getName(),
                                                                                                                             ioException.getMessage()),
                                      ioException);
            }
        }
    }

    void writeProperties(OpenMetadataArchiveProperties properties,
                         String                        methodName)
    {
        File propertiesFile = new File(archiveStoreName + archivePropertiesFileName);

        try
        {
            log.debug("fileId: " + archiveStoreName + archivePropertiesFileName);

            String archiveStoreFileContents = OBJECT_WRITER.writeValueAsString(properties);

            FileUtils.writeStringToFile(propertiesFile, archiveStoreFileContents, (String)null, false);
        }
        catch (IOException ioException)
        {
            log.error(methodName + " unusable Archive Store :(", ioException);

            if (auditLog != null)
            {
                final String actionDescription = "Unable to open archive directory";

                auditLog.logException(actionDescription,
                                      DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode.BAD_FILE.getMessageDefinition(archiveStoreName + archivePropertiesFileName,
                                                                                                                             ioException.getClass().getName(),
                                                                                                                             ioException.getMessage()),
                                      ioException);
            }
        }
    }


    OpenMetadataArchiveProperties readProperties(String methodName)
    {
        File elementFile = new File(archiveStoreName + archivePropertiesFileName);

        try
        {
            log.debug("fileId: " + archiveStoreName + archivePropertiesFileName);

            String archiveStoreFileContents = FileUtils.readFileToString(elementFile, "UTF-8");

            return OBJECT_READER.readValue(archiveStoreFileContents, OpenMetadataArchiveProperties.class);

        }
        catch (IOException ioException)
        {
            log.error(methodName + " unusable Archive Store :(", ioException);

            if (auditLog != null)
            {
                final String actionDescription = "Unable to open archive directory";

                auditLog.logException(actionDescription,
                                      DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode.BAD_FILE.getMessageDefinition(archiveStoreName + archivePropertiesFileName,
                                                                                                                             ioException.getClass().getName(),
                                                                                                                             ioException.getMessage()),
                                      ioException);
            }
        }

        return null;
    }


    void writeElement(String                  fileName,
                      RepositoryElementHeader element,
                      long                    version,
                      String                  methodName)
    {
        File elementFile;
        File latestElementFile = null;

        if (keepVersionHistory)
        {
            elementFile = new File(fileName + "/" + version + ".json");
            latestElementFile = new File(fileName + "/" + 0 + ".json");
        }
        else
        {
            elementFile = new File(fileName + ".json");
        }

        try
        {
            log.debug("fileId: " + fileName);

            String archiveStoreFileContents = OBJECT_WRITER.writeValueAsString(element);

            FileUtils.writeStringToFile(elementFile, archiveStoreFileContents, (String)null, false);

            if (latestElementFile != null)
            {
                FileUtils.writeStringToFile(latestElementFile, archiveStoreFileContents, (String)null, false);
            }
        }
        catch (IOException ioException)
        {
            log.error(methodName + " unusable Archive Store :(", ioException);

            if (auditLog != null)
            {
                final String actionDescription = "Unable to open archive directory";

                auditLog.logException(actionDescription,
                                      DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode.BAD_FILE.getMessageDefinition(fileName,
                                                                                                                             ioException.getClass().getName(),
                                                                                                                             ioException.getMessage()),
                                      ioException);
            }
        }
    }


    RepositoryElementHeader readElement(String  fileName,
                                        long    version,
                                        String  methodName)
    {
        File elementFile;

        if (keepVersionHistory)
        {
            elementFile = new File(fileName + "/" + version + ".json");
        }
        else
        {
            elementFile = new File(fileName + ".json");
        }

        try
        {
            log.debug("fileId: " + fileName);

            String archiveStoreFileContents = FileUtils.readFileToString(elementFile, "UTF-8");

            return OBJECT_READER.readValue(archiveStoreFileContents, RepositoryElementHeader.class);

        }
        catch (IOException ioException)
        {
            log.error(methodName + " unusable Archive Store :(", ioException);

            if (auditLog != null)
            {
                final String actionDescription = "Unable to open archive directory";

                auditLog.logException(actionDescription,
                                      DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode.BAD_FILE.getMessageDefinition(fileName,
                                                                                                                             ioException.getClass().getName(),
                                                                                                                             ioException.getMessage()),
                                      ioException);
            }
        }

        return null;
    }


    String getFileName(PrimitiveDef element)
    {
        return archiveStoreName + primitiveDefDirectoryName + "/" + element.getGUID();
    }

    String getFileName(CollectionDef element)
    {
        return archiveStoreName + collectionDefDirectoryName + "/" + element.getGUID();
    }

    String getFileName(EnumDef element)
    {
        return archiveStoreName + enumDefDirectoryName + "/" + element.getGUID();
    }

    String getFileName(EntityDef element)
    {
        return archiveStoreName + entityDefDirectoryName + "/" + element.getGUID();
    }

    String getFileName(RelationshipDef element)
    {
        return archiveStoreName + relationshipDefDirectoryName + "/" + element.getGUID();
    }

    String getFileName(ClassificationDef element)
    {
        return archiveStoreName + classificationDefDirectoryName + "/" + element.getGUID();
    }

    String getFileName(TypeDefPatch element)
    {
        return archiveStoreName + typeDefPatchesDirectoryName + "/" + element.getTypeDefGUID() + ":" + element.getApplyToVersion();
    }

    String getFileName(EntityDetail element)
    {
        return archiveStoreName + entitiesDirectoryName + "/" + element.getGUID();
    }

    String getFileName(Relationship element)
    {
        return archiveStoreName + relationshipsDirectoryName + "/" + element.getGUID();
    }

    String getFileName(ClassificationEntityExtension element)
    {
        return archiveStoreName + classificationsDirectoryName + "/" + element.getEntityToClassify().getGUID() + ":" + element.getClassification().getName();
    }

    List<String> getTypeDefDirectories()
    {
        List<String> directoryNames = new ArrayList<>();

        directoryNames.add(archiveStoreName + entityDefDirectoryName);
        directoryNames.add(archiveStoreName + relationshipDefDirectoryName);
        directoryNames.add(archiveStoreName + classificationDefDirectoryName);

        return directoryNames;
    }

    List<String> getAttributeTypeDefDirectories()
    {
        List<String> directoryNames = new ArrayList<>();

        directoryNames.add(archiveStoreName + primitiveDefDirectoryName);
        directoryNames.add(archiveStoreName + collectionDefDirectoryName);
        directoryNames.add(archiveStoreName + enumDefDirectoryName);

        return directoryNames;
    }


    List<String> getTypeDefPatchesDirectories()
    {
        List<String> directoryNames = new ArrayList<>();

        directoryNames.add(archiveStoreName + typeDefPatchesDirectoryName);

        return directoryNames;
    }


    List<String> getEntityDirectories()
    {
        List<String> directoryNames = new ArrayList<>();

        directoryNames.add(archiveStoreName + entitiesDirectoryName);

        return directoryNames;
    }


    List<String> getRelationshipDirectories()
    {
        List<String> directoryNames = new ArrayList<>();

        directoryNames.add(archiveStoreName + relationshipsDirectoryName);

        return directoryNames;
    }


    List<String> getClassificationDirectories()
    {
        List<String> directoryNames = new ArrayList<>();

        directoryNames.add(archiveStoreName + classificationsDirectoryName);

        return directoryNames;
    }
}
