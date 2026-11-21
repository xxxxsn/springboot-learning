package com.xiesn.springboot.docker.compose;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 解析 compose.yaml 绝对路径（优先模块根，其次工作目录，最后 classpath 抽出）。
 */
final class ComposeFileLocator {

    private ComposeFileLocator() {
    }

    /**
     * 查找可用的 compose 文件。
     *
     * @return 绝对路径
     */
    static Path requireComposeFile() {
        Set<Path> candidates = new LinkedHashSet<>();
        addModuleRootCandidate(candidates);

        Path userDir = Path.of(System.getProperty("user.dir", ".")).toAbsolutePath().normalize();
        candidates.add(userDir.resolve("springboot-docker-compose").resolve("compose.yaml"));
        candidates.add(userDir.resolve("compose.yaml"));

        List<String> tried = new ArrayList<>();
        for (Path candidate : candidates) {
            Path normalized = candidate.toAbsolutePath().normalize();
            tried.add(normalized.toString());
            if (Files.isRegularFile(normalized)) {
                return normalized;
            }
        }

        Path extracted = extractClasspathCompose();
        if (extracted != null) {
            return extracted;
        }

        throw new IllegalStateException(
                "找不到 compose.yaml。user.dir=" + userDir + "；已尝试: " + tried
                        + "；classpath:compose/compose.yaml 也无法抽出");
    }

    private static void addModuleRootCandidate(Set<Path> candidates) {
        try {
            URI uri = ComposeFileLocator.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            Path location = Path.of(uri);
            if (Files.isRegularFile(location) && location.getFileName().toString().endsWith(".jar")) {
                return;
            }
            Path classesDir = Files.isDirectory(location) ? location : location.getParent();
            if (classesDir != null
                    && "classes".equals(classesDir.getFileName().toString())
                    && classesDir.getParent() != null
                    && "target".equals(classesDir.getParent().getFileName().toString())
                    && classesDir.getParent().getParent() != null) {
                candidates.add(classesDir.getParent().getParent().resolve("compose.yaml"));
            }
        } catch (Exception ignored) {
            // ignore
        }
    }

    /**
     * 从 classpath 抽出 compose 到临时文件（IDE 资源目录也可能带上）。
     *
     * @return 临时文件路径；失败返回 null
     */
    private static Path extractClasspathCompose() {
        try (InputStream in = ComposeFileLocator.class.getClassLoader()
                .getResourceAsStream("compose/compose.yaml")) {
            if (in == null) {
                return null;
            }
            Path tempDir = Files.createTempDirectory("springboot-docker-compose-");
            Path target = tempDir.resolve("compose.yaml");
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            target.toFile().deleteOnExit();
            tempDir.toFile().deleteOnExit();
            return target.toAbsolutePath().normalize();
        } catch (Exception ex) {
            return null;
        }
    }
}