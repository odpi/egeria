/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.surveyaction.fileclassifier;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ValidMetadataValue;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyOpenMetadataStore;

import java.io.File;
import java.util.List;

/**
 * Manages different types of classifications for a single file.
 */
public class FileClassifier
{
    private String fileExtension              = null;
    private String fileType                   = null;
    private String deployedImplementationType = null;
    private String assetTypeName              = OpenMetadataType.DATA_FILE_TYPE_NAME;
    private boolean canRead                   = false;
    private boolean canWrite                  = false;
    private boolean canExecute                = false;


    /**
     * Retrieves the extension from a path name.  For example, if the pathname is "one/two/three.txt", the method
     * returns "txt".  If the path name has multiple extensions, such as "my-jar.jar.gz", the final extension is returned (ie "gz").
     * Null is returned if there is no file extension in the path name.
     *
     * @param pathName path name of file or directory
     * @return file extension
     */
    public static String getFileExtension(String pathName)
    {
        final String  fileTypeDivider = "\\.";

        String result = null;

        if ((pathName != null) && (! pathName.isEmpty()))
        {
            String[] tokens = pathName.split(fileTypeDivider);

            if (tokens.length > 1)
            {
                result = tokens[tokens.length - 1];
            }
        }

        return result;
    }


    private static final String fileTypeCategory =
            OpenMetadataValidValues.constructValidValueCategory(OpenMetadataType.DATA_FILE_TYPE_NAME,
                                                                OpenMetadataProperty.FILE_TYPE.name,
                                                                null);
    private static final String deployedImplementationTypeCategory =
            OpenMetadataValidValues.constructValidValueCategory(OpenMetadataType.DATA_FILE_TYPE_NAME,
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
    public FileClassifier (SurveyOpenMetadataStore openMetadataStore,
                           File                     file) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        this.fileExtension = getFileExtension(file.getAbsolutePath());


        /*
         * Is the file name or file extension recognized?
         */
        ValidMetadataValue validMetadataValue = openMetadataStore.getValidMetadataValue(OpenMetadataType.DATA_FILE_TYPE_NAME,
                                                                                        OpenMetadataProperty.FILE_NAME.name,
                                                                                        file.getName());

        List<ValidMetadataValue> consistentValues = null;

        if (validMetadataValue != null)
        {
            consistentValues = openMetadataStore.getConsistentMetadataValues(OpenMetadataType.DATA_FILE_TYPE_NAME,
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
                validMetadataValue = openMetadataStore.getValidMetadataValue(OpenMetadataType.DATA_FILE_TYPE_NAME,
                                                                             OpenMetadataProperty.FILE_EXTENSION.name,
                                                                             this.fileExtension);

                if (validMetadataValue != null)
                {
                    consistentValues = openMetadataStore.getConsistentMetadataValues(OpenMetadataType.DATA_FILE_TYPE_NAME,
                                                                                     OpenMetadataProperty.FILE_EXTENSION.name,
                                                                                     null,
                                                                                     validMetadataValue.getPreferredValue(),
                                                                                     0,
                                                                                     5);
                }
            }

            this.canRead = file.canRead();
            this.canWrite = file.canWrite();
            this.canExecute = file.canExecute();
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

                        if ((consistentValue.getAdditionalProperties() != null) &&
                                (consistentValue.getAdditionalProperties().get(OpenMetadataValidValues.ASSET_SUB_TYPE_NAME) != null))
                        {
                            this.assetTypeName = consistentValue.getAdditionalProperties().get(OpenMetadataValidValues.ASSET_SUB_TYPE_NAME);
                        }

                        List<ValidMetadataValue> consistentFileTypeValues =
                                openMetadataStore.getConsistentMetadataValues(OpenMetadataType.DATA_FILE_TYPE_NAME,
                                                                              OpenMetadataProperty.FILE_TYPE.name,
                                                                              null,
                                                                              validMetadataValue.getPreferredValue(),
                                                                              0,
                                                                              5);

                        if (consistentFileTypeValues != null)
                        {
                            for (ValidMetadataValue consistentFileTypeValue : consistentFileTypeValues)
                            {
                                if (consistentFileTypeValue != null)
                                {
                                    if (deployedImplementationTypeCategory.equals(consistentValue.getCategory()))
                                    {
                                        this.deployedImplementationType = consistentValue.getPreferredValue();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        this.canRead = file.canRead();
        this.canWrite = file.canWrite();
        this.canExecute = file.canExecute();
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
