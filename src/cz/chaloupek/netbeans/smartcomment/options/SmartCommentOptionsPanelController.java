package cz.chaloupek.netbeans.smartcomment.options;

import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

@OptionsPanelController.SubRegistration(
        location = "Advanced",
        displayName = "#OptionsCategory_Name_SmartCommenting",
        keywords = "#OptionsCategory_Keywords_SmartCommenting",
        keywordsCategory = "Advanced/SmartCommenting"
)
@Messages({
    "OptionsCategory_Name_SmartCommenting=Smart Commenting",
    "OptionsCategory_Keywords_SmartCommenting=comment,komentář,editor,indent,odsazení"
})
public final class SmartCommentOptionsPanelController extends OptionsPanelController
{
    private SmartCommentOptionsPanel panel;

    @Override
    public void update()
    {
        getPanel().load();
        getPanel().setChanged(false);
    }

    @Override
    public void applyChanges()
    {
        getPanel().store();
        getPanel().setChanged(false);
    }

    @Override
    public void cancel()
    {
        getPanel().load();
        getPanel().setChanged(false);
    }

    @Override
    public boolean isValid()
    {
        return getPanel().isConfigurationValid();
    }

    @Override
    public boolean isChanged()
    {
        return getPanel().isChanged();
    }

    @Override
    public HelpCtx getHelpCtx()
    {
        return null;
    }

    @Override
    public JComponent getComponent(Lookup masterLookup)
    {
        return getPanel();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        getPanel().addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        getPanel().removePropertyChangeListener(listener);
    }

    private SmartCommentOptionsPanel getPanel()
    {
        if (panel == null) panel = new SmartCommentOptionsPanel(this);
        return panel;
    }

    void changed()
    {
        getPanel().setChanged(true);
    }
}
