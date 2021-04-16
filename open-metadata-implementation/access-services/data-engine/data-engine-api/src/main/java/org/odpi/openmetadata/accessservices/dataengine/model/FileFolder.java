/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OM type FileFolder
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileFolder extends DataStore {

    private String pathName;

    /**
     * Gets the file path
     *
     * @return path
     */
    public String getPathName() {
        return pathName;
    }

    /**
     * Sets the file path
     *
     * @param pathName path
     */
    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    @Override
    public String toString() {
        return "DataFile{" +
                ", path='" + pathName + "'" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileFolder dataFile = (FileFolder) o;

        return Objects.equals(pathName, dataFile.pathName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pathName);
    }

}
