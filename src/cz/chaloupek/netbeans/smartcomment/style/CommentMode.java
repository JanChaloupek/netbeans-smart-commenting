package cz.chaloupek.netbeans.smartcomment.style;

/**
 * Určuje způsob komentování požadovaný uživatelem nebo konfigurací pluginu.
 *
 * Konkrétní režim může být vybrán přímo uživatelem nebo automaticky
 * podle nastavení jazyka a typu zpracovávaného textu.
 */
public enum CommentMode
{
    /**
     * Typ komentáře se vybere podle konfigurace konkrétního jazyka.
     *
     * Jazyk může používat jiný výchozí režim pro aktuální řádek
     * a jiný režim pro označený blok textu.
     */
    AUTO,

    /**
     * Použije se řádkový komentář.
     *
     * Typickými prefixy jsou například {@code //}, {@code #}
     * nebo {@code --}.
     */
    LINE,

    /**
     * Použije se blokový komentář tvořený počáteční a koncovou značkou.
     *
     * Jde například o komentáře jazyků C, Latte, HTML nebo XML.
     */
    BLOCK
}
