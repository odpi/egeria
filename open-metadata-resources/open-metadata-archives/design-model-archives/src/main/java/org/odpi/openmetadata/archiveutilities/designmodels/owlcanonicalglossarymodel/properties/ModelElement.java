/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.owlcanonicalglossarymodel.properties;

import java.util.*;

/**
 * Attribute describes an attribute in the model
 */
public class ModelElement
{
    private String guid          = null;
    private String id            = null;
    private String displayName   = null;
    private String technicalName = null;
    private String description   = null;
    private String version       = null;
    private Map<String, Link> domainOfLinks    = new HashMap<>();
    private Map<String, Link>      rangeOfLinks     = new HashMap<>();


    public ModelElement()
    {
    }


    public String getGUID()
    {
        return guid;
    }


    public void setGUID(String guid)
    {
        this.guid = guid;
    }


    public String getId()
    {
        return id;
    }


    public void setId(String id)
    {
        this.id = id;
    }


    public String getDisplayName()
    {
        return displayName;
    }


    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    public String getTechnicalName()
    {
        return technicalName;
    }


    public void setTechnicalName(String technicalName)
    {
        this.technicalName = technicalName;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getVersion()
    {
        return version;
    }


    public void setVersion(String version)
    {
        this.version = version;
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


    @Override
    public String toString()
    {
        return "ModelElement{" +
                       "guid='" + guid + '\'' +
                       ", id='" + id + '\'' +
                       ", technicalName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", version='" + version + '\'' +
                       '}';
    }


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
        ModelElement that = (ModelElement) objectToCompare;
        return Objects.equals(guid, that.guid) &&
                       Objects.equals(getId(), that.getId()) &&
                       Objects.equals(getDisplayName(), that.getDisplayName()) &&
                       Objects.equals(getDescription(), that.getDescription()) &&
                       Objects.equals(getVersion(), that.getVersion());
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(guid, getId(), getDisplayName(), getDescription(), getVersion());
    }
}
