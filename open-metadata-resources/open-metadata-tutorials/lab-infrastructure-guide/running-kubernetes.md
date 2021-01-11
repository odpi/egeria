<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Running Egeria in a self-contained environment using Kubernetes


With this approach, you do not need to download, install, configure and understand all of the underlying components
that Egeria itself uses.

## Kubernetes
[Kubernetes](https://kubernetes.io/) is an open platform for managing complex, containerized workloads. 

You can get access to a Kubernetes environment (cluster) through many major cloud providers. 

You can also install Kubernetes locally:
 * For Windows & MacOS the easiest way is to [download docker desktop](https://hub.docker.com/?overlay=onboarding). After install click on the Docker icon, go to 'Preferences' and under 'resources' ensure you have at least 6GB dedicated to Docker. Then click on 'Kubernetes' and check the checkbox to enable.
 * For Linux there is no easy installer, but there are many options. These also exist for Windows, Mac & indeed Raspberry Pi. Search for topics like `okd`,`minishift`,`microk8s`, `k3s` . 


The remainder of these instructions assume that you have a Kubernetes (k8s) cluster configured, and 'kubectl' available. 

## Helm

[Helm](https://helm.sh/) is a packaging tool that helps in deploying applications to Kubernetes & is required by Egeria.

Install [Helm v3.1 or above](https://github.com/helm/helm/releases) & ensure it is in your path.


## Running the environment

1. Download the latest [Egeria release](https://github.com/odpi/egeria/releases) & unpack
   
1. Navigate to the `open-metadata-resources/open-metadata-deployment/charts` directory
   
1. First we need to retrieve dependencies we use for Egeria:

    ```bash
    $ helm dep update odpi-egeria-lab
    ```
    
1. We can now deploy the lab environment:

    ```bash
    $ helm install lab odpi-egeria-lab
    ```

1. By default the Jupyter notebook is available on port 30888 and is defined as a `NodePort`. This means you need to know the
   IP address or DNS name of one of your Kubernetes worker nodes. If we call this 'mycloud-service.com' you would now get to the tutorials via `http://mycloud-service.com:30888`
   
1. To remove the lab environment

    ```bash
    $ helm delete lab
    ```

**Important note**: Be aware that each time you run the environment it will reset to a clean tutorial. So if you make
any changes within the Notebooks that you want to keep, be sure to save these to a local file on your computer before
shutting down.

## Help

For additional help refer to our slack channels at http://slack.lfai.foundation

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
