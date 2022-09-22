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
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * GovernanceDefinitionGraph documents the linked governance definitions of the governance program.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceDefinitionGraph extends GovernanceDefinitionElement
{
    private static final long    serialVersionUID = 1L;

    private List<RelatedElement>           parents            = null;
    private List<RelatedElement>           peers              = null;
    private List<RelatedElement>           children           = null;
    private List<RelatedElement>           metrics            = null;
    private List<RelatedElement>           externalReferences = null;
    private List<RelatedElement>           others             = null;


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
        super (template);

        if (template != null)
        {
            parents = template.getParents();
            peers = template.getPeers();
            children = template.getChildren();
            metrics = template.getMetrics();
            externalReferences = template.getExternalReferences();
            others = template.getOthers();
        }
    }


    /**
     * Return the list of governance definitions that are requirements for this governance definition.
     *
     * @return list of governance definition stubs
     */
    public List<RelatedElement> getParents()
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
    public void setParents(List<RelatedElement> parents)
    {
        this.parents = parents;
    }


    /**
     * Return the list of peer governance definitions that are related to this governance definition.
     *
     * @return list of governance definition stubs
     */
    public List<RelatedElement> getPeers()
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
    public void setPeers(List<RelatedElement> peers)
    {
        this.peers = peers;
    }


    /**
     * Return the governance definitions that support this governance definition.
     *
     * @return list of governance definition stubs
     */
    public List<RelatedElement> getChildren()
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
    public void setChildren(List<RelatedElement> children)
    {
        this.children = children;
    }


    /**
     * Return the governance metrics that measure this governance definition.
     *
     * @return list of governance definition stubs
     */
    public List<RelatedElement> getMetrics()
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
    public void setMetrics(List<RelatedElement> metrics)
    {
        this.metrics = metrics;
    }


    /**
     * Return details of the external references that have been linked to this governance definition.
     *
     * @return list of links to external references
     */
    public List<RelatedElement> getExternalReferences()
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
    public void setExternalReferences(List<RelatedElement> externalReferences)
    {
        this.externalReferences = externalReferences;
    }


    /**
     * Return details of other relationships
     *
     * @return details of other related elements
     */
    public List<RelatedElement> getOthers()
    {
        return others;
    }


    /**
     * Set details of other relationships.
     *
     * @param others details of other related elements
     */
    public void setOthers(List<RelatedElement> others)
    {
        this.others = others;
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
                       "relatedElement=" + getRelatedElement() +
                       ", elementHeader=" + getElementHeader() +
                       ", properties=" + getProperties() +
                       ", parents=" + parents +
                       ", peers=" + peers +
                       ", children=" + children +
                       ", metrics=" + metrics +
                       ", externalReferences=" + externalReferences +
                       ", others=" + others +
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
        if (! (objectToCompare instanceof GovernanceDefinitionGraph))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        GovernanceDefinitionGraph that = (GovernanceDefinitionGraph) objectToCompare;
        return Objects.equals(parents, that.parents) && Objects.equals(peers, that.peers) &&
                       Objects.equals(children, that.children) &&
                       Objects.equals(metrics, that.metrics) &&
                       Objects.equals(others, that.others) &&
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
        return Objects.hash(super.hashCode(), parents, peers, children, metrics, externalReferences, others);
    }
}
