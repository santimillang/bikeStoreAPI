package es.udc.ws.bikes.client.ui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.udc.ws.bikes.client.service.ClientBikeService;
import es.udc.ws.bikes.client.service.ClientBikeServiceFactory;
import es.udc.ws.bikes.client.service.dto.ClientBikeDto;
import es.udc.ws.bikes.client.service.dto.ClientRentDto;
import es.udc.ws.bikes.client.service.exceptions.ClientAlreadyRatedException;
import es.udc.ws.bikes.client.service.exceptions.ClientMoreThanFifteenDaysException;
import es.udc.ws.bikes.client.service.exceptions.ClientNotEnoughUnitsException;
import es.udc.ws.bikes.client.service.exceptions.ClientOutOfDateException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class UserServiceClient {

	static final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

	public static void main(String[] args) {
		if (args.length == 0) {
			printUsageAndExit();
		}

		ClientBikeService clientBikeService = ClientBikeServiceFactory.getService();

		if ("-r".equalsIgnoreCase(args[0])) {
			validateArgs(args, 7, new int[] { 1, 4 });

			// UserClient -r <bikeId> <email> <nTarjeta> <nUnits> <initialDate>
			// <expirationDate>

			try {
				Long rentalId = clientBikeService.rentBike(Long.valueOf(args[1]), args[2], args[3],
						Long.valueOf(args[4]), stringToCalendar(args[5]), stringToCalendar(args[6]));

				System.out.println("Rent " + rentalId + " created sucessfully");

			} catch (NumberFormatException | InputValidationException | InstanceNotFoundException
					| ClientNotEnoughUnitsException | ClientOutOfDateException
					| ClientMoreThanFifteenDaysException ex) {
				ex.printStackTrace(System.err);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		} else if ("-fr".equalsIgnoreCase(args[0])) {
			validateArgs(args, 2, new int[] {});
			// UserClient -findReservations <email>

			try {
				List<ClientRentDto> rents = clientBikeService.findRents(args[1]);

				System.out.println("Found " + rents.size() + " rent(s) with email '" + args[1] + "'");
				for (int i = 0; i < rents.size(); i++) {
					ClientRentDto rentDto = rents.get(i);
					System.out.println("Id: " + rentDto.getRentalId() + " with duration: " + rentDto.getDuration()
							+ " and with a " + rentDto.getPunctuation() + " score.");
				}
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		} else if ("-fb".equalsIgnoreCase(args[0])) {
			validateArgs(args, 3, new int[] {});

			// UserClient -fb <keywords> <date>
			try {
				List<ClientBikeDto> bikes = clientBikeService.findBikes(args[1], stringToCalendar(args[2]));
				System.out.println("Found " + bikes.size() + " bike(s) with keywords '" + args[1] + "'");
				for (int i = 0; i < bikes.size(); i++) {
					ClientBikeDto bikeDto = bikes.get(i);
					System.out.println("Id: " + bikeDto.getBikeId() + " ha recibido " + bikeDto.getReviewNumber()
							+ " reviews, y tiene una nota media de " + bikeDto.getAverage());
				}
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		} else if ("-p".equalsIgnoreCase(args[0])) {
			validateArgs(args, 4, new int[] { 1, 2 });

			// UserClient -p <rentalId> <punctuation> <email>

			try {

				clientBikeService.punctuate(Long.valueOf(args[1]), Float.valueOf(args[2]), args[3]);

				System.out.println("Rent " + args[1] + " punctuated correctly");
			} catch (NumberFormatException | InputValidationException | ClientAlreadyRatedException ex) {
				ex.printStackTrace(System.err);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		}

	}

	public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {
		if (expectedArgs != args.length) {
			printUsageAndExit();
		}
		for (int i = 0; i < numericArguments.length; i++) {
			int position = numericArguments[i];
			try {
				Double.parseDouble(args[position]);
			} catch (NumberFormatException n) {
				printUsageAndExit();
			}
		}
	}

	public static Calendar stringToCalendar(String fecha) throws ParseException {

		Calendar returnDate = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		Date date = sdf.parse(fecha);
		returnDate.setTime(date);

		return returnDate;
	}

	public static void printUsageAndExit() {
		printUsage();
		System.exit(-1);
	}

	public static void printUsage() {
		System.err.println("Usage:\n"
				+ "    [reserve] 		    UserClient -r <bikeId> <email> <nTarjeta> <nUnits> <initialDate> <expirationDate>\n"
				+ "    [findReservations]   UserClient -fr <email>\n"
				+ "    [findModels]   	    UserClient -fb <keywords> <date>\n"
				+ "    [punctuateReservation] UserClient -p <rentalId> <punctuation> <email>\n");
	}

}
