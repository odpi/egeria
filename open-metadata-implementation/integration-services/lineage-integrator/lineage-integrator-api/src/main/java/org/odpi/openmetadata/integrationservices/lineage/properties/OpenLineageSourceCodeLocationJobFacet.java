/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.lineage.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the content of an open lineage source code location job facet as defined in JSON
 * spec https://openlineage.io/spec/facets/1-0-0/SourceCodeLocationJobFacet.json#/$defs/SourceCodeLocationJobFacet.
 * It is used internally in Egeria to pass this information to the Lineage Integrator OMIS's integration connectors.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageSourceCodeLocationJobFacet extends OpenLineageJobFacet
{
    private String type = null;
    private URI    url = null;
    private String repoUrl = null;
    private String path = null;
    private String version = null;
    private String tag = null;
    private String branch = null;

    /**
     * Default constructor
     */
    public OpenLineageSourceCodeLocationJobFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-0/SourceCodeLocationJobFacet.json#/$defs/SourceCodeLocationJobFacet"));
    }


    /**
     * Return the type of source code control system.
     *
     * @return string name
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the type of source code control system.
     *
     * @param type string name
     */
    public void setType(String type)
    {
        this.type = type;
    }


    /**
     * Return the full http URL to locate the file.
     *
     * @return url
     */
    public URI getUrl()
    {
        return url;
    }


    /**
     * Set up the full http URL to locate the file.
     *
     * @param url url
     */
    public void setUrl(URI url)
    {
        this.url = url;
    }


    /**
     * Return the URL to the repository.
     *
     * @return url
     */
    public String getRepoUrl()
    {
        return repoUrl;
    }


    /**
     * Set up the URL to the repository.
     *
     * @param repoUrl url
     */
    public void setRepoUrl(String repoUrl)
    {
        this.repoUrl = repoUrl;
    }


    /**
     * Return the path in the repo containing the source files.
     *
     * @return name
     */
    public String getPath()
    {
        return path;
    }


    /**
     * Set up the path in the repo containing the source files.
     *
     * @param path name
     */
    public void setPath(String path)
    {
        this.path = path;
    }


    /**
     * Return the current version deployed (not a branch name, the actual unique version).
     *
     * @return version number
     */
    public String getVersion()
    {
        return version;
    }


    /**
     * Set up the current version deployed (not a branch name, the actual unique version).
     *
     * @param version version number
     */
    public void setVersion(String version)
    {
        this.version = version;
    }


    /**
     * Return the optional tag name.
     *
     * @return name
     */
    public String getTag()
    {
        return tag;
    }


    /**
     * Set up the optional tag name.
     *
     * @param tag name
     */
    public void setTag(String tag)
    {
        this.tag = tag;
    }


    /**
     * Return the optional branch name.
     *
     * @return name
     */
    public String getBranch()
    {
        return branch;
    }


    /**
     * Set up the optional branch name.
     *
     * @param branch name
     */
    public void setBranch(String branch)
    {
        this.branch = branch;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageSourceCodeLocationJobFacet{" +
                       "type='" + type + '\'' +
                       ", url=" + url +
                       ", repoUrl='" + repoUrl + '\'' +
                       ", path='" + path + '\'' +
                       ", version='" + version + '\'' +
                       ", tag='" + tag + '\'' +
                       ", branch='" + branch + '\'' +
                       ", _producer=" + get_producer() +
                       ", _schemaURL=" + get_schemaURL() +
                       ", additionalProperties=" + getAdditionalProperties() +
                       '}';
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
        OpenLineageSourceCodeLocationJobFacet that = (OpenLineageSourceCodeLocationJobFacet) objectToCompare;
        return Objects.equals(type, that.type) &&
                       Objects.equals(url, that.url) &&
                       Objects.equals(repoUrl, that.repoUrl) &&
                       Objects.equals(path, that.path) &&
                       Objects.equals(version, that.version) &&
                       Objects.equals(tag, that.tag) &&
                       Objects.equals(branch, that.branch);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), type, url, repoUrl, path, version, tag, branch);
    }
}
