package es.udc.ws.bikes.client.ui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.udc.ws.bikes.client.service.ClientBikeService;
import es.udc.ws.bikes.client.service.ClientBikeServiceFactory;
import es.udc.ws.bikes.client.service.dto.ClientBikeDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class AdminServiceClient {

	static final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

	public static void main(String[] args) {
		if (args.length == 0) {
			printUsageAndExit();
		}

		ClientBikeService clientBikeService = ClientBikeServiceFactory.getService();

		if ("-a".equalsIgnoreCase(args[0])) {
			validateArgs(args, 6, new int[] { 4, 5 });

			// [addModel] AdminServiceClient -a <model> <description> <availableSince>
			// <price> <nUnits>

			try {
				Calendar firstDate = stringToCalendar(args[3]);

				Long bikeId = clientBikeService.addBike(
						new ClientBikeDto(args[1], args[2], firstDate, Float.valueOf(args[4]), Long.valueOf(args[5])));

				System.out.println("Bike " + bikeId + " created sucessfully");
			} catch (NumberFormatException | InputValidationException ex) {
				ex.printStackTrace(System.err);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		} else if ("-f".equalsIgnoreCase(args[0])) {
			validateArgs(args, 2, new int[] { 1 });

			// [findBike] AdminServiceClient -findBike <bikeId>

			try {
				ClientBikeDto bike = clientBikeService.findBike(Long.parseLong(args[1]));

				System.out.println("bikeId=" + bike.getBikeId() + ", model=" + bike.getModel() + ", descripcion="
						+ bike.getDescription() + ", unidades=" + bike.getUnits() + ", disponible desde="
						+ df.format(bike.getFirstDate().getTime()) + ", precio=" + bike.getPrice() + ", score="
						+ bike.getAverage() + " de un total de=" + bike.getReviewNumber() + " reseñas" + "]");

			} catch (NumberFormatException | InstanceNotFoundException ex) {
				ex.printStackTrace(System.err);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		} else if ("-u".equalsIgnoreCase(args[0])) {
			validateArgs(args, 7, new int[] { 1, 5, 6 });
			// [upadteModel] AdminServiceClient -u <bikeId> <model> <description>
			// <availableSince> <price> <nUnits>

			try {
				clientBikeService.updateBike(
						new ClientBikeDto(Long.parseLong(args[1]), args[2], args[3], stringToCalendar(args[4]),
								Float.parseFloat(args[5]), Long.parseLong(args[6])));

				System.out.println("Bike " + args[1] + " updated sucessfully");

			} catch (NumberFormatException | InputValidationException | InstanceNotFoundException ex) {
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
		System.err.println("Usage: (dates of the form: 'dd-MM-yyyy')\n"
				+ "    [addModel]    		AdminServiceClient -a <model> <description> <availableSince> <price> <nUnits>\n"
				+ "    [findModel]   		AdminServiceClient -f <bikeId>\n"
				+ "    [upadteModel]    	AdminServiceClient -u <bikeId> <model> <description> <availableSince> <price> <nUnits>\n");
		;
	}

}
