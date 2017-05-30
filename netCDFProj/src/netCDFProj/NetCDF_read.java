package netCDFProj;

import java.io.IOException;

import ucar.multiarray.ArrayMultiArray;
import ucar.multiarray.IndexIterator;
import ucar.multiarray.MultiArray;
import ucar.multiarray.MultiArrayImpl;
import ucar.netcdf.Attribute;
import ucar.netcdf.Netcdf;
import ucar.netcdf.NetcdfFile;
import ucar.netcdf.Variable;



/**
  CDL:
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
public class NetCDF_read {
	static String fileName = "C:\\Users\\bulhwi\\Desktop\\example.nc";
	public static void main(String[] args) {
		
		try {
			Netcdf nc  = new NetcdfFile(
						fileName,
						true // 읽기전용으로 연다.
					);
			//title의 전역속성값을 가져옴
			Attribute titleA = nc.getAttribute("title");
			String title = titleA.getStringValue();
			System.out.println(title);
			
			Variable lat = nc.get("lat");
			
			int nlats = lat.getLengths()[0];
			double[] lats = new double[nlats];
			int[] index = new int[1];
			System.out.println(nlats);
			System.out.println(lat);
			for(int ilat = 0; ilat < nlats; ilat++){
				index[0] = ilat;
				lats[ilat] = lat.getDouble(index);
				System.out.println("lats [" + ilat + "] : " + lats[ilat]);
			}
			
			String latUnits = lat.getAttribute("units").getStringValue();
			System.out.println("attribute lats:units: " + latUnits);

			Variable lon = nc.get("lon");
			int[] origin = new int[lon.getRank()];
			int[] extent = lon.getLengths();
			MultiArray lonMa = lon.copyout(origin, extent);
			
			System.out.println("lons[3] : " + lonMa.getFloat(new int[]{3}));
			
			
			Variable time = nc.get("time");
			origin = new int[time.getRank()];
			extent = time.getLengths();
			System.out.println(
						"origin = new int[time.getRank()]" + origin.length
						+ "  extent = time.getLengths()" + extent.length
					);			
			MultiArray timeMa = time.copyout(origin, extent);
			System.out.println("time[1] : " + timeMa.getStorage());
			
			
			Variable rh = nc.get("area");
			int[] rhShape = rh.getLengths();
			System.out.println(rhShape[2]);
			
			int[][][] rhData = new int[rhShape[0]][rhShape[1]][rhShape[2]];
			rhData[1][0][1] = rh.getInt(new int[]{1,0,1});
			
			System.out.println("rh[1][0][1]  :  " + rhData[1][0][1]);
			  int[] ix = new int[3];
			    for (int itime = 0; itime < rhData.length; itime++) {
				ix[0] = itime;
				for (int ilat = 0; ilat < rhData[0].length; ilat++) {
				    ix[1] = ilat;
				    for (int ilon = 0; ilon < rhData[0][0].length; ilon++) {
					ix[2] = ilon;
					rhData[itime][ilat][ilon] = rh.getInt(ix);
				    }
				}
			    }
			    System.out.println("rh[0][0][0]: " + rhData[0][0][0]);
			    //전체 rh 데이타 출력
			for(int i = 0; i<rhShape[0]; i++){
				for(int j = 0; j<rhShape[1]; j++){
					for(int k = 0; k<rhShape[2]; k++){
						System.out.println("<" + i+","+j+"," +k +">" + rhData[i][j][k]);
						
					}
				}
			}
			
	//////////////////////////////////////////////////////
			MultiArray rhMa = new ArrayMultiArray(rhData);
			for(IndexIterator rhIx = new IndexIterator(rhShape); rhIx.notDone(); rhIx.incr()){
				rhMa.setInt(rhIx.value(), rh.getInt(rhIx.value()));
			}
			System.out.println("rh[1][1][1]: " + rhData[1][1][1]);
			
			///////////////
			
			
			System.out.println("nc"+ nc.getDimensions());
			Variable temperature = nc.get("T");
			origin = new int[temperature.getRank()];
			extent = temperature.getLengths();
			MultiArray tMa = temperature.copyout(origin, extent);
			double t000 = tMa.getDouble(new int[]{0,0,0});
			System.out.println("T[0][0][0] : " + t000);
			
			float t001 = (float) tMa.getDouble(new int[]{0,0,1});
			System.out.println("T[0][0][1] : " + t001);
			
			int[] tLengths = tMa.getLengths();
			System.out.println(tLengths[0]);
			System.out.println(tLengths[1]+1);
			System.out.println(tLengths[2]+2);
			
			   double[] t1d = (double[]) tMa.toArray();
			    double t123 = t1d[(1 * tLengths[1] + 2) * tLengths[2] + 3];
			    System.out.println("T[1][2][3]: " + t123);

			    /* Use the public storage member of an ArrayMultiArray to
		               get at the array storage and then access the [0][1][2]
		               element by computing where it would be in a 1-dimensional
		               array. */
			    MultiArrayImpl tAma = (MultiArrayImpl) tMa;
			    double[] tD = (double[]) tAma.storage;
			    double t012 = tD[(0 * tLengths[1] + 1) * tLengths[2] + 2];
			    System.out.println("T[0][1][2]: " + t012);

			    System.out.println("read " + fileName + " successfully");

			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
