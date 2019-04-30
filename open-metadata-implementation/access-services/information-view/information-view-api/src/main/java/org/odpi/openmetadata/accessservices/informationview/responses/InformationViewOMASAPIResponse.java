/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "class")
@JsonSubTypes
        ({
          @JsonSubTypes.Type(value = VoidResponse.class, name = "VoidResponse"),
          @JsonSubTypes.Type(value = DatabaseListResponse.class, name = "DatabaseListResponse"),
          @JsonSubTypes.Type(value = TableListResponse.class, name = "TableListResponse"),
          @JsonSubTypes.Type(value = TableColumnsResponse.class, name = "TableColumnsResponse"),
          @JsonSubTypes.Type(value = RegistrationResponse.class, name = "RegistrationResponse")
        })
public class InformationViewOMASAPIResponse{

    private int                  relatedHTTPCode = 200;
    private String               exceptionClassName = null;
    private String               exceptionErrorMessage = null;
    private String               exceptionSystemAction = null;
    private String               exceptionUserAction = null;
    private Map<String, Object> exceptionProperties = null;

    public int getRelatedHTTPCode() {
        return relatedHTTPCode;
    }

    public void setRelatedHTTPCode(int relatedHTTPCode) {
        this.relatedHTTPCode = relatedHTTPCode;
    }

    public String getExceptionClassName() {
        return exceptionClassName;
    }

    public void setExceptionClassName(String exceptionClassName) {
        this.exceptionClassName = exceptionClassName;
    }

    public String getExceptionErrorMessage() {
        return exceptionErrorMessage;
    }

    public void setExceptionErrorMessage(String exceptionErrorMessage) {
        this.exceptionErrorMessage = exceptionErrorMessage;
    }

    public String getExceptionSystemAction() {
        return exceptionSystemAction;
    }

    public void setExceptionSystemAction(String exceptionSystemAction) {
        this.exceptionSystemAction = exceptionSystemAction;
    }

    public String getExceptionUserAction() {
        return exceptionUserAction;
    }

    public void setExceptionUserAction(String exceptionUserAction) {
        this.exceptionUserAction = exceptionUserAction;
    }

    public Map<String, Object> getExceptionProperties() {
        return exceptionProperties;
    }

    public void setExceptionProperties(Map<String, Object> exceptionProperties) {
        this.exceptionProperties = exceptionProperties;
    }

    @Override
    public String toString() {
        return "InformationViewOMASAPIResponse{" +
                "relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                ", exceptionProperties=" + exceptionProperties +
                '}';
    }
}
