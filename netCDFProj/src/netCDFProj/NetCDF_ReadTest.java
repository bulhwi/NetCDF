package netCDFProj;

import ucar.netcdf.Attribute;
import ucar.netcdf.Dimension;
import ucar.netcdf.ProtoVariable;
import ucar.netcdf.Schema;
import ucar.netcdf.UnlimitedDimension;


/*
netcdf example1 {
dimensions:
	lat = 2 ;
	lon = 3 ;
	time = UNLIMITED ;
variables:
	int rh(time, lat, lon) ;
            T:long_name="relative humidity" ;
		T:units = "percent" ;
	double T(time, lat, lon) ;
            T:long_name="surface temperature" ;
		T:units = "degC" ;
	float lat(lat) ;
		lat:units = "degrees_north" ;
	float lon(lon) ;
		lon:units = "degrees_east" ;
	int time(time) ;
		time:units = "hours" ;
// global attributes:
		:title = "Example Data" ;
data:
}
*/


public class NetCDF_ReadTest {
	static String fileName = "C:\\Users\\bulhwi\\Desktop\\CreateNetCDF.nc";
	public static void main(String[] args) {
		Dimension timeD = new UnlimitedDimension("time");
		Dimension latD = new Dimension("lat", 2);
		Dimension lonD = new Dimension("lon", 3);
		
		
		//변수 생성1
		String rhPName = "rh";
		Class rhPType = int.class;
		Dimension[] rhPDims = {timeD, latD, lonD}; 
		Attribute rhPLongName = new Attribute("long_name", "relative humidity");
		Attribute rhPUnits = new Attribute("units","percent");
		Attribute[] rhPAtts = {rhPLongName, rhPUnits};
		ProtoVariable rhP = new ProtoVariable(
					rhPName, 
					rhPType, 
					rhPDims, 
					rhPAtts
				);
		
		
		//변수생성의 방법2
		ProtoVariable tP = new ProtoVariable(
					"T",
					double.class,
					new Dimension[]{timeD, latD, lonD},
					new Attribute[]{
						new Attribute("long_name", "surface temperature"),
						new Attribute("units", "degC"),
					}
				
				);
		
		//변수생성 방법3 - 보통 1차원배열로 구성된 경도,위도에 대한 변수를 생성할때 사용한다.
		ProtoVariable latP = new ProtoVariable(latD.getName(), float.class, latD );
		latP.putAttribute(new Attribute("units", "degrees_north"));
		ProtoVariable lonP = new ProtoVariable(lonD.getName(), float.class, lonD);
		lonP.putAttribute(new Attribute("units", "degrees_east"));
		ProtoVariable timeP = new ProtoVariable(timeD.getName(), float.class, timeD);
		timeP.putAttribute(new Attribute("units", "hours"));
		
		
		//전역속성
		Attribute titleA = new Attribute("title", "Example Data");

		Schema schema = new Schema(
			    new ProtoVariable[] {rhP, tP, latP, lonP, timeP},
			    new Attribute[] {titleA}
			    );
		
		
	}
}
