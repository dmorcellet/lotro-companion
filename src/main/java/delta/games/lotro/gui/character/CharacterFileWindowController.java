package delta.games.lotro.gui.character;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterInfosManager;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.events.CharacterEventsManager;
import delta.games.lotro.gui.log.CharacterLogWindowController;
import delta.games.lotro.gui.stats.crafting.CraftingWindowController;
import delta.games.lotro.gui.stats.reputation.CharacterReputationWindowController;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.toolbar.ToolbarController;
import delta.games.lotro.gui.utils.toolbar.ToolbarIconItem;
import delta.games.lotro.gui.utils.toolbar.ToolbarModel;
import delta.games.lotro.utils.gui.DefaultWindowController;
import delta.games.lotro.utils.gui.WindowController;
import delta.games.lotro.utils.gui.WindowsManager;
import delta.games.lotro.utils.gui.tables.GenericTableController;

/**
 * Controller for a "character" window.
 * @author DAM
 */
public class CharacterFileWindowController extends DefaultWindowController implements ActionListener
{
  private static final String NEW_TOON_DATA_ID="newToonData";
  private static final String LOG_COMMAND="log";
  private static final String REPUTATION_COMMAND="reputation";
  private static final String CRAFTING_COMMAND="crafting";

  private CharacterSummaryPanelController _summaryController;
  private CharacterDataTableController _toonsTable;
  private ToolbarController _toolbar;
  private CharacterFile _toon;
  private WindowsManager _windowsManager;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public CharacterFileWindowController(CharacterFile toon)
  {
    _toon=toon;
    _windowsManager=new WindowsManager();
    CharacterSummary summary=_toon.getSummary();
    _summaryController=new CharacterSummaryPanelController(summary);
  }

  /**
   * Get the window identifier for a given toon.
   * @param serverName Server name.
   * @param toonName Toon name.
   * @return A window identifier.
   */
  public static String getIdentifier(String serverName, String toonName)
  {
    String id="FILE#"+serverName+"#"+toonName;
    id=id.toUpperCase();
    return id;
  }

  @Override
  protected JComponent buildContents()
  {
    // Summary panel
    JPanel summaryPanel=_summaryController.getPanel();

    // Whole panel
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // - summary
    GridBagConstraints c=new GridBagConstraints(0,0,2,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(3,5,3,5),0,0);
    panel.add(summaryPanel,c);
    // Character data table
    JPanel tablePanel=buildTablePanel();
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    c=new GridBagConstraints(0,2,1,2,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    JPanel commandsPanel=buildCommandsPanel();
    panel.add(commandsPanel,c);

    // TODO crafting anvils?
    return panel;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServerName();
    String title="Character: "+name+" @ "+serverName;
    frame.setTitle(title);
    frame.pack();
    frame.setResizable(true);
    return frame;
  }

  @Override
  public String getWindowIdentifier()
  {
    String serverName=_toon.getServerName();
    String toonName=_toon.getName();
    String id=getIdentifier(serverName,toonName);
    return id;
  }

  private JPanel buildCommandsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    // Log
    JButton logButton=buildCommandButton("Log",LOG_COMMAND);
    panel.add(logButton,c);
    c.gridx++;
    // Reputation
    JButton reputationButton=buildCommandButton("Reputation",REPUTATION_COMMAND);
    panel.add(reputationButton,c);
    c.gridx++;
    // Crafting
    JButton craftingButton=buildCommandButton("Crafting",CRAFTING_COMMAND);
    panel.add(craftingButton,c);
    return panel;
  }

  private JButton buildCommandButton(String label, String command)
  {
    JButton b=GuiFactory.buildButton(label);
    b.setActionCommand(command);
    b.addActionListener(this);
    return b;
  }

  /**
   * Handle button actions.
   * @param e Source event.
   */
  public void actionPerformed(ActionEvent e)
  {
    String command=e.getActionCommand();
    if (LOG_COMMAND.equals(command))
    {
      // Show log 
      String serverName=_toon.getServerName();
      String toonName=_toon.getName();
      String id=CharacterLogWindowController.getIdentifier(serverName,toonName);
      WindowController controller=_windowsManager.getWindow(id);
      if (controller==null)
      {
        controller=new CharacterLogWindowController(_toon);
        _windowsManager.registerWindow(controller);
        controller.getWindow().setLocationRelativeTo(getFrame());
      }
      controller.bringToFront();
    }
    else if (REPUTATION_COMMAND.equals(command))
    {
      // Reputation
      String serverName=_toon.getServerName();
      String toonName=_toon.getName();
      String id=CharacterReputationWindowController.getIdentifier(serverName,toonName);
      WindowController controller=_windowsManager.getWindow(id);
      if (controller==null)
      {
        controller=new CharacterReputationWindowController(_toon);
        _windowsManager.registerWindow(controller);
        controller.getWindow().setLocationRelativeTo(getFrame());
      }
      controller.bringToFront();
    }
    else if (CRAFTING_COMMAND.equals(command))
    {
      // Crafting
      String serverName=_toon.getServerName();
      String toonName=_toon.getName();
      String id=CraftingWindowController.getIdentifier(serverName,toonName);
      WindowController controller=_windowsManager.getWindow(id);
      if (controller==null)
      {
        controller=new CraftingWindowController(_toon);
        _windowsManager.registerWindow(controller);
        controller.getWindow().setLocationRelativeTo(getFrame());
      }
      controller.bringToFront();
    }
    else if (NEW_TOON_DATA_ID.equals(command))
    {
      startNewCharacterData();
    }
    else if (GenericTableController.DOUBLE_CLICK.equals(command))
    {
      CharacterData data=(CharacterData)e.getSource();
      showCharacterData(data);
    }
  }

  private JPanel buildTablePanel()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new BorderLayout());
    _toolbar=buildToolBar();
    JToolBar toolbar=_toolbar.getToolBar();
    ret.add(toolbar,BorderLayout.NORTH);
    _toonsTable=buildToonsTable();
    JTable table=_toonsTable.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    ret.add(scroll,BorderLayout.CENTER);
    return ret;
  }

  private ToolbarController buildToolBar()
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // New icon
    String newIconPath=getToolbarIconPath("new");
    ToolbarIconItem newIconItem=new ToolbarIconItem(NEW_TOON_DATA_ID,newIconPath,NEW_TOON_DATA_ID,"Create a new character configuration...","New");
    model.addToolbarIconItem(newIconItem);
    controller.addActionListener(this);
    return controller;
  }

  private String getToolbarIconPath(String iconName)
  {
    String imgLocation="/resources/gui/icons/"+iconName+"-icon.png";
    return imgLocation;
  }

  private CharacterDataTableController buildToonsTable()
  {
    CharacterDataTableController tableController=new CharacterDataTableController(_toon);
    tableController.addActionListener(this);
    return tableController;
  }

  private void startNewCharacterData()
  {
    CharacterInfosManager infos=_toon.getInfosManager();
    CharacterData lastInfos=infos.getLastCharacterDescription();
    CharacterData newInfos=new CharacterData();
    CharacterSummary newSummary=new CharacterSummary(lastInfos.getSummary());
    newInfos.setSummary(newSummary);
    newInfos.setDate(Long.valueOf(System.currentTimeMillis()));
    boolean ok=_toon.getInfosManager().writeNewInfo(newInfos);
    if (ok)
    {
      CharacterEvent event=new CharacterEvent(_toon,newInfos);
      CharacterEventsManager.invokeEvent(CharacterEventType.CHARACTER_DATA_ADDED,event);
      showCharacterData(newInfos);
    }
  }

  private void showCharacterData(CharacterData data)
  {
    String serverName=data.getServer();
    String toonName=data.getName();
    String id=CharacterDataWindowController.getIdentifier(serverName,toonName);
    WindowController controller=_windowsManager.getWindow(id);
    if (controller==null)
    {
      controller=new CharacterDataWindowController(data);
      _windowsManager.registerWindow(controller);
      Window thisWindow=SwingUtilities.getWindowAncestor(_toonsTable.getTable());
      controller.getWindow().setLocationRelativeTo(thisWindow);
    }
    controller.bringToFront();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    if (_windowsManager!=null)
    {
      _windowsManager.disposeAll();
      _windowsManager=null;
    }
    super.dispose();
    if (_summaryController!=null)
    {
      _summaryController.dispose();
      _summaryController=null;
    }
    if (_toonsTable!=null)
    {
      _toonsTable.dispose();
      _toonsTable=null;
    }
    if (_toolbar==null)
    {
      _toolbar.dispose();
      _toolbar=null;
    }
    _toon=null;
  }
}