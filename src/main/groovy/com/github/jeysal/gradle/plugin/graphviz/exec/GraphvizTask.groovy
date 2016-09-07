package com.github.jeysal.gradle.plugin.graphviz.exec

import com.github.jeysal.gradle.plugin.graphviz.node.VizSetupTask
import org.gradle.api.DefaultTask

/**
 * @author Tim Seckinger
 * @since 9/7/16
 */
class GraphvizTask extends DefaultTask {
    public static final String NAME = 'graphviz'

    public GraphvizTask() {
        group = 'build'
        description = 'Generates Graphviz output from sources'

        dependsOn VizSetupTask.NAME
    }
}
