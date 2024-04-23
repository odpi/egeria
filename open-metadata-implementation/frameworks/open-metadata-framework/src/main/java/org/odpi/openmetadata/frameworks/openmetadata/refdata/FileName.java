/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;

/**
 * FileName describes the standard file names recognized by Egeria. These are encoded in the OpenConnectorsArchive.omarchive and are
 * available in the open metadata repository as valid values.
 */
public enum FileName
{
    /**
     * A text file containing a list of name-value property pairs describing the content of a Java Archive (JAR) file.
     */
    JAVA_ARCHIVE_MANIFEST_FILE("manifest.mf",  FileType.JAVA_ARCHIVE_MANIFEST_FILE,         DeployedImplementationType.DATA_FILE),

    /**
     * A XML file containing instructions for building a module using Apache Maven.
     */
    APACHE_MAVEN_BUILD_FILE("pom.xml",   FileType.APACHE_MAVEN_BUILD_FILE,  DeployedImplementationType.PROGRAM_FILE),

    /**
     * A file containing instructions for building a module using gradle.
     */
    GRADLE_BUILD_FILE("build.gradle",   FileType.GRADLE_BUILD_FILE,  DeployedImplementationType.PROGRAM_FILE),

    /**
     * A file containing instructions and properties for building a module using gradle.
     */
    GRADLE_PROPERTIES_FILE("settings.gradle",  FileType.GRADLE_PROPERTIES_FILE,   DeployedImplementationType.PROGRAM_FILE),

    /**
     * A file containing a description of a Java package that is incorporated into Javadoc documentation website.
     */
    JAVA_PACKAGE_DESCRIPTION("package-info.java",  FileType.JAVA_PACKAGE_DESCRIPTION,         DeployedImplementationType.PROGRAM_FILE),

    /**
     * A file containing instructions to build a Docker container.
     */
    DOCKER_CONTAINER_BUILD_SCRIPT("Dockerfile",  FileType.DOCKER_CONTAINER_BUILD_SCRIPT,             DeployedImplementationType.PROGRAM_FILE),

    /**
     * A file containing details of the files to exclude from checking to the git tools.
     */
    GIT_IGNORE_FILE(".gitignore",     FileType.GIT_IGNORE_FILE,      DeployedImplementationType.DATA_FILE),

    /**
     * A file listing the people responsible for maintaining the content of a git repository.
     */
    CODEOWNERS_FILE("CODEOWNERS",     FileType.CODEOWNERS_FILE,      DeployedImplementationType.DATA_FILE),

    /**
     * A file describing the license for using the co-located content in a git repository.
     */
    LICENSE_FILE("LICENSE",       FileType.LICENSE_FILE,          DeployedImplementationType.DATA_FILE),

    /**
     * A file describing the license or copyright for using the co-located content in a git repository.
     */
    NOTICE_FILE("NOTICE",        FileType.NOTICE_FILE,         DeployedImplementationType.DATA_FILE),

    /**
     * A properties file used to configure a Spring application during startup.
     */
    APPLICATION_PROPERTIES_FILE ("application.properties",   FileType.APPLICATION_PROPERTIES_FILE,        DeployedImplementationType.DATA_FILE),

    /**
     * A properties file used to configure the Logback log file management utilities.
     */
    LOGBACK_CONFIG_FILE("logback.xml",   FileType.LOGBACK_CONFIG_FILE,        DeployedImplementationType.DATA_FILE),

    /**
     * A document of words and linked to diagrams in Markdown format that describes the other files in the same directory (folder).
     */
    README_FILE("README.md",    FileType.README_FILE,             DeployedImplementationType.DATA_FILE),

    ;

    private final String                     fileName;
    private final FileType                   fileType;
    private final DeployedImplementationType deployedImplementationType;



    /**
     * Constructor for individual enum value.
     *
     * @param fileType the file type
     * @param deployedImplementationType value for deployedImplementationType
     */
    FileName(String                     fileName,
             FileType                   fileType,
             DeployedImplementationType deployedImplementationType)
    {
        this.fileName = fileName;
        this.fileType = fileType;
        this.deployedImplementationType = deployedImplementationType;
    }


    /**
     * Return the file name.
     *
     * @return string
     */
    public String getFileName()
    {
        return fileName;
    }


    /**
     * Return the file type.
     *
     * @return string
     */
    public FileType getFileType()
    {
        return fileType;
    }



    /**
     * Return preferred value for deployed implementation type.
     *
     * @return string
     */
    public DeployedImplementationType getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "FileName{" + fileName + '}';
    }
}
