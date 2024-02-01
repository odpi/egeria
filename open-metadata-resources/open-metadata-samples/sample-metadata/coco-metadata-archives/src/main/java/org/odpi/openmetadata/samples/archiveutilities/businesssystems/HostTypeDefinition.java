/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.businesssystems;


/**
 * The HostTypeDefinition is used to set up the open metadata type of host.
 */
public enum HostTypeDefinition
{
    /**
     * BareMetalComputer
     */
    BARE_METAL("BareMetalComputer"),

    /**
     * VirtualMachine
     */
    VIRTUAL_MACHINE("VirtualMachine"),

    /**
     * DockerContainer
     */
    DOCKER_CONTAINER("DockerContainer"),

    /**
     * HadoopCluster
     */
    HADOOP_CLUSTER("HadoopCluster"),

    /**
     * KubernetesCluster
     */
    KUBERNETES_CLUSTER("KubernetesCluster"),

    /**
     * SoftwareServerPlatform
     */
    SOFTWARE_SERVER_PLATFORM("SoftwareServerPlatform"),
    ;

    private final String openMetadataTypeName;

    /**
     * The constructor creates an instance of the enum
     *
     * @param openMetadataTypeName   unique id for the enum
     */
    HostTypeDefinition(String openMetadataTypeName)
    {
        this.openMetadataTypeName = openMetadataTypeName;
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
