package com.github.jeysal.gradle.plugin.graphviz.node

import com.moowork.gradle.node.NodeExtension
import groovy.transform.CompileStatic
import org.gradle.api.Project

/**
 * Encapsulates the node and npm viz setup.
 * Provides a task that installs the viz binaries.
 *
 * @author Tim Seckinger
 * @since 9/2/16
 */
@CompileStatic
class NodeManager {
    public static final String NODE_PLUGIN_ID = 'com.github.node-gradle.node'

    private final Project project

    NodeManager(final Project project) {
        this.project = project
    }

    /**
     * Applies and configures the gradle-node-plugin (if not already applied).
     *
     * @param project The project to apply the gradle-node-plugin to
     */
    void prepareNode() {
        if (!project.pluginManager.hasPlugin(NODE_PLUGIN_ID)) {
            project.pluginManager.apply(NODE_PLUGIN_ID)
            final NodeExtension node = project.extensions.findByType(NodeExtension)

            node.download = true
            node.nodeModulesDir = project.gradle.startParameter.projectCacheDir ?:
                    new File(project.rootProject.projectDir, '.gradle')
        }
    }

    /**
     * Adds the vizSetup tasks to the specified project.
     *
     * @param project The project to add the tasks to
     * @return The added task
     */
    VizSetupTask addVizSetupTask() {
        project.tasks.create(VizSetupTask.NAME, VizSetupTask)
    }
}
