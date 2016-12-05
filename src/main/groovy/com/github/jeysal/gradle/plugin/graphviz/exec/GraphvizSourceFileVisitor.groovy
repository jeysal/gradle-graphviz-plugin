package com.github.jeysal.gradle.plugin.graphviz.exec

import com.github.jeysal.gradle.plugin.graphviz.GraphvizTask
import groovy.util.logging.Slf4j
import org.gradle.api.file.FileVisitDetails
import org.gradle.api.file.FileVisitor
import org.gradle.api.tasks.TaskExecutionException

/**
 * @author Tim Seckinger
 * @since 9/14/16
 */
@Slf4j
class GraphvizSourceFileVisitor implements FileVisitor {
    private final GraphvizTask graphviz

    GraphvizSourceFileVisitor(final GraphvizTask graphviz) {
        this.graphviz = graphviz
    }

    @Override
    void visitDir(final FileVisitDetails dirDetails) {
        // Do nothing
    }

    @Override
    void visitFile(final FileVisitDetails fileDetails) {
        final source = fileDetails.relativePath.getFile(graphviz.sourceDir)
        final target = fileDetails.relativePath.getFile(graphviz.outputDir)

        graphviz.formats.each { final format ->
            try {
                final cmd = buildCmd(source, target, format)
                final proc = cmd.execute()

                proc.consumeProcessOutputStream(System.out as OutputStream)
                proc.consumeProcessErrorStream(System.err as OutputStream)

                final exitCode = proc.waitFor()
                if (exitCode) {
                    fileDetails.stopVisiting()
                    throw new TaskExecutionException(graphviz,
                            new RuntimeException("graphviz command $cmd failed with exit code $exitCode"))
                }
            } catch (final IOException | InterruptedException e) {
                throw new TaskExecutionException(graphviz, e)
            }
        }
    }

    private List<String> buildCmd(final File source, final File target, final String format) {
        final cmd = [graphviz.executablePath,
                         '-o', target.path + ((graphviz.formatSuffix && format) ? ".$format" : ''),
                         source.path]

        if (graphviz.layout)
            cmd << '-K' << graphviz.layout
        if (format)
            cmd << '-T' << format

        cmd
    }
}
