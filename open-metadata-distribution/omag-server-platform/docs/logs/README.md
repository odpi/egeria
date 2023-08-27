<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Debug logs and trace

The `logs` directory is a suggested location for logging data from the platform.
This can be activated by adding a `logback.xml` file to the `extra` directory.
There is a sample `logback.xml` in the `etc` directory.  Information on
setting up logging can be found
[on the Egeria website](https://egeria-project.org/guides/admin/configuring-the-omag-server-platform/#logging).

Logging can generate a large volume of output.  You may choose to have the log
files write directly into this directory, or use it as a mount point for
external storage.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.