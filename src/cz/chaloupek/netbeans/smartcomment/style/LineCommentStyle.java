package cz.chaloupek.netbeans.smartcomment.style;

import java.util.Objects;

/**
 * Popisuje řádkový komentář tvořený prefixem.
 *
 * Komentář se vkládá za původní odsazení řádku. Instance je neměnná.
 */
public final class LineCommentStyle
{
    // =========================================================================
    // KONFIGURACE ŘÁDKOVÉHO KOMENTÁŘE
    // =========================================================================

    private final String prefix;
    private final boolean spaceAfterPrefix;

    /**
     * Vytvoří styl řádkového komentáře s mezerou za prefixem.
     *
     * @param prefix prefix řádkového komentáře
     */
    public LineCommentStyle(String prefix)
    {
        this(prefix, true);
    }

    /**
     * Vytvoří styl řádkového komentáře.
     *
     * @param prefix prefix řádkového komentáře
     * @param spaceAfterPrefix zda se za prefix automaticky přidá mezera
     */
    public LineCommentStyle(String prefix, boolean spaceAfterPrefix)
    {
        if (prefix == null || prefix.isEmpty()) {
            throw new IllegalArgumentException("Prefix komentáře nesmí být prázdný.");
        }
        this.prefix = prefix;
        this.spaceAfterPrefix = spaceAfterPrefix;
    }

    /** @return samotný prefix řádkového komentáře */
    public String getPrefix()
    {
        return prefix;
    }

    /** @return prefix včetně případné koncové mezery */
    public String getPrefixWithSpacing()
    {
        return spaceAfterPrefix ? prefix + " " : prefix;
    }

    // =========================================================================
    // DETEKCE ŘÁDKOVÉHO KOMENTÁŘE
    // =========================================================================

    /**
     * Určí, zda první text za odsazením začíná prefixem komentáře.
     *
     * @param line kontrolovaný řádek
     * @return {@code true}, pokud je řádek komentovaný
     */
    public boolean isCommented(String line)
    {
        Objects.requireNonNull(line, "Kontrolovaný řádek nesmí být null.");
        int position = findFirstNonWhitespace(line);
        return line.substring(position).startsWith(prefix);
    }

    // =========================================================================
    // PŘIDÁNÍ ŘÁDKOVÉHO KOMENTÁŘE
    // =========================================================================

    /**
     * Zakomentuje řádek za jeho původním odsazením.
     *
     * Prázdný řádek nebo řádek obsahující pouze bílé znaky vrátí beze změny.
     *
     * @param line řádek určený k zakomentování
     * @return zakomentovaný nebo původní prázdný řádek
     */
    public String commentLine(String line)
    {
        Objects.requireNonNull(line, "Komentovaný řádek nesmí být null.");
        if (line.trim().isEmpty()) {
            return line;
        }
        int position = findFirstNonWhitespace(line);
        return line.substring(0, position) + commentText(line.substring(position));
    }

    /**
     * Přidá prefix přímo před text bez vyhodnocení odsazení.
     *
     * @param text text určený k zakomentování
     * @return text s prefixem nebo původní prázdný text
     */
    public String commentText(String text)
    {
        Objects.requireNonNull(text, "Komentovaný text nesmí být null.");
        return text.isEmpty() ? text : getPrefixWithSpacing() + text;
    }

    // =========================================================================
    // ODEBRÁNÍ ŘÁDKOVÉHO KOMENTÁŘE
    // =========================================================================

    /**
     * Odstraní řádkový komentář a zachová původní odsazení.
     *
     * @param line řádek určený k odkomentování
     * @return odkomentovaný nebo původní řádek
     */
    public String uncommentLine(String line)
    {
        Objects.requireNonNull(line, "Odkomentovaný řádek nesmí být null.");
        int position = findFirstNonWhitespace(line);
        String indentation = line.substring(0, position);
        String text = line.substring(position);
        String prefixWithSpacing = getPrefixWithSpacing();
        if (text.startsWith(prefixWithSpacing)) {
            return indentation + text.substring(prefixWithSpacing.length());
        }
        if (text.startsWith(prefix)) {
            return indentation + text.substring(prefix.length());
        }
        return line;
    }

    // =========================================================================
    // PŘEPNUTÍ STAVU KOMENTÁŘE
    // =========================================================================

    /**
     * Přepne stav řádkového komentáře.
     *
     * @param line zpracovávaný řádek
     * @return řádek s přepnutým stavem komentáře
     */
    public String toggleLine(String line)
    {
        return isCommented(line) ? uncommentLine(line) : commentLine(line);
    }

    // =========================================================================
    // INTERNÍ ZPRACOVÁNÍ ODSAZENÍ
    // =========================================================================

    /**
     * Vrátí pozici prvního nebílého znaku nebo délku řádku.
     *
     * @param line prohledávaný řádek
     * @return pozice prvního nebílého znaku nebo délka řádku
     */
    private int findFirstNonWhitespace(String line)
    {
        for (int i = 0; i < line.length(); i++) {
            if (!Character.isWhitespace(line.charAt(i))) {
                return i;
            }
        }
        return line.length();
    }
}
