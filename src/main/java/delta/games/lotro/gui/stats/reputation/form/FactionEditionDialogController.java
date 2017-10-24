package delta.games.lotro.gui.stats.reputation.form;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.reputation.FactionStatus;
import delta.games.lotro.lore.reputation.Faction;

/**
 * Controller for a "faction edition" dialog.
 * @author DAM
 */
public class FactionEditionDialogController extends DefaultFormDialogController<FactionStatus>
{
  // UI
  private FactionHistoryPanelController _edition;
  // Data
  FactionStatus _edited;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param status Data to edit.
   */
  public FactionEditionDialogController(WindowController parentController, FactionStatus status)
  {
    super(parentController,status);
    _edited=new FactionStatus(status);
    _edition=new FactionHistoryPanelController(_edited); 
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    Faction faction=_data.getFaction();
    String name=faction.getName();
    dialog.setTitle("Edit faction history: "+name);
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel dataPanel=buildPanel();
    return dataPanel;
  }

  private JPanel buildPanel()
  {
    return _edition.getPanel();
  }

  @Override
  protected void okImpl()
  {
    _edition.updateDataFromUi();
    _data.set(_edited);
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_edition!=null)
    {
      _edition.dispose();
      _edition=null;
    }
  }
}