package cz.chaloupek.netbeans.smartcomment.style;

import java.util.Objects;

/**
 * Popisuje podporované způsoby komentování jednoho jazyka.
 *
 * Jazyk může podporovat řádkový komentář, blokový komentář nebo oba
 * způsoby. Instance je neměnná.
 */
public final class LanguageCommentStyle
{
    // =========================================================================
    // PODPOROVANÉ STYLY A VÝCHOZÍ REŽIMY
    // =========================================================================

    private final LineCommentStyle lineStyle;
    private final BlockCommentStyle blockStyle;
    private final CommentMode singleLineMode;
    private final CommentMode selectionMode;

    /**
     * Vytvoří konfiguraci komentářů jednoho jazyka.
     *
     * @param lineStyle styl řádkového komentáře, nebo {@code null}
     * @param blockStyle styl blokového komentáře, nebo {@code null}
     * @param singleLineMode výchozí režim pro jeden řádek
     * @param selectionMode výchozí režim pro označený blok
     */
    public LanguageCommentStyle(
            LineCommentStyle lineStyle,
            BlockCommentStyle blockStyle,
            CommentMode singleLineMode,
            CommentMode selectionMode
    )
    {
        if (singleLineMode == CommentMode.AUTO || selectionMode == CommentMode.AUTO) {
            throw new IllegalArgumentException("Výchozí režim jazyka musí být LINE nebo BLOCK.");
        }        
        if (lineStyle == null && blockStyle == null) {
            throw new IllegalArgumentException(
                    "Jazyk musí podporovat alespoň jeden způsob komentování."
            );
        }
        this.lineStyle = lineStyle;
        this.blockStyle = blockStyle;
        this.singleLineMode = Objects.requireNonNull(
                singleLineMode, "Výchozí režim jednoho řádku nesmí být null."
        );
        this.selectionMode = Objects.requireNonNull(
                selectionMode, "Výchozí režim označeného bloku nesmí být null."
        );
    }

    // =========================================================================
    // DETEKCE PODPOROVANÝCH STYLŮ
    // =========================================================================

    /** @return {@code true}, pokud jazyk podporuje řádkový komentář */
    public boolean supportsLineComment()
    {
        return lineStyle != null;
    }

    /** @return {@code true}, pokud jazyk podporuje blokový komentář */
    public boolean supportsBlockComment()
    {
        return blockStyle != null;
    }

    // =========================================================================
    // PŘÍSTUP KE STYLŮM
    // =========================================================================

    /**
     * @return styl řádkového komentáře
     * @throws IllegalStateException pokud jej jazyk nepodporuje
     */
    public LineCommentStyle getLineStyle()
    {
        if (lineStyle == null) {
            throw new IllegalStateException("Jazyk nepodporuje řádkové komentáře.");
        }
        return lineStyle;
    }

    /**
     * @return styl blokového komentáře
     * @throws IllegalStateException pokud jej jazyk nepodporuje
     */
    public BlockCommentStyle getBlockStyle()
    {
        if (blockStyle == null) {
            throw new IllegalStateException("Jazyk nepodporuje blokové komentáře.");
        }
        return blockStyle;
    }

    // =========================================================================
    // VÝBĚR REŽIMU KOMENTOVÁNÍ
    // =========================================================================

    /**
     * Určí výsledný podporovaný režim komentování.
     *
     * Při AUTO vybere výchozí režim podle toho, zda je aktivní výběr.
     * Pokud jazyk zvolený režim nepodporuje, použije dostupný režim;
     * řádkový komentář má při náhradním výběru přednost.
     *
     * @param requestedMode požadovaný režim
     * @param selection {@code true}, pokud se zpracovává označený blok
     * @return konkrétní podporovaný režim LINE nebo BLOCK
     */
    public CommentMode resolveMode(CommentMode requestedMode, boolean selection)
    {
        Objects.requireNonNull(requestedMode, "Požadovaný režim nesmí být null.");
        CommentMode mode = requestedMode;
        if (mode == CommentMode.AUTO) {
            mode = selection ? selectionMode : singleLineMode;
        }
        if (mode == CommentMode.LINE && supportsLineComment()) {
            return CommentMode.LINE;
        }
        if (mode == CommentMode.BLOCK && supportsBlockComment()) {
            return CommentMode.BLOCK;
        }
        return supportsLineComment() ? CommentMode.LINE : CommentMode.BLOCK;
    }
}
