/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

function processErrorJson(operationName, json, response) {
  const relatedHTTPCode = json.relatedHTTPCode;
  let msg = operationName + "Unexpected response.";
  if (relatedHTTPCode) {
    if (json.exceptionUserAction) {
      msg = json.exceptionUserAction;
    } else if (relatedHTTPCode >= 300 && relatedHTTPCode <= 399) {
      msg =
        operationName +
        "Client error.";
    } else if (relatedHTTPCode >= 400 && relatedHTTPCode <= 499) {
      msg = "Server error.";
    } else {
      msg = "Http error code " + relatedHTTPCode + ".";
    }
  } else if (response.errno) {
    if (response.errno === "ECONNREFUSED") {
      msg = "Connection refused to the view server.";
    } else {
      // TODO create nice messages for all the http codes we think are relevant
      msg = "Http errno " + response.errno;
    }
  } else {
    msg = "Unexpected response" + JSON.stringify(response);
  }
  msg =
    operationName +
    " failed. " +
    msg +
    " Contact your administrator to review the server logs.";
  return msg;
}
export async function issueRestGet(url, onSuccessful, onError) {
  try {
    const response = await fetch(url, {
      method: "get",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    });
    const json = await response.json();
    console.log("issueRestGet complete");
    let msg;

    if (json.relatedHTTPCode === 200 && json.result) {
      if (json.result) {
        onSuccessful(json);
      } else {
        // got nothing
        msg =
          "Error. Get request succeded but there were no results. Contact your administrator to review the server logs for errors.";
      }
    } else {
      msg = processErrorJson("Get", json, response);
    }
    if (msg) {
      onError(msg);
    }
  } catch (msg) {
    onError(msg);
  }
}

export async function issueRestCreate(url, body, onSuccessful, onError) {
  console.log("issueRestCreate");
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
    const relatedHTTPCode = json.relatedHTTPCode;
    let msg;
    if (relatedHTTPCode === 200 && json.result) {
      if (json.result) {
        onSuccessful(json);
      } else {
        // got nothing
        msg =
          "Error. Create request succeded but there were no results. Contact your administrator to review the server logs for errors.";
      }
    } else {
      msg = processErrorJson("Create", json, response);
    }
    if (msg) {
      onError(msg);
    }
  } catch (msg) {
    onError(msg);
  }
}

export async function issueRestDelete(deleteUrl, onSuccessful, onError) {
  try {
    const response = await fetch(deleteUrl, {
      method: "delete",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    });
    await response.json();
    // no response from a delete so no json to return or look for errors in 
    onSuccessful();
  } catch (msg) {
    onError(msg);
  }
}

export async function issueRestUpdate(url, body, onSuccessful, onError) {
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
    const relatedHTTPCode = json.relatedHTTPCode;
    if (relatedHTTPCode === 200 && json.result) {
      if (json.result) {
        onSuccessful(json);
      } else {
        // got nothing
        msg =
          "Error. Update request succeded but there were no results. Contact your administrator to review the server logs for errors.";
      }
    } else {
      msg = processErrorJson("Update", json, response);
    }
    if (msg) {
      onError(msg);
    }
  } catch (msg) {
    onError(msg);
  }
}
