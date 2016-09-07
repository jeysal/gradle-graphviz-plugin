package com.github.jeysal.gradle.plugin.graphviz

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * @author Tim Seckinger
 * @since 9/7/16
 */
class GraphvizTaskTest extends Specification {
    private Project project
    private GraphvizTask graphviz

    def setup() {
        project = ProjectBuilder.builder().build()
        graphviz = project.tasks.create(GraphvizTask.NAME, GraphvizTask)
    }

    def 'sources(Closure) configures the pattern set'() {
        when:
        graphviz.sources {
            include 'abc'
            exclude 'xyz'
        }

        then:
        graphviz.sourcePatterns.includes == ['abc'] as Set
        graphviz.sourcePatterns.excludes == ['xyz'] as Set
    }

    def 'sources default to **/*.gv and **/*.dot files'() {
        expect:
        graphviz.sourcePatterns.includes == ['**/*.gv', '**/*.dot'] as Set
        graphviz.sourcePatterns.excludes.empty
    }
}
