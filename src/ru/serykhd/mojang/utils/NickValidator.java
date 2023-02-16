package ru.serykhd.mojang.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

// https://github.com/SpigotMC/BungeeCord/commit/3008d7ef2f50de7e3d38e76717df72dac7fe0da3
@UtilityClass
public final class NickValidator {

    public boolean isChatAllowedCharacter(char character) {
        // Section symbols, control sequences, and deletes are not allowed
        return character != '\u00A7' && character >= ' ' && character != 127;
    }
    
    private boolean isNameAllowedCharacter(char c, boolean onlineMode) {
        if (onlineMode) {
            return ( c >= 'a' && c <= 'z' ) || ( c >= '0' && c <= '9' ) || ( c >= 'A' && c <= 'Z' ) || c == '_' || c == '.' || c == '-';
        }
        
        // Don't allow spaces, Yaml config doesn't support them
        return isChatAllowedCharacter( c ) && c != ' ';
    }
    
    public boolean isValidName(@NonNull String name, boolean onlineMode) {
    	if (name.isBlank() || name.length() > 16 || name.length() < 3) {
    		return false;
    	}
    	
        for ( int index = 0, len = name.length(); index < len; index++ ) {
            if ( !isNameAllowedCharacter( name.charAt( index ), onlineMode ) ) {
                return false;
            }
        }
        return true;
    }
}