package delta.games.lotro.gui.items.essences;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.labels.MultilineLabel;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.BasicCharacterAttributes;
import delta.games.lotro.gui.items.ItemUiTools;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for the UI items of a single essence.
 * @author DAM
 */
public class SimpleSingleEssenceEditionController
{
  private static final String ITEM_WITH_NO_ICON="/resources/gui/equipment/itemNoIcon.png";

  // Data
  private BasicCharacterAttributes _attrs;
  private Item _essence;
  private int _linesCount;
  // Controllers
  private WindowController _parent;
  // UI
  private JButton _essenceIconButton;
  private MultilineLabel _essenceName;
  private JButton _deleteButton;
  // Listeners
  private EssenceUpdatedListener _listener;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param linesCount Number of lines to display the essence name.
   * @param attrs Attributes of toon to use.
   */
  public SimpleSingleEssenceEditionController(WindowController parent, int linesCount, BasicCharacterAttributes attrs)
  {
    _essence=null;
    _linesCount=linesCount;
    _attrs=attrs;
    _parent=parent;
    // Button
    _essenceIconButton=GuiFactory.buildButton("");
    _essenceIconButton.setOpaque(false);
    _essenceIconButton.setBorderPainted(false);
    _essenceIconButton.setMargin(new Insets(0,0,0,0));
    ActionListener listener=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        handleButtonClick((JButton)e.getSource());
      }
    };
    _essenceIconButton.addActionListener(listener);
    // Label
    _essenceName=new MultilineLabel();
    setEssence(null);
    // Delete button
    ImageIcon icon=IconsManager.getIcon("/resources/gui/icons/cross.png");
    _deleteButton=GuiFactory.buildButton("");
    _deleteButton.setIcon(icon);
    _deleteButton.setMargin(new Insets(0,0,0,0));
    _deleteButton.setContentAreaFilled(false);
    _deleteButton.setBorderPainted(false);
    _deleteButton.addActionListener(listener);
  }

  /**
   * Set the listener for this controller.
   * @param listener A listener.
   */
  public void setListener(EssenceUpdatedListener listener)
  {
    _listener=listener;
  }

  private void handleButtonClick(JButton button)
  {
    if (button==_essenceIconButton)
    {
      Item essence=EssenceChoice.chooseEssence(_parent,_attrs);
      if (essence!=null)
      {
        setEssence(essence);
        if (_listener!=null)
        {
          _listener.essenceUpdated(this);
        }
      }
    }
    else if (button==_deleteButton)
    {
      setEssence(null);
      if (_listener!=null)
      {
        _listener.essenceUpdated(this);
      }
    }
  }

  /**
   * Get the managed essence.
   * @return the managed essence.
   */
  public Item getEssence()
  {
    return _essence;
  }

  /**
   * Set current essence.
   * @param essence Essence to set.
   */
  public void setEssence(Item essence)
  {
    // Store essence
    _essence=essence;
    // Set essence icon
    Icon icon=null;
    if (essence!=null)
    {
      icon=ItemUiTools.buildItemIcon(essence);
    }
    else
    {
      icon=IconsManager.getIcon(ITEM_WITH_NO_ICON);
    }
    _essenceIconButton.setIcon(icon);
    // Text
    String text="";
    if (essence!=null)
    {
      text=essence.getName();
    }
    _essenceName.setText(text,_linesCount);
  }

  /**
   * Get the managed essence button.
   * @return the managed essence button.
   */
  public JButton getEssenceButton()
  {
    return _essenceIconButton;
  }

  /**
   * Get the label for the essence.
   * @return a label.
   */
  public JPanel getEssenceNameLabel()
  {
    return _essenceName;
  }

  /**
   * Get the delete button associated with this essence.
   * @return a button.
   */
  public JButton getDeleteButton()
  {
    return _deleteButton;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _essence=null;
    // Controllers
    _parent=null;
    // UI
    _essenceIconButton=null;
    _essenceName=null;
    _deleteButton=null;
    // Listeners
    _listener=null;
  }
}
