package cz.chaloupek.netbeans.smartcomment.options;

import cz.chaloupek.netbeans.smartcomment.BlockCommentAlignment;
import cz.chaloupek.netbeans.smartcomment.LineCommentAlignment;
import cz.chaloupek.netbeans.smartcomment.PairedCommentLayout;
import cz.chaloupek.netbeans.smartcomment.style.CommentMode;
import java.util.Objects;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/** Ukládá a načítá uživatelské nastavení rozšíření. */
public final class SmartCommentSettings
{
    private static final String KEY_COMMENT_MODE = "commentMode";
    private static final String KEY_LINE_ALIGNMENT = "lineAlignment";
    private static final String KEY_BLOCK_ALIGNMENT = "blockAlignment";
    private static final String KEY_PAIRED_LAYOUT = "pairedLayout";

    private SmartCommentSettings()
    {
    }

    private static Preferences preferences()
    {
        return NbPreferences.forModule(SmartCommentSettings.class);
    }

    public static CommentMode getCommentMode()
    {
        return readEnum(KEY_COMMENT_MODE, CommentMode.AUTO, CommentMode.class);
    }

    public static void setCommentMode(CommentMode value)
    {
        putEnum(KEY_COMMENT_MODE, value);
    }

    public static LineCommentAlignment getLineAlignment()
    {
        return readEnum(
                KEY_LINE_ALIGNMENT,
                LineCommentAlignment.ALIGN_TO_FIRST_LINE,
                LineCommentAlignment.class
        );
    }

    public static void setLineAlignment(LineCommentAlignment value)
    {
        putEnum(KEY_LINE_ALIGNMENT, value);
    }

    public static BlockCommentAlignment getBlockAlignment()
    {
        return readEnum(
                KEY_BLOCK_ALIGNMENT,
                BlockCommentAlignment.ALIGN_TO_BLOCK_INDENT,
                BlockCommentAlignment.class
        );
    }

    public static void setBlockAlignment(BlockCommentAlignment value)
    {
        putEnum(KEY_BLOCK_ALIGNMENT, value);
    }

    public static PairedCommentLayout getPairedLayout()
    {
        return readEnum(
                KEY_PAIRED_LAYOUT,
                PairedCommentLayout.INLINE_BOUNDARIES,
                PairedCommentLayout.class
        );
    }

    public static void setPairedLayout(PairedCommentLayout value)
    {
        putEnum(KEY_PAIRED_LAYOUT, value);
    }

    public static void resetDefaults()
    {
        Preferences preferences = preferences();
        preferences.remove(KEY_COMMENT_MODE);
        preferences.remove(KEY_LINE_ALIGNMENT);
        preferences.remove(KEY_BLOCK_ALIGNMENT);
        preferences.remove(KEY_PAIRED_LAYOUT);
    }

    private static <E extends Enum<E>> E readEnum(
            String key,
            E defaultValue,
            Class<E> enumType
    )
    {
        String value = preferences().get(key, defaultValue.name());
        try {
            return Enum.valueOf(enumType, value);
        } catch (IllegalArgumentException ex) {
            return defaultValue;
        }
    }

    private static void putEnum(String key, Enum<?> value)
    {
        Objects.requireNonNull(value, "Ukládaná hodnota nesmí být null.");
        preferences().put(key, value.name());
    }
}
