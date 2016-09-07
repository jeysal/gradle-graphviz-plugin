package com.github.jeysal.gradle.plugin.graphviz

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * @author Tim Seckinger
 * @since 9/7/16
 */
class GraphvizExtensionTest extends Specification {
    private Project project
    private GraphvizExtension extension

    def setup() {
        project = ProjectBuilder.builder().build()
        extension = new GraphvizExtension(project)
    }

    def 'sources(Closure) configures the pattern set'() {
        when:
        extension.sources {
            include 'abc'
            exclude 'xyz'
        }

        then:
        extension.sources.includes == ['abc'] as Set
        extension.sources.excludes == ['xyz'] as Set
    }

    def 'sources default to **/*.gv and **/*.dot files'() {
        expect:
        extension.sources.includes == ['**/*.gv', '**/*.dot'] as Set
        extension.sources.excludes.empty
    }
}
