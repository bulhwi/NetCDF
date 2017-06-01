package netCDFProj.test;

import java.io.FileNotFoundException;
import java.io.IOException;

import ucar.netcdf.Netcdf;
import ucar.netcdf.NetcdfFile;
import ucar.netcdf.Variable;
import ucar.netcdf.VariableIterator;


/*
 * lat = 34.31877, 35.71953, 37.12029
	lon = 125.1562, 126.5625, 127.9688, 129.375, 130.7812
	tas(time, lat, lon)
	
	*/

public class WriteTestClass {
	static String fileName = "C:\\Users\\bul92\\Desktop\\sresa1b_ncar_ccsm3-example.nc";

	public static void main(String[] args) {

		WriteTestClass t = new WriteTestClass();
		Netcdf nc = t.ncLoad(fileName);

		try {
			/* t.printNetCDFData(nc); */

			int latIndex = t.getIndex(nc, "lat", 34.31877136230469);
			int latIndex2 = t.getIndex(nc, "lat", 35.71953201293945 );
			int latIndex3 = t.getIndex(nc, "lat", 37.12029266357422);
			
			int lonIndex = t.getIndex(nc, "lon", 125.15625); //89
			int lonIndex2 = t.getIndex(nc, "lon", 126.5625); //90
			int lonIndex3 = t.getIndex(nc, "lon", 127.9688); //91
			int lonIndex4 = t.getIndex(nc, "lon", 129.375); //92
			int lonIndex5 = t.getIndex(nc, "lon", 130.7812); //93
			
			int timeIndex = t.getIndex(nc, "time", 730135.5);
			
			System.out.println(lonIndex);
			System.out.println(lonIndex2);
			System.out.println(lonIndex3);
			System.out.println(lonIndex4);
			System.out.println(lonIndex5);
			
			Variable lat = nc.get("lat");
			Variable lon = nc.get("lon");
			Variable time = nc.get("time");
			
			
			int leng = lat.getLengths()[0];
			int[] arr = new int[3];
			arr[0] = latIndex;
			arr[1] = latIndex2;
			arr[2] = latIndex3;
			
			int[] lats = new int[arr.length];//
			
			for(int i = 0; i< arr.length; i++){
				lats[i] = arr[i];
			}
			
			leng = lon.getLengths()[0];
			int[] arr2 = new int[5];
			
			arr2[0] = lonIndex;
			arr2[1] = lonIndex2;
			arr2[2] = lonIndex3;
			arr2[3] = lonIndex4;
			arr2[4] = lonIndex5;
			
			
			int[] lons = new int[arr2.length];
			for(int i =0; i<arr2.length; i++){
				lons[i] = arr2[i];
			}
			
			
			int[] arr3 = new int[1];
			
			int[] times = new int[arr3.length];
			for(int i=0; i<arr3.length; i++){
				times[i] = arr3[i];
			}
			
			
			System.out.println("*************");
			for(int i = 0; i<lats.length; i++){
				System.out.println(lats[i]);
			}
			System.out.println("*************");
			for(int i = 0; i<lons.length; i++){
				System.out.println(lons[i]);
			}
			System.out.println("*************");
			
			for(int i = 0; i< times.length; i++){
				System.out.println(times[i]);
			}
			System.out.println("*************");
			
			int[] searchIndex = new int[3];
			
			Variable tas = nc.get("tas");
			int[] tasDex = new int[tas.getRank()];
			double[][][] tatas = new double[times.length][lats.length][lons.length];
			for(int i=0; i<times.length; i++){
				searchIndex[0] = times[i];
				for(int j = 0; j < lats.length; j++){
					searchIndex[1] = lats[j];
					for(int k = 0; k<lons.length; k++){
						searchIndex[2] = lons[k];
						tatas[i][j][k] = tas.getDouble(searchIndex);
						System.out.println(tatas[i][j][k]);
					}
					
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	public int getIndex(Netcdf nc, String var, double searchVar)
			throws IOException {
		Variable cdfVar = nc.get(var);
		int length = cdfVar.getLengths()[0];
		double[] vars = new double[length];
		int[] index = new int[1];
		int returnIndex = 0;
		for (int i = 0; i < vars.length; i++) {
			index[0] = i;
			vars[i] = cdfVar.getDouble(index);
			System.out.println(vars[i] + "lons.print");
		}
		WriteTestClass w = new WriteTestClass();
		
		
		if(var.equals("time")){
			for (int i = 0; i < vars.length; i++) {
				if (vars[i] == searchVar) {
					returnIndex = i;
				}
			}
		}else{
			for (int i = 0; i < vars.length; i++) {
				if (w.round(vars[i], 2) == w.round(searchVar, 2)) {
					returnIndex = i;
				}
			}
		}
		
		
		return returnIndex;
	}

	public Netcdf ncLoad(String fileName) {
		Netcdf nc = null;
		try {

			nc = new NetcdfFile(fileName, false);
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

	public void printNetCDFData(Netcdf nc) throws IOException {

		VariableIterator vi = nc.iterator();

		while (vi.hasNext()) {
			Variable var = vi.next();
			System.out.println(var.getName() + " ....");
			var.copyout(new int[var.getRank()], var.getLengths());
		}

	}
	
	
	
	
	public double round(double v, int at){
		double aa = Math.pow(10, at);
		
		return ((int)(v*aa))/aa;
	}
	
	
	

}
