package netCDFProj.test;

import java.io.IOException;

import ucar.netcdf.Netcdf;
import ucar.netcdf.NetcdfFile;
import ucar.netcdf.Variable;





/*Variable times = nc.get("Times"); //char type : 2016-05-23_17:00:00*/
public class TestClass {
	public static void main(String[] args) {
		String fileName = "C:\\Users\\bulhwi\\Desktop\\fnl_2016_CFK_wrfout_d04\\wrfout_d04_2016-05-23_15_00_00";
		double[] latList = {37.40238952636719, 37.402366638183594, 37.40234375, 37.402320861816406, 37.40229034423828};
		double[] lonList = {127.90786743164062,	127.91726684570312,	127.92666625976562, 127.93609619140625,	127.94549560546875};
		Netcdf nc = null;
		int[] jValue = new int[latList.length];
		int[] kValue = new int[latList.length];
		try {
			nc = new NetcdfFile(fileName, true);
			System.out.println(nc);
			Variable xLat = nc.get("XLAT");
			double[][][] lats = new double[xLat.getLengths()[0]][xLat.getLengths()[1]][xLat.getLengths()[2]];
			int[] index = new int [3];
			for(int i = 0; i<lats.length; i++){
				index[0] = i;
				for(int j = 0; j < lats[i].length; j++){
					index[1] = j;
					for(int k =0; k<lats[i][j].length; k++){
						index[2] = k;
						lats[i][j][k] = xLat.getDouble(index);
						System.out.println(lats[i][j][k]);
						for(int z = 0; z < latList.length; z++){
							if(lats[i][j][k]==latList[z]){
								jValue[z] = j;
								kValue[z] = k;
							}
						}
					}
				}
			}
			
			Variable psfc = nc.get("PSFC");
			double[][][] psfcArr = new double[psfc.getLengths()[0]][psfc.getLengths()[1]][psfc.getLengths()[2]];
			int[] testIndex = new int[3];
			for(int i = 0; i < psfcArr.length; i++ ){
				testIndex[0] = i;
				for(int j = 0; j < jValue.length; j++){
					testIndex[1] = jValue[j];
					for(int k = 0; k<kValue.length;  k++){
						testIndex[2] = kValue[k];
						psfcArr[i][j][k] = psfc.getDouble(testIndex);
						System.out.println(psfcArr[i][j][k]);
					}
				}
			}
			
			Variable xLong = nc.get("XLONG");
			double[][][] lons = new double[xLong.getLengths()[0]][xLong.getLengths()[1]][xLong.getLengths()[2]];
			int[] xIndex = new int[3];
			for(int i=0; i<lons.length; i++){
				xIndex[0] = i;
				for(int j = 0; j<lons[i].length; j++){
					xIndex[1] = j;
					for(int k=0; k<lons[i][j].length; k++){
						xIndex[2] = k;
						lons[i][j][k] = xLong.getDouble(xIndex);
						for(int z = 0; z < lonList.length; z++){
							if(lons[i][j][k]==lonList[z]){
								
								jValue[z] = j;
								kValue[z] = k;
							}
						}
					}
				}
			}
			
			Variable psfc2 = nc.get("PSFC");
			double[][][] psfcArr2 = new double[psfc2.getLengths()[0]][psfc2.getLengths()[1]][psfc2.getLengths()[2]];
			int[] testIndex2 = new int[3];
			for(int i = 0; i < psfcArr2.length; i++ ){
				testIndex2[0] = i;
				for(int j = 0; j < jValue.length; j++){
					testIndex2[1] = jValue[j];
					for(int k = 0; k<kValue.length;  k++){
						testIndex2[2] = kValue[k];
						psfcArr2[i][j][k] = psfc2.getDouble(testIndex);
						System.out.println(psfcArr2[i][j][k]);
					}
				}
			}
			
			
			
			///////////////////////////
			
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void getIndex(Netcdf nc, String var, double searchData) throws IOException{
		Variable cdfVar = nc.get(var);
		double[][][] varArr = new double[cdfVar.getLengths()[0]][cdfVar.getLengths()[1]][cdfVar.getLengths()[2]];
		int[] xIndex = new int[3];
		for(int i = 0; i < varArr.length; i++ ){
			xIndex[0] = i;
			for(int j = 0; j < varArr[i].length; j++){
				xIndex[1] = j;
				for(int k = 0; k<varArr[i][j].length; k++){
					xIndex[2] = k;
					varArr[i][j][k] = cdfVar.getDouble(xIndex);
						
						if(varArr[i][j][k] == searchData){
							System.out.println();
						}
					
				}
			}
		}
		
	}
	
		
	
	
}
