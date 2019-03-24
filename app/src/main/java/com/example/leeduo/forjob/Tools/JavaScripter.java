package com.example.leeduo.forjob.Tools;


import android.support.annotation.Nullable;

import java.util.ArrayList;



/**
 * Created by LeeDuo on 2019/2/20.
 */

public class JavaScripter {
    private ChinaMapWorker chinaMapWorker;
    private SquareMapWorker squareMapWorker;
    private ScatterMapWorker scatterMapWorker;
    private WordCloudMapWorker wordCloudMapWorker;

    public Worker setMapType(Map map){
        switch (map){
            case squareMap:
                squareMapWorker = new SquareMapWorker();
                return squareMapWorker;

            case chinaMap:
                chinaMapWorker = new ChinaMapWorker();
                return chinaMapWorker;

            case scatterMap:
                scatterMapWorker = new ScatterMapWorker();
                return  scatterMapWorker;

            case wordCloudMap:
                wordCloudMapWorker = new WordCloudMapWorker();
                return wordCloudMapWorker;

        }
        return null;
    }

    public enum Map{
        squareMap,chinaMap,scatterMap,wordCloudMap;
    }



    public abstract class Worker{
        public abstract String create();
    }

    public class SquareMapWorker extends Worker{
        private ArrayList<String> xLabel;
        private ArrayList<Integer> yValue;
        private String data,data1;

        public SquareMapWorker(){
            xLabel = new ArrayList<>();
            yValue = new ArrayList<>();
            data = "";
            data1 = "";
        }

        public ArrayList<String> getxLabel() {
            return xLabel;
        }

        public void setxLabel(ArrayList<String> xLabel) {
            this.xLabel = xLabel;
        }

        public ArrayList<Integer> getyValue() {
            return yValue;
        }

        public void setyValue(ArrayList<Integer> yValue) {
            this.yValue = yValue;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getData1() {
            return data1;
        }

        public void setData1(String data1) {
            this.data1 = data1;
        }

        public SquareMapWorker pushData(ArrayList<String> xLabel, ArrayList<Integer> yValue){
            squareMapWorker.setxLabel(xLabel);
            squareMapWorker.setyValue(yValue);
            return squareMapWorker;
        }

        public SquareMapWorker parse(){
            for(int i =0;i<Math.min(squareMapWorker.getxLabel().size(),squareMapWorker.getyValue().size());i++){
                squareMapWorker.setData(squareMapWorker.getData()+"'"+squareMapWorker.getxLabel().get(i)+"',");
                squareMapWorker.setData1(squareMapWorker.getData1()+squareMapWorker.getyValue().get(i)+",");
            }
            squareMapWorker.setData("data: ["+squareMapWorker.getData()+"],");
            squareMapWorker.setData1("data: ["+squareMapWorker.getData1()+"],");
            return squareMapWorker;
        }

        @Override
        public String create() {
            return "{\n" +
                    "    //标题\n" +
                    "    title: {\n" +
                    "        x: 'center',\n" +
                    "        text: '',\n" +
                    "        subtext: '',\n" +
                    "    },\n" +
                    "    tooltip: {\n" +
                    "        trigger: 'item'\n" +
                    "    },\n" +
                    "    //右上角工具箱\n" +
                    "    toolbox: {\n" +
                    "        show: false,\n" +
                    "        feature: {\n" +
                    "            dataView: {show: true, readOnly: false},\n" +
                    "            restore: {show: true},\n" +
                    "            saveAsImage: {show: true}\n" +
                    "        }\n" +
                    "    },\n" +
                    "    calculable: true,\n" +
                    "    grid: {\n" +
                    "        borderWidth: 0,\n" +
                    "        y: 80,\n" +
                    "        y2: 60\n" +
                    "    },\n" +
                    "    //x轴\n" +
                    "    xAxis: [\n" +
                    "        {\n" +
                    "            type: 'category',\n" +
                    "            show: false,\n" +
                                 data+
                    "        }\n" +
                    "    ],\n" +
                    "    //y轴\n" +
                    "    yAxis: [\n" +
                    "        {\n" +
                    "            type: 'value',\n" +
                    "            show: false\n" +
                    "        }\n" +
                    "    ],\n" +
                    "//统计图类型\n" +
                    "    series: [\n" +
                    "        {\n" +
                    "            name: '数据',\n" +
                    "            type: 'bar',\n" +
                    "            itemStyle: {\n" +
                    "                normal: {\n" +
                    "                    color: function(params) {\n" +
                    "                        // build a color map as your need.\n" +
                    "                        var colorList = [\n" +
                    "                          '#C1232B','#B5C334','#FCCE10','#E87C25','#27727B',\n" +
                    "                           '#FE8463','#9BCA63','#FAD860','#F3A43B','#60C0DD',\n" +
                    "                           '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'\n" +
                    "                        ];\n" +
                    "                        return colorList[params.dataIndex]\n" +
                    "                    },\n" +
                    "                    label: {\n" +
                    "                        show: true,\n" +
                    "                        position: 'top',\n" +
                    "                        formatter: '{b}\\n{c}'\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            },\n" +
                                 data1+
                    "            markPoint: {\n" +
                    "                tooltip: {\n" +
                    "                    trigger: 'item',\n" +
                    "                    backgroundColor: 'rgba(0,0,0,0)',\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";
        }
    }
    ///////////////////////////////////////////////////
    public class ChinaMapWorker extends Worker{
        private ArrayList<String> name;
        private ArrayList<Integer> value;
        private ArrayList<Boolean> state;
        private String data;
        private String[] provinceList;

        public ChinaMapWorker(){
            name = new ArrayList<>();
            value = new ArrayList<>();
            state = new ArrayList<>();
            data = "";
            provinceList = new String[]{"北京","上海","天津","重庆","河北","河南","云南","辽宁",
                    "黑龙江","湖南", "安徽","山东","新疆","江苏","浙江","江西","湖北","广西",
                    "甘肃","山西","内蒙古","陕西","吉林", "福建","贵州","广东","青海","西藏",
                    "四川","宁夏","海南","台湾","香港","澳门","南海诸岛"};
        }

        public ArrayList<String> getName() {
            return name;
        }

        public void setName(ArrayList<String> name) {
            this.name = name;
        }

        public ArrayList<Integer> getValue() {
            return value;
        }

        public void setValue(ArrayList<Integer> value) {
            this.value = value;
        }

        public ArrayList<Boolean> getState() {
            return state;
        }

        public void setState(ArrayList<Boolean> state) {
            this.state = state;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public ChinaMapWorker pushData(@Nullable ArrayList<String> name, ArrayList<Integer> value, ArrayList<Boolean> state){
            chinaMapWorker.setName(name);
            chinaMapWorker.setValue(value);
            chinaMapWorker.setState(state);
            return chinaMapWorker;
        }

        public ChinaMapWorker parse(){
            if(name == null){
                for(int i=0;i<provinceList.length;i++){
                    chinaMapWorker.setData(chinaMapWorker.getData()+"{name: '"+provinceList[i]+"',value: '"+chinaMapWorker.getValue().get(i)+"' ,selected:"+chinaMapWorker.getState().get(i)+"},\n");
                }
            }else{
                for(int i=0;i<chinaMapWorker.getName().size();i++){
                    chinaMapWorker.setData(chinaMapWorker.getData()+"{name: '"+chinaMapWorker.getName().get(i)+"',value: '"+chinaMapWorker.getValue().get(i)+"' ,selected:"+chinaMapWorker.getState().get(i)+"},");
                }
            }
            chinaMapWorker.setData("data:["+chinaMapWorker.getData()+"]");
            return chinaMapWorker;
        }

        @Override
        public String create() {
            return "{  \n" +
                    "               \n" +
                    "                backgroundColor: '#333333',\n" +
                    "                \n" +
                    "                title: {  \n" +
                    "                    textStyle:{color:'#111111'},\n" +
                    "                    text: '全国招聘信息大数据',\n" +
                    "                    subtext: '',  \n" +
                    "                    x:'center'  \n" +
                    "                },  \n" +
                    "                tooltip : {\n" +
                    "                    trigger: 'item'  \n" +
                    "                },  \n" +
                    "                \n" +
                    "                //左侧小导航图标\n" +
                    "                visualMap: {  \n" +
                    "                    textStyle:{color:'#ffffff'},\n" +
                    "                    show : false,  \n" +
                    "                    x: 'left',  \n" +
                    "                    y: 'center',  \n" +
                    "                    splitList: [   \n" +
                    "                        {start: 500, end:600},{start: 400, end: 500},\n" +
                    "                        {start: 300, end: 400},{start: 200, end: 300},\n" +
                    "                        {start: 100, end: 200},{start: 0, end: 100},\n" +
                    "\n" +
                    "                    ],  \n" +
                    "                    //color: ['#5475f5', '#9feaa5', '#85daef','#74e2ca', '#e6ac53', '#9fb5ea']\n" +
                    "                    color: ['#60C0DD','#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0']\n" +
                    "                     \n" +
                    "                },  \n" +
                    "                \n" +
                    "                //配置属性\n" +
                    "                series: [{  \n" +
                    "                    name: '数据',  \n" +
                    "                    type: 'map',  \n" +
                    "                    mapType: 'china',   \n" +
                    "                    roam: false,  \n" +
                    "                    selectedMode:'single',\n" +
                    "                    //left: 0, top: 0, right: 0, bottom: 0,\n" +
                    "                    boundingCoords: [\n" +
                    "                    // 定位左上角经纬度\n" +
                    "                    [73, 53],\n" +
                    "                    // 定位右下角经纬度\n" +
                    "                    [135, 3]\n" +
                    "                    ],\n" +
                    "                    itemStyle:{\n" +
                    "                                normal:{\n" +
                    "                                          areaColor:'#B5C334',\n" +
                    "                                          borderWidth:0,\n" +
                    "                                          borderColor:'#ffffff'\n" +
                    "                                        },\n" +
                    "                                        emphasis:{\n" +
                    "                                           areaColor:'#333333',\n" +
                    "                                           borderWidth:0.5,\n" +
                    "                                           borderColor:'#ffffff'\n" +
                    "                                        }\n" +
                    "                                },\n" +
                    "                    label: {  \n" +
                    "                        normal: {  \n" +
                    "                            show: false  //省份名称\n" +
                    "                        },  \n" +
                    "                        emphasis: {\n" +
                    "                           show: false,\n" +
                    "                           color:'#ff0000'\n" +
                    "                        }  \n" +
                    "                    },  \n" +
                                           data+
                    "                }]  \n" +
                    "            }";
        }
    }
    ///////////////////////////////////////////////////
    public static class MapBean{
        private double latitude;
        private double longitude;
        private String city;
        private int value;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
    public class ScatterMapWorker extends Worker{
        private ArrayList<MapBean> mapBeen;
        private String data,data1;

        public ArrayList<MapBean> getMapBeen() {
            return mapBeen;
        }

        public void setMapBeen(ArrayList<MapBean> mapBeen) {
            this.mapBeen = mapBeen;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getData1() {
            return data1;
        }

        public void setData1(String data1) {
            this.data1 = data1;
        }

        public ScatterMapWorker(){
            mapBeen = new ArrayList<>();
            data = "";
            data1 = "";
        }

        public ScatterMapWorker pushData(ArrayList<MapBean> mapBeen){
            scatterMapWorker.setMapBeen(mapBeen);
            return  scatterMapWorker;
        }

        public ScatterMapWorker parse(){
            for (int i =0;i<scatterMapWorker.getMapBeen().size();i++){
                scatterMapWorker.setData(scatterMapWorker.getData()+"\""+scatterMapWorker.getMapBeen().get(i).getCity()
                        +"\":["+scatterMapWorker.getMapBeen().get(i).getLongitude()+","+scatterMapWorker.getMapBeen().get(i).getLatitude()+"],");
                scatterMapWorker.setData1(scatterMapWorker.getData1()+"{name: \""+scatterMapWorker.getMapBeen().get(i).getCity()+"\", value: "+scatterMapWorker.getMapBeen().get(i).getValue()+"},");
            }
            scatterMapWorker.setData("{"+scatterMapWorker.getData()+"}");
            scatterMapWorker.setData1("["+scatterMapWorker.getData1()+"]");
            return scatterMapWorker;
        }
        @Override
        public String create() {
            return "option = {\n" +
                    "    backgroundColor: '#404a59',\n" +
                    "    title: {\n" +
                    "        text: '全国就业热门城市',\n" +
                    "        subtext: '2018 - 2019',\n" +
                    "        x:'center',\n" +
                    "        textStyle: {\n" +
                    "            color: '#fff'\n" +
                    "        }\n" +
                    "    },\n" +
                    "    tooltip: {\n" +
                    "        show:false,\n" +
                    "        trigger: 'item',\n" +
                    "        formatter: function (params) {\n" +
                    "            return params.name + ' : ' + params.value[2];\n" +
                    "        }\n" +
                    "    },\n" +
                    "    legend: {\n" +
                    "        show:false,\n" +
                    "        orient: 'vertical',\n" +
                    "        y: 'bottom',\n" +
                    "        x:'right',\n" +
                    "        data:['pm2.5'],\n" +
                    "        textStyle: {\n" +
                    "            color: '#fff'\n" +
                    "        }\n" +
                    "    },\n" +
                    "    visualMap: {\n" +
                    "        show:false,\n" +
                    "        min: 0,\n" +
                    "        max: 200,\n" +
                    "        calculable: true,\n" +
                    "        inRange: {\n" +
                    "            color: ['#50a3ba', '#eac736', '#d94e5d']\n" +
                    "        },\n" +
                    "        textStyle: {\n" +
                    "            color: '#fff'\n" +
                    "        }\n" +
                    "    },\n" +
                    "    geo: {\n" +
                    "        map: 'china',\n" +
                    "        label: {\n" +
                    "            emphasis: {\n" +
                    "                show: false\n" +
                    "            }\n" +
                    "        },\n" +
                    "        itemStyle: {\n" +
                    "            normal: {\n" +
                    "                areaColor: '#323c48',\n" +
                    "                borderColor: '#111'\n" +
                    "            },\n" +
                    "            emphasis: {\n" +
                    "                areaColor: '#2a333d'\n" +
                    "            }\n" +
                    "        }\n" +
                    "    },\n" +
                    "    series: [\n" +
                    "        {\n" +
                    "            name: 'pm2.5',\n" +
                    "            type: 'scatter',\n" +
                    "            coordinateSystem: 'geo',\n" +
                    "            data: convertData("+data1+","+data+"),\n" +
                    "            symbolSize: 7,\n" +
                    "            label: {\n" +
                    "                normal: {\n" +
                    "                    show: true,\n" +
                    "                    formatter: function (params) {\n" +
                    "                                   return params.name;\n" +
                    "                                },\n" +
                    "                    position:'bottom',\n" +
                    "                    fontSize:8\n" +
                    "                },\n" +
                    "                emphasis: {\n" +
                    "                    show: false\n" +
                    "                }\n" +
                    "            },\n" +
                    "            itemStyle: {\n" +
                    "                emphasis: {\n" +
                    "                    borderColor: '#fff',\n" +
                    "                    borderWidth: 1\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";
        }
    }
    ///////////////////////////////////////////////////
    public class WordCloudMapWorker extends Worker{
        private ArrayList<String> name;
        private ArrayList<Integer> value;
        private String data;

        public WordCloudMapWorker(){
            data = "";
            name = new ArrayList<>();
            value = new ArrayList<>();
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public void setName(ArrayList<String> name) {
            this.name = name;
        }

        public void setValue(ArrayList<Integer> value) {
            this.value = value;
        }

        public ArrayList<String> getName() {
            return name;
        }

        public ArrayList<Integer> getValue() {
            return value;
        }

        public WordCloudMapWorker pushData(ArrayList<String> nameList, ArrayList<Integer> value){
            wordCloudMapWorker.setName(nameList);
            wordCloudMapWorker.setValue(value);
            return wordCloudMapWorker;

        }

        public WordCloudMapWorker parse(){
            wordCloudMapWorker.setData("");
            if(wordCloudMapWorker.getName().size() == wordCloudMapWorker.getValue().size()){
                for(int i=0;i<wordCloudMapWorker.getName().size();i++){
                    wordCloudMapWorker.setData(wordCloudMapWorker.getData()+"{name:'"+wordCloudMapWorker.getName().get(i)+"',value:"+wordCloudMapWorker.getValue().get(i)+"},");
                }
            }else{
                return null;
            }
            wordCloudMapWorker.setData("data:["+wordCloudMapWorker.getData()+"]");
            return wordCloudMapWorker;

        }

        @Override
        public String create() {
            return  "{\n" +
                "    series: [{\n" +
                "        name: 'Google Trends',\n" +
                "        type: 'wordCloud',\n" +
                "        size: ['80%', '80%'],\n" +
                "        shape: 'ellipse',\n" +
                "        rotationRange: [-80, 80],"+
                "        rotationStep: 20,"+
                "        textPadding: 0,\n" +
                "        autoSize: {\n" +
                "            enable: true,\n" +
                "            minSize: 14\n" +
                "        },\n" +
                "        textStyle: {\n" +
                "                            normal: {\n" +
                "                                color: \n" +
                "                                function() {\n" +
                "                                    return 'rgb(' +\n" +
                "                                            Math.round(Math.random() * 255) +\n" +
                "                                            ', ' + Math.round(Math.random() * 255) +\n" +
                "                                            ', ' + Math.round(Math.random() * 255) + ')'\n" +
                "                                }\n" +
                "                            }\n" +
                "                        },\n" +
                data+
                "    }]\n" +
                "}";
        }
    }

}
