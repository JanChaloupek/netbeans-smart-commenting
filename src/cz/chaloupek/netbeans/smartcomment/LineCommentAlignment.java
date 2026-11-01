package cz.chaloupek.netbeans.smartcomment;

/**
 * Určuje umístění řádkových komentářů při zpracování více řádků.
 *
 * Zvolené zarovnání se použije při zakomentování bloku pomocí
 * řádkového komentářového prefixu.
 */
public enum LineCommentAlignment
{
    /**
     * Zachová odsazení každého řádku.
     *
     * Komentářový prefix se vloží bezprostředně před první nebílý znak
     * každého neprázdného řádku.
     */
    PRESERVE_INDENT,

    /**
     * Zarovná komentářové prefixy na společnou pozici.
     *
     * Společná pozice odpovídá nejmenšímu odsazení neprázdných řádků
     * ve zpracovávaném bloku.
     */
    ALIGN_TO_FIRST_LINE
}
