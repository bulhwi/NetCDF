package netCDFProj;

import java.io.IOException;

import ucar.multiarray.MultiArray;
import ucar.netcdf.Attribute;
import ucar.netcdf.Netcdf;
import ucar.netcdf.NetcdfFile;
import ucar.netcdf.Variable;

public class NetCDF_ReadTest {
	static String fileName = "C:\\Users\\bulhwi\\Desktop\\CreateNetCDF.nc";

	public static void main(String[] args) {
		try {
			Netcdf nc = new NetcdfFile(fileName, true);
			Attribute titleA = nc.getAttribute("title");
			String title = titleA.getStringValue();
			System.out.println("title = " + title);

			Variable lat = nc.get("lat");
			int nlats = lat.getLengths()[0];
			System.out.println(nlats);
			double[] lats = new double[nlats];
			int[] index = new int[1];
			for (int i = 0; i < nlats; i++) {
				index[0] = i;
				lats[i] = lat.getDouble(index);
				System.out.println("lats[" + i + "]: " + lats[i]);
			}

			// 유닛속성을 읽어옴
			String latUnits = lat.getAttribute("units").getStringValue();
			System.out.println(latUnits);

			// 속성을 읽어올때 MultiArray를 이용 ex lon /// getRank 배열의 차원수를 리턴
			Variable lon = nc.get("lon");
			int[] origin = new int[lon.getRank()]; // 시작지점
			int[] extent = lon.getLengths(); // 길이
			MultiArray lonMa = lon.copyout(origin, extent);
			
			
			

			// read time
			Variable time = nc.get("time");
			origin = new int[time.getRank()];
			extent = time.getLengths();
			MultiArray timeMa = time.copyout(origin, extent);

			// read data
			Variable rh = nc.get("rh");
			int[] rhShape = rh.getLengths();
			System.out.println(rhShape[0] + ", " + rhShape[1] + ", "
					+ rhShape[2]);
			int[][][] rhData = new int[rhShape[0]][rhShape[1]][rhShape[2]];

			int[] ix = new int[3];
			for (int itime = 0; itime < rhData.length; itime++) {
				ix[0] = itime;
				for (int ilat = 0; ilat < rhData[itime].length; ilat++) {
					ix[1] = ilat;
					for (int ilon = 0; ilon < rhData[itime][ilat].length; ilon++) {
						ix[2] = ilon;
						rhData[itime][ilat][ilon] = rh.getInt(new int[] {
								itime, ilat, ilon });
						System.out.println(rhData[itime][ilat][ilon] + "=="
								+ itime + ", " + ilat + ", " + ilon);
					}
				}
			}
			
		
			
			
			System.out.println(rhData[1][0][2]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
