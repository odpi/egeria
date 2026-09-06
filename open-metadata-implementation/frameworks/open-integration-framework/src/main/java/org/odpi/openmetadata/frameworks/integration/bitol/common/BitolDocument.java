/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.OptBoolean;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * BitolDocument is the abstract root of the documents defined by the Bitol standards: the Open Data Contract
 * Standard (ODCS) DataContract and the Open Data Product Standard (ODPS) DataProduct. It holds the fundamentals
 * that the two standards share. The "kind" property identifies which type of document this is and is used by
 * Jackson to select the subclass when a document is parsed through this class. The Bitol documents are typically
 * encoded in YAML although JSON is also valid.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.EXISTING_PROPERTY,
              property = "kind",
              visible = true,
              requireTypeIdForSubtypes = OptBoolean.FALSE)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DataContract.class, name = BitolDocument.DATA_CONTRACT_KIND),
        @JsonSubTypes.Type(value = DataProduct.class,  name = BitolDocument.DATA_PRODUCT_KIND)
})
public abstract class BitolDocument
{
    /**
     * The value of the kind property for an Open Data Contract Standard (ODCS) document.
     */
    public static final String DATA_CONTRACT_KIND = "DataContract";

    /**
     * The value of the kind property for an Open Data Product Standard (ODPS) document.
     */
    public static final String DATA_PRODUCT_KIND = "DataProduct";


    private String                             apiVersion = null;
    private String                             kind = null;
    private String                             id = null;
    private String                             name = null;
    private String                             version = null;
    private String                             status = null;
    private String                             domain = null;
    private String                             tenant = null;
    private BitolDescription                   description = null;
    private List<String>                       tags = null;
    private List<BitolCustomProperty>          customProperties = null;
    private List<BitolAuthoritativeDefinition> authoritativeDefinitions = null;


    /**
     * Default constructor
     */
    public BitolDocument()
    {
    }


    /**
     * Constructor used by the subclasses to set the kind and the default apiVersion.
     *
     * @param kind kind of document
     * @param apiVersion default version of the standard
     */
    protected BitolDocument(String kind,
                            String apiVersion)
    {
        this.kind       = kind;
        this.apiVersion = apiVersion;
    }


    /**
     * Return the version of the Bitol standard that this document conforms to (for example v3.1.0 for ODCS or v1.0.0 for ODPS).
     *
     * @return string version
     */
    public String getApiVersion()
    {
        return apiVersion;
    }


    /**
     * Set up the version of the Bitol standard that this document conforms to (for example v3.1.0 for ODCS or v1.0.0 for ODPS).
     *
     * @param apiVersion string version
     */
    public void setApiVersion(String apiVersion)
    {
        this.apiVersion = apiVersion;
    }


    /**
     * Return the kind of document: either DataContract or DataProduct.
     *
     * @return string kind
     */
    public String getKind()
    {
        return kind;
    }


    /**
     * Set up the kind of document: either DataContract or DataProduct.
     *
     * @param kind string kind
     */
    public void setKind(String kind)
    {
        this.kind = kind;
    }


    /**
     * Return the unique identifier of the document, typically a UUID.  It is stable across versions of the document.
     *
     * @return string identifier
     */
    public String getId()
    {
        return id;
    }


    /**
     * Set up the unique identifier of the document, typically a UUID.  It is stable across versions of the document.
     *
     * @param id string identifier
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Return the name of the data contract or data product.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the data contract or data product.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the version of the data contract or data product (typically semantic versioning).
     *
     * @return string version
     */
    public String getVersion()
    {
        return version;
    }


    /**
     * Set up the version of the data contract or data product (typically semantic versioning).
     *
     * @param version string version
     */
    public void setVersion(String version)
    {
        this.version = version;
    }


    /**
     * Return the current status of the data contract or data product.  Standard values are defined in BitolStatus.
     *
     * @return string status
     */
    public String getStatus()
    {
        return status;
    }


    /**
     * Set up the current status of the data contract or data product.  Standard values are defined in BitolStatus.
     *
     * @param status string status
     */
    public void setStatus(String status)
    {
        this.status = status;
    }


    /**
     * Return the name of the logical data (business) domain.
     *
     * @return string domain name
     */
    public String getDomain()
    {
        return domain;
    }


    /**
     * Set up the name of the logical data (business) domain.
     *
     * @param domain string domain name
     */
    public void setDomain(String domain)
    {
        this.domain = domain;
    }


    /**
     * Return the organization (tenant) that the data is primarily associated with.
     *
     * @return string tenant name
     */
    public String getTenant()
    {
        return tenant;
    }


    /**
     * Set up the organization (tenant) that the data is primarily associated with.
     *
     * @param tenant string tenant name
     */
    public void setTenant(String tenant)
    {
        this.tenant = tenant;
    }


    /**
     * Return the high level description of the document (purpose, limitations, usage).
     *
     * @return description structure
     */
    public BitolDescription getDescription()
    {
        return description;
    }


    /**
     * Set up the high level description of the document (purpose, limitations, usage).
     *
     * @param description description structure
     */
    public void setDescription(BitolDescription description)
    {
        this.description = description;
    }


    /**
     * Return the list of tags attached to this element.
     *
     * @return list of tag strings
     */
    public List<String> getTags()
    {
        return tags;
    }


    /**
     * Set up the list of tags attached to this element.
     *
     * @param tags list of tag strings
     */
    public void setTags(List<String> tags)
    {
        this.tags = tags;
    }


    /**
     * Return the list of custom (key/value) properties attached to this element.
     *
     * @return list of custom properties
     */
    public List<BitolCustomProperty> getCustomProperties()
    {
        return customProperties;
    }


    /**
     * Set up the list of custom (key/value) properties attached to this element.
     *
     * @param customProperties list of custom properties
     */
    public void setCustomProperties(List<BitolCustomProperty> customProperties)
    {
        this.customProperties = customProperties;
    }


    /**
     * Return the list of links to sources that provide more details about this element.
     *
     * @return list of authoritative definitions
     */
    public List<BitolAuthoritativeDefinition> getAuthoritativeDefinitions()
    {
        return authoritativeDefinitions;
    }


    /**
     * Set up the list of links to sources that provide more details about this element.
     *
     * @param authoritativeDefinitions list of authoritative definitions
     */
    public void setAuthoritativeDefinitions(List<BitolAuthoritativeDefinition> authoritativeDefinitions)
    {
        this.authoritativeDefinitions = authoritativeDefinitions;
    }


    /**
     * Return whether the apiVersion of this document is one that Egeria's beans are able to represent.
     *
     * @return boolean flag
     */
    public abstract boolean hasSupportedApiVersion();


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BitolDocument{" +
                       "apiVersion='" + apiVersion + '\'' +
                       ", kind='" + kind + '\'' +
                       ", id='" + id + '\'' +
                       ", name='" + name + '\'' +
                       ", version='" + version + '\'' +
                       ", status='" + status + '\'' +
                       ", domain='" + domain + '\'' +
                       ", tenant='" + tenant + '\'' +
                       ", description=" + description +
                       ", tags=" + tags +
                       ", customProperties=" + customProperties +
                       ", authoritativeDefinitions=" + authoritativeDefinitions +
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
        BitolDocument that = (BitolDocument) objectToCompare;
        return Objects.equals(apiVersion, that.apiVersion) &&
                       Objects.equals(kind, that.kind) &&
                       Objects.equals(id, that.id) &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(version, that.version) &&
                       Objects.equals(status, that.status) &&
                       Objects.equals(domain, that.domain) &&
                       Objects.equals(tenant, that.tenant) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(tags, that.tags) &&
                       Objects.equals(customProperties, that.customProperties) &&
                       Objects.equals(authoritativeDefinitions, that.authoritativeDefinitions);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(apiVersion, kind, id, name, version, status, domain, tenant, description, tags, customProperties, authoritativeDefinitions);
    }
}
