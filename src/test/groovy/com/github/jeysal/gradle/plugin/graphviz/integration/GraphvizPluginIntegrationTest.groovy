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

    def 'graphviz task retains source directory structure'() {
        setup:
        new File(projectDir.newFolder('src', 'main', 'graphviz', 'abc', 'xyz'), 'source.gv') <<
                getClass().getResourceAsStream('/source.gv')

        expect:
        runner.withArguments('graphviz').build().task(':graphviz').outcome == TaskOutcome.SUCCESS
        graphvizBuildDir.list().toList() == ['abc']
        new File(graphvizBuildDir, 'abc').list().toList() == ['xyz']
        new File(graphvizBuildDir, 'abc/xyz').list().toList() == ['source.gv.xdot']
        new File(graphvizBuildDir, 'abc/xyz/source.gv.xdot').text == getClass().getResourceAsStream('/dot.xdot').text
    }

    def 'graphviz task ignores empty source directory'() {
        expect:
        runner.withArguments('graphviz').build().task(':graphviz').outcome == TaskOutcome.UP_TO_DATE
        !graphvizBuildDir.exists()
    }

    def 'graphviz task uses exact source file names if formatSuffix is false'() {
        setup:
        new File(projectDir.newFolder('src', 'main', 'graphviz'), 'source.gv') <<
                getClass().getResourceAsStream('/source.gv')
        buildFile << 'graphviz { formatSuffix = false }\n'

        expect:
        runner.withArguments('graphviz').build().task(':graphviz').outcome == TaskOutcome.SUCCESS
        graphvizBuildDir.list().toList() == ['source.gv']
        new File(graphvizBuildDir, 'source.gv').text == getClass().getResourceAsStream("/dot.xdot").text
    }

    def 'graphviz task reads from specified sourceDir'() {
        setup:
        new File(projectDir.newFolder('altSrc'), 'source.gv') <<
                getClass().getResourceAsStream('/source.gv')
        buildFile << 'graphviz { sourceDir = file("altSrc") }\n'

        expect:
        runner.withArguments('graphviz').build().task(':graphviz').outcome == TaskOutcome.SUCCESS
        graphvizBuildDir.list().toList() == ['source.gv.xdot']
        new File(graphvizBuildDir, 'source.gv.xdot').text == getClass().getResourceAsStream("/dot.xdot").text
    }

    def 'graphviz task writes to specified outputDir'() {
        setup:
        new File(projectDir.newFolder('src', 'main', 'graphviz'), 'source.gv') <<
                getClass().getResourceAsStream('/source.gv')
        buildFile << 'graphviz { outputDir = file("graphvizOut") }\n'
        graphvizBuildDir = new File(projectDir.root, 'graphvizOut')

        expect:
        runner.withArguments('graphviz').build().task(':graphviz').outcome == TaskOutcome.SUCCESS
        graphvizBuildDir.list().toList() == ['source.gv.xdot']
        new File(graphvizBuildDir, 'source.gv.xdot').text == getClass().getResourceAsStream("/dot.xdot").text

    }
}
