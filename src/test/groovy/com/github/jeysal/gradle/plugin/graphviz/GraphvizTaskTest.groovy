package com.github.jeysal.gradle.plugin.graphviz

import com.github.jeysal.gradle.plugin.graphviz.node.NodeManager
import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * @author Tim Seckinger
 * @since 9/7/16
 */
class GraphvizTaskTest extends Specification {
    private Project project
    private GraphvizTask graphviz

    private boolean isWindows

    def setup() {
        project = ProjectBuilder.builder().build()
        graphviz = project.tasks.create(GraphvizTask.NAME, GraphvizTask)
        isWindows = OperatingSystem.current().windows
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

    def 'getExecutablePath() returns the executablePath if set'() {
        when:
        graphviz.executablePath = 'asdf'

        then:
        graphviz.executablePath == 'asdf'
    }

    def 'getExecutablePath() returns the viz.js path if enabled'() {
        setup:
        final node = new NodeManager(project)
        node.prepareNode()

        when:
        node.addVizSetupTask()

        then:
        graphviz.executablePath == new File(project.projectDir, '.gradle/graphvizdot' + (isWindows ? '.cmd' : '')).path
    }

    def 'getExecutablePath() returns the default if VizSetupTask is not present'() {
        expect:
        graphviz.executablePath == 'dot' + (isWindows ? '.exe' : '')
    }

    def 'getExecutablePath() returns the default if VizSetupTask is disabled'() {
        setup:
        final node = new NodeManager(project)
        node.prepareNode()
        final vizSetup = node.addVizSetupTask()

        when:
        vizSetup.enabled = false

        then:
        graphviz.executablePath == 'dot' + (isWindows ? '.exe' : '')
    }
}
