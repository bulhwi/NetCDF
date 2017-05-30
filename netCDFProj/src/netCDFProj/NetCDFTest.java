package netCDFProj;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import ucar.multiarray.MultiArray;
import ucar.netcdf.Netcdf;
import ucar.netcdf.NetcdfFile;
import ucar.netcdf.Variable;

public class NetCDFTest {
	static String fileName = "C:\\Users\\bulhwi\\Desktop\\sresa1b_ncar_ccsm3-example.nc";
	
	public static void main(String[] args) {
		try {
			Netcdf nc  = new NetcdfFile(
					fileName,
					true // 읽기전용으로 연다.
				);
			//nc 파일의 위도 데이터를 가져옴
			Variable lat = nc.get("lat");
			System.out.println(lat);
			int index = lat.getLengths()[0]; 	//위도의 길이
			double[] lats = new double[index]; //위도 길이만큼의 배열을 생성
			int[] dex = new int[1]; 
			for(int i = 0; i<index; i++){
				dex[0] = i;
				lats[i] = lat.getDouble(dex);
				System.out.println("lats [" + i + "] : " + lats[i]);
			}
		
			//nc 파일의 경도 데이터를 가져옴
			Variable lon = nc.get("lon");
			System.out.println(lon);    
			index = lon.getLengths()[0];     //경도의 길이
			double[] lons = new double[index]; //경도 길이만큼의 배열을 생성
			dex = new int[1];
			for(int i = 0; i<index; i++){
				dex[0] = i;
				lons[i] = lon.getDouble(dex);
				System.out.println("lons [" + i + "] : " + lons[i]);
			}
			
			
			//time data
			Variable time = nc.get("time");
			System.out.println(time);
			index = time.getLengths()[0];
			double[] timeArray = new double[index];
			dex = new int[1];
			for(int i = 0; i<index; i++){
				dex[0] = i;
				timeArray[i] = time.getDouble(dex);
				System.out.println("timeArray [" + i + "] : " + timeArray[i]);
			}
			
			
			Variable pre = nc.get("plev");
			System.out.println(pre);
			index = pre.getLengths()[0];
			double[] plevs = new double[index];
			dex = new int[1];
			for(int i = 0; i<index; i++){
				dex[0] = i;
				plevs[i] = pre.getDouble(dex);
				System.out.println("plevs [" + i + "] : " + plevs[i]);
			}
			
			Variable area = nc.get("area");
			System.out.println(area.getRank()); // 배열이 몇차원의 배열인지를 확인
			int[] areaIndex = area.getLengths();
			int[][] areaData = new int[areaIndex[0]][areaIndex[1]];
			int[] areaDex = new int[3];
			areaData[0][1] = area.getInt(new int[]{0,1});
			System.out.println(areaData[0][1]);
			
			
			/*System.out.println("areaData length ==== " + areaData.length);
			System.out.println("areaData length ==== " + areaData[0].length);
			System.out.println("areaData length ==== " + areaData[1].length);*/
			
			//경도 위도의 값으로 area 데이터를 출력
			for(int i = 0; i < areaData.length; i++){
				areaDex[0] = i;
				for(int j = 0; j<areaData[0].length; j++){
					areaDex[1] = j;
					areaData[i][j] = area.getInt(areaDex);
					System.out.println("<"+lats[i]+", "+lons[j]+">"+areaData[i][j]);
				}
			}
			
			
			///////////////////////////////////
			
			Variable tas = nc.get("tas");
			int[] tasIndex = tas.getLengths();
			int[] tasDex = new int[tas.getRank()];
			MultiArray tasMa = tas.copyout(tasDex, tasIndex); 
			float tatas = (float) tasMa.getDouble(new int[]{0,0,0});
			System.out.println(tatas);
			System.out.println((float) tasMa.getDouble(new int[]{0,0,0}));
			
			System.out.println(timeArray[0]);
			System.out.println(lats.length);
			System.out.println(lons.length);
			
			//air_temperature data   대기온도 
			for(int i = 0 ; i< timeArray.length; i++){
				for(int j = 0; j<lats.length; j++){
					for(int k = 0; k < lons.length; k++){
						System.out.println("<"+timeArray[i] +", "+lats[i]+", "+lons[j]+">"+(float) tasMa.getDouble(new int[]{i,j,k}));
					}
				}
			}
		
			
			
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
