package delta.games.lotro.lore.deeds.index;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * Index for deeds.
 * @author DAM
 */
public class DeedsIndex
{
  private HashMap<String,DeedCategory> _categories;

  /**
   * Constructor.
   */
  public DeedsIndex()
  {
    _categories=new HashMap<String,DeedCategory>();
  }

  /**
   * Get a category by its name. 
   * @param name Name of category to get.
   * @return A category or <code>null</code> if not found.
   */
  public DeedCategory getCategory(String name)
  {
    return _categories.get(name);
  }

  /**
   * Add a deed.
   * @param category Deed category.
   * @param identifier Deed identifier.
   * @param name Deed name.
   * @return <code>true</code> if it was done, <code>false</code> otherwise.
   */
  public boolean addDeed(String category, String identifier, String name)
  {
    DeedCategory c=_categories.get(category);
    if (c==null)
    {
      c=new DeedCategory(category);
      _categories.put(category,c);
    }
    boolean ok=c.addDeed(identifier,name);
    return ok;
  }

  /**
   * Get all the categories.
   * @return A possibly empty array of categories.
   */
  public String[] getCategories()
  {
    String[] ret=null;
    Set<String> ids=_categories.keySet();
    if (ids!=null)
    {
      ret=ids.toArray(new String[ids.size()]);
      Arrays.sort(ret);
    }
    else
    {
      ret=new String[0];
    }
    return ret;
  }
}