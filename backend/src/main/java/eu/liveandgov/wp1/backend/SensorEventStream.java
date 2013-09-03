package eu.liveandgov.wp1.backend;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.print.attribute.standard.Finishings;

import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import eu.liveandgov.wp1.backend.SensorValueObjects.RawSensorValue;
import eu.liveandgov.wp1.backend.format.Sample;
import eu.liveandgov.wp1.backend.format.SampleType;

public class SensorEventStream implements Iterator<RawSensorValue>, Iterable<RawSensorValue> {

	private CsvListReader csv;
	private RawSensorValue next;
	
	public static SensorEventStream processStream(InputStream is, String id){
		SensorEventStream SESO = new SensorEventStream();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		
		SESO.csv = new CsvListReader(reader,CsvPreference.STANDARD_PREFERENCE);
		SESO.next = SESO.yieldNextSV();
		
		return SESO;
	}

	public void dump() {
		while(hasNext()) {
			System.out.println(next().toString());
		}
	}
	
	private RawSensorValue yieldNextSV() {
		// read row
		List<String> row;
		try {
			row = csv.read();
		} catch (IOException e) {
			System.out.println("Cannot read csv-file.");
			e.printStackTrace();
			return null;
		}
		
		if (row == null){
			return null;
		}
		
		if (row.size() <= 3){
			System.out.println("Illegeal row revieved: " + row.toString() + " size " + row.size());
			return null;
		}

		return new RawSensorValue(
				SampleType.valueOf(row.get(0)), // type
				Long.parseLong(row.get(1)), // timestamp
				row.get(2),					// id
				row.get(3)					// value string
		);
	}
	
	@Override
	public boolean hasNext() {
		return !(next == null);
	}

	@Override
	public RawSensorValue next() {
		if (next == null) { throw new NoSuchElementException(); }
		RawSensorValue out = next;
		next = yieldNextSV();
		return out;
	}

	@Override
	public void remove() {
		next = yieldNextSV();
	}

	@Override
	public Iterator iterator() {
		return this;
	}
}
