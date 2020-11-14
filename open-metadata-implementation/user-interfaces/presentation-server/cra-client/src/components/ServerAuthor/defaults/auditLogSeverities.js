/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// Information - The server is providing information about its normal operation.
// Event - An event was received from another member of the open metadata repository cohort.
// Decision - A decision has been made related to the interaction of the local metadata repository and the rest of the cohort.
// Action - An Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken.
// Error - An error occurred, possibly caused by an incompatibility between the local metadata repository and one of the remote repositories. The local repository may restrict some of the metadata interchange functions as a result.
// Exception - An unexpected exception occurred. This means that the server needs some administration attention to correct configuration or fix a logic error because it is not operating as a proper peer in the open metadata repository cohort.
// Security - Unauthorized access to a service or metadata instance has been attempted.
// Startup - A new component is starting up.
// Shutdown - An existing component is shutting down.
// Asset - An auditable action relating to an asset has been taken.
// Types - Activity is occurring that relates to the open metadata types in use by this server.
// Cohort - The server is exchanging registration information about an open metadata repository cohort that it is connecting to.
// Trace - This is additional information on the operation of the server that may be of assistance in debugging a problem. It is not normally logged to any destination, but can be added when needed.,
// PerfMon - This log record contains performance monitoring timing information for specific types of processing. It is not normally logged to any destination, but can be added when needed.
// <Unknown> - Uninitialized Severity

const auditLogSeverities = [
  {
    id: "Information",
    label: "Information"
  },
  {
    id: "Event",
    label: "Event"
  },
  {
    id: "Decision",
    label: "Decision"
  },
  {
    id: "Action",
    label: "Action"
  },
  {
    id: "Error",
    label: "Error"
  },
  {
    id: "Exception",
    label: "Exception"
  },
  {
    id: "Security",
    label: "Security"
  },
  {
    id: "Startup",
    label: "Startup"
  },
  {
    id: "Shutdown",
    label: "Shutdown"
  },
  {
    id: "Asset",
    label: "Asset"
  },
  {
    id: "Types",
    label: "Types"
  },
  {
    id: "Cohort",
    label: "Cohort"
  },
  {
    id: "Trace",
    label: "Trace"
  },
  {
    id: "PerfMon",
    label: "PerfMon"
  },
  {
    id: "<Unknown>",
    label: "<Unknown>"
  }
]

export default auditLogSeverities;