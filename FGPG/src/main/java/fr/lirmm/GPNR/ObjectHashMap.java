package fr.lirmm.GPNR;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/////////////object hash map for sythentic graph generator data ////////////
public class ObjectHashMap {

	Map<String, Boolean> Patient = new HashMap<String, Boolean>();
	Map<String, Boolean> Doctor = new HashMap<String, Boolean>();
	Map<String, Boolean> Medicine = new HashMap<String, Boolean>();

	Map<String, Boolean> VISIT = new HashMap<String, Boolean>();
	Map<String, Boolean> ADVISE = new HashMap<String, Boolean>();
	Map<String, Boolean> TAKES = new HashMap<String, Boolean>();

	ObjectHashMap() {

		Patient.put("A1", true);
		Patient.put("A2", true);
		Patient.put("A3", true);
		Patient.put("B1", true);
		Patient.put("B2", true);
		Patient.put("B3", true);
		Patient.put("C1", true);
		Patient.put("C2", true);
		Patient.put("C3", true);

		Doctor.put("A1", true);
		Doctor.put("A2", true);
		Doctor.put("A3", true);
		Doctor.put("B1", true);
		Doctor.put("B2", true);
		Doctor.put("B3", true);
		Doctor.put("C1", true);
		Doctor.put("C2", true);
		Doctor.put("C3", true);

		Medicine.put("A1", true);
		Medicine.put("A2", true);
		Medicine.put("A3", true);
		Medicine.put("B1", true);
		Medicine.put("B2", true);
		Medicine.put("B3", true);
		Medicine.put("C1", true);
		Medicine.put("C2", true);
		Medicine.put("C3", true);

	}

	public List<String> getSortablePropertiesList(String mapName) {

		List<String> propertyList = new ArrayList<String>();

		if (mapName.equalsIgnoreCase("patient")) {
			Set<Entry<String, Boolean>> keySet = Patient.entrySet();
			for (Entry<String, Boolean> val : keySet) {
				if ((boolean) val.getValue()) {
					propertyList.add(val.getKey());
				}
			}
		} else if (mapName.equalsIgnoreCase("doctor")) {
			Set<Entry<String, Boolean>> keySet = Doctor.entrySet();
			for (Entry<String, Boolean> val : keySet) {
				if ((boolean) val.getValue()) {
					propertyList.add(val.getKey());
				}
			}
		}
		if (mapName.equalsIgnoreCase("medicine")) {
			Set<Entry<String, Boolean>> keySet = Medicine.entrySet();
			for (Entry<String, Boolean> val : keySet) {
				if ((boolean) val.getValue()) {
					propertyList.add(val.getKey());
				}
			}
		}
		return propertyList;
	}

	//adding function

	public List<String> getSortableRelPropertiesList(String mapName) {

		List<String> propertyList = new ArrayList<String>();

		if (mapName.equalsIgnoreCase("visit")) {
			Set<Entry<String, Boolean>> keySet = VISIT.entrySet();
			for (Entry<String, Boolean> val : keySet) {
				if ((boolean) val.getValue()) {
					propertyList.add(val.getKey());
				}
			}
		} else if (mapName.equalsIgnoreCase("advise")) {
			Set<Entry<String, Boolean>> keySet = ADVISE.entrySet();
			for (Entry<String, Boolean> val : keySet) {
				if ((boolean) val.getValue()) {
					propertyList.add(val.getKey());
				}
			}
		}
		if (mapName.equalsIgnoreCase("takes")) {
			Set<Entry<String, Boolean>> keySet = TAKES.entrySet();
			for (Entry<String, Boolean> val : keySet) {
				if ((boolean) val.getValue()) {
					propertyList.add(val.getKey());
				}
			}
		}
		return propertyList;
	}

}