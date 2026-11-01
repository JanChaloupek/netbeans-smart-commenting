package cz.chaloupek.netbeans.smartcomment.style;

import cz.chaloupek.netbeans.smartcomment.SmartCommentLog;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Registr MIME typů, přípon a způsobů komentování podporovaných jazyků.
 */
public final class CommentStyleRegistry
{
    private static final LineCommentStyle SLASH = new LineCommentStyle("//");
    private static final LineCommentStyle HASH = new LineCommentStyle("#");
    private static final LineCommentStyle DASH = new LineCommentStyle("--");
    private static final LineCommentStyle SEMICOLON = new LineCommentStyle(";");
    private static final LineCommentStyle PERCENT = new LineCommentStyle("%");
    private static final LineCommentStyle REM = new LineCommentStyle("REM");

    private static final BlockCommentStyle C_BLOCK = new BlockCommentStyle("/*", "*/");
    private static final BlockCommentStyle LATTE_BLOCK = new BlockCommentStyle("{*", "*}");
    private static final BlockCommentStyle HTML_BLOCK = new BlockCommentStyle("<!--", "-->");
    private static final BlockCommentStyle PASCAL_BLOCK = new BlockCommentStyle("{", "}");
    private static final BlockCommentStyle HASKELL_BLOCK = new BlockCommentStyle("{-", "-}");
    private static final BlockCommentStyle LUA_BLOCK = new BlockCommentStyle("--[[", "]]");

    private static final Map<String, LanguageCommentStyle> MIME_STYLES = new HashMap<>();
    private static final Map<String, LanguageCommentStyle> EXTENSION_STYLES = new HashMap<>();

    static {
        LanguageCommentStyle cFamily = lineAndBlock(SLASH, C_BLOCK);
        LanguageCommentStyle hashOnly = lineOnly(HASH);
        LanguageCommentStyle sql = lineAndBlock(DASH, C_BLOCK);
        LanguageCommentStyle pascal = new LanguageCommentStyle(
                SLASH, PASCAL_BLOCK, CommentMode.LINE, CommentMode.BLOCK
        );
        LanguageCommentStyle haskell = lineAndBlock(DASH, HASKELL_BLOCK);
        LanguageCommentStyle lua = lineAndBlock(DASH, LUA_BLOCK);

        registerMime(cFamily,
                "text/x-php5", "text/x-php", "text/x-java",
                "text/javascript", "text/x-javascript", "text/typescript",
                "text/x-c", "text/x-c++", "text/x-h", "text/x-csharp",
                "text/x-go", "text/x-kotlin", "text/x-rust",
                "text/x-swift", "text/x-dart");
        registerMime(hashOnly,
                "text/x-python", "text/x-sh", "text/x-neon", "text/x-yaml",
                "text/x-ruby", "text/x-perl", "text/x-r", "text/x-toml",
                "text/x-properties", "text/x-dockerfile");
        registerMime(sql, "text/x-sql");
        registerMime(pascal, "text/x-pascal");
        registerMime(lineOnly(REM), "text/x-bat");
        registerMime(lua, "text/x-lua");
        registerMime(haskell, "text/x-haskell");
        registerMime(blockOnly(LATTE_BLOCK), "text/x-latte");
        registerMime(blockOnly(HTML_BLOCK), "text/html", "text/xml");
        registerMime(blockOnly(C_BLOCK), "text/css");
        registerMime(lineOnly(SEMICOLON), "text/x-ini");
        registerMime(lineOnly(PERCENT), "text/x-tex");

        registerExtension(cFamily,
                "php", "php3", "php4", "php5", "phtml",
                "java", "js", "mjs", "cjs", "ts", "tsx",
                "c", "h", "cc", "cpp", "cxx", "hh", "hpp", "hxx",
                "cs", "go", "kt", "kts", "rs", "swift", "dart");
        registerExtension(hashOnly,
                "py", "pyw", "pyi", "sh", "bash", "zsh",
                "neon", "yaml", "yml", "rb", "pl", "pm", "r",
                "toml", "properties", "dockerfile");
        registerExtension(sql, "sql");
        registerExtension(pascal, "pas", "pp", "dpr", "dpk", "lpr");
        registerExtension(lineOnly(REM), "bat", "cmd");
        registerExtension(lua, "lua");
        registerExtension(haskell, "hs", "lhs");
        registerExtension(blockOnly(LATTE_BLOCK), "latte");
        registerExtension(blockOnly(HTML_BLOCK), "html", "htm", "xhtml", "xml", "xsd", "xsl", "xslt", "svg");
        registerExtension(blockOnly(C_BLOCK), "css");
        registerExtension(lineOnly(SEMICOLON), "ini", "cfg");
        registerExtension(lineOnly(PERCENT), "tex", "sty", "cls");
    }

    private CommentStyleRegistry()
    {
    }

    public static LanguageCommentStyle get(String mimeType, String extension)
    {
        SmartCommentLog.LOG.info(
                "MIME: " + mimeType + ", přípona: " + extension
        );

        LanguageCommentStyle style = normalizeLookup(MIME_STYLES, mimeType);
        if (style != null) {
            return style;
        }

        return normalizeLookup(EXTENSION_STYLES, extension);
    }

    private static LanguageCommentStyle normalizeLookup(
            Map<String, LanguageCommentStyle> map,
            String key
    )
    {
        if (key == null || key.trim().isEmpty()) {
            return null;
        }
        return map.get(key.toLowerCase(Locale.ROOT));
    }

    private static void registerMime(LanguageCommentStyle style, String... mimeTypes)
    {
        for (String mimeType : mimeTypes) {
            MIME_STYLES.put(mimeType.toLowerCase(Locale.ROOT), style);
        }
    }

    private static void registerExtension(LanguageCommentStyle style, String... extensions)
    {
        for (String extension : extensions) {
            EXTENSION_STYLES.put(extension.toLowerCase(Locale.ROOT), style);
        }
    }

    private static LanguageCommentStyle lineOnly(LineCommentStyle line)
    {
        return new LanguageCommentStyle(line, null, CommentMode.LINE, CommentMode.LINE);
    }

    private static LanguageCommentStyle blockOnly(BlockCommentStyle block)
    {
        return new LanguageCommentStyle(null, block, CommentMode.BLOCK, CommentMode.BLOCK);
    }

    private static LanguageCommentStyle lineAndBlock(
            LineCommentStyle line,
            BlockCommentStyle block
    )
    {
        return new LanguageCommentStyle(line, block, CommentMode.LINE, CommentMode.LINE);
    }
}
