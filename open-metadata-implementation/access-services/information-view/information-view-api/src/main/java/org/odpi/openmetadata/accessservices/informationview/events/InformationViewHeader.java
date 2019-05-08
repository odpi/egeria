/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = InformationViewEvent.class, name = "InformationViewEvent"),
        @JsonSubTypes.Type(value = TableContextEvent.class, name = "TableContextEvent"),
        @JsonSubTypes.Type(value = RegistrationRequestBody.class, name = "RegistrationRequestBody"),
        @JsonSubTypes.Type(value = ReportRequestBody.class, name = "ReportRequestBody"),
        @JsonSubTypes.Type(value = DataViewRequestBody.class, name = "DataViewRequestBody"),
        @JsonSubTypes.Type(value = SemanticAssignment.class, name = "SemanticAssignment"),
        @JsonSubTypes.Type(value = UpdatedEntityEvent.class, name = "UpdatedEntityEvent"),
})
public abstract class InformationViewHeader {

    private long eventVersionId = 1L;

    public long getEventVersionId() {
        return eventVersionId;
    }

    public void setEventVersionId(long eventVersionId) {
        this.eventVersionId = eventVersionId;
    }


    @Override
    public String toString() {
        return "{" +
                "eventVersionId=" + eventVersionId +
                '}';
    }
}
