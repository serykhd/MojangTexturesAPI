package ru.serykhd.mojang.profile.impl;

import ru.serykhd.mojang.profile.IProfileUUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class ProfileUUID implements IProfileUUID {
	
	private final String name;
	private final String id;
//	private boolean legacy,demo;
	
}
