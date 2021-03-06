package delta.games.lotro.gui.quests.filter;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.common.LockType;
import delta.games.lotro.common.Repeatability;
import delta.games.lotro.common.Size;
import delta.games.lotro.common.rewards.RewardsExplorer;
import delta.games.lotro.gui.common.requirements.RequirementsFilterController;
import delta.games.lotro.gui.common.rewards.filter.RewardsFilterController;
import delta.games.lotro.gui.items.FilterUpdateListener;
import delta.games.lotro.gui.quests.QuestsUiUtils;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestDescription.FACTION;
import delta.games.lotro.lore.quests.QuestsManager;
import delta.games.lotro.lore.quests.filter.AutoBestowedQuestFilter;
import delta.games.lotro.lore.quests.filter.InstancedQuestFilter;
import delta.games.lotro.lore.quests.filter.LockTypeFilter;
import delta.games.lotro.lore.quests.filter.QuestArcFilter;
import delta.games.lotro.lore.quests.filter.QuestCategoryFilter;
import delta.games.lotro.lore.quests.filter.QuestFactionFilter;
import delta.games.lotro.lore.quests.filter.QuestFilter;
import delta.games.lotro.lore.quests.filter.QuestNameFilter;
import delta.games.lotro.lore.quests.filter.QuestSizeFilter;
import delta.games.lotro.lore.quests.filter.RepeatabilityFilter;
import delta.games.lotro.lore.quests.filter.SessionPlayQuestFilter;
import delta.games.lotro.lore.quests.filter.ShareableQuestFilter;

/**
 * Controller for a quest filter edition panel.
 * @author DAM
 */
public class QuestFilterController implements ActionListener
{
  // Data
  private QuestFilter _filter;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // -- Quest attributes UI --
  private JTextField _contains;
  private ComboBoxController<String> _category;
  private ComboBoxController<String> _questArc;
  private ComboBoxController<Boolean> _instanced;
  private ComboBoxController<Boolean> _shareable;
  private ComboBoxController<Boolean> _sessionPlay;
  private ComboBoxController<Boolean> _autoBestowed;
  private ComboBoxController<Repeatability> _repeatability;
  private ComboBoxController<LockType> _lockType;
  private ComboBoxController<Size> _size;
  private ComboBoxController<FACTION> _faction;
  // -- Requirements UI --
  private RequirementsFilterController _requirements;
  // -- Rewards UI --
  private RewardsFilterController _rewards;
  // Controllers
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   * @param useRequirements Use requirements or not.
   */
  public QuestFilterController(QuestFilter filter, FilterUpdateListener filterUpdateListener, boolean useRequirements)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
    if (useRequirements)
    {
      _requirements=new RequirementsFilterController(filter.getRequirementsFilter(),filterUpdateListener);
    }
    RewardsExplorer explorer=QuestsManager.getInstance().buildRewardsExplorer();
    _rewards=new RewardsFilterController(filter.getRewardsFilter(),filterUpdateListener,explorer,true);
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<QuestDescription> getFilter()
  {
    return _filter;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
      setFilter();
      filterUpdated();
    }
    return _panel;
  }

  /**
   * Invoked when the managed filter has been updated.
   */
  private void filterUpdated()
  {
    _filterUpdateListener.filterUpdated();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    if (source==_reset)
    {
      _category.selectItem(null);
      _questArc.selectItem(null);
      _size.selectItem(null);
      _faction.selectItem(null);
      _instanced.selectItem(null);
      _shareable.selectItem(null);
      _sessionPlay.selectItem(null);
      _autoBestowed.selectItem(null);
      _repeatability.selectItem(null);
      _lockType.selectItem(null);
      if (_requirements!=null)
      {
        _requirements.reset();
      }
      _rewards.reset();
      _contains.setText("");
    }
  }

  private void setFilter()
  {
    // Name
    QuestNameFilter nameFilter=_filter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Category
    QuestCategoryFilter categoryFilter=_filter.getCategoryFilter();
    String category=categoryFilter.getQuestCategory();
    _category.selectItem(category);
    // Quest arc
    QuestArcFilter questArcFilter=_filter.getQuestArcFilter();
    String questArc=questArcFilter.getQuestArc();
    _questArc.selectItem(questArc);
    // Size
    QuestSizeFilter sizeFilter=_filter.getQuestSizeFilter();
    Size size=sizeFilter.getQuestSize();
    _size.selectItem(size);
    // Faction
    QuestFactionFilter factionFilter=_filter.getQuestFactionFilter();
    FACTION faction=factionFilter.getFaction();
    _faction.selectItem(faction);
    // Instanced
    InstancedQuestFilter instancedFilter=_filter.getInstancedQuestFilter();
    Boolean instancedFlag=instancedFilter.getIsInstancedFlag();
    _instanced.selectItem(instancedFlag);
    // Shareable
    ShareableQuestFilter shareableFilter=_filter.getShareableQuestFilter();
    Boolean shareableFlag=shareableFilter.getIsShareableFlag();
    _shareable.selectItem(shareableFlag);
    // Session-play
    SessionPlayQuestFilter sessionPlayFilter=_filter.getSessionPlayQuestFilter();
    Boolean sessionPlayFlag=sessionPlayFilter.getIsSessionPlayFlag();
    _sessionPlay.selectItem(sessionPlayFlag);
    // Auto-bestowed
    AutoBestowedQuestFilter autoBestowedFilter=_filter.getAutoBestowedQuestFilter();
    Boolean autoBestowedFlag=autoBestowedFilter.getIsAutoBestowedFlag();
    _autoBestowed.selectItem(autoBestowedFlag);
    // Repeatability
    RepeatabilityFilter repeatabilityFilter=_filter.getRepeatabilityFilter();
    Repeatability repeatability=repeatabilityFilter.getRepeatability();
    _repeatability.selectItem(repeatability);
    // Lock type
    LockTypeFilter lockTypeFilter=_filter.getLockTypeFilter();
    LockType lockType=lockTypeFilter.getLockType();
    _lockType.selectItem(lockType);
    // Requirements
    if (_requirements!=null)
    {
      _requirements.setFilter();
    }
    // Rewards
    _rewards.setFilter();
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;

    // Quest attributes
    JPanel questPanel=buildQuestPanel();
    Border questBorder=GuiFactory.buildTitledBorder("Quest");
    questPanel.setBorder(questBorder);
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(questPanel,c);

    // Reset
    _reset=GuiFactory.buildButton("Reset");
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,y,1,1,0.0,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,new Insets(0,5,5,0),0,0);
    panel.add(_reset,c);
    y++;

    // Requirements
    if (_requirements!=null)
    {
      JPanel requirementsPanel=_requirements.getPanel();
      Border requirementsBorder=GuiFactory.buildTitledBorder("Requirements");
      requirementsPanel.setBorder(requirementsBorder);
      c=new GridBagConstraints(0,y,2,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(requirementsPanel,c);
      y++;
    }

    // Rewards
    JPanel rewardsPanel=_rewards.getPanel();
    Border rewardsBorder=GuiFactory.buildTitledBorder("Rewards");
    rewardsPanel.setBorder(rewardsBorder);
    c=new GridBagConstraints(0,y,2,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(rewardsPanel,c);
    y++;

    return panel;
  }

  private JPanel buildQuestPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,0,0));
    // Label filter
    {
      JPanel containsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      containsPanel.add(GuiFactory.buildLabel("Name filter:"));
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(20);
      containsPanel.add(_contains);
      TextListener listener=new TextListener()
      {
        @Override
        public void textChanged(String newText)
        {
          if (newText.length()==0) newText=null;
          QuestNameFilter nameFilter=_filter.getNameFilter();
          nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
      line1Panel.add(containsPanel);
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line1Panel,c);
    y++;

    JPanel line2Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Category
    {
      JLabel label=GuiFactory.buildLabel("Category:");
      line2Panel.add(label);
      _category=QuestsUiUtils.buildCategoryCombo();
      ItemSelectionListener<String> categoryListener=new ItemSelectionListener<String>()
      {
        @Override
        public void itemSelected(String category)
        {
          QuestCategoryFilter categoryFilter=_filter.getCategoryFilter();
          categoryFilter.setQuestCategory(category);
          filterUpdated();
        }
      };
      _category.addListener(categoryListener);
      line2Panel.add(_category.getComboBox());
    }
    // Quest arc
    {
      JLabel label=GuiFactory.buildLabel("Quest arc:");
      line2Panel.add(label);
      _questArc=buildQuestArcCombobox();
      line2Panel.add(_questArc.getComboBox());
    }
    c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line2Panel,c);
    y++;

    // Flags line
    {
      JPanel line=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      // Instanced
      line.add(GuiFactory.buildLabel("Instanced:"));
      _instanced=buildInstancedCombobox();
      line.add(_instanced.getComboBox());
      // Shareable
      line.add(GuiFactory.buildLabel("Shareable:"));
      _shareable=buildShareableCombobox();
      line.add(_shareable.getComboBox());
      // Session-play
      line.add(GuiFactory.buildLabel("Session-play:"));
      _sessionPlay=buildSessionPlayCombobox();
      line.add(_sessionPlay.getComboBox());
      // Auto-bestowed
      line.add(GuiFactory.buildLabel("Auto-bestowed:"));
      _autoBestowed=buildAutoBestowedCombobox();
      line.add(_autoBestowed.getComboBox());

      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,0),0,0);
      panel.add(line,c);
      y++;
    }
    // 4th line
    {
      JPanel line=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      // Repeatability
      line.add(GuiFactory.buildLabel("Repeatability:"));
      _repeatability=buildRepeatabilityCombobox();
      line.add(_repeatability.getComboBox());
      // Lock type
      line.add(GuiFactory.buildLabel("Lock type:"));
      _lockType=buildLockTypeCombobox();
      line.add(_lockType.getComboBox());
      // Size
      line.add(GuiFactory.buildLabel("Size:"));
      _size=buildSizeCombobox();
      line.add(_size.getComboBox());
      // Faction
      line.add(GuiFactory.buildLabel("Faction:"));
      _faction=buildFactionCombobox();
      line.add(_faction.getComboBox());

      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,0),0,0);
      panel.add(line,c);
      y++;
    }
    return panel;
  }

  private ComboBoxController<String> buildQuestArcCombobox()
  {
    ComboBoxController<String> combo=QuestsUiUtils.buildQuestArcCombo();
    ItemSelectionListener<String> questArcListener=new ItemSelectionListener<String>()
    {
      @Override
      public void itemSelected(String questArc)
      {
        QuestArcFilter questArcFilter=_filter.getQuestArcFilter();
        questArcFilter.setQuestArc(questArc);
        filterUpdated();
      }
    };
    combo.addListener(questArcListener);
    return combo;
  }

  private ComboBoxController<Size> buildSizeCombobox()
  {
    ComboBoxController<Size> combo=QuestsUiUtils.buildQuestSizeCombo();
    ItemSelectionListener<Size> questSizeListener=new ItemSelectionListener<Size>()
    {
      @Override
      public void itemSelected(Size size)
      {
        QuestSizeFilter questSizeFilter=_filter.getQuestSizeFilter();
        questSizeFilter.setQuestSize(size);
        filterUpdated();
      }
    };
    combo.addListener(questSizeListener);
    return combo;
  }

  private ComboBoxController<FACTION> buildFactionCombobox()
  {
    ComboBoxController<FACTION> combo=QuestsUiUtils.buildQuestFactionCombo();
    ItemSelectionListener<FACTION> questSizeListener=new ItemSelectionListener<FACTION>()
    {
      @Override
      public void itemSelected(FACTION faction)
      {
        QuestFactionFilter questFactionFilter=_filter.getQuestFactionFilter();
        questFactionFilter.setFaction(faction);
        filterUpdated();
      }
    };
    combo.addListener(questSizeListener);
    return combo;
  }

  private ComboBoxController<Repeatability> buildRepeatabilityCombobox()
  {
    ComboBoxController<Repeatability> combo=QuestsUiUtils.buildRepeatabilityCombo();
    ItemSelectionListener<Repeatability> listener=new ItemSelectionListener<Repeatability>()
    {
      @Override
      public void itemSelected(Repeatability repeatability)
      {
        RepeatabilityFilter repeatabilityFilter=_filter.getRepeatabilityFilter();
        repeatabilityFilter.setRepeatability(repeatability);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<LockType> buildLockTypeCombobox()
  {
    ComboBoxController<LockType> combo=QuestsUiUtils.buildLockTypeCombo();
    ItemSelectionListener<LockType> listener=new ItemSelectionListener<LockType>()
    {
      @Override
      public void itemSelected(LockType lockType)
      {
        LockTypeFilter lockTypeFilter=_filter.getLockTypeFilter();
        lockTypeFilter.setLockType(lockType);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Boolean> buildInstancedCombobox()
  {
    ComboBoxController<Boolean> combo=QuestsUiUtils.build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        InstancedQuestFilter filter=_filter.getInstancedQuestFilter();
        filter.setIsInstancedFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Boolean> buildShareableCombobox()
  {
    ComboBoxController<Boolean> combo=QuestsUiUtils.build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        ShareableQuestFilter filter=_filter.getShareableQuestFilter();
        filter.setIsShareableFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Boolean> buildSessionPlayCombobox()
  {
    ComboBoxController<Boolean> combo=QuestsUiUtils.build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        SessionPlayQuestFilter filter=_filter.getSessionPlayQuestFilter();
        filter.setIsSessionPlayFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Boolean> buildAutoBestowedCombobox()
  {
    ComboBoxController<Boolean> combo=QuestsUiUtils.build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        AutoBestowedQuestFilter filter=_filter.getAutoBestowedQuestFilter();
        filter.setIsAutoBestowedFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _filter=null;
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_textController!=null)
    {
      _textController.dispose();
      _textController=null;
    }
    if (_category!=null)
    {
      _category.dispose();
      _category=null;
    }
    if (_questArc!=null)
    {
      _questArc.dispose();
      _questArc=null;
    }
    if (_size!=null)
    {
      _size.dispose();
      _size=null;
    }
    if (_faction!=null)
    {
      _faction.dispose();
      _faction=null;
    }
    if (_instanced!=null)
    {
      _instanced.dispose();
      _instanced=null;
    }
    if (_shareable!=null)
    {
      _shareable.dispose();
      _shareable=null;
    }
    if (_sessionPlay!=null)
    {
      _sessionPlay.dispose();
      _sessionPlay=null;
    }
    if (_autoBestowed!=null)
    {
      _autoBestowed.dispose();
      _autoBestowed=null;
    }
    if (_repeatability!=null)
    {
      _repeatability.dispose();
      _repeatability=null;
    }
    if (_lockType!=null)
    {
      _lockType.dispose();
      _lockType=null;
    }
    if (_requirements!=null)
    {
      _requirements.dispose();
      _requirements=null;
    }
    if (_rewards!=null)
    {
      _rewards.dispose();
      _rewards=null;
    }
    _contains=null;
    _reset=null;
  }
}
