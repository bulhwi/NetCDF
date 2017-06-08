package netCDFProj.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ucar.multiarray.ArrayMultiArray;
import ucar.netcdf.Netcdf;
import ucar.netcdf.NetcdfFile;
import ucar.netcdf.Variable;


/*
https://www.unidata.ucar.edu/software/netcdf/java/examples/ -- 소스
https://www.unidata.ucar.edu/software/netcdf/examples/files.html - 파일
https://www.unidata.ucar.edu/downloads/netcdf/netcdf-java-4/index.jsp --jar
*/

public class TestClass {
   public static void main(String[] args) {
       float[] latList = {37.40239f, 37.402367f, 37.402344f, 37.40232f, 37.40229f};
        float[] lonList = {127.90787f, 127.91727f, 127.92667f, 127.9361f, 127.945496f};
        
        String fileName = "C:\\Users\\bulhwi\\Desktop\\fnl_2016_CFK_wrfout_d04\\wrfout_d04_2016-05-23_15_00_00";
        
        try {
         Netcdf nc = new NetcdfFile(fileName, false); //nc파일객체를 가져온다. false - 쓰기전용으로 ... true - 읽기전용으로...
         System.out.println(nc); // 파일의 구조를 출력 
         Variable xLat = nc.get("XLAT");  //XLAT 객체를 얻어온다.
         float[][][] xLats = new float[xLat.getLengths()[0]][xLat.getLengths()[1]][xLat.getLengths()[2]];
         int[] latIndex = new int[xLat.getRank()];
         
         //lat load
         for(int i = 0; i<xLats.length; i++ ){
            latIndex[0] = i;
            for(int j = 0; j<xLats[i].length; j++){
               latIndex[1] = j;
               for(int k = 0; k<xLats[i][j].length; k++){
                  latIndex[2] = k;
                  xLats[i][j][k] = xLat.getFloat(latIndex); // 인덱스에 해당되는 데이터를 가져온다.
                  /*System.out.println("<" + i+ ", "+ j + ", " + k +">" + xLats[i][j][k]);*/
               }
            }
         }
         

         //xLong load
         Variable xLong = nc.get("XLONG");
         float[][][] xLongs = new float[xLong.getLengths()[0]][xLong.getLengths()[1]][xLong.getLengths()[2]];
         int[] lonIndex = new int[xLong.getRank()];
         
         for(int i = 0; i<xLongs.length; i++){
            lonIndex[0] = i;
            for(int j = 0; j<xLongs[i].length; j++){
               lonIndex[1] = j;
               for(int k = 0; k< xLongs[i][j].length; k++){
                  lonIndex[2] = k;
                  xLongs[i][j][k] = xLong.getFloat(lonIndex);
                  /*System.out.println("<" + i+ ", "+ j + ", " + k +">" + xLongs[i][j][k]);*/
               }
            }
         }
         
         //test PSFC data load
         Variable psfc = nc.get("PSFC");
         float[][][] psfcArr = new float[psfc.getLengths()[0]][psfc.getLengths()[1]][psfc.getLengths()[2]];
         int[] psfcIndex = new int[psfc.getRank()];
         
         for(int i=0; i<psfcArr.length; i++){
            psfcIndex[0] = i;
            for(int j=0; j<psfcArr[i].length; j++){
               psfcIndex[1] = j;
               for(int k = 0; k<psfcArr[i][j].length; k++){
                  psfcIndex[2] = k;
                  psfcArr[i][j][k] = psfc.getFloat(psfcIndex);
                  /*System.out.println("<" + i+ ", "+ j + ", " + k +">" + psfcArr[i][j][k]);*/
               }
            }
         }
      
         
     /////
         
         List<Integer> xList = new ArrayList<Integer>(); //x축 인덱스
         List<Integer> yList = new ArrayList<Integer>(); //y축 인덱스

         for(int i = 0; i<xLats.length; i++){
            latIndex[0] = i;
            for(int j = 0; j<xLats[i].length; j++){
               latIndex[1] = j;
               for(int k =0; k<xLats[i][j].length; k++){
                  latIndex[2] = k;
                  for(int q = 0; q<latList.length; q++){
                     if(xLats[i][j][k] == latList[q]){
                        if(xList.size() == 0){
                           xList.add(j);
                        }else if(xList.get(0) != j){
                           xList.add(j);
                        }
                        yList.add(k);
                     }
                  }
               }
            }
         }
         
         //위도의 인덱스를 이용하여 검색하고자 하는 데이터의 경도데이터를 추출한다. 해당 경도값이 같으면 yIndex에 경도의 인덱스값을 추가한다
         List<Integer> yIndex = new ArrayList<Integer>();
         for(int i = 0; i<xLongs.length; i++){
            lonIndex[0] = i;
            for(int j = 0; j<xList.size(); j++){
               lonIndex[1] = xList.get(j);
               for(int k = 0; k<yList.size(); k++){
                  lonIndex[2] = yList.get(k);
                  for(int q = 0; q<lonList.length; q++){
                     if(xLongs[lonIndex[0]][lonIndex[1]][lonIndex[2]] == lonList[q]){
                        yIndex.add(lonIndex[2]);
                        /*System.out.println(xLongs[lonIndex[0]][lonIndex[1]][lonIndex[2]] + "***");*/
                     }
                  }
               }
            }
         }
         
         ////////////////
         //수정전 데이터
         for(int i = 0; i<psfcArr.length; i++){
            lonIndex[0] = i;
            for(int j = 0; j<xList.size(); j++){
               lonIndex[1] = xList.get(j);
               for(int k = 0; k<yIndex.size(); k++){
                  lonIndex[2] = yIndex.get(k);
                  System.out.print(lonIndex[0] + ", ");
                  System.out.print(lonIndex[1] + ", ");
                  System.out.print(lonIndex[2] + " _ ");
                  System.out.println(psfcArr[lonIndex[0]][lonIndex[1]][lonIndex[2]]);
               }
            }
         }
         
         //수정하려는 값을 배열로 
         float[][][] updateData = new float[1][xList.size()][yIndex.size()];
         for(int i=0; i<updateData.length; i++){
            for(int j=0; j<updateData[i].length; j++){
               for(int k = 0; k<updateData[i][j].length; k++){
                  updateData[i][j][k] = 300.12345F; 
               }
            }
         }
         int[] updateIndex = new int[psfc.getRank()];
         //수정하려는값 세팅
         for(int i =0; i<psfcArr.length; i++){
            updateIndex[0] = i;
            for(int j = 0; j<xList.size(); j++){
               updateIndex[1] = xList.get(j);
               for(int k=0; k<yIndex.size(); k++){
                  updateIndex[2] = yIndex.get(k);
                  psfc.set(updateIndex, updateData[i][j][k]);
               }
            }
         }
         // 세팅된 데이터값을 넣는다 .
         psfc.copyin(updateIndex, new ArrayMultiArray(updateData));
         System.out.println("수정 후 ");
         
         
         //확인
         int[] index = new int[3];
         for(int i = 0; i< psfcArr.length; i++){
            index[0] = i;
            for(int j=0; j<psfcArr[i].length; j++){
               index[1] = j;
               for(int k =0; k<psfcArr[i][j].length; k++){
                  index[2] = k;
                  psfcArr[i][j][k] = psfc.getFloat(index);
                  
                /*  System.out.println("<" + i+ ", "+ j + ", " + k +">" + psfcArr[i][j][k]);*/
               }
            }
         }
         
         
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
        
        
        
        
   }
}