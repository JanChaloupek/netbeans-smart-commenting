package cz.chaloupek.netbeans.smartcomment;

import java.util.logging.Logger;

/**
 * Poskytuje společný logger rozšíření Smart Comment.
 *
 * Třída slouží pouze jako držitel sdílené statické instance loggeru
 * a nelze ji vytvořit pomocí konstruktoru.
 */
public final class SmartCommentLog
{
    // =========================================================================
    // SDÍLENÝ LOGGER ROZŠÍŘENÍ
    // =========================================================================

    /**
     * Logger používaný všemi částmi rozšíření Smart Comment.
     */
    public static final Logger LOG = Logger.getLogger("SmartComment");

    /**
     * Zabrání vytvoření instance pomocné statické třídy.
     */
    private SmartCommentLog()
    {
    }
}
