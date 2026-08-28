<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Content Pack Duplicate Report

Compares two versions of an open metadata archive and reports the elements they disagree about: the ones that
keep their qualified name but are given a new unique identifier.

Each of those elements becomes a duplicate pair in any repository that has loaded both versions - two
elements of the same type, with the same qualified name, different identifiers, and nothing recording that
they are the same thing.  That is the situation
[duplicate management](https://egeria-project.org/features/duplicate-management/overview/) exists to resolve,
and it arises without anybody creating it deliberately: between Egeria 6.0 and 6.1, **874 elements** of the
core content pack changed identity this way.

Run it before shipping a content pack to see how much work an upgrade will create for the people who already
have the previous version loaded.

## Running it

```
./gradlew :open-metadata-resources:open-metadata-dev-utilities:content-pack-duplicate-report:run \
          --args="/tmp/CoreContentPack-6.0.omarchive /tmp/CoreContentPack-6.1.omarchive"
```

Archives from a released branch can be extracted without checking the whole branch out:

```
git fetch --depth 1 --filter=blob:none upstream egeria-release-6.0 egeria-release-6.1
git show upstream/egeria-release-6.0:content-packs/CoreContentPack.omarchive > /tmp/CoreContentPack-6.0.omarchive
git show upstream/egeria-release-6.1:content-packs/CoreContentPack.omarchive > /tmp/CoreContentPack-6.1.omarchive
```

## Output

```
Earlier archive : CoreContentPack-6.0.omarchive (2449 named elements)
Later archive   : CoreContentPack-6.1.omarchive (2618 named elements)

874 element(s) keep their qualified name but change their unique identifier.
Each one becomes a duplicate pair in a repository that loads both archives.

By type:

  Connection : 412
  GovernanceActionType : 87
  ...
```

Elements that appear in only one of the archives are new or withdrawn content rather than duplicates, so they
are not counted.

## Why it reads the JSON directly

The archives are read as JSON rather than loaded through the archive utilities.  The point of the utility is
to compare archives from *different releases*, and an archive written by an older release may no longer
deserialize into the current Java types.  Reading the JSON directly means any two archive files can be
compared, whatever produced them.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
