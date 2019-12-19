<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# User Interface (UI) Server

A **UI server** is a software server that
runs inside the [UI server platform](ui-server-platform.md). 
The UI Server is like the [OMAG Server](../../../../admin-services/docs/concepts/omag-server.md) for the 
user interface.
 

It is started up and shut down using administration commands.
The start up command (called **initialize**) passes a
[configuration document](configuration-document.md) to
the UI Server Platform.
This configuration document defines the properties of the server to be started including
how the services within it are configured.

The shutdown command (called **terminate**) performs an orderly shutdown
of the services within the server and disables its URL.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.