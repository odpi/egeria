/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CodeAnalysisAnnotationProperties describes the content of the code in a code repository like GitHub.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CodeAnalysisAnnotationProperties extends AnnotationProperties
{
    private int     fileCount                  = 0;
    private long    lineCount                  = 0;
    private long    codeLineCount              = 0;
    private long    commentLineCount           = 0;
    private String  primaryLanguage            = null;
    private int     languageCount              = 0;
    private long    publicSymbolCount          = 0;
    private int     entryPointCount            = 0;
    private long    dataReadCount              = 0;
    private long    dataCreateCount            = 0;
    private long    dataUpdateCount            = 0;
    private long    dataDeleteCount            = 0;
    private long    dataChecksCount            = 0;
    private long    dataStoreCount             = 0;
    private long    externalCallCount          = 0;
    private long    functionCount              = 0;
    private long    cyclomaticComplexityTotal  = 0;
    private int     cyclomaticComplexityMax    = 0;
    private int     maxNestingDepth            = 0;
    private int     testFileCount              = 0;
    private long    documentedSymbolCount      = 0;


    /**
     * Default constructor
     */
    public CodeAnalysisAnnotationProperties()
    {
        super();
        super.typeName = OpenMetadataType.CODE_ANALYSIS_ANNOTATION.typeName;
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public CodeAnalysisAnnotationProperties(CodeAnalysisAnnotationProperties template)
    {
        super(template);

        if (template != null)
        {
            this.fileCount = template.getFileCount();
            this.lineCount = template.getLineCount();
            this.codeLineCount = template.getCodeLineCount();
            this.commentLineCount = template.getCommentLineCount();
            this.primaryLanguage = template.getPrimaryLanguage();
            this.languageCount = template.getLanguageCount();
            this.publicSymbolCount = template.getPublicSymbolCount();
            this.entryPointCount = template.getEntryPointCount();
            this.dataReadCount = template.getDataReadCount();
            this.dataCreateCount = template.getDataCreateCount();
            this.dataUpdateCount = template.getDataUpdateCount();
            this.dataDeleteCount = template.getDataDeleteCount();
            this.dataChecksCount = template.getDataChecksCount();
            this.dataStoreCount = template.getDataStoreCount();
            this.externalCallCount = template.getExternalCallCount();
            this.functionCount = template.getFunctionCount();
            this.cyclomaticComplexityTotal = template.getCyclomaticComplexityTotal();
            this.cyclomaticComplexityMax = template.getCyclomaticComplexityMax();
            this.maxNestingDepth = template.getMaxNestingDepth();
            this.testFileCount = template.getTestFileCount();
            this.documentedSymbolCount = template.getDocumentedSymbolCount();
        }
    }


    /**
     * Return number of files in the source code repository.
     *
     * @return int
     */
    public int getFileCount()
    {
        return fileCount;
    }


    /**
     * Set up number of files in the source code repository.
     *
     * @param fileCount int
     */
    public void setFileCount(int fileCount)
    {
        this.fileCount = fileCount;
    }


    /**
     * Return total number of physical lines in the source code repository, including blank lines and comments.
     *
     * @return long
     */
    public long getLineCount()
    {
        return lineCount;
    }


    /**
     * Set up total number of physical lines in the source code repository, including blank lines and comments.
     *
     * @param lineCount long
     */
    public void setLineCount(long lineCount)
    {
        this.lineCount = lineCount;
    }


    /**
     * Return number of lines of code in the source code repository, excluding blank lines and comments.
     *
     * @return long
     */
    public long getCodeLineCount()
    {
        return codeLineCount;
    }


    /**
     * Set up number of lines of code in the source code repository, excluding blank lines and comments.
     *
     * @param codeLineCount long
     */
    public void setCodeLineCount(long codeLineCount)
    {
        this.codeLineCount = codeLineCount;
    }


    /**
     * Return number of comment lines in the source code repository.
     *
     * @return long
     */
    public long getCommentLineCount()
    {
        return commentLineCount;
    }


    /**
     * Set up number of comment lines in the source code repository.
     *
     * @param commentLineCount long
     */
    public void setCommentLineCount(long commentLineCount)
    {
        this.commentLineCount = commentLineCount;
    }


    /**
     * Return the programming language that most of the source code in the repository is written in.
     *
     * @return string
     */
    public String getPrimaryLanguage()
    {
        return primaryLanguage;
    }


    /**
     * Set up the programming language that most of the source code in the repository is written in.
     *
     * @param primaryLanguage string
     */
    public void setPrimaryLanguage(String primaryLanguage)
    {
        this.primaryLanguage = primaryLanguage;
    }


    /**
     * Return number of distinct programming languages used in the source code repository.
     *
     * @return int
     */
    public int getLanguageCount()
    {
        return languageCount;
    }


    /**
     * Set up number of distinct programming languages used in the source code repository.
     *
     * @param languageCount int
     */
    public void setLanguageCount(int languageCount)
    {
        this.languageCount = languageCount;
    }


    /**
     * Return number of symbols that the component exports for use by its callers, such as public functions, classes and endpoints.
     *
     * @return long
     */
    public long getPublicSymbolCount()
    {
        return publicSymbolCount;
    }


    /**
     * Set up number of symbols that the component exports for use by its callers, such as public functions, classes and endpoints.
     *
     * @param publicSymbolCount long
     */
    public void setPublicSymbolCount(long publicSymbolCount)
    {
        this.publicSymbolCount = publicSymbolCount;
    }


    /**
     * Return number of entry points into the component, such as main methods, command line commands, route handlers and task definitions.
     *
     * @return int
     */
    public int getEntryPointCount()
    {
        return entryPointCount;
    }


    /**
     * Set up number of entry points into the component, such as main methods, command line commands, route handlers and task definitions.
     *
     * @param entryPointCount int
     */
    public void setEntryPointCount(int entryPointCount)
    {
        this.entryPointCount = entryPointCount;
    }


    /**
     * Return number of places in the code where data is read.
     *
     * @return long
     */
    public long getDataReadCount()
    {
        return dataReadCount;
    }


    /**
     * Set up number of places in the code where data is read.
     *
     * @param dataReadCount long
     */
    public void setDataReadCount(long dataReadCount)
    {
        this.dataReadCount = dataReadCount;
    }


    /**
     * Return number of places in the code where data is created.
     *
     * @return long
     */
    public long getDataCreateCount()
    {
        return dataCreateCount;
    }


    /**
     * Set up number of places in the code where data is created.
     *
     * @param dataCreateCount long
     */
    public void setDataCreateCount(long dataCreateCount)
    {
        this.dataCreateCount = dataCreateCount;
    }


    /**
     * Return number of places in the code where data is updated.
     *
     * @return long
     */
    public long getDataUpdateCount()
    {
        return dataUpdateCount;
    }


    /**
     * Set up number of places in the code where data is updated.
     *
     * @param dataUpdateCount long
     */
    public void setDataUpdateCount(long dataUpdateCount)
    {
        this.dataUpdateCount = dataUpdateCount;
    }


    /**
     * Return number of places in the code where data is deleted.
     *
     * @return long
     */
    public long getDataDeleteCount()
    {
        return dataDeleteCount;
    }


    /**
     * Set up number of places in the code where data is deleted.
     *
     * @param dataDeleteCount long
     */
    public void setDataDeleteCount(long dataDeleteCount)
    {
        this.dataDeleteCount = dataDeleteCount;
    }


    /**
     * Return number of places in the code where data is validated.
     *
     * @return long
     */
    public long getDataChecksCount()
    {
        return dataChecksCount;
    }


    /**
     * Set up number of places in the code where data is validated.
     *
     * @param dataChecksCount long
     */
    public void setDataChecksCount(long dataChecksCount)
    {
        this.dataChecksCount = dataChecksCount;
    }


    /**
     * Return number of distinct data stores that the code touches.
     *
     * @return long
     */
    public long getDataStoreCount()
    {
        return dataStoreCount;
    }


    /**
     * Set up number of distinct data stores that the code touches.
     *
     * @param dataStoreCount long
     */
    public void setDataStoreCount(long dataStoreCount)
    {
        this.dataStoreCount = dataStoreCount;
    }


    /**
     * Return number of calls that the code makes out of the component.
     *
     * @return long
     */
    public long getExternalCallCount()
    {
        return externalCallCount;
    }


    /**
     * Set up number of calls that the code makes out of the component.
     *
     * @param externalCallCount long
     */
    public void setExternalCallCount(long externalCallCount)
    {
        this.externalCallCount = externalCallCount;
    }


    /**
     * Return number of functions in the source code repository.
     *
     * @return long
     */
    public long getFunctionCount()
    {
        return functionCount;
    }


    /**
     * Set up number of functions in the source code repository.
     *
     * @param functionCount long
     */
    public void setFunctionCount(long functionCount)
    {
        this.functionCount = functionCount;
    }


    /**
     * Return sum of the cyclomatic complexity of every function in the source code repository.
     *
     * @return long
     */
    public long getCyclomaticComplexityTotal()
    {
        return cyclomaticComplexityTotal;
    }


    /**
     * Set up sum of the cyclomatic complexity of every function in the source code repository.
     *
     * @param cyclomaticComplexityTotal long
     */
    public void setCyclomaticComplexityTotal(long cyclomaticComplexityTotal)
    {
        this.cyclomaticComplexityTotal = cyclomaticComplexityTotal;
    }


    /**
     * Return the highest cyclomatic complexity found in any single function in the source code repository.
     *
     * @return int
     */
    public int getCyclomaticComplexityMax()
    {
        return cyclomaticComplexityMax;
    }


    /**
     * Set up the highest cyclomatic complexity found in any single function in the source code repository.
     *
     * @param cyclomaticComplexityMax int
     */
    public void setCyclomaticComplexityMax(int cyclomaticComplexityMax)
    {
        this.cyclomaticComplexityMax = cyclomaticComplexityMax;
    }


    /**
     * Return the deepest level of nested control flow found in any single function in the source code repository.
     *
     * @return int
     */
    public int getMaxNestingDepth()
    {
        return maxNestingDepth;
    }


    /**
     * Set up the deepest level of nested control flow found in any single function in the source code repository.
     *
     * @param maxNestingDepth int
     */
    public void setMaxNestingDepth(int maxNestingDepth)
    {
        this.maxNestingDepth = maxNestingDepth;
    }


    /**
     * Return number of files in the source code repository that contain tests.
     *
     * @return int
     */
    public int getTestFileCount()
    {
        return testFileCount;
    }


    /**
     * Set up number of files in the source code repository that contain tests.
     *
     * @param testFileCount int
     */
    public void setTestFileCount(int testFileCount)
    {
        this.testFileCount = testFileCount;
    }


    /**
     * Return number of exported symbols that carry documentation.
     *
     * @return long
     */
    public long getDocumentedSymbolCount()
    {
        return documentedSymbolCount;
    }


    /**
     * Set up number of exported symbols that carry documentation.
     *
     * @param documentedSymbolCount long
     */
    public void setDocumentedSymbolCount(long documentedSymbolCount)
    {
        this.documentedSymbolCount = documentedSymbolCount;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "CodeAnalysisAnnotationProperties{" +
                "fileCount=" + fileCount +
                ", lineCount=" + lineCount +
                ", codeLineCount=" + codeLineCount +
                ", commentLineCount=" + commentLineCount +
                ", primaryLanguage=" + primaryLanguage +
                ", languageCount=" + languageCount +
                ", publicSymbolCount=" + publicSymbolCount +
                ", entryPointCount=" + entryPointCount +
                ", dataReadCount=" + dataReadCount +
                ", dataCreateCount=" + dataCreateCount +
                ", dataUpdateCount=" + dataUpdateCount +
                ", dataDeleteCount=" + dataDeleteCount +
                ", dataChecksCount=" + dataChecksCount +
                ", dataStoreCount=" + dataStoreCount +
                ", externalCallCount=" + externalCallCount +
                ", functionCount=" + functionCount +
                ", cyclomaticComplexityTotal=" + cyclomaticComplexityTotal +
                ", cyclomaticComplexityMax=" + cyclomaticComplexityMax +
                ", maxNestingDepth=" + maxNestingDepth +
                ", testFileCount=" + testFileCount +
                ", documentedSymbolCount=" + documentedSymbolCount +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        CodeAnalysisAnnotationProperties that = (CodeAnalysisAnnotationProperties) objectToCompare;
        return fileCount == that.fileCount &&
               lineCount == that.lineCount &&
               codeLineCount == that.codeLineCount &&
               commentLineCount == that.commentLineCount &&
               Objects.equals(primaryLanguage, that.primaryLanguage) &&
               languageCount == that.languageCount &&
               publicSymbolCount == that.publicSymbolCount &&
               entryPointCount == that.entryPointCount &&
               dataReadCount == that.dataReadCount &&
               dataCreateCount == that.dataCreateCount &&
               dataUpdateCount == that.dataUpdateCount &&
               dataDeleteCount == that.dataDeleteCount &&
               dataChecksCount == that.dataChecksCount &&
               dataStoreCount == that.dataStoreCount &&
               externalCallCount == that.externalCallCount &&
               functionCount == that.functionCount &&
               cyclomaticComplexityTotal == that.cyclomaticComplexityTotal &&
               cyclomaticComplexityMax == that.cyclomaticComplexityMax &&
               maxNestingDepth == that.maxNestingDepth &&
               testFileCount == that.testFileCount &&
               documentedSymbolCount == that.documentedSymbolCount;
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), fileCount, lineCount, codeLineCount, commentLineCount, primaryLanguage, languageCount, publicSymbolCount, entryPointCount, dataReadCount, dataCreateCount, dataUpdateCount, dataDeleteCount, dataChecksCount, dataStoreCount, externalCallCount, functionCount, cyclomaticComplexityTotal, cyclomaticComplexityMax, maxNestingDepth, testFileCount, documentedSymbolCount);
    }
}
