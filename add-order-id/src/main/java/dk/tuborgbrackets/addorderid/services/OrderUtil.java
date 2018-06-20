package dk.tuborgbrackets.addorderid.services;

import java.util.UUID;

public class OrderUtil {
	
	public static String generateId( ) {
		 return UUID.randomUUID().toString();
	}

}
