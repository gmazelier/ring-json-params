# `ring-json-params`

[Ring](http://github.com/mmcgrana/ring) middleware that augments `:params` according to a parsed JSON request body.

## Notes for this fork

### Features

+ Allow JSON array and augments `:params` with a value identified by `:json-array`.
+ Prevent overriding existing params when merging.
+ Parse as keywords.

### Artifact

With Leiningen:

    [org.clojars.gmazelier/ring-json-params "0.1.4-SNAPSHOT"]

With Maven:

    <dependency>
      <groupId>org.clojars.gmazelier</groupId>
      <artifactId>ring-json-params</artifactId>
      <version>0.1.4-SNAPSHOT</version>
    </dependency>