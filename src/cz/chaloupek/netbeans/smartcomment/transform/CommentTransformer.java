package cz.chaloupek.netbeans.smartcomment.transform;

import cz.chaloupek.netbeans.smartcomment.BlockCommentAlignment;
import cz.chaloupek.netbeans.smartcomment.LineCommentAlignment;
import cz.chaloupek.netbeans.smartcomment.PairedCommentLayout;
import cz.chaloupek.netbeans.smartcomment.style.BlockCommentStyle;
import cz.chaloupek.netbeans.smartcomment.style.CommentMode;
import cz.chaloupek.netbeans.smartcomment.style.LanguageCommentStyle;
import cz.chaloupek.netbeans.smartcomment.style.LineCommentStyle;
import java.util.Objects;

/**
 * Provádí čistou textovou transformaci komentářů bez závislosti na editoru.
 *
 * Stejnou třídu používá editorová akce i živý náhled v nastavení. Náhled
 * proto zobrazuje přesně stejný výsledek, jaký bude vložen do dokumentu.
 */
public final class CommentTransformer
{
    private CommentTransformer()
    {
    }

    /**
     * Přepne komentář předaného textu podle stylu jazyka a nastavení.
     *
     * @param text zpracovávaný řádek nebo blok
     * @param language styly podporované jazykem
     * @param requestedMode požadovaný režim komentování
     * @param lineAlignment zarovnání řádkových komentářů
     * @param blockAlignment zarovnání blokového komentáře
     * @param pairedLayout rozložení značek blokového komentáře
     * @param selection {@code true}, pokud text představuje výběr
     * @return text s přepnutým stavem komentáře
     */
    public static String toggle(
            String text,
            LanguageCommentStyle language,
            CommentMode requestedMode,
            LineCommentAlignment lineAlignment,
            BlockCommentAlignment blockAlignment,
            PairedCommentLayout pairedLayout,
            boolean selection
    )
    {
        Objects.requireNonNull(text, "Zpracovávaný text nesmí být null.");
        Objects.requireNonNull(language, "Styl jazyka nesmí být null.");
        Objects.requireNonNull(requestedMode, "Režim komentování nesmí být null.");
        Objects.requireNonNull(lineAlignment, "Zarovnání řádkového komentáře nesmí být null.");
        Objects.requireNonNull(blockAlignment, "Zarovnání blokového komentáře nesmí být null.");
        Objects.requireNonNull(pairedLayout, "Rozložení blokového komentáře nesmí být null.");

        CommentMode mode = language.resolveMode(requestedMode, selection);

        if (mode == CommentMode.LINE) {
            LineCommentStyle style = language.getLineStyle();
            return selection
                    ? toggleLineBlock(text, style, lineAlignment)
                    : style.toggleLine(text);
        }

        return toggleBlockComment(
                text,
                language.getBlockStyle(),
                blockAlignment,
                pairedLayout
        );
    }

    private static String toggleLineBlock(
            String block,
            LineCommentStyle style,
            LineCommentAlignment alignment
    )
    {
        return alignment == LineCommentAlignment.ALIGN_TO_FIRST_LINE
                ? toggleAlignedLineBlock(block, style)
                : togglePreservedLineBlock(block, style);
    }

    private static String toggleAlignedLineBlock(
            String block,
            LineCommentStyle style
    )
    {
        String[] lines = block.split("\\n", -1);
        boolean uncomment = areAllCommented(lines, style);
        int baseIndent = findBaseIndent(lines);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            if (!line.trim().isEmpty()) {
                if (uncomment) {
                    line = style.uncommentLine(line);
                } else {
                    int split = Math.min(baseIndent, line.length());
                    line = line.substring(0, split)
                            + style.commentText(line.substring(split));
                }
            }

            if (i > 0) result.append('\n');
            result.append(line);
        }

        return result.toString();
    }

    private static String togglePreservedLineBlock(
            String block,
            LineCommentStyle style
    )
    {
        String[] lines = block.split("\\n", -1);
        boolean uncomment = areAllCommented(lines, style);

        for (int i = 0; i < lines.length; i++) {
            if (!lines[i].trim().isEmpty()) {
                lines[i] = uncomment
                        ? style.uncommentLine(lines[i])
                        : style.commentLine(lines[i]);
            }
        }

        return String.join("\n", lines);
    }

    private static String toggleBlockComment(
            String block,
            BlockCommentStyle style,
            BlockCommentAlignment alignment,
            PairedCommentLayout layout
    )
    {
        if (alignment != BlockCommentAlignment.ALIGN_TO_BLOCK_INDENT) {
            return style.toggle(block, layout);
        }

        String[] lines = block.split("\\n", -1);
        int baseIndent = findBaseIndent(lines);
        String indentation = getCommonIndentation(lines, baseIndent);
        String content = removeCommonIndentation(lines, baseIndent);
        String toggled = style.toggle(content, layout);

        return addCommonIndentation(toggled, indentation);
    }

    private static boolean areAllCommented(
            String[] lines,
            LineCommentStyle style
    )
    {
        boolean found = false;

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            found = true;
            if (!style.isCommented(line)) return false;
        }

        return found;
    }

    private static int findBaseIndent(String[] lines)
    {
        int result = Integer.MAX_VALUE;

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                result = Math.min(result, getIndent(line));
            }
        }

        return result == Integer.MAX_VALUE ? 0 : result;
    }

    private static int getIndent(String line)
    {
        int result = 0;
        while (result < line.length()
                && Character.isWhitespace(line.charAt(result))) {
            result++;
        }
        return result;
    }

    private static String getCommonIndentation(String[] lines, int baseIndent)
    {
        if (baseIndent <= 0) return "";

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                return line.substring(0, Math.min(baseIndent, line.length()));
            }
        }

        return "";
    }

    private static String removeCommonIndentation(
            String[] lines,
            int baseIndent
    )
    {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (!line.trim().isEmpty()) {
                line = line.substring(Math.min(baseIndent, line.length()));
            }
            if (i > 0) result.append('\n');
            result.append(line);
        }

        return result.toString();
    }

    private static String addCommonIndentation(
            String block,
            String indentation
    )
    {
        if (indentation.isEmpty()) return block;

        String[] lines = block.split("\\n", -1);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (!line.trim().isEmpty()) line = indentation + line;
            if (i > 0) result.append('\n');
            result.append(line);
        }

        return result.toString();
    }
}
