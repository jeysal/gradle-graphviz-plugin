package com.github.jeysal.gradle.plugin.graphviz.integration

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Specification
import spock.lang.TempDir

/**
 * @author Tim Seckinger
 * @since 9/18/16
 */
class GraphvizPluginIntegrationTest extends Specification {
    @TempDir
    File projectDir

    private File buildFile
    private File graphvizSrcDir
    private File graphvizBuildDir

    private GradleRunner runner

    def setup() {
        buildFile = new File(projectDir, 'build.gradle')
        buildFile.text = 'plugins { id "com.github.jeysal.graphviz" }\n'

        graphvizSrcDir = new File(projectDir, 'src/main/graphviz');
        graphvizSrcDir.mkdirs()
        graphvizBuildDir = new File(projectDir, 'build/graphviz')

        runner = GradleRunner.create().withProjectDir(projectDir).withPluginClasspath()
    }

    def 'graphviz task generates output from sources with specified layout and format'() {
        setup:
        new File(graphvizSrcDir, 'source.gv') << getClass().getResourceAsStream('/source.gv')
        buildFile << """graphviz {
                            layout = '$layout'
                            formats = ['$format']
                        }\n"""

        expect:
        runner.withArguments('graphviz').build().task(':graphviz').outcome == TaskOutcome.SUCCESS
        graphvizBuildDir.list() as Set == [outputName.toString()] as Set
        new File(graphvizBuildDir, outputName).text == getClass().getResourceAsStream("/$layout.$format").text

        where:
        layout  | format
        'dot'   | 'xdot'
        'dot'   | 'svg'
        'circo' | 'xdot'
        'circo' | 'svg'

        outputName = "source.gv.$format"
    }

    def 'graphviz task generates multiple output files from multiple source files'() {
        setup:
        new File(graphvizSrcDir, 'source.gv') << getClass().getResourceAsStream('/source.gv')
        new File(graphvizSrcDir, 'other.gv') << getClass().getResourceAsStream('/source.gv')

        expect:
        runner.withArguments('graphviz').build().task(':graphviz').outcome == TaskOutcome.SUCCESS
        graphvizBuildDir.list() as Set == ['source.gv', 'other.gv'] as Set
        new File(graphvizBuildDir, 'source.gv').text == getClass().getResourceAsStream("/dot.xdot").text
        new File(graphvizBuildDir, 'other.gv').text == getClass().getResourceAsStream("/dot.xdot").text
    }

    def 'graphviz task generates multiple output files when specifying multiple formats'() {
        setup:
        new File(graphvizSrcDir, 'source.gv') << getClass().getResourceAsStream('/source.gv')
        buildFile << 'graphviz { formats = ["xdot", "svg"] }\n'

        expect:
        runner.withArguments('graphviz').build().task(':graphviz').outcome == TaskOutcome.SUCCESS
        graphvizBuildDir.list() as Set == ['source.gv.xdot', 'source.gv.svg'] as Set
        new File(graphvizBuildDir, 'source.gv.xdot').text == getClass().getResourceAsStream("/dot.xdot").text
        new File(graphvizBuildDir, 'source.gv.svg').text == getClass().getResourceAsStream("/dot.svg").text
    }

    def 'graphviz task retains source directory structure'() {
        setup:
        def subdir = new File(graphvizSrcDir, 'abc/xyz')
        subdir.mkdirs()
        new File(subdir, 'source.gv') << getClass().getResourceAsStream('/source.gv')

        expect:
        runner.withArguments('graphviz').build().task(':graphviz').outcome == TaskOutcome.SUCCESS
        graphvizBuildDir.list() as Set == ['abc'] as Set
        new File(graphvizBuildDir, 'abc').list() as Set == ['xyz'] as Set
        new File(graphvizBuildDir, 'abc/xyz').list() as Set == ['source.gv'] as Set
        new File(graphvizBuildDir, 'abc/xyz/source.gv').text == getClass().getResourceAsStream('/dot.xdot').text
    }

    def 'graphviz task ignores empty source directory'() {
        expect:
        runner.withArguments('graphviz').build().task(':graphviz').outcome == TaskOutcome.NO_SOURCE
        !graphvizBuildDir.exists()
    }

    def 'graphviz task uses exact source file names if formatSuffix is false'() {
        setup:
        new File(graphvizSrcDir, 'source.gv') << getClass().getResourceAsStream('/source.gv')
        buildFile << 'graphviz { formatSuffix = false }\n'

        expect:
        runner.withArguments('graphviz').build().task(':graphviz').outcome == TaskOutcome.SUCCESS
        graphvizBuildDir.list() as Set == ['source.gv'] as Set
        new File(graphvizBuildDir, 'source.gv').text == getClass().getResourceAsStream("/dot.xdot").text
    }

    def 'the last format remains when disabling formatSuffix despite multiple formats'() {
        setup:
        new File(graphvizSrcDir, 'source.gv') << getClass().getResourceAsStream('/source.gv')
        buildFile << '''graphviz {
                            formats = ["xdot", "svg"]
                            formatSuffix = false
                        }\n'''

        expect:
        runner.withArguments('graphviz').build().task(':graphviz').outcome == TaskOutcome.SUCCESS
        graphvizBuildDir.list() as Set == ['source.gv'] as Set
        new File(graphvizBuildDir, 'source.gv').text == getClass().getResourceAsStream("/dot.svg").text
    }

    def 'graphviz task reads from specified sourceDir'() {
        setup:
        def altSrcDir = new File(projectDir, 'altSrc')
        altSrcDir.mkdir()
        new File(altSrcDir, 'source.gv') << getClass().getResourceAsStream('/source.gv')
        buildFile << 'graphviz { sourceDir = file("altSrc") }\n'

        expect:
        runner.withArguments('graphviz').build().task(':graphviz').outcome == TaskOutcome.SUCCESS
        graphvizBuildDir.list() as Set == ['source.gv'] as Set
        new File(graphvizBuildDir, 'source.gv').text == getClass().getResourceAsStream("/dot.xdot").text
    }

    def 'graphviz task writes to specified outputDir'() {
        setup:
        new File(graphvizSrcDir, 'source.gv') << getClass().getResourceAsStream('/source.gv')
        buildFile << 'graphviz { outputDir = file("graphvizOut") }\n'
        graphvizBuildDir = new File(projectDir, 'graphvizOut')

        expect:
        runner.withArguments('graphviz').build().task(':graphviz').outcome == TaskOutcome.SUCCESS
        graphvizBuildDir.list() as Set == ['source.gv'] as Set
        new File(graphvizBuildDir, 'source.gv').text == getClass().getResourceAsStream("/dot.xdot").text

    }

    def 'graphviz task processes only source files matching specified patterns'() {
        setup:
        new File(graphvizSrcDir, 'source.gv') << getClass().getResourceAsStream('/source.gv')
        new File(graphvizSrcDir, 'other.gv') << getClass().getResourceAsStream('/source.gv')
        buildFile << 'graphviz { sources { include "source.*" } }\n'

        expect:
        runner.withArguments('graphviz').build().task(':graphviz').outcome == TaskOutcome.SUCCESS
        graphvizBuildDir.list() as Set == ['source.gv'] as Set
        new File(graphvizBuildDir, 'source.gv').text == getClass().getResourceAsStream("/dot.xdot").text
    }

    def 'graphviz task is UP-TO-DATE on unchanged second execution'() {
        setup:
        new File(graphvizSrcDir, 'source.gv') << getClass().getResourceAsStream('/source.gv')

        expect:
        runner.withArguments('graphviz').build().task(':graphviz').outcome == TaskOutcome.SUCCESS
        GradleRunner.create().withProjectDir(projectDir).withPluginClasspath()
                .withArguments('graphviz').build().task(':graphviz').outcome == TaskOutcome.UP_TO_DATE
        graphvizBuildDir.list() as Set == ['source.gv'] as Set
        new File(graphvizBuildDir, 'source.gv').text == getClass().getResourceAsStream("/dot.xdot").text
    }
}
