<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Egeria helm charts for Kubernetes

This directory currently contains two helm charts for Egeria

## odpi-egeria-lab

This directory contains the 'lab' helm chart which creates a tutorial environment intended to show how
Egeria can be used to support the metadata needs of a small, hypothetical, pharmaceutical company known
as Coco Pharmecuticals. Configuration & demonstration is done through Jupyter Notebooks and Python.

Please refer to [odpi-egeria-lab/README.md] for more detailed information

## egeria-base

This directory contains a simpler helm chart which creates a basic egeria environment with
a single server preconfigured. This is a simpler environment than we use for coco, but is likely
useful for experimenting further with Egeria once you understand the tutorials.

Please refer to [egeria-base/README.md] for more detailed information

## Additional Kubernetes related content

Other helm charts can be found in the new egeria-samples respository at https://github.com/odpi/egeria-samples/helm-charts

See also the https://github.com/odpi/egeria-k8s-operator repository for an alternative approach to kubernetes deployment


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
