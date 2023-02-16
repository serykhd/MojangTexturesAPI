package ru.serykhd.mojang.profile;

import ru.serykhd.mojang.profile.impl.property.Property;
import ru.serykhd.mojang.profile.impl.skin.Skin;

import java.util.Collection;

public interface IProfileProperties extends IProfileUUID {

	Collection<Property> getProperties();

	Skin asSkin();
	
}
