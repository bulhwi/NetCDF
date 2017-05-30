package netCDFProj;

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

/**
 * Simple example to create a new netCDF file corresponding to the following
 * CDL:
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
 *
 * @author: Russ Rew
 */
public class NetCDF_create {
	/*static String fileName = "C:\\Users\\bulhwi\\Desktop\\sresa1b_ncar_ccsm3-example.nc";*/
	 static String fileName = "C:\\Users\\bulhwi\\Desktop\\example.nc.nc"; // 만들려는 nc파일의 이름과 경로
	 
	public static void main(String[] args) {
		Dimension timeD = new UnlimitedDimension("time"); //UNLIMITED
		Dimension latD = new Dimension("lat", 3); //위도 1차원배열
		Dimension lonD = new Dimension("lon", 4); // 경도 1차원배열
		
		String rhPName = "rh";
		Class rhPType = int.class;
		Dimension[] rhPDims = {timeD, latD, lonD};
		Attribute rhPLongName = new Attribute("long_name", "relative humidity");
		Attribute rhPUnits = new Attribute("units", "percent");
		Attribute[] rhPAtts = {rhPLongName, rhPUnits};
		ProtoVariable rhP = new ProtoVariable(rhPName, rhPType, rhPDims, rhPAtts);
		
		ProtoVariable tP = new ProtoVariable(
			    "T",
			    double.class,
			    new Dimension[] {timeD, latD, lonD}, // shape (anonymous array)
			    new Attribute[] {	// attributes (anonymous array)
				new Attribute("long_name", "surface temperature"),
				new Attribute("units", "degC"),
				}
			    );
		
		ProtoVariable latP = new ProtoVariable(latD.getName(), float.class, latD);
		latP.putAttribute(new Attribute("units", "degrees_north"));
		ProtoVariable lonP = new ProtoVariable(lonD.getName(), float.class, lonD);
		lonP.putAttribute(new Attribute("units", "degrees_east"));
		
		ProtoVariable timeP = new ProtoVariable(timeD.getName(), float.class, timeD);
		timeP.putAttribute(new Attribute("units", "hours"));

		/* Create an Attribute to use as a global attribute */
		Attribute titleA = new Attribute("title", "Example Data");

		/* Create the Schema for the desired netCDF file.   */
		Schema schema = new Schema(
		    new ProtoVariable[] {rhP, tP, latP, lonP, timeP},
		    new Attribute[] {titleA}
		    );

		/* Here's another way to create the same schema,
	           incrementally. */
		Schema schema1 = new Schema(); // an empty schema
		schema1.put(rhP);
		schema1.put(tP);
		schema1.put(latP);
		schema1.put(lonP);
		schema1.put(timeP);
		schema1.putAttribute(titleA);

		try {
		    if (args.length > 0)
			fileName = args[0];
		    
		    /* Use the Schema to construct a new netCDF file */
		    NetcdfFile nc = new NetcdfFile(fileName,
						   true, // clobber an existing file
						   true, // prefill variable values
						   schema // metadata template
			                          );

		    /* Now get the variables and write some data into them.  We
		       show two ways to write data, first writing one value at a
		       time, then using a MultiArray.  */
		    int[][][] rhData = {
			{{ 1,  2,  3,  4}, { 5,  6,  7,  8}, { 9, 10, 11, 12}},
			{{21, 22, 23, 24}, {25, 26, 27, 28}, {29, 30, 31, 32}}
		    };
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
			{{  1, 2,  3,  4}, {2,  4,  6,  8}, {  3,  6,  9,   12}},
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
		} catch (java.io.IOException e) {
		    e.printStackTrace();
		}
		
	}
}
