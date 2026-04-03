# More Health Mod

A Minecraft Forge mod for Minecraft 1.20.1 that scales player health with XP level. Players start with 1 heart and gain more through leveling up or consuming Heart Container items.

Current release channel: **Beta** (`1.0.0-beta.1`).

## Features

- Players start with 1 heart on first login
- Health increases automatically as XP level rises (never decreases from XP loss)
- Configurable XP thresholds for each heart tier (up to 15 hearts)
- Heart Container item: consume to instantly gain 1 heart
- In-game config screen accessible from the Mods menu

## Compatibility

- Minecraft: `1.20.1`
- Forge: `47.4.10+`
- Java: `17`

## Default XP Thresholds

| XP Level | Hearts |
|----------|--------|
| 0        | 1      |
| 10       | 2      |
| 20       | 3      |
| 30       | 4      |
| 40       | 5      |
| 50       | 6      |
| 75       | 8      |
| 100      | 10     |
| 150      | 12     |
| 200      | 15     |

## Prerequisites

- macOS (based on development environment)
- Homebrew (package manager for macOS)
- OpenJDK 17
- Gradle (wrapper included)
- Minecraft Forge MDK 1.20.1-47.4.10

## Setup Instructions

### 1. Install Homebrew

If you don't have Homebrew installed:

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

More info: [Homebrew Official Site](https://brew.sh/)

### 2. Install OpenJDK 17

```bash
brew install openjdk@17
echo 'export PATH="/usr/local/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

If using bash instead of zsh:

```bash
echo 'export PATH="/usr/local/opt/openjdk@17/bin:$PATH"' >> ~/.bashrc
source ~/.bashrc
```

Accept Xcode license if prompted:

```bash
sudo xcodebuild -license accept
```

Create symlink for Java Virtual Machine:

```bash
sudo ln -sfn /usr/local/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
```

### 3. Set Up Forge MDK

This project is based on the Forge MDK for 1.20.1-47.4.10. If starting from scratch:

1. Download the MDK from: [Forge Files](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.20.1.html)
2. Extract the MDK zip file
3. Copy the contents to your project directory, replacing existing files as needed
4. Run initial setup:

```bash
./gradlew genEclipseRuns  # or genIntellijRuns for IntelliJ
```

### 4. Build the Project

```bash
./gradlew build
```

This will generate the JAR at `build/libs/healthscaling-1.0.0-beta.1.jar`. Copy it to your Prism Launcher instance's mods folder to install.

## Running the Mod

### Using VS Code Tasks

- **Run Client**: Launches Minecraft with the mod
- **Build**: Compiles the mod
- **Gen VS Code Runs**: Generates VS Code launch configurations

### Manual Commands

```bash
./gradlew runClient       # Launch Minecraft client
./gradlew build           # Build the mod JAR
./gradlew genEclipseRuns  # Generate Eclipse run configs
./gradlew genIntellijRuns # Generate IntelliJ run configs
./gradlew :genVSCodeRuns  # Generate VS Code run configs
./gradlew clean           # Clean build outputs
```

## Configuration

Config is stored at `<gameDir>/config/healthscaling.json5` and is editable in-game via the Mods menu config button.

```json5
{
  // XP level required for 2 hearts
  "xpThreshold_1": 10,
  // XP level required for 3 hearts
  "xpThreshold_2": 20,
  // ...through xpThreshold_9 for 15 hearts
  "xpThreshold_9": 200
}
```

## Project Structure

```
src/main/java/com/dividedby0/healthscaling/
├── HealthScalingMod.java       # Main mod entry point
├── HealthScalingHandler.java   # XP → heart scaling logic, NBT persistence
├── SimpleConfigScreen.java     # In-game config UI
├── config/
│   ├── ConfigManager.java      # Config singleton
│   └── JSON5ConfigManager.java # JSON5 file reader/writer
└── item/
    ├── HeartContainerItem.java  # Consumable item: +1 heart
    └── ModItems.java            # Item registration
```

## Troubleshooting

- If you get Java version errors, ensure OpenJDK 17 is properly installed and PATH is set
- For Gradle issues, try `./gradlew --no-daemon clean build`
- Ensure you're using Forge 47.4.10 for Minecraft 1.20.1

## Project Links

- Repository: https://github.com/0dividedby0/MoreHealth
- Issues: https://github.com/0dividedby0/MoreHealth/issues

Note: The internal mod ID remains `healthscaling` for save/config compatibility.

## Release Notes (1.0.0-beta.1)

- Initial beta release on Forge 1.20.1.
- Added XP-to-hearts scaling system.
- Added nine configurable XP milestone thresholds.
- Added Heart Container item for permanent bonus hearts.
- Added persistent NBT data and migration handling.
- Added in-game configuration screen.

## License

MIT License — see [LICENSE](LICENSE) for full text.

## Credits

Inspired by **More Health Enchanted** by nohero.
Original mod thread: https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/1272363

This mod is an independent, ground-up rewrite for Minecraft Forge 1.20.1 and shares no code or assets with the original.
