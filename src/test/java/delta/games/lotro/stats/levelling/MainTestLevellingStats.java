package delta.games.lotro.stats.levelling;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.level.LevelHistory;
import delta.games.lotro.character.log.LotroTestUtils;
import delta.games.lotro.gui.stats.curves.DatedCurvesChartConfiguration;
import delta.games.lotro.gui.stats.curves.DatedCurvesChartController;
import delta.games.lotro.gui.stats.curves.MultipleToonsDatedCurvesProvider;
import delta.games.lotro.gui.stats.levelling.LevelCurveProvider;
import delta.games.lotro.stats.level.MultipleToonsLevellingStats;

/**
 * Test for character levelling graph.
 * @author DAM
 */
public class MainTestLevellingStats
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    List<CharacterFile> toons=utils.getAllFiles();
    MultipleToonsLevellingStats stats=new MultipleToonsLevellingStats();
    //String[] names={"Glumlug","Feroce","Tilmogrim","Beleganth"};
    //for(String name : names)
    //CharacterFile toon=utils.getMainToon();
    for(CharacterFile toon : toons)
    {
      stats.addToon(toon);
    }

    JFrame f=new JFrame();
    DatedCurvesChartConfiguration configuration=new DatedCurvesChartConfiguration();
    configuration.setChartTitle("Characters levelling");
    configuration.setValueAxisLabel("Level");
    configuration.setValueAxisTicks(new double[]{1,5,10});
    LevelCurveProvider curveProvider=new LevelCurveProvider();
    MultipleToonsDatedCurvesProvider<LevelHistory> provider=new MultipleToonsDatedCurvesProvider<LevelHistory>(stats,curveProvider);
    DatedCurvesChartController controller=new DatedCurvesChartController(provider,configuration);
    JPanel panel=controller.getPanel();
    f.getContentPane().add(panel);
    f.pack();
    f.setVisible(true);
    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }
}
