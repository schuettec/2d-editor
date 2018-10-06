package de.schuette.cobra2D.workbench.gui.entityEditor;

public class EntityEditorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected EntityEditorException() {
	}

	protected EntityEditorException(String message) {
		super(message);
	}

	protected EntityEditorException(Throwable cause) {
		super(cause);
	}

	protected EntityEditorException(String message, Throwable cause) {
		super(message, cause);
	}

	protected EntityEditorException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
