<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Labs

The open metadata labs contain an interactive environment that allow you to
experiment with different capabilities of ODPi Egeria.  As such we often refer to them as
**Hands-on Labs**.

Each lab notebook describes a scenario from the
[Coco Pharmaceuticals](https://github.com/odpi/data-governance/tree/master/docs/coco-pharmaceuticals)
case study, focusing on a challenge that one or more of the characters face and
how they approached the solution.

The calls to the Egeria APIs necessary to complete the challenge are encoded in
the [notebook](../../developer-resources/tools/Jupyter-Notebooks.md) so you can experiment with the APIs.

These labs can be used for individual study, as part of a class and / or
as the basis of a workbook for using Egeria within a specific organization.

## Running the Labs

There are three main ways to set up the software to run these labs.  These are listed below.
They each create exactly the same environment that supports the labs. 

The easiest option is listed first ...

* [Using Docker Compose](../open-metadata-tutorials/lab-infrastructure-guide/running-docker-compose.md) to run them in a
self-contained environment on your own system.
* [Using Kubernetes](../open-metadata-tutorials/lab-infrastructure-guide/running-kubernetes.md) to run them in a more
flexible, possibly public cloud self-contained environment.
* [Using your own local environment directly](../open-metadata-tutorials/lab-infrastructure-guide/running-natively.md).

Once the software is in place, you then go to the `JupyterLab` browser window 
(typically at `http://localhost:8888/lab`) and begin with the
[read-me-first.ipynb](./read-me-first.ipynb) lab notebook to familiarize yourself with the tutorial tools. 
This notebook will guide you to the rest of the labs.


![Read me first Jupyter Notebook](../../developer-resources/tools/jupyter-notebook-browser-window.png#pagewidth)


You can start running a notebook by simply double-clicking the filename in the left pane of the Jupyter interface.

----
* Return to [open-metadata-resources](..)
* Return to [Site Map](../../Content-Organization.md)
* Return to [Home Page](../../index.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
