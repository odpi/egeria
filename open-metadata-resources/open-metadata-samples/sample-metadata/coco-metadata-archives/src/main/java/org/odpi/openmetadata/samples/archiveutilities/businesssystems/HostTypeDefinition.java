/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.businesssystems;


import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

/**
 * The HostTypeDefinition is used to set up the open metadata type of host.
 */
public enum HostTypeDefinition
{
    /**
     * BareMetalComputer
     */
    BARE_METAL(DeployedImplementationType.BARE_METAL_COMPUTER.getAssociatedTypeName(), DeployedImplementationType.BARE_METAL_COMPUTER.getDeployedImplementationType()),

    /**
     * VirtualMachine
     */
    VIRTUAL_MACHINE(DeployedImplementationType.VIRTUAL_MACHINE.getAssociatedTypeName(), DeployedImplementationType.VIRTUAL_MACHINE.getDeployedImplementationType()),

    /**
     * DockerContainer
     */
    DOCKER_CONTAINER(DeployedImplementationType.DOCKER_CONTAINER.getAssociatedTypeName(), DeployedImplementationType.DOCKER_CONTAINER.getDeployedImplementationType()),

    /**
     * HadoopCluster
     */
    HADOOP_CLUSTER(DeployedImplementationType.HADOOP_CLUSTER.getAssociatedTypeName(), DeployedImplementationType.HADOOP_CLUSTER.getDeployedImplementationType()),

    /**
     * KubernetesCluster
     */
    KUBERNETES_CLUSTER(DeployedImplementationType.KUBERNETES_CLUSTER.getAssociatedTypeName(), DeployedImplementationType.KUBERNETES_CLUSTER.getDeployedImplementationType()),


    ;

    private final String openMetadataTypeName;
    private final String deployedImplementationType;

    /**
     * The constructor creates an instance of the enum
     *
     * @param openMetadataTypeName   unique id for the enum
     */
    HostTypeDefinition(String openMetadataTypeName,
                       String deployedImplementationType)
    {
        this.openMetadataTypeName = openMetadataTypeName;
        this.deployedImplementationType = deployedImplementationType;
    }

    /**
     * This is the name of the open metadata type to use when creating the Host entity.
     *
     * @return string value
     */
    public String getOpenMetadataTypeName()
    {
        return openMetadataTypeName;
    }


    /**
     * This is the name of the deployed implementation type to use when creating the Host entity.
     *
     * @return string value
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }

    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "HostTypeDefinition{" + openMetadataTypeName + '}';
    }
}
