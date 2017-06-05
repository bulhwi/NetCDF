package netCDFProj;

import java.io.IOException;

import ucar.multiarray.ArrayMultiArray;
import ucar.multiarray.IndexIterator;
import ucar.multiarray.MultiArray;
import ucar.multiarray.MultiArrayProxy;
import ucar.multiarray.SliceMap;
import ucar.netcdf.Dimension;
import ucar.netcdf.Netcdf;
import ucar.netcdf.NetcdfFile;
import ucar.netcdf.Variable;

/*
 * <pre>
 *  netcdf example {
 *  dimensions:
 *  	lat = 3 ;
 *  	lon = 4 ;
 *  	time = UNLIMITED ;
 *  variables:
 *  	int rh(time, lat, lon) ;
 *              rh:long_name="relative humidity" ;
 *  		rh:units = "percent" ;
 *  	double T(time, lat, lon) ;
 *              T:long_name="surface temperature" ;
 *  		T:units = "degC" ;
 *  	float lat(lat) ;
 *  		lat:units = "degrees_north" ;
 *  	float lon(lon) ;
 *  		lon:units = "degrees_east" ;
 *  	int time(time) ;
 *  		time:units = "hours" ;
 *  // global attributes:
 *  		:title = "Example Data" ;
 *  data:
 *   rh =
 *     1, 2, 3, 4,
 *     5, 6, 7, 8,
 *     9, 10, 11, 12,
 *     21, 22, 23, 24,
 *     25, 26, 27, 28,
 *     29, 30, 31, 32 ;
 *   T =
 *     1, 2, 3, 4,
 *     2, 4, 6, 8,
 *     3, 6, 9, 12,
 *     2.5, 5, 7.5, 10,
 *     5, 10, 15, 20,
 *     7.5, 15, 22.5, 30 ;
 *   lat = 41, 40, 39 ;
 *   lon = -109, -107, -105, -103 ;
 *   time = 6, 18 ;
 *  }
 * </pre>
 */

public class NetCDF_write {
	static String fileName = "C:\\Users\\bulhwi\\git\\NetCDF\\netCDFProj\\ncFile\\example.nc";

	public static void main(String[] args) {
		try {
			Netcdf nc = new NetcdfFile(fileName, false); // false - 쓰기전용으로 연다.
			Variable rh = nc.get("rh");
			int[] index = { 1, 0, 2 };

			rh.setInt(index, rh.getInt(index) + 1);
			rh.setFloat(index, rh.getFloat(index) + 1);

			// time변수에 새로운 값을 입력
			Variable time = nc.get("time");
			int[] extent = time.getLengths();
			time.setInt(extent, 24); // add 24

			int[][] rhSlice = { { 10, 20, 30, 40 }, { 20, 30, 40, 50 },	{ 30, 40, 50, 60 } };
			Dimension timeD = nc.getDimensions().get("time");
			int numRecs = timeD.getLength(); // time의 index값
			int[] origin = new int[3];
			origin[0] = numRecs - 1; // index of last record

			for (int ilat = 0; ilat < rhSlice.length; ilat++) {
				origin[1] = ilat;

				for (int ilon = 0; ilon < rhSlice[0].length; ilon++) {
					origin[2] = ilon;
					rh.setInt(origin, rhSlice[ilat][ilon]);
				}
			}
			
			////위와 같은 작업을 다른 방법으로 실행 indexIterator를 얻어 옴으로써 하나의 루프로 실행
			IndexIterator rhIi = new IndexIterator(
				new int[] {numRecs-1, 0, 0},
				rh.getLengths()
					);
			MultiArray sliceMa = new ArrayMultiArray(rhSlice); //* rhslice배열의 multiArray 생성
			IndexIterator sliceIi = new IndexIterator(sliceMa.getLengths()); //* rhSlice의 indexIterator를 얻어옴
			
			while(rhIi.notDone()){
				rh.setInt(rhIi.value(), sliceMa.getInt(sliceIi.value()) );
				rhIi.incr(); //iterator 증감
				sliceIi.incr();
			}
			
			
			//새로운 데이터를 multiArray를 이용하여 작성
			double[][][] TSlice = {{{ 5, 10, 15, 20}, {10, 15, 20, 25}, {15, 20, 25, 30}}};
			
			Variable temperature = nc.get("T"); // T 요소를 가져온다 
			origin[0] = numRecs - 1;
			origin[1] = 0;
			origin[2] = 0;
			
			temperature.copyin(origin, new ArrayMultiArray(TSlice)); //TSlice의 값을 추가하기 위한 MultiArray를 생성
			
			MultiArray T2d = new MultiArrayProxy(temperature, new SliceMap(0, numRecs - 2)); //0번째 레코드를 수정
			double[][] T2dSlice = {{4,8,12,16},{8,12,16,20},{12,16,20,24}};
			
			 T2d.copyin(new int[2], // just 0's
				       new ArrayMultiArray(T2dSlice));
			
			 System.out.println("write " + fileName + " successfully");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
