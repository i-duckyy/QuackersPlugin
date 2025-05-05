plugins {
    id("java")
}

group = "duckyplugin"
version = "1.0.0"

tasks {
    withType<JavaCompile> {
        options.release = 8
        options.compilerArgs.add("-Xlint:-options") // Suppress Java 8 deprecation warnings
    }
}
