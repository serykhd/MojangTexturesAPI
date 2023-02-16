package ru.serykhd.mojang.profile.impl.skin;

import ru.serykhd.mojang.profile.IProfileTextures;
import ru.serykhd.mojang.profile.impl.property.Property;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString(exclude = {"textures"})
@RequiredArgsConstructor
public class Skin implements IProfileTextures {

    private final String name;
    private final String id;
    private final Property textures;
}
