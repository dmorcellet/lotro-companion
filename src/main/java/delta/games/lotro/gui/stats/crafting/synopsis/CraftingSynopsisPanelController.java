package delta.games.lotro.gui.stats.crafting.synopsis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventListener;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.events.CharacterEventsManager;
import delta.games.lotro.crafting.ProfessionFilter;
import delta.games.lotro.gui.character.chooser.CharactersChooserController;

/**
 * Controller for a crafting synopsis panel.
 * @author DAM
 */
public class CraftingSynopsisPanelController implements CharacterEventListener
{
  // Controllers
  private CraftingSynopsisWindowController _parent;
  private CraftingSynopsisFilterController _filterController;
  private CraftingSynopsisTableController _tableController;
  // Data
  private ProfessionFilter _filter;
  // GUI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parentController Parent controller.
   */
  public CraftingSynopsisPanelController(CraftingSynopsisWindowController parentController)
  {
    _parent=parentController;
    _filter=new ProfessionFilter();
    _filterController=new CraftingSynopsisFilterController(_filter,this);
    _tableController=new CraftingSynopsisTableController(_filter);
    CharacterEventsManager.addListener(this);
  }

  /**
   * Get the table controller.
   * @return the table controller.
   */
  public CraftingSynopsisTableController getTableController()
  {
    return _tableController;
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=buildPanel();
    }
    return _panel;
  }

  private void doChooseToons()
  {
    CharactersManager manager=CharactersManager.getInstance();
    List<CharacterFile> toons=manager.getAllToons();
    List<CharacterFile> selectedToons=_tableController.getToons();
    List<CharacterFile> newSelectedToons=CharactersChooserController.selectToons(_parent,toons,selectedToons);
    if (newSelectedToons!=null)
    {
      _tableController.setToons(newSelectedToons);
    }
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    JPanel statsPanels=buildStatsPanel();
    panel.add(statsPanels,BorderLayout.CENTER);
    JPanel commandsPanel=buildTopPanel();
    panel.add(commandsPanel,BorderLayout.NORTH);
    return panel;
  }

  private JPanel buildTopPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);

    // Filter
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter");
    filterPanel.setBorder(filterBorder);
    panel.add(filterPanel,c);

    // Choose toons button
    JButton chooser=GuiFactory.buildButton("Choose characters...");
    ActionListener al=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        doChooseToons();
      }
    };
    chooser.addActionListener(al);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(chooser,c);
    return panel;
  }

  private JPanel buildStatsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    TitledBorder border=GuiFactory.buildTitledBorder("Crafting synopsis");
    panel.setBorder(border);

    // Table
    JTable table=_tableController.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    scroll.setPreferredSize(new Dimension(850,400));
    panel.add(scroll,BorderLayout.CENTER);
    return panel;
  }

  /**
   * Update filter.
   */
  public void updateFilter()
  {
    _tableController.updateFilter();
  }

  /**
   * Handle character events.
   * @param type Event type.
   * @param event Source event.
   */
  public void eventOccurred(CharacterEventType type, CharacterEvent event)
  {
    if (type==CharacterEventType.CHARACTER_CRAFTING_UPDATED)
    {
      CharacterFile toon=event.getToonFile();
      List<CharacterFile> currentToons=_tableController.getToons();
      if (currentToons.contains(toon))
      {
        _tableController.updateToon(toon);
      }
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    CharacterEventsManager.removeListener(this);
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    _parent=null;
    // Data
    _filter=null;
  }
}
