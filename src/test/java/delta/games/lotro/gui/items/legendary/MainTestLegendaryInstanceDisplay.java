package delta.games.lotro.gui.items.legendary;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import delta.games.lotro.gui.items.ItemsTestUtils;
import delta.games.lotro.gui.items.legendary.shared.LegendariesTestUtils;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;

/**
 * Test class for the legendary attributes display panel.
 * @author DAM
 */
public class MainTestLegendaryInstanceDisplay
{
  private static final Logger LOGGER=Logger.getLogger(MainTestLegendaryInstanceDisplay.class);

  private void doIt()
  {
    for(String sample : LegendariesTestUtils.TEST_SAMPLES)
    {
      try
      {
        doIt(sample);
      }
      catch(Throwable t)
      {
        LOGGER.error("Failed to handle sample: "+sample,t);
      }
    }
  }

  private void doIt(String instanceName)
  {
    ItemInstance<? extends Item> item=ItemsTestUtils.loadItemInstance(LegendariesTestUtils.class,instanceName);
    LegendaryInstanceAttrs legendaryAttrs=LegendariesTestUtils.getLegendaryAttrs(item);

    // Build controller
    LegendaryInstanceDisplayPanelController controller=new LegendaryInstanceDisplayPanelController();
    // Set data
    Integer itemLevelInt=item.getEffectiveItemLevel();
    int itemLevel=(itemLevelInt!=null)?itemLevelInt.intValue():1;
    controller.setData(itemLevel,legendaryAttrs);

    // Show window
    JFrame frame=new JFrame();
    frame.setTitle(instanceName);
    frame.add(controller.getPanel());
    frame.pack();
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.setVisible(true);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestLegendaryInstanceDisplay().doIt();
  }
}
