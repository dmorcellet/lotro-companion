package delta.games.lotro.gui.maps;

import java.awt.Dimension;

import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.gui.maps.basemap.DatBasemapImageProvider;
import delta.games.lotro.lore.geo.GeoBoundingBox;
import delta.games.lotro.lore.maps.MapDescription;
import delta.games.lotro.maps.data.GeoBox;
import delta.games.lotro.maps.data.GeoPoint;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemap;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemapsManager;
import delta.games.lotro.maps.ui.MapCanvas;
import delta.games.lotro.maps.ui.MapPanelController;
import delta.games.lotro.maps.ui.MapUiUtils;
import delta.games.lotro.maps.ui.layers.BasemapLayer;
import delta.games.lotro.maps.ui.layers.radar.RadarImageProvider;
import delta.games.lotro.utils.maps.Maps;

/**
 * Configures a map panel to show the given map description.
 * @author DAM
 */
public class MapPanelConfigurator
{
  private static final Dimension MAX_SIZE=new Dimension(1024,768);

  /**
   * Constructor.
   * @param facade Data facade.
   * @param panel Panel to use.
   * @param mapDescription Map description to use.
   */
  public static void configureCanvas(DataFacade facade, MapPanelController panel, MapDescription mapDescription)
  {
    MapCanvas canvas=panel.getCanvas();

    GeoreferencedBasemap basemap=null;

    // Configure bounding box
    // - specified bounding box?
    GeoBoundingBox geoBBox=mapDescription.getBoundingBox();
    if (geoBBox!=null)
    {
      GeoPoint p1=new GeoPoint(geoBBox.getMin().x,geoBBox.getMin().y);
      GeoPoint p2=new GeoPoint(geoBBox.getMax().x,geoBBox.getMax().y);
      GeoBox box=new GeoBox(p1,p2);
      MapUiUtils.configureMapPanel(panel,box,MAX_SIZE);
    }
    else
    {
      // Use the bouding box of the map, if any
      Integer mapId=mapDescription.getMapId();
      if (mapId!=null)
      {
        MapsManager mapsManager=Maps.getMaps().getMapsManager();
        GeoreferencedBasemapsManager basemapsManager=mapsManager.getBasemapsManager();
        basemap=basemapsManager.getMapById(mapId.intValue());
        if (basemap!=null)
        {
          MapUiUtils.configureMapPanel(panel,basemap.getBoundingBox(),MAX_SIZE);
        }
      }
    }

    // Basemap?
    if (basemap!=null)
    {
      BasemapLayer basemapLayer=new BasemapLayer();
      DatBasemapImageProvider imageProvider=new DatBasemapImageProvider(facade);
      basemapLayer.setBasemapImageProvider(imageProvider);
      basemapLayer.setMap(basemap);
      canvas.addLayer(basemapLayer);
    }
    // Radar map?
    RadarImageProvider provider=new DatRadarImageProvider(facade);
    int region=mapDescription.getRegion();
    if (region!=0)
    {
      RadarMapLayer radarLayer=new RadarMapLayer(region,provider);
      canvas.addLayer(radarLayer);
    }
  }
}
