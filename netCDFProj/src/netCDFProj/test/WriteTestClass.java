package netCDFProj.test;

import java.io.FileNotFoundException;
import java.io.IOException;

import ucar.multiarray.ArrayMultiArray;
import ucar.netcdf.Dimension;
import ucar.netcdf.Netcdf;
import ucar.netcdf.NetcdfFile;
import ucar.netcdf.Variable;
import ucar.netcdf.VariableIterator;

/**
 * 
 * 
 * 정해진 영역의 데이터를 이용해서 대기온도 값을 조회하여 해당 데이터를 수정하는 테스트클래스
 * @author bulhwi
 * 
 * 		https://www.unidata.ucar.edu/software/netcdf/java/examples/ -- 소스
 * 		https://www.unidata.ucar.edu/software/netcdf/examples/files.html - 파일
 * 		https://www.unidata.ucar.edu/downloads/netcdf/netcdf-java-4/index.jsp --jar
 * 
 *		sample file = sresa1b_ncar_ccsm3-example.nc
 *
 */
public class WriteTestClass {
	/*static String fileName = "C:\\Users\\bulhwi\\git\\NetCDF\\netCDFProj\\ncFile\\sresa1b_ncar_ccsm3-example.nc";*/
	static String fileName = "C:\\Users\\bulhwi\\Desktop\\fnl_2016_CFK_wrfout_d04\\wrfout_d04_2016-05-23_15_00_00";

	public static void main(String[] args) {

		WriteTestClass t = new WriteTestClass();
		Netcdf nc = t.ncLoad(fileName);
		
		try {
/////		
			System.out.println(nc);
			t.printNetCDFData(nc);
			int latIndex = t.getIndex(nc, "lat", 34.31877136230469);
			int latIndex2 = t.getIndex(nc, "lat", 35.71953201293945 );
			int latIndex3 = t.getIndex(nc, "lat", 37.12029266357422);
			
			int lonIndex = t.getIndex(nc, "lon", 125.15625); //89
			int lonIndex2 = t.getIndex(nc, "lon", 126.5625); //90
			int lonIndex3 = t.getIndex(nc, "lon", 127.9688); //91
			int lonIndex4 = t.getIndex(nc, "lon", 129.375); //92
			int lonIndex5 = t.getIndex(nc, "lon", 130.7812); //93
/////
			Variable lat = nc.get("lat");
			Variable lon = nc.get("lon");
			Variable time = nc.get("time"); // time 값은 현재 1개
			
			
			int leng = lat.getLengths()[0];
			int[] arr = new int[3];
			arr[0] = latIndex;
			arr[1] = latIndex2;
			arr[2] = latIndex3;
			
			int[] lats = new int[arr.length];//
			
			for(int i = 0; i< arr.length; i++){
				lats[i] = arr[i];
			}
			
			leng = lon.getLengths()[0];
			
			int[] arr2 = new int[5];
			
			arr2[0] = lonIndex;
			arr2[1] = lonIndex2;
			arr2[2] = lonIndex3;
			arr2[3] = lonIndex4;
			arr2[4] = lonIndex5;
			
			
			int[] lons = new int[arr2.length];
			for(int i =0; i<arr2.length; i++){
				lons[i] = arr2[i];
			}
			
			
			int[] arr3 = new int[1];
			
			int[] times = new int[arr3.length];
			for(int i=0; i<arr3.length; i++){
				times[i] = arr3[i];
			}
			
			
			System.out.println("****lats*********");
			for(int i = 0; i<lats.length; i++){
				System.out.println(lats[i]);
			}
			System.out.println("****lons*********");
			for(int i = 0; i<lons.length; i++){
				System.out.println(lons[i]);
			}
			System.out.println("****times*********");
			for(int i = 0; i< times.length; i++){
				System.out.println(times[i]);
			}
			System.out.println("*************");
			
			int[] searchIndex = new int[3];
			Variable tas = nc.get("tas");
			int[] tasDex = new int[tas.getRank()];
			double[][][] tatas = new double[times.length][lats.length][lons.length];
			for(int i=0; i<times.length; i++){
				searchIndex[0] = times[i];
				for(int j = 0; j < lats.length; j++){
					searchIndex[1] = lats[j];
					for(int k = 0; k<lons.length; k++){
						searchIndex[2] = lons[k];
						tatas[i][j][k] = tas.getDouble(searchIndex);
						System.out.println(tatas[i][j][k]);
					}
				}
			}
			 
			
			///////////////////////////////////
			
			
			
			float[][][] data = new float[1][3][5];
			System.out.println(data[0][2].length);
			
			for(int i=0; i<data.length; i++){
				for(int j = 0; j<data[i].length; j++){
					for(int k= 0; k<data[i][j].length; k++){
							data[i][j][k] = 101.20017f;
					}
				}
			}

			
			
			Dimension timeD = nc.getDimensions().get("time");
			
			
			//수정된 데이터 배열을 netCDF 파일에 해당 변수에 초기화한다.
			int timeDex = timeD.getLength();
			int[] writeArr = new int[3];
			writeArr[0] = timeDex -1 ;
			for(int i=0; i<lats.length; i++){
				writeArr[1] = lats[i];
				for(int j=0; j<lons.length; j++){
					writeArr[2] = lons[j];
					tas.set(writeArr, data[writeArr[0]][i][j]);
				}
			}
			
			tas.copyin(writeArr, new ArrayMultiArray(data));
			
			///
			System.out.println("수정 후  ");
			searchIndex = new int[3];
			
			tasDex = new int[tas.getRank()];
			tatas = new double[times.length][lats.length][lons.length];
			for(int i=0; i<times.length; i++){
				searchIndex[0] = times[i];
				for(int j = 0; j < lats.length; j++){
					searchIndex[1] = lats[j];
					for(int k = 0; k<lons.length; k++){
						searchIndex[2] = lons[k];
						tatas[i][j][k] = tas.getDouble(searchIndex);
						System.out.println(tatas[i][j][k]);
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * nc파일에 선언된 배열구조의 변수에서  특정값을 검색하여 그 값의 인덱스번호를 리턴
	 * @param nc - 로딩되어져 온 nc File 객체
	 * @param var - 검색하려는 값의 변수 명
	 * @param searchVar - 검색하려는 값
	 * @return
	 * @throws IOException
	 */
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
		WriteTestClass w = new WriteTestClass();
		
		
		if(var.equals("time")){
			for (int i = 0; i < vars.length; i++) {
				if (vars[i] == searchVar) {
					returnIndex = i;
				}
			}
		}else{
			for (int i = 0; i < vars.length; i++) {
				if (w.round(vars[i], 2) == w.round(searchVar, 2)) {
					returnIndex = i;
				}
			}
		}
		
		return returnIndex;
	}

	
	
	
	
	/**
	 * 특정 ncFile을 가져오기 위한 메서드
	 * @param fileName
	 * @return
	 */
	
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
	
	
	
	
	
	
	
	/**
	 * ncFile에 구조를 출력하기 위한 메서드
	 * @param nc
	 * @throws IOException
	 */
	public void printNetCDFData(Netcdf nc) throws IOException {

		VariableIterator vi = nc.iterator();

		while (vi.hasNext()) {
			Variable var = vi.next();
			/*System.out.println(var.getName() + " ....");*/
			var.copyout(new int[var.getRank()], var.getLengths());
		}
	}
	
	
	
	
	
	
	public double round(double v, int at){
		double aa = Math.pow(10, at);
		
		return ((int)(v*aa))/aa;
	}
	
	
	

}

