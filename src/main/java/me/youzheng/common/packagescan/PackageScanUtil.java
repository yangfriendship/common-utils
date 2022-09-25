package me.youzheng.common.packagescan;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.partitioningBy;

public class PackageScanUtil {

    /**
     * 파라미터로 넘긴 클래스의 위치를 기준으로 하위 패키지를 모두 조회
     * 파라미터로 넘긴 클래스의 하위 패키지를 기준으로 하위 패키지의 Class 정보를 조회
     * @param startClass
     * @return
     */
    public static List<Class<?>> getAllClasses(final Class<?> startClass) {
        if (!startClass.isAnnotationPresent(PackageScan.class)) {
            return getAllClasses(startClass.getPackage().getName());
        }
        final PackageScan annotation = startClass.getAnnotation(PackageScan.class);
        final String[] packageNames = annotation.value();
        if (packageNames == null || packageNames.length == 0) {
            throw new RuntimeException();
        }
        return getAllClasses(packageNames);
    }

    /**
     * 입력한 패키지의 하위 패키지를 기준으로 하위 패키지의 Class 정보를 조회
     * @param packagePaths
     * @return
     */
    public static List<Class<?>> getAllClasses(final String... packagePaths) {
        if (packagePaths == null || packagePaths.length == 0) {
            return Collections.emptyList();
        }

        try {
            final List<Class<?>> allClasses = new ArrayList<>();
            for (final String packageName : packagePaths) {
                final String packageRelativePath = packageName.replace('.', '/');

                final URI packageUri = Thread.currentThread().getContextClassLoader().getResource(packageRelativePath).toURI();

                if (packageUri.getScheme().equals("file")) {
                    final Path packageFullPath = Paths.get(packageUri);
                    allClasses.addAll(getAllPackageInternal(packageFullPath, packageName));
                } else if (packageUri.getScheme().equals("jar")) {
                    final FileSystem fileSystem = FileSystems.newFileSystem(packageUri, Collections.emptyMap());
                    final Path packageFullPathInJar = fileSystem.getPath(packageRelativePath);
                    allClasses.addAll(getAllPackageInternal(packageFullPathInJar, packageName));
                    fileSystem.close();
                }
            }
            return allClasses;
        }catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private static List<Class<?>> getAllPackageInternal(final Path packagePath, final String packageName) throws ClassNotFoundException, IOException {
        if (!Files.exists(packagePath)) {
            return Collections.emptyList();
        }

        final Map<Boolean, List<Path>> fileTypeIsRegularFiles = Files.list(packagePath)
                .collect(partitioningBy(Files::isRegularFile));

        final List<Class<?>> classes = new ArrayList<>();

        final List<Path> regularFiles = fileTypeIsRegularFiles.get(true);
        for (final Path filePath : regularFiles) {
            String fileName = filePath.getFileName().toString();

            if (fileName.endsWith(".class")) {
                final String classFullName = packageName + '.' + fileName.replaceFirst("\\.class$", "");
                classes.add(Class.forName(classFullName));
            }
        }

        final List<Path> directories = fileTypeIsRegularFiles.get(false)
                .stream().filter(Files::isDirectory)
                .collect(Collectors.toList());

        for (final Path path : directories) {
            classes.addAll(getAllPackageInternal(path, packageName + "." + path.getFileName()));
        }
        return classes;
    }

}