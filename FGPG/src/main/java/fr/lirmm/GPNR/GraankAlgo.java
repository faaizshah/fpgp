package fr.lirmm.GPNR;


//import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import org.neo4j.cypher.internal.compiler.v2_3.No;
import org.neo4j.driver.v1.AuthToken;
import org.neo4j.driver.v1.AuthTokens;

import au.com.bytecode.opencsv.CSVReader;

class FinalSet {
	double result;
	byte[][] matrix = null;
	Set<List<String>> str = new HashSet<>();

	@Override
	public String toString() {

		return "result: " + result + "\nstr:" + str;
	}
}

public class GraankAlgo {

	public void run(double support) {

		String uri = "bolt://127.0.0.1:7687";
		AuthToken auth = AuthTokens.basic("neo4j", "lirmm");

		RetrieveData retrieve = new RetrieveData(uri, auth);
		
		new File("./csv/").mkdirs();
		Arrays.stream(new File("./csv/").listFiles()).forEach(File::delete); //	clearFiles
		
		retrieve.getNodesWithRelationships(); // to retrieve data+structure and write to files per label
		

		List<String> files = new ArrayList<>();
		final File folder = new File("./csv/");
		int file_cnt = folder.listFiles().length;
		String csv_name;
	//	System.out.println("file_cnt: "+file_cnt);
		
		for (final File fileEntry : folder.listFiles()) {
			csv_name = fileEntry.getName();
			files.add(csv_name);
//			System.out.println("csv_name:"+csv_name);
		}
		
		System.out.println("Files-count:"+file_cnt);
		
	//	String s = "./fuzz/Fuzzy_Low.csv";
		
		for (int i = 0; i < file_cnt; i++) {
			List<String[]> dataSet = readCSVAll("./csv/" + files.get(i)); // parsing the CSV and storing to List object
			
	//		List<String[]> dataSet = readCSVAll(s); // parsing the CSV and storing to List object			
	//		List<String[]> dataSet = readCSVforFuzzy("./csv/" + files.get(i)); // parsing the CSV and storing to List object
			
			List<List<String>> sortedDS = sortData(dataSet);
			List<FinalSet> grInitGP = GraankInit(sortedDS, false);
			RankCorrelation(grInitGP, support);
			System.out.println("\n*************************************************** ");
		}

		retrieve.close(); // closing db connection

		
	}

	public void listFilesForFolder(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				System.out.println(fileEntry.getName());
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Set<List<String>> invert(Set<List<String>> temp) {

		String t = null;

		Set<List<String>> hinvtSet = new HashSet<>(temp);
		List<List<String>> invSet = new ArrayList<>(hinvtSet);

		List<String> linvertSet = new ArrayList<String>();

		for (int k = 0; k < invSet.size(); k++) {

			if (invSet.get(k).toString().contains("+")) {
				t = invSet.get(k).toString().replace("+", "-");
				linvertSet.add(t);
			}
			if (invSet.get(k).toString().contains("-")) {
				t = invSet.get(k).toString().replace("-", "+");
				linvertSet.add(t);
			}

		}

		Set invertSet = new HashSet<>(linvertSet);

		return invertSet;
	}

	public static List<String> invert1(List<List<String>> temp) {

		String t = null;

		List<List<String>> invSet = new ArrayList<>(temp);
		List<String> linvertSet = new ArrayList<String>();

		for (int k = 0; k < invSet.size(); k++) {

			if (invSet.get(k).toString().contains("+")) {
				t = invSet.get(k).toString().replace("+", "-");
				linvertSet.add(t);
			}
			if (invSet.get(k).toString().contains("-")) {
				t = invSet.get(k).toString().replace("-", "+");
				linvertSet.add(t);
			}

		}

		return linvertSet;
	}

	@SuppressWarnings("unused")
	public List<String[]> readCSVAll(String csvFile) {

		List<String[]> array = null;

		try {
		

			CSVReader csvReader = new CSVReader(new FileReader(csvFile));
			List<String[]> content = csvReader.readAll();
			
			
			System.out.println("");
			System.out.println("List of Properties for GP Extraction in file " + csvFile);
			System.out.println("--------------------------------------------------------------");
		
			
		
			for (int i = 0; i < content.get(0).length; i++) {
				System.out.println(i + 1 + " : " + (content.get(0)[i]));
			}

			for (Object object : content) {
				String[] row = (String[]) object;
			//	System.out.println(Arrays.deepToString(row));

			}

			array = content;

			csvReader.close();

		} catch (IOException e) {
			System.out.println("exception in readCSVAll()");
			e.printStackTrace();
		}

		return array;
	}
	public static List<List<String>> sortData(List<String[]> list) {

		List<String[]> arrayList = list;

		List<String> obj = new ArrayList<String>();
		List<List<String>> objList = new ArrayList<List<String>>();

		for (int i = 1; i <= arrayList.get(0).length; i++) {
			for (int j = 0; j < arrayList.size() - 1; j++) {
				obj.add((arrayList.get(j + 1)[i - 1]));
			}
			objList.add(obj);
		//	System.out.println(" ObjList: "+objList);
			obj = new ArrayList<String>();

		}

		return objList;
	}

	public List<FinalSet> GraankInit(List<List<String>> sortedDS, boolean eq) {

		// System.out.println(" In GraankInit");
		int len = sortedDS.get(0).size();

		System.out.println("\nNo of Records = " + len);

		List<FinalSet> listSR = new ArrayList<FinalSet>();

		// Starting to build CONCORDANCE MATRICES

		for (int i = 0; i < sortedDS.size(); i++) {
			List<String> npl = Arrays.asList(Integer.toString(i + 1) + '+');
			List<String> nm = Arrays.asList(Integer.toString(i + 1) + '-');

			FinalSet setP = new FinalSet();
			FinalSet setM = new FinalSet();

			setP.str.add(npl);
			setP.matrix = new byte[len][len];

			setM.str.add(nm);
			setM.matrix = new byte[len][len];

			for (int bi = 0; bi < len; bi++) {
				for (int ci = 0; ci < len; ci++) {
					setP.matrix[bi][ci] = 0;

				}
			}

			for (int bi = 0; bi < len; bi++) {
				for (int ci = 0; ci < len; ci++) {
					setM.matrix[bi][ci] = 0;
				}
			}

			for (int j = 0; j < sortedDS.get(0).size(); j++) {

				for (int k = 0; k < sortedDS.get(0).size(); k++) {

					if ((sortedDS.get(i).get(j).equals("NULL")) || (sortedDS.get(i).get(k).equals("NULL"))) {
						// System.out.println(" bt here");

						setP.matrix[k][j] = -1;
						setM.matrix[j][k] = -1;
						// System.out.println (" i: " +i+" j: " +j+" k: "+k );
					} else if (Float.parseFloat((String) sortedDS.get(i).get(j)) > Float
							.parseFloat((String) sortedDS.get(i).get(k))) {

						setP.matrix[k][j] = 1;
						setM.matrix[j][k] = 1;

					} else if (Float.parseFloat((String) sortedDS.get(i).get(j)) < Float
							.parseFloat((String) sortedDS.get(i).get(k))) {

						setM.matrix[j][k] = 0;
						setP.matrix[k][j] = 0;
					}

					else if (eq) {

						setM.matrix[j][k] = 1;
						setP.matrix[k][j] = 1;
						setP.matrix[j][k] = 1;
						setM.matrix[k][j] = 1;
					}
				}
			}

			listSR.add(setP);
			listSR.add(setM);

		}

		return listSR;
	}

	public void RankCorrelation(List<FinalSet> initSetList, double support) {

		List<FinalSet> apriorilistRS = new ArrayList<FinalSet>();
		List<FinalSet> finalListSR = new ArrayList<FinalSet>();

		double resBeforeApriori = 0;
		double result = 0;

		double summ = 0;
		System.out.println("\nSupport Threshold is  : " + support);

		double n = (initSetList.get(0).matrix.length);

		double nprime = 0;
		double sprime = 0;
		double card = 0;
		double flag = 0;
		double vdb = 0;
		double sum = 0;

		for (int listItem = 0; listItem < initSetList.size(); listItem++) {
			for (int row = 0; row < initSetList.get(listItem).matrix.length; row++) {
				for (int col = 0; col < initSetList.get(listItem).matrix.length; col++) {
//					System.out.print(initSetList.get(listItem).matrix[row][col]+ "	");

					if (initSetList.get(listItem).matrix[row][col] == 1) { // if (grInitMatrix.get(x)[y][z] == 1)
						sum = sum + 1;
					} else if (initSetList.get(listItem).matrix[row][col] == -1) {
						nprime = nprime + 1;
						card = card + 1;
					}
					if (card == initSetList.get(listItem).matrix.length) {
						flag = flag + 1;
					}
					// System.out.println(" flag = " + flag + " card = " + card);

				}
				card = 0;

				// this checks if all the items of a row are \bottom then increment the flag
				// which will be used to deduction of matrix size in order to get vdb

			}

			vdb = (initSetList.get(listItem).matrix.length - flag);
			resBeforeApriori = sum / (vdb * (vdb - 1) / 2);

			// System.out.println(" vdb = matrix.length = " +
			// initSetList.get(listItem).matrix.length);
			// System.out.println(" support = " + resBeforeApriori);

			sum = 0;
			nprime = 0;
			flag = 0;
			vdb = 0;

			if (resBeforeApriori <= support) {
				// System.out.println("Removing itemset indexed at <" + listItem
				// + "> because support compute is less than given support");
				initSetList.remove(listItem);

			}
			// System.out.println();

		}

		apriorilistRS = initSetList;

		// This while loop is for level-wise check for GP. First for 2 items and then
		// results for 2items taken to check for three items

		while (apriorilistRS.size() != 0) {

			apriorilistRS = Apriori(apriorilistRS, support, n);

			// System.out.println("\nGG ");
			// System.out.println("apriorilistRS count is: "+apriorilistRS.size());

			int i = 0;
			while ((i < apriorilistRS.size()) && (apriorilistRS != null)) {

				for (int row = 0; row < apriorilistRS.get(0).matrix.length; row++) {
					for (int col = 0; col < apriorilistRS.get(0).matrix.length; col++) {
						if (apriorilistRS.get(i).matrix[row][col] == 1) {
							summ = summ + 1;
						} else if (apriorilistRS.get(i).matrix[row][col] == -1) {
							sprime = sprime + 1;
							card = card + 1;
						}

					}
					// this checks if all the items of a row are \bottom then increment the flag
					// which will be used to deduction of matrix size in order to get vdb
					if (card == apriorilistRS.get(i).matrix.length) {
						flag = flag + 1;
					}

					card = 0;
				}

				vdb = (apriorilistRS.get(i).matrix.length - flag);
				result = summ / (vdb * (vdb - 1) / 2);

				summ = 0;
				sprime = 0;
				flag = 0;
				vdb = 0;

				// System.out.println("result in Graank : " + result);

				if (result < support) {
					apriorilistRS.remove(i);
				} else {
					finalListSR.add(apriorilistRS.get(i));
					i = i + 1;

				}

			}

		}
		// System.out.println("\n>>Final List of Candidates are: "+finalListSR.size());

		// System.out.println("------------------------------------------------");
		System.out.println("\nList of Patterns with Valid_Database:");
		System.out.println("------------------------------------------------");
		System.out.println("\nTotal Number of Patterns :  " + finalListSR.size() + " \n");

		for (int i = 0; i < finalListSR.size(); i++) {
			System.out.println((finalListSR.get(i).str) + " " + finalListSR.get(i).result);
		}

	}

	// @SuppressWarnings({ "unlikely-arg-type" })
	public List<FinalSet> Apriori(List<FinalSet> initSetList, double support, double n) {

		List<List<List<String>>> I = new ArrayList<>();

		List<Set<List<String>>> ckSet = new ArrayList<>();

		for (int x = 0; x < initSetList.size(); x++) {
			ckSet.add(x, initSetList.get(x).str);
		}

		// System.out.println("\nCk: "+ckSet);
		// System.out.println();

		Set<List<String>> temp = new HashSet<>();
		Set<List<String>> iSet = new HashSet<>();
		Set<List<String>> jSet = new HashSet<>();
		Set<List<String>> invtemp = new HashSet<>();
		List<String> invtemp1 = new ArrayList<>();

		Set<List<String>> temp2 = new HashSet<>();
		Set<List<String>> invtemp2 = new HashSet<>();
		List<String> invtemp21 = new ArrayList<>();

		int dimension = initSetList.get(0).matrix.length;

		double result = 0;
		double sum = 0;

		double sump = 0;
		double card = 0;
		double flag = 0;
		double vdb = 0;

		List<FinalSet> rsFinal = new ArrayList<FinalSet>();

		FinalSet setFinalResult = new FinalSet();
		setFinalResult.matrix = null;

		for (int i = 0; i < initSetList.size() - 1; i++) {
			for (int j = i + 1; j < initSetList.size(); j++) {

				iSet = ((initSetList.get(i).str));
				jSet = ((initSetList.get(j).str));

				temp.addAll(iSet);
				temp.addAll(jSet);

				// System.out.println("\n>>i : "+ i + " j: "+j);
				// System.out.println("temp: " + temp);

				List<List<String>> Sort = new ArrayList<>(temp);
				Sort.sort((l1, l2) -> l1.get(0).compareTo(l2.get(0)));

				invtemp1 = invert1(Sort);
				invtemp = invert(temp);

				Set<List<String>> tempCk = new HashSet<>(temp);
				List<List<String>> tempCkList = new ArrayList<>(tempCk);

				List<List<String>> tempCkListSort = new ArrayList<>(tempCkList);
				tempCkListSort.sort((l1, l2) -> l1.get(0).compareTo(l2.get(0)));

				tempCkList = tempCkListSort;

				Set<List<String>> invtempCk = new HashSet<>(invtemp);
				List<List<String>> invtempCkList = new ArrayList<>(invtempCk);

				Set<String> invtempCk1 = new HashSet<>(invtemp1);
				List<String> invtempCkList1 = new ArrayList<>(invtempCk1);

				List<String> invtempCkListSort = new ArrayList<>(invtempCkList1);
				invtempCkListSort.sort((l1, l2) -> l1.compareTo(l2));

				/*
				 * if(I.toString().contains(tempCkList.toString())){
				 * System.out.println("I containts temp"); } else if
				 * (I.toString().contains(invtempCkList.toString())){
				 * System.out.println("I containts invtemp"); }
				 */

				if (

				(temp.size() == ((initSetList.get(0).str.size()) + 1)) &&

						(!((I.toString() != null) && (I.toString().contains(tempCkList.toString())))) &&

						(!((I.toString() != null) && (I.toString().contains(invtempCkList.toString())))) &&

						(!((I.toString() != null) && (I.toString().contains(invtempCkListSort.toString()))))

				) {
					int test = 1;

					// System.out.println("e");

					Set<List<String>> convTemptoList = new HashSet<>(temp);
					List<List<String>> tempList = new ArrayList<>(convTemptoList);

					for (int k = 0; k < temp.size(); k++) {
						// System.out.println("f");
						List<String> sl = new ArrayList<>();
						sl = tempList.get(k);

						Set<List<String>> A = new HashSet<>();
						A.add(sl);
						Set<List<String>> diffSet = new HashSet<>();
						diffSet.addAll(temp);
						diffSet.removeAll(A);

						temp2 = diffSet;

						invtemp2 = invert(temp2);

						List<List<String>> temp2Sort = new ArrayList<>(temp2);
						temp2Sort.sort((l1, l2) -> l1.get(0).compareTo(l2.get(0)));

						invtemp21 = invert1(temp2Sort);

						if ((!(ckSet.contains(temp2))) && (!(ckSet.contains(invtemp2)))
								&& (!(ckSet.toString().contains(invtemp21.toString())))) {

							// System.out.println(">>>>>>>>>>anti-monotonicity check
							// successful>>>>>>>>>>>>>g");

							test = 0;
							break; // exit because of anti-monotonicity , b/c the subset is infrequent, hence
									// superset is also
						}

					}

					if (test == 1) {

						setFinalResult.matrix = new byte[dimension][dimension];

						for (int row = 0; row < initSetList.get(i).matrix.length; row++) {
							for (int col = 0; col < initSetList.get(i).matrix.length; col++) {

								if ((initSetList.get(i).matrix[row][col] == -1)
										|| (initSetList.get(j).matrix[row][col] == -1)) {
									setFinalResult.matrix[row][col] = -1;

								} else {

									setFinalResult.matrix[row][col] = (byte) (initSetList.get(i).matrix[row][col]
											* initSetList.get(j).matrix[row][col]);

								}

								if (setFinalResult.matrix[row][col] == 1) {
									sum = sum + 1;
								} else if (setFinalResult.matrix[row][col] == -1) {
									sump = sump + 1; // gives the sum of all the bit where missing values have occurred
														// card = card + 1;

								}

							}

							// flag variable is set if all the values in row are /bottom
							// the value of flag will be used to calculate the size of valid database

							if (card == initSetList.get(i).matrix.length) {
								flag = flag + 1;

							}

							card = 0;
						}

						// result = sum / value;

						vdb = (initSetList.get(i).matrix.length - flag);
						result = sum / (vdb * (vdb - 1) / 2);

						// System.out.println("SuppResult in Apri : " + result);
						// System.out.println("Pattern: "+temp+" : " + result);

						sum = 0;
						sump = 0;
						card = 0;
						flag = 0;
						vdb = 0;

						if (result > support) {
							FinalSet rs = new FinalSet();

							rs.result = result;
							rs.matrix = setFinalResult.matrix;

							Set<List<String>> targetSet = new HashSet<>(temp);
							rs.str = targetSet;

							// System.out.println("Considering Pattern in Apri:"+rs.str+"\n");
							rsFinal.add(rs);
						}

					}
					Set<List<String>> hinvtSetToList = new HashSet<>(temp);
					List<List<String>> invSet = new ArrayList<>(hinvtSetToList);

					List<List<String>> invSort = new ArrayList<>(invSet);
					invSort.sort((l1, l2) -> l1.get(0).compareTo(l2.get(0)));

					I.add(invSort);
					temp = new HashSet<>();
					invtemp = new HashSet<>();
					temp2.removeAll(temp);

				}

				temp.removeAll(iSet);
				temp.removeAll(jSet);

			}

		}

		return rsFinal;

	}	

}
