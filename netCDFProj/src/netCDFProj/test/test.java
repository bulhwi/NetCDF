package netCDFProj.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import ucar.multiarray.MultiArray;
import ucar.netcdf.Netcdf;
import ucar.netcdf.NetcdfFile;
import ucar.netcdf.Variable;

public class test {
	static String fileName = "C:\\Users\\bulhwi\\Desktop\\sresa1b_ncar_ccsm3-example.nc";
	public static void main(String[]args){
	
		
		test t = new test();
		Netcdf nc = t.ncLoad(fileName);
		
		
		
		try{
		Variable lat = nc.get("lat");
		int latLength = lat.getLengths()[0];
		double[] lats = new double[latLength];
		int[] index = new int[1];
		System.out.println(latLength);
		for(int i = 0; i < latLength; i++){
			index[0] = i;
			lats[i] = lat.getDouble(index);
			System.out.println(lats[i]);
		}
		for(int i =0; i< lats.length; i++){
			if(lats[i] == 88.927734375){
				System.out.println(lats[i]+" ==== " + "index = " + i);
			}
		}
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}
	
	
	
	
	public Netcdf ncLoad(String fileName){
		Netcdf nc = null;
		try {
			
			nc = new NetcdfFile(fileName, true);
			System.out.println(nc);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nc;
		
	}
	
	
}
