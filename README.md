# gradle-graphviz-plugin

[![Build status](https://img.shields.io/travis/jeysal/gradle-graphviz-plugin.svg?style=flat-square)](https://travis-ci.org/jeysal/gradle-graphviz-plugin)
[![AppVeyor build status](https://img.shields.io/appveyor/ci/jeysal/gradle-graphviz-plugin.svg?style=flat-square&label=windows+build)](https://ci.appveyor.com/project/jeysal/gradle-graphviz-plugin)
[![License](https://img.shields.io/github/license/jeysal/gradle-graphviz-plugin.svg?style=flat-square)](https://github.com/jeysal/gradle-graphviz-plugin/blob/master/LICENSE)

A [Gradle](https://gradle.org/) plugin to make builds using [Graphviz](http://www.graphviz.org/) portable.
The plugin will download all necessary files and run Graphviz on your sources - on Windows, Mac and Linux.

Uses [viz.js](https://github.com/mdaines/viz.js/) via [viz.js-cli-wrapper](https://github.com/jeysal/viz.js-cli-wrapper) installed via [gradle-node-plugin](https://github.com/srs/gradle-node-plugin).
Compared to the original Graphviz, viz.js has a few limitations. See [here](https://github.com/jeysal/viz.js-cli-wrapper#usage) for details.

## Installation

You can retrieve the plugin from the jitpack.io repository. The jcenter (or maven central) is additionally required for transitive dependencies.
Add the following repository and dependency to your Gradle or Maven build config.

**Note that specifying the version `master-SNAPSHOT` will get you the artifact based on the current commit on branch master.  
You can specify any release number, the latest one available is**
[![Latest release](https://jitpack.io/v/com.github.jeysal/gradle-graphviz-plugin.svg?style=flat-square)](https://jitpack.io/#com.github.jeysal/gradle-graphviz-plugin)

### Gradle

```groovy
    buildscript {
        repositories {
            jcenter()
            maven { url 'https://jitpack.io' }
        }
        dependencies {
            classpath 'com.github.jeysal:gradle-graphviz-plugin:master-SNAPSHOT'
        }
    }

    apply plugin: 'com.github.jeysal.graphviz'
```

## Usage

After applying the plugin, Graphviz will convert files in src/main/graphviz ending with .gv or .dot to xdot format and
place the outputs in build/graphviz.

## Configuration example

```groovy
/* Do not download a Graphviz executable.
The plugin will try to find Graphviz on your PATH instead.
Note that using a regular Graphviz may cause problems
such as failing to write files in subdirectories of the outputDir */
vizSetup.enabled = false

graphviz {
    /* Path of the Graphviz executable to use.
    If not set, viz provided by :vizSetup or Graphviz from PATH environment variable is used. */
    executablePath = '/usr/bin/dot'

    // Graphviz layout engine to use. Defaults to the Graphviz executable's default layout (usually dot).
    layout = 'neato'

    /* Graphviz output formats to produce. Defaults to the Graphviz executable's default format (usually xdot).
    Note that if you specify a format twice or set formatSuffix to false,
    the formats overwrite each other and only the last one remains. */
    formats = ['svg']

    /* If true, the chosen format will be appended to every output file name. Defaults to true.
    The default format will not be appended since Graphviz itself decides what to generate in that case. */
    formatSuffix = false

    // Directory to scan for source files. Defaults to src/main/graphviz.
    sourceDir = file('graphvizSrc')
    
    // Directory to generate Graphviz output into. Defaults to build/graphviz.
    outputDir = file('graphvizOut')

    /* Configures the sources to use via ant-style patterns.
    If not configured, sources default to the example below. */
    sources {
        include '**/*.gv', '**/*.dot'
    }
}
```

## Credits

A huge thanks to [Mike Daines](https://github.com/mdaines) for making GraphViz to some extent portable with [viz.js](https://github.com/mdaines/viz.js)
and [Sten Roger Sandvik](https://github.com/srs) for creating [gradle-node-plugin](https://github.com/srs/gradle-node-plugin) which is used for setting up viz.js!

## License

gradle-graphviz-plugin is [MIT-licensed](https://github.com/jeysal/gradle-graphviz-plugin/blob/master/LICENSE).
