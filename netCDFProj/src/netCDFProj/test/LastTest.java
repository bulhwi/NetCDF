package netCDFProj.test;

import java.io.IOException;

import ucar.multiarray.MultiArray;
import ucar.netcdf.Netcdf;
import ucar.netcdf.NetcdfFile;
import ucar.netcdf.Variable;

public class LastTest {
	public static void main(String[] args) {
		String fileName = "C:\\Users\\bul92\\Desktop\\sresa1b_ncar_ccsm3-example.nc";
		
		try {
			Netcdf nc = new NetcdfFile(fileName, true);
			System.out.println(nc);
			
			
			Variable lat = nc.get("lat");
			
			int[] latStart = new int[lat.getRank()];
			int[] latsLeng = lat.getLengths();
			MultiArray lats = lat.copyout(latStart, latsLeng);
		
			
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
