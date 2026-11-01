# Changelog

All notable changes to **Smart Commenting for Apache NetBeans** are documented in this file.

The project follows [Semantic Versioning](https://semver.org/).

## [1.0.0] - 2026-10-30

### Added

- Initial public release of Smart Commenting for Apache NetBeans.
- Smart toggling of comments on the current line or selected block.
- Automatic expansion of a selection to all affected lines.
- Correct handling of a selection ending at the beginning of the next line.
- Line and block comment support according to the selected language.
- Automatic comment-style detection by document MIME type.
- File-extension fallback when NetBeans reports only a general or unsupported MIME type.
- Configurable comment mode:
  - automatic,
  - line comment,
  - block comment.
- Configurable alignment of line comments.
- Configurable alignment of block-comment boundaries with the common block indentation.
- Configurable inline or separate-line layout for block-comment boundaries.
- Options panel under NetBeans settings.
- Live preview for a single line and a multi-line selection.
- Custom-code preview that reacts to the caret position and selected block.
- Keyboard shortcut registration for the Smart Toggle Comment action.
- Support for the following languages and file types:
  - PHP,
  - Java,
  - JavaScript and TypeScript,
  - C, C++, and header files,
  - C#,
  - Go,
  - Kotlin,
  - Rust,
  - Swift,
  - Dart,
  - Pascal and Delphi,
  - Windows Batch,
  - Python,
  - Shell scripts,
  - Ruby,
  - Perl,
  - Lua,
  - Haskell,
  - R,
  - NEON,
  - YAML,
  - TOML,
  - INI and CFG,
  - Java Properties,
  - Dockerfile,
  - SQL,
  - Latte,
  - HTML and XML,
  - CSS,
  - TeX.
- English and Czech project documentation.
- Apache License 2.0 licensing information for source and NBM distribution.

### Technical details

- Shared `CommentTransformer` used by both the editor action and the live preview.
- Editor line boundaries determined from the Swing `Document` element structure.
- User settings stored through NetBeans preferences.
- Ant-based NetBeans module project with Java 8 source compatibility.

[1.0.0]: https://github.com/USERNAME/netbeans-smart-commenting/releases/tag/v1.0.0
