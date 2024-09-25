/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;

import java.util.Arrays;
import java.util.List;

/**
 * FileExtension describes the standard file extensions recognized by Egeria. These are encoded in the CoreContentPack.omarchive and are
 * available in the open metadata repository as valid values.
 */
public enum FileExtension
{
    /**
     * A text file with comma-separated values.
     */
    CSV_FILE("csv",     new FileType[]{FileType.CSV_FILE}),

    /**
     * A text file containing an Avro object structure.
     */
    AVRO_FILE("avro",      new FileType[]{FileType.AVRO_FILE}),

    /**
     * A text file formatted using the JSON (JavaScript Object Notation) standard.
     */
    JSN_FILE("jsn",      new FileType[]{FileType.JSON_FILE}),

    /**
     * A text file formatted using the JSON (JavaScript Object Notation) standard.
     */
    JSON_FILE("json",      new FileType[]{FileType.JSON_FILE}),

    /**
     * A JSON (JavaScript Object Notation) file containing Open Metadata Types and Instances.
     */
    OM_ARCHIVE_FILE("omarchive",      new FileType[]{FileType.OM_ARCHIVE_FILE}),

    /**
     * A JSON (JavaScript Object Notation) file containing the configuration for an Open Metadata and Governance (OMAG) Server.
     */
    OM_CONFIG_DOC("config",     new FileType[]{FileType.OM_CONFIG_DOC}),

    /**
     * A JSON (JavaScript Object Notation) file the cohort registry for an Open Metadata and Governance (OMAG) Server.
     */
    OM_COHORT_REGISTRY("cohortregistry",    new FileType[]{FileType.OM_COHORT_REGISTRY}),

    /**
     * A JSON (JavaScript Object Notation) file containing an audit log record.
     */
    OM_AUDIT_LOG_RECORD_FILE("omalrecord",    new FileType[]{FileType.OM_AUDIT_LOG_RECORD_FILE}),

    /**
     * A directory (folder) containing audit log records.
     */
    OM_AUDIT_LOG_FOLDER("auditlog",     new FileType[]{FileType.OM_AUDIT_LOG_FOLDER}),

    /**
     * A file containing an open lineage event.
     */
    OL_EVENT_FILE("openlineageevent",       new FileType[]{FileType.OL_EVENT_FILE}),

    /**
     * A text file formatted using the XML (eXtended Markup Language) standard.
     */
    XML_FILE("xml",       new FileType[]{FileType.XML_FILE}),

    /**
     * A data file formatted in columns using the Apache Parquet standard.
     */
    PQT_FILE("pqt",     new FileType[]{FileType.PARQUET_FILE}),

    /**
     * A data file formatted in columns using the Apache Parquet standard.
     */
    PARQUET_FILE("parquet",     new FileType[]{FileType.PARQUET_FILE}),

    /**
     * A data file formatted using Optimized Row Columnar (ORC) file format that provides a highly efficient way to store Hive data.
     */
    ORC_FILE("orc",      new FileType[]{FileType.ORC_FILE}),

    /**
     * A data file formatted in tabbed sheets of tabular data and formulas for Microsoft Excel.
     */
    XLSX_FILE("xlsx",    new FileType[]{FileType.XLSX_FILE}),

    /**
     * A data file formatted in tabbed sheets of tabular data and formulas for Microsoft Excel.
     */
    XLS_FILE("xls",     new FileType[]{FileType.XLS_FILE}),

    /**
     * A data file formatted in tabbed sheets of tabular data and formulas for Apple Numbers.
     */
    NUMBER_FILE("numbers",     new FileType[]{FileType.NUMBER_FILE}),

    /**
     * A document of words and diagrams in deprecated Microsoft Word format.
     */
    OLD_WORD_DOC("doc",       new FileType[]{FileType.OLD_WORD_DOC}),

    /**
     * A document of words and diagrams in Microsoft Word format.
     */
    WORD_DOC("docx",         new FileType[]{FileType.WORD_DOC}),

    /**
     * A document of words and diagrams in OpenDocument format.
     */
    ODT_DOC("odt",       new FileType[]{FileType.ODT_DOC}),

    /**
     * A document of words and diagrams in Apple Pages format.
     */
    PAGES_DOC("pages",        new FileType[]{FileType.PAGES_DOC}),

    /**
     * A document of words and diagrams in XML format for the draw.io tool.
     */
    DRAWIO_FILE("drawio",        new FileType[]{FileType.DRAW_IO_FILE}),

    /**
     * A document of words and linked to diagrams in Markdown format.
     */
    MD_FILE("md",        new FileType[]{FileType.MARKDOWN_FILE}),

    /**
     * A free-form text document.
     */
    TXT_FILE("txt",        new FileType[]{FileType.TEXT_FILE}),

    /**
     * A free-form text document.
     */
    TEX_FILE("tex",        new FileType[]{FileType.TEXT_FILE}),

    /**
     * A rich text document.
     */
    RTF_FILE("rtf", new FileType[]{FileType.RICH_TEXT}),

    /**
     * A container of embedded text and graphics based on a standard developed by Adobe.
     */
    PDF_FILE("pdf",        new FileType[]{FileType.PDF_FILE}),

    /**
     * A file containing a presentation for the Microsoft PowerPoint application.
     */
    PPT_FILE("ppt",       new FileType[]{FileType.PPT_FILE}),

    /**
     * A file containing a presentation for the Microsoft PowerPoint application.
     */
    POT_FILE("pot",         new FileType[]{FileType.POT_FILE}),

    /**
     * A file containing a presentation for the Microsoft PowerPoint application.
     */
    PPTX_FILE("pptx",      new FileType[]{FileType.PPTX_FILE}),

    /**
     * A file containing a presentation for the Microsoft PowerPoint application.
     */
    POTX_FILE("potx",        new FileType[]{FileType.POTX_FILE}),

    /**
     * A file containing a presentation for the Apple Keynote application, or a file containing encrypted secrets.
     */
    KEY_FILE("key",      new FileType[]{FileType.KEYNOTE_FILE, FileType.ENCRYPT_KEYS_FILE}),


    /**
     * A file containing a digital certificate that uses PKCS#12 (Public Key Cryptography Standard #12) encryption. It is used as a portable format for transferring personal private keys and other sensitive information.
     */
    P12_FILE("p12", new FileType[]{FileType.PIEF_FILE}),

    /**
     * A file containing a digital certificate that uses PKCS#12 (Public Key Cryptography Standard #12) encryption. It is used as a portable format for transferring personal private keys and other sensitive information.
     */
    PFX_FILE("pfx", new FileType[]{FileType.PIEF_FILE}),

    /**
     * A Java-specific key store containing private keys and certificates, but it cannot be used to store secret keys.
     */
    JKS_FILE("jks",    new FileType[]{FileType.JKS_FILE}),

    /**
     * A Java-specific key store containing private keys and certificates, but it should not be used to store secret keys because it is vulnerable to brute-force cyberattacks.
     */
    JCEKS_FILE("jceks",    new FileType[]{FileType.JCEKS_FILE}),

    /**
     * A document of words and linked to diagrams in Hypertext Markup Language (HTML) format.
     */
    HTM_FILE("htm",  new FileType[]{FileType.HTML_FILE}),

    /**
     * A document of words and linked to diagrams in Hypertext Markup Language (HTML) format.
     */
    HTML_FILE("html",  new FileType[]{FileType.HTML_FILE}),

    /**
     * A device independent image file with tag descriptions, some standard, some proprietary.
     */
    TIF_FILE("tif",  new FileType[]{FileType.TIF_FILE}),

    /**
     * A device independent image file with tag descriptions, some standard, some proprietary.
     */
    TIFF_FILE("tiff",  new FileType[]{FileType.TIF_FILE}),

    /**
     * A device independent image file with tag descriptions, some standard, some proprietary.
     */
    TFF_FILE("tff",  new FileType[]{FileType.TIF_FILE}),

    /**
     * "A TIFF formatted file with additional geo-referencing information embedded."
     */
    GTIFF_FILE("gtiff",   new FileType[]{FileType.GEOTIFF_FILE}),

    /**
     * A device independent formatted image file with no compression (lossless).
     */
    BMP_FILE("bmp",  new FileType[]{FileType.BMP_FILE}),

    /**
     * A device independent formatted image file from the Joint Photographic Experts Group (JPEG) with lossy compression used by most digital cameras.
     */
    JPG_FILE("jpg",  new FileType[]{FileType.JPG_FILE}),

    /**
     * A device independent formatted image file from the Joint Photographic Experts Group (JPEG) with lossy compression used by most digital cameras.
     */
    JPEG_FILE("jpeg",  new FileType[]{FileType.JPG_FILE}),

    /**
     * A device independent formatted image file with limited colour depth for graphics with limited colours for simple diagrams, icons, logos and cartoons. It uses lossless compression.
     */
    GIF_FILE("gif",  new FileType[]{FileType.GIF_FILE}),

    /**
     * A device independent formatted image file with an 8-bit (256 colors) palette image and 24-bit truecolor (16 million colors) or 48-bit truecolor with and without alpha channel. It uses lossless compression.
     */
    PNG_FILE("png", new FileType[]{FileType.PNG_FILE}),

    /**
     * A device independent formatted image container file following the High Efficiency Image Format (HEIF). It adds High Efficiency Video Coding (HEVC).
     */
    HEIC_FILE("heic",   new FileType[]{FileType.HEIC_FILE}),

    /**
     * A file containing one or more small images of different sizes and colour depths for use as icons.
     */
    ICO_FILE("ico",   new FileType[]{FileType.ICO_FILE}),

    /**
     * A descriptive file used to define the appearance of elements in a web browser user interface.
     */
    CSS_FILE("css",   new FileType[]{FileType.CSS_FILE}),

    /**
     * A descriptive file used to define the appearance of elements in a web browser user interface.  A superset of CSS.
     */
    SCSS_FILE("sccs",    new FileType[]{FileType.SCSS_FILE}),

    /**
     * A file containing Python source code.
     */
    PY_FILE("py",   new FileType[]{FileType.PY_FILE}),

    /**
     * A file containing Javascript source code.
     */
    JS_FILE("js",  new FileType[]{FileType.JS_FILE}),

    /**
     * A file containing Typescript source code or a file containing MPEG-2-compressed video data.
     */
    TS_FILE("ts",   new FileType[]{FileType.TYPESCRIPT_FILE, FileType.VIDEO_TS_FILE}),

    /**
     * A file containing a list of SQL statements used to configure or load data into a relational database.
     */
    SQL_FILE("sql",   new FileType[]{FileType.SQL_FILE}),

    /**
     * A file containing a list of Microsoft DOS commands.
     */
    BAT_FILE("bat",  new FileType[]{FileType.DOS_BAT_FILE}),

    /**
     * A file containing a list of Microsoft DOS commands.
     */
    COM_FILE("com",  new FileType[]{FileType.DOS_COM_FILE}),

    /**
     * A file containing a list of Microsoft Windows commands.
     */
    WSF_FILE("wsf",   new FileType[]{FileType.MS_WSF_FILE}),

    /**
     * A file containing a list of Unix commands that run in the Bash shell.
     */
    SH_FILE("sh", new FileType[]{FileType.BASH_SCRIPT_FILE}),

    /**
     * A file containing a list of Unix commands that run in the Korn shell.
     */
    KSH_FILE("ksh",   new FileType[]{FileType.KORN_SCRIPT_FILE}),

    /**
     * A file containing a list of name-value properties.
     */
    PROPERTIES_FILE("properties",   new FileType[]{FileType.PROPERTIES_FILE}),

    /**
     * A file containing a list of name-value properties for configuration.
     */
    CFG_FILE("cfg",   new FileType[]{FileType.CONFIGURATION_FILE}),

    /**
     * A file containing a list of name-value properties for configuration.
     */
    CONF_FILE("conf",   new FileType[]{FileType.CONFIGURATION_FILE}),

    /**
     * A file where each row represents a log record.
     */
    LOG_FILE("log",    new FileType[]{FileType.LOG_FILE}),

    /**
     * A file where each row represents a log message.
     */
    MSG_FILE("msg",    new FileType[]{FileType.LOG_MESSAGE_FILE}),

    /**
     * A file where each row represents a string output on the standard error (stderr) destination of a running process.
     */
    ERR_FILE("err",     new FileType[]{FileType.STDERR_FILE}),

    /**
     * A file where each row represents a string output on the standard out (stdout) destination of a running process.
     */
    STDOUT_FILE("out",   new FileType[]{FileType.STDOUT_FILE}),

    /**
     * A file containing Java programming language source code and comments.
     */
    JAVA_FILE("java",   new FileType[]{FileType.JAVA_FILE}),

    /**
     * A file containing complied Java code.
     */
    CLASS_FILE("class",  new FileType[]{FileType.CLASS_FILE}),

    /**
     * A file containing executable application code for the Apple macOS operating system.
     */
    APP_FILE("app",   new FileType[]{FileType.APP_FILE}),

    /**
     * A file containing executable application code for the Microsoft Windows operating system.
     */
    EXE_FILE("exe",    new FileType[]{FileType.EXE_FILE}),

    /**
     * A file containing an organized collection of executable Java code and related resources.
     */
    JAR_FILE("jar",   new FileType[]{FileType.JAR_FILE}),

    /**
     * A file containing an organized collection of files.  Often used for install.
     */
    TAR_FILE("tar",      new FileType[]{FileType.TAR_FILE}),

    /**
     * "A file containing an organized collection of files that has been compressed to save storage."
     */
    GZ_FILE("gz",       new FileType[]{FileType.GZ_FILE}),

    /**
     * A file containing an organized collection of files that have been compressed to save storage.
     */
    ZIP_FILE("zip",      new FileType[]{FileType.ZIP_FILE}),

    /**
     * A file containing an organized collection of files that represent a web application.
     */
    WAR_FILE("war",      new FileType[]{FileType.WAR_FILE}),

    /**
     * A file containing an organized collection of files that represent a JEE application.
     */
    EAR_FILE("ear",       new FileType[]{FileType.EAR_FILE}),

    /**
     * "A file containing an organized collection of files that represent an application for Apple macOS."
     */
    DMG_FILE("dmg",       new FileType[]{FileType.DMG_FILE}),

    /**
     * A file containing an organized collection of files that represent an application for Microsoft Windows.
     */
    ISO_FILE("iso",       new FileType[]{FileType.ISO_FILE}),

    /**
     * A file containing a set of properties in the YAML format.
     */
    YML_FILE("yml",      new FileType[]{FileType.YAML_FILE}),

    /**
     * A file containing a set of properties in the YAML format.
     */
    YAML_FILE("yaml",      new FileType[]{FileType.YAML_FILE}),


    /**
     * A file containing a secrets store in the YAML format.
     */
    OM_SECRETS("omsecrets",      new FileType[]{FileType.OM_SECRETS_FILE}),

    /**
     * A file containing binary data.
     */
    BIN_FILE("bin",        new FileType[]{FileType.BIN_FILE}),

    /**
     * A file containing data, typically from an application.  The format is known by the application.
     */
    DAT_FILE("dat",         new FileType[]{FileType.DAT_FILE}),

    /**
     * A file containing report data, typically from an application.  The format is known by the application.
     */
    RPT_FILE("rpt",        new FileType[]{FileType.RPT_FILE}),

    /**
     * A file containing data from Microsoft Project.
     */
    MPP_FILE("mpp",       new FileType[]{FileType.MPP_FILE}),

    /**
     * A text file format used in bioinformatics for storing gene sequence variations.
     */
    VCF_FILE("vcf",      new FileType[]{FileType.VCF_FILE}),

    /**
     * A file containing audio samples and music.  Often used for storing uncompressed, high quality audio on CDs and personal computers.
     */
    AIFF_FILE("aiff",    new FileType[]{FileType.AIFF_FILE}),

    /**
     * A file containing lossless compression audio coding format.
     */
    FLAC_FILE("flac",      new FileType[]{FileType.FLAC_FILE}),

    /**
     * A file containing lossless compression audio coding format.
     */
    WAV_FILE("wav",        new FileType[]{FileType.WAV_FILE}),

    /**
     * A file containing audio data with Moving Pictures Experts Group (MPEG) compression.
     */
    MP3_FILE("mp3",      new FileType[]{FileType.MP3_FILE}),

    /**
     * A file containing audio data with Microsoft's specific compression.
     */
    WMA_FILE("wma",    new FileType[]{FileType.WMA_FILE}),

    /**
     * A file containing video data with Moving Pictures Experts Group (MPEG) compression.
     */
    MP4_FILE("mp4",      new FileType[]{FileType.MP4_FILE}),

    /**
     * A file containing video data in small file sizes for mobile phones and other devices.
     */
    THREE_GP_FILE("3gp",      new FileType[]{FileType.MOBILE_VIDEO_FILE}),

    /**
     * A file containing video data in small file sizes for mobile phones and other devices.
     */
    THREE_G2_FILE("3g2",      new FileType[]{FileType.MOBILE_VIDEO_FILE}),

    /**
     * A file containing high quality video data.
     */
    MOV_FILE("mov",        new FileType[]{FileType.MOV_FILE}),

    /**
     * A file containing video data for Microsoft Windows.
     */
    AVI_FILE("avi",   new FileType[]{FileType.AVI_FILE}),

    /**
     * A file containing video data for Apple systems, often includes Apple’s FairPlay DRM copyright protection.
     */
    M4V_FILE("m4v",     new FileType[]{FileType.M4V_FILE}),

    /**
     * A file containing video data for Microsoft Windows Media Player.
     */
    WMV_FILE("wmv",       new FileType[]{FileType.WMV_FILE}),

    /**
     * A file containing vector image data for Adobe Illustrator.
     */
    AI_FILE("ai",        new FileType[]{FileType.AI_FILE}),

    /**
     * A file containing vector image data.
     */
    SVG_FILE("svg",       new FileType[]{FileType.SVG_FILE}),

    /**
     * A file containing high quality vector image data. Used in the printing industry.
     */
    EPS_FILE("eps",       new FileType[]{FileType.EPS_FILE}),

    /**
     * A file containing an image formatted in the PostScript language.
     */
    PS_FILE("ps",        new FileType[]{FileType.PS_FILE}),

    /**
     * A file containing a 3 dimensional object.
     */
    OBJ_FILE("obj",       new FileType[]{FileType.OBJ_FILE}),

    /**
     * A file that describes how a program should apply textures to that object. MTL files contain the names of the texture bitmap files to
     * apply to an object as well as the 3D coordinates at which to apply them.
     */
    MTL_FILE("mtl",       new FileType[]{FileType.MTL_FILE}),

    ;

    private final String                     fileExtension;
    private final FileType[]                 fileTypes;



    /**
     * Constructor for individual enum value.
     *
     * @param fileExtension extension
     * @param fileTypes the file type
     */
    FileExtension(String     fileExtension,
                  FileType[] fileTypes)
    {
        this.fileExtension = fileExtension;
        this.fileTypes = fileTypes;
    }


    /**
     * Return the file name.
     *
     * @return string
     */
    public String getFileExtension()
    {
        return fileExtension;
    }


    /**
     * Return the file type.
     *
     * @return string
     */
    public List<FileType> getFileTypes()
    {
        if (fileTypes != null)
        {
            return Arrays.asList(fileTypes);
        }

        return null;
    }



    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "FileExtension{" + fileExtension + '}';
    }
}
