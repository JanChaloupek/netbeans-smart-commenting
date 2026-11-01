package cz.chaloupek.netbeans.smartcomment.options;

import cz.chaloupek.netbeans.smartcomment.BlockCommentAlignment;
import cz.chaloupek.netbeans.smartcomment.LineCommentAlignment;
import cz.chaloupek.netbeans.smartcomment.PairedCommentLayout;
import cz.chaloupek.netbeans.smartcomment.style.CommentMode;
import cz.chaloupek.netbeans.smartcomment.style.CommentStyleRegistry;
import cz.chaloupek.netbeans.smartcomment.style.LanguageCommentStyle;
import cz.chaloupek.netbeans.smartcomment.transform.CommentTransformer;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import org.netbeans.spi.options.OptionsPanelController;

/**
 * Panel nastavení rozšíření s živým náhledem všech podporovaných jazyků.
 *
 * Náhled obsahuje dvě samostatné situace:
 * - komentování jednoho aktuálního řádku,
 * - komentování víceřádkového výběru.
 *
 * Oba výsledky respektují právě vybraný režim a způsob zarovnání.
 */
@OptionsPanelController.Keywords(
        location = "Advanced",
        tabTitle = "Smart Commenting",
        keywords = {"comment", "komentář", "indent", "odsazení", "preview", "náhled"}
)
public final class SmartCommentOptionsPanel extends JPanel
{
    private final SmartCommentOptionsPanelController controller;

    private final JComboBox<CommentMode> commentModeCombo =
            new JComboBox<>(CommentMode.values());
    private final JComboBox<LineCommentAlignment> lineAlignmentCombo =
            new JComboBox<>(LineCommentAlignment.values());
    private final JComboBox<BlockCommentAlignment> blockAlignmentCombo =
            new JComboBox<>(BlockCommentAlignment.values());
    private final JComboBox<PairedCommentLayout> pairedLayoutCombo =
            new JComboBox<>(PairedCommentLayout.values());
    private final JComboBox<PreviewLanguage> previewLanguageCombo =
            new JComboBox<>(PreviewLanguage.values());

    private final JLabel capabilitiesLabel = new JLabel();

    private final JTextArea singleSourcePreview = createPreviewArea(3);
    private final JTextArea singleResultPreview = createPreviewArea(3);
    private final JTextArea blockSourcePreview = createPreviewArea(4);
    private final JTextArea blockResultPreview = createPreviewArea(4);

    private final CustomPreviewPanel customPreviewPanel = new CustomPreviewPanel();

    private boolean loading;
    private boolean changed;

    SmartCommentOptionsPanel(SmartCommentOptionsPanelController controller)
    {
        this.controller = controller;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(createSettingsPanel(), BorderLayout.NORTH);
        add(createPreviewPanel(), BorderLayout.CENTER);
        installListeners();
    }

    private JPanel createSettingsPanel()
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Nastavení"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 5, 3, 5);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addRow(panel, c, row++, "Režim komentování:", commentModeCombo);
        addRow(panel, c, row++, "Zarovnání řádkových komentářů:", lineAlignmentCombo);
        addRow(panel, c, row++, "Zarovnání blokových komentářů:", blockAlignmentCombo);
        addRow(panel, c, row++, "Rozložení blokového komentáře:", pairedLayoutCombo);
        addRow(panel, c, row++, "Jazyk náhledu:", previewLanguageCombo);

        c.gridx = 1;
        c.gridy = row++;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        panel.add(capabilitiesLabel, c);

        JButton defaultsButton = new JButton("Obnovit výchozí nastavení");
        defaultsButton.addActionListener(event -> restoreDefaultsInControls());
        c.gridx = 1;
        c.gridy = row;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        panel.add(defaultsButton, c);
        return panel;
    }

    private JTabbedPane createPreviewPanel()
    {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBorder(BorderFactory.createTitledBorder("Živý náhled"));
        tabs.addTab(
                "Jeden řádek",
                createComparisonPanel(singleSourcePreview, singleResultPreview)
        );
        tabs.addTab(
                "Víceřádkový výběr",
                createComparisonPanel(blockSourcePreview, blockResultPreview)
        );
        tabs.addTab("Vlastní kód", customPreviewPanel);
        return tabs;
    }

    private static JPanel createComparisonPanel(
            JTextArea source,
            JTextArea result
    )
    {
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane sourceScroll = new JScrollPane(source);
        sourceScroll.setBorder(BorderFactory.createTitledBorder("Původní text"));
        JScrollPane resultScroll = new JScrollPane(result);
        resultScroll.setBorder(BorderFactory.createTitledBorder("Výsledek"));

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                sourceScroll,
                resultScroll
        );
        split.setResizeWeight(0.5);
        split.setContinuousLayout(true);
        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    private static void addRow(
            JPanel panel,
            GridBagConstraints c,
            int row,
            String label,
            JComboBox<?> combo
    )
    {
        c.gridy = row;
        c.gridx = 0;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(label), c);

        c.gridx = 1;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(combo, c);
    }

    private static JTextArea createPreviewArea(int rows)
    {
        JTextArea area = new JTextArea(rows, 28);
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setTabSize(4);
        return area;
    }

    private void installListeners()
    {
        commentModeCombo.addActionListener(event -> settingsChanged());
        lineAlignmentCombo.addActionListener(event -> settingsChanged());
        blockAlignmentCombo.addActionListener(event -> settingsChanged());
        pairedLayoutCombo.addActionListener(event -> settingsChanged());
        previewLanguageCombo.addActionListener(event -> updatePreview());
        customPreviewPanel.setChangeListener(this::updateCustomPreview);
    }

    void load()
    {
        loading = true;
        try {
            commentModeCombo.setSelectedItem(SmartCommentSettings.getCommentMode());
            lineAlignmentCombo.setSelectedItem(SmartCommentSettings.getLineAlignment());
            blockAlignmentCombo.setSelectedItem(SmartCommentSettings.getBlockAlignment());
            pairedLayoutCombo.setSelectedItem(SmartCommentSettings.getPairedLayout());
            if (previewLanguageCombo.getSelectedItem() == null) {
                previewLanguageCombo.setSelectedItem(PreviewLanguage.PHP);
            }
            updatePreview();
        } finally {
            loading = false;
        }
    }

    void store()
    {
        SmartCommentSettings.setCommentMode(selected(commentModeCombo));
        SmartCommentSettings.setLineAlignment(selected(lineAlignmentCombo));
        SmartCommentSettings.setBlockAlignment(selected(blockAlignmentCombo));
        SmartCommentSettings.setPairedLayout(selected(pairedLayoutCombo));
    }

    boolean isConfigurationValid()
    {
        return commentModeCombo.getSelectedItem() != null
                && lineAlignmentCombo.getSelectedItem() != null
                && blockAlignmentCombo.getSelectedItem() != null
                && pairedLayoutCombo.getSelectedItem() != null;
    }

    boolean isChanged()
    {
        return changed;
    }

    void setChanged(boolean value)
    {
        boolean old = changed;
        changed = value;
        if (old != value) {
            firePropertyChange(OptionsPanelController.PROP_CHANGED, old, value);
        }
        firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }

    private void settingsChanged()
    {
        if (!loading) controller.changed();
        updatePreview();
    }

    private void restoreDefaultsInControls()
    {
        loading = true;
        try {
            commentModeCombo.setSelectedItem(CommentMode.AUTO);
            lineAlignmentCombo.setSelectedItem(LineCommentAlignment.ALIGN_TO_FIRST_LINE);
            blockAlignmentCombo.setSelectedItem(BlockCommentAlignment.ALIGN_TO_BLOCK_INDENT);
            pairedLayoutCombo.setSelectedItem(PairedCommentLayout.INLINE_BOUNDARIES);
        } finally {
            loading = false;
        }
        controller.changed();
        updatePreview();
    }

    private void updatePreview()
    {
        PreviewLanguage previewLanguage = selected(previewLanguageCombo);
        if (previewLanguage == null) return;

        LanguageCommentStyle language = CommentStyleRegistry.get(
                previewLanguage.getMimeType(),
                previewLanguage.getExtension()
        );

        String singleSource = PreviewSamples.getSingleLineSample(previewLanguage);
        String blockSource = PreviewSamples.getBlockSample(previewLanguage);
        setPreviewSource(singleSourcePreview, singleSource);
        setPreviewSource(blockSourcePreview, blockSource);

        if (language == null || !isConfigurationValid()) {
            String unavailable = "Pro vybraný jazyk není dostupný styl komentování.";
            setPreviewSource(singleResultPreview, unavailable);
            setPreviewSource(blockResultPreview, unavailable);
            capabilitiesLabel.setText("Podporované styly: nezjištěno");
            return;
        }

        capabilitiesLabel.setText(createCapabilitiesText(language));

        String singleResult = CommentTransformer.toggle(
                singleSource,
                language,
                selected(commentModeCombo),
                selected(lineAlignmentCombo),
                selected(blockAlignmentCombo),
                selected(pairedLayoutCombo),
                false
        );
        String blockResult = CommentTransformer.toggle(
                blockSource,
                language,
                selected(commentModeCombo),
                selected(lineAlignmentCombo),
                selected(blockAlignmentCombo),
                selected(pairedLayoutCombo),
                true
        );

        setPreviewSource(singleResultPreview, singleResult);
        setPreviewSource(blockResultPreview, blockResult);

        if (customPreviewPanel.getSourceText().isEmpty()) {
            customPreviewPanel.setSourceText(blockSource);
        }
        updateCustomPreview();
    }

    /**
     * Aktualizuje vlastní náhled podle skutečné polohy kurzoru nebo výběru.
     *
     * Bez výběru se transformuje pouze aktuální řádek. Při existujícím
     * výběru se rozsah rozšíří na celé dotčené řádky stejně jako v editoru.
     * Výsledkové pole zobrazuje celý zdrojový text, aby bylo zřejmé, která
     * část byla změněna.
     */
    private void updateCustomPreview()
    {
        PreviewLanguage previewLanguage = selected(previewLanguageCombo);
        if (previewLanguage == null || !isConfigurationValid()) {
            return;
        }

        LanguageCommentStyle language = CommentStyleRegistry.get(
                previewLanguage.getMimeType(),
                previewLanguage.getExtension()
        );
        if (language == null) {
            customPreviewPanel.setResultText(
                    "Pro vybraný jazyk není dostupný styl komentování."
            );
            return;
        }

        String source = customPreviewPanel.getSourceText();
        if (source.isEmpty()) {
            customPreviewPanel.setResultText("");
            return;
        }

        boolean selection = customPreviewPanel.hasSelection();
        int rangeStart;
        int rangeEnd;

        if (selection) {
            rangeStart = findLineStart(
                    source,
                    customPreviewPanel.getSelectionStart()
            );
            rangeEnd = findSelectionBlockEnd(
                    source,
                    customPreviewPanel.getSelectionStart(),
                    customPreviewPanel.getSelectionEnd()
            );
        } else {
            int caret = customPreviewPanel.getCaretPosition();
            rangeStart = findLineStart(source, caret);
            rangeEnd = findLineEnd(source, caret);
        }

        String transformedPart = CommentTransformer.toggle(
                source.substring(rangeStart, rangeEnd),
                language,
                selected(commentModeCombo),
                selected(lineAlignmentCombo),
                selected(blockAlignmentCombo),
                selected(pairedLayoutCombo),
                selection
        );

        String result = source.substring(0, rangeStart)
                + transformedPart
                + source.substring(rangeEnd);
        customPreviewPanel.setResultText(result);
    }

    /**
     * Vrátí začátek řádku obsahujícího zadanou pozici.
     */
    private static int findLineStart(String text, int position)
    {
        int safePosition = Math.max(0, Math.min(position, text.length()));
        int lineStart = text.lastIndexOf('\n', Math.max(0, safePosition - 1));
        return lineStart < 0 ? 0 : lineStart + 1;
    }

    /**
     * Vrátí konec řádku bez znaku nového řádku.
     */
    private static int findLineEnd(String text, int position)
    {
        int safePosition = Math.max(0, Math.min(position, text.length()));
        int lineEnd = text.indexOf('\n', safePosition);
        return lineEnd < 0 ? text.length() : lineEnd;
    }

    /**
     * Vrátí konec posledního řádku dotčeného výběrem.
     *
     * Pokud výběr končí přesně na začátku dalšího řádku, tento řádek
     * se do náhledu nezahrne.
     */
    private static int findSelectionBlockEnd(
            String text,
            int selectionStart,
            int selectionEnd
    )
    {
        int safeEnd = Math.max(0, Math.min(selectionEnd, text.length()));

        if (safeEnd > selectionStart
                && safeEnd > 0
                && text.charAt(safeEnd - 1) == '\n') {
            safeEnd--;
        }

        return findLineEnd(text, safeEnd);
    }

    private static String createCapabilitiesText(LanguageCommentStyle language)
    {
        if (language.supportsLineComment() && language.supportsBlockComment()) {
            return "Podporované styly: řádkový i blokový komentář";
        }
        if (language.supportsLineComment()) {
            return "Podporované styly: pouze řádkový komentář";
        }
        return "Podporované styly: pouze blokový komentář";
    }

    private static void setPreviewSource(JTextArea area, String text)
    {
        area.setText(text);
        area.setCaretPosition(0);
    }

    @SuppressWarnings("unchecked")
    private static <T> T selected(JComboBox<T> combo)
    {
        return (T) combo.getSelectedItem();
    }
}
