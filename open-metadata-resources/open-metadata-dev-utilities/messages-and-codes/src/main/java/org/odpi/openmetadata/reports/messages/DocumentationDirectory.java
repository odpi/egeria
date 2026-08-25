/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.messages;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * DocumentationDirectory manages the contents of the generated documentation directory.
 * <br><br>
 * The generated pages are checked in so that they can be read and searched on GitHub, which means this
 * utility runs on every build against a directory that is already populated.  So that a build that changes
 * nothing leaves the working tree clean, a page is only written when its content has actually changed.  Pages
 * left over from a message set that has since been renamed or removed are deleted.
 */
class DocumentationDirectory
{
    private final Path        rootDirectory;
    private final Set<String> writtenFiles = new HashSet<>();

    private int createdCount = 0;
    private int updatedCount = 0;
    private int deletedCount = 0;


    /**
     * Constructor.
     *
     * @param rootDirectory root of the documentation directory
     */
    DocumentationDirectory(Path rootDirectory)
    {
        this.rootDirectory = rootDirectory;
    }


    /**
     * Save a page.  The file is only written if its content has changed.
     *
     * @param relativePath location of the page within the documentation directory
     * @param content markdown content of the page
     * @throws IOException the page could not be written
     */
    void savePage(String relativePath, String content) throws IOException
    {
        Path pageFile = rootDirectory.resolve(relativePath);

        writtenFiles.add(relativePath);

        if (Files.exists(pageFile))
        {
            if (Files.readString(pageFile, StandardCharsets.UTF_8).equals(content))
            {
                return;
            }

            updatedCount = updatedCount + 1;
        }
        else
        {
            Files.createDirectories(pageFile.getParent());
            createdCount = createdCount + 1;
        }

        Files.writeString(pageFile, content, StandardCharsets.UTF_8);
    }


    /**
     * Remove any markdown page that this run did not write, along with any directory that is left empty as a
     * result.  This keeps the documentation in step with the message sets that actually exist.
     *
     * @throws IOException the directory could not be tidied
     */
    void removeStalePages() throws IOException
    {
        if (! Files.isDirectory(rootDirectory))
        {
            return;
        }

        List<Path> stalePages = new ArrayList<>();

        try (Stream<Path> existingFiles = Files.walk(rootDirectory))
        {
            existingFiles.filter(Files::isRegularFile)
                         .filter(file -> file.toString().endsWith(".md"))
                         .filter(file -> ! writtenFiles.contains(getRelativePath(file)))
                         .forEach(stalePages::add);
        }

        for (Path stalePage : stalePages)
        {
            Files.delete(stalePage);
            deletedCount = deletedCount + 1;
        }

        removeEmptyDirectories();
    }


    /**
     * Remove the directories that no longer hold any documentation.
     *
     * @throws IOException the directory could not be tidied
     */
    private void removeEmptyDirectories() throws IOException
    {
        List<Path> directories = new ArrayList<>();

        try (Stream<Path> existingFiles = Files.walk(rootDirectory))
        {
            existingFiles.filter(Files::isDirectory)
                         .filter(directory -> ! directory.equals(rootDirectory))
                         .forEach(directories::add);
        }

        /*
         * Deepest first, so that a directory holding only empty directories is emptied before it is tested.
         */
        directories.sort(Comparator.comparingInt(Path::getNameCount).reversed());

        for (Path directory : directories)
        {
            try (Stream<Path> contents = Files.list(directory))
            {
                if (contents.findAny().isEmpty())
                {
                    Files.delete(directory);
                }
            }
        }
    }


    /**
     * Return a summary of the changes made to the documentation directory.
     *
     * @return one line description
     */
    String getChangeSummary()
    {
        if ((createdCount == 0) && (updatedCount == 0) && (deletedCount == 0))
        {
            return "the documentation was already up-to-date";
        }

        return createdCount + " pages created, " + updatedCount + " pages updated, " + deletedCount +
                       " pages deleted";
    }


    /**
     * Return the path of a file relative to the documentation directory, using "/" separators whatever the
     * platform.
     *
     * @param file file in the documentation directory
     * @return relative path
     */
    private String getRelativePath(Path file)
    {
        return rootDirectory.relativize(file).toString().replace('\\', '/');
    }
}
