<p align="center">
  <img src="docs/logo.png" alt="OpenHachimi" width="560">
</p>

<h1 align="center">
  <img src="docs/title.png" alt="OpenHachimi" width="560">
</h1>

A readable source reconstruction of the Hachimi V2 client: a client-side Fabric mod for Minecraft 1.21.1 (Yarn mappings, Mixin, access widener).

The sources were recovered from the protected `Hachimi-V2-crack.jar` by bytecode deobfuscation and decompilation, so they are a reconstruction, not the original repository. Most class, field and method names are inferred from what the code does and are not the original names. Behaviour was checked by launching the client in a development environment and toggling all 176 modules in a singleplayer world; it was not compared against the original jar. The recovery pipeline, the remaining gaps and the evidence live in the parent workspace (`ACCEPTANCE.md`, `SCOPE.md`).

## Build and run

Requires JDK 21 or newer (JDK 25 is what the project is built with) and network access to Maven Central and `maven.fabricmc.net` for Minecraft, Yarn, Fabric and the test dependencies.

```
./gradlew build       # build/libs/OpenHachimi-2.0.jar (remapped, for Fabric Loader), build/devlibs/OpenHachimi-2.0-dev.jar (Yarn names)
./gradlew test
./gradlew runClient   # starts the client with the working directory run/
```

On Windows use `gradlew.bat`. The bytecode target is Java 21 (`--release 21`).

IntelliJ IDEA: `File > Open` and select this directory, import it as a Gradle project and set the Gradle JVM to JDK 21 or newer. Loom downloads and maps Minecraft on the first import (slow) and generates the **Minecraft Client** run configuration; its working directory is `run/`. The `.idea/` directory is generated, so it is not meant to be committed.

Gradle's test worker cannot start when the project path contains non-ASCII characters, so run `./gradlew build` from an ASCII-only path.

[![QQ Group 1078719379](https://img.shields.io/badge/QQ%20Group-1078719379-12B7F5?logo=tencentqq&logoColor=white)](https://qm.qq.com/q/OnrCkjX7Y4)
