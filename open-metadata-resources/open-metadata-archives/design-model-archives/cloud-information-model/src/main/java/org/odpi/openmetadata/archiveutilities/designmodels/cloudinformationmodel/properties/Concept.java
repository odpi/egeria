/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.cloudinformationmodel.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Concept describes a concept in the model.  It may be an entity or a relationship
 */
public class Concept extends ModelElement
{
    private boolean                referenceDataSet = false;
    private boolean                relationship     = false;
    private String                 superClassId     = null;
    private Map<String, String>    subClasses       = new HashMap<>();
    private Map<String, Link>      domainOfLinks    = new HashMap<>();
    private Map<String, Link>      rangeOfLinks     = new HashMap<>();
    private Map<String, Attribute> attributes       = new HashMap<>();


    public Concept(String id)
    {
        super.setId(id);
    }


    public boolean isReferenceDataSet()
    {
        return referenceDataSet;
    }


    public void setReferenceDataSet(boolean referenceDataSet)
    {
        this.referenceDataSet = referenceDataSet;
    }


    public boolean isRelationship()
    {
        return relationship;
    }


    public void setRelationship(boolean relationship)
    {
        this.relationship = relationship;
    }


    public void addSubClass(String id,
                            String subClassId)
    {
        subClasses.put(id, subClassId);
    }


    public List<String> getSubClasses()
    {
        if (subClasses.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(subClasses.values());
        }
    }


    public String getSuperClassId()
    {
        return superClassId;
    }


    public void setSuperClassId(String superClassId)
    {
        this.superClassId = superClassId;
    }


    public void setDomainOfLink(String   linkId,
                                Link     link)
    {
        domainOfLinks.put(linkId, link);
    }

    public Link getDomainLink(String  linkId)
    {
        if (domainOfLinks.isEmpty())
        {
            return null;
        }
        else
        {
            return domainOfLinks.get(linkId);
        }
    }

    public List<Link> getDomainOfLinks()
    {
        if (domainOfLinks.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(domainOfLinks.values());
        }
    }


    public List<String> getDomainOfLinkNames()
    {
        if (domainOfLinks.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(domainOfLinks.keySet());
        }
    }


    public void setRangeOfLink(String  linkId,
                               Link    link)
    {
        rangeOfLinks.put(linkId, link);
    }


    public List<Link> getRangeOfLinks()
    {
        if (rangeOfLinks.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(rangeOfLinks.values());
        }
    }


    public List<String> getRangeOfLinkNames()
    {
        if (rangeOfLinks.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(rangeOfLinks.keySet());
        }
    }


    public Link getRangeLink(String  linkId)
    {
        if (rangeOfLinks.isEmpty())
        {
            return null;
        }
        else
        {
            return rangeOfLinks.get(linkId);
        }
    }

    public void setAttribute(String    attributeId,
                             Attribute attribute)
    {
        attributes.put(attributeId, attribute);
    }


    public Attribute getAttribute(String  attributeId)
    {
        if (attributes.isEmpty())
        {
            return null;
        }
        else
        {
            return attributes.get(attributeId);
        }
    }


    public List<String> getAttributeNames()
    {
        if (attributes.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(attributes.keySet());
        }
    }


    public List<Attribute> getAttributes()
    {
        if (attributes.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(attributes.values());
        }
    }
}
