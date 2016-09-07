package com.github.jeysal.gradle.plugin.graphviz

import org.gradle.api.Project

/**
 * @author Tim Seckinger
 * @since 9/2/16
 */
class GraphvizExtension {
    public static final String NAME = 'graphviz'

    /**
     * Whether to download and use viz.js as Graphviz executable.
     */
    boolean useVizJs = true
    /**
     * Path of the Graphviz executable (or folder that contains it) to use.<br/>
     * If not set, PATH environment variable is used.
     */
    String executablePath = ''
    /**
     * Graphviz layout engine to use. Defaults to 'dot'.
     */
    String layout = 'dot'

    Object sourceDir

    GraphvizExtension(Project project) {
        sourceDir = new File(project.projectDir, 'src/main/graphviz')
    }
}
