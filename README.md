# openttd-admin

[![Release](https://img.shields.io/badge/release-1.0.0--jgrpp--0.73.4-blue.svg)](CHANGELOG.md)
[![OpenTTD](https://img.shields.io/badge/OpenTTD-jgrpp--0.73.4-blue.svg)](https://github.com/JGRennison/OpenTTD-patches)
[![Java 27](https://img.shields.io/badge/Java-27-orange.svg)](https://adoptium.net/)
[![Gradle](https://img.shields.io/badge/Gradle-9.9.0--milestone--2-green?style=flat)](https://gradle.org)
[![Spotless](https://img.shields.io/badge/code%20style-spotless-brightgreen.svg)](https://github.com/diffplug/spotless)
[![Notice](https://img.shields.io/badge/notice-third--party%20attribution-lightgrey.svg)](NOTICE)

A Java library and client implementation for the OpenTTD Admin Network Protocol, providing administrative controls, event listeners, and GameScript integration.

## Compilation

```
gradlew spotlessApply build -x test buildGameScript
```

## Usage

```bash
java -jar openttd-admin.jar -cp slf4j-api-2.0.18.jar localhost:3977 admin_password
```

## Changelog

For a detailed list of changes and release history, please see [CHANGELOG.md](CHANGELOG.md).
- **Latest Release (v0.2.0-jgrpp-0.73.4)**: Integrated Renewed Village Growth (RVG bigy-fork) and Peaks & Troughs mechanics into bundled GameScript (`src/squirrel/`), added modular GameScript subsystems (cargo, industry, town, company, subsidies, story, strings), added NewGRF compatibility sets and 11 language localizations.
- **Previous Release (v0.1.0-jgrpp-0.73.4)**: Added support for OpenTTD JGR Patch Pack (`jgrpp-0.73.4`), bundled RemoteGS GameScript with modern OpenTTD compatibility, added RemoteGS JSON models and constants, upgraded Java toolchain to Java 27 and Gradle wrapper to 9.9.0, upgraded project dependencies, integrated Spotless code formatting, and added third-party attributions in NOTICE.

## Attribution & Third-Party Works

This project builds upon and bundles third-party software:
- **Openttd-admin**: Built upon the original work by [Eric Renard](https://github.com/erenard/openttd-admin), modified for compatibility with JGR's Patchpack (`jgrpp-0.73.4`) and modern toolchains.
- **RemoteGS**: Bundles [RemoteGS](https://github.com/BasicBeluga/RemoteGS) (by Ian Earle, forked from ServerGS by Leif Linse / Zuu), slightly modified for compatibility with modern OpenTTD versions and API bindings under GPLv2.

See [NOTICE](NOTICE) and [src/squirrel/LICENSE](src/squirrel/LICENSE) for full details.
