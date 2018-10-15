/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportRequestBody {


    private ConnectionDetails sourceConnectionDetails;
    private Map<String, Source> sources;

    private String id;
    private String createdTime;
    private String author;
    private String reportName;
    private String reportPath;
    private String reportUrl;
    private String lastModifier;
    private String lastModifiedTime;
    private List<ReportColumn> reportColumns;
    private Map<String, Object> additionalProperties;

    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString() {
        return "ReportRequestBody{" +
                "sourceConnectionDetails=" + sourceConnectionDetails +
                ", sources=" + sources +
                ", id='" + id + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", author='" + author + '\'' +
                ", reportName='" + reportName + '\'' +
                ", reportPath='" + reportPath + '\'' +
                ", reportUrl='" + reportUrl + '\'' +
                ", lastModifier='" + lastModifier + '\'' +
                ", lastModifiedTime='" + lastModifiedTime + '\'' +
                ", reportColumns=" + reportColumns +
                ", additionalProperties=" + additionalProperties +
                '}';
    }

    /**
     * Default constructor
     */
    public ReportRequestBody() {

    }


    public ConnectionDetails getSourceConnectionDetails() {
        return sourceConnectionDetails;
    }

    public void setSourceConnectionDetails(ConnectionDetails sourceConnectionDetails) {
        this.sourceConnectionDetails = sourceConnectionDetails;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportPath() {
        return reportPath;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(String lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public Map<String, Source> getSources() {
        return sources;
    }

    public void setSources(Map<String, Source> sources) {
        this.sources = sources;
    }


    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public List<ReportColumn> getReportColumns() {
        return reportColumns;
    }

    public void setReportColumns(List<ReportColumn> reportColumns) {
        this.reportColumns = reportColumns;
    }


}
