# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2026-10-20

### Added
- Integrated Renewed Village Growth (RVG bigy-fork) and Peaks & Troughs gameplay mechanics with RemoteGS Admin Port capabilities.
- Added modular GameScript subsystems: `cargo.nut`, `industry.nut`, `town.nut`, `company.nut`, `subsidies.nut`, `story.nut`, and `strings.nut`.
- Added support for multiple Industry NewGRFs (FIRS, XIS, AXIS, ITI, OTIS, PIRS, Real Industries, Default Industries Plus, etc.).
- Added dynamic subsidy generation and Peaks & Troughs preset economy configurations.
- Added in-game Story Book pages for cargo category overviews, welcome guide, warnings, and custom server rules.
- Added multi-language localization files (`czech`, `french`, `galician`, `german`, `japanese`, `polish`, `russian`, `simplified_chinese`, `slovak`, `traditional_chinese`, `ukrainian`).
- Added configurable town growth, cargo category, difficulty scale, and local authority rating ("Eternal Love") settings.

### Changed
- Restarted semantic versioning at `1.0.0` (`SELF_VERSION 10`, `SELF_MAJORVERSION 1`, `SELF_MINORVERSION 0`) to clearly identify the split from upstream RemoteGS numbering conventions.
- Updated project metadata and script name to `Experimental Renewed Village Growth byh-fork + Peaks & Troughs + RemoteGS` (`RVBP`).
- Increased maximum savable town limit (`SELF_MAX_TOWNS`) to 4000 towns.
- Targeted OpenTTD GameScript API version 14 in `info.nut`.
- Updated `main.nut` game loop to coordinate RVG growth calculations, town signs, statistics tracking, and SuperLib / GSToyLib / SCPLib libraries alongside AdminPort JSON RPC interface.

### Fixed
- Patched changes to cargos in FIRS 5 and ITI.

## [4.0.0] - 2026-11-01

### Added
- Updated API symbol definitions for OpenTTD GameScript API version 15 and JGR's Patchpack `jgrpp-0.73.4`.

### Changed
- Bumped internal script version to 4 (`SELF_VERSION 4`).
- Updated OpenTTD GameScript API compatibility version target in `info.nut` from 1.3 to 15.
- Updated SuperLib dependency import from version 36 to version 41 in `main.nut`.
- Updated API binding generator script `gen_api_binding.py` to scan `*.sq.hpp` headers for `jgrpp-0.73.4`.
- Updated release archive packager script `make_tar.py` to bundle `README.md`.

### Fixed
- Fixed Squirrel argument type check for booleans in `admin_port.nut` (`typeof(arg) == "bool"` instead of `"boolean"`).
