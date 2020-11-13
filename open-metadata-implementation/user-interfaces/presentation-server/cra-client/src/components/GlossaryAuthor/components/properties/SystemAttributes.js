/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";

const getSystemAttributes = () => {
  return {
      attributes: [
                   {
                    "label": "Global unique identifier (guid)",
                    "key": "guid",
                    "type": "text",
                    "url":"aaa",
                    "readonly": true,
                    "object": "systemAttributes"
                   },
                   {
                    "label": "Status",
                    "key": "status",
                    "type": "text",
                    "url":"aaa",
                    "readonly": true,
                    "object": "systemAttributes"
                   },
                   {
                    "label": "Created by",
                    "key":"createdBy",
                    "type": "text",
                    "url":"aaa",
                    "readonly": true,
                    "object": "systemAttributes"
                   },
                   {
                    "label": "Updated by",
                    "key": "updatedBy",
                    "type": "text",
                    "url":"aaa",
                    "readonly": true,
                    "object": "systemAttributes"
                   },
                   {
                    "label": "Create time",
                    "key": "createTime",
                    "type": "date",
                    "url":"aaa",
                    "readonly": true,
                    "object": "systemAttributes"
                   },
                   {
                    "label": "Update time",
                    "key": "updateTime",
                    "type": "date",
                    "url":"aaa",
                    "readonly": true,
                    "object": "systemAttributes"
                   },
                   {
                    "label": "Version",
                    "key": "version",
                    "type": "text",
                    "url": "bbb",
                    "readonly": true,
                    "object": "systemAttributes"
                   }
                  ]
   };
};

export default getSystemAttributes;
