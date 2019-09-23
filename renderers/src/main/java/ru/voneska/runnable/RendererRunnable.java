package ru.voneska.runnable;

import java.io.File;

public interface RendererRunnable extends Runnable {

	void setFile(File file);

	File getFile();

	void setDpi(int dpi);

	void setSaveToFile(boolean saveToFile);

	void setPrint(boolean print);

	String getMeta();
}
