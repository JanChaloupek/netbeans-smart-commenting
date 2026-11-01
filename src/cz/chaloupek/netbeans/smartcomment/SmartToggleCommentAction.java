package cz.chaloupek.netbeans.smartcomment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorRegistry;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Akce přepínající komentář aktuálního řádku nebo označeného bloku.
 *
 * Akce je dostupná v nabídce Edit a prostřednictvím výchozí klávesové
 * zkratky Ctrl+Alt+C na Windows a Linuxu. Uživatel může přiřazení změnit
 * v nastavení klávesových zkratek NetBeans.
 */
@ActionID(
        category = "Edit",
        id = "cz.chaloupek.netbeans.smartcomment.SmartToggleCommentAction"
)
@ActionRegistration(
        displayName = "#CTL_SmartToggleCommentAction"
)
@ActionReferences({
    @ActionReference(
            path = "Menu/Edit",
            position = 100
    ),
    @ActionReference(
            path = "Shortcuts",
            name = "DA-C"
    )
})
@Messages(
        "CTL_SmartToggleCommentAction=Smart Toggle Comment"
)
public final class SmartToggleCommentAction implements ActionListener
{
    /**
     * Spustí přepnutí komentáře v naposledy aktivním editoru.
     *
     * Pokud žádný editor není aktivní, akce neprovede žádnou změnu.
     *
     * @param event událost vyvolávající akci
     */
    @Override
    public void actionPerformed(ActionEvent event)
    {
        JTextComponent editor = EditorRegistry.lastFocusedComponent();

        if (editor != null) {
            SmartCommentProcessor.toggleSelection(editor);
        }
    }
}
