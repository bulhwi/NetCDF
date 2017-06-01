package netCDFProj.test;

import java.io.FileNotFoundException;
import java.io.IOException;

import ucar.multiarray.MultiArray;
import ucar.netcdf.Netcdf;
import ucar.netcdf.NetcdfFile;
import ucar.netcdf.Variable;
import ucar.netcdf.VariableIterator;


/*
 * lat = 34.31877, 35.71953, 37.12029
	lon = 125.1562, 126.5625, 127.9688, 129.375, 130.7812

	tas(time, lat, lon)
	
	*
	*/


public class test {
	static String fileName = "C:\\Users\\bulhwi\\Desktop\\sresa1b_ncar_ccsm3-example.nc";

	public static void main(String[] args) {

		test t = new test();
		Netcdf nc = t.ncLoad(fileName);

		try {
			/* t.printNetCDFData(nc); */

			int latIndex = t.getIndex(nc, "lat", -88.927734375);
			int lonIndex2 = t.getIndex(nc, "lon", 1.40625);
			int timeIndex3 = t.getIndex(nc, "time", 730135.5);
			System.out.println(latIndex);
			System.out.println(lonIndex2);
			System.out.println(timeIndex3);
			
			Variable lat = nc.get("lat");
			Variable lon = nc.get("lon");
			Variable time = nc.get("time");
			
			int leng = lat.getLengths()[0];
			int[] arr = new int[1];
			
			double[] lats = new double[leng];//
			
			for(int i = 0; i< leng; i++){
				arr[0] = i;
				lats[i] = lat.getDouble(arr);
			}
			
			leng = lon.getLengths()[0];
			double[] lons = new double[leng];
			for(int i =0; i<leng; i++){
				arr[0] = i;
				lons[i] = lon.getDouble(arr);
			}
			
			leng = time.getLengths()[0];
			double[] times = new double[leng];
			for(int i=0; i<leng; i++){
				arr[0] = i;
				times[i] = time.getDouble(arr);
			}
			
			int[] searchIndex = new int[3];
			
			Variable tas = nc.get("tas");
			System.out.println(tas.getRank());
			int[] tasIndex = tas.getLengths();
			int[] tasDex = new int[tas.getRank()];
			/*MultiArray tasMa = tas.copyout(tasIndex, tasDex);
			float tass = (float)tasMa.getDouble(new int[]{0,0,0});*/
			double[][][] tatas = new double[times.length][lats.length][lons.length];
			for(int i = 0; i<times.length; i++){
				searchIndex[0] = i;
				for(int j = 0; j<lats.length; j++){
					searchIndex[1] = j;
					
					for(int k=0; k<lons.length; k++){
						searchIndex[2] = k;
						tatas[i][j][k] = tas.getDouble(searchIndex);
						System.out.println(tatas[i][j][k] + "<" +times[i] +", "+lats[j] +", "+ lons[k]);
						/*System.out.println("<"+times[i] +", "+lats[i]+", "+lons[j]+">"+(float) tasMa.getDouble(new int[]{i,j,k}));*/
					}
				}
			}
			
			
			System.out.println(tatas[timeIndex3][latIndex][lonIndex2] + "asdasdas");
			
			System.out.println(tatas.length);
			System.out.println(tatas[0].length);
			System.out.println(tatas[0][0].length);
			
			// // 검색된 값을 수정
			
			
			
			
			
			
			
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
		}

		for (int i = 0; i < vars.length; i++) {
			if (vars[i] == searchVar) {
				returnIndex = i;
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

}
