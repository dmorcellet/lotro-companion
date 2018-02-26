package delta.games.lotro.gui.deed;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.common.rewards.filters.TitleRewardFilter;
import delta.games.lotro.common.rewards.filters.VirtueRewardFilter;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.filters.DeedCategoryFilter;
import delta.games.lotro.lore.deeds.filters.DeedNameFilter;
import delta.games.lotro.lore.deeds.filters.DeedRewardFilter;
import delta.games.lotro.lore.deeds.filters.DeedTypeFilter;

/**
 * Deed filter.
 * @author DAM
 */
public class DeedFilter implements Filter<DeedDescription>
{
  private Filter<DeedDescription> _filter;

  private DeedNameFilter _nameFilter;
  private DeedTypeFilter _typeFilter;
  private DeedCategoryFilter _categoryFilter;
  private TitleRewardFilter _titleFilter;
  private VirtueRewardFilter _virtueFilter;

  /**
   * Constructor.
   */
  public DeedFilter()
  {
    List<Filter<DeedDescription>> filters=new ArrayList<Filter<DeedDescription>>();
    // Name
    _nameFilter=new DeedNameFilter();
    filters.add(_nameFilter);
    // Type
    _typeFilter=new DeedTypeFilter(null);
    filters.add(_typeFilter);
    // Category
    _categoryFilter=new DeedCategoryFilter(null);
    filters.add(_categoryFilter);
    // Title
    _titleFilter=new TitleRewardFilter(null);
    filters.add(new DeedRewardFilter(_titleFilter));
    // Virtue
    _virtueFilter=new VirtueRewardFilter(null);
    filters.add(new DeedRewardFilter(_virtueFilter));
    _filter=new CompoundFilter<DeedDescription>(Operator.AND,filters);
  }

  /**
   * Get the filter on deed name.
   * @return a deed name filter.
   */
  public DeedNameFilter getNameFilter()
  {
    return _nameFilter;
  }

  /**
   * Get the filter on deed type.
   * @return a deed type filter.
   */
  public DeedTypeFilter getTypeFilter()
  {
    return _typeFilter;
  }

  /**
   * Get the filter on deed category.
   * @return a deed category filter.
   */
  public DeedCategoryFilter getCategoryFilter()
  {
    return _categoryFilter;
  }

  /**
   * Get the filter on deed title.
   * @return a deed title filter.
   */
  public TitleRewardFilter getTitleFilter()
  {
    return _titleFilter;
  }

  /**
   * Get the filter on deed virtue.
   * @return a deed virtue filter.
   */
  public VirtueRewardFilter getVirtueFilter()
  {
    return _virtueFilter;
  }

  @Override
  public boolean accept(DeedDescription item)
  {
    return _filter.accept(item);
  }
}
