buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'org.asciidoctor:asciidoctor-gradle-jvm:3.3.2'
    }
}

plugins {
    id 'org.asciidoctor.jvm.convert' version '3.3.2'
    id 'com.github.jeysal.graphviz'
}
repositories {
    mavenCentral()
}

asciidoctorj {
    modules {
       diagram.use()
    }
}

import org.asciidoctor.gradle.jvm.AsciidoctorTask

task moreAsciidoctor(type: AsciidoctorTask) {
    sourceDir file('src/docs/asciidoc')
    outputDir file('build/docs/asciidoc')
}
