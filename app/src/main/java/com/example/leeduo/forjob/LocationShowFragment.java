package com.example.leeduo.forjob;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

/**
 * Created by LeeDuo on 2019/2/17.
 */

public class LocationShowFragment extends Fragment {
    private View mView;
    private TextView titleText,location,distance,changeButton;
    private MapView mapView;
    private ImageView backImage;
    private Bundle bundle;
    private double latitude,longitude;
    private String locationString,companyShortNameString;
    private BaiduMap baiduMap;
    private MyLocationData myLocationData;
    private MyLocationConfiguration config;
    private boolean mapIsNormal = true;
    private MapStatusUpdate mapStatusUpdate;
    private LocationClient locationClient;
    private LocationClientOption locationClientOption;
    private double distanceNum;
    private LatLng myPlace,companyPlace;
    private TextView carText,busText,walkText;
    private ImageView carImage,busImage,walkImage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_location_show,container,false);

        bundle = getArguments();
        latitude = bundle.getDouble("latitude",46.00);
        longitude = bundle.getDouble("longitude",112.00);
        locationString = bundle.getString("location","");
        companyShortNameString = bundle.getString("company_short_name","");

        titleText = mView.findViewById(R.id.title_text);
        location = mView.findViewById(R.id.location);
        distance = mView.findViewById(R.id.distance);
        mapView = mView.findViewById(R.id.baidu_map);
        changeButton = mView.findViewById(R.id.change_button);
        backImage = mView.findViewById(R.id.back_image);
        carText = mView.findViewById(R.id.car_text);
        busText = mView.findViewById(R.id.bus_text);
        walkText = mView.findViewById(R.id.walk_text);

        carImage = mView.findViewById(R.id.car_image);
        busImage = mView.findViewById(R.id.bus_image);
        walkImage = mView.findViewById(R.id.walk_image);

        titleText.setText(companyShortNameString);
        location.setText(locationString);

        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        myLocationData = new MyLocationData.Builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
        baiduMap.setMyLocationData(myLocationData);

        //BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_map);

        config = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING,true,null);
        baiduMap.setMyLocationConfiguration(config);
        //baiduMap.setTrafficEnabled(true);

        mapStatusUpdate = MapStatusUpdateFactory.zoomTo(17.0f);
        baiduMap.setMapStatus(mapStatusUpdate);

        locationClient = new LocationClient(getActivity());
        locationClientOption = new LocationClientOption();
        locationClientOption.setCoorType("bd09ll");
        locationClientOption.setIsNeedAddress(false);
        locationClientOption.setOpenGps(true);
        locationClientOption.setScanSpan(1000);
        locationClient.setLocOption(locationClientOption);

        locationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                myPlace = new LatLng(latitude,longitude);
                companyPlace = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                distanceNum = DistanceUtil.getDistance(myPlace,companyPlace);
                distance.setText(distance2StringTool(distanceNum));
                advise(distanceNum);
            }
        });

        locationClient.start();



        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapIsNormal){
                    baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                    mapIsNormal = !mapIsNormal;
                }else{
                    baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                    mapIsNormal = !mapIsNormal;
                }
            }
        });

        return mView;
    }

    private String distance2StringTool(double distance){
        String distanceString = String.valueOf(distance);
        if(distanceString.contains("E")){
            String s[] = distanceString.split("E");
            int size = Integer.valueOf(s[1]);
                size = size - 3;
                double myDistance = Double.valueOf(s[0]) *Math.pow(10,size);
                return ((float)myDistance)+"千米";

        }else{
            if(distance > 1000){
                return ((float)(distance/1000.0))+"千米";
            }else{
                return ((float)distance)+"米";
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        baiduMap.setMyLocationEnabled(false);
        locationClient.stop();
        baiduMap = null;
        locationClient = null;
        mapView.onDestroy();
    }

    private void advise(double distanceNum){
        if(distanceNum < 2000){
            carImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_map_car_on));
            carText.setText("推荐");
            carText.setTextColor(getResources().getColor(R.color.colorGreen));
            busImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_map_bus_on));
            busText.setText("推荐");
            busText.setTextColor(getResources().getColor(R.color.colorGreen));
            walkImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_map_foot_on));
            walkText.setText("推荐");
            walkText.setTextColor(getResources().getColor(R.color.colorGreen));
        }else if(distanceNum>= 2000 && distanceNum<10000){
            carImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_map_car_on));
            carText.setText("推荐");
            carText.setTextColor(getResources().getColor(R.color.colorGreen));
            busImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_map_bus_on));
            busText.setText("推荐");
            busText.setTextColor(getResources().getColor(R.color.colorGreen));
            walkImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_map_foot_off));
            walkText.setText("不推荐");
            walkText.setTextColor(getResources().getColor(R.color.textColor));
        }else if (distanceNum >= 10000){
            carImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_map_car_on));
            carText.setText("推荐");
            carText.setTextColor(getResources().getColor(R.color.colorGreen));
            busImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_map_bus_off));
            busText.setText("不推荐");
            busText.setTextColor(getResources().getColor(R.color.textColor));
            walkImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_map_foot_off));
            walkText.setText("不推荐");
            walkText.setTextColor(getResources().getColor(R.color.textColor));
        }
    }
}
