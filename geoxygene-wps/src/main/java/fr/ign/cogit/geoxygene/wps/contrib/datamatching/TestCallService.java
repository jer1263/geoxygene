/**
 *
 *        This software is released under the licence CeCILL
 * 
 *        see Licence_CeCILL_V2-fr.txt
 *        see Licence_CeCILL_V2-en.txt
 * 
 *        see <a href="http://www.cecill.info/">http://www.cecill.info</a>
 * 
 * 
 * @copyright IGN
 *
 */
package fr.ign.cogit.geoxygene.wps.contrib.datamatching;

import fr.ign.cogit.geoxygene.http.SimpleHttpClient;

/**
 * Exemples d'appel de processus WPS en GET ou en POST.
 * 
 *
 */
public class TestCallService {
  
  
  /**
   * Test appel WPS en GET.
   */
  public void testGET() throws Exception {
    
    String serviceAppariement = "http://localhost:8086/geoserver/wps?"
        + "service=WPS&version=1.0.0&request=Execute&identifier=JTS:buffer&"
        + "datainputs=geom=POINT%280%200%29"
        + "@mimetype=application/wkt;distance=10";
     
    // On se connecte
    System.out.println("Connexion a geoserver : " + serviceAppariement);
    SimpleHttpClient client = new SimpleHttpClient(serviceAppariement);
    client.connect("GET");
    
    // On envoie et on receptionne
    // client.post(result.toString());
    String response = client.getResponse();
    System.out.println(response);
    
  }
  
  /**
   * Test appel WPS en POST.
   * 
   * @param xml
   * @throws Exception
   */
  public void testPOST(String xml) throws Exception {
    
    System.out.println("Debut POST");
    
    try {
      
      // String serviceAppariement = "http://localhost:8086/geoserver/wps/";
      String serviceAppariement = "http://hinano.ign.fr:8081/geoserver/wps/";
       
      // On se connecte
      System.out.println("Connexion a geoserver : " + serviceAppariement);
      SimpleHttpClient client = new SimpleHttpClient(serviceAppariement);
      client.connect("POST");
      
      // On envoie
      System.out.println(xml);
      client.post(xml);
      
      // On receptionne
      String response = client.getResponse();
      System.out.println(response);
      
      // On se deconnecte
      System.out.println("Deconnexion a geoserver.");
      client.disconnect();
      
    } catch (Exception e) {
      e.printStackTrace();
      // throw e;
    }
    
    System.out.println("Fin POST");
  }
  
  /**
   * 
   * @param args
   */
  public static void main(String[] args) {
    
    StringBuffer result = new StringBuffer();
    
    result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    result.append("<wps:Execute version=\"1.0.0\" service=\"WPS\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.opengis.net/wps/1.0.0\" xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd\">");   
    
    
    result.append("<ows:Identifier>gs:ImportAppariement</ows:Identifier>");
    result.append("<wps:DataInputs>");
    
    result.append("<wps:Input>");
    result.append("<ows:Identifier>userId</ows:Identifier>");
    result.append("<wps:Data>");
    result.append("<wps:LiteralData>8</wps:LiteralData>");
    result.append("</wps:Data>");
    result.append("</wps:Input>");
    
    result.append("<wps:Input>");
    result.append("<ows:Identifier>datasetIdApparie</ows:Identifier>");
    result.append("<wps:Data>");
    result.append("<wps:LiteralData>504</wps:LiteralData>");
    result.append("</wps:Data>");
    result.append("</wps:Input>");
    
    result.append("<wps:Input>");
    result.append("<ows:Identifier>datasetIdOrigine</ows:Identifier>");
    result.append("<wps:Data>");
    result.append("<wps:LiteralData>845</wps:LiteralData>");
    result.append("</wps:Data>");
    result.append("</wps:Input>");
    
    result.append("<wps:Input>");
    result.append("<ows:Identifier>layerIdAppariement</ows:Identifier>");
    result.append("<wps:Data>");
    result.append("<wps:LiteralData>TEST05</wps:LiteralData>");
    result.append("</wps:Data>");
    result.append("</wps:Input>");
        
    result.append("<wps:Input>");
    result.append("<ows:Identifier>resultAppariement</ows:Identifier>");
    result.append("<wps:Reference mimeType=\"text/xml\" xlink:href=\"http://geoserver/wps\" method=\"POST\">");
    result.append("<wps:Body>");
    result.append("<wps:Execute version=\"1.0.0\" service=\"WPS\">");
    result.append("<ows:Identifier>gs:AppariementReseau</ows:Identifier>");
    result.append("<wps:DataInputs>");
    
    result.append("<wps:Input>");
    result.append("<ows:Identifier>popRef</ows:Identifier>");
    result.append("<wps:Reference mimeType=\"text/xml\" xlink:href=\"http://geoserver/wps\" method=\"POST\">");
    result.append("<wps:Body>");
    result.append("<wps:Execute version=\"1.0.0\" service=\"WPS\">");
    result.append("<ows:Identifier>gs:Reproject</ows:Identifier>");
    result.append("<wps:DataInputs>");
    result.append("<wps:Input>");
    result.append("<ows:Identifier>features</ows:Identifier>");
    result.append("<wps:Reference mimeType=\"text/xml\" xlink:href=\"http://geoserver/wfs\" method=\"POST\">");
    result.append("<wps:Body>");
    result.append("<wfs:GetFeature service=\"WFS\" version=\"1.0.0\" outputFormat=\"GML2\" xmlns:WS_User-8=\"http://WS_User-8\">");
    result.append("<wfs:Query typeName=\"WS_User-8:i-f62579c0-824d-11e2-b63d-00199968a6ba\"/>");
    result.append("</wfs:GetFeature>");
    result.append("</wps:Body>");
    result.append("</wps:Reference>");
    result.append("</wps:Input>");
                
    result.append("<wps:Input>");
    result.append("<ows:Identifier>forcedCRS</ows:Identifier>");
    result.append("<wps:Data>");
    result.append("<wps:LiteralData>EPSG:3857</wps:LiteralData>");
    result.append("</wps:Data>");
    result.append("</wps:Input>");
                  
    result.append("<wps:Input>");
    result.append("<ows:Identifier>targetCRS</ows:Identifier>");
    result.append("<wps:Data>");
    result.append("<wps:LiteralData>EPSG:2154</wps:LiteralData>");
    result.append("</wps:Data>");
    result.append("</wps:Input>");
              
    result.append("</wps:DataInputs>");
    
    result.append("<wps:ResponseForm>");
    result.append("<wps:RawDataOutput mimeType=\"text/xml; subtype=wfs-collection/1.0\">");
    result.append("<ows:Identifier>result</ows:Identifier>");
    result.append("</wps:RawDataOutput>");
    result.append("</wps:ResponseForm>");
              
    result.append("</wps:Execute>");
    result.append("</wps:Body>");
    result.append("</wps:Reference>");
    result.append("</wps:Input>");
          
    result.append("<wps:Input>");
    result.append("<ows:Identifier>distanceNoeudsMax</ows:Identifier>");
    result.append("<wps:Data>");
    result.append("<wps:LiteralData>30</wps:LiteralData>");
    result.append("</wps:Data>");
    result.append("</wps:Input>");
    
    result.append("</wps:DataInputs>");
    result.append("<wps:ResponseForm>");
    result.append("<wps:RawDataOutput mimeType=\"text/xml\">");
    result.append("<ows:Identifier>popApp</ows:Identifier>");
    result.append("</wps:RawDataOutput>");
    result.append("</wps:ResponseForm>");
    result.append("</wps:Execute>");
    result.append("</wps:Body>");
    result.append("</wps:Reference>");
    result.append("</wps:Input>");
    result.append("</wps:DataInputs>");
    result.append("<wps:ResponseForm>");
    result.append("<wps:RawDataOutput>");
    result.append("<ows:Identifier>userLayerApp</ows:Identifier>");
    result.append("</wps:RawDataOutput>");
    result.append("</wps:ResponseForm>");
    result.append("</wps:Execute>");
    
    /*result.append("<ows:Identifier>gs:NetworkDataMatching</ows:Identifier>");
    
    result.append("<wps:DataInputs>");
    result.append("<wps:Input>");
    result.append("<ows:Identifier>popRef</ows:Identifier>");
    result.append("<wps:Reference mimeType=\"text/xml; subtype=wfs-collection/1.0\" xlink:href=\"http://geoserver/wfs\" method=\"POST\">");
    result.append("<wps:Body>");
    result.append("<wfs:GetFeature service=\"WFS\" version=\"1.0.0\" outputFormat=\"GML2\" xmlns:local=\"local\">");
    result.append("<wfs:Query typeName=\"local:shp_bdcarto_route\" />");
    result.append("</wfs:GetFeature>");
    result.append("</wps:Body>");
    result.append("</wps:Reference>");
    result.append("</wps:Input>");
    result.append("<wps:Input>");
    result.append("<ows:Identifier>popComp</ows:Identifier>");
    result.append("<wps:Reference mimeType=\"text/xml; subtype=wfs-collection/1.0\" xlink:href=\"http://geoserver/wfs\" method=\"POST\">");
    result.append("<wps:Body>");
    result.append("<wfs:GetFeature service=\"WFS\" version=\"1.0.0\" outputFormat=\"GML2\" xmlns:local=\"local\">");
    result.append("<wfs:Query typeName=\"local:shp_bdtopo_route\" />");
    result.append("</wfs:GetFeature>");
    result.append("</wps:Body>");
    result.append("</wps:Reference>");
    result.append("</wps:Input>");
    result.append("</wps:DataInputs>");
    
    result.append("<wps:ResponseForm>");
    result.append("<wps:RawDataOutput mimeType=\"text/xml\">");
    result.append("<ows:Identifier>popApp</ows:Identifier>");
    result.append("</wps:RawDataOutput>");
    result.append("</wps:ResponseForm>");
    
    result.append("</wps:Execute>");*/
    
    try {
      
      TestCallService test = new TestCallService();
      
      // Test appel en GET
      // test.testGET();
      
      // Test appel en POST
      test.testPOST(result.toString());
    
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    
  }
  
  
}
