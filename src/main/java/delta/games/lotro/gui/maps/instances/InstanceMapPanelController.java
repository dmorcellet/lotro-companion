package delta.games.lotro.gui.maps.instances;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.gui.maps.MapPanelConfigurator;
import delta.games.lotro.lore.instances.InstanceMapDescription;
import delta.games.lotro.lore.instances.PrivateEncounter;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.data.categories.CategoriesManager;
import delta.games.lotro.maps.data.markers.MarkersFinder;
import delta.games.lotro.maps.ui.DefaultMarkerIconsProvider;
import delta.games.lotro.maps.ui.MapCanvas;
import delta.games.lotro.maps.ui.MapPanelController;
import delta.games.lotro.maps.ui.MarkerIconProvider;
import delta.games.lotro.maps.ui.filter.MapFilterPanelController;
import delta.games.lotro.maps.ui.layers.MarkersLayer;
import delta.games.lotro.maps.ui.layers.SimpleMarkersProvider;
import delta.games.lotro.utils.maps.Maps;

/**
 * Controller for an instance map panel.
 * @author DAM
 */
public class InstanceMapPanelController
{
  private PrivateEncounter _privateEncounter;
  private InstanceMapDescription _mapDescription;
  private MapPanelController _mapPanel;

  /**
   * Constructor.
   * @param facade Data facade.
   * @param privateEncounter Private encounter to use.
   * @param mapDescription Map description for this private encounter.
   */
  public InstanceMapPanelController(DataFacade facade, PrivateEncounter privateEncounter, InstanceMapDescription mapDescription)
  {
    _privateEncounter=privateEncounter;
    _mapDescription=mapDescription;
    _mapPanel=buildPanel(facade);
  }

  /**
   * Get the managed map panel controller.
   * @return the managed map panel controller.
   */
  public MapPanelController getMapPanelController()
  {
    return _mapPanel;
  }

  /**
   * Build the map panel.
   * @param facade Data facade.
   * @return the new map panel controller.
   */
  private MapPanelController buildPanel(DataFacade facade)
  {
    MapPanelController panel=new MapPanelController();
    MapCanvas canvas=panel.getCanvas();
    MapsManager mapsManager=Maps.getMaps().getMapsManager();

    // Markers filter UI
    CategoriesManager categoriesManager=mapsManager.getCategories();
    MapFilterPanelController mapFilterCtrl=new MapFilterPanelController(categoriesManager,canvas);
    panel.addFilterButton(mapFilterCtrl);

    // Basemap
    MapPanelConfigurator.configureCanvas(facade,panel,_mapDescription.getMap());
    // Markers
    MarkerIconProvider iconsProvider=new DefaultMarkerIconsProvider(categoriesManager);
    SimpleMarkersProvider markersProvider=new SimpleMarkersProvider();
    List<Marker> markers=findMarkers();
    markersProvider.setMarkers(markers);
    MarkersLayer markersLayer=new MarkersLayer(iconsProvider,markersProvider);
    markersLayer.setFilter(mapFilterCtrl.getFilter());
    canvas.addLayer(markersLayer);
    return panel;
  }

  private List<Marker> findMarkers()
  {
    List<Marker> ret=new ArrayList<Marker>();
    MapsManager mapsManager=Maps.getMaps().getMapsManager();
    MarkersFinder markersFinder=mapsManager.getMarkersFinder();
    int contentLayer=_privateEncounter.getContentLayerId();
    /*
    List<Marker> markers=markersFinder.findMarkersForContentLayer(contentLayer);
    ret.addAll(markers);
    System.out.println("Nb markers in CL: "+markers.size());
    for(Marker marker : markers)
    {
      System.out.println(marker+" => "+getBlock(marker));
    }
    */
    List<Integer> zones=_mapDescription.getZoneIds();
    for(Integer zone : zones)
    {
      List<Marker> markers=markersFinder.findMarkers(zone.intValue(),contentLayer);
      ret.addAll(markers);
      for(Integer contentLayerId : _privateEncounter.getAdditionalContentLayers())
      {
        List<Marker> clMarkers=markersFinder.findMarkers(zone.intValue(),contentLayerId.intValue());
        ret.addAll(clMarkers);
      }
      List<Marker> markers2=markersFinder.findMarkers(zone.intValue(),2);
      ret.addAll(markers2);
      /*
      for(Marker marker2 : markers2)
      {
        System.out.println(marker2);
      }
      */
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _privateEncounter=null;
    _mapDescription=null;
    if (_mapPanel!=null)
    {
      _mapPanel.dispose();
      _mapPanel=null;
    }
  }
}
