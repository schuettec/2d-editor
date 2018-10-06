package de.schuette.cobra2D.workbench.gui.mapEditor;

public class MapEditorSettings {
	private boolean focusEntityOnListSelect;
	private boolean askBeforeRemoval;

	public MapEditorSettings() {
		focusEntityOnListSelect = true;
		askBeforeRemoval = true;
	}

	public boolean isFocusEntityOnListSelect() {
		return focusEntityOnListSelect;
	}

	public void setFocusEntityOnListSelect(boolean focusEntityOnListSelect) {
		this.focusEntityOnListSelect = focusEntityOnListSelect;
	}

	public boolean isAskBeforeRemoval() {
		return askBeforeRemoval;
	}

	public void setAskBeforeRemoval(boolean askBeforeRemoval) {
		this.askBeforeRemoval = askBeforeRemoval;
	}

}
