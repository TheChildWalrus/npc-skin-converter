package npcskinconverter;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class ImageConversion
{
	private final BufferedImage srcImg;
	private final SkinType skinType;
	private final BufferedImage outputImg;
	private boolean doneConversion;
	
	public ImageConversion(BufferedImage srcImg, SkinType skinType)
	{
		this.srcImg = Objects.requireNonNull(srcImg, "source image must not be null");
		this.skinType = Objects.requireNonNull(skinType, "skin type must not be null");
		this.outputImg = new BufferedImage(skinType.renewedFormatWidth, skinType.renewedFormatHeight, BufferedImage.TYPE_INT_ARGB);
	}

	public BufferedImage getConvertedImage()
	{
		if (!doneConversion)
		{
			convert();
			doneConversion = true;
		}
		return outputImg;
	}

	private void convert()
	{
		// head
		// Copy the head piece-by-piece so as to only include the used areas, because the empty top corners can contain other parts (elf/orc ears, chest)
		copyUnchanged(8, 0, 16, 8); // top and bottom faces
		copyUnchanged(0, 8, 32, 8); // front, back, and side faces
		
		// body
		copyUnchanged(16, 16, 24, 16);
		
		// right arm
		copyUnchanged(40, 16, 16, 16);
		
		// right leg
		copyUnchanged(0, 16, 16, 16);
		
		// hair (long for NPCs)
		if (skinType != SkinType.ORC) // Orcs don't have the long hair, as their hair was in the same place as player skins.
		{
			copyToPosition(0, 32, 32, 32, 56, 0);
		}
		else
		{
			copyToPosition(32, 0, 32, 16, 56, 0); // Legacy Orcs had the hair part in the same place as player skins.
			// Renewed orcs' hair part is consistent with other humanoids.
		}
		
		// left leg (copied & mirrored from original)
		copyMirroredToPosition(0, 20, 12, 12, 16, 52); // left, front, right faces. Mirror the front face, but for the left/right both mirror them AND swap their positions
		copyMirroredToPosition(12, 20, 4, 12, 28, 52); // back face
		copyMirroredToPosition(4, 16, 4, 4, 20, 48); // top face
		copyMirroredToPosition(8, 16, 4, 4, 24, 48); // bottom face (shoe / foot)
		
		// left arm (copied & mirrored from original)
		copyMirroredToPosition(40, 20, 12, 12, 32, 52); // left, front, right faces - same treatment as leg
		copyMirroredToPosition(52, 20, 4, 12, 44, 52); // back face
		copyMirroredToPosition(44, 16, 4, 4, 36, 48); // top face (shoulder)
		copyMirroredToPosition(48, 16, 4, 4, 40, 48); // bottom face (hand)
		
		// chest
		if (skinType != SkinType.ORC) // Orcs don't have the chest part, as their hair was in the same place as player skins.
		{
			copyToPosition(24, 0, 16, 5, 32, 8);
		}
		
		if (skinType == SkinType.ELF)
		{
			// right ear
			copyUnchanged(0, 0, 6, 6);
			
			// left ear (copied & mirrored from original)
			copyMirroredToPosition(0, 2, 5, 4, 26, 2); // left, front, right faces - same treatment as arms/legs
			copyMirroredToPosition(5, 2, 1, 4, 31, 2); // back face
			copyMirroredToPosition(2, 0, 1, 2, 28, 0); // top face
			copyMirroredToPosition(3, 0, 1, 2, 29, 0); // bottom face
		}
		
		if (skinType == SkinType.HOBBIT)
		{
			// right foot
			copyToPosition(40, 32, 14, 6, 64, 48);
			
			// left foot (copied & mirrored from original)
			copyMirroredToPosition(40, 35, 10, 3, 78, 51); // left, front, right faces - same treatment as arms/legs
			copyMirroredToPosition(50, 35, 4, 3, 88, 51); // back face
			copyMirroredToPosition(43, 32, 4, 3, 81, 48); // top face
			copyMirroredToPosition(47, 32, 4, 3, 85, 48); // bottom face (sole)
		}
		
		// Nothing special needed for dwarven skins.
		
		if (skinType == SkinType.ORC)
		{
			// right ear
			copyUnchanged(0, 0, 8, 5);
			
			// left ear
			copyUnchanged(24, 0, 8, 5); // Thankfully the orcs already had separate right/left ears in Legacy, so no intricate copying and mirroring needed here!
			
			// nose
			// Orcs also have a nose element below the head at (14, 17) which is 4x3 - but this will have been captured already in copying the body and right arm!
		}
	}
	
	private void copyUnchanged(int srcX, int srcY, int width, int height)
	{
		copyToPosition(srcX, srcY, width, height, srcX, srcY);
	}
	
	private void copyToPosition(int srcX, int srcY, int width, int height, int destX, int destY)
	{
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				outputImg.setRGB(destX + x, destY + y, srcImg.getRGB(srcX + x, srcY + y));
			}
		}
	}
	
	private void copyMirroredToPosition(int srcX, int srcY, int width, int height, int destX, int destY)
	{
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				// Only mirrored in the x dimension!
				outputImg.setRGB(destX + width - x - 1, destY + y, srcImg.getRGB(srcX + x, srcY + y));
			}
		}
	}
}
