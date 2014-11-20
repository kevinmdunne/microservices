package com.mini.io.utils;

import java.util.UUID;

public class UUIDGenerator {

	public static String generateID(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
}
