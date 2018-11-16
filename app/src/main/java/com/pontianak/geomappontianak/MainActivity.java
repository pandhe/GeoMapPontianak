package com.pontianak.geomappontianak;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.RasterSource;
import com.mapbox.mapboxsdk.style.sources.TileSet;
import com.mapbox.mapboxsdk.style.sources.VectorSource;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import com.pontianak.geomappontianak.modal.PolygonKecamatan;
import com.pontianak.geomappontianak.modal.model_list_metadata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.color;
import static com.mapbox.mapboxsdk.style.expressions.Expression.eq;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.step;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOutlineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback {
    private MapView mapView;
    private MapboxMap mapboxMap;
    final static int timeout=5000;
    private Service_Connector service_connector;
    private static final String GEOJSON_SOURCE_ID = "GEOJSONFILE";
    private static final String geoJsonSourceId = "geoJsonData";
    private static final String geoJsonLayerId = "polygonFillLayer";
    private static final String geoJsonSourceId2 = "geoJsonData2";
    private static final String geoJsonLayerId2 = "CircleLayer";
    private static final String geoJsonSourceId3 = "geoJsonData3";
    private static final String geoJsonLayerId3 = "PolygonLayer";

    private static final String geoJsonLayerId31 = "PolygonLayer1";
    private static final String geoJsonSourceId4 = "geoJsonData4";
    private static final String geoJsonLayerId4 = "PointGedung2";
    private static final String geoJsonSourceId5 = "geoJsonData5";
    private static final String geoJsonLayerId5 = "Polygonrumah";
    private FillLayer layer,polygonrumah;
    private LineLayer layers;
    private CircleLayer pointsLayer,pointGedung;

    private CircleLayer cr;
    private Marker marker;
    ArrayList<model_list_metadata> arrayofmeta=new ArrayList<>();

    int urusanterpilih,bdurusanterpilih,indexbd,indexur=0;

    private String[] peta = {"Street","Satelite","Dark"};
    private int checkedItems2 = 1;


    private GeoJsonSource source;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        service_connector=new Service_Connector();
        //Button button=findViewById(R.id.button);
       /* button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachlayer();
            }
        });*/


        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                AlertDialog dialog;

                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        builder.setTitle("Pilih Data");
                        final String[] arr = new String[arrayofmeta.size()];
                        final boolean[] bool = new boolean[arrayofmeta.size()];
                        for(int i=0 ; i< arrayofmeta.size();i++){
                            arr[i] = arrayofmeta.get(i).toString();
                            //getProductName or any suitable method
                        }
                        for(int i=0 ; i< arrayofmeta.size();i++){
                            bool[i] = arrayofmeta.get(i).isChecked();
                            //getProductName or any suitable method
                        }
// add a checkbox list

                        builder.setMultiChoiceItems(arr, bool, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                // user checked or unchecked a box
                               // toggleLayer(which,isChecked);
                                attachlayer(which,isChecked);
                            }
                        });

// add OK and Cancel buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user clicked OK
                            }
                        });


// create and show the alert dialog
                        dialog = builder.create();
                        dialog.show();
                        return true;
                    case R.id.navigation_dashboard:
                        builder.setTitle("Pilih Tampilan Peta");

// add a checkbox list

                        builder.setSingleChoiceItems(peta, checkedItems2, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user checked an item
                                mapboxMap.removeLayer(geoJsonLayerId3);
                               /* switch (which){
                                    case 0:
                                        mapboxMap.setStyleUrl(Style.MAPBOX_STREETS);
                                        break;
                                    case 1:
                                        mapView.setStyleUrl(Style.SATELLITE);

                                        break;
                                    case 2:
                                        mapboxMap.setStyleUrl(Style.DARK);
                                }

                               attachlayer();*/

                            }
                        });
                        ;


// add OK and Cancel buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user clicked OK
                            }
                        });


// create and show the alert dialog
                        dialog = builder.create();
                        dialog.show();
                        return true;
                    case R.id.navigation_notifications:
                        Log.d("ezz Feature ", "dashboard");
                        Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });

        Mapbox.getInstance(this, "pk.eyJ1IjoicGFuZGhlMjQiLCJhIjoiY2ptZGt5dXViMTd0MjNxcjFkZmgyMnp5cSJ9.-ATn2I1ll-HLsEySM6EtcQ");
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        indexmeta();

    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        Log.i("ezz","rewsume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {
                PointF pointf = mapboxMap.getProjection().toScreenLocation(point);
                RectF rectF = new RectF(pointf.x - 10, pointf.y - 10, pointf.x + 10, pointf.y + 10);
                List<Feature> featureList = mapboxMap.queryRenderedFeatures(rectF, geoJsonLayerId3);
                if (featureList.size() > 0) {
                    for (Feature feature : featureList) {
                        try {
                            JSONObject metadata = new JSONObject(feature.toJson());
                            JSONObject propertis = new JSONObject(metadata.getString("properties"));
                            Log.d("ezz Feature ", feature.toJson());
                            Toast.makeText(MainActivity.this, propertis.getString("namobj"),
                                    Toast.LENGTH_SHORT).show();
                            if (marker != null) {
                                mapboxMap.removeMarker(marker);
                            }

                            marker = mapboxMap.addMarker(new MarkerOptions()
                                    .position(point)
                                    .title(propertis.getString("namobj"))
                                    .snippet(propertis.getString("namobj")));

                            //marker.showInfoWindow(mapboxMap, mapView);
                        }

                        catch (JSONException jre){

                        }





                    }
                }
            }
        });

        mapView.addOnMapChangedListener(new MapView.OnMapChangedListener() {
            @Override
            public void onMapChanged(int change) {


            }
        });


        this.mapboxMap = mapboxMap;
        this.mapboxMap.getUiSettings().setRotateGesturesEnabled(false);



        addGeoJsonSourceToMap();

        layer = new FillLayer(geoJsonLayerId3, geoJsonSourceId3);
        layer.setProperties(fillOpacity(1f),
                fillOutlineColor(Color.RED),
                fillColor(Color.TRANSPARENT));






        layers=new LineLayer(geoJsonLayerId,geoJsonSourceId);
        layers.setProperties(
                PropertyFactory.lineDasharray(new Float[]{1f, 0.9f}),
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(3f),
                lineColor(Color.parseColor("#e55e5e")));
       // mapboxMap.addLayer(layers);









        //attachlayer();





    }

    private void addGeoJsonSourceToMap() {

    }


public void showdetail(String js,LatLng point){

}
    private void toggleLayer(int mark,Boolean selected) {
         //checkedItems[mark]=selected;
        Layer layer;
        Log.i("ez",String.valueOf(mark));
        switch (mark){
            case 0:
                layer = mapboxMap.getLayer(geoJsonLayerId3);
                break;
            case 1:
                layer = mapboxMap.getLayer(geoJsonLayerId2);
                break;
            case 2:
                layer = mapboxMap.getLayer(geoJsonLayerId4);
                break;
            case 3:
                layer = mapboxMap.getLayer(geoJsonLayerId5);
                break;
                default:
                    layer = mapboxMap.getLayer("museums");
                    break;
        }
        if(layer!=null) {
            if (selected) {
                layer.setProperties(visibility(VISIBLE));
            } else {
                layer.setProperties(visibility(NONE));
            }
        }


    }

    @Override
    public void onClick(View view) {
        Log.i("ezh",view.toString());
    }

    public void attachlayer(int idx,boolean ischk){
        if(ischk){
            try {
// Load GeoJSONSource

                source = new GeoJsonSource(arrayofmeta.get(idx).getLink_meta()+String.valueOf(idx), new URL("http://landakkab.ina-sdi.or.id:8080/geoserver/"+arrayofmeta.get(idx).getPublisher_meta()+"/ows?service=WFS&version=1.0.0&request=GetFeature&typeName="+arrayofmeta.get(idx).getPublisher_meta()+":"+arrayofmeta.get(idx).getLink_meta()+"&maxFeatures=50&outputFormat=application%2Fjson"));

// Add GeoJsonSource to map
                mapboxMap.addSource(source);
                //source
                if(arrayofmeta.get(idx).getLayer_Style().equals("polygon")){
                    layer = new FillLayer(arrayofmeta.get(idx).getLink_meta()+String.valueOf(idx)+"layer", arrayofmeta.get(idx).getLink_meta()+String.valueOf(idx));
                    layer.setProperties(fillOpacity(1f),
                            fillOutlineColor(Color.RED),
                            fillColor(Color.TRANSPARENT));

                    mapboxMap.addLayer(layer);
                }
                else if (arrayofmeta.get(idx).getLayer_Style().equals("point")){
                    pointsLayer = new CircleLayer(arrayofmeta.get(idx).getLink_meta()+String.valueOf(idx)+"layer", arrayofmeta.get(idx).getLink_meta()+String.valueOf(idx));
                    pointsLayer.setProperties(
                            PropertyFactory.circleColor(Color.YELLOW),
                            PropertyFactory.circleRadius(10f));
                    pointsLayer.setFilter(eq(literal("$type"), literal("Point")));
                    mapboxMap.addLayer(pointsLayer);
                }
                else if (arrayofmeta.get(idx).getLayer_Style().equals("line")){
                    layers=new LineLayer(arrayofmeta.get(idx).getLink_meta()+String.valueOf(idx)+"layer", arrayofmeta.get(idx).getLink_meta()+String.valueOf(idx));
                    layers.setProperties(
                            PropertyFactory.lineDasharray(new Float[]{1f, 0.9f}),
                            lineCap(Property.LINE_CAP_ROUND),
                            lineJoin(Property.LINE_JOIN_ROUND),
                            lineWidth(3f),
                            lineColor(Color.parseColor("#e55e5e")));

                    mapboxMap.addLayer(layers);
                }






                //Log.i("ez",source.get);




            } catch (Throwable throwable) {
                Log.e("ClickOnLayerActivity", "Couldn't add GeoJsonSource to map", throwable);
            }
        }





    }
    public void indexmeta(){
        service_connector.sendgetrequest(this, "api/getWMSlayers", new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                try{
                    JSONArray urusans=new JSONArray(response);

                    arrayofmeta.clear();
                    for(int i=0;i<urusans.length();i++){
                        JSONObject urusan=new JSONObject(urusans.get(i).toString());
                        String[] separated = urusan.getString("layer_nativename").split(":");
                        model_list_metadata mo=new model_list_metadata(false,urusan.getString("layer_name"),separated[0],separated[1],urusan.getString("layer_style"));
                        if(i==0){
                           // bdurusanterpilih=mo.id_urusan;
                        }
                        arrayofmeta.add(mo);

                    }


                }
                catch (JSONException JEO){

                }

            }

            @Override
            public void onNoConnection(String message) {

            }

            @Override
            public void OnServerError(String message) {

            }

            @Override
            public void OnTimeOut() {

            }
        });
    }
}
