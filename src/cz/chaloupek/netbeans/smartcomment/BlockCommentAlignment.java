package cz.chaloupek.netbeans.smartcomment;

/**
 * Určuje umístění značek blokového komentáře vůči odsazení bloku.
 */
public enum BlockCommentAlignment
{
    /**
     * Značka komentáře se vloží před celý původní blok.
     *
     * Příklad:
     *
     * /*     value
     *        value *​/
     */
    BEFORE_BLOCK,

    /**
     * Značka komentáře se vloží za společné odsazení bloku.
     *
     * Příklad:
     *
     *     /* value
     *     value *​/
     */
    ALIGN_TO_BLOCK_INDENT
}
