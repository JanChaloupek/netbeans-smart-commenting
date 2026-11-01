package cz.chaloupek.netbeans.smartcomment;

import cz.chaloupek.netbeans.smartcomment.options.SmartCommentSettings;
import cz.chaloupek.netbeans.smartcomment.style.CommentStyleRegistry;
import cz.chaloupek.netbeans.smartcomment.style.LanguageCommentStyle;
import cz.chaloupek.netbeans.smartcomment.transform.CommentTransformer;
import java.util.Objects;
import java.util.logging.Level;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.openide.filesystems.FileObject;

/**
 * Propojuje editor NetBeans se společnou textovou transformací komentářů.
 *
 * Třída určuje zpracovávaný řádek nebo blok, vyhledá styl jazyka a předá
 * text třídě {@link CommentTransformer}. Všechny volby se načítají z
 * {@link SmartCommentSettings}, takže editor i živý náhled používají stejné
 * nastavení a stejný algoritmus.
 */
public final class SmartCommentProcessor
{
    private SmartCommentProcessor()
    {
    }

    public static void toggleSelection(JTextComponent editor)
    {
        Objects.requireNonNull(editor, "Editor nesmí být null.");
        Document document = editor.getDocument();
        LanguageCommentStyle language = getLanguageStyle(document);

        if (language == null) {
            SmartCommentLog.LOG.warning(
                    "Pro aktuální dokument nebyl nalezen styl komentování."
            );
            return;
        }

        if (editor.getSelectionStart() == editor.getSelectionEnd()) {
            toggleCurrentLine(editor, document, language);
        } else {
            toggleSelectedBlock(editor, document, language);
        }
    }

    private static LanguageCommentStyle getLanguageStyle(Document document)
    {
        String mimeType = NbEditorUtilities.getMimeType(document);
        FileObject file = NbEditorUtilities.getFileObject(document);
        String extension = file != null ? file.getExt() : null;
        return CommentStyleRegistry.get(mimeType, extension);
    }

    private static void toggleCurrentLine(
            JTextComponent editor,
            Document document,
            LanguageCommentStyle language
    )
    {
        try {
            int caret = editor.getCaretPosition();
            Element root = document.getDefaultRootElement();
            Element line = getLineElement(root, caret, document.getLength());
            int start = line.getStartOffset();
            int end = getContentEndOffset(line, document.getLength());
            String source = document.getText(start, end - start);
            String result = transform(source, language, false);

            document.remove(start, source.length());
            document.insertString(start, result, null);
            editor.setCaretPosition(Math.min(
                    caret + result.length() - source.length(),
                    document.getLength()
            ));
        } catch (Exception ex) {
            SmartCommentLog.LOG.log(Level.SEVERE, "Chyba při komentování řádku.", ex);
        }
    }

    private static void toggleSelectedBlock(
            JTextComponent editor,
            Document document,
            LanguageCommentStyle language
    )
    {
        try {
            Element root = document.getDefaultRootElement();
            int length = document.getLength();
            int selectionStart = clampPosition(editor.getSelectionStart(), length);
            int selectionEnd = normalizeSelectionEnd(
                    root, selectionStart, editor.getSelectionEnd(), length
            );
            Element firstLine = getLineElement(root, selectionStart, length);
            Element lastLine = getLineElement(root, selectionEnd, length);
            int start = firstLine.getStartOffset();
            int end = getContentEndOffset(lastLine, length);
            String source = document.getText(start, end - start);
            String result = transform(source, language, true);

            document.remove(start, source.length());
            document.insertString(start, result, null);
            editor.select(start, start + result.length());
        } catch (Exception ex) {
            SmartCommentLog.LOG.log(Level.SEVERE, "Chyba při komentování výběru.", ex);
        }
    }

    private static String transform(
            String source,
            LanguageCommentStyle language,
            boolean selection
    )
    {
        return CommentTransformer.toggle(
                source,
                language,
                SmartCommentSettings.getCommentMode(),
                SmartCommentSettings.getLineAlignment(),
                SmartCommentSettings.getBlockAlignment(),
                SmartCommentSettings.getPairedLayout(),
                selection
        );
    }

    private static Element getLineElement(Element root, int position, int length)
    {
        int safePosition = clampPosition(position, length);
        return root.getElement(root.getElementIndex(safePosition));
    }

    private static int getContentEndOffset(Element line, int documentLength)
    {
        return Math.min(
                Math.max(line.getStartOffset(), line.getEndOffset() - 1),
                documentLength
        );
    }

    private static int normalizeSelectionEnd(
            Element root,
            int selectionStart,
            int selectionEnd,
            int documentLength
    )
    {
        int safeEnd = clampPosition(selectionEnd, documentLength);
        if (safeEnd <= selectionStart || safeEnd == 0) return safeEnd;
        Element endLine = getLineElement(root, safeEnd, documentLength);
        return safeEnd == endLine.getStartOffset() ? safeEnd - 1 : safeEnd;
    }

    private static int clampPosition(int position, int documentLength)
    {
        return Math.max(0, Math.min(position, documentLength));
    }
}
