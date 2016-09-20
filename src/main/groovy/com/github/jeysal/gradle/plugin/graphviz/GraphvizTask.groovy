package com.github.jeysal.gradle.plugin.graphviz

import com.github.jeysal.gradle.plugin.graphviz.exec.GraphvizSourceFileVisitor
import com.github.jeysal.gradle.plugin.graphviz.node.VizSetupTask
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.*
import org.gradle.api.tasks.util.PatternSet
import org.gradle.internal.os.OperatingSystem

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
     * Graphviz layout engine to use. Defaults to the Graphviz executable's default layout (usually dot).
     */
    @Input
    String layout = ''
    /**
     * Graphviz output format to produce. Defaults to the Graphviz executable's default format (usually xdot).
     */
    @Input
    String format = ''
    /**
     * If true, the chosen format will be appended to every output file name. Defaults to true.
     */
    @Input
    boolean formatSuffix = true
    /**
     * Directory to scan for source files. Defaults to src/main/graphviz.
     */
    @Input
    Object sourceDir = new File(new File(new File(project.projectDir, 'src'), 'main'), 'graphviz')

    /**
     * Directory to generate Graphviz output into
     */
    @OutputDirectory
    Object outputDir = new File(project.buildDir, 'graphviz')

    private PatternSet sourcePatterns

    public GraphvizTask() {
        group = 'build'
        description = 'Generates Graphviz output from sources'

        dependsOn VizSetupTask.NAME
    }

    String getExecutablePath() {
        def isWindows = OperatingSystem.current().windows
        def useVizJs = project.tasks.find { it.name == VizSetupTask.NAME }?.enabled
        return executablePath ?: (useVizJs
                ? new File(new File(new File(project.extensions.node.nodeModulesDir as File, 'node_modules'), '.bin'),
                'dot' + (isWindows ? '.cmd' : '')).path
                : 'dot' + (isWindows ? '.exe' : ''))
    }

    File getSourceDir() {
        return project.file(sourceDir)
    }

    File getOutputDir() {
        return project.file(outputDir)
    }

    /**
     * Configures the sources to use via ant-style patterns.<br/>
     * If not configured, sources default to the example below.<br/>
     * <b>Example:</b><br/>
     * <code>sources { include '**&#47;*.gv', '**&#47;*.dot' }</code>
     */
    void sources(Closure config) {
        sourcePatterns = sourcePatterns ?: new PatternSet()
        Closure configClone = config.clone() as Closure
        configClone.delegate = sourcePatterns
        configClone()
    }

    PatternSet getSourcePatterns() {
        return sourcePatterns ?: new PatternSet().include('**/*.gv', '**/*.dot')
    }

    @InputFiles
    @SkipWhenEmpty
    FileTree getSourceFiles() {
        return project.fileTree(sourceDir)
                .matching(getSourcePatterns())
    }

    @TaskAction
    void exec() {
        sourceFiles.visit(new GraphvizSourceFileVisitor(this))
    }
}
