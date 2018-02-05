package delta.games.lotro.gui.character.stats.details;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Test class for the single stat display controller.
 * @author DAM
 */
public class MainTestSingleStatWidgetsController
{
  private void doIt()
  {
    BasicStatsSet value=new BasicStatsSet();
    value.addStat(STAT.MORALE,new FixedDecimalsInteger(100));
    value.addStat(STAT.CRITICAL_MELEE_PERCENTAGE,new FixedDecimalsInteger(12.3f));
    BasicStatsSet reference=new BasicStatsSet();
    reference.addStat(STAT.MORALE,new FixedDecimalsInteger(67));
    reference.addStat(STAT.CRITICAL_MELEE_PERCENTAGE,new FixedDecimalsInteger(13.3f));

    JLabel morale=GuiFactory.buildLabel("Morale");
    SingleStatWidgetsController moraleCtrl=new SingleStatWidgetsController(STAT.MORALE,false);
    moraleCtrl.updateStats(reference,value);
    showFrameForStat(morale,moraleCtrl);
    JLabel critMelee=GuiFactory.buildLabel("Crit Melee %");
    SingleStatWidgetsController critCtrl=new SingleStatWidgetsController(STAT.CRITICAL_MELEE_PERCENTAGE,true);
    critCtrl.updateStats(reference,value);
    showFrameForStat(critMelee,critCtrl);
  }

  private void showFrameForStat(JLabel label,SingleStatWidgetsController singleCtrl)
  {
    JPanel panel=new JPanel(new FlowLayout());
    panel.add(label);
    panel.add(singleCtrl.getValueLabel());
    panel.add(singleCtrl.getDeltaValueLabel());
    JFrame frame=new JFrame();
    frame.add(panel);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestSingleStatWidgetsController().doIt();
  }
}