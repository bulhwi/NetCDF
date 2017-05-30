package netCDFProj;

import java.io.IOException;

import ucar.multiarray.ArrayMultiArray;
import ucar.multiarray.IndexIterator;
import ucar.multiarray.MultiArray;
import ucar.netcdf.Attribute;
import ucar.netcdf.Dimension;
import ucar.netcdf.NetcdfFile;
import ucar.netcdf.ProtoVariable;
import ucar.netcdf.Schema;
import ucar.netcdf.UnlimitedDimension;
import ucar.netcdf.Variable;


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


public class NetCDF_CreatTest {
	static String fileName = "C:\\Users\\bulhwi\\Desktop\\CreateNetCDF.nc";
	public static void main(String[] args) {
		Dimension timeD = new UnlimitedDimension("time");
		Dimension latD = new Dimension("lat", 3);
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
		
		try {
			NetcdfFile nc = new NetcdfFile(
						fileName, //파일 이름
						true, // 
						true,
						schema
					);
			
			 /* Now get the variables and write some data into them.  We
		       show two ways to write data, first writing one value at a
		       time, then using a MultiArray.  */
		    int[][][] rhData = {{{ 1,  2,  3,  4}, { 5,  6,  7,  8}, { 9, 10, 11, 12}},	{{21, 22, 23, 24}, {25, 26, 27, 28}, {29, 30, 31, 32}} };
		    Variable rh = nc.get(rhP.getName()); // or nc.get("rh")

		    /* writing a single value is simple */
		    rh.setInt(new int[] {1,0,2}, rhData[1][0][2]);

		    /* writing all the values, one at a time, is similar */
		    int[] ix = new int[3];
		    for (int time = 0; time < rhData.length; time++) { 
			ix[0] = time;
			for (int lat = 0; lat < latD.getLength(); lat++) {
			    ix[1] = lat;
			    for (int lon = 0; lon < lonD.getLength(); lon++) {
				ix[2] = lon;
				rh.setInt(ix, rhData[time][lat][lon]);
			    }
			}
		    }

		    /* We could do this much more simply using an IndexIterator
	               from the multiarray package to set the values one at a
	               time.  Note that this approach doesn't need nested loops,
	               just a single loop no matter what the rank of the
	               variable. */
		    IndexIterator odom = new IndexIterator(rh.getLengths());
		    MultiArray rhMa = new ArrayMultiArray(rhData);
		    for ( ; odom.notDone(); odom.incr()) {
			rh.setInt(odom.value(), rhMa.getInt(odom.value()));
		    }

		    /* Here's a MultiArray approach to set the values of T all
	               at once. */
		    Variable T = nc.get(tP.getName()); // or nc.get("T")
		    double[][][] TData = {
			{{  1, 2,   3,  4}, {2,  4,  6,  8}, {  3,  6,  9,   12}},
			{{2.5, 5, 7.5, 10}, {5, 10, 15, 20}, {7.5, 15, 22.5, 30}}
		    };
		    MultiArray tMa = new ArrayMultiArray(TData);
		    int[] origin = {0, 0, 0};
		    T.copyin(origin, tMa);

		    /* Store the rest of variable values using MultiArrays */
		    Variable lat = nc.get(latP.getName());
		    Variable lon = nc.get(lonP.getName());
		    Variable time = nc.get(timeP.getName());
		    lat.copyin(origin, new ArrayMultiArray(new float[] 
							   {41, 40, 39}));
		    lon.copyin(origin, new ArrayMultiArray(new float[] 
							   {-109, -107, -105, -103}));
		    time.copyin(origin, new ArrayMultiArray(new float[] 
							    {6, 18}));

		    nc.close();
		    System.out.println("created " + fileName + " successfully");
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
