package com.github.jeysal.gradle.plugin.graphviz

import org.gradle.api.Project
import org.gradle.api.tasks.util.PatternSet

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

    /**
     * Directory to scan for source files. Defaults to src/main/graphviz.
     */
    Object sourceDir

    private final PatternSet sources = new PatternSet()

    GraphvizExtension(Project project) {
        sourceDir = new File(project.projectDir, 'src/main/graphviz')
    }

    /**
     * Configures the sources to use via ant-style patterns.<br/>
     * <b>Example:</b><br/>
     * <code>sources { include '**&#47;*.gv', '**&#47;*.dot' }</code>
     */
    void sources(Closure config) {
        Closure configClone = config.clone() as Closure
        configClone.delegate = sources
        configClone()
    }
}
