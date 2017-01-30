package delta.games.lotro.gui.character.buffs;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.events.CharacterEventsManager;
import delta.games.lotro.character.stats.buffs.Buff;
import delta.games.lotro.character.stats.buffs.BuffInstance;
import delta.games.lotro.character.stats.buffs.BuffRegistry;
import delta.games.lotro.character.stats.buffs.BuffsManager;
import delta.games.lotro.gui.utils.GuiFactory;

/**
 * Controller for a panel to edit buffs.
 * @author DAM
 */
public class BuffEditionPanelController implements ActionListener
{
  private static final String REMOVE_COMMAND="remove";

  // Data
  private CharacterData _toon;
  // UI
  private List<BuffIconController> _buffControllers;
  private JPanel _panel;
  private JPanel _iconsPanel;
  private JPopupMenu _contextMenu;

  /**
   * Constructor.
   * @param character Targeted character.
   */
  public BuffEditionPanelController(CharacterData character)
  {
    _toon=character;
    _buffControllers=new ArrayList<BuffIconController>();
    build();
    updateIconsPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private void build()
  {
    _panel=GuiFactory.buildPanel(new FlowLayout());
    _iconsPanel=GuiFactory.buildBackgroundPanel(new FlowLayout(FlowLayout.LEFT));
    buildBuffControllers(_panel);
    _panel.add(_iconsPanel);
    JButton button=GuiFactory.buildButton("Add...");
    _panel.add(button);
    ActionListener al=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        add();
      }
    };
    button.addActionListener(al);
    _contextMenu=buildContextualMenu();
  }

  private void updateIconsPanel()
  {
    _iconsPanel.removeAll();
    for(BuffIconController controller : _buffControllers)
    {
      JLabel label=controller.getLabel();
      _iconsPanel.add(label);
    }
    _panel.revalidate();
    _panel.repaint();
  }

  private void buildBuffControllers(JPanel panel)
  {
    BuffsManager buffs=_toon.getBuffs();
    int nbBuffs=buffs.getBuffsCount();
    for(int i=0;i<nbBuffs;i++)
    {
      BuffInstance buff=buffs.getBuffAt(i);
      BuffIconController controller=buildBuffController(buff);
      _buffControllers.add(controller);
    }
  }

  private BuffIconController buildBuffController(BuffInstance buff)
  {
    Font font=_iconsPanel.getFont();
    BuffIconController controller=new BuffIconController(buff,font);
    JLabel label=controller.getLabel();
    MouseListener popupListener=buildRightClickListener();
    label.addMouseListener(popupListener);
    MouseListener listener=buildLeftClickListener();
    label.addMouseListener(listener);
    return controller;
  }

  private JPopupMenu buildContextualMenu()
  {
    JPopupMenu popup=new JPopupMenu();
    JMenuItem remove=new JMenuItem("Remove");
    remove.setActionCommand(REMOVE_COMMAND);
    remove.addActionListener(this);
    popup.add(remove);
    return popup;
  }

  private MouseListener buildRightClickListener()
  {
    class PopClickListener extends MouseAdapter
    {
      public void mousePressed(MouseEvent e)
      {
        if (e.isPopupTrigger()) doPop(e);
      }

      public void mouseReleased(MouseEvent e)
      {
        if (e.isPopupTrigger()) doPop(e);
      }

      private void doPop(MouseEvent e)
      {
        _contextMenu.show(e.getComponent(),e.getX(),e.getY());
      }
    }
    return new PopClickListener();
  }

  private MouseListener buildLeftClickListener()
  {
    class LeftClickListener extends MouseAdapter
    {
      public void mouseReleased(MouseEvent e)
      {
        if (e.getButton()==MouseEvent.BUTTON1)
        {
          updateTier(e);
        }
      }
    }
    return new LeftClickListener();
  }

  public void actionPerformed(ActionEvent e)
  {
    String cmd=e.getActionCommand();
    if (REMOVE_COMMAND.equals(cmd))
    {
      // From contextual menu
      Component invoker=_contextMenu.getInvoker();
      int index=getIndex(invoker);
      if (index!=-1)
      {
        remove(index);
      }
    }
  }

  private void updateTier(MouseEvent e)
  {
    // Straight click
    Object invoker=e.getSource();
    int index=getIndex(invoker);
    if (index!=-1)
    {
      // Update tier
      updateTier(index);
    }
  }

  private int getIndex(Object invoker)
  {
    int index=0;
    for(BuffIconController controller : _buffControllers)
    {
      JLabel label=controller.getLabel();
      if (label==invoker)
      {
        return index;
      }
      index++;
    }
    return -1;
  }

  private void add()
  {
    BuffsManager buffs=_toon.getBuffs();
    List<Buff> possibleBuffs=BuffRegistry.getInstance().buildBuffSelection(_toon,buffs);
    Buff buff=BuffChoiceWindowController.selectBuff(null,possibleBuffs,null);
    if (buff!=null)
    {
      BuffInstance buffInstance=buff.buildInstance();
      buffs.addBuff(buffInstance);
      BuffIconController controller=buildBuffController(buffInstance);
      _buffControllers.add(controller);
      // Update UI
      updateIconsPanel();
      // Broadcast toon update event...
      CharacterEvent event=new CharacterEvent(null,_toon);
      CharacterEventsManager.invokeEvent(CharacterEventType.CHARACTER_DATA_UPDATED,event);
    }
  }

  private void updateTier(int index)
  {
    BuffsManager buffs=_toon.getBuffs();
    BuffInstance buff=buffs.getBuffAt(index);
    List<Integer> tiers=buff.getBuff().getImpl().getTiers();
    if ((tiers!=null) && (tiers.size()>0))
    {
      Integer currentTier=buff.getTier();
      int currentTierIndex=tiers.indexOf(currentTier);
      if (currentTierIndex!=-1)
      {
        currentTierIndex++;
        if (currentTierIndex==tiers.size())
        {
          currentTierIndex=0;
        }
        buff.setTier(tiers.get(currentTierIndex));
        _buffControllers.get(index).update();
        // Broadcast toon update event...
        CharacterEvent event=new CharacterEvent(null,_toon);
        CharacterEventsManager.invokeEvent(CharacterEventType.CHARACTER_DATA_UPDATED,event);
      }
    }
  }

  private void remove(int index)
  {
    BuffsManager buffs=_toon.getBuffs();
    buffs.removeBuffAt(index);
    _buffControllers.remove(index);
    // Update UI
    updateIconsPanel();
    // Broadcast toon update event...
    CharacterEvent event=new CharacterEvent(null,_toon);
    CharacterEventsManager.invokeEvent(CharacterEventType.CHARACTER_DATA_UPDATED,event);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_iconsPanel!=null)
    {
      _iconsPanel.removeAll();
      _iconsPanel=null;
    }
    _buffControllers.clear();
    _toon=null;
  }
}