package delta.games.lotro.gui.items.legendary.relics;

import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.legendary.relics.Relic;
import delta.games.lotro.lore.items.legendary.relics.RelicType;
import delta.games.lotro.lore.items.legendary.relics.RelicsManager;

/**
 * Test for relic choice window.
 * @author DAM
 */
public class MainTestShowRelicChoiceWindow
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    RelicsManager relicsMgr=RelicsManager.getInstance();
    EquipmentLocation slot=EquipmentLocation.CLASS_SLOT;
    RelicType type=RelicType.SETTING;
    Relic initialRelic=relicsMgr.getAllRelics(type,slot).get(10);
    System.out.println(initialRelic);
    Relic relic=RelicChooser.selectRelic(null,type,slot,initialRelic);
    System.out.println(relic);
  }
}
