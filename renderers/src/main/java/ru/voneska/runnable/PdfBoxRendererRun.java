package ru.voneska.runnable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.Data;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

@Data
public class PdfBoxRendererRun implements RendererRunnable {

	private File file;

	private int dpi = 300;

	private boolean saveToFile;

	private boolean print;

	@Override
	public void run() {
		PDDocument document = null;
		try {
			document = PDDocument.load(file);
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			for (int pageCount = 0; pageCount < document.getNumberOfPages(); ++pageCount) {
				BufferedImage image = pdfRenderer.renderImageWithDPI(pageCount, this.dpi, ImageType.GRAY);

				if (saveToFile) {
					ImageIO.write(image, "png", new File(this.getClass().getName() + "_" + pageCount + ".png"));
				}
				if (print) {
					System.out.println(image);
				}
			}
		} catch (IOException e) {
			e.printStackTrace(System.err);
		} finally {
			try {
				if (document != null) {
					document.close();
				}
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		}

	}
}
