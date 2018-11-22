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
public class ReportRequestBody extends InformationViewHeader {
    private List<Source> sources;
    private String id;
    private Long createdTime;
    private String author;
    private String reportName;
    private String reportPath;
    private String reportUrl;
    private String lastModifier;
    private Long lastModifiedTime;
    private List<ReportElement> reportElements;
    private Map<String, Object> additionalProperties;

    /**
     * Default constructor
     */
    public ReportRequestBody() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
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

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public List<ReportElement> getReportElements() {
        return reportElements;
    }

    public void setReportElements(List<ReportElement> reportElements) {
        this.reportElements = reportElements;
    }

    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString() {
        return "ReportRequestBody{" +
                "sources=" + sources +
                ", id='" + id + '\'' +
                ", createdTime=" + createdTime +
                ", author='" + author + '\'' +
                ", reportName='" + reportName + '\'' +
                ", reportPath='" + reportPath + '\'' +
                ", reportUrl='" + reportUrl + '\'' +
                ", lastModifier='" + lastModifier + '\'' +
                ", lastModifiedTime=" + lastModifiedTime +
                ", reportElements=" + reportElements +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
