<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# IntelliJ IDEA

IntelliJ IDEA by JetBrains is the Interactive Developer Environment (IDE) used by most of the Egeria developers.
The community edition is free to use and covers all of the function needed by an Egeria developer.

Go to 
```
https://www.jetbrains.com/idea/
```
and what the **Take a Tour** video if you are not familiar with IntelliJ.
You can download IntelliJ from this site:
```
https://www.jetbrains.com/idea/download/index.html
```

There is a [tutorial for IntelliJ](../../open-metadata-resources/open-metadata-tutorials/intellij-tutorial) as part of the
[Egeria Dojo](../../open-metadata-resources/open-metadata-tutorials/egeria-dojo).

## Lombok Plugin

Egeria makes use of Project Lombok. If using JetBrains IntelliJ IDEA ensure the IDEA has the required plugin configured. See https://projectlombok.org/setup/intellij for more information.
Also before running a Maven build please choose "Don't detect" from the "Generated sources folders" dropdown in Preferences -> Build, Execution, Deployment -> Build Tools -> Maven -> Importing. This will avoid triggering a duplicate classes build error caused by delomboked sources folder being added as source folder for the Maven module.

If this wasn’t set when your project was initially setup, you may find that ‘delombok’ directories are already present in IntelliJ’s source path for some modules, leading to errors with duplicate classes.

To check for any modules still refering to delombok you can run this at the command line, from your top level source tree:

```
find . -name '*.iml' | xargs -n50 grep -y delombok
```
If you find any hits such as:
```
./open-metadata-implementation/access-services/data-engine/data-engine-api/data-engine-api.iml:      <sourceFolder url="file://$MODULE_DIR$/target/delombok" isTestSource="false" />
```

then either remove those lines WITH INTELLIJ NOT RUNNING, or go into File->Project Structure->Modules, and remove ’target/delombok' from the ‘Source Folders’ list

Explanation - In addition to importing importation on source files from maven, IntelliJ also tries to look for generated source. It finds the delombok directory causing duplicates — in fact we only use this directory for generating javadoc of lombok-enabled modules. Switching the setting/removing these source folders prevents duplicate classes.

----
* Return to [Developer Tools](.)


* Link to [Egeria's Community Guide](../../Community-Guide.md)
* Link to the [Egeria Dojo Education](../../open-metadata-resources/open-metadata-tutorials/egeria-dojo)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
