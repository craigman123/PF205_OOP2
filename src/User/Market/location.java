/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User.Market;

import org.jxmapviewer.viewer.GeoPosition;

/**
 *
 * @author user
 */
public class location {
    private GeoPosition makeLocationGlobal;
    
    public void location(GeoPosition locate){
        makeLocationGlobal = locate;
    }
    
    public GeoPosition GetLocation(){
        return makeLocationGlobal;
    }
    
    public GeoPosition TurnToGeo(String locationString){
        GeoPosition pos = null;
        
        if (locationString != null && !locationString.isEmpty() && locationString.contains(",")) {
            try {
                String[] parts = locationString.split(",");
                double lat = Double.parseDouble(parts[0].trim()); 
                double lon = Double.parseDouble(parts[1].trim()); 
                pos = new GeoPosition(lat, lon);
            } catch (NumberFormatException e) {
                pos = new GeoPosition(10.244983, 123.794176);
            }
        } else {
            pos = new GeoPosition(10.244983, 123.794176);
        }

        System.out.println("Latitude: " + pos.getLatitude() + ", Longitude: " + pos.getLongitude());

        return pos;
    }
    
    public String GeoToString(GeoPosition locationGeo){
        String pos = locationGeo.getLatitude() + ", " + locationGeo.getLongitude();
        System.out.println("Parse To String: " + pos);
        return pos;
    }
}
