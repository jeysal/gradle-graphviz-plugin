package com.github.jeysal.gradle.plugin.graphviz

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * @author Tim Seckinger
 * @since 9/4/16
 */
class GraphvizPluginTest extends Specification {
    private Project project
    private GraphvizPlugin plugin

    def setup() {
        project = ProjectBuilder.builder().build()
        plugin = new GraphvizPlugin()
    }

    def "apply() creates the GraphvizExtension"() {
        when:
        plugin.apply(project)
        then:
        project.extensions.graphviz instanceof GraphvizExtension
    }
}
