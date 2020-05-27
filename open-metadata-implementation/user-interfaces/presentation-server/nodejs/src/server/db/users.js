/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
var records = [
    { id: 1, username: 'faith', password: 'admin', displayName: 'Faith Broker', emails: [ { value: 'faith@coco.com' } ] }
  , { id: 2, username: 'callie', password: 'admin', displayName: 'Callie Quartile', emails: [ { value: 'callie@coco.com' } ] }
];

exports.findById = function(id, cb) {
  process.nextTick(function() {
    console.log("findByUserId");
    var idx = id - 1;
    if (records[idx]) {
      cb(null, records[idx]);
    } else {
      cb(new Error('User ' + id + ' does not exist'));
    }
  });
}

exports.findByUsername = function(username, cb) {
  process.nextTick(function() {
    console.log("findByUsername");
    for (var i = 0, len = records.length; i < len; i++) {
      var record = records[i];
      if (record.username === username) {
        return cb(null, record);
      }
    }
    return cb(null, null);
  });
}
