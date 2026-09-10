/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.bitol;

import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.controls.FilesTemplateType;
import org.odpi.openmetadata.adapters.connectors.integration.bitol.ffdc.BitolIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.odpi.openmetadata.frameworks.integration.bitol.mapping.BitolMapperBase;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.controls.FileSystemConfigurationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.fileclassifier.FileClassification;
import org.odpi.openmetadata.frameworks.openmetadata.fileclassifier.FileClassifier;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.FileType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * BitolFileCataloguer catalogs a file that holds a Bitol document as a data asset, using the YAML or JSON file
 * template for the file's format and the deployed implementation type for the document's kind, and links the asset
 * as a resource of the agreement or digital product catalogued from the document.  It is shared by the files receiver
 * (for the documents it reads) and the file store (for the documents it writes).
 * <br><br>
 * The templates come from the Files Content Pack.  When they are not loaded, the failure is reported once per
 * connector and the documents continue to flow: cataloguing the file is an addition to publishing the document, not
 * a condition of it.
 */
public class BitolFileCataloguer
{
    private final IntegrationContext integrationContext;
    private final AuditLog           auditLog;
    private final String             connectorName;
    private final PropertyHelper     propertyHelper = new PropertyHelper();

    private boolean templatesReported = false;


    /**
     * Constructor.
     *
     * @param integrationContext context used to catalog the files
     * @param auditLog logging destination
     * @param connectorName name of the calling connector (for messages)
     */
    public BitolFileCataloguer(IntegrationContext integrationContext,
                               AuditLog           auditLog,
                               String             connectorName)
    {
        this.integrationContext = integrationContext;
        this.auditLog           = auditLog;
        this.connectorName      = connectorName;
    }


    /**
     * Catalog a document file and link it to the element catalogued from the document.
     *
     * @param file the file
     * @param kind kind of document (DataContract or DataProduct)
     * @param document parsed document, used to describe the asset and find the catalogued element (may be null)
     * @return unique identifier of the asset, or null if the file could not be catalogued
     */
    public synchronized String catalogDocumentFile(File          file,
                                                   String        kind,
                                                   BitolDocument document)
    {
        final String methodName = "catalogDocumentFile";

        if ((integrationContext == null) || (file == null) || (kind == null))
        {
            return null;
        }

        try
        {
            FileClassifier fileClassifier = integrationContext.getFileClassifier(FileSystemConfigurationProperty.FILE_SYSTEM_NAME.getExample(),
                                                                                 FileSystemConfigurationProperty.CANONICAL_MOUNT_POINT.getExample(),
                                                                                 FileSystemConfigurationProperty.LOCAL_MOUNT_POINT.getExample());

            FileClassification fileClassification = fileClassifier.classifyFile(file);

            String assetTypeName = fileClassification.getAssetTypeName();
            String templateGUID  = FilesTemplateType.getDefaultTemplateGUID(assetTypeName);

            if (templateGUID == null)
            {
                reportTemplateProblem(methodName, kind, file, "no file template is known for asset type " + assetTypeName);
                return null;
            }

            boolean                    isJSON                     = OpenMetadataType.JSON_FILE.typeName.equals(assetTypeName);
            DeployedImplementationType deployedImplementationType = getDeployedImplementationType(kind, isJSON);
            FileType                   fileType                   = getFileType(kind, isJSON);

            Map<String, String> placeholderProperties = new HashMap<>();

            placeholderProperties.put(PlaceholderProperty.FILE_SYSTEM_NAME.getName(), fileClassification.getFileSystemName());
            placeholderProperties.put(PlaceholderProperty.FILE_PATH_NAME.getName(), fileClassification.getCanonicalPathName());
            placeholderProperties.put(PlaceholderProperty.FILE_ADDRESS.getName(), fileClassification.getFileAddress());
            placeholderProperties.put(PlaceholderProperty.FILE_NAME.getName(), fileClassification.getFileName());
            placeholderProperties.put(PlaceholderProperty.FILE_TYPE.getName(), fileType.getFileTypeName());
            placeholderProperties.put(PlaceholderProperty.FILE_EXTENSION.getName(), fileClassification.getFileExtension());
            placeholderProperties.put(PlaceholderProperty.FILE_ENCODING.getName(), fileClassification.getEncoding());
            placeholderProperties.put(PlaceholderProperty.PROGRAMMING_LANGUAGE.getName(), null);
            placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), (document != null) ? document.getVersion() : null);
            placeholderProperties.put(PlaceholderProperty.DESCRIPTION.getName(), getDescription(kind, document));
            placeholderProperties.put(PlaceholderProperty.DEPLOYED_IMPLEMENTATION_TYPE.getName(), deployedImplementationType.getDeployedImplementationType());

            AssetClient     assetClient     = integrationContext.getAssetClient(assetTypeName);
            TemplateOptions templateOptions = new TemplateOptions(assetClient.getMetadataSourceOptions());

            /*
             * A file that is already catalogued (for example on a later refresh, or by a folder cataloguer) is
             * reused rather than duplicated.
             */
            templateOptions.setAllowRetrieve(true);

            String assetGUID = assetClient.createAssetFromTemplate(templateOptions, templateGUID, null, null, placeholderProperties, null);

            /*
             * The template substitutes the deployedImplementationType placeholder when it creates the asset.  An
             * asset that a folder cataloguer created earlier carries that connector's type, so the type that
             * identifies the document is (re)applied to cover the retrieved case too.
             */
            DataFileProperties assetProperties = new DataFileProperties();

            assetProperties.setDeployedImplementationType(deployedImplementationType.getDeployedImplementationType());

            assetClient.updateAsset(assetGUID, assetClient.getUpdateOptions(true), assetProperties);

            linkToCataloguedElement(kind, document, assetGUID);

            auditLog.logMessage(methodName,
                                BitolIntegrationConnectorAuditCode.FILE_CATALOGUED.getMessageDefinition(connectorName,
                                                                                                        kind,
                                                                                                        file.getPath(),
                                                                                                        assetGUID,
                                                                                                        deployedImplementationType.getDeployedImplementationType()));

            return assetGUID;
        }
        catch (Exception error)
        {
            reportTemplateProblem(methodName, kind, file, error.getClass().getSimpleName() + ": " + error.getMessage());

            return null;
        }
    }


    /**
     * Link the asset as a resource of the agreement or digital product catalogued from the document, if that element
     * exists and is not already linked to it.
     *
     * @param kind kind of document
     * @param document parsed document (may be null)
     * @param assetGUID asset for the file
     * @throws Exception problem working with the metadata store
     */
    private void linkToCataloguedElement(String        kind,
                                         BitolDocument document,
                                         String        assetGUID) throws Exception
    {
        if ((document == null) || (document.getId() == null))
        {
            return;
        }

        String qualifiedName = BitolMapperBase.getDocumentQualifiedName(kind, document.getId(), document.getVersion());

        OpenMetadataElement element = integrationContext.getOpenMetadataStore().getMetadataElementByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name);

        if (element == null)
        {
            return;
        }

        OpenMetadataRootElement collection = integrationContext.getCollectionClient().getCollectionByGUID(element.getElementGUID(), integrationContext.getCollectionClient().getGetOptions());

        if ((collection != null) && (collection.getResourceList() != null))
        {
            for (RelatedMetadataElementSummary resource : collection.getResourceList())
            {
                if ((resource != null) && (resource.getRelatedElement() != null) && (assetGUID.equals(resource.getRelatedElement().getElementHeader().getGUID())))
                {
                    return;
                }
            }
        }

        ElementProperties properties = propertyHelper.addStringProperty(null, OpenMetadataProperty.RESOURCE_USE.name, ResourceUse.BITOL_DOCUMENT.getResourceUse());

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DESCRIPTION.name, ResourceUse.BITOL_DOCUMENT.getDescription());

        integrationContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                                              element.getElementGUID(),
                                                                              assetGUID,
                                                                              null,
                                                                              null,
                                                                              properties);
    }


    /**
     * Report that a file could not be catalogued.  The first report for a connector is a full message; later
     * reports are suppressed so that a missing template does not fill the audit log on every refresh.
     *
     * @param methodName calling method
     * @param kind kind of document
     * @param file the file
     * @param reason why
     */
    private void reportTemplateProblem(String methodName,
                                       String kind,
                                       File   file,
                                       String reason)
    {
        if (! templatesReported)
        {
            auditLog.logMessage(methodName,
                                BitolIntegrationConnectorAuditCode.FILE_NOT_CATALOGUED.getMessageDefinition(connectorName,
                                                                                                            kind,
                                                                                                            file.getPath(),
                                                                                                            reason));
            templatesReported = true;
        }
    }


    /**
     * Return the deployed implementation type for a document file.
     *
     * @param kind kind of document
     * @param isJSON true for a JSON file, false for YAML
     * @return deployed implementation type
     */
    static DeployedImplementationType getDeployedImplementationType(String  kind,
                                                                    boolean isJSON)
    {
        if (BitolDocument.DATA_PRODUCT_KIND.equals(kind))
        {
            return isJSON ? DeployedImplementationType.OPEN_DATA_PRODUCT_JSON_FILE : DeployedImplementationType.OPEN_DATA_PRODUCT_FILE;
        }

        return isJSON ? DeployedImplementationType.OPEN_DATA_CONTRACT_JSON_FILE : DeployedImplementationType.OPEN_DATA_CONTRACT_FILE;
    }


    /**
     * Return the file type for a document file.
     *
     * @param kind kind of document
     * @param isJSON true for a JSON file, false for YAML
     * @return file type
     */
    static FileType getFileType(String  kind,
                                boolean isJSON)
    {
        if (BitolDocument.DATA_PRODUCT_KIND.equals(kind))
        {
            return isJSON ? FileType.OPEN_DATA_PRODUCT_JSON_FILE : FileType.OPEN_DATA_PRODUCT_FILE;
        }

        return isJSON ? FileType.OPEN_DATA_CONTRACT_JSON_FILE : FileType.OPEN_DATA_CONTRACT_FILE;
    }


    /**
     * Build the description of the asset from the document.
     *
     * @param kind kind of document
     * @param document parsed document (may be null)
     * @return description
     */
    private static String getDescription(String        kind,
                                         BitolDocument document)
    {
        if ((document == null) || (document.getId() == null))
        {
            return "Bitol " + kind + " document.";
        }

        String name = (document.getName() != null) ? document.getName() + " (" + document.getId() + ")" : document.getId();

        return "Bitol " + kind + " document " + name + ((document.getVersion() != null) ? " version " + document.getVersion() : "") + ".";
    }
}
