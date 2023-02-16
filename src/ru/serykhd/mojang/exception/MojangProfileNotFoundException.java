package ru.serykhd.mojang.exception;


import ru.serykhd.http.exception.HttpIOException;

public class MojangProfileNotFoundException extends HttpIOException {
	
	public MojangProfileNotFoundException(String name) {
		super(String.format("Profile %s does not exist", name));
	}
}
