buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'org.asciidoctor:asciidoctor-gradle-plugin:1.5.3'
        classpath 'com.github.jruby-gradle:jruby-gradle-plugin:1.5.0'
    }
}

plugins {
    id 'com.github.jeysal.graphviz'
}

apply plugin: 'org.asciidoctor.convert'
apply plugin: 'com.github.jruby-gradle.base'

dependencies {
    gems 'rubygems:asciidoctor-diagram:1.5.4'
}

import org.asciidoctor.gradle.AsciidoctorTask

task moreAsciidoctor(type: AsciidoctorTask)

[asciidoctor, moreAsciidoctor]*.configure {
    dependsOn jrubyPrepare
    requires = ['asciidoctor-diagram']
    gemPath = jrubyPrepare.outputDir
}
