package netCDFProj;

import java.io.IOException;

import ucar.multiarray.ArrayMultiArray;
import ucar.multiarray.IndexIterator;
import ucar.multiarray.MultiArray;
import ucar.netcdf.Attribute;
import ucar.netcdf.Dimension;
import ucar.netcdf.Netcdf;
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
	public static void main(String[] args) {
		
		
		/*String fileName = "생성될 파일의 저장 경로";*/
		String fileName = "C:\\Users\\bul92\\Desktop\\test_NetCDF.nc";
		Dimension timeD = new UnlimitedDimension("time");
		Dimension latD = new Dimension("lat", 3);
		Dimension lonD = new Dimension("lon", 4);
		
		
		//변수 생성1
		String rhPName = "rh";
		Class rhPType = int.class;
		Dimension[] rhPDims = {timeD, latD, lonD}; 
		Attribute rhPLongName = new Attribute("long_name", "test_relative humidity");
		Attribute rhPUnits = new Attribute("units","테스트_단위");
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
						new Attribute("long_name", "test_surface temperature"),
						new Attribute("units", "테스트_단위"),
					}
				);
		
		
		
		//변수생성 방법3 - 보통 1차원배열로 구성된 경도,위도에 대한 변수를 생성할때 사용한다.
		ProtoVariable latP = new ProtoVariable(latD.getName(), float.class, latD );
		latP.putAttribute(new Attribute("units", "test_degrees_north"));
		ProtoVariable lonP = new ProtoVariable(lonD.getName(), float.class, lonD);
		lonP.putAttribute(new Attribute("units", "test_degrees_east"));
		ProtoVariable timeP = new ProtoVariable(timeD.getName(), float.class, timeD);
		timeP.putAttribute(new Attribute("units", "test_hours"));
		
		
		//전역속성
		Attribute titleA = new Attribute("title", "Example Data");
		Attribute titleB = new Attribute("TEST", "테스트 파일입니다. ");
		

		Schema schema = new Schema(
			    new ProtoVariable[] {rhP, tP, latP, lonP, timeP},
			    new Attribute[] {titleA, titleB}
			    );
		
		try {
			NetcdfFile nc = new NetcdfFile(
						fileName, //파일 이름
						true, // 
						false,
						schema
					);
			
			
		    int[][][] rhData = { 
		    					{{ 1,  2,  3,  4}, { 5,  6,  7,  8}, { 9, 10, 11, 12}},	
		    					{{21, 22, 23, 24}, {25, 26, 27, 28}, {29, 30, 31, 32}} 
		    					};
		    Variable rh = nc.get(rhP.getName()); // or nc.get("rh")

		    rh.setInt(new int[] {1,0,2}, rhData[1][0][2]);

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

		  //	indexIterator를 이용한 방법
		    IndexIterator odom = new IndexIterator(rh.getLengths());
		    MultiArray rhMa = new ArrayMultiArray(rhData);
		    for ( ; odom.notDone(); odom.incr()) {
			rh.setInt(odom.value(), rhMa.getInt(odom.value()));
		    }

		
		    
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
		    
		    
		    
		    Variable testRh = nc.get("rh");
		    int[] index = new int[testRh.getRank()];
		    int[][][] testData = new int[testRh.getLengths()[0]][testRh.getLengths()[1]][testRh.getLengths()[2]];
		    
		    for(int i = 0; i< testData.length; i++){
		    	index[0] = i;
		    	for(int j = 0; j<testData[i].length; j++){
		    		index[1] = j;
		    		for(int k = 0; k<testData[i][j].length; k++){
		    			index[2] = k;
		    			testData[i][j][k] = testRh.getInt(index);
		    			System.out.print("<"+i+", "+j + ", " +k+">" + testData[i][j][k] + ",  ");
		    		}
		    		System.out.println();
		    	}
		    }
		    System.out.println(nc); // 파일의 데이터 구조 출력
		    nc.close();
		    System.out.println("created " + fileName + " successfully");
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
