/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
var records = [
      { id: 1, username: 'faith', password: 'admin', displayName: 'Faith Broker', emails: [ { value: 'faith@coco.com' } ] }
    , { id: 2, username: 'faithbroker', password: 'admin', displayName: 'Faith Broker', emails: [ { value: 'faith@coco.com' } ] }
    , { id: 3, username: 'callie', password: 'admin', displayName: 'Callie Quartile', emails: [ { value: 'callie@coco.com' } ] }
    , { id: 4, username: 'calliequartile', password: 'admin', displayName: 'Callie Quartile', emails: [ { value: 'callie@coco.com' } ] }
    , { id: 5, username: 'gary', password: 'admin', displayName: 'Faith Broker', emails: [ { value: 'gary@coco.com' } ] }
    , { id: 6, username: 'garygeeke', password: 'admin', displayName: 'Gary Geeke', emails: [ { value: 'gary@coco.com' } ] }
    , { id: 7, username: 'erin', password: 'admin', displayName: 'Erin Overview', emails: [ { value: 'erin@coco.com' } ] }
    , { id: 8, username: 'erinoverview', password: 'admin', displayName: 'Erin Overview', emails: [ { value: 'erin@coco.com' } ] }

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
