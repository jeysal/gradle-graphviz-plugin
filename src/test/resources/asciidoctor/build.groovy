buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'org.asciidoctor:asciidoctor-gradle-plugin:1.5.3'
        classpath 'com.github.jruby-gradle:jruby-gradle-plugin:1.3.3'
    }
}

plugins {
    id 'com.github.jeysal.graphviz'
}

apply plugin: 'org.asciidoctor.convert'
apply plugin: 'com.github.jruby-gradle.base'

dependencies {
    gems 'rubygems:asciidoctor-diagram:1.5.1'
}

asciidoctor {
    dependsOn jrubyPrepare
    requires = ['asciidoctor-diagram']
    gemPath = jrubyPrepare.outputDir
}
