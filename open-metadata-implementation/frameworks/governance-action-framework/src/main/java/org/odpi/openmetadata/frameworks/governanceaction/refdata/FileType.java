/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.refdata;

/**
 * FileType describes the standard file types supplied with Egeria.  These are encoded in the OpenConnectorsArchive.omarchive and are
 * available in the open metadata repository as valid values.
 */
public enum FileType
{
    /**
     * A text file containing a list of name-value property pairs describing the content of a Java Archive (JAR) file.
     */
    JAVA_ARCHIVE_MANIFEST_FILE("Java Archive Manifest File",
                               null,
                               "PropertiesFile",
                               DeployedImplementationType.DATA_FILE,
                               "A text file containing a list of name-value property pairs describing the content of a Java Archive (JAR) file."),

    /**
     * An XML file containing instructions for building a module using Apache Maven.
     */
    APACHE_MAVEN_BUILD_FILE("Apache Maven Build File",
                            "XML",
                            "BuildInstructionFile",
                            DeployedImplementationType.PROGRAM_FILE,
                            "A XML file containing instructions for building a module using Apache Maven."),

    /**
     * A file containing instructions for building a module using gradle.
     */
    GRADLE_BUILD_FILE("Gradle Build File",
                      null,
                      "BuildInstructionFile",
                      DeployedImplementationType.PROGRAM_FILE,
                      "A file containing instructions for building a module using gradle."),

    /**
     * A file containing instructions and properties for building a module using gradle.
     */
    GRADLE_PROPERTIES_FILE("Gradle Properties File",
                           null,
                           "BuildInstructionFile",
                           DeployedImplementationType.PROGRAM_FILE,
                           "A file containing instructions and properties for building a module using gradle."),

    /**
     * A file containing a description of a Java package that is incorporated into Javadoc documentation website.
     */
    JAVA_PACKAGE_DESCRIPTION("Java Package Description",
                             null,
                             "SourceCodeFile",
                             DeployedImplementationType.PROGRAM_FILE,
                             "A file containing a description of a Java package that is incorporated into Javadoc documentation website."),

    /**
     * A file containing instructions to build a Docker container.
     */
    DOCKER_CONTAINER_BUILD_SCRIPT("Docker Container Build Script",
                                  null,
                                  "ScriptFile",
                                  DeployedImplementationType.PROGRAM_FILE,
                                  "A file containing instructions to build a Docker container."),

    /**
     * A file containing details of the files to exclude from checking to the git tools.
     */
    GIT_IGNORE_FILE("Git Ignore File",
                    null,
                    "PropertiesFile",
                    DeployedImplementationType.DATA_FILE,
                    "A file containing details of the files to exclude from checking to the git tools."),

    /**
     * A file listing the people responsible for maintaining the content of a git repository.
     */
    CODEOWNERS_FILE("Code Owners File",
                    null,
                    "PropertiesFile",
                    DeployedImplementationType.DATA_FILE,
                    "A file listing the people responsible for maintaining the content of a git repository."),

    /**
     * A file describing the license for using the co-located content in a git repository.
     */
    LICENSE_FILE("License File",
                 null,
                 "Document",
                 DeployedImplementationType.DATA_FILE,
                 "A file describing the license for using the co-located content in a git repository."),

    /**
     * A file describing the license or copyright for using the co-located content in a git repository.
     */
    NOTICE_FILE("Notice File",
                null,
                "Document",
                DeployedImplementationType.DATA_FILE,
                "A file describing the license or copyright for using the co-located content in a git repository."),

    /**
     * A properties file used to configure a Spring application during startup.
     */
    APPLICATION_PROPERTIES_FILE("Spring application.properties File",
                                null,
                                "PropertiesFile",
                                DeployedImplementationType.DATA_FILE,
                                "A properties file used to configure a Spring application during startup."),

    /**
     * A properties file used to configure the Logback log file management utilities.
     */
    LOGBACK_CONFIG_FILE("Logback Configuration File",
                        "XML",
                        "PropertiesFile",
                        DeployedImplementationType.DATA_FILE,
                        "A properties file used to configure the Logback log file management utilities."),

    /**
     * A document of words and linked to diagrams in Markdown format that describes the other files in the same directory (folder).
     */
    README_FILE("README File",
                "Markdown",
                "Document",
                DeployedImplementationType.DATA_FILE,
                "A document of words and linked to diagrams in Markdown format that describes the other files in the same directory (folder)."),

    /**
     * A text file with comma-separated values.
     */
    CSV_FILE("CSV File",
             "CSV",
             "CSVFile",
             DeployedImplementationType.DATA_FILE,
             "A text file with comma-separated values."),

    /**
     * A text file containing an Avro object structure.
     */
    AVRO_FILE("Avro File",
              "Avro",
              "AVROFile",
              DeployedImplementationType.DATA_FILE,
              "A text file containing an Avro object structure."),

    /**
     * A text file formatted using the JSON (JavaScript Object Notation) standard.
     */
    JSON_FILE("JSON File",
              "JSON",
              "JSONFile",
              DeployedImplementationType.DATA_FILE,
              "A text file formatted using the JSON (JavaScript Object Notation) standard."),

    /**
     * A JSON (JavaScript Object Notation) file containing Open Metadata Types and Instances.
     */
    OM_ARCHIVE_FILE("Open Metadata Archive File",
                    "JSON",
                    "ArchiveFile",
                    DeployedImplementationType.DATA_FILE,
                    "A JSON (JavaScript Object Notation) file containing Open Metadata Types and Instances."),

    /**
     * A JSON (JavaScript Object Notation) file containing the configuration for an Open Metadata and Governance (OMAG) Server.
     */
    OM_CONFIG_DOC("OMAG Server Configuration Document",
                  "JSON",
                  "PropertiesFile",
                  DeployedImplementationType.DATA_FILE,
                  "A JSON (JavaScript Object Notation) file containing the configuration for an Open Metadata and Governance (OMAG) Server."),

    /**
     * A JSON (JavaScript Object Notation) file the cohort registry for an Open Metadata and Governance (OMAG) Server.
     */
    OM_COHORT_REGISTRY("OMAG Server Cohort Registry",
                       "JSON",
                       "PropertiesFile",
                       DeployedImplementationType.DATA_FILE,
                       "A JSON (JavaScript Object Notation) file the cohort registry for an Open Metadata and Governance (OMAG) Server."),

    /**
     * A JSON (JavaScript Object Notation) file containing an audit log record.
     */
    OM_AUDIT_LOG_RECORD_FILE("Open Metadata Audit Log Record File",
                             "JSON",
                             "LogFile",
                             DeployedImplementationType.DATA_FILE,
                             "A JSON (JavaScript Object Notation) file containing an audit log record."),

    /**
     * A directory (folder) containing audit log records.
     */
    OM_AUDIT_LOG_FOLDER("Open Metadata Audit Log Folder",
                        null,
                        "DataFolder",
                        DeployedImplementationType.DATA_FOLDER,
                        "A directory (folder) containing audit log records."),

    /**
     * A file containing an open lineage event.
     */
    OL_EVENT_FILE("Open Lineage Event File",
                  "JSON",
                  "LogFile",
                  DeployedImplementationType.DATA_FILE,
                  "A file containing an open lineage event."),

    /**
     * A text file formatted using the XML (eXtended Markup Language) standard.
     */
    XML_FILE("XML File",
             "XML",
             "XMLFile",
             DeployedImplementationType.DATA_FILE,
             "A text file formatted using the XML (eXtended Markup Language) standard."),

    /**
     * A data file formatted in columns using the Apache Parquet standard.
     */
    PARQUET_FILE("Parquet File",
                 "Parquet",
                 "ParquetFile",
                 DeployedImplementationType.DATA_FILE,
                 "A data file formatted in columns using the Apache Parquet standard."),

    /**
     * A data file formatted using Optimized Row Columnar (ORC) file format that provides a highly efficient way to store Hive data.
     */
    ORC_FILE("Optimized Row Columnar (ORC) File",
             "ORC",
             "DataFile",
             DeployedImplementationType.DATA_FILE,
             "A data file formatted using Optimized Row Columnar (ORC) file format that provides a highly efficient way to store Hive data."),

    /**
     * A data file formatted in tabbed sheets of tabular data and formulas for Microsoft Excel.
     */
    XLSX_FILE("Microsoft Excel Spreadsheet File",
              "XML",
              "SpreadsheetFile",
              DeployedImplementationType.DATA_FILE,
              "A data file formatted in tabbed sheets of tabular data and formulas for Microsoft Excel."),

    /**
     * A data file formatted in tabbed sheets of tabular data and formulas for Microsoft Excel.
     */
    XLS_FILE("Microsoft Excel Spreadsheet File (Old Format)",
             null,
             "SpreadsheetFile",
             DeployedImplementationType.DATA_FILE,
             "A data file formatted in tabbed sheets of tabular data and formulas for Microsoft Excel."),

    /**
     * A data file formatted in tabbed sheets of tabular data and formulas for Apple Numbers.
     */
    NUMBER_FILE("Apple Numbers Spreadsheet File",
                null,
                "SpreadsheetFile",
                DeployedImplementationType.DATA_FILE,
                "A data file formatted in tabbed sheets of tabular data and formulas for Apple Numbers."),

    /**
     * A document of words and diagrams in deprecated Microsoft Word format.
     */
    OLD_WORD_DOC("Microsoft Word Document (Old Format)",
                 null,
                 "Document",
                 DeployedImplementationType.DATA_FILE,
                 "A document of words and diagrams in deprecated Microsoft Word format."),

    /**
     * A document of words and diagrams in Microsoft Word format.
     */
    WORD_DOC("Microsoft Word Document",
             "XML",
             "Document",
             DeployedImplementationType.DATA_FILE,
             "A document of words and diagrams in Microsoft Word format."),

    /**
     * A document of words and diagrams in OpenDocument format.
     */
    ODT_DOC("OpenDocument File",
            null,
            "Document",
            DeployedImplementationType.DATA_FILE,
            "A document of words and diagrams in OpenDocument format."),

    /**
     * A document of words and diagrams in Apple Pages format.
     */
    PAGES_DOC("Apple Pages Document",
              null,
              "Document",
              DeployedImplementationType.DATA_FILE,
              "A document of words and diagrams in Apple Pages format."),

    /**
     * A document of words and diagrams in XML format for the draw.io tool.
     */
    DRAW_IO_FILE("Draw.io Document",
                 "XML",
                 "Document",
                 DeployedImplementationType.DATA_FILE,
                 "A document of words and diagrams in XML format for the draw.io tool."),

    /**
     * A document of words and linked to diagrams in Markdown format.
     */
    MARKDOWN_FILE("Markdown File",
                  "Markdown",
                  "Document",
                  DeployedImplementationType.DATA_FILE,
                  "A document of words and linked to diagrams in markdown format."),

    /**
     * A freeform text document.
     */
    TEXT_FILE("Text File",
              null,
              "Document",
              DeployedImplementationType.DATA_FILE,
              "A freeform text document."),

    /**
     * A rich text document.
     */
    RICH_TEXT("Rich-text File",
              "RTF",
              "Document",
              DeployedImplementationType.DATA_FILE,
              "A rich text document."),

    /**
     * A container of embedded text and graphics based on a standard developed by Adobe.
     */
    PDF_FILE("Portable Document Format (PDF) File",
             "PDF",
             "Document",
             DeployedImplementationType.DATA_FILE,
             "A container of embedded text and graphics based on a standard developed by Adobe."),

    /**
     * A file containing a presentation for the Microsoft PowerPoint application.
     */
    PPT_FILE("Microsoft PowerPoint Presentation File (Old Format)",
             null,
             "Document",
             DeployedImplementationType.DATA_FILE,
             "A file containing a presentation for the Microsoft PowerPoint application."),

    /**
     * A file containing a presentation for the Microsoft PowerPoint application.
     */
    POT_FILE("Microsoft PowerPoint Presentation Template File (Old Format)",
             null,
             "Document",
             DeployedImplementationType.DATA_FILE,
             "A file containing a presentation for the Microsoft PowerPoint application."),

    /**
     * A file containing a presentation for the Microsoft PowerPoint application.
     */
    PPTX_FILE("Microsoft PowerPoint Presentation File",
              "XML",
              "Document",
              DeployedImplementationType.DATA_FILE,
              "A file containing a presentation for the Microsoft PowerPoint application."),

    /**
     * A file containing a presentation for the Microsoft PowerPoint application.
     */
    POTX_FILE("Microsoft PowerPoint Presentation Template File",
              "XML",
              "Document",
              DeployedImplementationType.DATA_FILE,
              "A file containing a presentation for the Microsoft PowerPoint application."),

    /**
     * A file containing a presentation for the Apple Keynote application.
     */
    KEYNOTE_FILE("Apple Keynote Presentation File",
                 null,
                 "Document",
                 DeployedImplementationType.DATA_FILE,
                 "A file containing a presentation for the Apple Keynote application."),

    /**
     * A file containing encrypted secrets.
     */
    ENCRYPT_KEYS_FILE("Encryption keys File",
                      null,
                      "KeystoreFile",
                      DeployedImplementationType.DATA_FILE,
                      "A file containing encrypted secrets."),

    /**
     * A file containing a digital certificate that uses PKCS#12 (Public Key Cryptography Standard #12) encryption.
     */
    PIEF_FILE("Personal Information Exchange File",
              "PKCS#12",
              "KeystoreFile",
              DeployedImplementationType.DATA_FILE,
              "A file containing a digital certificate that uses PKCS#12 (Public Key Cryptography Standard #12) encryption. It is used as a portable format for transferring personal private keys and other sensitive information."),

    /**
     * A Java-specific key store containing private keys and certificates, but it cannot be used to store secret keys.
     */
    JKS_FILE("Java Keystore File",
             null,
             "KeystoreFile",
             DeployedImplementationType.DATA_FILE,
             "A Java-specific key store containing private keys and certificates, but it cannot be used to store secret keys."),

    /**
     * A Java-specific key store containing private keys and certificates.
     */
    JCEKS_FILE("Java Cryptography Extension (JCE) Keystore File",
               null,
               "KeystoreFile",
               DeployedImplementationType.DATA_FILE,
               "A Java-specific key store containing private keys and certificates, but it should not be used to store secret keys because it is vulnerable to brute-force cyber attacks."),

    /**
     * A document of words and linked to diagrams in Hypertext Markup Language (HTML) format.
     */
    HTML_FILE("HTML Web Document",
              "HTML",
              "Document",
              DeployedImplementationType.DATA_FILE,
              "A document of words and linked to diagrams in Hypertext Markup Language (HTML) format."),

    /**
     * A device independent image file with tag descriptions, some standard, some proprietary.
     */
    TIF_FILE("Tag Image File Format (TIFF)",
             "TIFF",
             "RasterFile",
             DeployedImplementationType.DATA_FILE,
             "A device independent image file with tag descriptions, some standard, some proprietary."),

    /**
     * A TIFF formatted file with additional geo-referencing information embedded.
     */
    GEOTIFF_FILE("GeoTIFF File",
                 "GeoTIFF",
                 "RasterFile",
                 DeployedImplementationType.DATA_FILE,
                 "A TIFF formatted file with additional geo-referencing information embedded."),

    /**
     * A device independent formatted image file with no compression (lossless).
     */
    BMP_FILE("Windows Bitmap File",
             null,
             "RasterFile",
             DeployedImplementationType.DATA_FILE,
             "A device independent formatted image file with no compression (lossless)."),

    /**
     * A device independent formatted image file from the Joint Photographic Experts Group (JPEG) with lossy compression used by most digital cameras.
     */
    JPG_FILE("JPEG File",
             "JPEG",
             "RasterFile",
             DeployedImplementationType.DATA_FILE,
             "A device independent formatted image file from the Joint Photographic Experts Group (JPEG) with lossy compression used by most digital cameras."),

    /**
     * A device independent formatted image file with limited colour depth for graphics with limited colours for simple diagrams, icons, logos and cartoons.
     */
    GIF_FILE("Graphics Interchange Format (GIF) File",
             "GIF",
             "RasterFile",
             DeployedImplementationType.DATA_FILE,
             "A device independent formatted image file with limited colour depth for graphics with limited colours for simple diagrams, icons, logos and cartoons. It uses lossless compression."),

    /**
     * A device independent formatted image file with an 8-bit (256 colors) palette image and 24-bit truecolor (16 million colors) or 48-bit
     * truecolor with and without alpha channel.
     */
    PNG_FILE("Portable Network Graphics (PNG) File",
             "PNG",
             "RasterFile",
             DeployedImplementationType.DATA_FILE,
             "A device independent formatted image file with an 8-bit (256 colors) palette image and 24-bit truecolor (16 million colors) or 48-bit truecolor with and without alpha channel. It uses lossless compression."),

    /**
     * A device independent formatted image container file following the High Efficiency Image Format (HEIF).
     */
    HEIC_FILE("High-Efficiency Image Codec (HEIC) File",
              "HEIC",
              "RasterFile",
              DeployedImplementationType.DATA_FILE,
              "A device independent formatted image container file following the High Efficiency Image Format (HEIF). It adds High Efficiency Video Coding (HEVC)."),

    /**
     * A file containing one or more small images of different sizes and colour depths for use as icons.
     */
    ICO_FILE("Computer Icons File",
             "ICO",
             "RasterFile",
             DeployedImplementationType.DATA_FILE,
             "A file containing one or more small images of different sizes and colour depths for use as icons."),

    /**
     * A descriptive file used to define the appearance of elements in a web browser user interface.
     */
    CSS_FILE("Cascading Style Sheet File",
             "CSS",
             "ScriptFile",
             DeployedImplementationType.PROGRAM_FILE,
             "A descriptive file used to define the appearance of elements in a web browser user interface."),

    /**
     * A descriptive file used to define the appearance of elements in a web browser user interface.  A superset of CSS.
     */
    SCSS_FILE("Extended Cascading Style Sheet File",
              "SCSS",
              "ScriptFile",
              DeployedImplementationType.PROGRAM_FILE,
              "A descriptive file used to define the appearance of elements in a web browser user interface.  A superset of CSS."),

    /**
     * A file containing Python source code.
     */
    PY_FILE("Python Program File",
            null,
            "ScriptFile",
            DeployedImplementationType.PROGRAM_FILE,
            "A file containing Python source code."),

    /**
     * A file containing Javascript source code.
     */
    JS_FILE("Javascript Program File",
            null,
            "ScriptFile",
            DeployedImplementationType.PROGRAM_FILE,
            "A file containing Javascript source code."),

    /**
     * A file containing Typescript source code.
     */
    TYPESCRIPT_FILE("Typescript Program File",
                    null,
                    "ScriptFile",
                    DeployedImplementationType.PROGRAM_FILE,
                    "A file containing Typescript source code."),

    /**
     * A file containing a list of SQL statements used to configure or load data into a relational database.
     */
    SQL_FILE("SQL Program File",
             null,
             "ScriptFile",
             DeployedImplementationType.PROGRAM_FILE,
             "A file containing a list of SQL statements used to configure or load data into a relational database."),

    /**
     * A file containing a list of Microsoft DOS commands.
     */
    DOS_BAT_FILE("Microsoft DOS Batch File",
                 null,
                 "ScriptFile",
                 DeployedImplementationType.PROGRAM_FILE,
                 "A file containing a list of Microsoft DOS commands."),

    /**
     * A file containing a list of Microsoft DOS commands.
     */
    DOS_COM_FILE("Microsoft DOS Command File",
                 null,
                 "ScriptFile",
                 DeployedImplementationType.PROGRAM_FILE,
                 "A file containing a list of Microsoft DOS commands."),

    /**
     * A file containing a list of Microsoft Windows commands.
     */
    MS_WSF_FILE("Microsoft Windows Script File",
                null,
                "ScriptFile",
                DeployedImplementationType.PROGRAM_FILE,
                "A file containing a list of Microsoft Windows commands."),

    /**
     * A file containing a list of Unix commands that run in the Bash shell.
     */
    BASH_SCRIPT_FILE("Bash Shell Script File",
                     null,
                     "ScriptFile",
                     DeployedImplementationType.PROGRAM_FILE,
                     "A file containing a list of Unix commands that run in the Bash shell."),

    /**
     * A file containing a list of Unix commands that run in the Korn shell.
     */
    KORN_SCRIPT_FILE("Korn Shell Script File",
                     null,
                     "ScriptFile",
                     DeployedImplementationType.PROGRAM_FILE,
                     "A file containing a list of Unix commands that run in the Korn shell."),

    /**
     * A file containing a list of name-value properties.
     */
    PROPERTIES_FILE("Properties File",
                    null,
                    "PropertiesFile",
                    DeployedImplementationType.DATA_FILE,
                    "A file containing a list of name-value properties."),

    /**
     * A file containing a list of name-value properties for configuration.
     */
    CONFIGURATION_FILE("Configuration File",
                       null,
                       "PropertiesFile",
                       DeployedImplementationType.DATA_FILE,
                       "A file containing a list of name-value properties for configuration."),

    /**
     * A file where each row represents a log record.
     */
    LOG_FILE("Log File",
             null,
             "LogFile",
             DeployedImplementationType.LOG_FILE,
             "A file where each row represents a log record."),

    /**
     * A file where each row represents a log message.
     */
    LOG_MESSAGE_FILE("Log Message File",
                     null,
                     "LogFile",
                     DeployedImplementationType.LOG_FILE,
                     "A file where each row represents a log message."),

    /**
     * A file where each row represents a string output on the standard error (stderr) destination of a running process.
     */
    STDERR_FILE("Stderr Log File",
                null,
                "LogFile",
                DeployedImplementationType.LOG_FILE,
                "A file where each row represents a string output on the standard error (stderr) destination of a running process."),

    /**
     * A file where each row represents a string output on the standard out (stdout) destination of a running process.
     */
    STDOUT_FILE("Stdout Log File",
                null,
                "LogFile",
                DeployedImplementationType.LOG_FILE,
                "A file where each row represents a string output on the standard out (stdout) destination of a running process."),

    /**
     * A file containing Java programming language source code and comments.
     */
    JAVA_FILE("Java Program Source File",
              null,
              "SourceCodeFile",
              DeployedImplementationType.PROGRAM_FILE,
              "A file containing Java programming language source code and comments."),

    /**
     * A file containing complied Java code.
     */
    CLASS_FILE("Java Program Object File",
               null,
               "ExecutableFile",
               DeployedImplementationType.PROGRAM_FILE,
               "A file containing complied Java code."),

    /**
     * A file containing executable application code for the Apple macOS operating system.
     */
    APP_FILE("MacOS Application Bundle File",
             null,
             "ExecutableFile",
             DeployedImplementationType.PROGRAM_FILE,
             "A file containing executable application code for the Apple MacOS operating system."),

    /**
     * A file containing executable application code for the Microsoft Windows operating system.
     */
    EXE_FILE("Windows Executable File",
             null,
             "ExecutableFile",
             DeployedImplementationType.PROGRAM_FILE,
             "A file containing executable application code for the Microsoft Windows operating system."),

    /**
     * A file containing an organized collection of executable Java code and related resources.
     */
    JAR_FILE("Java Archive File",
             null,
             "ArchiveFile",
             DeployedImplementationType.PROGRAM_FILE,
             "A file containing an organized collection of executable Java code and related resources."),

    /**
     * A file containing an organized collection of files.  Often used for install.
     */
    TAR_FILE("Tarball Archive File",
             null,
             "ArchiveFile",
             DeployedImplementationType.ARCHIVE_FILE,
             "A file containing an organized collection of files.  Often used for install."),

    /**
     * A file containing an organized collection of files that has been compressed to save storage.
     */
    GZ_FILE("Compressed Archive File",
            null,
            "ArchiveFile",
            DeployedImplementationType.ARCHIVE_FILE,
            "A file containing an organized collection of files that has been compressed to save storage."),

    /**
     * A file containing an organized collection of files that have been compressed to save storage.
     */
    ZIP_FILE("Zipped Archive File",
             null,
             "ArchiveFile",
             DeployedImplementationType.ARCHIVE_FILE,
             "A file containing an organized collection of files that have been compressed to save storage."),

    /**
     * A file containing an organized collection of files that represent a web application.
     */
    WAR_FILE("Web Archive File",
             null,
             "ArchiveFile",
             DeployedImplementationType.PROGRAM_FILE,
             "A file containing an organized collection of files that represent a web application."),


    /**
     * A file containing an organized collection of files that represent a JEE application.
     */
    EAR_FILE("Enterprise Archive File",
             null,
             "ArchiveFile",
             DeployedImplementationType.PROGRAM_FILE,
             "A file containing an organized collection of files that represent a JEE application."),

    /**
     * A file containing an organized collection of files that represent an application for Apple macOS.
     */
    DMG_FILE("Apple Disk Image File",
             null,
             "ArchiveFile",
             DeployedImplementationType.PROGRAM_FILE,
             "A file containing an organized collection of files that represent an application for Apple MacOS."),

    /**
     * A file containing an organized collection of files that represent an application for Microsoft Windows.
     */
    ISO_FILE("Windows Disk Image File",
             null,
             "ArchiveFile",
             DeployedImplementationType.PROGRAM_FILE,
             "A file containing an organized collection of files that represent an application for Microsoft Windows."),

    /**
     * A file containing a set of properties in the YAML format.
     */
    YAML_FILE("YAML File",
              "YAML",
              "YAMLFile",
              DeployedImplementationType.DATA_FILE,
              "A file containing a set of properties in the YAML format."),

    /**
     * A file containing binary data.
     */
    BIN_FILE("Binary Format File",
             "Binary",
             "DataFile",
             DeployedImplementationType.DATA_FILE,
             "A file containing binary data."),

    /**
     * A file containing data, typically from an application.  The format is known by the application.
     */
    DAT_FILE("Application Data File",
             null,
             "DataFile",
             DeployedImplementationType.DATA_FILE,
             "A file containing data, typically from an application.  The format is known by the application."),

    /**
     * A file containing report data, typically from an application.  The format is known by the application.
     */
    RPT_FILE("Application Report File",
             null,
             "DataFile",
             DeployedImplementationType.DATA_FILE,
             "A file containing report data, typically from an application.  The format is known by the application."),

    /**
     * A file containing data from Microsoft Project.
     */
    MPP_FILE("Microsoft Project File",
             null,
             "DataFile",
             DeployedImplementationType.DATA_FILE,
             "A file containing data from Microsoft Project."),

    /**
     * A text file format used in bioinformatics for storing gene sequence variations.
     */
    VCF_FILE("Variant Call Format (VCF) File",
             "VCF",
             "DataFile",
             DeployedImplementationType.DATA_FILE,
             "A text file format used in bioinformatics for storing gene sequence variations."),

    /**
     * A file containing audio samples and music.
     */
    AIFF_FILE("Audio Interchange File Format (AIFF) File",
              "AIFF",
              "AudioFile",
              DeployedImplementationType.DATA_FILE,
              "A file containing audio samples and music.  Often used for storing uncompressed, high quality audio on CDs and personal computers."),

    /**
     * A file containing lossless compression audio coding format.
     */
    FLAC_FILE("Free Lossless Audio Codec (FLAC) File",
              "FLAC",
              "AudioFile",
              DeployedImplementationType.DATA_FILE,
              "A file containing lossless compression audio coding format."),

    /**
     * A file containing lossless compression audio coding format.
     */
    WAV_FILE("Waveform Audio File",
             "WAV",
             "AudioFile",
             DeployedImplementationType.DATA_FILE,
             "A file containing lossless compression audio coding format."),

    /**
     * A file containing audio data with Moving Pictures Experts Group (MPEG) compression.
     */
    MP3_FILE("MP3 Audio File",
             "MP3",
             "AudioFile",
             DeployedImplementationType.DATA_FILE,
             "A file containing audio data with Moving Pictures Experts Group (MPEG) compression."),

    /**
     * A file containing audio data with Microsoft's specific compression.
     */
    WMA_FILE("Window Media Audio File",
             "WMA",
             "AudioFile",
             DeployedImplementationType.DATA_FILE,
             "A file containing audio data with Microsoft's specific compression."),

    /**
     * A file containing video data with Moving Pictures Experts Group (MPEG) compression.
     */
    MP4_FILE("MP4 Video File",
             "MP4 (H.264)",
             "VideoFile",
             DeployedImplementationType.DATA_FILE,
             "A file containing video data with Moving Pictures Experts Group (MPEG) compression."),

    /**
     * A file containing MPEG-2-compressed video data.
     */
    VIDEO_TS_FILE("Video Transport System File",
                  null,
                  "VideoFile",
                  DeployedImplementationType.DATA_FILE,
                  "A file containing MPEG-2-compressed video data."),

    /**
     * A file containing video data in small file sizes for mobile phones and other devices.
     */
    MOBILE_VIDEO_FILE("Mobile Video File",
                      null,
                      "VideoFile",
                      DeployedImplementationType.DATA_FILE,
                      "A file containing video data in small file sizes for mobile phones and other devices."),

    /**
     * A file containing high quality video data.
     */
    MOV_FILE("Apple MOV Video File",
             null,
             "VideoFile",
             DeployedImplementationType.DATA_FILE,
             "A file containing high quality video data."),

    /**
     * A file containing video data for Microsoft Windows.
     */
    AVI_FILE("Windows AVI Video File",
             null,
             "VideoFile",
             DeployedImplementationType.DATA_FILE,
             "A file containing video data for Microsoft Windows."),

    /**
     * A file containing video data for Apple systems, often includes Apple’s FairPlay DRM copyright protection.
     */
    M4V_FILE("Apple M4V Video File",
             "M4V",
             "VideoFile",
             DeployedImplementationType.DATA_FILE,
             "A file containing video data for Apple systems, often includes Apple’s FairPlay DRM copyright protection."),

    /**
     * A file containing video data for Microsoft Windows Media Player.
     */
    WMV_FILE("Windows Media Video File",
             "WMV",
             "VideoFile",
             DeployedImplementationType.DATA_FILE,
             "A file containing video data for Microsoft Windows Media Player."),

    /**
     * A file containing vector image data for Adobe Illustrator.
     */
    AI_FILE("Adobe Illustrator Image File",
            null,
            "VectorFile",
            DeployedImplementationType.DATA_FILE,
            "A file containing vector image data for Adobe Illustrator."),

    /**
     * A file containing vector image data.
     */
    SVG_FILE("Scalar Vector Graphics File",
             "SVG",
             "VectorFile",
             DeployedImplementationType.DATA_FILE,
             "A file containing vector image data."),

    /**
     * A file containing high quality vector image data. Used in the printing industry.
     */
    EPS_FILE("Encapsulated Postscript File",
             "EPS",
             "VectorFile",
             DeployedImplementationType.DATA_FILE,
             "A file containing high quality vector image data. Used in the printing industry."),

    /**
     * A file containing an image formatted in the PostScript language.
     */
    PS_FILE("Postscript File",
            "PS",
            "VectorFile",
            DeployedImplementationType.DATA_FILE,
            "A file containing an image formatted in the PostScript language."),

    /**
     * A file containing a 3 dimensional object.
     */
    OBJ_FILE("Wavefront 3D Object File",
             null,
             "3DImageFile",
             DeployedImplementationType.DATA_FILE,
             "A file containing a 3 dimensional object."),

    /**
     * A file that describes how a program should apply textures to that object.
     */
    MTL_FILE("OBJ Material Template Library File",
             null,
             "3DImageFile",
             DeployedImplementationType.DATA_FILE,
             "A file that describes how a program should apply textures to that object. MTL files contain the names of the texture bitmap files to apply to an object as well as the 3D coordinates at which to apply them."),


    ;
    private final String                     fileTypeName;
    private final String                     encoding;
    private final DeployedImplementationType deployedImplementationType;
    private final String                     assetSubTypeName;
    private final String                     description;


    /**
     * Constructor for individual enum value.
     *
     * @param fileTypeName the name of the file type
     * @param encoding the optional name of the encoding method used in the file
     * @param deployedImplementationType value for deployedImplementationType
     * @param assetSubTypeName the open metadata type where this value is used
     * @param description description of the type
     */
    FileType(String                     fileTypeName,
             String                     encoding,
             String                     assetSubTypeName,
             DeployedImplementationType deployedImplementationType,
             String                     description)
    {
        this.deployedImplementationType = deployedImplementationType;
        this.assetSubTypeName = assetSubTypeName;
        this.description = description;
        this.fileTypeName = fileTypeName;
        this.encoding = encoding;
    }


    /**
     * Return the name of the file type.
     *
     * @return string
     */
    public String getFileTypeName()
    {
        return fileTypeName;
    }


    /**
     * Return the optional name of the encoding method used in the file.
     *
     * @return string
     */
    public String getEncoding()
    {
        return encoding;
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
     * Return the type name that this deployed implementation type is associated with.
     *
     * @return string
     */
    public String getAssetSubTypeName()
    {
        return assetSubTypeName;
    }


    /**
     * Return the description for this value.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }



    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "FileType{" + fileTypeName + '}';
    }
}
