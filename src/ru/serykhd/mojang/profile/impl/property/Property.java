package ru.serykhd.mojang.profile.impl.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class Property {

	private final String name;
	private final String value;
	private String signature;
	
	public boolean isSigned() {
		return signature != null;
	}
}
