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

	public static void main(String[] args) {

		test t = new test();
		Netcdf nc = t.ncLoad(fileName);

		try {
			int index = t.getIndex(nc, "lat", -88.927734375);
			int index2 = t.getIndex(nc, "lon", 1.40625);
			System.out.println(index);
			System.out.println(index2);

			Variable area = nc.get("area");
			int[] searchIndex = new int[area.getRank()];
			int[] areaLength = area.getLengths();
			double areaData = 0.0;
			for (int i = 0; i < areaLength.length; i++) {
				searchIndex[0] = index;
				searchIndex[1] = index2;
				areaData = area.getDouble(searchIndex);
			}

		

			// ///////////////

			Variable lat = nc.get("lat");
			Variable lon = nc.get("lon");
			int leng = lat.getLengths()[0];
			int[] testIndex = new int[1];
			double[] lats = new double[leng];

			for (int i = 0; i < leng; i++) {
				testIndex[0] = i;
				lats[i] = lat.getDouble(testIndex);
			}

			leng = lon.getLengths()[0];
			double[] lons = new double[leng];

			for (int i = 0; i < leng; i++) {
				testIndex[0] = i;
				lons[i] = lon.getDouble(testIndex);
			}
			int[] sdex = new int[2];
			double[][] areas = new double[lats.length][lons.length];
			for(int i = 0; i<lats.length; i++){
				sdex[0] = i;
				for(int j = 0; j < lons.length; j++){
					sdex[1] = j;
					areas[i][j] = area.getDouble(sdex);
				}
			}
			if(areas[0][1] == areaData){
				System.out.println("areas[0][1] = " + areas[0][1]);
				System.out.println("areaData = " + areaData);
			}
			////////////////////
		

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
