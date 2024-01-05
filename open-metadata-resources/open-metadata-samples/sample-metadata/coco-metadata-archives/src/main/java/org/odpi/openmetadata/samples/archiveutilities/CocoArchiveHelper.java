/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities;

import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumElementDef;

import java.util.Date;

/**
 * CocoArchiveHelper extends the archive helpers provided by core egeria (egeria.git).
 */
public class CocoArchiveHelper extends GovernanceArchiveHelper
{
    /**
     * Constructor passes parameters used to build the open metadata archive's property header.
     * This version is used for multiple dependant archives, and they need to share the guid map.
     *
     * @param archiveBuilder builder where content is cached
     * @param archiveGUID unique identifier for this open metadata archive.
     * @param archiveRootName non-spaced root name of the open metadata archive elements.
     * @param originatorName name of the originator (person or organization) of the archive.
     * @param creationDate data that this archive was created.
     * @param versionNumber version number of the archive.
     * @param versionName version name for the archive.
     * @param guidMapFileName name of the guid map file.
     */
    public CocoArchiveHelper(OpenMetadataArchiveBuilder archiveBuilder,
                             String                     archiveGUID,
                             String                     archiveRootName,
                             String                     originatorName,
                             Date                       creationDate,
                             long                       versionNumber,
                             String                     versionName,
                             String                     guidMapFileName)
    {
        super(archiveBuilder, archiveGUID, archiveRootName, originatorName, creationDate, versionNumber, versionName, guidMapFileName);
    }


    /**
     * Add the DeployedOn relationship to the archive.
     *
     * @param deployedElementQName qualified name of element being deployed
     * @param deployedOnQName qualified name of target
     * @param deploymentTime time of the deployment
     * @param deployerTypeName type name of the element representing the deployer
     * @param deployerPropertyName property name used to identify the deployer
     * @param deployer identifier of the deployer
     * @param deploymentStatus status of the deployment
     */
    public void addDeployedOnRelationship(String deployedElementQName,
                                          String deployedOnQName,
                                          Date   deploymentTime,
                                          String deployerTypeName,
                                          String deployerPropertyName,
                                          String deployer,
                                          int    deploymentStatus)
    {
        final String methodName = "addDeployedOnRelationship";
        final String operationStatus = "OperationalStatus";

        String deployedElementId = this.idToGUIDMap.getGUID(deployedElementQName);
        String deployedOnId = this.idToGUIDMap.getGUID(deployedOnQName);

        EntityProxy end1    = this.archiveHelper.getEntityProxy(this.archiveBuilder.getEntity(deployedElementId));
        EntityProxy end2    = this.archiveHelper.getEntityProxy(this.archiveBuilder.getEntity(deployedOnId));

        EnumElementDef statusEnumElement = archiveHelper.getEnumElement(operationStatus, deploymentStatus);

        InstanceProperties properties = archiveHelper.addDatePropertyToInstance(archiveRootName, null, "deploymentTime", deploymentTime, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, "deployerTypeName", deployerTypeName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, "deployerPropertyName", deployerPropertyName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, "deployer", deployer, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, "deploymentStatus", statusEnumElement.getOrdinal(), statusEnumElement.getValue(), statusEnumElement.getDescription(), methodName);

        this.archiveBuilder.addRelationship(this.archiveHelper.getRelationship("DeployedOn", this.idToGUIDMap.getGUID(deployedElementId + "_to_" + deployedOnId + "_deployed_on_relationship"), properties, InstanceStatus.ACTIVE, end1, end2));
    }


    /**
     * Add the DeployedOn relationship to the archive.
     *
     * @param deployedElementQName qualified name of element being deployed
     * @param deployedOnQName qualified name of target
     * @param deploymentTime time of the deployment
     * @param deployerTypeName type name of the element representing the deployer
     * @param deployerPropertyName property name used to identify the deployer
     * @param deployer identifier of the deployer
     * @param serverCapabilityStatus status of the deployment
     */
    public void addSupportedSoftwareCapabilityRelationship(String deployedElementQName,
                                                           String deployedOnQName,
                                                           Date   deploymentTime,
                                                           String deployerTypeName,
                                                           String deployerPropertyName,
                                                           String deployer,
                                                           int    serverCapabilityStatus)
    {
        final String methodName = "addSupportedSoftwareCapabilityRelationship";
        final String operationStatus = "OperationalStatus";

        String deployedElementId = this.idToGUIDMap.getGUID(deployedElementQName);
        String deployedOnId = this.idToGUIDMap.getGUID(deployedOnQName);

        EntityProxy end1    = this.archiveHelper.getEntityProxy(this.archiveBuilder.getEntity(deployedElementId));
        EntityProxy end2    = this.archiveHelper.getEntityProxy(this.archiveBuilder.getEntity(deployedOnId));

        EnumElementDef statusEnumElement = archiveHelper.getEnumElement(operationStatus, serverCapabilityStatus);

        InstanceProperties properties = archiveHelper.addDatePropertyToInstance(archiveRootName, null, "deploymentTime", deploymentTime, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, "deployerTypeName", deployerTypeName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, "deployerPropertyName", deployerPropertyName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, "deployer", deployer, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, "softwareServerStatus", statusEnumElement.getOrdinal(), statusEnumElement.getValue(), statusEnumElement.getDescription(), methodName);

        this.archiveBuilder.addRelationship(this.archiveHelper.getRelationship("SupportedSoftwareCapability", this.idToGUIDMap.getGUID(deployedElementId + "_to_" + deployedOnId + "_supported_software-capability_relationship"), properties, InstanceStatus.ACTIVE, end1, end2));
    }


    /**
     * Add a ServerPurpose classification to an IT Infrastructure element.
     *
     * @param elementGUID element to attach the classification
     * @param classificationName name of the classification
     * @param deployedImplementationType deployed implementation type property
     */
    public void addServerPurposeClassification(String elementGUID,
                                               String classificationName,
                                               String deployedImplementationType)
    {
        final String methodName = "addServerPurposeClassification";

        EntityDetail   assetEntity    = this.archiveBuilder.getEntity(elementGUID);
        EntityProxy    entityProxy    = this.archiveHelper.getEntityProxy(assetEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, "deployedImplementationType", deployedImplementationType, methodName);

        Classification classification = this.archiveHelper.getClassification(classificationName, properties, InstanceStatus.ACTIVE);
        this.archiveBuilder.addClassification(this.archiveHelper.getClassificationEntityExtension(entityProxy, classification));
    }
}
