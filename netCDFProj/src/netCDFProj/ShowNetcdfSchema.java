package netCDFProj;

import java.io.IOException;

import ucar.netcdf.Netcdf;
import ucar.netcdf.NetcdfFile;


/**
 * nc파일에 설정되어져 있는 데이터구조를 출력
 * @author bulhwi
 *
 */
public class ShowNetcdfSchema {
	static String fileName = "C:\\Users\\bulhwi\\Desktop\\sresa1b_ncar_ccsm3-example.nc";
	/*static String fileName = "C:\\Users\\bulhwi\\Desktop\\CreateNetCDf.nc"; */
	/*static String fileName = "C:\\Users\\bulhwi\\Desktop\\ncdf_sample\\data\\tvd.nc";*/
	public static void main(String[] args){
		try {
			Netcdf nc = new NetcdfFile(fileName, true);
			System.out.println(nc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
}
