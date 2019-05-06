package delta.games.lotro.gui.common.rewards.filter;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.games.lotro.common.VirtueId;
import delta.games.lotro.common.rewards.RewardsExplorer;
import delta.games.lotro.common.rewards.filters.ClassPointRewardFilter;
import delta.games.lotro.common.rewards.filters.DestinyPointsRewardFilter;
import delta.games.lotro.common.rewards.filters.EmoteRewardFilter;
import delta.games.lotro.common.rewards.filters.ItemRewardFilter;
import delta.games.lotro.common.rewards.filters.LotroPointsRewardFilter;
import delta.games.lotro.common.rewards.filters.ReputationRewardFilter;
import delta.games.lotro.common.rewards.filters.TitleRewardFilter;
import delta.games.lotro.common.rewards.filters.TraitRewardFilter;
import delta.games.lotro.common.rewards.filters.VirtueRewardFilter;
import delta.games.lotro.gui.common.rewards.RewardsUiUtils;
import delta.games.lotro.gui.items.FilterUpdateListener;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.games.lotro.lore.reputation.Faction;

/**
 * Controller for a reward filter edition panel.
 * @author DAM
 */
public class RewardsFilterController
{
  // Data
  private RewardsFilter _filter;
  private FilterUpdateListener _filterUpdateListener;
  private RewardsUiUtils _uiUtils;

  // GUI
  private JPanel _panel;

  // Controllers
  private ComboBoxController<Faction> _reputation;
  private ComboBoxController<Boolean> _lotroPoints;
  private ComboBoxController<Boolean> _destinyPoints;
  private ComboBoxController<Boolean> _classPoints;
  private ComboBoxController<String> _trait;
  private ComboBoxController<String> _title;
  private ComboBoxController<VirtueId> _virtue;
  private ComboBoxController<String> _emote;
  private ComboBoxController<Integer> _item;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   * @param explorer Rewards explorer.
   */
  public RewardsFilterController(RewardsFilter filter, FilterUpdateListener filterUpdateListener, RewardsExplorer explorer)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
    _uiUtils=new RewardsUiUtils(explorer);
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=buildRewardsPanel();
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

  /**
   * Reset all gadgets.
   */
  public void reset()
  {
    _reputation.selectItem(null);
    _lotroPoints.selectItem(null);
    _destinyPoints.selectItem(null);
    _classPoints.selectItem(null);
    _trait.selectItem(null);
    _title.selectItem(null);
    _virtue.selectItem(null);
    _emote.selectItem(null);
    _item.selectItem(null);
  }

  /**
   * Apply current filter into the managed gadgets.
   */
  public void setFilter()
  {
    // Reputation
    ReputationRewardFilter factionFilter=_filter.getReputationFilter();
    Faction faction=factionFilter.getFaction();
    _reputation.selectItem(faction);
    // LOTRO points
    LotroPointsRewardFilter lotroPointsFilter=_filter.getLotroPointsFilter();
    Boolean lotroPoints=lotroPointsFilter.getHasLotroPointsFlag();
    _lotroPoints.selectItem(lotroPoints);
    // Destiny points
    DestinyPointsRewardFilter destinyPointsFilter=_filter.getDestinyPointsFilter();
    Boolean destinyPoints=destinyPointsFilter.getHasDestinyPointsFlag();
    _destinyPoints.selectItem(destinyPoints);
    // Class point
    ClassPointRewardFilter classPointFilter=_filter.getClassPointsFilter();
    Boolean classPoint=classPointFilter.getHasClassPointFlag();
    _classPoints.selectItem(classPoint);
    // Trait
    TraitRewardFilter traitFilter=_filter.getTraitFilter();
    String trait=traitFilter.getTrait();
    _trait.selectItem(trait);
    // Title
    TitleRewardFilter titleFilter=_filter.getTitleFilter();
    String title=titleFilter.getTitle();
    _title.selectItem(title);
    // Virtue
    VirtueRewardFilter virtueFilter=_filter.getVirtueFilter();
    VirtueId virtueId=virtueFilter.getVirtueId();
    _virtue.selectItem(virtueId);
    // Emote
    EmoteRewardFilter emoteFilter=_filter.getEmoteFilter();
    String emote=emoteFilter.getEmote();
    _emote.selectItem(emote);
    // Item
    ItemRewardFilter itemFilter=_filter.getItemFilter();
    Integer itemId=itemFilter.getItemId();
    _item.selectItem(itemId);
  }

  private JPanel buildRewardsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;

    GridBagConstraints c;
    {
      JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      // Reputation
      linePanel.add(GuiFactory.buildLabel("Reputation:"));
      _reputation=buildReputationCombobox();
      linePanel.add(_reputation.getComboBox());
      // Title
      linePanel.add(GuiFactory.buildLabel("Title:"));
      _title=buildTitlesCombobox();
      linePanel.add(_title.getComboBox());
      // Virtue
      linePanel.add(GuiFactory.buildLabel("Virtue:"));
      _virtue=buildVirtuesCombobox();
      linePanel.add(_virtue.getComboBox());
      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,0,0),0,0);
      panel.add(linePanel,c);
      y++;
    }

    {
      JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      // Traits
      linePanel.add(GuiFactory.buildLabel("Trait:"));
      _trait=buildTraitsCombobox();
      linePanel.add(_trait.getComboBox());
      // Emotes
      linePanel.add(GuiFactory.buildLabel("Emote:"));
      _emote=buildEmotesCombobox();
      linePanel.add(_emote.getComboBox());
      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,0,0),0,0);
      panel.add(linePanel,c);
      y++;
    }

    {
      JPanel line=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      // Items
      line.add(GuiFactory.buildLabel("Item:"));
      _item=buildItemsCombobox();
      line.add(_item.getComboBox());
      // LOTRO points
      line.add(GuiFactory.buildLabel("LOTRO points:"));
      _lotroPoints=buildLotroPointsCombobox();
      line.add(_lotroPoints.getComboBox());
      // Destiny points
      line.add(GuiFactory.buildLabel("Destiny points:"));
      _destinyPoints=buildDestinyPointsCombobox();
      line.add(_destinyPoints.getComboBox());
      // Class point
      line.add(GuiFactory.buildLabel("Class point:"));
      _classPoints=buildClassPointsCombobox();
      line.add(_classPoints.getComboBox());
      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,0),0,0);
      panel.add(line,c);
      y++;
    }

    return panel;
  }

  private ComboBoxController<Boolean> buildLotroPointsCombobox()
  {
    ComboBoxController<Boolean> combo=build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        LotroPointsRewardFilter filter=_filter.getLotroPointsFilter();
        filter.setHasLotroPointsFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Boolean> buildDestinyPointsCombobox()
  {
    ComboBoxController<Boolean> combo=build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        DestinyPointsRewardFilter filter=_filter.getDestinyPointsFilter();
        filter.setHasLotroPointsFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Boolean> buildClassPointsCombobox()
  {
    ComboBoxController<Boolean> combo=build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        ClassPointRewardFilter filter=_filter.getClassPointsFilter();
        filter.setHasClassPointFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  /**
   * Build a combo-box controller to choose from null, true or false.
   * @return A new combo-box controller.
   */
  private ComboBoxController<Boolean> build3StatesBooleanCombobox()
  {
    ComboBoxController<Boolean> ctrl=new ComboBoxController<Boolean>();
    ctrl.addEmptyItem("");
    ctrl.addItem(Boolean.TRUE,"With");
    ctrl.addItem(Boolean.FALSE,"Without");
    ctrl.selectItem(null);
    return ctrl;
  }

  private ComboBoxController<Faction> buildReputationCombobox()
  {
    ComboBoxController<Faction> combo=SharedUiUtils.buildFactionCombo();
    ItemSelectionListener<Faction> listener=new ItemSelectionListener<Faction>()
    {
      @Override
      public void itemSelected(Faction faction)
      {
        ReputationRewardFilter filter=_filter.getReputationFilter();
        filter.setFaction(faction);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<String> buildTraitsCombobox()
  {
    ComboBoxController<String> combo=_uiUtils.buildTraitsCombo();
    ItemSelectionListener<String> listener=new ItemSelectionListener<String>()
    {
      @Override
      public void itemSelected(String trait)
      {
        TraitRewardFilter filter=_filter.getTraitFilter();
        filter.setTrait(trait);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<String> buildTitlesCombobox()
  {
    ComboBoxController<String> combo=_uiUtils.buildTitlesCombo();
    ItemSelectionListener<String> listener=new ItemSelectionListener<String>()
    {
      @Override
      public void itemSelected(String title)
      {
        TitleRewardFilter filter=_filter.getTitleFilter();
        filter.setTitle(title);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<VirtueId> buildVirtuesCombobox()
  {
    ComboBoxController<VirtueId> combo=SharedUiUtils.buildVirtueCombo();
    ItemSelectionListener<VirtueId> listener=new ItemSelectionListener<VirtueId>()
    {
      @Override
      public void itemSelected(VirtueId virtueId)
      {
        VirtueRewardFilter filter=_filter.getVirtueFilter();
        filter.setVirtueId(virtueId);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<String> buildEmotesCombobox()
  {
    ComboBoxController<String> combo=_uiUtils.buildEmotesCombo();
    ItemSelectionListener<String> listener=new ItemSelectionListener<String>()
    {
      @Override
      public void itemSelected(String emote)
      {
        EmoteRewardFilter filter=_filter.getEmoteFilter();
        filter.setEmote(emote);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Integer> buildItemsCombobox()
  {
    ComboBoxController<Integer> combo=_uiUtils.buildItemsCombo();
    ItemSelectionListener<Integer> listener=new ItemSelectionListener<Integer>()
    {
      @Override
      public void itemSelected(Integer itemId)
      {
        ItemRewardFilter filter=_filter.getItemFilter();
        filter.setItemId(itemId);
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
    // Controllers
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_reputation!=null)
    {
      _reputation.dispose();
      _reputation=null;
    }
    if (_lotroPoints!=null)
    {
      _lotroPoints.dispose();
      _lotroPoints=null;
    }
    if (_destinyPoints!=null)
    {
      _destinyPoints.dispose();
      _destinyPoints=null;
    }
    if (_classPoints!=null)
    {
      _classPoints.dispose();
      _classPoints=null;
    }
    if (_trait!=null)
    {
      _trait.dispose();
      _trait=null;
    }
    if (_title!=null)
    {
      _title.dispose();
      _title=null;
    }
    if (_virtue!=null)
    {
      _virtue.dispose();
      _virtue=null;
    }
    if (_emote!=null)
    {
      _emote.dispose();
      _emote=null;
    }
    if (_item!=null)
    {
      _item.dispose();
      _item=null;
    }
  }
}