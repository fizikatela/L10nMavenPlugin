package sidorovoleg.mojo;

import sidorovoleg.mojo.locale.AbstractLocalizer;
import sidorovoleg.mojo.locale.LocalizerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Реализация FileVisitor обхода директорий для локализации
 * @author fizikatela
 * Date: 28.01.2018
 */
public class L10nFileVizitor extends SimpleFileVisitor<Path> {

    /** Расширения файлов со словарем */
    private static final String DICT_EXT = ".dct";
    /** Расширение - любой фаил  */
    private static final String ALL_FILE_EXT = "*.";
    /** Путь до директории для локализации */
    private final Path srcPath;
    /** Путь до директории с результатом локализации */
    private final Path destPath;
    /** Список локалей локализации */
    private final Set<String> locales;
    /** Список расширений файлов для локализации */
    private final Set<String> exts;
    /** Карта [локаль -> путь до директории с результатом локализации]  */
    private final Map<String, Path> destLocalePaths;
    /** Признак локализации файлов с любым расширением */
    private final boolean isAllFile;

    /**
     * Конструктор
     * @param srcPath  путь до директории для локализации
     * @param destPath путь до директории с результатом локализации
     * @param locales  список локалей локализации
     * @param exts     список расширений файлов для локализации
     * @throws IOException при ошибке ввода / вывода
     */
    public L10nFileVizitor(Path srcPath, Path destPath, Set<String> locales, Set<String> exts) throws IOException {
        this.srcPath = srcPath;
        this.destPath = destPath;
        this.locales = locales;
        this.exts = exts;
        this.isAllFile = exts.contains(ALL_FILE_EXT);

        destLocalePaths = locales.stream()
                .collect(Collectors.toMap(locale -> locale, locale -> getBaseLocalePath(locale)));
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

        Path dictPath = Paths.get(file.toAbsolutePath().toString() + DICT_EXT);
        if (isAcceptFile(file) && Files.exists(dictPath) && Files.isRegularFile(dictPath)) {
            AbstractLocalizer localizer = LocalizerFactory.getLocalizer(FileType.valueOf(file), dictPath);
            Map<String, List<String>> contents = new HashMap<>();
            try (Stream<String> lines = Files.lines(file)) {
                lines.forEach(line -> locales
                        .forEach(locale -> contents
                                .computeIfAbsent(locale, emptyList -> new ArrayList<>())
                                .add(localizer.localizeString(line, locale))));
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

    /**
     * Возвращает базовый путь до директории с результатом локализации с учетом локали
     * @param locale локаль
     * @return базовый путь с учетом локали
     */
    private Path getBaseLocalePath(String locale) {
        return Paths.get(destPath.toAbsolutePath().toString() + "/" + srcPath.getFileName() + "_" + locale);
    }

    /**
     * Проверяем необходимость локализации файла
     * @param file путь до файла
     * @return {@code true}, если нужно локализировать файл, иначе {@code false}
     */
    private boolean isAcceptFile(Path file) {
        String ext = FileType.getExtFile(file);
        if (ext == null || DICT_EXT.equals(ext)) {
            return false;
        }
        return isAllFile || exts.contains(ext);
    }
}
