/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.duplicates;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * ContentPackDuplicateReport compares two versions of an open metadata archive and reports the elements they
 * disagree about - the ones that keep their qualified name but are given a new unique identifier.
 * <p>
 * Each of those elements becomes a duplicate pair in any repository that has loaded both versions: two
 * elements, the same type, the same qualified name, different identifiers, and nothing to say they are the
 * same thing.  That is the situation
 * <a href="https://egeria-project.org/features/duplicate-management/overview/">duplicate management</a>
 * exists to resolve, and it arises without anybody creating it deliberately - between Egeria 6.0 and 6.1,
 * 874 elements of the core content pack changed identity this way.
 * <p>
 * Run it before shipping a content pack to see how much work an upgrade will create for the people who
 * already have the previous version loaded:
 * <pre>
 *     ./gradlew :open-metadata-resources:open-metadata-dev-utilities:content-pack-duplicate-report:run \
 *               --args="/path/to/previous/CoreContentPack.omarchive /path/to/new/CoreContentPack.omarchive"
 * </pre>
 * Archives from a released branch can be extracted without checking out the whole branch:
 * <pre>
 *     git fetch --depth 1 --filter=blob:none upstream egeria-release-6.0
 *     git show upstream/egeria-release-6.0:content-packs/CoreContentPack.omarchive &gt; /tmp/CoreContentPack-6.0.omarchive
 * </pre>
 */
public class ContentPackDuplicateReport
{
    /**
     * How many changed elements are listed in full before the report switches to counting them.
     */
    private static final int MAXIMUM_ELEMENTS_LISTED = 25;


    /**
     * Compare two archives and write the report to the console.
     *
     * @param args the earlier archive file, then the later one
     */
    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.out.println("Usage: ContentPackDuplicateReport <earlier archive file> <later archive file>");
            System.out.println();
            System.out.println("Reports the elements that keep their qualified name but change their unique identifier");
            System.out.println("between the two archives.  Each one becomes a duplicate pair in a repository that loads both.");
            System.exit(1);
        }

        try
        {
            new ContentPackDuplicateReport().runReport(new File(args[0]), new File(args[1]));
        }
        catch (Exception error)
        {
            System.err.println("The report could not be produced: " + error.getClass().getSimpleName() + " - " + error.getMessage());
            System.exit(1);
        }
    }


    /**
     * Produce the report.
     *
     * @param earlierArchive the archive shipped first
     * @param laterArchive the archive that replaces it
     * @throws Exception either archive could not be read
     */
    private void runReport(File earlierArchive,
                           File laterArchive) throws Exception
    {
        Map<String, ArchivedElement> earlierElements = this.readElements(earlierArchive);
        Map<String, ArchivedElement> laterElements   = this.readElements(laterArchive);

        System.out.println("Open metadata archive duplicate report");
        System.out.println("======================================");
        System.out.println();
        System.out.println("Earlier archive : " + earlierArchive.getName() + " (" + earlierElements.size() + " named elements)");
        System.out.println("Later archive   : " + laterArchive.getName() + " (" + laterElements.size() + " named elements)");
        System.out.println();

        /*
         * An element is counted when both archives describe something of the same type with the same
         * qualified name, but give it a different unique identifier.  Elements that only appear in one of
         * the archives are new or withdrawn content, not duplicates.
         */
        Map<String, List<ArchivedElement>> changedElementsByType = new TreeMap<>();

        int changedElementCount = 0;

        for (Map.Entry<String, ArchivedElement> earlierEntry : earlierElements.entrySet())
        {
            ArchivedElement laterElement = laterElements.get(earlierEntry.getKey());

            if ((laterElement != null) && (! laterElement.guid.equals(earlierEntry.getValue().guid)))
            {
                changedElementsByType.computeIfAbsent(laterElement.typeName, typeName -> new ArrayList<>())
                                     .add(new ArchivedElement(laterElement.typeName,
                                                               laterElement.qualifiedName,
                                                               earlierEntry.getValue().guid + " -> " + laterElement.guid));
                changedElementCount++;
            }
        }

        if (changedElementCount == 0)
        {
            System.out.println("The two archives agree about the identifier of every element they share.");
            System.out.println("Loading both creates no duplicates.");
            return;
        }

        System.out.println(changedElementCount + " element(s) keep their qualified name but change their unique identifier.");
        System.out.println("Each one becomes a duplicate pair in a repository that loads both archives.");
        System.out.println();
        System.out.println("By type:");
        System.out.println();

        for (Map.Entry<String, List<ArchivedElement>> typeEntry : changedElementsByType.entrySet())
        {
            System.out.println("  " + typeEntry.getKey() + " : " + typeEntry.getValue().size());
        }

        System.out.println();
        System.out.println("Details:");
        System.out.println();

        int listed = 0;

        for (Map.Entry<String, List<ArchivedElement>> typeEntry : changedElementsByType.entrySet())
        {
            for (ArchivedElement changedElement : typeEntry.getValue())
            {
                if (listed >= MAXIMUM_ELEMENTS_LISTED)
                {
                    System.out.println("  ... and " + (changedElementCount - listed) + " more");
                    return;
                }

                System.out.println("  " + changedElement.typeName + " : " + changedElement.qualifiedName);
                System.out.println("      " + changedElement.guid);

                listed++;
            }
        }
    }


    /**
     * Read an archive and return every entity in it that has a qualified name, keyed by type name and
     * qualified name together - two elements are only the same thing if both agree.
     *
     * @param archiveFile archive to read
     * @return map of "typeName::qualifiedName" to the element
     * @throws Exception the archive could not be read
     */
    private Map<String, ArchivedElement> readElements(File archiveFile) throws Exception
    {
        Map<String, ArchivedElement> elements = new HashMap<>();

        if (! archiveFile.isFile())
        {
            throw new IllegalArgumentException(archiveFile.getAbsolutePath() + " is not a file");
        }

        JsonNode archive       = new ObjectMapper().readTree(archiveFile);
        JsonNode instanceStore = archive.get("archiveInstanceStore");

        if ((instanceStore == null) || (instanceStore.get("entities") == null))
        {
            return elements;
        }

        for (JsonNode entity : instanceStore.get("entities"))
        {
            JsonNode guid       = entity.get("guid");
            JsonNode type       = entity.get("type");
            JsonNode properties = entity.get("properties");

            if ((guid == null) || (type == null) || (properties == null))
            {
                continue;
            }

            JsonNode instanceProperties = properties.get("instanceProperties");

            if (instanceProperties == null)
            {
                continue;
            }

            JsonNode qualifiedName = instanceProperties.get("qualifiedName");

            if (qualifiedName == null)
            {
                continue;
            }

            JsonNode qualifiedNameValue = qualifiedName.get("primitiveValue");

            if (qualifiedNameValue == null)
            {
                continue;
            }

            JsonNode typeName = type.get("typeDefName");

            String elementTypeName = (typeName == null) ? "<unknown type>" : typeName.asText();

            elements.put(elementTypeName + "::" + qualifiedNameValue.asText(),
                          new ArchivedElement(elementTypeName, qualifiedNameValue.asText(), guid.asText()));
        }

        return elements;
    }


    /**
     * One element read from an archive.
     */
    private static class ArchivedElement
    {
        private final String typeName;
        private final String qualifiedName;
        private final String guid;


        /**
         * Constructor.
         *
         * @param typeName type of the element
         * @param qualifiedName qualified name of the element
         * @param guid unique identifier of the element - or, in the report, the change to it
         */
        ArchivedElement(String typeName,
                        String qualifiedName,
                        String guid)
        {
            this.typeName      = typeName;
            this.qualifiedName = qualifiedName;
            this.guid          = guid;
        }
    }
}
