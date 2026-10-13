# Changelog

All notable changes to this project will be documented in this file.

## [0.1.0-jgrpp-0.73.4] - 2026-10-13

### Added
- Compatibility and protocol support for OpenTTD JGR Patch Pack (`jgrpp-0.73.4`).
- Bundled RemoteGS GameScript (`src/squirrel/`) modified for compatibility with modern OpenTTD versions, including Squirrel bindings generation script (`gen_api_binding.py`), symbol tables (`symbols.nut`), and packaging tool (`make_tar.py`).
- Java JSON model classes `RemoteGSJson` and `JsonStringParameter` for GameScript admin interaction via JSON payloads.
- Constants `CompanyID`, `GSNews`, and `TestMode`.
- `NOTICE` file providing third-party attribution for bundled RemoteGS and upstream Openttd-admin modified for `jgrpp-0.73.4`.
- Git semantic versioning plugin (`git-sensitive-semantic-versioning`).
- Badges and attribution documentation in `README.md`.

## [Pre-alpha] - 2026-10-13

### Added
- Java 27 Adoptium toolchain integration (`.sdkmanrc`, `build.gradle`).
- Diffplug Spotless plugin (v8.10.4) for automated code style enforcement and formatting.
- Foojay toolchain resolver convention in `settings.gradle`.

### Changed
- Upgraded Gradle wrapper to version 9.9.0-milestone-2.
- Upgraded project dependencies:
  - `org.apache.logging.log4j:log4j-core` to `2.26.1`
  - `org.slf4j:slf4j-api` and `org.slf4j:slf4j-reload4j` to `2.0.18`
  - `com.google.guava:guava` to `32.0.1-jre`
  - `com.google.code.gson:gson` to `2.8.9`
- Modernized GameScript builder task (`buildGameScript`) to invoke `python3`.
- Modernized Java source code with pattern matching for `instanceof` and improved type safety.
- Re-formatted entire codebase according to Spotless Java formatting rules.

### Fixed
- Fixed typo in disconnection log messages ("disconneting" -> "disconnecting").
