/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDefinitionProperties;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceDefinitionGraph documents the linked governance definitions of the governance program.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceDefinitionGraph implements Serializable, MetadataElement
{
    private static final long    serialVersionUID = 1L;

    private ElementHeader                  elementHeader      = null;
    private GovernanceDefinitionProperties properties         = null;
    private List<GovernanceDelegation>     parents            = null;
    private List<GovernanceLink>           peers              = null;
    private List<GovernanceDelegation>     children           = null;
    private List<GovernanceDelegation>     metrics            = null;
    private List<ExternalReferenceElement> externalReferences = null;


    /**
     * Default Constructor
     */
    public GovernanceDefinitionGraph()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceDefinitionGraph(GovernanceDefinitionGraph template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = template.getProperties();
            parents = template.getParents();
            peers = template.getPeers();
            children = template.getChildren();
            metrics = template.getMetrics();
            externalReferences = template.getExternalReferences();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties of this governance definition.
     *
     * @return properties bean
     */
    public GovernanceDefinitionProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties of this governance definition.
     *
     * @param properties properties bean
     */
    public void setProperties(GovernanceDefinitionProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the list of governance definitions that are requirements for this governance definition.
     *
     * @return list of governance definition stubs
     */
    public List<GovernanceDelegation> getParents()
    {
        if (parents == null)
        {
            return null;
        }
        else if (parents.isEmpty())
        {
            return null;
        }
        else
        {
            return parents;
        }
    }


    /**
     * Set up the list of governance definition that are requirements for this governance definition.
     *
     * @param parents list of governance definition stubs
     */
    public void setParents(List<GovernanceDelegation> parents)
    {
        this.parents = parents;
    }


    /**
     * Return the list of peer governance definitions that are related to this governance definition.
     *
     * @return list of governance definition stubs
     */
    public List<GovernanceLink> getPeers()
    {
        if (peers == null)
        {
            return null;
        }
        else if (peers.isEmpty())
        {
            return null;
        }
        else
        {
            return peers;
        }
    }


    /**
     * Set up the list of governance definitions that are related to this governance definition.
     *
     * @param peers list of governance definition stubs
     */
    public void setPeers(List<GovernanceLink> peers)
    {
        this.peers = peers;
    }


    /**
     * Return the governance definitions that support this governance definition.
     *
     * @return list of governance definition stubs
     */
    public List<GovernanceDelegation> getChildren()
    {
        if (children == null)
        {
            return null;
        }
        else if (children.isEmpty())
        {
            return null;
        }
        else
        {
            return children;
        }
    }


    /**
     * Set up the governance definitions that support this governance definition.
     *
     * @param children list of governance definition stubs
     */
    public void setChildren(List<GovernanceDelegation> children)
    {
        this.children = children;
    }


    /**
     * Return the governance metrics that measure this governance definition.
     *
     * @return list of governance definition stubs
     */
    public List<GovernanceDelegation> getMetrics()
    {
        if (metrics == null)
        {
            return null;
        }
        else if (metrics.isEmpty())
        {
            return null;
        }
        else
        {
            return metrics;
        }
    }


    /**
     * Set up the governance metrics that measure this governance definition.
     *
     * @param metrics list of governance definition stubs
     */
    public void setMetrics(List<GovernanceDelegation> metrics)
    {
        this.metrics = metrics;
    }


    /**
     * Return details of the external references that have been linked to this governance definition.
     *
     * @return list of links to external references
     */
    public List<ExternalReferenceElement> getExternalReferences()
    {
        if (externalReferences == null)
        {
            return null;
        }
        else if (externalReferences.isEmpty())
        {
            return null;
        }
        else
        {
            return externalReferences;
        }
    }


    /**
     * Set up the details of the external references that have been linked to this governance definition.
     *
     * @param externalReferences list of links to external references
     */
    public void setExternalReferneces(List<ExternalReferenceElement> externalReferences)
    {
        this.externalReferences = externalReferences;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "GovernanceDefinitionGraph{" +
                       "elementHeader=" + elementHeader +
                       ", properties=" + properties +
                       ", parents=" + parents +
                       ", peers=" + peers +
                       ", children=" + children +
                       ", metrics=" + metrics +
                       ", externalReferences=" + externalReferences +
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
        GovernanceDefinitionGraph that = (GovernanceDefinitionGraph) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(parents, that.parents) &&
                       Objects.equals(peers, that.peers) &&
                       Objects.equals(children, that.children) &&
                       Objects.equals(metrics, that.metrics) &&
                       Objects.equals(externalReferences, that.externalReferences);
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, properties, parents, peers, children, metrics, externalReferences);
    }
}
