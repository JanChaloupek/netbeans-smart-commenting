package cz.chaloupek.netbeans.smartcomment;

/**
 * Určuje rozmístění značek párového komentáře při komentování bloku.
 */
public enum PairedCommentLayout
{
    /**
     * Počáteční a koncová značka komentáře se vloží na samostatné řádky.
     */
    SEPARATE_LINES,

    /**
     * Počáteční značka se vloží před obsah a koncová značka za obsah.
     *
     * Značky zůstávají na stejných hraničních řádcích jako komentovaný text.
     */
    INLINE_BOUNDARIES
}
