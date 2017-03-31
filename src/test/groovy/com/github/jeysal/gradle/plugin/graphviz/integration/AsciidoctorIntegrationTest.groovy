package com.github.jeysal.gradle.plugin.graphviz.integration

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/**
 * @author Tim Seckinger
 * @since 9/24/16
 */
class AsciidoctorIntegrationTest extends Specification {
    @Rule
    TemporaryFolder projectDir

    private File buildFile
    private File asciidoctorBuildDir

    private GradleRunner runner

    def setup() {
        buildFile = projectDir.newFile('build.gradle')
        buildFile << getClass().getResourceAsStream('/asciidoctor/build.groovy')

        new File(projectDir.newFolder('src', 'docs', 'asciidoc'), 'source.adoc') <<
                getClass().getResourceAsStream('/asciidoctor/source.adoc')

        asciidoctorBuildDir = new File(projectDir.root, 'build/asciidoc/html5')

        runner = GradleRunner.create().withProjectDir(projectDir.root).withPluginClasspath()
    }

    def 'asciidoctor generates diagrams using the provided Graphviz executable'() {
        when:
        final result = runner.withArguments('asciidoctor').build()

        then:
        result.task(':asciidoctor').outcome == TaskOutcome.SUCCESS
        result.task(':vizSetup').outcome == TaskOutcome.SUCCESS
        asciidoctorBuildDir.list() as Set == ['source.html', 'test.svg', '.asciidoctor'] as Set
        new File(asciidoctorBuildDir, 'test.svg').text.startsWith(
                '<?xml version="1.0" encoding="UTF-8" standalone="no"?>' +
                        '<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"')
        !new File(asciidoctorBuildDir, 'test.svg').text.contains('Cannot find Graphviz')
    }

    def 'vizSetup is not run if asciidoctor hook is disabled'() {
        setup:
        buildFile << 'graphvizHooks.asciidoctor = false\n'

        when:
        final result = runner.withArguments('asciidoctor').build()

        then:
        result.task(':asciidoctor').outcome == TaskOutcome.SUCCESS
        result.task(':vizSetup') == null
        asciidoctorBuildDir.list() as Set == ['source.html', 'test.svg', '.asciidoctor'] as Set
        new File(asciidoctorBuildDir, 'test.svg').text.startsWith(
                '<?xml version="1.0" encoding="UTF-8" standalone="no"?>' +
                        '<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"')
    }

    def 'a custom AsciidoctorTask generates diagrams using the provided Graphviz executable'() {
        when:
        final result = runner.withArguments('moreAsciidoctor').build()

        then:
        result.task(':moreAsciidoctor').outcome == TaskOutcome.SUCCESS
        result.task(':vizSetup').outcome == TaskOutcome.SUCCESS
        asciidoctorBuildDir.list() as Set == ['source.html', 'test.svg', '.asciidoctor'] as Set
        new File(asciidoctorBuildDir, 'test.svg').text.startsWith(
                '<?xml version="1.0" encoding="UTF-8" standalone="no"?>' +
                        '<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"')
        !new File(asciidoctorBuildDir, 'test.svg').text.contains('Cannot find Graphviz')
    }
}
