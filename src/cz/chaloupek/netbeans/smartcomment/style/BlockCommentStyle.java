package cz.chaloupek.netbeans.smartcomment.style;

import cz.chaloupek.netbeans.smartcomment.PairedCommentLayout;
import java.util.Objects;

/**
 * Popisuje blokový komentář tvořený počáteční a koncovou značkou.
 *
 * Třída podporuje rozložení značek na stejných řádcích s obsahem
 * i na samostatných řádcích. Instance je neměnná.
 *
 * Znak {@code \n} se zde používá záměrně. Metoda nevytváří soubor přímo,
 * ale text určený k vložení do editorového {@link javax.swing.text.Document},
 * který pracuje s logickými řádky oddělenými znakem LF.
 */
public final class BlockCommentStyle
{
    // =========================================================================
    // ZNAČKY BLOKOVÉHO KOMENTÁŘE
    // =========================================================================

    private final String opening;
    private final String closing;

    /**
     * Vytvoří styl blokového komentáře.
     *
     * @param opening počáteční značka blokového komentáře
     * @param closing koncová značka blokového komentáře
     * @throws IllegalArgumentException pokud je některá značka null nebo prázdná
     */
    public BlockCommentStyle(String opening, String closing)
    {
        if (opening == null || opening.isEmpty()
                || closing == null || closing.isEmpty()) {
            throw new IllegalArgumentException(
                    "Značky blokového komentáře nesmí být prázdné."
            );
        }

        this.opening = opening;
        this.closing = closing;
    }

    /** @return počáteční značka blokového komentáře */
    public String getOpening()
    {
        return opening;
    }

    /** @return koncová značka blokového komentáře */
    public String getClosing()
    {
        return closing;
    }

    // =========================================================================
    // DETEKCE BLOKOVÉHO KOMENTÁŘE
    // =========================================================================

    /**
     * Určí, zda blok odpovídá tomuto stylu a zadanému rozložení.
     *
     * @param block kontrolovaný blok textu
     * @param layout očekávané rozložení značek
     * @return {@code true}, pokud blok odpovídá stylu komentáře
     */
    public boolean isCommented(String block, PairedCommentLayout layout)
    {
        Objects.requireNonNull(block, "Kontrolovaný blok nesmí být null.");

        String separator = getBoundarySeparator(layout);
        return block.startsWith(opening + separator)
                && block.endsWith(separator + closing);
    }

    // =========================================================================
    // PŘIDÁNÍ A ODEBRÁNÍ KOMENTÁŘE
    // =========================================================================

    /**
     * Obalí blok textu značkami blokového komentáře.
     *
     * Metoda nekontroluje, zda je blok již komentovaný.
     *
     * @param block blok určený k zakomentování
     * @param layout požadované rozložení značek
     * @return zakomentovaný blok
     */
    public String comment(String block, PairedCommentLayout layout)
    {
        Objects.requireNonNull(block, "Komentovaný blok nesmí být null.");

        String separator = getBoundarySeparator(layout);
        return opening + separator + block + separator + closing;
    }

    /**
     * Odstraní značky blokového komentáře.
     *
     * Pokud blok neodpovídá stylu a rozložení, vrátí jej beze změny.
     *
     * @param block blok určený k odkomentování
     * @param layout očekávané rozložení značek
     * @return odkomentovaný nebo původní blok
     */
    public String uncomment(String block, PairedCommentLayout layout)
    {
        if (!isCommented(block, layout)) {
            return block;
        }

        String separator = getBoundarySeparator(layout);
        int prefixLength = opening.length() + separator.length();
        int suffixLength = separator.length() + closing.length();

        return block.substring(prefixLength, block.length() - suffixLength);
    }

    // =========================================================================
    // PŘEPNUTÍ STAVU KOMENTÁŘE
    // =========================================================================

    /**
     * Přepne stav blokového komentáře.
     *
     * @param block zpracovávaný blok textu
     * @param layout rozložení značek komentáře
     * @return blok s přepnutým stavem komentáře
     */
    public String toggle(String block, PairedCommentLayout layout)
    {
        return isCommented(block, layout)
                ? uncomment(block, layout)
                : comment(block, layout);
    }

    // =========================================================================
    // INTERNÍ ZPRACOVÁNÍ ROZLOŽENÍ
    // =========================================================================

    /**
     * Vrátí oddělovač odpovídající rozložení komentáře.
     *
     * Pro rozložení na samostatných řádcích se vrací logický znak nového
     * řádku používaný uvnitř editorového dokumentu, nikoliv systémový
     * oddělovač řádků podle operačního systému.
     *
     * @param layout rozložení značek blokového komentáře
     * @return mezera nebo logický znak nového řádku
     */
    private String getBoundarySeparator(PairedCommentLayout layout)
    {
        Objects.requireNonNull(
                layout,
                "Rozložení blokového komentáře nesmí být null."
        );

        switch (layout) {
            case INLINE_BOUNDARIES:
                return " ";
            case SEPARATE_LINES:
                return "\n";
            default:
                throw new IllegalStateException(
                        "Nepodporované rozložení: " + layout
                );
        }
    }
}
