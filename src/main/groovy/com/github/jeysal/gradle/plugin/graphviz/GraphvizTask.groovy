package com.github.jeysal.gradle.plugin.graphviz

import com.github.jeysal.gradle.plugin.graphviz.node.VizSetupTask
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.util.PatternSet

/**
 * @author Tim Seckinger
 * @since 9/7/16
 */
class GraphvizTask extends DefaultTask {
    public static final String NAME = 'graphviz'

    /**
     * Path of the Graphviz executable to use.<br/>
     * If not set, viz provided by :vizSetup or Graphviz from PATH environment variable is used.
     */
    @Input
    String executablePath = ''
    /**
     * Graphviz layout engine to use. Defaults to 'dot'.
     */
    @Input
    String layout = 'dot'
    /**
     * Graphviz output format to produce. Defaults to 'xdot'
     */
    @Input
    String format = 'xdot'
    /**
     * Directory to scan for source files. Defaults to src/main/graphviz.
     */
    @InputDirectory
    Object sourceDir

    /**
     * Directory to generate Graphviz output into
     */
    @OutputDirectory
    Object outputDir = new File(project.buildDir, 'graphviz')

    private PatternSet sources

    public GraphvizTask() {
        group = 'build'
        description = 'Generates Graphviz output from sources'

        dependsOn VizSetupTask.NAME

        sourceDir = new File(project.projectDir, 'src/main/graphviz')
    }

    /**
     * Configures the sources to use via ant-style patterns.<br/>
     * If not configured, sources default to the example below<br/>
     * <b>Example:</b><br/>
     * <code>sources { include '**&#47;*.gv', '**&#47;*.dot' }</code>
     */
    void sources(Closure config) {
        sources = sources ?: new PatternSet()
        Closure configClone = config.clone() as Closure
        configClone.delegate = sources
        configClone()
    }

    @Input
    PatternSet getSources() {
        return sources ?: new PatternSet().include('**/*.gv', '**/*.dot')
    }
}
