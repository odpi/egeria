/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.governanceaction.fileclassifier;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ValidMetadataValue;

import java.io.File;
import java.util.List;

/**
 * Manages different types of classifications for a single file.
 */
public class FileClassifier
{
    private final String  fileName;
    private final String  pathName;
    private final String  fileExtension;
    private final boolean canRead;
    private final boolean canWrite;
    private final boolean canExecute;
    private       String  fileType                   = null;
    private       String  deployedImplementationType = null;
    private       String  encoding                   = null;
    private       String  assetTypeName              = OpenMetadataType.DATA_FILE.typeName;

    private final static String folderDivider = "/";
    private final static String fileExtensionDivider = "\\.";


    /**
     * Retrieves the extension from a file name.  For example, if the file name is "three.txt", the method
     * returns "txt".  If the path name has multiple extensions, such as "my-jar.jar.gz", the final extension is returned (ie "gz").
     * Null is returned if there is no file extension in the file name.
     *
     * @param fileName short name
     * @return file extension
     */
    public static String getFileExtension(String fileName)
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


    private static final String fileTypeCategory =
            OpenMetadataValidValues.constructValidValueCategory(OpenMetadataType.DATA_FILE.typeName,
                                                                OpenMetadataProperty.FILE_TYPE.name,
                                                                null);
    private static final String deployedImplementationTypeCategory =
            OpenMetadataValidValues.constructValidValueCategory(OpenMetadataType.DATA_FILE.typeName,
                                                                OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                null);


    /**
     * Use the valid values to classify the file.
     *
     * @param openMetadataStore open metadata where the valid values are stored.
     * @param file file to analyse
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem connecting to the open metadata repositories
     * @throws UserNotAuthorizedException insufficient access
     */
    public FileClassifier (OpenMetadataStore openMetadataStore,
                           File              file) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        this.fileName      = file.getName();
        this.pathName      = file.getAbsolutePath();
        this.fileExtension = getFileExtension(file.getName());
        this.canRead       = file.canRead();
        this.canWrite      = file.canWrite();
        this.canExecute    = file.canExecute();

        /*
         * Is the file name or file extension recognized?
         */
        ValidMetadataValue validMetadataValue;
        try
        {
            validMetadataValue = openMetadataStore.getValidMetadataValue(OpenMetadataType.DATA_FILE.typeName,
                                                                         OpenMetadataProperty.FILE_NAME.name,
                                                                         file.getName());
        }
        catch (InvalidParameterException notKnown)
        {
            validMetadataValue = null;
        }

        List<ValidMetadataValue> consistentValues = null;

        if (validMetadataValue != null)
        {
            consistentValues = openMetadataStore.getConsistentMetadataValues(OpenMetadataType.DATA_FILE.typeName,
                                                                             OpenMetadataProperty.FILE_NAME.name,
                                                                             null,
                                                                             validMetadataValue.getPreferredValue(),
                                                                             0,
                                                                             5);
        }
        else
        {
            if (this.fileExtension != null)
            {
                try
                {
                    validMetadataValue = openMetadataStore.getValidMetadataValue(OpenMetadataType.DATA_FILE.typeName,
                                                                                 OpenMetadataProperty.FILE_EXTENSION.name,
                                                                                 this.fileExtension);
                }
                catch (InvalidParameterException notKnown)
                {
                    // validMetadataValue = null - already set
                }

                if (validMetadataValue != null)
                {
                    consistentValues = openMetadataStore.getConsistentMetadataValues(OpenMetadataType.DATA_FILE.typeName,
                                                                                     OpenMetadataProperty.FILE_EXTENSION.name,
                                                                                     null,
                                                                                     validMetadataValue.getPreferredValue(),
                                                                                     0,
                                                                                     5);
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
                    if (fileTypeCategory.equals(consistentValue.getCategory()))
                    {
                        this.fileType = consistentValue.getPreferredValue();

                        if (consistentValue.getAdditionalProperties() != null)
                        {
                            if (consistentValue.getAdditionalProperties().get(OpenMetadataValidValues.ASSET_SUB_TYPE_NAME) != null)
                            {
                                this.assetTypeName = consistentValue.getAdditionalProperties().get(OpenMetadataValidValues.ASSET_SUB_TYPE_NAME);
                            }

                            if (consistentValue.getAdditionalProperties().get(OpenMetadataProperty.ENCODING.name) != null)
                            {
                                this.encoding = consistentValue.getAdditionalProperties().get(OpenMetadataProperty.ENCODING.name);
                            }
                        }

                        List<ValidMetadataValue> consistentFileTypeValues =
                                openMetadataStore.getConsistentMetadataValues(OpenMetadataType.DATA_FILE.typeName,
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
                                    if (deployedImplementationTypeCategory.equals(consistentFileTypeValue.getCategory()))
                                    {
                                        this.deployedImplementationType = consistentFileTypeValue.getPreferredValue();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Return the short file name of the file.
     *
     * @return string name
     */
    public String getFileName()
    {
        return fileName;
    }


    /**
     * Return the full pathname of the file.
     *
     * @return string name
     */
    public String getPathName()
    {
        return pathName;
    }


    /**
     * Return the file extension of the file.
     *
     * @return letters after the "dot"
     */
    public String getFileExtension()
    {
        return fileExtension;
    }


    /**
     * return the logical file type.
     *
     * @return string name
     */
    public String getFileType()
    {
        return fileType;
    }


    /**
     * Return the deployed implementation type.
     *
     * @return string name
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Return the asset type name.
     *
     * @return string name
     */
    public String getAssetTypeName()
    {
        return assetTypeName;
    }


    /**
     * Return the encoding of the file - may be null
     *
     * @return encoding standard
     */
    public String getEncoding()
    {
        return encoding;
    }


    /**
     * Is the file readable?
     *
     * @return boolean
     */
    public boolean isCanRead()
    {
        return canRead;
    }


    /**
     * Is the file writable?
     *
     * @return boolean
     */
    public boolean isCanWrite()
    {
        return canWrite;
    }


    /**
     * Is the file executable?
     *
     * @return boolean
     */
    public boolean isCanExecute()
    {
        return canExecute;
    }

}
