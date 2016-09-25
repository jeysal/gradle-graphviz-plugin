package com.github.jeysal.gradle.plugin.graphviz

import com.github.jeysal.gradle.plugin.graphviz.asciidoctor.AsciidoctorManager
import com.github.jeysal.gradle.plugin.graphviz.node.NodeManager
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author Tim Seckinger
 * @since 9/2/16
 */
class GraphvizPlugin implements Plugin<Project> {
    @Override
    void apply(final Project project) {
        final def nodeManager = new NodeManager(project)
        nodeManager.prepareNode()
        nodeManager.addVizSetupTask()

        project.tasks.create(GraphvizTask.NAME, GraphvizTask)

        final def asciidoctorManager = new AsciidoctorManager(project)
        project.afterEvaluate asciidoctorManager.&registerGraphviz
    }
}
