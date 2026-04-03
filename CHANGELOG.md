# Changelog

All notable changes to this project will be documented in this file.

The format is based on Keep a Changelog and this project follows semantic versioning.

## [1.0.0-beta.1] - 2026-04-02

### Added
- Players start with 1 heart (2 HP) at level 0 and gain hearts through XP level milestones.
- Nine configurable XP threshold levels (default: 10, 20, 30, 40, 50, 75, 100, 150, 200).
- Heart Container item: craftable/obtainable item that grants a permanent bonus heart on use.
- In-game configuration screen for all XP threshold settings.
- Persistent heart data across death, respawn, and server restart.
- Legacy data migration from earlier `HealthScaling_TotalHearts` NBT key.
