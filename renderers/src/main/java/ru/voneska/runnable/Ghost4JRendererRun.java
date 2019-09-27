package ru.voneska.runnable;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.ghost4j.document.DocumentException;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.RendererException;
import org.ghost4j.renderer.SimpleRenderer;
import static ru.voneska.Measurer.DELIMITER;

@Data
@Log4j
public class Ghost4JRendererRun implements RendererRunnable {

	private String name = "ghostj4";

	private File file;

	private int dpi = 300;

	private boolean saveToFile;

	private boolean print;

	private String meta;

	@Override
	public void run() {
		PDFDocument document = new PDFDocument();
		try {
			document.load(file);
			SimpleRenderer renderer = new SimpleRenderer();
			renderer.setResolution(dpi);
			// forked jvm rendering processes
//			renderer.setMaxProcessCount(3);
			// render
			List<Image> images = renderer.render(document);
			meta = dpi + DELIMITER + name + DELIMITER + images.size();
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
