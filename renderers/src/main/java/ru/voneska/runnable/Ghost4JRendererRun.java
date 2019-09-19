package ru.voneska.runnable;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import lombok.Data;
import org.ghost4j.document.DocumentException;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.RendererException;
import org.ghost4j.renderer.SimpleRenderer;

@Data
public class Ghost4JRendererRun implements RendererRunnable {
	private File file;

	private int dpi = 300;

	private boolean saveToFile;

	private boolean print;

	@Override
	public void run() {
		PDFDocument document = new PDFDocument();
		try {
			document.load(file);
			SimpleRenderer renderer = new SimpleRenderer();
			renderer.setResolution(dpi);
			// render
			List<Image> images = renderer.render(document);

			for (int pageCount = 0; pageCount < images.size(); pageCount++) {
				if (saveToFile) {
					ImageIO.write((BufferedImage) images.get(pageCount), "png", new File(this.getClass().getName() + "_" + pageCount + ".png"));
				}
				if (print) {
					System.out.println(images.get(pageCount));
				}
			}
		} catch (IOException | DocumentException | RendererException e) {
			e.printStackTrace(System.err);
		}
	}
}
