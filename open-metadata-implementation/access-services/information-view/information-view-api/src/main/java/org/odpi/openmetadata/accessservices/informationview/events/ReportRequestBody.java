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

    /**
     *
     * @return the unique identifier specific to BI tool for the report
     */
    public String getId() {
        return id;
    }

    /**
     * set the id of the report
     *
     * @param id - unique identifier of the report
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return timestamp for the report creation time
     */
    public Long getCreatedTime() {
        return createdTime;
    }

    /**
     * set the createdTime
     *
     * @param createdTime - timestamp of the creation time of the report
     */
    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    /**
     *
     * @return the author of the report
     */
    public String getAuthor() {
        return author;
    }

    /**
     * set the report's author
     *
     * @param author - creator of the report
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     *
     * @return report name
     */
    public String getReportName() {
        return reportName;
    }

    /**
     * set the report name
     *
     * @param reportName - name of the report
     */
    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    /**
     *
     * @return report path - location on server
     */
    public String getReportPath() {
        return reportPath;
    }

    /**
     * set the report path
     *
     * @param reportPath - path on server
     */
    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    /**
     *
     * @return the url for accessing the report
     */
    public String getReportUrl() {
        return reportUrl;
    }

    /**
     * set set the url of the report
     *
     * @param reportUrl - url to access the report
     */
    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    /**
     *
     * @return the last modifier
     */
    public String getLastModifier() {
        return lastModifier;
    }

    /**
     * set up the last modifier of the report
     *
     * @param lastModifier - last modifier of the report
     */
    public void setLastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
    }

    /**
     *
     * @return time of the last report modification
     */
    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    /**
     *set the last time the report was modified
     *
     * @param lastModifiedTime - time of the last report modification
     */
    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    /**
     *
     * @return list of sources referenced by the report
     */
    public List<Source> getSources() {
        return sources;
    }

    /**
     * set the list of sources referenced by the report
     *
     * @param sources - list of sources referenced by the report
     */
    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    /**
     *
     * @return additional properties of the report
     */
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    /**
     * set additional properties of the report that are not represented as basic report properties
     *
     * @param additionalProperties - additional properties of the report
     */
    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    /**
     *
     * @return elemens composing the reports
     */
    public List<ReportElement> getReportElements() {
        return reportElements;
    }

    /**
     * set the elements of the report
     *
     * @param reportElements - elements composing the report
     */
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
