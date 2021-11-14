package npcskinconverter;

import static java.util.stream.Collectors.joining;

import java.util.stream.Stream;

public enum SkinType
{
	MAN, ELF, HOBBIT, DWARF, ORC;
	
	public static SkinType forName(String name)
	{
		return Stream.of(values()).filter(type -> type.name().equalsIgnoreCase(name)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Unknown skin type " + name + "! Needs to be one of " + getTypesForSuggestion()));
	}
	
	public static String getTypesForSuggestion()
	{
		return Stream.of(values()).map(type -> type.name().toLowerCase()).collect(joining(", "));
	}
}
