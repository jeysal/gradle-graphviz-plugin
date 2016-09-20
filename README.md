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
