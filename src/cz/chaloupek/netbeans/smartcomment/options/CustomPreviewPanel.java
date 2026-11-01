package cz.chaloupek.netbeans.smartcomment.options;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Editovatelná záložka živého náhledu pro vlastní zdrojový text.
 *
 * Náhled napodobuje chování skutečné editorové akce. Bez označeného textu
 * se transformuje řádek obsahující kurzor. Pokud uživatel označí text,
 * výběr se rozšíří na celé dotčené řádky a transformuje se jako blok.
 */
public final class CustomPreviewPanel extends JPanel
{
    private static final String DESCRIPTION =
            "Umístěte kurzor na řádek, nebo označte část kódu. "
            + "Bez výběru se zpracuje aktuální řádek; s výběrem všechny dotčené řádky.";

    private final JTextArea sourceArea = createArea(true);
    private final JTextArea resultArea = createArea(false);
    private Runnable changeListener = () -> { };
    private boolean updatingSource;

    public CustomPreviewPanel()
    {
        setLayout(new BorderLayout(5, 5));

        JLabel descriptionLabel = new JLabel(DESCRIPTION);
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(0, 3, 3, 3));
        add(descriptionLabel, BorderLayout.NORTH);

        JScrollPane sourceScroll = new JScrollPane(sourceArea);
        sourceScroll.setBorder(BorderFactory.createTitledBorder("Vlastní kód"));

        JScrollPane resultScroll = new JScrollPane(resultArea);
        resultScroll.setBorder(BorderFactory.createTitledBorder("Výsledek"));

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                sourceScroll,
                resultScroll
        );
        split.setResizeWeight(0.5);
        split.setContinuousLayout(true);
        add(split, BorderLayout.CENTER);

        sourceArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event)
            {
                notifyChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent event)
            {
                notifyChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent event)
            {
                notifyChanged();
            }
        });

        sourceArea.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent event)
            {
                notifyChanged();
            }
        });
    }

    public void setChangeListener(Runnable listener)
    {
        changeListener = listener != null ? listener : () -> { };
    }

    public String getSourceText()
    {
        return sourceArea.getText();
    }

    public void setSourceText(String text)
    {
        updatingSource = true;
        try {
            sourceArea.setText(text != null ? text : "");
            sourceArea.setCaretPosition(0);
        } finally {
            updatingSource = false;
        }
        notifyChanged();
    }

    public void setResultText(String text)
    {
        resultArea.setText(text != null ? text : "");
        resultArea.setCaretPosition(0);
    }

    /**
     * @return aktuální pozice kurzoru v editovatelném náhledu
     */
    public int getCaretPosition()
    {
        return sourceArea.getCaretPosition();
    }

    /**
     * @return začátek označeného textu
     */
    public int getSelectionStart()
    {
        return sourceArea.getSelectionStart();
    }

    /**
     * @return konec označeného textu
     */
    public int getSelectionEnd()
    {
        return sourceArea.getSelectionEnd();
    }

    /**
     * @return {@code true}, pokud uživatel označil neprázdný rozsah
     */
    public boolean hasSelection()
    {
        return getSelectionStart() != getSelectionEnd();
    }

    private void notifyChanged()
    {
        if (!updatingSource) {
            changeListener.run();
        }
    }

    private static JTextArea createArea(boolean editable)
    {
        JTextArea area = new JTextArea(7, 24);
        area.setEditable(editable);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setTabSize(4);
        return area;
    }
}
