package netCDFProj;

import java.io.IOException;

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
			
		Variable lat = nc.get("lat");
			System.out.println(lat);
			int index = lat.getLengths()[0];
			System.out.println(index);
			
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
