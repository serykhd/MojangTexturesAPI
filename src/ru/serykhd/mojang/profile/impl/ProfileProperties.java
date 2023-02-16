package ru.serykhd.mojang.profile.impl;

import ru.serykhd.mojang.profile.IProfileProperties;
import ru.serykhd.mojang.profile.impl.property.Property;
import ru.serykhd.mojang.profile.impl.skin.Skin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
public class ProfileProperties implements IProfileProperties {
	
	private final String name;
	private final String id;
	private final List<Property> properties;

	public Skin asSkin() {
		return new Skin(name, id, properties.get(0));
	}
}
