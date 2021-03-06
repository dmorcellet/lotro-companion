package delta.games.lotro.gui.stats.deeds.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.character.achievables.AchievablesStatusManager;
import delta.games.lotro.character.achievables.filter.DeedStatusFilter;
import delta.games.lotro.gui.deed.table.DeedColumnIds;
import delta.games.lotro.gui.deed.table.DeedsTableController;
import delta.games.lotro.gui.items.FilterUpdateListener;
import delta.games.lotro.gui.items.chooser.ItemChooser;
import delta.games.lotro.gui.stats.achievables.table.AchievableStatusColumnIds;
import delta.games.lotro.gui.stats.achievables.table.AchievableStatusColumnsBuilder;
import delta.games.lotro.lore.deeds.DeedDescription;

/**
 * Controller for a table that shows the status of all deeds for a single character.
 * @author DAM
 */
public class DeedStatusTableController
{
  // Data
  private TypedProperties _prefs;
  private List<AchievableStatus> _statuses;
  // GUI
  private JTable _table;
  private GenericTableController<AchievableStatus> _tableController;

  /**
   * Constructor.
   * @param deedsStatus Status to show.
   * @param prefs Preferences.
   * @param filter Managed filter.
   * @param deeds Deeds to use.
   * @param listener Listener for updates.
   */
  public DeedStatusTableController(AchievablesStatusManager deedsStatus, TypedProperties prefs, DeedStatusFilter filter, List<DeedDescription> deeds, FilterUpdateListener listener)
  {
    _prefs=prefs;
    _statuses=new ArrayList<AchievableStatus>();
    for(DeedDescription deed : deeds)
    {
      AchievableStatus status=deedsStatus.get(deed,true);
      _statuses.add(status);
    }
    _tableController=buildTable(listener);
    _tableController.setFilter(filter);
  }

  private GenericTableController<AchievableStatus> buildTable(FilterUpdateListener listener)
  {
    ListDataProvider<AchievableStatus> provider=new ListDataProvider<AchievableStatus>(_statuses);
    GenericTableController<AchievableStatus> table=new GenericTableController<AchievableStatus>(provider);
    // Deed columns
    List<TableColumnController<DeedDescription,?>> deedColumns=DeedsTableController.buildColumns();
    CellDataProvider<AchievableStatus,DeedDescription> dataProvider=new CellDataProvider<AchievableStatus,DeedDescription>()
    {
      @Override
      public DeedDescription getData(AchievableStatus status)
      {
        return (DeedDescription)status.getAchievable();
      }
    };
    for(TableColumnController<DeedDescription,?> deedColumn : deedColumns)
    {
      @SuppressWarnings("unchecked")
      TableColumnController<DeedDescription,Object> c=(TableColumnController<DeedDescription,Object>)deedColumn;
      TableColumnController<AchievableStatus,Object> proxiedColumn=new ProxiedTableColumnController<AchievableStatus,DeedDescription,Object>(c,dataProvider);
      table.addColumnController(proxiedColumn);
    }
    // Achievable status columns
    for(TableColumnController<AchievableStatus,?> column : AchievableStatusColumnsBuilder.buildDeedStateColumns(listener))
    {
      table.addColumnController(column);
    }

    TableColumnsManager<AchievableStatus> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);

    return table;
  }

  private List<String> getColumnIds()
  {
    List<String> columnIds=null;
    if (_prefs!=null)
    {
      columnIds=_prefs.getStringList(ItemChooser.COLUMNS_PROPERTY);
    }
    if (columnIds==null)
    {
      columnIds=new ArrayList<String>();
      columnIds.add(AchievableStatusColumnIds.COMPLETED.name());
      columnIds.add(DeedColumnIds.NAME.name());
      columnIds.add(DeedColumnIds.CATEGORY.name());
      columnIds.add(DeedColumnIds.LEVEL.name());
      columnIds.add(AchievableStatusColumnIds.COMPLETION_DATE.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<AchievableStatus> getTableController()
  {
    return _tableController;
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    _tableController.filterUpdated();
  }

  /**
   * Get the total number of deeds.
   * @return A number of deeds.
   */
  public int getNbItems()
  {
    return _statuses.size();
  }

  /**
   * Get the number of filtered items in the managed table.
   * @return A number of items.
   */
  public int getNbFilteredItems()
  {
    int ret=_tableController.getNbFilteredItems();
    return ret;
  }

  /**
   * Get the managed table.
   * @return the managed table.
   */
  public JTable getTable()
  {
    if (_table==null)
    {
      _table=_tableController.getTable();
    }
    return _table;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Preferences
    if (_prefs!=null)
    {
      List<String> columnIds=_tableController.getColumnsManager().getSelectedColumnsIds();
      _prefs.setStringList(ItemChooser.COLUMNS_PROPERTY,columnIds);
      _prefs=null;
    }
    // GUI
    _table=null;
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    _statuses=null;
  }
}
