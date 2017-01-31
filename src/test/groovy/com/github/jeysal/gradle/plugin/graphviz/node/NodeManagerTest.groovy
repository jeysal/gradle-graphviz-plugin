package com.github.jeysal.gradle.plugin.graphviz.node

import com.moowork.gradle.node.NodeExtension
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * @author Tim Seckinger
 * @since 9/2/16
 */
class NodeManagerTest extends Specification {
    private Project project
    private NodeManager manager

    def setup() {
        project = ProjectBuilder.builder().build()
        manager = new NodeManager(project)
    }

    def 'prepareNode() applies the gradle-node-plugin'() {
        when:
        manager.prepareNode()

        then:
        project.pluginManager.hasPlugin(NodeManager.NODE_PLUGIN_ID)
    }

    def 'prepareNode() configures the gradle-node-plugin'() {
        when:
        manager.prepareNode()

        then:
        final NodeExtension node = project.extensions.node
        node.download
        node.nodeModulesDir == new File(project.projectDir, '.gradle')
    }

    def 'prepareNode() takes projectCacheDir into account'() {
        setup:
        project.gradle.startParameter.projectCacheDir = project.projectDir

        when:
        manager.prepareNode()

        then:
        project.extensions.node.nodeModulesDir == project.projectDir
    }

    def 'prepareNode() does not configure the gradle-node-plugin if already applied'() {
        setup:
        project.pluginManager.apply(NodeManager.NODE_PLUGIN_ID)
        final NodeExtension node = project.extensions.node
        node.download = false
        node.version = 'asdf'
        node.nodeModulesDir = project.projectDir

        when:
        manager.prepareNode()

        then:
        !node.download
        node.version == 'asdf'
        node.nodeModulesDir == project.projectDir
    }

    def 'addVizSetupTask() adds the viz setup task'() {
        setup:
        manager.prepareNode()

        when:
        manager.addVizSetupTask()

        then:
        project.getTasksByName(VizSetupTask.NAME, false)[0] instanceof VizSetupTask
    }
}
