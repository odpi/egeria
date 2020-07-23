<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Building the Egeria Source

When you [download the egeria source from GitHub](task-downloading-egeria-source.md)
a new directory called `egeria` is created that contains all of the source and the build scripts.

The build scripts use [Apache Maven](https://maven.apache.org) to ensure the software is
built in the correct order.  If you change into the `egeria` directory, you will see
a file called `pom.xml`.  This is the file that controls the build.

Issue the following command from the `egeria` directory.

```bash
$ mvn clean install
``` 

The build can take 15-30 minutes depending on the speed and load on your machine.  However eventually you will see the message:

```text
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 14:54 min
[INFO] Finished at: 2020-01-29T09:33:17Z
[INFO] Final Memory: 171M/3510M
[INFO] ------------------------------------------------------------------------

Process finished with exit code 0
```

This means you have completed this tutorial and are ready to [choose the next step](..).

----
* Return to [Tutorial List](..)
* Return to [Egeria Dojo](../egeria-dojo/egeria-dojo-day-2-3-contribution-to-egeria.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.