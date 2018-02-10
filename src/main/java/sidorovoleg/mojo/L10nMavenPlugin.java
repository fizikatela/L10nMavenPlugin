package sidorovoleg.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * Created by fizikatela
 * Email: aka.hunter@gmail.com
 * 27.01.2018.
 */
@Mojo(name = "l10n", defaultPhase = LifecyclePhase.PROCESS_SOURCES, threadSafe = true)
public class L10nMavenPlugin extends AbstractMojo {

    @Parameter(name = "srcFolders", required = true)
    private List<File> srcFolders;

    @Parameter(name = "destFolder", required = true)
    private File destFolder;

    @Parameter(name = "locales", required = true)
    private Set<String> locales;

    @Parameter(name = "exts", required = true)
    private Set<String> exts;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Log logger = getLog();
        logger.info("L10n-maven-plugin start...");
        logger.info("L10n-maven-plugin check input params...");
        checkInputParams();

        Path destPath = destFolder.toPath();
        if (!Files.exists(destPath)) {
            try {
                Files.createDirectories(destPath);
            } catch (IOException e) {
                throw new MojoExecutionException("Unable to create folder: " + destPath);
            }
        }

        try {
            for (File srcFolder : srcFolders) {
                Files.walkFileTree(srcFolder.toPath(), new L10nFileVizitor(srcFolder.toPath(), destPath, locales, exts));
            }
        } catch (IOException e) {
            throw new MojoExecutionException("L10n-maven-plugin error. " + e);
        }

        logger.info("L10n-maven-plugin finish...");
    }

    /**
     * Проверяет входящие параметры
     * @throws MojoExecutionException
     */
    private void checkInputParams() throws MojoExecutionException {
        if (srcFolders.isEmpty()) {
            throw new MojoExecutionException("Empty 'srcFolders' params");
        }

        if (locales.isEmpty()) {
            throw new MojoExecutionException("Empty 'locales' params");
        }

        if (exts.isEmpty()) {
            throw new MojoExecutionException("Empty 'ext' params");
        }
    }
}
