/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useContext } from "react";

import { GlossaryAuthorContext } from "../contexts/GlossaryAuthorContext";

export async function issueRestGet(url, onSuccessful, onError ) {
  try {
    const response = await fetch(url, {
      method: "get",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    });
    const json = await response.json();

    if (json.relatedHTTPCode == 200 && json.result) {
      let msg = "";
      if (json.result) {
        onSuccessful(json);
      } else if (json.relatedHTTPCode) {
        if (json.exceptionUserAction) {
          msg = "Get Failed: " + json.exceptionUserAction;
        } else {
          msg =
            "Get Failed unexpected Egeria response: " +
            JSON.stringify(json);
        }
      } else if (response.errno) {
        if (response.errno == "ECONNREFUSED") {
          msg = "Connection refused to the view server.";
        } else {
          // TODO create nice messages for all the http codes we think are relevant
          msg = "Get Failed with http errno " + response.errno;
        }
      } else {
        msg = "Get Failed - unexpected response" + JSON.stringify(res);
      }
      onError(msg);
    }
  } catch (msg) {
    onError(msg);
  } 
}

export async function issueRestCreate(url, body, onSuccessful, onError) {
  try {
    const response = await fetch(url, {
      method: "post",
      headers: {
        Accept: "application/json",
       "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
    });
    const json = await response.json();
    let msg;
    if (json.relatedHTTPCode == 200 && json.result && json.result[0]) {
        onSuccessful(json);
    } else if (json.relatedHTTPCode) {
        if (json.exceptionUserAction) {
          msg = "Create Failed: " + json.exceptionUserAction;
        } else {
          msg =
            "Create Failed unexpected Egeria response: " +
            JSON.stringify(json);
        }
    } else if (response.errno) {
        if (response.errno == "ECONNREFUSED") {
          msg = "Connection refused to the view server.";
        } else {
          // TODO create nice messages for all the http codes we think are relevant
          msg = "Create Failed with http errno " + response.errno;
        }
    } else {
        msg = "Create Failed - unexpected response" + JSON.stringify(res);
    }
    if (msg) {
      onError(msg);
    }
  } catch (msg) {
    onError(msg);
  } 
}

export async function issueRestRestDelete(deleteUrl, onSuccessful, onError ) {
  try {
    const response = await fetch(fetchUrl, {
      method: "delete",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    });
    const json = await response.json();
    // for delete there is no response
    if (json.relatedHTTPCode == 200) {
      let msg;

      if (json.relatedHTTPCode) {
        if (json.exceptionUserAction) {
          msg = "Delete Failed: " + json.exceptionUserAction;
        } else {
          msg =
            "Delete Failed unexpected Egeria response: " +
            JSON.stringify(json);
        }
      } else if (response.errno) {
        if (response.errno == "ECONNREFUSED") {
          msg = "Connection refused to the view server.";
        } else {
          // TODO create nice messages for all the http codes we think are relevant
          msg = "Delete Failed with http errno " + response.errno;
        }
      } else {
        onSuccessful(json);
      }
      if (msg) {
        onError(msg);
      }
    }
  } catch (msg) {
    onError(msg);
  }     
  
}

export async function  issueUpdate(url, body, onSuccessful, onError) {
  try {
    const response = await fetch(url, {
      method: "put",
      headers: {
        Accept: "application/json",
       "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
    });
    const json = await response.json();
    let msg;
    if (json.relatedHTTPCode == 200 && json.result && json.result[0]) {
        onSuccessful(json);
    } else if (json.relatedHTTPCode) {
        if (json.exceptionUserAction) {
          msg = "Update Failed: " + json.exceptionUserAction;
        } else {
          msg =
            "Update Failed unexpected Egeria response: " +
            JSON.stringify(json);
        }
    } else if (response.errno) {
        if (response.errno == "ECONNREFUSED") {
          msg = "Connection refused to the view server.";
        } else {
          // TODO create nice messages for all the http codes we think are relevant
          msg = "Delete Failed with http errno " + response.errno;
        }
    } else {
        msg = "Delete Failed - unexpected response" + JSON.stringify(res);
    }
    if (msg) {
      onError(msg);
    }
  } catch (msg) {
    onError(msg);
  } 
}
