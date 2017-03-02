package com.github.jeysal.gradle.plugin.graphviz

import com.github.jeysal.gradle.plugin.graphviz.asciidoctor.AsciidoctorManager
import com.github.jeysal.gradle.plugin.graphviz.node.NodeManager
import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author Tim Seckinger
 * @since 9/2/16
 */
@CompileStatic
class GraphvizPlugin implements Plugin<Project> {
    @Override
    void apply(final Project project) {
        final nodeManager = new NodeManager(project)
        nodeManager.prepareNode()
        nodeManager.addVizSetupTask()

        project.tasks.create(GraphvizTask.NAME, GraphvizTask)

        project.extensions.create(GraphvizHooksExtension.NAME, GraphvizHooksExtension)

        final asciidoctorManager = new AsciidoctorManager(project)
        project.afterEvaluate asciidoctorManager.&registerGraphviz
    }
}
