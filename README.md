# Smart Commenting for Apache NetBeans

**English** | [Čeština](README.cs.md)

Smart Commenting is an extension for Apache NetBeans that provides configurable line and block comment toggling with indentation-aware formatting.

The extension selects the appropriate comment syntax according to the document MIME type and file extension. It can process the current line or all lines affected by the current selection.

## Features

- Toggle comments on the current line or selected block
- Line and block comment support
- Indentation-aware comment placement
- Automatic language detection by MIME type and file extension
- Configurable comment mode, alignment, and block layout
- Live settings preview
- Custom-code preview based on the caret position or selected block
- Configurable keyboard shortcut in the NetBeans Keymap

## Supported languages and file types

The extension currently includes support for these language families and formats:

- PHP
- Java
- JavaScript and TypeScript
- C, C++, and header files
- C#
- Go
- Kotlin
- Rust
- Swift
- Dart
- Pascal and Delphi
- Windows Batch
- Python
- Shell scripts
- Ruby
- Perl
- Lua
- Haskell
- R
- NEON
- YAML
- TOML
- INI and CFG
- Java Properties
- Dockerfile
- SQL
- Latte
- HTML and XML
- CSS
- TeX

Comment syntax can also be resolved by file extension when NetBeans reports only a general MIME type such as `text/plain`.

## Installation

### Install from an NBM file

1. Download the latest `.nbm` file from the repository's **Releases** section.
2. Open Apache NetBeans.
3. Select **Tools → Plugins**.
4. Open the **Downloaded** tab.
5. Click **Add Plugins...** and select the downloaded `.nbm` file.
6. Complete the installation wizard and restart NetBeans if requested.

## Usage

Place the caret on a line or select part of a block, then run **Smart Toggle Comment**.

- With no selection, the extension processes the line containing the caret.
- With a selection, the extension expands the range to all affected lines and processes the resulting block.
- If the selection ends exactly at the beginning of the next line, that next line is not included.

The proposed default shortcut is:

- Windows and Linux: `Ctrl+Alt+C`
- macOS: `Command+Option+C`

The shortcut can be changed under **Tools → Options → Keymap** by searching for **Smart Toggle Comment**.

## Configuration

Open:

**Tools → Options → Miscellaneous → Smart Commenting**

Available settings include:

- Requested comment mode: automatic, line, or block
- Line-comment alignment
- Block-comment alignment
- Block-comment boundary layout
- Preview language

The live preview contains examples for a single line and a multi-line selection. The **Custom Code** tab lets you enter your own code. Without a selection, the preview transforms the line containing the caret; with a selection, it transforms all affected lines.

## Building from source

This repository contains an Ant-based Apache NetBeans module project.

1. Open the project in Apache NetBeans.
2. Verify the module dependencies in **Project Properties → Libraries**.
3. Run **Clean and Build**.
4. Use **Create NBM** to produce the installable module package.

The project currently uses Java source level 8.

## Project structure

```text
netbeans-smart-commenting/
├── src/
├── nbproject/
├── build.xml
├── manifest.mf
├── LICENSE
├── NOTICE
├── CHANGELOG.md
├── README.md
└── README.cs.md
```

Generated and local files such as `build/` and `nbproject/private/` should not be committed.

## Reporting issues

Please use GitHub Issues to report:

- unsupported languages or file extensions,
- incorrect comment syntax,
- indentation problems,
- keyboard-shortcut conflicts,
- incompatibility with a specific Apache NetBeans version.

When reporting a problem, include the Apache NetBeans version, operating system, file extension, detected MIME type if known, input code, and actual output.

## Contributing

Contributions are welcome. Before submitting a change, please verify comment and uncomment behavior for:

- a single line,
- a multi-line selection,
- empty lines inside a selection,
- spaces and tabs,
- the final line without a trailing newline,
- a selection ending at the beginning of the next line.

A useful invariant is:

```text
uncomment(comment(original)) == original
```

## License

Smart Commenting is licensed under the Apache License, Version 2.0.

See [LICENSE](LICENSE) and [NOTICE](NOTICE) for details.

`SPDX-License-Identifier: Apache-2.0`
