/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.messages;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * MessageSetScanner walks the Egeria source tree looking for the enums that implement one of the message set
 * interfaces.  Scanning the source - rather than the class path - means that the documentation covers every
 * message set in the repository, including those defined in modules that are not part of the OMAG Server
 * Platform assembly, and it keeps this utility free of a dependency on all of them.
 */
public class MessageSetScanner
{
    private static final String JAVA_SOURCE_ROOT = "src/main/java";
    private static final String JAVA_SUFFIX      = ".java";

    /*
     * Matches one constant of the audit log framework's AuditLogRecordSeverityLevel enum - its name, the
     * display name that appears in an audit log record, and the description of when the severity is used.
     */
    private static final Pattern SEVERITY_CONSTANT_PATTERN =
            Pattern.compile("([A-Z][A-Z0-9_]*)\\s*\\(\\s*\\d+\\s*,\\s*\"([^\"]*)\"\\s*,\\s*(.*?)\\)",
                            Pattern.DOTALL);

    private static final String SEVERITY_SOURCE_PATH =
            "open-metadata-implementation/frameworks/audit-log-framework/src/main/java/org/odpi/openmetadata/" +
                    "frameworks/auditlog/AuditLogRecordSeverityLevel.java";

    private final Path repositoryRoot;


    /**
     * Constructor.
     *
     * @param repositoryRoot root directory of the Egeria repository
     */
    public MessageSetScanner(Path repositoryRoot)
    {
        this.repositoryRoot = repositoryRoot;
    }


    /**
     * Find and parse every message set in the repository.
     *
     * @return the message sets, sorted by name
     * @throws IOException the source tree could not be read
     * @throws MessageSetParsingException a message set could not be understood - the caller should fail the
     *                                    build rather than publish incomplete documentation
     */
    public List<MessageSetDescription> scanForMessageSets() throws IOException, MessageSetParsingException
    {
        List<MessageSetDescription> messageSets = new ArrayList<>();
        List<Path>                  sourceFiles = new ArrayList<>();
        MessageSetParser            parser      = new MessageSetParser();

        try (Stream<Path> candidates = Files.walk(repositoryRoot))
        {
            candidates.filter(this::isCandidateSourceFile).forEach(sourceFiles::add);
        }
        catch (UncheckedIOException error)
        {
            throw new IOException("Unable to walk the source tree under " + repositoryRoot, error);
        }

        for (Path sourceFile : sourceFiles)
        {
            String                sourcePath = getRelativePath(sourceFile);
            String                sourceText = Files.readString(sourceFile, StandardCharsets.UTF_8);
            MessageSetDescription messageSet = parser.parseMessageSet(sourcePath, sourceText);

            if (messageSet != null)
            {
                messageSets.add(messageSet);
            }
        }

        messageSets.sort(Comparator.comparing(MessageSetDescription::getEnumName)
                                   .thenComparing(MessageSetDescription::getPackageName));

        return messageSets;
    }


    /**
     * Read the audit log severities out of the audit log framework so that the documentation explains each
     * severity in the framework's own words.
     *
     * @return map of severity name to its description, in the order that they are declared
     * @throws IOException the audit log framework's source could not be read
     */
    public Map<String, AuditLogSeverityDescription> scanForSeverities() throws IOException
    {
        Map<String, AuditLogSeverityDescription> severities = new LinkedHashMap<>();
        Path                                     sourceFile = repositoryRoot.resolve(SEVERITY_SOURCE_PATH);

        if (! Files.exists(sourceFile))
        {
            return severities;
        }

        String              sourceText   = Files.readString(sourceFile, StandardCharsets.UTF_8);
        JavaSourceTokenizer tokenizer    = new JavaSourceTokenizer(sourceText);
        String              maskedText   = tokenizer.getMaskedText();
        int                 bodyStart    = maskedText.indexOf('{');
        int                 constantsEnd = (bodyStart < 0) ? -1 : maskedText.indexOf(';', bodyStart);

        if (constantsEnd < 0)
        {
            return severities;
        }

        Matcher matcher = SEVERITY_CONSTANT_PATTERN.matcher(sourceText.substring(bodyStart + 1, constantsEnd));

        while (matcher.find())
        {
            String description = matcher.group(3)
                                        .replaceAll("\"\\s*\\+\\s*\"", "")
                                        .replace("\"", "")
                                        .replaceAll("\\s+", " ")
                                        .strip();

            severities.put(matcher.group(1),
                           new AuditLogSeverityDescription(matcher.group(1), matcher.group(2), description));
        }

        return severities;
    }


    /**
     * Is this file worth opening?  Only the main source of a module is scanned - the message sets in a test
     * tree, or in a build directory, are not part of Egeria's published behaviour.
     *
     * @param candidate a file found in the source tree
     * @return true if the file should be parsed
     */
    private boolean isCandidateSourceFile(Path candidate)
    {
        if ((! Files.isRegularFile(candidate)) || (! candidate.toString().endsWith(JAVA_SUFFIX)))
        {
            return false;
        }

        String relativePath = getRelativePath(candidate);

        return (relativePath.contains(JAVA_SOURCE_ROOT)) && (! relativePath.contains("/build/"));
    }


    /**
     * Return the path of a file relative to the repository root, using "/" separators whatever the platform.
     *
     * @param file file in the source tree
     * @return relative path
     */
    private String getRelativePath(Path file)
    {
        return repositoryRoot.relativize(file).toString().replace('\\', '/');
    }
}
