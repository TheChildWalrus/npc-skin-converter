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
		this.outputImg = new BufferedImage(128, 64, BufferedImage.TYPE_INT_ARGB);
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
		copyToPosition(0, 32, 32, 32, 56, 0);
		
		// left leg (copied & mirrored from original)
		copyMirroredToPosition(0, 20, 12, 12, 16, 52); // left, front, right faces. Mirror the front face, but for the left/right both mirror them AND swap their positions
		copyMirroredToPosition(12, 20, 4, 12, 28, 52); // back face
		copyMirroredToPosition(4, 16, 4, 4, 20, 48); // top face
		copyMirroredToPosition(8, 16, 4, 4, 24, 48); // bottom face (shoe / foot)
		
		// left arm (copied & mirrored from original)
		copyMirroredToPosition(40, 20, 12, 12, 32, 52); // left, front, right faces
		copyMirroredToPosition(52, 20, 4, 12, 44, 52); // back face
		copyMirroredToPosition(44, 16, 4, 4, 36, 48); // top face (shoulder)
		copyMirroredToPosition(48, 16, 4, 4, 40, 48); // bottom face (hand)
		
		// chest
		copyToPosition(24, 0, 16, 5, 32, 8);
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
				outputImg.setRGB(destX + width - x, destY + y, srcImg.getRGB(srcX + x, srcY + y));
			}
		}
	}
}
