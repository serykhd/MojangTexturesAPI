package ru.serykhd.mojang.profile;

import ru.serykhd.mojang.profile.impl.property.Property;

public interface IProfileTextures extends IProfileUUID {

    String TEXTURES = "textures";

    Property getTextures();
}
