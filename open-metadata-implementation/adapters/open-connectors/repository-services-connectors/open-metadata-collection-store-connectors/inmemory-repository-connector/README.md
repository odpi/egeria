<!-- SPDX-License-Identifier: Apache-2.0 -->
  
# In Memory Repository Connector

The In-memory OMRS Repository Connector provides a simple repository
implementation that "stores" metadata in hash maps within the JVM. 
It is used for testing, or for environments where metadata maintained in other repositories
needs to be cached locally for performance/scalability reasons.