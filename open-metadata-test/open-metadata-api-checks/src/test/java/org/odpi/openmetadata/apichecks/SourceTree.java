/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.apichecks;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

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
    private static final String IMPLEMENTATION        = "open-metadata-implementation";

    /**
     * The directory Gradle writes into, which holds no source and is being rewritten while the build runs.
     */
    private static final String BUILD_DIRECTORY        = "build";


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
     * <br><br>
     * A module's {@code build} directory is <b>pruned</b> rather than filtered out of the results.  Filtering
     * afterwards still walks into it, and during a full build those directories are being written and
     * deleted by other tasks at the same time - a javadoc task recreating {@code build/docs/javadoc} while
     * the walk is inside it ends the walk with a {@code NoSuchFileException}.  That made these checks pass
     * when run on their own and fail in a parallel build, which is the worst way for a test to behave.
     * <br><br>
     * A file that disappears mid-walk is skipped for the same reason: it cannot be a source file this check
     * cares about, because those do not come and go while the build runs.
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

            try
            {
                Files.walkFileTree(directory, new SimpleFileVisitor<>()
                {
                    @Override
                    public FileVisitResult preVisitDirectory(Path candidate, BasicFileAttributes attributes)
                    {
                        return BUILD_DIRECTORY.equals(candidate.getFileName().toString())
                                       ? FileVisitResult.SKIP_SUBTREE
                                       : FileVisitResult.CONTINUE;
                    }


                    @Override
                    public FileVisitResult visitFile(Path candidate, BasicFileAttributes attributes)
                    {
                        if (attributes.isRegularFile() && candidate.getFileName().toString().endsWith(suffix))
                        {
                            results.add(candidate);
                        }

                        return FileVisitResult.CONTINUE;
                    }


                    @Override
                    public FileVisitResult visitFileFailed(Path candidate, IOException error)
                    {
                        return FileVisitResult.CONTINUE;
                    }
                });
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
     * Return every class in the implementation - the main source of every module under
     * open-metadata-implementation.  Test sources are left out: a check here is about the shipped code,
     * and a test is allowed to do things a bean is not.
     *
     * @return java files
     */
    static List<Path> implementationClasses()
    {
        List<Path> results = new ArrayList<>();

        for (Path path : sourceFiles(".java", IMPLEMENTATION))
        {
            if (! path.toString().contains("/src/test/java/"))
            {
                results.add(path);
            }
        }

        return results;
    }


    /**
     * Return the open metadata framework's bean packages - the properties beans, the metadata element
     * beans that carry them, and the search beans.  These are the beans that travel between the platform
     * and its callers, and the ones that travel as their base type rather than their own.
     *
     * @return java files
     */
    static List<Path> frameworkBeans()
    {
        return sourceFiles(".java",
                           FRAMEWORK + "/properties",
                           FRAMEWORK + "/metadataelements",
                           FRAMEWORK + "/search");
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
