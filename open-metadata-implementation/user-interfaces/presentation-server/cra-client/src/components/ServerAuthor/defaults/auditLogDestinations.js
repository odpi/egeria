/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

const auditLogDestinations = [
  {
    id: "default",
    label: "Default"
  },
  {
    id: "console",
    label: "Console"
  },
  {
    id: "slf4j",
    label: "Simple Logging Facade for Java (SLF4J)"
  },
  {
    id: "files",
    label: "JSON Files"
  },
  {
    id: "event-topic",
    label: "Event Bus"
  }
]

export default auditLogDestinations;