package eu.liveandgov.wp1.backend;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import eu.liveandgov.wp1.backend.format.Parser;
import eu.liveandgov.wp1.backend.format.Sample;

/**
 * Servlet implementation class UploadServlet2
 */
@WebServlet("/upload")
@MultipartConfig
public class UploadServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadServlet2() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Part uploadedFile = null;
		try {
			uploadedFile = request.getPart("upfile");
		} catch (ServletException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		if (uploadedFile == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		InputStream uploadedFileInputStream = uploadedFile.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		copyStream(uploadedFileInputStream, baos);
		
//		saveToDisk(new ByteArrayInputStream(baos.toByteArray()), request.getHeader("id"));
		saveToDatabase(new ByteArrayInputStream(baos.toByteArray()));
		
		
	}
	
	private void saveToDisk(InputStream input, String id) throws IOException {
		Date date = new Date();
		DateFormat dateformat = DateFormat.getDateInstance(DateFormat.MEDIUM);
		DateFormat timeformat = DateFormat.getTimeInstance(DateFormat.MEDIUM);
		String datetimestring = dateformat.format(date) + "_"
				+ timeformat.format(date);
		String absoluteFilename = "/tmp/liveandgov/uploads/" + id + "-" + datetimestring;
		File outfile = new File(absoluteFilename);
		OutputStream outstream = new FileOutputStream(outfile);
		copyStream(input, outstream);
		outstream.flush();
		outstream.close();
	}
	
	private void saveToDatabase(InputStream input) throws IOException {
		List<Sample> samples = Parser.parse(new InputStreamReader(input));
		
		for (Sample sample : samples) {
			switch (sample.getType()) {
			case ACC:
//				insertAcc(sample.getTimestamp(), sample.getId(), Parser.getAccValues(sample.getValue()));
				break;
			case BLT:
				break;
			case GPS:
				break;
			case GSM:
				break;
			case GYR:
				break;
			case MAG:
				break;
			case TAG:
				break;
			case WIFI:
				break;
			default:
				break;
			}
		}
		
	}
	
	private void copyStream(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}

}
