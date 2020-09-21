/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import { useContext } from "react";
import { IdentificationContext } from "../../../../contexts/IdentificationContext";

const getLineType = (key) => {
  const identificationContext = useContext(IdentificationContext);

  const LineTypes = {
    termAnchor: {
      key: "termAnchor",
      plural: "termAnchors",
      typeName: "TermAnchor",
      attributes: [
        {
          key: "description",
          label: "Description",
        },
        {
          key: "steward",
          label: "Steward",
        },
        {
          key: "source",
          label: "Source",
        },
        {
          key: "status",
          label: "Status",
          // TODO implement enum values
        },
      ],
      summaryResponseAttributes: [
        {
          key: "guid",
          label: "Guid",
        },
      ],
    },
  };
  hasA: {
    key: "has-a",
    plural: "has-a",
    typeName: "HasA",
    attributes: [
      "description",
      "status",
      "steward",
      "source",
      {
        key: "description",
        label: "Description",
      },
      {
        key: "steward",
        label: "Steward",
      },
      {
        key: "source",
        label: "Source",
      },
      {
        key: "status",
        label: "Status",
        // TODO implement enum values
      },
    ],
    summaryResponseAttributes: [
      {
        key: "guid",
        label: "Guid",
      },
    ],
  },
};

  let lineType = LineTypes[key];
  if (lineType) {
    lineType.url =
      identificationContext.getRestURL("glossary-author") +
      "/" +
      lineType.plural;
  } else {
    lineType = nodeTypes["unSet"];
  }
  return lineType;
};

export default getLineType;
