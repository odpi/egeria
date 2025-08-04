/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetElement contains the properties and header for an asset retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetElement extends OpenMetadataRootElement
{
    private List<RelatedMetadataElementSummary> connections                   = null;
    private List<RelatedMetadataElementSummary> hostedITAssets                = null; // DeployedOn
    private List<RelatedMetadataElementSummary> deployedOn                    = null; // DeployedOn
    private List<RelatedMetadataElementSummary> supportedSoftwareCapabilities = null; // SupportedSoftwareCapability
    private List<RelatedMetadataElementSummary> supportedDataSets             = null;
    private List<RelatedMetadataElementSummary> dataSetContent                = null;
    private List<RelatedMetadataElementSummary> visibleEndpoint               = null;
    private List<RelatedMetadataElementSummary> apiEndpoints                  = null;
    private List<RelatedMetadataElementSummary> attachedStorage               = null;
    private List<RelatedMetadataElementSummary> parentProcesses               = null;
    private List<RelatedMetadataElementSummary> childProcesses                = null;
    private List<RelatedMetadataElementSummary> ports                         = null;
    private RelatedMetadataElementSummary       homeFolder                    = null;
    private List<RelatedMetadataElementSummary> nestedFiles                   = null;
    private List<RelatedMetadataElementSummary> linkedFiles                   = null;
    private List<RelatedMetadataElementSummary> linkedFolders                 = null;
    private List<RelatedMetadataElementSummary> linkedMediaFiles              = null;
    private List<RelatedMetadataElementSummary> topicSubscribers              = null;
    private List<RelatedMetadataElementSummary> associatedLogs                = null;
    private List<RelatedMetadataElementSummary> associatedLogSubjects         = null;
    private RelatedMetadataElementSummary       cohortMember                  = null;
    private List<RelatedMetadataElementSummary> archiveContents               = null;


    /**
     * Default constructor
     */
    public AssetElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetElement(AssetElement template)
    {
        super(template);

        if (template != null)
        {
            this.connections                   = template.getConnections();
            this.hostedITAssets                = template.getHostedITAssets();
            this.deployedOn                    = template.getDeployedOn();
            this.supportedSoftwareCapabilities = template.getSupportedSoftwareCapabilities();
            this.supportedDataSets             = template.getSupportedDataSets();
            this.dataSetContent                = template.getDataSetContent();
            this.visibleEndpoint               = template.getVisibleEndpoint();
            this.apiEndpoints                  = template.getApiEndpoints();
            this.attachedStorage               = template.getAttachedStorage();
            this.parentProcesses               = template.getParentProcesses();
            this.childProcesses                = template.getChildProcesses();
            this.ports                         = template.getPorts();
            this.homeFolder                    = template.getHomeFolder();
            this.nestedFiles                   = template.getNestedFiles();
            this.linkedFiles                   = template.getLinkedFiles();
            this.linkedFolders                 = template.getLinkedFolders();
            this.linkedMediaFiles              = template.getLinkedMediaFiles();
            this.topicSubscribers              = template.getTopicSubscribers();
            this.associatedLogs                = template.getAssociatedLogs();
            this.associatedLogSubjects         = template.getAssociatedLogSubjects();
            this.cohortMember                  = template.getCohortMember();
            this.archiveContents               = template.getArchiveContents();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetElement(OpenMetadataRootElement template)
    {
        super(template);
    }


    /**
     * Return the attached connections for this asset.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getConnections()
    {
        return connections;
    }


    /**
     * Set up the attached connections for this asset.
     *
     * @param connections list
     */
    public void setConnections(List<RelatedMetadataElementSummary> connections)
    {
        this.connections = connections;
    }


    /**
     * Return the list of assets deployed to this asset.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getHostedITAssets()
    {
        return hostedITAssets;
    }


    /**
     * Set up which assets are deployed on this asset.
     *
     * @param hostedITAssets list
     */
    public void setHostedITAssets(List<RelatedMetadataElementSummary> hostedITAssets)
    {
        this.hostedITAssets = hostedITAssets;
    }



    public List<RelatedMetadataElementSummary> getDeployedOn()
    {
        return deployedOn;
    }

    public void setDeployedOn(List<RelatedMetadataElementSummary> deployedOn)
    {
        this.deployedOn = deployedOn;
    }

    public List<RelatedMetadataElementSummary> getSupportedSoftwareCapabilities()
    {
        return supportedSoftwareCapabilities;
    }

    public void setSupportedSoftwareCapabilities(List<RelatedMetadataElementSummary> supportedSoftwareCapabilities)
    {
        this.supportedSoftwareCapabilities = supportedSoftwareCapabilities;
    }

    public List<RelatedMetadataElementSummary> getSupportedDataSets()
    {
        return supportedDataSets;
    }

    public void setSupportedDataSets(List<RelatedMetadataElementSummary> supportedDataSets)
    {
        this.supportedDataSets = supportedDataSets;
    }

    public List<RelatedMetadataElementSummary> getDataSetContent()
    {
        return dataSetContent;
    }

    public void setDataSetContent(List<RelatedMetadataElementSummary> dataSetContent)
    {
        this.dataSetContent = dataSetContent;
    }


    public List<RelatedMetadataElementSummary> getVisibleEndpoint()
    {
        return visibleEndpoint;
    }

    public void setVisibleEndpoint(List<RelatedMetadataElementSummary> visibleEndpoint)
    {
        this.visibleEndpoint = visibleEndpoint;
    }

    public List<RelatedMetadataElementSummary> getApiEndpoints()
    {
        return apiEndpoints;
    }

    public void setApiEndpoints(List<RelatedMetadataElementSummary> apiEndpoints)
    {
        this.apiEndpoints = apiEndpoints;
    }

    public List<RelatedMetadataElementSummary> getAttachedStorage()
    {
        return attachedStorage;
    }

    public void setAttachedStorage(List<RelatedMetadataElementSummary> attachedStorage)
    {
        this.attachedStorage = attachedStorage;
    }

    public List<RelatedMetadataElementSummary> getParentProcesses()
    {
        return parentProcesses;
    }

    public void setParentProcesses(List<RelatedMetadataElementSummary> parentProcesses)
    {
        this.parentProcesses = parentProcesses;
    }

    public List<RelatedMetadataElementSummary> getChildProcesses()
    {
        return childProcesses;
    }

    public void setChildProcesses(List<RelatedMetadataElementSummary> childProcesses)
    {
        this.childProcesses = childProcesses;
    }

    public List<RelatedMetadataElementSummary> getPorts()
    {
        return ports;
    }

    public void setPorts(List<RelatedMetadataElementSummary> ports)
    {
        this.ports = ports;
    }

    public RelatedMetadataElementSummary getHomeFolder()
    {
        return homeFolder;
    }

    public void setHomeFolder(RelatedMetadataElementSummary homeFolder)
    {
        this.homeFolder = homeFolder;
    }

    public List<RelatedMetadataElementSummary> getNestedFiles()
    {
        return nestedFiles;
    }

    public void setNestedFiles(List<RelatedMetadataElementSummary> nestedFiles)
    {
        this.nestedFiles = nestedFiles;
    }

    public List<RelatedMetadataElementSummary> getLinkedFiles()
    {
        return linkedFiles;
    }

    public void setLinkedFiles(List<RelatedMetadataElementSummary> linkedFiles)
    {
        this.linkedFiles = linkedFiles;
    }

    public List<RelatedMetadataElementSummary> getLinkedFolders()
    {
        return linkedFolders;
    }

    public void setLinkedFolders(List<RelatedMetadataElementSummary> linkedFolders)
    {
        this.linkedFolders = linkedFolders;
    }

    public List<RelatedMetadataElementSummary> getLinkedMediaFiles()
    {
        return linkedMediaFiles;
    }

    public void setLinkedMediaFiles(List<RelatedMetadataElementSummary> linkedMediaFiles)
    {
        this.linkedMediaFiles = linkedMediaFiles;
    }

    public List<RelatedMetadataElementSummary> getTopicSubscribers()
    {
        return topicSubscribers;
    }

    public void setTopicSubscribers(List<RelatedMetadataElementSummary> topicSubscribers)
    {
        this.topicSubscribers = topicSubscribers;
    }

    public List<RelatedMetadataElementSummary> getAssociatedLogs()
    {
        return associatedLogs;
    }

    public void setAssociatedLogs(List<RelatedMetadataElementSummary> associatedLogs)
    {
        this.associatedLogs = associatedLogs;
    }


    public List<RelatedMetadataElementSummary> getAssociatedLogSubjects()
    {
        return associatedLogSubjects;
    }

    public void setAssociatedLogSubjects(List<RelatedMetadataElementSummary> associatedLogSubjects)
    {
        this.associatedLogSubjects = associatedLogSubjects;
    }

    public RelatedMetadataElementSummary getCohortMember()
    {
        return cohortMember;
    }

    public void setCohortMember(RelatedMetadataElementSummary cohortMember)
    {
        this.cohortMember = cohortMember;
    }

    public List<RelatedMetadataElementSummary> getArchiveContents()
    {
        return archiveContents;
    }

    public void setArchiveContents(List<RelatedMetadataElementSummary> archiveContents)
    {
        this.archiveContents = archiveContents;
    }

    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetElement{" +
                ", connections=" + connections +
                ", hostedITAssets=" + hostedITAssets +
                ", deployedOn=" + deployedOn +
                ", supportedSoftwareCapabilities=" + supportedSoftwareCapabilities +
                ", supportedDataSets=" + supportedDataSets +
                ", dataSetContent=" + dataSetContent +
                ", visibleEndpoint=" + visibleEndpoint +
                ", apiEndpoints=" + apiEndpoints +
                ", attachedStorage=" + attachedStorage +
                ", parentProcesses=" + parentProcesses +
                ", childProcesses=" + childProcesses +
                ", ports=" + ports +
                ", homeFolder=" + homeFolder +
                ", nestedFiles=" + nestedFiles +
                ", linkedFiles=" + linkedFiles +
                ", linkedFolders=" + linkedFolders +
                ", linkedMediaFiles=" + linkedMediaFiles +
                ", topicSubscribers=" + topicSubscribers +
                ", associatedLogs=" + associatedLogs +
                ", associatedLogSubjects=" + associatedLogSubjects +
                ", cohortMember=" + cohortMember +
                ", archiveContents=" + archiveContents +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        AssetElement that = (AssetElement) objectToCompare;
        return Objects.equals(connections, that.connections) &&
                Objects.equals(hostedITAssets, that.hostedITAssets) &&
                Objects.equals(deployedOn, that.deployedOn) &&
                Objects.equals(supportedSoftwareCapabilities, that.supportedSoftwareCapabilities) &&
                Objects.equals(supportedDataSets, that.supportedDataSets) &&
                Objects.equals(dataSetContent, that.dataSetContent) &&
                Objects.equals(visibleEndpoint, that.visibleEndpoint) &&
                Objects.equals(apiEndpoints, that.apiEndpoints) &&
                Objects.equals(attachedStorage, that.attachedStorage) &&
                Objects.equals(parentProcesses, that.parentProcesses) &&
                Objects.equals(childProcesses, that.childProcesses) &&
                Objects.equals(ports, that.ports) &&
                Objects.equals(homeFolder, that.homeFolder) &&
                Objects.equals(nestedFiles, that.nestedFiles) &&
                Objects.equals(linkedFiles, that.linkedFiles) &&
                Objects.equals(linkedFolders, that.linkedFolders) &&
                Objects.equals(linkedMediaFiles, that.linkedMediaFiles) &&
                Objects.equals(topicSubscribers, that.topicSubscribers) &&
                Objects.equals(associatedLogs, that.associatedLogs) &&
                Objects.equals(associatedLogSubjects, that.associatedLogSubjects) &&
                Objects.equals(cohortMember, that.cohortMember) &&
                Objects.equals(archiveContents, that.archiveContents);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), connections, hostedITAssets, deployedOn,
                            supportedSoftwareCapabilities, supportedDataSets, dataSetContent, visibleEndpoint,
                            apiEndpoints, attachedStorage, parentProcesses, childProcesses, ports, homeFolder,
                            nestedFiles, linkedFiles, linkedFolders, linkedMediaFiles, topicSubscribers,
                            associatedLogs, associatedLogSubjects, cohortMember, archiveContents);
    }
}
