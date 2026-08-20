/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.apichecks;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * SourceTree locates the project's own java source files so that the checks in this module can read
 * them.  These checks look at source rather than at compiled classes because what they are looking for -
 * an annotation value, a null test around a parameter - is not reliably visible at runtime.
 */
class SourceTree
{
    private static final String VIEW_SERVICES         = "open-metadata-implementation/view-services";
    private static final String VIEW_GENERIC_SERVICES = "open-metadata-implementation/view-server-generic-services";
    private static final String FRAMEWORK             = "open-metadata-implementation/frameworks/open-metadata-framework/src/main/java/org/odpi/openmetadata/frameworks/openmetadata";


    /**
     * Return the root of the repository.  The working directory of a test is its own module, so walk up
     * until the file that marks the root of the build is found.
     *
     * @return repository root
     */
    static Path repositoryRoot()
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

        throw new IllegalStateException("Unable to locate the repository root - no settings.gradle found above " +
                                                Paths.get("").toAbsolutePath());
    }


    /**
     * Return every java source file under the supplied directories whose name ends with the supplied suffix.
     *
     * @param suffix file name suffix, for example "Resource.java"
     * @param relativeDirectories directories to search, relative to the repository root
     * @return matching files
     */
    static List<Path> sourceFiles(String suffix, String... relativeDirectories)
    {
        List<Path> results = new ArrayList<>();
        Path       root    = repositoryRoot();

        for (String relativeDirectory : relativeDirectories)
        {
            Path directory = root.resolve(relativeDirectory);

            if (! Files.isDirectory(directory))
            {
                continue;
            }

            try (Stream<Path> walk = Files.walk(directory))
            {
                walk.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(suffix))
                    .filter(path -> ! path.toString().contains("/build/"))
                    .forEach(results::add);
            }
            catch (IOException error)
            {
                throw new UncheckedIOException(error);
            }
        }

        return results;
    }


    /**
     * Return the Spring resource classes for every view service.
     *
     * @return resource files
     */
    static List<Path> viewServiceResources()
    {
        return sourceFiles("Resource.java", VIEW_SERVICES, VIEW_GENERIC_SERVICES);
    }


    /**
     * Return the REST services classes for every view service.
     *
     * @return REST services files
     */
    static List<Path> viewServiceRESTServices()
    {
        return sourceFiles("RESTServices.java", VIEW_SERVICES, VIEW_GENERIC_SERVICES);
    }


    /**
     * Return the open metadata handlers.
     *
     * @return handler files
     */
    static List<Path> handlers()
    {
        return sourceFiles("Handler.java", FRAMEWORK + "/handlers");
    }


    /**
     * Return the connector context clients.
     *
     * @return client files
     */
    static List<Path> connectorContextClients()
    {
        return sourceFiles("Client.java", FRAMEWORK + "/connectorcontext");
    }


    /**
     * Read a source file.
     *
     * @param path file to read
     * @return contents
     */
    static String read(Path path)
    {
        try
        {
            return Files.readString(path);
        }
        catch (IOException error)
        {
            throw new UncheckedIOException(error);
        }
    }


    /**
     * Return the contents of every supplied file, joined together.  Used where a check only needs to know
     * whether something appears anywhere in a set of files.
     *
     * @param paths files to read
     * @return combined contents
     */
    static String readAll(List<Path> paths)
    {
        StringBuilder combined = new StringBuilder();

        for (Path path : paths)
        {
            combined.append(read(path)).append('\n');
        }

        return combined.toString();
    }
}
