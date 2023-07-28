//package com.swayaan.nysf.APIs;
//
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.Charset;
//
//import org.springframework.http.MediaType;
//import org.springframework.util.StreamUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/v1/championship/kheloindia")
//public class KheloIndiaController {
//
//	
//	@GetMapping(value = "/KISchedule", produces = MediaType.APPLICATION_JSON_VALUE)
//	public String getKISchedule() {
//		String json = "";
//		try (InputStream stream = getClass().getResourceAsStream("/schedule.json")) {
//			json = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
//		} catch (IOException ioe) {
//			System.out.println("Couldn't fetch JSON! Error: " + ioe.getMessage());
//		}
//		return json;
//	}
//
//	@GetMapping(value = "/livescores", produces = MediaType.APPLICATION_JSON_VALUE)
//	public String getLiveScores() {
//		String json = "";
//		try (InputStream stream = getClass().getResourceAsStream("/livescores.json")) {
//			json = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
//		} catch (IOException ioe) {
//			System.out.println("Couldn't fetch JSON! Error: " + ioe.getMessage());
//		}
//		return json;
//	}
//
//	@GetMapping(value = "/winners", produces = MediaType.APPLICATION_JSON_VALUE)
//	public String getWinners() {
//		String json = "";
//		try (InputStream stream = getClass().getResourceAsStream("/winners.json")) {
//			json = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
//		} catch (IOException ioe) {
//			System.out.println("Couldn't fetch JSON! Error: " + ioe.getMessage());
//		}
//		return json;
//	}
//
//}
