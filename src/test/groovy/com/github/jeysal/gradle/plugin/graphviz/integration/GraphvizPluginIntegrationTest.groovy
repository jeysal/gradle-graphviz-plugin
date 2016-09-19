package com.github.jeysal.gradle.plugin.graphviz.integration

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/**
 * @author Tim Seckinger
 * @since 9/18/16
 */
class GraphvizPluginIntegrationTest extends Specification {
    @Rule
    TemporaryFolder projectDir

    private File buildFile
    private File graphvizBuildDir

    private GradleRunner runner

    def setup() {
        buildFile = projectDir.newFile('build.gradle')
        buildFile.text = 'plugins { id "com.github.jeysal.graphviz" }\n'

        graphvizBuildDir = new File(projectDir.root, 'build/graphviz')

        runner = GradleRunner.create().withProjectDir(projectDir.root).withPluginClasspath()
    }

    def 'graphviz task generates output from sources with specified layout and format'() {
        setup:
        new File(projectDir.newFolder('src', 'main', 'graphviz'), 'source.gv') <<
                getClass().getResourceAsStream('/source.gv')
        buildFile << """graphviz {
                            layout = '$layout'
                            format = '$format'
                        }\n"""

        expect:
        runner.withArguments('graphviz').build().task(':graphviz').outcome == TaskOutcome.SUCCESS
        graphvizBuildDir.list().toList() == [outputName.toString()]
        new File(graphvizBuildDir, outputName).text == getClass().getResourceAsStream("/$layout.$format").text

        where:
        layout  | format
        'dot'   | 'xdot'
        'dot'   | 'svg'
        'circo' | 'xdot'
        'circo' | 'svg'

        outputName = "source.gv.$format"
    }
}
