// build.gradle.kts - ЗАМЕНИТЕ ВСЁ СОДЕРЖИМОЕ на это:
plugins {
    id("java")
}

allprojects {
    group = "com.senla"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }

    apply(plugin = "java")

    // ЯВНО УКАЗЫВАЕМ JAVA 21
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }
}

subprojects {
    apply(plugin = "java")

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
}