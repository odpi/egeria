/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Concept describes a concept in the model.  It may be an entity or a relationship.
 */
public class Concept extends ModelElement
{
    private boolean relationship   = false;
    private String  superClassGUID = null;
    private String  superClassName = null;

    private final List<String>           subClasses    = new ArrayList<>();
    private final Map<String, Link>      domainOfLinks = new HashMap<>();
    private final Map<String, Link>      rangeOfLinks  = new HashMap<>();
    private final Map<String, Attribute> attributes    = new HashMap<>();

    public Concept()
    {
    }

    public boolean isRelationship()
    {
        return relationship;
    }

    public void setRelationship(boolean relationship)
    {
        this.relationship = relationship;
    }

    public void addSubClass(String subClassTechnicalName)
    {
        subClasses.add(subClassTechnicalName);
    }

    public List<String> getSubClasses()
    {
        if (subClasses.isEmpty())
        {
            return null;
        }
        else
        {
            return subClasses;
        }
    }

    public String getSuperClassGUID()
    {
        return superClassGUID;
    }


    public void setSuperClassGUID(String superClassGUID)
    {
        this.superClassGUID = superClassGUID;
    }


    public String getSuperClassName()
    {
        return superClassName;
    }


    public void setSuperClassName(String superClassName)
    {
        this.superClassName = superClassName;
    }


    public void setDomainOfLink(String   linkTechnicalName,
                                Link     link)
    {
        domainOfLinks.put(linkTechnicalName, link);
    }


    public Link getDomainLink(String  linkTechnicalName)
    {
        if (domainOfLinks.isEmpty())
        {
            return null;
        }
        else
        {
            return domainOfLinks.get(linkTechnicalName);
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


    public void setRangeOfLink(String  linkTechnicalName,
                               Link    link)
    {
        rangeOfLinks.put(linkTechnicalName, link);
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


    public Link getRangeLink(String  linkTechnicalName)
    {
        if (rangeOfLinks.isEmpty())
        {
            return null;
        }
        else
        {
            return rangeOfLinks.get(linkTechnicalName);
        }
    }

    public void setAttribute(String    attributeTechnicalName,
                             Attribute attribute)
    {
        attributes.put(attributeTechnicalName, attribute);
    }


    public Attribute getAttribute(String  attributeTechnicalName)
    {
        return attributes.get(attributeTechnicalName);
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
