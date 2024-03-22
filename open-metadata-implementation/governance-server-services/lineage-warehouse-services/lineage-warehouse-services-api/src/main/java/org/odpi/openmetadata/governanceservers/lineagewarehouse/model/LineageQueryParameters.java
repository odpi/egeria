/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The values that define the
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineageQueryParameters implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private Scope scope;
    private String displayNameMustContain;
    private boolean includeProcesses;

    /**
     * Default constructor used by Jackson
     */
    public LineageQueryParameters()
    {
    }

    /**
     * Constructor to set up the bean in one go.
     *
     * @param scope scope of the request
     * @param includeProcesses should processes be returned?
     */
    public LineageQueryParameters(Scope   scope,
                                  boolean includeProcesses)
    {
        this.scope = scope;
        this.includeProcesses = includeProcesses;
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

    public void setDisplayNameMustContain(String displayNameMustContain)
    {
        this.displayNameMustContain = displayNameMustContain;
    }

    public boolean isIncludeProcesses() {
        return includeProcesses;
    }

    public void setIncludeProcesses(boolean includeProcesses) {
        this.includeProcesses = includeProcesses;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "LineageQueryParams{" +
                "scope=" + scope +
                ", displayNameMustContain='" + displayNameMustContain + '\'' +
                ", includeProcesses=" + includeProcesses +
                '}';
    }
}


