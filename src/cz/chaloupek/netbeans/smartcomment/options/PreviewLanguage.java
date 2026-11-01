package cz.chaloupek.netbeans.smartcomment.options;

/**
 * Jazyky a formáty dostupné v živém náhledu nastavení.
 *
 * Seznam odpovídá jazykům a příponám podporovaným registrem komentářů.
 */
public enum PreviewLanguage
{
    PHP("PHP", "text/x-php", "php"),
    JAVA("Java", "text/x-java", "java"),
    JAVASCRIPT("JavaScript", "text/javascript", "js"),
    TYPESCRIPT("TypeScript", "text/typescript", "ts"),
    C("C", "text/x-c", "c"),
    CPP("C++", "text/x-c++", "cpp"),
    C_HEADER("C/C++ hlavička", "text/x-h", "h"),
    CSHARP("C#", "text/x-csharp", "cs"),
    GO("Go", "text/x-go", "go"),
    KOTLIN("Kotlin", "text/x-kotlin", "kt"),
    RUST("Rust", "text/x-rust", "rs"),
    SWIFT("Swift", "text/x-swift", "swift"),
    DART("Dart", "text/x-dart", "dart"),
    PASCAL("Pascal / Delphi", "text/x-pascal", "pas"),
    BATCH("Windows Batch", "text/x-bat", "bat"),
    PYTHON("Python", "text/x-python", "py"),
    SHELL("Shell", "text/x-sh", "sh"),
    RUBY("Ruby", "text/x-ruby", "rb"),
    PERL("Perl", "text/x-perl", "pl"),
    LUA("Lua", "text/x-lua", "lua"),
    HASKELL("Haskell", "text/x-haskell", "hs"),
    R("R", "text/x-r", "r"),
    NEON("NEON", "text/x-neon", "neon"),
    YAML("YAML", "text/x-yaml", "yaml"),
    TOML("TOML", "text/x-toml", "toml"),
    INI("INI / CFG", "text/x-ini", "ini"),
    PROPERTIES("Java Properties", "text/x-properties", "properties"),
    DOCKERFILE("Dockerfile", "text/x-dockerfile", "dockerfile"),
    SQL("SQL", "text/x-sql", "sql"),
    LATTE("Latte", "text/x-latte", "latte"),
    HTML("HTML", "text/html", "html"),
    XML("XML", "text/xml", "xml"),
    CSS("CSS", "text/css", "css"),
    TEX("TeX", "text/x-tex", "tex");

    private final String displayName;
    private final String mimeType;
    private final String extension;

    PreviewLanguage(String displayName, String mimeType, String extension)
    {
        this.displayName = displayName;
        this.mimeType = mimeType;
        this.extension = extension;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public String getExtension()
    {
        return extension;
    }

    @Override
    public String toString()
    {
        return displayName;
    }
}
