package delta.games.lotro.gui.items.legendary;

import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.gui.items.legendary.shared.LegendariesTestUtils;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegendaryInstanceAttrs;

/**
 * Simple test class for the legendary instance edition panel.
 * @author DAM
 */
public class MainTestLegendaryInstanceEdition
{
  private ItemInstance<? extends Item> buildTestAttrs()
  {
    //String name="CaptainEmblemSecondAge75NonImbued.xml";
    String name="CaptainGreatSwordFirstAgeImbued.xml";
    ItemInstance<? extends Item> item=LegendariesTestUtils.loadItemInstance(name);
    return item;
  }

  private void doIt()
  {
    final ItemInstance<? extends Item> itemInstance=buildTestAttrs();

    LegendaryInstance legendaryInstance=(LegendaryInstance)itemInstance;
    final LegendaryInstanceAttrs legendaryAttrs=legendaryInstance.getLegendaryAttributes();
    ClassAndSlot constraints=new ClassAndSlot(CharacterClass.CAPTAIN,EquipmentLocation.CLASS_SLOT);
    final LegendaryInstanceEditionPanelController controller=new LegendaryInstanceEditionPanelController(null,itemInstance,constraints);

    DefaultFormDialogController<LegendaryInstanceAttrs> dialog=new DefaultFormDialogController<LegendaryInstanceAttrs>(null,legendaryAttrs)
    {
      @Override
      protected JPanel buildFormPanel()
      {
        return controller.getPanel();
      }

      @Override
      protected void okImpl()
      {
        super.okImpl();
        controller.getData(legendaryAttrs);
      }
    };
    LegendaryInstanceAttrs result=dialog.editModal();
    System.out.println("Result: "+result);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestLegendaryInstanceEdition().doIt();
  }
}
