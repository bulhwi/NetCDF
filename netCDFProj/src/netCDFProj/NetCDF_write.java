package netCDFProj;

import java.io.IOException;

import ucar.multiarray.ArrayMultiArray;
import ucar.multiarray.IndexIterator;
import ucar.multiarray.MultiArray;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.netcdf.Dimension;
import ucar.netcdf.Netcdf;
import ucar.netcdf.NetcdfFile;
import ucar.netcdf.Variable;



/*
* <pre>
*  netcdf example {
*  dimensions:
*  	lat = 3 ;
*  	lon = 4 ;
*  	time = UNLIMITED ;
*  variables:
*  	int rh(time, lat, lon) ;
*              rh:long_name="relative humidity" ;
*  		rh:units = "percent" ;
*  	double T(time, lat, lon) ;
*              T:long_name="surface temperature" ;
*  		T:units = "degC" ;
*  	float lat(lat) ;
*  		lat:units = "degrees_north" ;
*  	float lon(lon) ;
*  		lon:units = "degrees_east" ;
*  	int time(time) ;
*  		time:units = "hours" ;
*  // global attributes:
*  		:title = "Example Data" ;
*  data:
*   rh =
*     1, 2, 3, 4,
*     5, 6, 7, 8,
*     9, 10, 11, 12,
*     21, 22, 23, 24,
*     25, 26, 27, 28,
*     29, 30, 31, 32 ;
*   T =
*     1, 2, 3, 4,
*     2, 4, 6, 8,
*     3, 6, 9, 12,
*     2.5, 5, 7.5, 10,
*     5, 10, 15, 20,
*     7.5, 15, 22.5, 30 ;
*   lat = 41, 40, 39 ;
*   lon = -109, -107, -105, -103 ;
*   time = 6, 18 ;
*  }
* </pre>
*/

public class NetCDF_write {
	
	public static void main(String[] args) {
		
		
	}
}
