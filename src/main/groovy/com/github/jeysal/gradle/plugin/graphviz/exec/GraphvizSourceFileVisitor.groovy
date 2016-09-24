package com.github.jeysal.gradle.plugin.graphviz.exec

import com.github.jeysal.gradle.plugin.graphviz.GraphvizTask
import org.gradle.api.file.FileVisitDetails
import org.gradle.api.file.FileVisitor
import org.gradle.api.tasks.TaskExecutionException

/**
 * @author Tim Seckinger
 * @since 9/14/16
 */
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
        final def source = fileDetails.relativePath.getFile(graphviz.sourceDir)
        final def target = fileDetails.relativePath.getFile(graphviz.outputDir)

        graphviz.formats.forEach { final format ->

            final def cmd = [graphviz.executablePath,
                             '-o', target.path + ((graphviz.formatSuffix && format) ? ".$format" : ''),
                             source.path]

            if (graphviz.layout)
                cmd << '-K' << graphviz.layout
            if (format)
                cmd << '-T' << format

            try {
                final def proc = cmd.execute()

                proc.consumeProcessOutputStream(System.out as OutputStream)
                proc.consumeProcessErrorStream(System.err as OutputStream)

                final def exitCode = proc.waitFor()
                if (exitCode) {
                    fileDetails.stopVisiting()
                    throw new TaskExecutionException(graphviz,
                            new RuntimeException("graphviz command $cmd failed with exit code $exitCode"))
                }
            } catch (IOException | InterruptedException e) {
                throw new TaskExecutionException(graphviz, e)
            }
        }
    }
}
