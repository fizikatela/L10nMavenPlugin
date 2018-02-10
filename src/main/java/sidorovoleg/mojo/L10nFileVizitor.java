package sidorovoleg.mojo;

import sidorovoleg.mojo.locale.Localizer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by fizikatela
 * Email: aka.hunter@gmail.com
 * 28.01.2018.
 */
public class L10nFileVizitor extends SimpleFileVisitor<Path> {

    private Path srcPath;

    private Path destPath;

    private Set<String> locales;

    private Set<String> exts;

    private Map<String, Path> destLocalePaths;

    public L10nFileVizitor(Path srcPath, Path destPath, Set<String> locales, Set<String> exts) throws IOException {
        this.srcPath = srcPath;
        this.destPath = destPath;
        this.locales = locales;
        this.exts = exts;

        destLocalePaths = locales.stream()
                .collect(Collectors.toMap(locale -> locale, locale -> getLocalePath(locale)));
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        for (Path destPath : destLocalePaths.values()) {
            Files.createDirectories(destPath.resolve(srcPath.relativize(dir)));
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        Path dictPath = Paths.get(file.toAbsolutePath().toString() + ".dct");
        if (Files.exists(dictPath) && Files.isRegularFile(dictPath)) {
            Localizer localizer = new Localizer(dictPath);
            Map<String, List<String>> contents = new HashMap<>();
            try (Stream<String> lines = Files.lines(file)) {
                locales.forEach(locale ->
                        lines.forEachOrdered(line ->
                                contents.computeIfAbsent(locale, emptyList -> new ArrayList<>())
                                        .add(localizer.localizeLine(line, locale))));
            }

            for (Map.Entry<String, List<String>> entry : contents.entrySet()) {
                Files.write(destLocalePaths.get(entry.getKey()).resolve((srcPath.relativize(file))), entry.getValue(),
                        StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            }

        } else {
            for (Path destPath : destLocalePaths.values()) {
                Files.copy(file, destPath.resolve(srcPath.relativize(file)),
                        StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        return FileVisitResult.CONTINUE;
    }

    private Path getLocalePath(String locale) {
        return Paths.get(destPath.toAbsolutePath().toString() + "/" + srcPath.getFileName() + "_" + locale);
    }
}
