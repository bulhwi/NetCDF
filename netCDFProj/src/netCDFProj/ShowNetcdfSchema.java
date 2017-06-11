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


	public static void main(String[] args){
		String fileName = "C:\\Users\\bul92\\Desktop\\NetCDF (2)\\wrfout_d04_2016-05-24_04_00_00";
		try {
			Netcdf nc = new NetcdfFile(fileName, true);
			System.out.println(nc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
}
