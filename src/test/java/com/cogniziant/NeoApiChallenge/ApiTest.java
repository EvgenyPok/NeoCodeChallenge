//Write a program that does the following:
//Call the API to retrieve the list of all near earth objects (neo)
//Display three neo (and their properties) that orbit earth and have the ‘close approach date’ nearest to current date (ignore those which have a ‘close approach date’ in the past). 
//Display the smallest neo that orbits Jupiter and the largest neo that orbits Mercury (based on diameter)
//Write automated unit test cases for the above code
//Provide a readme file with the steps required to setup and run the above code
package com.cogniziant.NeoApiChallenge;

import static io.restassured.RestAssured.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ApiTest {

	public List<HashMap<String,String>> fullNeoList;
	long closestDate = Long.MAX_VALUE;
	float smallestSize = Long.MAX_VALUE;
	float biggestSize = Long.MIN_VALUE;
	String[] closestApproachNeoID = new String[3];
	String neo_id="";
	String smallestJupiterID;
	String largestMercuryID;

	@Test
	public void AgetAllNeos() throws ParseException {

		RestAssured.baseURI = "http://www.neowsapp.com/rest/v1/neo";

		// fetching total number of pages
		JsonPath jp = given().when().get("/browse").jsonPath();
//		int numberOfPages = jp.getInt("page.total_pages");
		int numberOfPages = 20; // just for testing the functionality on small numbers
		
		// retrieving the list of all NEOs
		fullNeoList = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i <= numberOfPages; i++) {
			Response resp = given().queryParam("page", i).when().get("/browse");

			String json = resp.body().asString();

			JsonPath jPath = new JsonPath(json);

			List<HashMap<String, String>> neoList = jPath.get("near_earth_objects");

			for (int j = 0; j < neoList.size(); j++) {
				neo_id = jPath.get("near_earth_objects[" + j + "].id");
				Float minDiamKm = jPath
						.get("near_earth_objects[" + j + "].estimated_diameter.kilometers.estimated_diameter_min");
				if (!((ArrayList) jPath.get("near_earth_objects[" + j + "].close_approach_data.orbiting_body"))
						.isEmpty()) {
					for (int x = 0; x < ((ArrayList) jPath
							.get("near_earth_objects[" + j + "].close_approach_data.orbiting_body")).size(); x++) {
						String orbitingBody = jPath
								.get("near_earth_objects[" + j + "].close_approach_data[" + x + "].orbiting_body");
						String closeApproachDate = jPath.get(
								"near_earth_objects[" + j + "].close_approach_data[" + x + "].close_approach_date");
						buildThreeClosestNeos(orbitingBody, closeApproachDate);
						findSmallestNeoJuptr(orbitingBody, minDiamKm);
						findBiggestNeoMerc(orbitingBody, minDiamKm);
					}
				}
				
			}

			fullNeoList.addAll(neoList);
			System.out.println("Current size of fullNeoList: " + fullNeoList.size() + ". Pages worked " + i + " of "
					+ numberOfPages);

		}
		
		
		
		System.out.println("closestApproachNeo ID's: " + closestApproachNeoID[0] + " " + closestApproachNeoID[1] + " "
				+ closestApproachNeoID[2]);
		System.out.println("smallestJupiterID: " + smallestJupiterID);
		System.out.println("largestMercuryID: " + largestMercuryID);

		Scanner scan = new Scanner(System.in);
		System.out.println("Would you like to print NEO full details by using Neo ID? 'YES' or 'NO'");
		String input = scan.nextLine();
		while (!input.equalsIgnoreCase("NO")) {
			while (!scan.hasNextInt()) scan.next();
			printNeoDetailsByID(input);
			break;
		}
		scan.close();
	}
			

// method fills the String[] with 3 NEO ID's that have orbiting body Earth and their close_approach_date is in nearest future, closest to today's date
	public void buildThreeClosestNeos(String orbit, String date) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		long today = new Date().getTime();
		long approachDate = dateFormat.parse(date).getTime();
		if (orbit.equals("Earth") && approachDate - today > 0 && approachDate - today < closestDate) {
			closestDate = approachDate - today;
			closestApproachNeoID[2] = closestApproachNeoID[1];
			closestApproachNeoID[1] = closestApproachNeoID[0];
			closestApproachNeoID[0] = neo_id;
		}
	}

	
	public void findSmallestNeoJuptr(String orbit, Float diameter) {
		if (orbit.equals("Juptr") && diameter < smallestSize) {
			smallestSize = diameter;
			smallestJupiterID = neo_id;
		}
	}

	
	public void findBiggestNeoMerc(String orbit, Float diameter) {
		if (orbit.equals("Merc") && diameter > biggestSize) {
			biggestSize = diameter;
			largestMercuryID = neo_id;
		}
	}
	
	public void printNeoDetailsByID (String id) {
		given().when().get("/"+id+"").jsonPath().prettyPrint();
	}
}
