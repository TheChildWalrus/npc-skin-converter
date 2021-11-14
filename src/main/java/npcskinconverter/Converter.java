package npcskinconverter;

import static java.util.stream.Collectors.joining;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

public class Converter
{
	private static enum SkinType
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
	
	public static void main(String[] args)
	{
		if (args.length < 1)
		{
			throw new IllegalArgumentException("Not enough args! Need skin type argument - can be one of " + SkinType.getTypesForSuggestion());
		}
		
		String skinTypeName = args[0];
		SkinType skinType = SkinType.forName(skinTypeName);
		
		Path currentDir = Paths.get("");
		Path outputDir;
		try
		{
			outputDir = Files.createDirectories(Paths.get(currentDir.toString(), "output"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException("Exception creating output directory", e);
		}
		
		try (Stream<Path> paths = Files.walk(currentDir, 1))
		{
		    paths.filter(path -> path.toFile().getName().endsWith(".png"))
		    	.forEach(path -> convertSkin(path, skinType, outputDir));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException("Exception visiting files in directory", e);
		}
	}

	private static void convertSkin(Path path, SkinType skinType, Path outputDir)
	{
		try
		{
			BufferedImage srcImg = ImageIO.read(path.toFile());
			if (srcImg.getWidth() != 64 || srcImg.getHeight() != 64)
			{
				throw new IllegalArgumentException(String.format("Expected skin %s to be 64x64, but it was %dx%d!", path, srcImg.getWidth(), srcImg.getHeight()));
			}
			
			BufferedImage outputImg = convertImage(srcImg, skinType);
			
			File outputFile = new File(outputDir.toFile(), path.getFileName().toString());
			if (outputFile.exists())
			{
				throw new IllegalStateException("Output file art " + outputFile + " already exists! Skipping this skin");
			}
			ImageIO.write(outputImg, "png", outputFile);
			System.out.println("Converted file " + path + " and output at " + outputFile.toString());
		}
		catch (Exception e)
		{
			System.out.println("Failed to convert skin at " + path);
			e.printStackTrace();
		}
	}
	
	private static BufferedImage convertImage(BufferedImage srcImg, SkinType skinType)
	{
		BufferedImage outputImg = new BufferedImage(128, 64, BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < 64; y++)
		{
			for (int x = 0; x < 64; x++)
			{
				outputImg.setRGB(x, y, srcImg.getRGB(x, y));
			}
		}
		return outputImg;
	}
}
