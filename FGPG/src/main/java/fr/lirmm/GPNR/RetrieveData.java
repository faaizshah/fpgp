package fr.lirmm.GPNR;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.neo4j.driver.v1.AuthToken;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.Value;

import au.com.bytecode.opencsv.CSVReader;

public class RetrieveData implements AutoCloseable {

	private final Driver driver;

	public RetrieveData(String uri, AuthToken auth) {
		driver = GraphDatabase.driver(uri, auth);
	}

	public void close() {
		driver.close();
	}

	// Get the Nodes and Property Keys
	public List<Record> getNodes() {

		Session session = driver.session();

		StatementResult result = session
				.run("MATCH (n)\r\n" + "RETURN head(labels(n)) as label, keys(n) as properties, count(*) as count\r\n"
						+ "ORDER BY count DESC");

		List<Record> records = result.list();

		return records;

	}

	public void getNodesWithRelationships() {
		
	//	GraankAlgo.clearFiles();

		ObjectHashMap objHashMap = new ObjectHashMap();
		List<Record> labels = getNodeLabels();
		
		try {
			for (int i = 0; i < labels.size(); i++) {

				Record recLabel = labels.get(i);
				String srcLabel = recLabel.get("label").asString();
				// Value labelNodeCount = recLabel.get("nodeCount");
				// System.out.println(srcLabel+" "+labelCount);

				List<Record> perLabelRelationships = getPerLabelRelationshipTypes(srcLabel);
				List<String> sortedkeyValueofLabel = objHashMap.getSortablePropertiesList(srcLabel);
				List<String> relListVal = new ArrayList<>();
				
			
		
				for (int j = 0; j < perLabelRelationships.size(); j++) {

					Record recRelType = perLabelRelationships.get(j);
					String relType = recRelType.get("rel").asString();
					relListVal.add(relType);

				}
				
				Path path = Paths.get("./csv/" + srcLabel +relListVal+ ".csv");
				BufferedWriter writer = Files.newBufferedWriter(path);
				writer.write(sortedkeyValueofLabel.toString() + "," + relListVal);
				writer.write("\n");

				List<Record> getNRSS = getNodeRelSummStructure(srcLabel, relListVal);
				
		//		System.out.println("Label: " + srcLabel + ", nodeCount: " + labelNodeCount);
		//		System.out.println(sortedkeyValueofLabel + " \t" + relListVal); // Properties(Attributes)_List_src_node

				for (int k = 0; k < getNRSS.size(); k++) {

					List<Value> row = getNRSS.get(k).values();

					for (int l = 0; l < sortedkeyValueofLabel.size(); l++) {
						Value rowVal = row.get(0);
						String propName = sortedkeyValueofLabel.get(l).toString();
		//				System.out.print(rowVal.get(propName) + " \t");
						writer.write(rowVal.get(propName).toString());
						writer.write(",");

					}
					if (relListVal.size() == 1) {
		//				System.out.print("\t" + row.get(1));
						writer.write(row.get(1).toString() + ",");
						writer.write("\n");

					} else if (relListVal.size() == 2) {
		//				System.out.print("\t" + row.get(1) + "\t" + row.get(2));
						writer.write(row.get(1).toString() + "," + row.get(2).toString() + ",");
						writer.write("\n");
					}
					if (relListVal.size() == 3) {
		//				System.out.print("\t" + row.get(1) + "\t" + row.get(2) + "\t" + row.get(3));
						writer.write(row.get(1).toString() + "," + row.get(2).toString() + "," + row.get(3).toString()
								+ ",");
						writer.write("\n");
					}

		//			System.out.println();

					// System.out.println(ssRec);
				}
				
				writer.close();
				//System.out.println( "PATH for file"+i+" ="+path);
			//	String fz_csv = srcLabel +relListVal+ ".csv";
			//	System.out.println("Fz_CSV ="+fz_csv);
			//	readCSVforFuzzy(fz_csv);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<String> files = new ArrayList<>();
		final File folder = new File("./csv/");
		int file_cnt = folder.listFiles().length;
		String csv_name;
	//	System.out.println("file_cnt: "+file_cnt);
		
		for (final File fileEntry : folder.listFiles()) {
			csv_name = fileEntry.getName();
			files.add(csv_name);
		//	System.out.println("csv_name IN RETR:"+csv_name);
		}
		
	//	System.out.println("Files-count IN RETR:"+file_cnt);
		
	//	new File("./withoutfuzzCSV/").mkdirs();
	//	Arrays.stream(new File("./fuzzCSV/").listFiles()).forEach(File::delete); //	clearFiles
		
		
		
		System.out.println("\n");
		for (int i = 0; i < file_cnt; i++) {
			
			readCSVforFuzzy("./csv/" + files.get(i));

			
		}
			
	}//end of function
	
	// this function is called in getNodeRecords()
	public List<Value> getKeyValues(String objName) {
		Session session = driver.session();
		StatementResult result = session.run("MATCH (n:" + objName + ") RETURN n");

		List<Value> listOfValues = new ArrayList<Value>();

		while (result.hasNext()) {
			Record record = result.next();

			for (Value val : record.values()) {
				listOfValues.add(val);
			}

		}

		return listOfValues;

	} // getKeyValues ends

	// This method is called in method getNodesWithRelationships()
	public List<Record> getNodeLabels() {
		Session session = driver.session();

		StatementResult result1 = session.run("MATCH (n) RETURN count(n) as totalnodeCount");
		List<Record> records1 = result1.list();
		for (Record record1 : records1) {
			System.out.println("Total Number of Nodes: " +record1.get("totalnodeCount"));

		}
		
		StatementResult result2 = session.run("MATCH ()-[r]->()  RETURN count(*) as totalRelCount");
		List<Record> records2 = result2.list();
		for (Record record2 : records2) {
			System.out.println("Total Number of Relationships: "+record2.get("totalRelCount"));

		}
	
		StatementResult result = session.run("MATCH (n) RETURN head(labels(n)) as label, count(*) as nodeCount");

		List<Record> records = result.list();

		System.out.println("\n***Node Labels with nodes Count***\n");

		for (Record record : records) {
			System.out.println(record.get("label").asString() + " : " + record.get("nodeCount"));

		}
		
		StatementResult result3 = session.run("MATCH ()-[r]->() RETURN type(r) as rel, count(*) as relCount");
		List<Record> records3 = result3.list();

		System.out.println("\n***Relationship Types with relationships Count***\n");

		for (Record record3 : records3) {
			System.out.println(record3.get("rel").asString() + " : " + record3.get("relCount"));

		}
		
		System.out.println();

		return records;

	}

	// This method is called in method getNodesWithRelationships()
	public List<Record> getPerLabelRelationshipTypes(String srcNodeLabelName) {
		Session session = driver.session();
		StatementResult result = session
				.run("MATCH (n:" + srcNodeLabelName + ")-[r]->() RETURN type(r) as rel, count(*) as relCount");

		List<Record> records = result.list();

		return records;
	}

	// This method is called in method getNodesWithRelationships()
	public List<Record> getNodeRelSummStructure(String srcNodeLabelName, List<String> relTypeList) {

		Session session = driver.session();
		List<Record> records = new ArrayList<>();
		int i = 0;

		if (relTypeList.size() == 1) {
			StatementResult result = session.run("MATCH (n:" + srcNodeLabelName + ") RETURN n as node, size ((n)-[:"
					+ relTypeList.get(i) + "]->()) as rel1");
			records = result.list();

		} else if (relTypeList.size() == 2) {
			StatementResult result = session.run("MATCH (n:" + srcNodeLabelName + ") RETURN n as node, size ((n)-[:"
					+ relTypeList.get(i) + "]->()) as rel1, size ((n)-[:" + relTypeList.get(i + 1) + "]->()) as rel2");
			records = result.list();
		} else if (relTypeList.size() == 3) {
			StatementResult result = session.run("MATCH (n:" + srcNodeLabelName + ") RETURN n as node, size ((n)-[:"
					+ relTypeList.get(i) + "]->()) as rel1, size ((n)-[:" + relTypeList.get(i + 1)
					+ "]->()) as rel2, size ((n)-[:" + relTypeList.get(i + 2) + "]->()) as rel3");
			records = result.list();
		}

		return records;

	}
	public static long countN(Transaction tx) {
		String COUNT_NODES = ("MATCH (a) RETURN count(a)");
		StatementResult result = tx.run(COUNT_NODES);
		return result.single().get(0).asLong();

	}

	public static long countR(Transaction tx) {
		String COUNT_REL = ("MATCH ()-[r]->()  RETURN count(*)");
		StatementResult result = tx.run(COUNT_REL);
		return result.single().get(0).asLong();

	}

	public static List<Record> getDB(Transaction tx) {
		String GET_DB = ("MATCH (n)\r\n"
				+ "RETURN head(labels(n)) as label, keys(n) as properties, count(*) as count\r\n"
				+ "ORDER BY count DESC");

		StatementResult result = tx.run(GET_DB);
		List<Record> records = result.list();

		return records;

	}

	//********************************************
	/////********************Reading CSV for adding Fuzzy Modalities*********************///// 
	public static void readCSVforFuzzy(String csvFile) {
		

		List<String[]> array = null;

		try {
			CSVReader csvReader = new CSVReader(new FileReader(csvFile));
			List<String[]> content = csvReader.readAll();
			String colName, colLow, colHigh;
			int colSize = (content.get(0).length) + (content.get(0).length*2);

		/*	System.out.println("");
			System.out.println("List of Properties for Fuzzy GP Extraction in file " + csvFile);
			System.out.println("--------------------------------------------------------------");*/
			System.out.println("No of columns =" + content.get(0).length);			
			System.out.println("Updated No of columns ="+ colSize);
			System.out.println("csv file name ="+ csvFile);
			
			int j=0;
		//	Path path = Paths.get("./fuzzCSV/" +csvFile);
	
		//	Path temp = Files.copy(Paths.get(csvFile), Paths.get(csvFile));
			
			Path temp = Files.copy(Paths.get(csvFile), Paths.get(csvFile));
			
	//		Path path = Paths.get("/home/faaiz/myProject/fgpg/FGPG/fuzz/new.csv");
			
	//		System.out.println("temp = "+temp);
			
	//		Path path = Paths.get(csvFile+"Fuzz-Updated");		
	//		BufferedWriter writer = Files.newBufferedWriter(path);
			
			BufferedWriter writer = Files.newBufferedWriter(temp);
			
	//		BufferedWriter writer1 = Files.newBufferedWriter(path);
			
			List<String> attributesList = new ArrayList<>();
	//		List<String> fuzzAttributesList = new ArrayList<>();
			
			for (int i = 0; i < content.get(0).length; i++) {
				
				if (i < content.get(0).length-1) {
					
					colName = (content.get(0)[i]);
					//System.out.println(i + 1 + " : " + (content.get(0)[i]));
					colLow  = colName +"_Low";
					colHigh  = colName +"_High";
									
				//	System.out.println(j+1+" : " +colName);
				//	System.out.println(j+2 + " : " +colLow);
				//	System.out.println(j+3 + " : " +colHigh);
					
					j=j+3;
							
					
					writer.write( colName.toString()+ "," + colLow.toString()+ "," +colHigh.toString()+",");
					
				//	writer1.write(colLow.toString()+","); // writing fuzzy dimension
				//	writer1.write(colHigh.toString()+","); // writing fuzzy dimension
					
					
					attributesList.add(colName);
					attributesList.add(colLow);
					attributesList.add(colHigh);
					
				//	fuzzAttributesList.add(colHigh);
				//	fuzzAttributesList.add(colLow);
					
					
				}else if (i == content.get(0).length-1)  {

					colName = (content.get(0)[i]);
					//System.out.println(i + 1 + " : " + (content.get(0)[i]));
					colLow  = colName +"_Low";
					colHigh  = colName +"_High";
									
				//	System.out.println(j+1+" : " +colName);
				//	System.out.println(j+2 + " : " +colLow);
				//	System.out.println(j+3 + " : " +colHigh);
					j=j+3;
							
					
					writer.write( colName.toString()+ "," + colLow.toString()+ "," +colHigh.toString());
					
				//	writer1.write(colLow.toString()); // writing fuzzy dimension				
				//	writer1.write(colHigh.toString()); // writing fuzzy dimension
					
					attributesList.add(colName);
					attributesList.add(colLow);
					attributesList.add(colHigh);
					
				//	fuzzAttributesList.add(colLow);
				//	fuzzAttributesList.add(colHigh);
					
					
				}
			
			}
		//	System.out.println();
		//	System.out.println(attributesList);
		//	System.out.println();
			
			
/*			for (Object object : content) {
				String[] row = (String[]) object;
			//	System.out.println(Arrays.deepToString(row));

			}
*/
			array = content;				
			writer.write("\n");
	//		writer1.write("\n");

	//		List<String[]> arrayList = array;
			List<String> rowItem = new ArrayList<>();
			
		//	System.out.println();
		//	System.out.println( "tuples size : " + (array.size()-1));
		//	System.out.println( "Actual Attributes : " +arrayList.get(0).length); 
		//	System.out.println( "Attributes With Fuzzy  : " +attributesList.size());
		//	System.out.println();
			
		//	System.out.println( "Arrayyy : " + (array.get(1)[0]));
			
			// array.size() = the number of rows 
			// arrayList.get(0).length = number of attributes 
			
			double m, min, max, l,u;
		
			min=1;
			max = 100;
			m = (max-min)/2;
			
						
			l = m-(min*0.2);
			u =  m+(max*0.2);
			
			System.out.println();
			System.out.println("m = "+m+ " l = "+l+" u = "+u);
			System.out.println();
			String x;
			
			for (int row=1; row < array.size();row++) {				
				for (int col=0;col< array.get(0).length;col++) {
					
					rowItem.add(array.get(row)[col]);
					writer.write(array.get(row)[col]+","); // retrieve and write the attribute values like for (Age =30_
					
					x= array.get(row)[col];
				//	System.out.println("x = "+x);
					
					String fz_hg;
					String fz_lw;
					
					
					if (x.equals("NULL")) { // checking if the attribute value is NULL
					
						fz_lw ="NULL";
						fz_hg ="NULL";

						rowItem.add(fz_lw);
						writer.write(fz_lw+",");
						
												
						rowItem.add(fz_hg);
						writer.write(fz_hg+",");
						
						
					//	writer1.write(fz_lw+",");
					//	writer1.write(fz_hg+",");

					 // Applying the membership function (Low and High )for the respective value eg Age =20 	
					}else if (Float.parseFloat((String) array.get(row)[col]) < l) {
						fz_lw ="0";
						fz_hg ="1";
						
						rowItem.add(fz_lw);
						writer.write(fz_lw+",");
											
						

						rowItem.add(fz_hg);
						writer.write(fz_hg+",");
						
					//	writer1.write(fz_lw+",");
					//	writer1.write(fz_hg+",");
						
					} else if (Float.parseFloat((String) array.get(row)[col]) > u) {
						fz_lw ="0";
						fz_hg ="1";
						

						rowItem.add(fz_lw);
						writer.write(fz_lw+",");
						
						

						rowItem.add(fz_hg);
						writer.write(fz_hg+",");
						
					//	writer1.write(fz_lw+",");	
					//	writer1.write(fz_hg+",");
						
					} else if (Float.parseFloat((String) array.get(row)[col])<= u && Float.parseFloat((String) array.get(row)[col]) >= l) {
						
						double fl,fh;
						
						fl= (u-Float.parseFloat((String) array.get(row)[col]))/(u-l);
						fh = (Float.parseFloat((String) array.get(row)[col])-l)/(u-l);
						
						fz_lw =Double.toString(fl); 
						fz_hg =Double.toString(fh);

						rowItem.add(fz_lw);
						writer.write(fz_lw+",");			
											

						rowItem.add(fz_hg);
						writer.write(fz_hg+",");
						
					//	writer1.write(fz_lw+",");
					//	writer1.write(fz_hg+",");
					}					
					
				
				}
				
			
			//	System.out.println("row"+row+"=" +rowItem.toString());
			
				//System.out.println();
				rowItem = new ArrayList<>(); 
				writer.write("\n");
			//	writer1.write("\n");
				
			}			
	
			
			writer.close();
		//	writer1.close();
			csvReader.close();

		} catch (IOException e) {
			System.out.println("exception in readFuzzyCSVAll()");
			e.printStackTrace();
		}

		
	}

	//*********************************************	

	

}

