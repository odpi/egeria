/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities;


import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.Category;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;

import java.util.*;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueNamespace;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;


/**
 * CocoBaseArchiveWriter provides a base class for utilities creating a physical open metadata archive file containing  definitions for
 * Coco Pharmaceuticals.
 */
public abstract class EgeriaBaseArchiveWriter extends OMRSArchiveWriter
{
    protected static final String                  archiveLicense     = "Apache 2.0";
    protected static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    protected static final String                  originatorName     = "Egeria Project";

    protected static final String guidMapFileName = "EgeriaContentPacksGUIDMap.json";

    /*
     * Common values for naming of elements in the archive.
     */
    protected static final String openMetadataValidValueSetPrefix = "OpenMetadata.ValidValueSet.";

    /*
     * Specific values for initializing TypeDefs
     */
    protected static final String versionName   = "5.4-SNAPSHOT";

    private final Map<String, String> parentValidValueQNameToGUIDMap = new HashMap<>();

    protected       OMRSArchiveBuilder      archiveBuilder;
    protected       GovernanceArchiveHelper archiveHelper;
    protected final String                  archiveGUID;
    protected final String                  archiveName;
    protected final Date                    creationDate;
    protected final String                  archiveFileName;


    /**
     * Constructor for an archive.
     *
     * @param archiveGUID unique identifier of the archive
     * @param archiveName name of the archive
     * @param archiveDescription description of archive
     * @param creationDate date/time this archive writer ran
     * @param archiveFileName name of file to write archive to
     */
    protected EgeriaBaseArchiveWriter(String                  archiveGUID,
                                      String                  archiveName,
                                      String                  archiveDescription,
                                      Date                    creationDate,
                                      String                  archiveFileName)
    {
        this(archiveGUID, archiveName, archiveDescription, creationDate, archiveFileName, null);
    }


    /**
     * Constructor for an archive.
     *
     * @param archiveGUID unique identifier of the archive
     * @param archiveName name of the archive
     * @param archiveDescription description of archive
     * @param creationDate date/time this archive writer ran
     * @param archiveFileName name of file to write archive to
     * @param additionalDependencies archive that this archive is dependent on
     */
    protected EgeriaBaseArchiveWriter(String                archiveGUID,
                                      String                archiveName,
                                      String                archiveDescription,
                                      Date                  creationDate,
                                      String                archiveFileName,
                                      OpenMetadataArchive[] additionalDependencies)
    {
        this.archiveGUID = archiveGUID;
        this.archiveName = archiveName;
        this.creationDate = creationDate;

        List<OpenMetadataArchive> dependentOpenMetadataArchives = new ArrayList<>();

        /*
         * This value allows the definitions to be based on the existing open metadata types and definitions.
         */
        dependentOpenMetadataArchives.add(new OpenMetadataTypesArchive().getOpenMetadataArchive());

        /*
         * Add in any additional dependencies.
         */
        if (additionalDependencies != null)
        {
            dependentOpenMetadataArchives.addAll(Arrays.asList(additionalDependencies));
        }

        this.archiveBuilder = new OMRSArchiveBuilder(archiveGUID,
                                                     archiveName,
                                                     archiveDescription,
                                                     archiveType,
                                                     originatorName,
                                                     archiveLicense,
                                                     creationDate,
                                                     dependentOpenMetadataArchives);

        /*
         * Note: the version number is based off of the build time to ensure the
         * latest version of the archive elements is loaded.
         */
        this.archiveHelper = new GovernanceArchiveHelper(archiveBuilder,
                                                         archiveGUID,
                                                         archiveName,
                                                         originatorName,
                                                         creationDate,
                                                         new Date().getTime(),
                                                         versionName,
                                                         guidMapFileName);

        this.archiveFileName = archiveFileName;
    }


    /**
     * Provide an alternative archive builder.  Used when consolidating archives.
     *
     * @param archiveBuilder new archive builder
     */
    public void setArchiveBuilder(OMRSArchiveBuilder      archiveBuilder,
                                  GovernanceArchiveHelper archiveHelper)
    {
        this.archiveBuilder = archiveBuilder;
        this.archiveHelper = archiveHelper;
    }


    /**
     * Returns the open metadata archive containing new metadata entities.
     *
     * @return populated open metadata archive object
     */
    public OpenMetadataArchive getOpenMetadataArchive()
    {
        getArchiveContent();

        /*
         * The completed archive is ready to be packaged up and returned
         */
        return this.archiveBuilder.getOpenMetadataArchive();
    }


    /**
     * Implemented by subclass to add the content.
     */
    public abstract void getArchiveContent();



    /**
     * Find or create the parent set for a valid value.
     *
     * @param requestedGUID optional guid for the valid value
     * @param typeName name of the type (can be null)
     * @param propertyName name of the property (can be null)
     * @param mapName name of the mapName (can be null)
     * @return unique identifier (guid) of the parent set
     */
    protected String getParentSet(String requestedGUID,
                                  String typeName,
                                  String propertyName,
                                  String mapName)
    {
        final String parentDescription = "Organizing set for valid metadata values";

        String parentQualifiedName = constructValidValueQualifiedName(typeName, propertyName, mapName, null);
        String parentSetGUID = parentValidValueQNameToGUIDMap.get(parentQualifiedName);

        if (parentSetGUID == null)
        {
            String grandParentSetGUID = null;
            String parentDisplayName = parentQualifiedName.substring(26);

            if (mapName != null)
            {
                grandParentSetGUID = getParentSet(null, typeName, propertyName, null);
            }
            else if (propertyName != null)
            {
                grandParentSetGUID = getParentSet(null, typeName, null, null);
            }
            else if (typeName != null)
            {
                grandParentSetGUID = getParentSet(null, null, null, null);
            }

            parentSetGUID =  archiveHelper.addValidValue(requestedGUID,
                                                         grandParentSetGUID,
                                                         grandParentSetGUID,
                                                         OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                         OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                         null,
                                                         OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                         parentQualifiedName,
                                                         Category.VALID_METADATA_VALUES.getName(),
                                                         parentDisplayName,
                                                         parentDescription,
                                                         constructValidValueNamespace(typeName, propertyName, mapName),
                                                         OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                                         null,
                                                         OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                                         null,
                                                         null,
                                                         false,
                                                         null);

            parentValidValueQNameToGUIDMap.put(parentQualifiedName, parentSetGUID);

            return parentSetGUID;
        }
        else
        {
            return parentSetGUID;
        }
    }


    /**
     * Generates and writes out the open metadata archive created in the builder.
     */
    public void writeOpenMetadataArchive()
    {
        writeOpenMetadataArchive(null);
    }


    /**
     * Generates and writes out the open metadata archive created in the builder.
     *
     * @param folderName name of the folder to add the archive into
     */
    public void writeOpenMetadataArchive(String folderName)
    {
        try
        {
            String pathName = archiveFileName;

            if (folderName != null)
            {
                pathName = folderName + "/" + archiveFileName;
            }

            System.out.println("Writing to file: " + pathName);
            super.writeOpenMetadataArchive(pathName, this.getOpenMetadataArchive());
        }
        catch (Exception error)
        {
            System.out.println("error is " + error);
        }

        archiveHelper.saveGUIDs();
        archiveHelper.saveUsedGUIDs();
    }
}
