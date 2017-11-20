package delta.games.lotro.gui.stats.crafting;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.crafting.CraftingStatus;
import delta.games.lotro.character.crafting.GuildStatus;
import delta.games.lotro.character.crafting.ProfessionStatus;
import delta.games.lotro.crafting.Profession;
import delta.games.lotro.crafting.Vocation;
import delta.games.lotro.gui.stats.reputation.form.FactionStatusPanelController;

/**
 * Controller for the vocation edition panel. This panel contains:
 * <ul>
 * <li>3 profession panels,
 * <li>an optional guild panel.
 * </ul>
 * @author DAM
 */
public class VocationEditionPanelController
{
  // Controllers
  private HashMap<Profession,ProfessionStatusPanelController> _panels;
  private FactionStatusPanelController _guildStatus;
  // UI
  private JPanel _vocationPanel;
  private JTabbedPane _tabbedPane;
  // Data
  private CraftingStatus _status;

  /**
   * Constructor.
   * @param status Crafting status to edit.
   */
  public VocationEditionPanelController(CraftingStatus status)
  {
    _panels=new HashMap<Profession,ProfessionStatusPanelController>();
    _status=status;
    _vocationPanel=GuiFactory.buildPanel(new BorderLayout());
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _vocationPanel;
  }

  /**
   * Update UI display from underlying data.
   */
  public void updateUiFromData()
  {
    updateProfessionsUi();
    updateGuildUi();

    // Select first tab, if any
    if (_tabbedPane!=null)
    {
      _tabbedPane.setSelectedIndex(0);
    }
  }

  /**
   * Update the UI for professions.
   */
  private void updateProfessionsUi()
  {
    Vocation vocation=_status.getVocation();
    _vocationPanel.removeAll();
    JComponent centerComponent=null;
    List<Profession> professions=(vocation!=null)?vocation.getProfessions():null;
    if ((professions!=null) && (professions.size()>0))
    {
      _tabbedPane=GuiFactory.buildTabbedPane();
      // Professions
      for(Profession profession : professions)
      {
        ProfessionStatus stats=_status.getProfessionStatus(profession,true);
        ProfessionStatusPanelController craftingPanelController=_panels.get(profession);
        if (craftingPanelController==null)
        {
          craftingPanelController=new ProfessionStatusPanelController(stats);
          _panels.put(profession,craftingPanelController);
        }
        JPanel craftingPanel=craftingPanelController.getPanel();
        _tabbedPane.add(profession.getLabel(),craftingPanel);
      }
      // Clean other professions
      for(Profession profession : Profession.getAll())
      {
        if (!professions.contains(profession))
        {
          _panels.remove(profession);
        }
      }
      centerComponent=_tabbedPane;
    }
    else
    {
      JLabel centerLabel=new JLabel("No vocation!");
      centerComponent=centerLabel;
      _tabbedPane=null;
    }
    _vocationPanel.add(centerComponent,BorderLayout.CENTER);
    _vocationPanel.revalidate();
    _vocationPanel.repaint();
  }

  /**
   * Update the UI for guild edition.
   */
  public void updateGuildUi()
  {
    // Cleanup
    if ((_guildStatus!=null) && (_tabbedPane!=null))
    {
      JPanel guildPanel=_guildStatus.getPanel();
      _tabbedPane.remove(guildPanel);
    }
    GuildStatus guildStatus=_status.getGuildStatus();
    Profession guild=guildStatus.getProfession();
    // Add tab if needed
    if (guild!=null)
    {
      _guildStatus=new FactionStatusPanelController(guildStatus.getFactionStatus());
      JPanel guildPanel=_guildStatus.getPanel();
      _tabbedPane.add("Guild",guildPanel);
      _tabbedPane.setSelectedComponent(guildPanel);
    }
  }

  /**
   * Update data from UI contents.
   */
  public void updateDataFromUi()
  {
    for(ProfessionStatusPanelController controller: _panels.values())
    {
      controller.updateDataFromUi();
    }
    if (_guildStatus!=null)
    {
      _guildStatus.updateDataFromUi();
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panels!=null)
    {
      for(ProfessionStatusPanelController controller : _panels.values())
      {
        controller.dispose();
      }
      _panels.clear();
      _panels=null;
    }
    if (_guildStatus!=null)
    {
      _guildStatus.dispose();
      _guildStatus=null;
    }
    if (_vocationPanel!=null)
    {
      _vocationPanel.removeAll();
      _vocationPanel=null;
    }
    _tabbedPane=null;
    _status=null;
  }
}