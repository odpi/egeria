/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class"
)
public class LineageQueryParameters {

    private Scope scope;
    private String displayNameMustContain;
    private boolean includeProcesses;
    private  boolean includeGlossaryTerms;

    public LineageQueryParameters() {
    }

    public LineageQueryParameters(Scope scope, String displayNameMustContain, boolean includeProcesses, boolean includeGlossaryTerms) {
        this.scope = scope;
        this.displayNameMustContain = displayNameMustContain;
        this.includeProcesses = includeProcesses;
        this.includeGlossaryTerms = includeGlossaryTerms;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public String getDisplayNameMustContain() {
        return displayNameMustContain;
    }

    public void setDisplayNameMustContain(String displayNameMustContain) {
        this.displayNameMustContain = displayNameMustContain;
    }

    public boolean isIncludeProcesses() {
        return includeProcesses;
    }

    public void setIncludeProcesses(boolean includeProcesses) {
        this.includeProcesses = includeProcesses;
    }

    public boolean isIncludeGlossaryTerms() {
        return includeGlossaryTerms;
    }

    public void setIncludeGlossaryTerms(boolean includeGlossaryTerms) {
        this.includeGlossaryTerms = includeGlossaryTerms;
    }

    @Override
    public String toString() {
        return "LineageQueryParams{" +
                "scope=" + scope +
                ", displayNameMustContain='" + displayNameMustContain + '\'' +
                ", includeProcesses=" + includeProcesses +
                ", includeGlossaryTerms=" + includeGlossaryTerms +
                '}';
    }
}


