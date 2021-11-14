package npcskinconverter;

import static java.util.stream.Collectors.joining;

import java.util.stream.Stream;

public enum SkinType
{
	MAN(64, 64), ELF(64, 64), HOBBIT(64, 64), DWARF(64, 64), ORC(64, 32);
	
	public final int legacyFormatWidth;
	public final int legacyFormatHeight;
	
	// Can make these per-type if necessary in the future.
	public final int renewedFormatWidth = 128;
	public final int renewedFormatHeight = 64;
	
	private SkinType(int w, int h)
	{
		legacyFormatWidth = w;
		legacyFormatHeight = h;
	}
	
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
