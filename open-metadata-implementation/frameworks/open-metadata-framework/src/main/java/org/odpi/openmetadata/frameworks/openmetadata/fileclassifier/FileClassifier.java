/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.fileclassifier;

import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ValidMetadataValuesClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ValidMetadataValue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;

/**
 * Manages different types of classifications for files.  It retrieves file reference data from
 * the open metadata store and maintains a cache.
 */
public class FileClassifier
{
    private final static String folderDivider = "/";
    private final static String fileExtensionDivider = "\\.";

    private final static Map<String, FileReferenceDataCache> fileNameReferenceDataCache = new HashMap<>();
    private final static Map<String, FileReferenceDataCache> fileExtensionReferenceDataCache = new HashMap<>();

    private final ValidMetadataValuesClient validMetadataValuesClient;

    /**
     * Construct the name used to find the file type reference value
     */
    private static final String fileTypeCategory =
            OpenMetadataValidValues.constructValidValueNamespace(OpenMetadataType.DATA_FILE.typeName,
                                                                 OpenMetadataProperty.FILE_TYPE.name,
                                                                 null);

    /**
     * Construct the name used to find the deployed implementation type reference value
     */
    private static final String deployedImplementationTypeCategory =
            OpenMetadataValidValues.constructValidValueNamespace(OpenMetadataType.DATA_FILE.typeName,
                                                                 OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                 null);





    /**
     * Use the valid values to classify files on request.
     *
     * @param connectorContext context being used by the connector.
     */
    public FileClassifier (ConnectorContextBase connectorContext)
    {
        this.validMetadataValuesClient = connectorContext.getValidMetadataValuesClient();
    }


    /**
     * Classify the properties of the file  represented by the path name.
     *
     * @param pathName name of the file
     * @return file classification
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem connecting to the open metadata repositories
     * @throws UserNotAuthorizedException insufficient access
     * @throws IOException unable to access the attributes of the file
     */
    public FileClassification classifyFile(String pathName)  throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException,
                                                                    IOException
    {
        return classifyFile(new File(pathName));
    }


    /**
     * Classify the properties of the file represented by the Java File object.
     *
     * @param file details of the file
     * @return file classification
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem connecting to the open metadata repositories
     * @throws UserNotAuthorizedException insufficient access
     */
    public FileClassification classifyFile(File file) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException,
                                                             IOException
    {
        Date creationTime = null;
        Date lastModifiedTime = null;
        Date lastAccessedTime = null;

        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);

        if (attr.creationTime() != null)
        {
            creationTime = new Date(attr.creationTime().toMillis());
        }
        if (attr.lastModifiedTime() != null)
        {
            lastModifiedTime = new Date(attr.lastModifiedTime().toMillis());
        }
        if (attr.lastAccessTime() != null)
        {
            lastAccessedTime = new Date(attr.lastAccessTime().toMillis());
        }

        String fileExtension = getFileExtension(file.getName());

        FileReferenceDataCache fileReferenceDataCache = getFileReferenceDataCache(file.getName(),
                                                                                  fileExtension);

        return new FileClassification(file.getName(),
                                      file.getCanonicalPath(),
                                      fileExtension,
                                      creationTime,
                                      lastModifiedTime,
                                      lastAccessedTime,
                                      file.canRead(),
                                      file.canWrite(),
                                      file.canExecute(),
                                      file.isHidden(),
                                      FileUtils.isSymlink(file),
                                      fileReferenceDataCache.fileType,
                                      fileReferenceDataCache.deployedImplementationType,
                                      fileReferenceDataCache.encoding,
                                      fileReferenceDataCache.assetTypeName,
                                      attr.size());
    }


    /**
     * Supports the caching of file reference data.
     */
    static class FileReferenceDataCache
    {
        String fileType                   = null;
        String assetTypeName              = null;
        String encoding                   = null;
        String deployedImplementationType = null;
    }


    /**
     * Retrieve the reference data for a particular type of file.
     *
     * @param fileName name of the file
     * @param fileExtension file extension
     * @return file reference data
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem connecting to the open metadata repositories
     * @throws UserNotAuthorizedException insufficient access
     */
    synchronized FileReferenceDataCache getFileReferenceDataCache(String fileName,
                                                                  String fileExtension) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        FileReferenceDataCache fileReferenceDataCache = fileNameReferenceDataCache.get(fileName);

        if (fileReferenceDataCache == null)
        {
            fileReferenceDataCache = fileExtensionReferenceDataCache.get(fileExtension);
        }

        if (fileReferenceDataCache == null)
        {
            fileReferenceDataCache = lookupFileReferenceData(fileName, fileExtension);
        }

        return fileReferenceDataCache;
    }



    /**
     * Retrieves the extension from a file name.  For example, if the file name is "three.txt", the method
     * returns "txt".  If the path name has multiple extensions, such as "my-jar.jar.gz", the final extension is returned (ie "gz").
     * Null is returned if there is no file extension in the file name.
     *
     * @param fileName short name
     * @return file extension
     */
    public String getFileExtension(String fileName)
    {
        String result = null;

        if ((fileName != null) && (! fileName.isEmpty()))
        {
            String[] tokens = fileName.split(fileExtensionDivider);

            if (fileName.startsWith("."))
            {
                if (tokens.length > 2)
                {
                    result = tokens[tokens.length - 1];
                }
            }
            else
            {

                if (tokens.length > 1)
                {
                    result = tokens[tokens.length - 1];
                }
            }
        }

        return result;
    }


    /**
     * Retrieve the reference data for a particular type of file.
     *
     * @param fileName name of the file
     * @param fileExtension file extension
     * @return file reference data
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem connecting to the open metadata repositories
     * @throws UserNotAuthorizedException insufficient access
     */
    private FileReferenceDataCache lookupFileReferenceData(String fileName,
                                                           String fileExtension) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        FileReferenceDataCache fileReferenceDataCache = new FileReferenceDataCache();
        boolean                fileNameMatched        = false;
        boolean                fileExtensionMatched   = false;

        /*
         * Is the file name or file extension recognized?
         */
        ValidMetadataValue validMetadataValue;
        try
        {
            validMetadataValue = validMetadataValuesClient.getValidMetadataValue(OpenMetadataType.DATA_FILE.typeName,
                                                                         OpenMetadataProperty.FILE_NAME.name,
                                                                         fileName);
        }
        catch (InvalidParameterException notKnown)
        {
            validMetadataValue = null;
        }

        List<ValidMetadataValue> consistentValues = null;

        if (validMetadataValue != null)
        {
            consistentValues = validMetadataValuesClient.getConsistentMetadataValues(OpenMetadataType.DATA_FILE.typeName,
                                                                             OpenMetadataProperty.FILE_NAME.name,
                                                                             null,
                                                                             validMetadataValue.getPreferredValue(),
                                                                             0,
                                                                             5);
            fileNameMatched = true;
        }
        else
        {
            if (fileExtension != null)
            {
                try
                {
                    validMetadataValue = validMetadataValuesClient.getValidMetadataValue(OpenMetadataType.DATA_FILE.typeName,
                                                                                 OpenMetadataProperty.FILE_EXTENSION.name,
                                                                                 fileExtension);
                }
                catch (InvalidParameterException notKnown)
                {
                    // validMetadataValue = null - already set
                }

                if (validMetadataValue != null)
                {
                    consistentValues = validMetadataValuesClient.getConsistentMetadataValues(OpenMetadataType.DATA_FILE.typeName,
                                                                                     OpenMetadataProperty.FILE_EXTENSION.name,
                                                                                     null,
                                                                                     validMetadataValue.getPreferredValue(),
                                                                                     0,
                                                                                     5);

                    fileExtensionMatched = true;
                }
            }
        }


        /*
         * The fileType valid metadata value links to the deployed implementation type.
         */
        if (consistentValues != null)
        {
            for (ValidMetadataValue consistentValue : consistentValues)
            {
                if (consistentValue != null)
                {
                    if (consistentValue.getNamespace().contains(OpenMetadataProperty.FILE_TYPE.name))
                    {
                        fileReferenceDataCache.fileType = consistentValue.getPreferredValue();

                        if (consistentValue.getAdditionalProperties() != null)
                        {
                            if (consistentValue.getAdditionalProperties().get(OpenMetadataValidValues.ASSET_SUB_TYPE_NAME) != null)
                            {
                                fileReferenceDataCache.assetTypeName = consistentValue.getAdditionalProperties().get(OpenMetadataValidValues.ASSET_SUB_TYPE_NAME);
                            }

                            if (consistentValue.getAdditionalProperties().get(OpenMetadataProperty.ENCODING_TYPE.name) != null)
                            {
                                fileReferenceDataCache.encoding = consistentValue.getAdditionalProperties().get(OpenMetadataProperty.ENCODING_TYPE.name);
                            }
                        }

                        List<ValidMetadataValue> consistentFileTypeValues =
                                validMetadataValuesClient.getConsistentMetadataValues(OpenMetadataType.DATA_FILE.typeName,
                                                                              OpenMetadataProperty.FILE_TYPE.name,
                                                                              null,
                                                                              consistentValue.getPreferredValue(),
                                                                              0,
                                                                              5);

                        if (consistentFileTypeValues != null)
                        {
                            for (ValidMetadataValue consistentFileTypeValue : consistentFileTypeValues)
                            {
                                if (consistentFileTypeValue != null)
                                {
                                    if (consistentFileTypeValue.getNamespace().contains(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name))
                                    {
                                        fileReferenceDataCache.deployedImplementationType = consistentFileTypeValue.getPreferredValue();
                                    }
                                }
                            }
                        }
                    }
                    else if (consistentValue.getNamespace().contains(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name))
                    {
                        fileReferenceDataCache.deployedImplementationType = consistentValue.getPreferredValue();
                    }
                }
            }
        }

        if (fileNameMatched)
        {
            fileNameReferenceDataCache.put(fileName, fileReferenceDataCache);
        }
        if (fileExtensionMatched)
        {
            fileExtensionReferenceDataCache.put(fileExtension, fileReferenceDataCache);
        }

        return fileReferenceDataCache;
    }
}
