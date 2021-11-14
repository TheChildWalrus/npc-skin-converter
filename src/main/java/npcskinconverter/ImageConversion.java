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
		copyUnchanged(0, 0, 32, 16);
		
		// body
		copyUnchanged(16, 16, 24, 16);
		
		// right arm
		copyUnchanged(40, 16, 16, 16);
		
		// right leg
		copyUnchanged(0, 16, 16, 16);
		
		// hair (long for NPCs)
		copyToPosition(0, 32, 32, 32, 56, 0);
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
}
