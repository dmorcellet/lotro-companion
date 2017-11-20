package delta.games.lotro.gui.character.stats.contribs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.checkbox.CheckboxController;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.stats.CharacterStatsComputer;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.character.stats.contribs.ContribsByStat;
import delta.games.lotro.character.stats.contribs.StatsContributionsManager;

/**
 * Controller for a panel that displays the contributions of to stats.
 * @author DAM
 */
public class StatContribsPanelController
{
  // Data
  private CharacterData _toon;
  // Utils
  private CharacterStatsComputer _statsComputer;
  private StatsContributionsManager _contribs;
  // UI
  private JPanel _panel;
  // Controllers
  private ComboBoxController<STAT> _statChooser;
  private StatContribsChartPanelController _chartPanel;
  private CheckboxController _merged;
  private StatContribsTableController _table;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public StatContribsPanelController(CharacterData toon)
  {
    _toon=toon;
    _contribs=new StatsContributionsManager();
    _statsComputer=new CharacterStatsComputer(_contribs);
    _table=new StatContribsTableController();
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Update values.
   */
  public void update()
  {
    _statsComputer.getStats(_toon);
    _contribs.compute();
    updateStatCombo();
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    // Top panel
    panel.add(buildTopPanel(),BorderLayout.NORTH);
    // Center panel
    {
      _chartPanel=new StatContribsChartPanelController();
      JPanel chartPanel=_chartPanel.getPanel();
      TitledBorder border=GuiFactory.buildTitledBorder("Contributions Chart");
      chartPanel.setBorder(border);
      panel.add(chartPanel,BorderLayout.CENTER);
    }
    // Right panel
    JPanel right=buildRightPanel();
    panel.add(right,BorderLayout.EAST);
    return panel;
  }

  private JPanel buildTopPanel()
  {
    JPanel comboPanel=buildComboPanel();
    return comboPanel;
  }

  private JPanel buildRightPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    JPanel configPanel=buildConfigurationPanel();
    panel.add(configPanel,BorderLayout.NORTH);
    JScrollPane scroll=GuiFactory.buildScrollPane(_table.getTable());
    TitledBorder border=GuiFactory.buildTitledBorder("Contributions Table");
    scroll.setBorder(border);
    panel.add(scroll,BorderLayout.CENTER);
    return panel;
  }

  private JPanel buildConfigurationPanel()
  {
    _merged=new CheckboxController("Merged");
    _merged.setSelected(false);
    ActionListener l=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        setMerged(_merged.isSelected());
      }
    };
    _merged.getCheckbox().addActionListener(l);
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(_merged.getCheckbox(),c);
    TitledBorder border=GuiFactory.buildTitledBorder("Configuration");
    panel.setBorder(border);
    return panel;
  }

  private JPanel buildComboPanel()
  {
    _statChooser=buildStatCombo();
    JPanel panel=GuiFactory.buildPanel(new FlowLayout());
    panel.add(GuiFactory.buildLabel("Stat:"));
    panel.add(_statChooser.getComboBox());
    return panel;
  }

  private void updateStatCombo()
  {
    STAT currentStat=_statChooser.getSelectedItem();
    _statChooser.removeAllItems();
    boolean found=false;
    for(STAT stat : STAT.values())
    {
      ContribsByStat contribs=_contribs.getContribs(stat);
      if (contribs!=null)
      {
        _statChooser.addItem(stat,stat.getName());
        if (stat==currentStat)
        {
          found=true;
        }
      }
    }
    if (found)
    {
      _statChooser.selectItem(currentStat);
    }
  }

  private ComboBoxController<STAT> buildStatCombo()
  {
    ComboBoxController<STAT> controller=new ComboBoxController<STAT>();
    ItemSelectionListener<STAT> listener=new ItemSelectionListener<STAT>()
    {
      public void itemSelected(STAT stat)
      {
        updateStat(stat);
      }
    };
    controller.addListener(listener);
    return controller;
  }

  private void updateStat(STAT stat)
  {
    ContribsByStat contribs=_contribs.getContribs(stat);
    if (contribs==null)
    {
      contribs=new ContribsByStat(stat);
    }
    _chartPanel.setContributions(contribs);
    _table.setContributions(contribs);
  }

  /**
   * Update the value of the 'merged' flag (and update chart UI).
   * @param merged Value to set.
   */
  private void setMerged(boolean merged)
  {
    _contribs.setResolveIndirectContributions(merged);
    update();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _toon=null;
    // Utils
    _statsComputer=null;
    _contribs=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_statChooser!=null)
    {
      _statChooser.dispose();
      _statChooser=null;
    }
    if (_chartPanel!=null)
    {
      _chartPanel.dispose();
      _chartPanel=null;
    }
    if (_merged!=null)
    {
      _merged.dispose();
      _merged=null;
    }
  }
}