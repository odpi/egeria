/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveInstanceStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * ContentPackReader reads a committed content pack, for the checks in this module that compare what the
 * packs contain.
 * <br><br>
 * The packs are read back into {@link OpenMetadataArchive} - the platform's own bean - rather than parsed as
 * loose JSON, so a pack the platform could not load fails these checks rather than being quietly
 * misinterpreted by a second reader that understands the format slightly differently.
 */
class ContentPackReader
{
    private static final String CONTENT_PACKS  = "content-packs";
    private static final String ARCHIVE_SUFFIX = ".omarchive";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    private ContentPackReader()
    {
        // no instances
    }


    /**
     * Return the qualified name of every entity in the named content pack.
     *
     * @param packName the pack's file name, with or without the .omarchive suffix
     * @return qualified names
     */
    static Set<String> qualifiedNames(String packName)
    {
        Set<String>                      names         = new LinkedHashSet<>();
        OpenMetadataArchiveInstanceStore instanceStore = read(packName).getArchiveInstanceStore();

        if ((instanceStore == null) || (instanceStore.getEntities() == null))
        {
            return names;
        }

        for (EntityDetail entity : instanceStore.getEntities())
        {
            String qualifiedName = qualifiedNameOf(entity.getProperties());

            if (qualifiedName != null)
            {
                names.add(qualifiedName);
            }
        }

        return names;
    }


    /**
     * Read and deserialize a content pack.
     *
     * @param packName the pack's file name, with or without the .omarchive suffix
     * @return the archive
     */
    static OpenMetadataArchive read(String packName)
    {
        String fileName = packName.endsWith(ARCHIVE_SUFFIX) ? packName : packName + ARCHIVE_SUFFIX;
        Path   archive  = repositoryRoot().resolve(CONTENT_PACKS).resolve(fileName);

        if (! Files.isRegularFile(archive))
        {
            throw new AssertionError("Content pack " + archive + " not found - has it been renamed or moved?");
        }

        try
        {
            return OBJECT_MAPPER.readValue(archive.toFile(), OpenMetadataArchive.class);
        }
        catch (IOException error)
        {
            throw new AssertionError(fileName + " does not deserialize into an OpenMetadataArchive - the" +
                                             " platform could not load it either: " + error.getMessage(), error);
        }
    }


    /**
     * Return the qualified name held in the supplied properties, or null when there is none.
     *
     * @param properties instance properties
     * @return qualified name
     */
    private static String qualifiedNameOf(InstanceProperties properties)
    {
        if ((properties == null) || (properties.getInstanceProperties() == null))
        {
            return null;
        }

        InstancePropertyValue qualifiedName = properties.getInstanceProperties().get("qualifiedName");

        return (qualifiedName == null) ? null : qualifiedName.valueAsString();
    }


    /**
     * Return the root of the repository.  A test's working directory is its own module, so walk up until the
     * file that marks the root of the build is found.
     *
     * @return repository root
     */
    private static Path repositoryRoot()
    {
        Path candidate = Paths.get("").toAbsolutePath();

        while (candidate != null)
        {
            if (Files.exists(candidate.resolve("settings.gradle")))
            {
                return candidate;
            }

            candidate = candidate.getParent();
        }

        throw new AssertionError("Unable to locate the repository root - no settings.gradle found above " +
                                         Paths.get("").toAbsolutePath());
    }
}
